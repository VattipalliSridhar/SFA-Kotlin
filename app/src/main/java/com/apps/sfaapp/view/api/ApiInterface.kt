package com.apps.sfaapp.view.api


import com.apps.sfaapp.model.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


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
        @Field("checklist2[]") checkData: ArrayList<String>
    ): Response<SubmitCleanModel>


    @FormUrlEncoded
    @POST("sfaAssignedList.php")
    suspend fun statusData(
        @Field("accesskey") access_key: String,
        @Field("login_status") login_status: String,
        @Field("jawan_id") jawan_id: String,
        @Field("cleandate") dateData: String,
        @Field("user_type") type: String
    ): Response<StatusModel>

}
