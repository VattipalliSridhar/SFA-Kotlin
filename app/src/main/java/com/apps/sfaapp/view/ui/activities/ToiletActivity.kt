package com.apps.sfaapp.view.ui.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.sfaapp.databinding.ActivityToiletBinding
import com.apps.sfaapp.model.CheckListModel
import com.apps.sfaapp.view.base.BaseActivity
import com.apps.sfaapp.view.base.Constants
import com.apps.sfaapp.view.base.SharedPreferConstant
import com.apps.sfaapp.view.ui.adapters.RecyclerCheckAdapter
import com.apps.sfaapp.viewmodel.CheckListViewModel
import com.apps.sfaapp.viewmodel.SubmitCleanViewModel

class ToiletActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {

    private lateinit var binding: ActivityToiletBinding


    private var toiletLocation: String? = null
    var toiletId: String? = null
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
    var cleanStatus: String? = null

    private var arrayList: ArrayList<CheckListModel.CheckList> = ArrayList()
    private val selectedIdsArray: ArrayList<String> = ArrayList()

    private lateinit var recyclerCheckAdapter: RecyclerCheckAdapter
    private lateinit var checkListViewModel: CheckListViewModel

    private lateinit var submitCleanViewModel: SubmitCleanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToiletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkListViewModel = ViewModelProvider(this).get(CheckListViewModel::class.java)
        submitCleanViewModel = ViewModelProvider(this).get(SubmitCleanViewModel::class.java)

        toiletLocation = intent.getStringExtra("toiletLocation")
        toiletId = intent.getStringExtra("toiletId")
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


        binding.etToilet.setText(toiletLocation)
        binding.etCircle.setText(circleName)
        binding.etWard.setText(wardName)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.recyclerChecklist.layoutManager = LinearLayoutManager(this)
        binding.recyclerChecklist.setHasFixedSize(true)
        recyclerCheckAdapter = RecyclerCheckAdapter(applicationContext, arrayList, this)
        binding.recyclerChecklist.adapter = recyclerCheckAdapter
        recyclerCheckAdapter.notifyDataSetChanged()


        binding.radioGroup.setOnCheckedChangeListener(this)



        checkListViewModel.checkListViewModelLiveData.observe(this, Observer {
            if (it.status == 200) {
                if (it.checkList.size > 0) {
                    arrayList.addAll(it.checkList)
                }
                recyclerCheckAdapter.notifyDataSetChanged()

            } else {
                showToastMessage("${it.message}")
            }
            dismissDialog()
        })

        checkListViewModel.messageShow.observe(this, Observer {
            showToastMessage(it)
        })



        binding.formSubBut.setOnClickListener {
            if (validation()) {

                if (isNetworkAvailable()) {
                    saveCleanData()
                }
            }
        }


        submitCleanViewModel.submitCleanModelLiveData.observe(this, Observer {
            if (it.status == 200) {
                pop(it.bin_name + "\n" + it.message)
            } else {
                showToastMessage("${it.message}")
            }
            dismissDialog()
        })


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
        val intent = Intent(this@ToiletActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }


    private fun saveCleanData() {
        showDialog()
        submitCleanViewModel.saveDataClean(
                Constants.ACCESS_KEY,
                getPreferLogin(SharedPreferConstant.login_status).toString(),
                getPreferLogin(SharedPreferConstant.jawan_id).toString(),
                toiletId.toString(),
                getPreferLogin(
                        SharedPreferConstant.ulbid
                ).toString(),
                getPreferLogin(SharedPreferConstant.LATTITUDE).toString(),
                getPreferLogin(SharedPreferConstant.LONGITUDE).toString(),
                scannedCode.toString(),
                dateTime,
                cleanStatus.toString(),
                getPreferLogin(SharedPreferConstant.zone_id).toString(),
                selectedIdsArray,
                wardId.toString(),
                roadId.toString(),
                zoneId.toString(),
                circleId.toString()
        )
    }

    private fun validation(): Boolean {
        if (binding.radioYes.isChecked || binding.radioNo.isChecked) {
            //if (binding.radioNo.isChecked) {
            if (selectedIdsArray.size == 0) {
                showToastMessage("Please select check box")
                return false
            }
            // }

        } else {
            showToastMessage("Please select Toilet cleaned")
            return false
        }


        return true

    }

    private fun getCheckList() {
        showDialog()
        checkListViewModel.getCheckListData(
                Constants.ACCESS_KEY,
                getPreferLogin(SharedPreferConstant.login_status).toString()
        )
    }

    fun clickData() {
        selectedIdsArray.clear()
        for (i in 0 until arrayList.size) {
            if (arrayList[i].selected) {
                selectedIdsArray.add(arrayList[i].id)
            }
        }
        Log.e("selected_checklist", (selectedIdsArray).toString())
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val checkedRadioButton = group!!.findViewById(checkedId) as RadioButton
        val isChecked = checkedRadioButton.isChecked
        if (isChecked) {
            if (checkedRadioButton.text.toString().equals("yes", ignoreCase = true)) {
                binding.recyclerChecklist.visibility = View.VISIBLE
                arrayList.clear()
                cleanStatus = "1"
                if (isNetworkAvailable()) {
                    getCheckList()
                }
            } else if (checkedRadioButton.text.toString().equals("no", ignoreCase = true)) {
                binding.recyclerChecklist.visibility = View.VISIBLE
                cleanStatus = "2"
                arrayList.clear()
                if (isNetworkAvailable()) {
                    getCheckList()
                }
            }
        }
    }
}