package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.ReprintAWBModules.ResponseReprintAWB;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReprintAWBViewModel extends ViewModel {
    private MutableLiveData<ResponseReprintAWB> OrderDataLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseReprintAWB> getOrderDataLiveData() {
        return OrderDataLiveData;
    }

    public  MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

    public void fetchdata(String OrderNumber ,String TRACKING_NO) {

        HashMap<String, String> map = new HashMap<>();
        map.put("ORDER_NO", OrderNumber);
        map.put("TRACKING_NO", TRACKING_NO);

        ApiClient.build().Reprint_AWB(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe( (responseGetOrderData) -> {
                            OrderDataLiveData.setValue(responseGetOrderData);
                            //  Log.d(TAG, "fetchdata: "+responseGetOrderData);
                        }
                        ,throwable -> {
                            mutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());
                        });
    }


}
