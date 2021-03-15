package com.apps.sfaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sfaapp.model.ViolationModel
import com.apps.sfaapp.view.repository.ViolationListRepository
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ViolationViewModel : ViewModel() {

    var violationMutableLiveData = MutableLiveData<ViolationModel>()
    var messageShow = MutableLiveData<String>()
    var violationListRepository: ViolationListRepository = ViolationListRepository()
    private lateinit var response: Response<ViolationModel>

    fun getViolation(accessKey: String, loginStatus: String) {

        viewModelScope.launch {

            response = violationListRepository.getData(accessKey, loginStatus)
            if (response.isSuccessful) {
                violationMutableLiveData.value = response.body()
            } else {
                val error = response.errorBody()?.string()
                val message = StringBuilder()
                error?.let {
                    try {
                        message.append(JSONObject(it).getString("message"))
                    } catch (e: JSONException) {
                    }
                    message.append("\n")
                }
                message.append("Error Code: ${response.code()}")
                messageShow.value = message.toString()
            }
        }


    }


}