package com.apps.sfaapp.view.base

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

open class BaseFragment : Fragment() {
    private var progressDialog: ProgressDialog? = null
    var activeNetworkInfo: NetworkInfo? = null
    private var mcontext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext= context

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val connectivityManager =
            activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        activeNetworkInfo = connectivityManager.activeNetworkInfo
        progressDialog = ProgressDialog(activity)

    }

    val isNetworkAvailable: Boolean
        get() = activeNetworkInfo != null && activeNetworkInfo!!.isConnected

    fun showToastMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun snakeBarView(view: View?, msg: String?) {
        Snackbar.make(view!!, msg!!, Snackbar.LENGTH_SHORT).show()
    }

    fun showDialogs() {
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        if (!progressDialog!!.isShowing) progressDialog!!.show()
    }

    fun dismissDialog() {
        if (progressDialog!!.isShowing) progressDialog!!.dismiss()
    }

    val dateTime: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }
    val date: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }


    val date2: String
        get() {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }

    fun hideKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
   /* fun setPreference(TAG: String?, value: String?) {
        val preferences = context!!.applicationContext.getSharedPreferences(
            SharedPreferConstant.MY_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = preferences.edit()
        editor.putString(TAG, value)
        editor.apply()
    }

    fun getPreference(TAG: String?): String? {
        val preferences = context!!.applicationContext.getSharedPreferences(
            SharedPreferConstant.MY_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return preferences.getString(TAG, "")
    }*/

    fun setPreferLogin(TAG: String?, value: String?) {
        val preferences = mcontext?.getSharedPreferences(
            SharedPreferConstant.LOGIN_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = preferences?.edit()
        editor?.putString(TAG, value)
        editor?.apply()
    }

    fun getPreferLogin(TAG: String?): String? {
        val preferences = mcontext?.getSharedPreferences(
            SharedPreferConstant.LOGIN_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return preferences?.getString(TAG, "")
    }

    fun clearAllPreference() {
        if (context != null) {
            val editor = mcontext?.getSharedPreferences(
                SharedPreferConstant.MY_PREFERENCES,
                Context.MODE_PRIVATE
            )?.edit()
            editor?.clear()
            editor?.apply()
        }
    }

    fun clearLoginPreference() {
        if (context != null) {
            val editor = mcontext?.getSharedPreferences(
                SharedPreferConstant.LOGIN_PREFERENCES,
                Context.MODE_PRIVATE
            )?.edit()
            editor?.clear()
            editor?.apply()
        }
    }
}