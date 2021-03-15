package com.apps.sfaapp.view.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.sfaapp.R
import com.apps.sfaapp.databinding.FragmentToiletBinding
import com.apps.sfaapp.model.CheckListModel
import com.apps.sfaapp.view.base.BaseFragment
import com.apps.sfaapp.view.base.Constants
import com.apps.sfaapp.view.base.SharedPreferConstant
import com.apps.sfaapp.view.ui.activities.MainActivity
import com.apps.sfaapp.view.ui.adapters.RecyclerCheckAdapter
import com.apps.sfaapp.viewmodel.CheckListViewModel
import com.apps.sfaapp.viewmodel.SubmitCleanViewModel


class ToiletFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener {

    private lateinit var binding: FragmentToiletBinding
    private var toiletLocation: String? = null
    var toiletId: String? = null
    var supervisorName: String? = null
    private var supervisorMobile: String? = null
    private var scannedCode: String? = null
    var circleName: String? = null
    var wardName: String? = null
    var cleanStatus: String? = null

    private var arrayList: ArrayList<CheckListModel.CheckList> =
        ArrayList<CheckListModel.CheckList>()
    private val selectedIdsArray: ArrayList<String> = ArrayList()

    private lateinit var recyclerCheckAdapter: RecyclerCheckAdapter
    private lateinit var checkListViewModel: CheckListViewModel

    private lateinit var submitCleanViewModel: SubmitCleanViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentToiletBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.titleTextChange("Toilet")

        checkListViewModel = ViewModelProvider(this).get(CheckListViewModel::class.java)
        submitCleanViewModel = ViewModelProvider(this).get(SubmitCleanViewModel::class.java)


        toiletLocation = arguments!!.getString("toiletLocation")
        toiletId = arguments!!.getString("toiletId")
        supervisorName = arguments!!.getString("supervisorName")
        supervisorMobile = arguments!!.getString("supervisorMobile")
        scannedCode = arguments!!.getString("scannedCode")
        circleName = arguments!!.getString("circle")
        wardName = arguments!!.getString("ward")

        binding.etToilet.setText(toiletLocation)
        binding.etCircle.setText(circleName)
        binding.etWard.setText(wardName)

        binding.recyclerChecklist.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.recyclerChecklist.setHasFixedSize(true)
       // recyclerCheckAdapter = RecyclerCheckAdapter(activity as MainActivity, arrayList, this)
        //binding.recyclerChecklist.adapter = recyclerCheckAdapter
       // recyclerCheckAdapter.notifyDataSetChanged()


        binding.radioGroup.setOnCheckedChangeListener(this)



        checkListViewModel.checkListViewModelLiveData.observe((activity as MainActivity), Observer {
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

        checkListViewModel.messageShow.observe((activity as MainActivity), Observer {
            showToastMessage(it)
        })



        binding.formSubBut.setOnClickListener {
            if (validation()) {

                if(isNetworkAvailable)
                {
                    //saveCleanData()
                }
            }
        }


        submitCleanViewModel.submitCleanModelLiveData.observe((activity as MainActivity), Observer {
            if (it.status == 200) {

                pop()

            } else {
                showToastMessage("${it.message}")
            }
            dismissDialog()
        })


        return binding.root
    }

    private fun pop() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder((activity as MainActivity))
        alertDialogBuilder.setMessage("Submitted Successfully!")
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
        /*val intent = Intent(this@SuprvisorActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)*/
        //activity?.onBackPressed()

        //(activity as MainActivity).onItemClick()
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.frame_layout, HomeFragment())
            .detach(this)
            .attach(this)
            .commit()


      /*  for (i in 0 until fragmentManager!!.backStackEntryCount) {
            fragmentManager!!.popBackStack()
        }*/

    }

 /*   private fun saveCleanData() {
        showDialogs()
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
    }*/

    private fun validation(): Boolean {
        if (binding.radioYes.isChecked || binding.radioNo.isChecked) {
            if (binding.radioNo.isChecked) {
                if (selectedIdsArray.size == 0) {
                    showToastMessage("Please select check box")
                    return false
                }
            }

        } else {
            showToastMessage("Please select Toilet cleaned")
            return false
        }


        return true

    }

    private fun getCheckList() {
        showDialogs()
        checkListViewModel.getCheckListData(
            Constants.ACCESS_KEY,
            getPreferLogin(SharedPreferConstant.login_status).toString()
        )
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        // This will get the radiobutton that has changed in its check state
        // This will get the radiobutton that has changed in its check state
        val checkedRadioButton = group!!.findViewById(checkedId) as RadioButton
        // This puts the value (true/false) into the variable
        // This puts the value (true/false) into the variable
        val isChecked = checkedRadioButton.isChecked
        // If the radiobutton that has changed in check state is now checked...
        // If the radiobutton that has changed in check state is now checked...
        if (isChecked) {
            if (checkedRadioButton.text.toString().equals("yes", ignoreCase = true)) {
                binding.recyclerChecklist.visibility = View.GONE
                arrayList.clear()
                cleanStatus = "1"
            } else if (checkedRadioButton.text.toString().equals("no", ignoreCase = true)) {
                binding.recyclerChecklist.visibility = View.VISIBLE
                cleanStatus = "2"
                if (isNetworkAvailable) {
                    getCheckList()
                }
            }
        }
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



}