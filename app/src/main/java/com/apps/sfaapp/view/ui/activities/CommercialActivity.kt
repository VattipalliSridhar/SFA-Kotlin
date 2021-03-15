package com.apps.sfaapp.view.ui.activities

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apps.sfaapp.R
import com.apps.sfaapp.databinding.ActivityCommercialBinding
import com.apps.sfaapp.view.base.BaseActivity
import com.apps.sfaapp.view.base.Constants
import com.apps.sfaapp.view.base.SharedPreferConstant
import com.apps.sfaapp.viewmodel.SubmitViolationViewModel
import com.apps.sfaapp.viewmodel.ViolationViewModel
import com.bumptech.glide.Glide
import com.zxy.tiny.Tiny
import com.zxy.tiny.Tiny.FileCompressOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CommercialActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCommercialBinding


    private val cameraRequestCode = 100
    private val directoryName = "SFA"
    private var outFile: File? = null

    private val multiplePermission = 101


    private var binLocation: String? = null
    var binId: String? = null
    var supervisorName: String? = null
    private var supervisorMobile: String? = null
    private var scannedCode: String? = null
    var circleName: String? = null
    var wardName: String? = null
    var roadName: String? = null
    var wardId: String? = null
    var circleId: String? = null
    var roadId: String? = null
    var zoneId: String? = null
    var violationId: String? = null

    private lateinit var violationViewModel: ViolationViewModel
    private lateinit var submitViolationViewModel: SubmitViolationViewModel

    private var violationTypeArray: ArrayList<String> = ArrayList()
    private var violationIdArray: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommercialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        violationViewModel = ViewModelProvider(this).get(ViolationViewModel::class.java)
        submitViolationViewModel = ViewModelProvider(this).get(SubmitViolationViewModel::class.java)

        binding.photoClickLayout.setOnClickListener(this)
        binding.backButton.setOnClickListener(this)



        binLocation = intent.getStringExtra("toiletLocation")
        binId = intent.getStringExtra("toiletId")
        supervisorName = intent.getStringExtra("supervisorName")
        supervisorMobile = intent.getStringExtra("supervisorMobile")
        scannedCode = intent.getStringExtra("scannedCode")
        circleName = intent.getStringExtra("circle")
        wardName = intent.getStringExtra("ward")
        roadName = intent.getStringExtra("road")
        wardId = intent.getStringExtra("ward_id")
        circleId = intent.getStringExtra("circle_id")
        roadId = intent.getStringExtra("road_id")
        zoneId = intent.getStringExtra("zone_id")

        binding.etCircle.setText(circleName)
        binding.etCommercial.setText(binLocation)
        binding.etWard.setText(wardName)
        binding.etRoad.setText(roadName)
        binding.formSubBut.setOnClickListener(this)

        if (isNetworkAvailable()) {
            getViolationData()
        } else {
            showToastMessage("Please Connect internet...............")
        }

        violationViewModel.violationMutableLiveData.observe(this, Observer {
            if (it.status == 200) {
                violationTypeArray.clear()
                violationIdArray.clear()
                if (it.violations.size > 0) {
                    for (i in it.violations.indices) {
                        violationTypeArray.add(it.violations[i].violationName)
                        violationIdArray.add(it.violations[i].violationId)
                    }

                    val categoryAdapter: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, android.R.layout.simple_dropdown_item_1line, violationTypeArray)
                    binding.spinnerViolation.adapter = categoryAdapter
                }
            } else {

            }
            dismissDialog()
        })


        binding.spinnerViolation.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                violationId = violationIdArray[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        submitViolationViewModel.insertModelMutableLiveData.observe(this, Observer {
            if (it.status == 200) {
                pop(it.bin_name + "\n" + it.message)
            } else {
                showToastMessage("${it.message}")
            }
        })

    }

    private fun getViolationData() {
        showDialog()
        violationViewModel.getViolation(Constants.ACCESS_KEY, getPreferLogin(SharedPreferConstant.login_status).toString())
    }

    override fun onClick(v: View?) {

        if (v == binding.photoClickLayout) {
            try {
                val directory: File = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        directoryName
                )
                if (directory.exists()) {
                    deleteFile1(directory)
                }
                if (getPreference(SharedPreferConstant.IMAGE_PATH) != null) {
                    deleteTinyImages(getPreference(SharedPreferConstant.IMAGE_PATH).toString())
                }
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(Runnable { //Do something after 500ms
                    setPreference(SharedPreferConstant.IMAGE_PATH, "")
                    binding.filename.text = "File Name.jpg"
                    binding.imageViewLayout.visibility = View.GONE
                    capturePhoto()
                }, 500)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (v == binding.backButton) {
            setPreference(SharedPreferConstant.IMAGE_PATH, "")
            this.finish()
        }

        if (v == binding.formSubBut) {
            if (validation()) {

                if (isNetworkAvailable()) {
                    saveViolation()
                }

            }
        }
    }

    private fun deleteFile1(f: File) {
        val flist = f.list()
        if (flist != null && flist.isNotEmpty()) {
            for (s in flist) {
                println(" " + f.absolutePath)
                val temp = File(f.absolutePath + "/" + s)
                if (temp.isDirectory) {
                    deleteFile(temp.toString())
                    temp.delete()
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(temp)))
                    Log.d("main_dir_deleted", "" + temp.name)
                } else {
                    temp.delete()
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(temp)))
                    Log.d("main_file_deleted", "" + temp.name)
                }
            }
        } else {
            println("No Files")
        }

    }

    /* fun deleteFile1(f: File) {
         val flist = f.list()
         if (flist != null && flist.size > 0) {
             for (s in flist) {
                 println(" " + f.absolutePath)
                 val temp = File(f.absolutePath + "/" + s)
                 if (temp.isDirectory) {
                     deleteFile(temp.toString())
                     temp.delete()
                     sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(temp)))
                     Log.d("main_dir_deleted", "" + temp.name)
                 } else {
                     temp.delete()
                     sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(temp)))
                     Log.d("main_file_deleted", "" + temp.name)
                 }
             }
         } else {
             println("No Files")
         }
     }*/


    private val selectedIdsArray: ArrayList<String> = ArrayList()
    private fun saveViolation() {
        showDialog()
        submitViolationViewModel.saveData(
                Constants.ACCESS_KEY,
                getPreferLogin(SharedPreferConstant.login_status).toString(),
                getPreferLogin(SharedPreferConstant.jawan_id).toString(),
                binId.toString(),
                getPreferLogin(
                        SharedPreferConstant.ulbid
                ).toString(),
                getPreferLogin(SharedPreferConstant.LATTITUDE).toString(),
                getPreferLogin(SharedPreferConstant.LONGITUDE).toString(),
                scannedCode.toString(),
                dateTime,
                "",
                getPreferLogin(SharedPreferConstant.zone_id).toString(),
                selectedIdsArray,
                wardId.toString(),
                roadId.toString(),
                zoneId.toString(),
                circleId.toString(),
                getPreference(SharedPreferConstant.IMAGE_PATH).toString(),
                "2",
                violationId.toString()
        )
    }

    private fun validation(): Boolean {


        if (binding.spinnerViolation.selectedItemPosition == 0) {
            showToastMessage("Please Select Violation Type")
            return false
        }

        if (!validatePhoto()) {
            return false
        }

        return true
    }

    private fun validatePhoto(): Boolean {
        if (getPreference(SharedPreferConstant.IMAGE_PATH)!!.isNotEmpty()) {
            return true
        }
        snakeBarView(binding.imageViewLayout, "Please Capture  Image")
        return false
    }


    private fun capturePhoto() {
        if (ActivityCompat.checkSelfPermission(
                        this@CommercialActivity,
                        Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@CommercialActivity,
                        WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@CommercialActivity,
                        READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isDeviceSupportCamera()) {
                val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (camIntent.resolveActivity(packageManager) != null) {
                    var photoFile: File? = null
                    try {
                        photoFile = getOutputMediaFile()
                    } catch (ex2: java.lang.Exception) {
                        Log.d("Error", ex2.message.toString())
                    }
                    if (photoFile != null) {
                        val outputUri: Uri = FileProvider.getUriForFile(
                                this@CommercialActivity,
                                "$packageName.provider", photoFile
                        )
                        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
                        camIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivityForResult(camIntent, cameraRequestCode)
                    }
                }
                //File file = getOutputMediaFile();
            } else Toast.makeText(
                    this@CommercialActivity,
                    "Device Not supports Camera",
                    Toast.LENGTH_LONG
            ).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        arrayOf(
                                Manifest.permission.CAMERA,
                                READ_EXTERNAL_STORAGE,
                                WRITE_EXTERNAL_STORAGE
                        ),
                        multiplePermission
                )
            }
        }
    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    private fun isDeviceSupportCamera(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun getOutputMediaFile(): File? {
        val directory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                directoryName
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val timestamp: String =
                SimpleDateFormat("ddMMMyyyy_HHmmss", Locale.getDefault()).format(Date())
        outFile =
                File(directory.toString() + File.separator + getPreferLogin(SharedPreferConstant.username) + "_" + timestamp + ".jpg")
        return outFile
    }

    private fun deleteTinyImages(tinyImage: String) {
        val target = File(tinyImage)
        Log.d("tiny_path", "" + tinyImage)
        if (target.exists() && target.isFile && target.canWrite()) {
            target.delete()
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(target)))
            Log.d("tiny_deleted", "" + target.name)
        }
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String?>,
            grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == multiplePermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // put your function here
                capturePhoto()
            } else {
                Toast.makeText(
                        this,
                        "Please allow the permission to utilize this feature.",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === cameraRequestCode) {
            if (resultCode === RESULT_OK) {
                val options = FileCompressOptions()
                Tiny.getInstance().source(outFile!!.absolutePath).asFile().withOptions(options)
                        .compress { isSuccess, outfile, t -> //return the compressed file path
                            setPhoto(outfile)
                            setPreference(SharedPreferConstant.IMAGE_PATH, outfile)
                        }
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outFile)))
            } else {
                Log.e("canceled", "camrea")
            }
        }

    }


    private fun setPhoto(absolutePath: String) {
        binding.imageViewLayout.visibility = View.VISIBLE
        Glide.with(this).load(Uri.fromFile(File(absolutePath)))
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24).into(binding.imgView)
        val filename = absolutePath.substring(absolutePath.lastIndexOf("/") + 1)
        binding.filename.text = "" + filename
    }


    private fun pop(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { arg0, arg1 ->
                    arg0.dismiss()
                    resetForm()
                })

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun resetForm() {
        viewModelStore.clear()
        val intent = Intent(this@CommercialActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }
}


