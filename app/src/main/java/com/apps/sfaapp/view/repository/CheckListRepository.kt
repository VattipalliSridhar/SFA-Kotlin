package com.apps.sfaapp.view.repository

import com.apps.sfaapp.model.CheckListModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CheckListRepository {
    suspend fun getCheckList(accessKey: String, loginStatus: String): Response<CheckListModel> {
        return withContext(Dispatchers.IO){
            RetrofitClient.apiInterface.checkList(accessKey,loginStatus)
        }
    }



}