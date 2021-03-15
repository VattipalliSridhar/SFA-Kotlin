package com.apps.sfaapp.view.ui.utils;

import com.apps.sfaapp.model.SubmitCleanModel;
import com.apps.sfaapp.view.base.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApis {


    class Factory {
        public static RetrofitApis create() {
            //create logger
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            // default time out is 15 seconds
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(RetrofitApis.class);
        }
    }


    @Multipart
    @POST("saveToiletCleaningCheckDetails.php")
    Call<SubmitCleanModel> getSaveServer(@Part("accesskey") RequestBody accessKey,
                                         @Part("login_status") RequestBody loginStatus,
                                         @Part("jawan_id") RequestBody jawanId,
                                         @Part("bin_id") RequestBody binId,
                                         @Part("ulbid") RequestBody ulbId,
                                         @Part("lat") RequestBody latt,
                                         @Part("lng") RequestBody langs,
                                         @Part("scanCode") RequestBody scanCode,
                                         @Part("datetime") RequestBody dateTime,
                                         @Part("cleanStatus") RequestBody cleanStatus,
                                         @Part("zone_id") RequestBody zoneId,
                                         @Part("ward_id") RequestBody wardId,
                                         @Part("circle_id") RequestBody circleId,
                                         @Part("road_id") RequestBody roadId,
                                         @Part("cat_type") RequestBody catType,
                                         @Part("ViolationId") RequestBody violationId,
                                         @Part MultipartBody.Part photo_file);


}
