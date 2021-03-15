package com.apps.sfaapp.view.ui.activities

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.apps.sfaapp.R
import com.apps.sfaapp.databinding.ActivityCommercialBinding
import com.apps.sfaapp.view.base.BaseActivity
import com.apps.sfaapp.view.base.SharedPreferConstant
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommercialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.photoClickLayout.setOnClickListener(this)
        binding.backButton.setOnClickListener(this)



    }

    override fun onClick(v: View?) {

        if(v == binding.photoClickLayout)
        {
            try {
                val directory: File = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    directoryName
                )
                if (directory.exists()) {
                    deleteFile(directory.toString())
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

        if(v == binding.backButton)
        {
            this.finish()
        }
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

}