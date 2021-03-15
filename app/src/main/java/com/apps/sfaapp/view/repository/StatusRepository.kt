package com.apps.sfaapp.view.repository

import com.apps.sfaapp.model.StatusModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class StatusRepository {


    suspend fun getStatusList(accessKey: String, loginStatus: String, jawanId: String, date: String, userType: String): Response<StatusModel> {


        return withContext(Dispatchers.IO){
            RetrofitClient.apiInterface.statusData(accessKey,loginStatus,jawanId,date,userType)
        }

    }
}