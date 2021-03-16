package com.apps.sfaapp.view.api


import com.apps.sfaapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {


    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
            @Field("username") user: String,
            @Field("password") password: String,
            @Field("accesskey") access_key: String
    ): Response<LoginModel>


    @FormUrlEncoded
    @POST("SfaDashboard.php")
    suspend fun dashBoard(
            @Field("accesskey") access_key: String,
            @Field("login_status") login_status: String,
            @Field("jawan_id") jawan_id: String,
            @Field("cleandate") dateData: String,
    ): Response<DashBoardModel>


    @FormUrlEncoded
    @POST("sfaCheckScanner.php")
    suspend fun scanQrDetails(
            @Field("accesskey") access_key: String,
            @Field("login_status") login_status: String,
            @Field("ScannerCode") scanCode: String,
            @Field("jawan_id") jawnID: String,
            @Field("lat") latt: String,
            @Field("lng") lngg: String,
            @Field("today_date") todayDate: String,
            @Field("user_type") useType: String,
            @Field("zone_id") zone_id: String
    ): Response<ScannerModel>


    @FormUrlEncoded
    @POST("ToiletNotCleanedChecklist.php")
    suspend fun checkList(
            @Field("accesskey") access_key: String,
            @Field("login_status") login_status: String
    ): Response<CheckListModel>


    @FormUrlEncoded
    @POST("saveToiletCleaningCheckDetails.php")
    suspend fun saveCleanData(
            @Field("accesskey") access_key: String,
            @Field("login_status") login_status: String,
            @Field("jawan_id") jawnID: String,
            @Field("bin_id") binId: String,
            @Field("ulbid") ulbid: String,
            @Field("lat") latt: String,
            @Field("lng") lngg: String,
            @Field("scanCode") scanCode: String,
            @Field("datetime") todayDate: String,
            @Field("cleanStatus") clean: String,
            @Field("zone_id") zone_id: String,
            @Field("ward_id") ward_id: String,
            @Field("circle_id") circle_id: String,
            @Field("road_id") road_id: String,
            @Field("cat_type") cat_type: String,
            @Field("checklist2[]") checkData: ArrayList<String>
    ): Response<SubmitCleanModel>

    @Multipart
    @POST("saveToiletCleaningCheckDetails.php")
    suspend fun saveCleanDataData(
            @Part("accesskey") access_key: RequestBody,
            @Part("login_status") login_status: RequestBody,
            @Part("jawan_id") jawnID: RequestBody,
            @Part("bin_id") binId: RequestBody,
            @Part("ulbid") ulbid: RequestBody,
            @Part("lat") latt: RequestBody,
            @Part("lng") lngg: RequestBody,
            @Part("scanCode") scanCode: RequestBody,
            @Part("datetime") todayDate: RequestBody,
            @Part("cleanStatus") clean: RequestBody,
            @Part("zone_id") zone_id: RequestBody,
            @Part("ward_id") ward_id: RequestBody,
            @Part("circle_id") circle_id: RequestBody,
            @Part("road_id") road_id: RequestBody,
            @Part("cat_type") cat_type: RequestBody,
            @Part("ViolationId") violationId: RequestBody,
            @Part fileToUpload: MultipartBody.Part
    ): Response<SubmitCleanModel>


    /* @Multipart
     @POST("insertDetails")
     fun getSaveServer(@Part("ulbid") ulb: RequestBody?,
                       @Part("id") id: RequestBody?,
                       @Part("user_id") user: RequestBody?,
                       @Part("ward_id") ward: RequestBody?,
                       @Part("circle_id") circleId: RequestBody?,
                       @Part("zone_id") zoneId: RequestBody?,
                       @Part("lat") latt: RequestBody?,
                       @Part("lng") langs: RequestBody?,
                       @Part("apk_version") apk_version: RequestBody?,
                       @Part("csrf_token") csrf_token: RequestBody?,
                       @Part("vialationCat_id") cat_Id: RequestBody?,
                       @Part("subDate") date: RequestBody?,
                       @Part photo_file: Part?): Call<InsertModel?>?*/


    @FormUrlEncoded
    @POST("sfaAssignedList.php")
    suspend fun statusData(
            @Field("accesskey") access_key: String,
            @Field("login_status") login_status: String,
            @Field("jawan_id") jawan_id: String,
            @Field("cleandate") dateData: String,
            @Field("user_type") type: String
    ): Response<StatusModel>


    @FormUrlEncoded
    @POST("violations.php")
    suspend fun violationList(
            @Field("accesskey") access_key: String,
            @Field("login_status") login_status: String
    ): Response<ViolationModel>

}
