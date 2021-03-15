package com.apps.sfaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sfaapp.model.DashBoardModel
import com.apps.sfaapp.view.repository.DashBoardRepository
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class DashBoardViewModel : ViewModel() {

    var dashBoardModelLiveData = MutableLiveData<DashBoardModel>()
    var messageShow  = MutableLiveData<String>()
    var dashBoardRepository: DashBoardRepository = DashBoardRepository()
    private lateinit var response: Response<DashBoardModel>


    fun getDashBoard(accessKey: String, loginStatus: String, jawanId: String, date: String) {

        viewModelScope.launch {
            response = dashBoardRepository.getDashBoard(accessKey, loginStatus, jawanId,date)
            if(response.isSuccessful)
            {
                dashBoardModelLiveData.value=response.body()
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