package com.apps.sfaapp.view.ui.activities

import android.Manifest.permission
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apps.sfaapp.databinding.ActivityLoginBinding
import com.apps.sfaapp.view.base.BaseActivity
import com.apps.sfaapp.view.base.Constants
import com.apps.sfaapp.view.base.SharedPreferConstant
import com.apps.sfaapp.view.ui.navigators.LoginNavigator
import com.apps.sfaapp.viewmodel.LoginViewModel
import kotlin.system.exitProcess

class LoginActivity : BaseActivity(), LoginNavigator {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionDialog()
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        binding.loginButton.setOnClickListener {
            hideKeyboard()

            if (validation()) {
                if (isNetworkAvailable()) {
                    getLoginData()
                } else {
                    showToastMessage("Please Connect Internet.....")
                }
            }
        }

        loginViewModel.loginLiveData.observe(this, Observer {
            if (it.status == 200) {
                //Toast.makeText(applicationContext, "${it.username}", Toast.LENGTH_LONG).show()
                setPreferLogin(SharedPreferConstant.ulbid, it.ulbid)
                setPreferLogin(SharedPreferConstant.spid, it.spid)
                setPreferLogin(SharedPreferConstant.username, it.username)
                setPreferLogin(SharedPreferConstant.user_id, it.id)
                setPreferLogin(SharedPreferConstant.usermobile, it.usermobile)
                setPreferLogin(SharedPreferConstant.ulbname, it.ulbname)
                setPreferLogin(SharedPreferConstant.user_type, it.user_type)
                setPreferLogin(SharedPreferConstant.login_status, it.login_status.toString())
                setPreferLogin(SharedPreferConstant.login_id, it.login_id)
                setPreferLogin(SharedPreferConstant.jawan_id, it.jawan_id)
                setPreferLogin(SharedPreferConstant.zone_id, it.zone_id)
                setPreferLogin(SharedPreferConstant.session_id, it.session_id)
                setPreferLogin(SharedPreferConstant.LOGIN_SUCCESS, "1")

                openMainActivity()

            } else {
                Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_LONG).show()
            }
            dismissDialog()
        })


        loginViewModel.messageShow.observe(this, Observer {
            showToastMessage(it)
            dismissDialog()
        })
    }

    private fun getLoginData() {
        showDialog()
        loginViewModel.getLogin(binding.etUser.text.toString().trim(), binding.etPassword.text.toString().trim(), Constants.ACCESS_KEY)
    }

    private fun validation(): Boolean {


        if (TextUtils.isEmpty(binding.etUser.text.toString())) {
            showToastMessage("Enter User Name")
            binding.etUser.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
            showToastMessage("Enter Password")
            binding.etPassword.requestFocus()
            return false
        }


        return true
    }

    override fun openMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }


    private val requestPermissionCode = 1

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun permissionDialog() {
        if (
                ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (
                    shouldShowRequestPermissionRationale(permission.ACCESS_FINE_LOCATION) &&
                    shouldShowRequestPermissionRationale(permission.ACCESS_COARSE_LOCATION)) {
            }
            requestPermissions(arrayOf(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION), requestPermissionCode)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == requestPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            } else {
                permissionDialogBox()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun permissionDialogBox() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("App requires permissions to work perfectly..!")
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        builder.setNegativeButton("Exit"
        ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }


    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to exit from the application?")
        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            clearAllPreferences()
            moveTaskToBack(true)
            Process.killProcess(Process.myPid())
            exitProcess(1)
        }
        builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()

    }


}