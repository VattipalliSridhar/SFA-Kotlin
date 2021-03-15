package com.apps.sfaapp.view.repository

import com.apps.sfaapp.model.SubmitCleanModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class SubmitCleanRepository {
    suspend fun saveCleanData(
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
    ): Response<SubmitCleanModel> {

        return withContext(Dispatchers.IO) {
            RetrofitClient.apiInterface.saveCleanData(accessKey, loginStatus, jawanId, toiletId, ulbId, latt, lanng, scannedCode, dateTime,
                    cleanStatus, zonId, wardId, circleId, roadId, "1", selectedIdsArray)
        }
    }


}