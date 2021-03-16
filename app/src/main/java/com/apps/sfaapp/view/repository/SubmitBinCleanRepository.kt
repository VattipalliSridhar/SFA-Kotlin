package com.apps.sfaapp.view.repository

import com.apps.sfaapp.model.SubmitCleanModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SubmitBinCleanRepository {

    suspend fun getData(accessValue: RequestBody, loginStatus: RequestBody, jawanId: RequestBody, binId: RequestBody,
                        ulbId: RequestBody, latitude: RequestBody, langg: RequestBody, scannedCode: RequestBody,
                        dateTime: RequestBody, cleanStatus: RequestBody, zoneId: RequestBody, wardId: RequestBody,
                        roadId: RequestBody, zoneIds: RequestBody, circleId: RequestBody, catType: RequestBody, violationId: RequestBody,
                        imgPath: MultipartBody.Part): Response<SubmitCleanModel> {

        return withContext(Dispatchers.IO) {
            RetrofitClient.apiInterface.saveCleanDataData(accessValue, loginStatus, jawanId, binId, ulbId, latitude, langg, scannedCode,
                    dateTime, cleanStatus, zoneId, wardId, circleId, roadId, catType, violationId,imgPath)
        }

    }
}