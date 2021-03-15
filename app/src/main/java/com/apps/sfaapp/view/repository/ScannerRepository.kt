package com.apps.sfaapp.view.repository

import com.apps.sfaapp.model.ScannerModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ScannerRepository {
    suspend fun getScanQrData(
        accessKey: String,
        loginStatus: String,
        scanCode: String,
        jawanId: String,
        latt: String,
        lanng: String,
        date: String,
        userType: String,
        zoneId: String
    ): Response<ScannerModel> {


        return withContext(Dispatchers.IO){
            RetrofitClient.apiInterface.scanQrDetails(accessKey,loginStatus,scanCode,jawanId,latt,lanng,date,userType,zoneId)
        }

    }


}