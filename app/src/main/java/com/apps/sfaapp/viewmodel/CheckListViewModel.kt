package com.apps.sfaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sfaapp.model.CheckListModel
import com.apps.sfaapp.model.DashBoardModel
import com.apps.sfaapp.view.repository.CheckListRepository
import com.apps.sfaapp.view.repository.DashBoardRepository
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class CheckListViewModel : ViewModel() {


    var checkListViewModelLiveData = MutableLiveData<CheckListModel>()
    var messageShow = MutableLiveData<String>()
    var checkListRepository: CheckListRepository = CheckListRepository()
    private lateinit var response: Response<CheckListModel>


    fun getCheckListData(accessKey: String, loginStatus: String) {

        viewModelScope.launch {
            response = checkListRepository.getCheckList(accessKey,loginStatus)
            if(response.isSuccessful)
            {
                checkListViewModelLiveData.value=response.body()
            }else{
                val error = response.errorBody()?.string()
                val message = StringBuilder()
                error?.let{
                    try{
                        message.append(JSONObject(it).getString("message"))
                    }catch(e: JSONException){ }
                    message.append("\n")
                }
                message.append("Error Code: ${response.code()}")
                messageShow.value = message.toString()
            }
        }


    }


}