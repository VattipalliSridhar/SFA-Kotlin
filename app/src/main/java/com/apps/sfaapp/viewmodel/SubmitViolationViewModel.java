package com.apps.sfaapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apps.sfaapp.model.SubmitCleanModel;
import com.apps.sfaapp.view.api.RetrofitClient;
import com.apps.sfaapp.view.ui.utils.RetrofitApis;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitViolationViewModel extends ViewModel {

    private MutableLiveData<SubmitCleanModel> insertModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> messageToShow = new MutableLiveData<>();
    private RetrofitApis retrofitApis = RetrofitApis.Factory.create();

    public void saveData(String accessKey,
                         String login_status, String jawan_id, String binId, String ulbid,
                         String latt, String lngg, String scannedCode, String dateTime,
                         String cleanStatus, String zone_id, ArrayList<String> selectedIdsArray, String wardId,
                         String roadId, String zoneId, String circleId, String img_path,
                         String catType, String violationId) {
        MultipartBody.Part photo_file = null;
        String paths = img_path;
        File split_photo = new File(paths);
        if (split_photo.exists()) {

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), split_photo);
            photo_file = MultipartBody.Part.createFormData("fileToUpload", split_photo.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            photo_file = MultipartBody.Part.createFormData("fileToUpload", "", attachmentEmpty);
        }

        RequestBody access_Key = RequestBody.create(MediaType.parse("multipart/form-data"), accessKey);
        RequestBody loginStatus = RequestBody.create(MediaType.parse("multipart/form-data"), login_status);
        RequestBody jawanId = RequestBody.create(MediaType.parse("multipart/form-data"), jawan_id);
        RequestBody bin_Id = RequestBody.create(MediaType.parse("multipart/form-data"), binId);
        RequestBody ulb_id = RequestBody.create(MediaType.parse("multipart/form-data"), ulbid);
        RequestBody latts = RequestBody.create(MediaType.parse("multipart/form-data"), latt);
        RequestBody lnggs = RequestBody.create(MediaType.parse("multipart/form-data"), lngg);
        RequestBody scanned_Code = RequestBody.create(MediaType.parse("multipart/form-data"), scannedCode);
        RequestBody date_Time = RequestBody.create(MediaType.parse("multipart/form-data"), dateTime);
        RequestBody clean_Status = RequestBody.create(MediaType.parse("multipart/form-data"), cleanStatus);
        RequestBody zoneIdd = RequestBody.create(MediaType.parse("multipart/form-data"), zone_id);
        RequestBody ward_Id = RequestBody.create(MediaType.parse("multipart/form-data"), wardId);
        RequestBody road_Id = RequestBody.create(MediaType.parse("multipart/form-data"), roadId);
        RequestBody zoneIds = RequestBody.create(MediaType.parse("multipart/form-data"), zoneId);
        RequestBody circle_Id = RequestBody.create(MediaType.parse("multipart/form-data"), circleId);
        RequestBody cat_Type = RequestBody.create(MediaType.parse("multipart/form-data"), catType);
        RequestBody violation_Id = RequestBody.create(MediaType.parse("multipart/form-data"), violationId);


        Call<SubmitCleanModel> submitCleanModelCall = retrofitApis.getSaveServer(access_Key, loginStatus, jawanId, bin_Id, ulb_id, latts, lnggs, scanned_Code, date_Time, clean_Status, zoneIdd, ward_Id,
                circle_Id, road_Id, cat_Type, violation_Id, photo_file);
        submitCleanModelCall.enqueue(new Callback<SubmitCleanModel>() {
            @Override
            public void onResponse(Call<SubmitCleanModel> call, Response<SubmitCleanModel> response) {
                if (response.isSuccessful()) {

                    SubmitCleanModel insertModel = response.body();
                    if (insertModel != null && insertModel.getStatus() == 200) {
                        insertModelMutableLiveData.setValue(insertModel);
                    } else {
                        messageToShow.setValue(insertModel.getMessage());
                    }


                } else {
                    String errorCode;
                    switch (response.code()) {
                        case 400:
                            errorCode = "404! not found";
                            messageToShow.setValue(errorCode);
                            break;
                        case 500:
                            errorCode = "500! server broken";
                            messageToShow.setValue(errorCode);
                            break;
                        default:
                            // errorCode = "Unknown error";
                            messageToShow.setValue(response.message());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SubmitCleanModel> call, Throwable t) {

            }
        });


    }

    public MutableLiveData<SubmitCleanModel> getInsertModelMutableLiveData() {
        if (insertModelMutableLiveData == null)
            insertModelMutableLiveData = new MutableLiveData<>();
        return insertModelMutableLiveData;
    }

    public MutableLiveData<String> getMessageToShow() {
        if (messageToShow == null)
            messageToShow.setValue("");
        return messageToShow;
    }
}
