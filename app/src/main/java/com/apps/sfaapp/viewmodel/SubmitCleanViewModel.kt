package com.apps.sfaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sfaapp.model.SubmitCleanModel
import com.apps.sfaapp.view.repository.SubmitCleanRepository
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class SubmitCleanViewModel : ViewModel() {

    var submitCleanModelLiveData = MutableLiveData<SubmitCleanModel>()
    var messageShow  = MutableLiveData<String>()
    var submitCleanRepository: SubmitCleanRepository = SubmitCleanRepository()
    private lateinit var response: Response<SubmitCleanModel>

    fun saveDataClean(
            accessKey: String,
            loginStatus: String,
            jawanId: String,
            toiletId: String,
            ulbId: String,
            latt: String,
            lanng: String,
            scannedCode: String,
            dateTime: String,
            cleanStatus: String,
            zonId: String,
            selectedIdsArray: ArrayList<String>,
            wardId: String,
            roadId: String,
            zonIds: String,
            circleId: String
    ) {

        viewModelScope.launch {




            response =  submitCleanRepository.saveCleanData(accessKey, loginStatus, jawanId, toiletId, ulbId, latt, lanng, scannedCode, dateTime, cleanStatus, zonId, selectedIdsArray, wardId, roadId, zonIds, circleId)
            if(response.isSuccessful)
            {
                submitCleanModelLiveData.value=response.body()
            }else{
                val error = response.errorBody()?.string()
                val message = StringBuilder()
                error?.let{
                    try{
                        message.append(JSONObject(it).getString("message"))
                    }catch (e: JSONException){ }
                    message.append("\n")
                }
                message.append("Error Code: ${response.code()}")
                messageShow.value = message.toString()
            }
        }




    }


}