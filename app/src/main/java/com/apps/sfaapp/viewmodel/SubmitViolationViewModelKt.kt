package com.apps.sfaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sfaapp.model.SubmitCleanModel
import com.apps.sfaapp.view.repository.SubmitBinCleanRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class SubmitViolationViewModelKt : ViewModel() {
    var submitCleanModelLiveData = MutableLiveData<SubmitCleanModel>()
    var messageShow = MutableLiveData<String>()
    var submitBinCleanRepository: SubmitBinCleanRepository = SubmitBinCleanRepository()
    private lateinit var response: Response<SubmitCleanModel>

    fun saveData(accessKey: String, login_status: String, jawan_id: String, binId: String,
                 ulbid: String, latt: String, lngg: String, scannedCode: String,
                 dateTime: String, cleanStatus: String, zone_id: String,
                 selectedIdsArray: ArrayList<String>, wardId: String, roadId: String,
                 zoneIds: String, circleId: String, img_path: String, catType: String, violationId: String) {


        val accessValue: RequestBody = accessKey.toRequestBody(accessKey.toMediaTypeOrNull())
        val loginStatus: RequestBody = login_status.toRequestBody(login_status.toMediaTypeOrNull())
        val jawanId: RequestBody = jawan_id.toRequestBody(jawan_id.toMediaTypeOrNull())
        val binId: RequestBody = binId.toRequestBody(binId.toMediaTypeOrNull())
        val ulbId: RequestBody = ulbid.toRequestBody(ulbid.toMediaTypeOrNull())
        val latitude: RequestBody = latt.toRequestBody(latt.toMediaTypeOrNull())
        val langg: RequestBody = lngg.toRequestBody(lngg.toMediaTypeOrNull())
        val scannedCode: RequestBody = scannedCode.toRequestBody(scannedCode.toMediaTypeOrNull())
        val dateTime: RequestBody = dateTime.toRequestBody(dateTime.toMediaTypeOrNull())
        val cleanStatus: RequestBody = cleanStatus.toRequestBody(cleanStatus.toMediaTypeOrNull())
        val zoneId: RequestBody = zone_id.toRequestBody(zone_id.toMediaTypeOrNull())
        val wardId: RequestBody = wardId.toRequestBody(wardId.toMediaTypeOrNull())
        val roadId: RequestBody = roadId.toRequestBody(roadId.toMediaTypeOrNull())
        val zoneIds: RequestBody = zoneIds.toRequestBody(zoneIds.toMediaTypeOrNull())
        val circleId: RequestBody = circleId.toRequestBody(circleId.toMediaTypeOrNull())
        val catType: RequestBody = catType.toRequestBody(catType.toMediaTypeOrNull())
        val violationId: RequestBody = violationId.toRequestBody(violationId.toMediaTypeOrNull())
        //val imgPath: RequestBody = img_path.toRequestBody(img_path.toMediaTypeOrNull())

        var photoFile: MultipartBody.Part
        val capturePhoto = File(img_path)


        photoFile = if (capturePhoto.exists()) {
            val requestBody = capturePhoto.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("fileToUpload", capturePhoto.name, requestBody)
        } else {
            val attachmentEmpty = "".toRequestBody("text/plain".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("fileToUpload", "", attachmentEmpty)
        }

       /* var requestBody = img_path.toRequestBody("image/jpeg".toMediaTypeOrNull())
        var filePart = MultipartBody.Part.createFormData("upload_file", file.name, requestBody)*/



        viewModelScope.launch {
            response = submitBinCleanRepository.getData(accessValue, loginStatus, jawanId, binId, ulbId, latitude, langg, scannedCode, dateTime,
                    cleanStatus, zoneId, wardId, roadId, zoneIds, circleId, catType, violationId, photoFile)
            if (response.isSuccessful) {
                submitCleanModelLiveData.value = response.body()
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