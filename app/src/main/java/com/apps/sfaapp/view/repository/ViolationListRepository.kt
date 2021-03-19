package com.apps.sfaapp.view.repository

import com.apps.sfaapp.model.ViolationModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ViolationListRepository {
    suspend fun getData(accessKey: String, loginStatus: String): Response<ViolationModel> {
        return withContext(Dispatchers.IO){
            RetrofitClient.apiInterface.violationList(accessKey,loginStatus)
        }

    }


}