package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.Message;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewDialog_ReprintAWBViewModel extends ViewModel {
    private MutableLiveData<Message> OrderDataLiveData = new MutableLiveData<>();
    public MutableLiveData<Message> getOrderDataLiveData() {
        return OrderDataLiveData;
    }

    public static MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

    public void Insertdata(String ORDER_NO ,String TRACKING_NO ,String Username) {

        HashMap<String, String> map = new HashMap<>();
        map.put("ORDER_NO", ORDER_NO);
        map.put("TRACKING_NO", TRACKING_NO);
        map.put("Username", Username);

        ApiClient.build().Reprint_AWB_insertlog(map)
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
