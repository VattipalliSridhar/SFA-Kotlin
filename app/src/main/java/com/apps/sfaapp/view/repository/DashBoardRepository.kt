package com.apps.sfaapp.view.repository

import com.apps.sfaapp.model.DashBoardModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class DashBoardRepository {

    suspend fun getDashBoard(accessKey: String, loginStatus: String, jawanId: String, date: String): Response<DashBoardModel> {
        return withContext(Dispatchers.IO){
            RetrofitClient.apiInterface.dashBoard(accessKey,loginStatus,jawanId,date)
        }
    }


}