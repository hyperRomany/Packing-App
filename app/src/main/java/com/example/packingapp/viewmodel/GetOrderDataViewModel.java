package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.OrderDataModuleDB;
import com.example.packingapp.model.ResponseGetOrderData;
import com.example.packingapp.model.ResponseLogin;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetOrderDataViewModel extends ViewModel {
    private MutableLiveData<OrderDataModuleDB> OrderDataLiveData = new MutableLiveData<>();
    public MutableLiveData<OrderDataModuleDB> getOrderDataLiveData() {
        return OrderDataLiveData;
    }
    public static MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

    public void fetchdata(String OrderNumber) {

        HashMap<String, String> map = new HashMap<>();
        map.put("OrderNumber", OrderNumber);

        ApiClient.buildRo().GetOrderData(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe( (OrderDataModuleDB responseGetOrderData) -> {
                            OrderDataLiveData.setValue(responseGetOrderData);
                        }
                        ,throwable -> {
                            mutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());

                        });
    }
}
