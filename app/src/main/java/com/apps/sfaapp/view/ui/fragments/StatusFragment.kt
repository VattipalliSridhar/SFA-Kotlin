package com.apps.sfaapp.view.ui.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.sfaapp.databinding.FragmentStatusBinding
import com.apps.sfaapp.model.StatusModel
import com.apps.sfaapp.view.base.BaseFragment
import com.apps.sfaapp.view.base.Constants
import com.apps.sfaapp.view.base.SharedPreferConstant
import com.apps.sfaapp.view.ui.activities.MainActivity
import com.apps.sfaapp.view.ui.adapters.RecyclerStatusAdapter
import com.apps.sfaapp.viewmodel.StatusViewModel
import java.util.*
import kotlin.collections.ArrayList


class StatusFragment : BaseFragment() {


    private lateinit var binding: FragmentStatusBinding
    private lateinit var statusViewModel: StatusViewModel

    private var statusList: ArrayList<StatusModel.Binlist> = ArrayList()

    private lateinit var recyclerStatusAdapter: RecyclerStatusAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)

        statusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)

        (activity as MainActivity?)!!.titleTextChange("Status")
        (activity as MainActivity?)!!.qrImg("Status")

        setDefaultDate()




        binding.statusRecycler.layoutManager = LinearLayoutManager((activity as MainActivity))
        binding.statusRecycler.setHasFixedSize(true)
        recyclerStatusAdapter = RecyclerStatusAdapter((activity as MainActivity), statusList, this)
        binding.statusRecycler.adapter = recyclerStatusAdapter



        binding.imgCalender.setOnClickListener {

            getCleanView()
        }

        statusViewModel.statusModelLiveData.observe((activity as MainActivity), Observer {
            if (it.status == 200) {
                statusList.clear()
                if (it.binlist.size > 0) {
                    statusList.addAll(it.binlist)
                }
                recyclerStatusAdapter.notifyDataSetChanged()

            } else {
                showToastMessage("${it.message}")
            }
            dismissDialog()
        })
        statusViewModel.messageShow.observe((activity as MainActivity), Observer {
            showToastMessage(it)
            dismissDialog()
        })


        return binding.root
    }

    private var picker: DatePickerDialog? = null
    private fun getCleanView() {
        val cldr = Calendar.getInstance()
        val day = cldr[Calendar.DAY_OF_MONTH]
        val month = cldr[Calendar.MONTH]
        val year = cldr[Calendar.YEAR]


        // date picker dialog

        // date picker dialog
        picker = DatePickerDialog(
            (activity as MainActivity),
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                var monthString = (monthOfYear + 1).toString()
                var dayString = dayOfMonth.toString()
                if (dayString.length == 1) {
                    dayString = "0$dayString"
                }
                if (monthString.length == 1) {
                    monthString = "0$monthString"
                }
                binding.txtDatedisplay.text = "$year-$monthString-$dayString"
                if (isNetworkAvailable) {
                    statusList.clear()
                    recyclerStatusAdapter.notifyDataSetChanged()

                    getCleanData(
                        Constants.ACCESS_KEY,
                        getPreferLogin(SharedPreferConstant.login_status).toString(),
                        getPreferLogin(SharedPreferConstant.jawan_id).toString(),
                        "$year-$monthString-$dayString",
                        getPreferLogin(SharedPreferConstant.user_type).toString()
                    )
                } else {
                    showToastMessage("Please check the internet connection")
                }
            }, year, month, day
        )
        picker!!.show()

        picker!!.datePicker.maxDate = System.currentTimeMillis()

    }

    private fun setDefaultDate() {
        binding.txtDatedisplay.text = date.toString()
        if (isNetworkAvailable) {
            getCleanData(
                Constants.ACCESS_KEY, getPreferLogin(SharedPreferConstant.login_status).toString(),
                getPreferLogin(SharedPreferConstant.jawan_id).toString(),
                date.toString(), getPreferLogin(SharedPreferConstant.user_type).toString()
            )
        } else {
            showToastMessage("Please check the internet connection")
        }
    }

    private fun getCleanData(
        accessKey: String,
        loginStatus: String,
        jawanId: String,
        date: String,
        userType: String
    ) {

        showDialogs()
        statusViewModel.getStatusData(accessKey, loginStatus, jawanId, date, userType)

    }


}