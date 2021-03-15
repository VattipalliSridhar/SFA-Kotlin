package com.apps.sfaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sfaapp.model.StatusModel
import com.apps.sfaapp.view.repository.StatusRepository
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class StatusViewModel : ViewModel() {


    var statusRepository : StatusRepository = StatusRepository()
    var statusModelLiveData = MutableLiveData<StatusModel>()
    var messageShow = MutableLiveData<String>()
    private lateinit var response: Response<StatusModel>
    fun getStatusData(
        accessKey: String,
        loginStatus: String,
        jawanId: String,
        date: String,
        userType: String
    ) {
        viewModelScope.launch {
            response = statusRepository.getStatusList(accessKey,loginStatus,jawanId,date,userType)


            if(response.isSuccessful)
            {
                statusModelLiveData.value=response.body()
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