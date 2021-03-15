package com.apps.sfaapp.view.ui.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentServices extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param "FetchAddressIntentServices" Used to name the worker thread, important only for debugging.
     */

    private ResultReceiver resultReceiver;

    public FetchAddressIntentServices() {
        super("FetchAddressIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent!=null)
        {
            String errorMsg = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if(location==null)
            {
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = null;
            try {

                addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            }catch (Exception e)
            {
                errorMsg=e.getMessage();
            }

            if(addressList == null || addressList.isEmpty())
            {
                deliveryResultToReceiver(Constants.FAILURE_RESULT,errorMsg);
            }else{
                Address address = addressList.get(0);
                ArrayList<String> stringArrayList = new ArrayList<>();
                for(int i=0;i<=address.getMaxAddressLineIndex();i++)
                {
                    stringArrayList.add(address.getAddressLine(i));
                }

                deliveryResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),stringArrayList));
            }



        }



    }



    private void deliveryResultToReceiver(int resultCode, String addressMessage)
    {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY,addressMessage);
        resultReceiver.send(resultCode,bundle);
    }
}
