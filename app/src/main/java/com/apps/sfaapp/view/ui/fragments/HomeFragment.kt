package com.apps.sfaapp.view.ui.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apps.sfaapp.databinding.FragmentHomeBinding
import com.apps.sfaapp.view.base.*
import com.apps.sfaapp.view.ui.activities.CommercialActivity
import com.apps.sfaapp.view.ui.activities.MainActivity
import com.apps.sfaapp.view.ui.activities.ScannerActivity
import com.apps.sfaapp.view.ui.activities.ToiletActivity
import com.apps.sfaapp.view.ui.viewclass.BinsCustomViewClass
import com.apps.sfaapp.view.ui.viewclass.ToiletCustomViewClass
import com.apps.sfaapp.viewmodel.DashBoardViewModel
import com.apps.sfaapp.viewmodel.ScannerViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.zxing.integration.android.IntentIntegrator


class HomeFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var customViewClass: ToiletCustomViewClass
    private lateinit var binsCustomViewClass: BinsCustomViewClass


    private var screenWidth = 0
    private var screenHeight: Int = 0
    private val CAMERA_PERMISSION_CODE = 101

    private lateinit var dashBoardViewModel: DashBoardViewModel


    //map
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    private val REQUEST_CHECK_SETTINGS = 0x1
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 100
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2


    private lateinit var scannerViewModel: ScannerViewModel


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        dashBoardViewModel = ViewModelProvider(this).get(DashBoardViewModel::class.java)
        scannerViewModel = ViewModelProvider(this).get(ScannerViewModel::class.java)


        //getLastLocation();


        //getLastLocation();
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        mSettingsClient = LocationServices.getSettingsClient(activity as MainActivity)

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        startLocationUpdates()


        binding.dateTxt.text = "Date : $date2"
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        binding.toiletView.layoutParams.height = (screenWidth * 25) / 100
        binding.toiletView.layoutParams.width = (screenWidth * 25) / 100

        binding.binView.layoutParams.height = (screenWidth * 25) / 100
        binding.binView.layoutParams.width = (screenWidth * 25) / 100



        (activity as MainActivity?)!!.titleTextChange("Home")
        (activity as MainActivity?)!!.qrImg("Home")

        (activity as MainActivity?)!!.binding.menuScan.setOnClickListener(this)
        binding.scanLayout.setOnClickListener(this)


        if (isNetworkAvailable) {
            getDashBoardData()
        } else {
            showToastMessage("Please Connect Internet........")
        }

        dashBoardViewModel.dashBoardModelLiveData.observe((activity as MainActivity), Observer {

            if (it.status == 200) {
                setToiletCount(it.ToiletsAssigned, it.ToiletsCleaned, it.Toiletspercent)
                setBinsCount(it.BinsAssigned, it.BinsCleaned, it.Binspercent)
            } else {
                showToastMessage("${it.message}")
            }

            dismissDialog()
        })




        scannerViewModel.scannerLiveData.observe((activity as MainActivity), Observer {
            if (it.status == 200) {
                if (it.cat_type == "Toilet") {

                    startActivity(
                            Intent((activity as MainActivity), ToiletActivity::class.java)
                                    .putExtra("toiletId", it.bin_id)
                                    .putExtra("toiletLocation", it.bin_location)
                                    .putExtra("supervisorName", it.supervisorName)
                                    .putExtra("supervisorMobile", it.supervisorMobile)
                                    .putExtra("scannedCode", it.ScannerCode)
                                    .putExtra("circle", it.circelName)
                                    .putExtra("ward", it.wardName)
                                    .putExtra("road", it.roadName)
                                    .putExtra("ward_id", it.ward_id)
                                    .putExtra("circle_id", it.circle_id)
                                    .putExtra("road_id", it.road_id)
                                    .putExtra("zone_id", it.zone_id)
                    )


                } else {
                    startActivity(
                            Intent((activity as MainActivity), CommercialActivity::class.java)
                                    .putExtra("toiletId", it.bin_id)
                                    .putExtra("toiletLocation", it.bin_location)
                                    .putExtra("supervisorName", it.supervisorName)
                                    .putExtra("supervisorMobile", it.supervisorMobile)
                                    .putExtra("scannedCode", it.ScannerCode)
                                    .putExtra("circle", it.circelName)
                                    .putExtra("ward", it.wardName)
                                    .putExtra("road", it.roadName)
                                    .putExtra("ward_id", it.ward_id)
                                    .putExtra("circle_id", it.circle_id)
                                    .putExtra("road_id", it.road_id)
                                    .putExtra("zone_id", it.zone_id))
                }

                dismissDialog()

            } else {
                showToastMessage("${it.message}")
            }
            dismissDialog()
        })

        scannerViewModel.messageShow.observe((activity as MainActivity), Observer {
            showToastMessage(it)
            dismissDialog()
        })







        return binding.root
    }

    private fun getDashBoardData() {
        showDialogs()
        dashBoardViewModel.getDashBoard(
                Constants.ACCESS_KEY,
                getPreferLogin(SharedPreferConstant.login_status).toString(),
                getPreferLogin(SharedPreferConstant.jawan_id).toString(),
                date.toString()
        )
    }

    private lateinit var binsCustomViewAnimation: BinsCustomViewAnimation
    private fun setBinsCount(binsAssigned: String, binsCleaned: String, binspercent: String) {
        val newFloat: Float = java.lang.String.valueOf(binspercent).toFloat()

        try {
            binsCustomViewClass = BinsCustomViewClass(activity)
            binding.binView.removeAllViews()
            binding.binView.addView(binsCustomViewClass)
        } catch (e: Exception) {
        }

        binsCustomViewAnimation = BinsCustomViewAnimation(
                binsCustomViewClass, newFloat / 100 * 360,
                binsCleaned,
                binsAssigned
        )
        binsCustomViewAnimation.duration = 5000
        binsCustomViewClass.startAnimation(binsCustomViewAnimation)
        binsCustomViewAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private lateinit var toiletCustomViewAnimation: ToiletCustomViewAnimation

    private fun setToiletCount(
            toiletsAssigned: String,
            toiletsCleaned: String,
            toiletspercent: String
    ) {
        val newFloat: Float = java.lang.String.valueOf(toiletspercent).toFloat()

        try {
            customViewClass = ToiletCustomViewClass(activity)
            binding.toiletView.removeAllViews()
            binding.toiletView.addView(customViewClass)
        } catch (e: Exception) {
        }


        toiletCustomViewAnimation = ToiletCustomViewAnimation(
                customViewClass,
                newFloat / 100 * 360,
                toiletsCleaned,
                toiletsAssigned
        )
        toiletCustomViewAnimation.duration = 5000
        customViewClass.startAnimation(toiletCustomViewAnimation)

        toiletCustomViewAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                activity as MainActivity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String?>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // openScanner();
                try {
                    IntentIntegrator(activity as MainActivity?).setCaptureActivity(ScannerActivity::class.java)
                            .initiateScan()
                } catch (e: java.lang.Exception) {
                    showToastMessage(e.message)
                }
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                                    activity as MainActivity,
                                    Manifest.permission.CAMERA
                            )
                            != PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel("You need to allow access permissions") { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermission()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity as MainActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null || result.contents.isEmpty()) {
                Toast.makeText(activity as MainActivity?, "You Closed Scanner", Toast.LENGTH_LONG)
                        .show()
            } else {
                if (isNetworkAvailable) {
                    getScanData(result.contents)
                } else {
                    showToastMessage("Please Connect Internet....")
                }
            }
        } else {
            Toast.makeText(activity as MainActivity?, "You Closed Scanner", Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun getScanData(scan_code: String) {
        val scannerCode: String =
                scan_code.substring(scan_code.lastIndexOf("=") + 1, scan_code.length)
        Log.e("msg", "" + scannerCode)
        if (scannerCode.isEmpty()) {
            showToastMessage("not scanned")
        } else {
            showDialogs()
            scannerViewModel.scanQrData(
                    Constants.ACCESS_KEY,
                    getPreferLogin(SharedPreferConstant.login_status).toString(),
                    scannerCode,
                    getPreferLogin(SharedPreferConstant.jawan_id).toString(),
                    getPreferLogin(SharedPreferConstant.LATTITUDE).toString(),
                    getPreferLogin(SharedPreferConstant.LONGITUDE).toString(),
                    date,
                    getPreferLogin(SharedPreferConstant.user_type).toString(),
                    getPreferLogin(SharedPreferConstant.zone_id).toString()
            )

        }

    }

    override fun onClick(v: View?) {
        if (v == binding.scanLayout || v == (activity as MainActivity).binding.menuScan) {

            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    clearAllPreference()
                    IntentIntegrator(activity as MainActivity?).setCaptureActivity(ScannerActivity::class.java)
                            .initiateScan()
                } else {
                    requestPermission()
                }
            } else {
                clearAllPreference()
                IntentIntegrator(activity as MainActivity?).setCaptureActivity(ScannerActivity::class.java)
                        .initiateScan()
            }
        }
    }


    //location
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                updateLocationUI()
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }


    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            setPreferLogin(SharedPreferConstant.LATTITUDE, mCurrentLocation!!.latitude.toString())
            setPreferLogin(SharedPreferConstant.LONGITUDE, mCurrentLocation!!.longitude.toString())
        }
    }

    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(
                        activity!!,
                        OnSuccessListener { // Log.i(TAG, "All location settings are satisfied.");
                            if (context != null) {
                                if (ContextCompat.checkSelfPermission(
                                                context!!,
                                                Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(
                                                context!!,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    return@OnSuccessListener
                                }
                            }
                            mFusedLocationClient!!.requestLocationUpdates(
                                    mLocationRequest,
                                    mLocationCallback,
                                    Looper.myLooper()
                            )
                            updateUI()
                        })
                .addOnFailureListener(activity!!) { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                               /*  Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");*/try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                        } catch (sie: SendIntentException) {
                            // Log.i(TAG, "PendingIntent unable to execute request.");
                        }

                    }
                    updateUI()
                }
    }

    private fun updateUI() {
        updateLocationUI()
    }

}