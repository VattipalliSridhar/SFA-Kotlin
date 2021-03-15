package com.apps.sfaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sfaapp.model.ScannerModel
import com.apps.sfaapp.view.repository.ScannerRepository
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ScannerViewModel : ViewModel() {
    var scannerRepository: ScannerRepository = ScannerRepository()
    private lateinit var response: Response<ScannerModel>
    var scannerLiveData = MutableLiveData<ScannerModel>()
    var messageShow = MutableLiveData<String>()

    fun scanQrData(
        accessKey: String,
        loginStatus: String,
        scanCode: String,
        jawanId: String,
        latt: String,
        lanng: String,
        date: String,
        userType: String,
        zoneId: String
    ) {


        viewModelScope.launch {
            response = scannerRepository.getScanQrData(
                accessKey,
                loginStatus,
                scanCode,
                jawanId,
                latt,
                lanng,
                date,
                userType,
                zoneId
            )

            if(response.isSuccessful)
            {
                scannerLiveData.value=response.body()
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