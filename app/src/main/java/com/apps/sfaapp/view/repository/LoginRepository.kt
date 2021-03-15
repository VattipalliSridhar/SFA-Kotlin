package com.apps.sfaapp.view.repository

import androidx.lifecycle.MutableLiveData
import com.apps.sfaapp.model.LoginModel
import com.apps.sfaapp.view.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {

    suspend fun getLoginData(user: String, pass: String, accessKey: String): Response<LoginModel> {

        return withContext(Dispatchers.IO) {
            RetrofitClient.apiInterface.login(user, pass, accessKey)
        }
    }
}