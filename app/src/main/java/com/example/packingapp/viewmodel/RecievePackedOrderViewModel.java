package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.ResponseSms;
import com.example.packingapp.model.ResponseUpdateStatus;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecievePackedOrderViewModel extends ViewModel {
    private static final String TAG = "RecievePackedOrderViewM";
    private MutableLiveData<RecievePackedModule> OrderDataLiveData = new MutableLiveData<>();
    public MutableLiveData<RecievePackedModule> getOrderDataLiveData() {
        return OrderDataLiveData;
    }

    public  MutableLiveData<String> mutableLiveDataError_fetch = new MutableLiveData<>();

    public void fetchdata(String OrderNumber) {

        HashMap<String, String> map = new HashMap<>();
        map.put("ORDER_NO", OrderNumber);

        ApiClient.build().GetOrderNumberAndNumPackage(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe( (responseGetOrderData) -> {
                            OrderDataLiveData.setValue(responseGetOrderData);
                            //  Log.d(TAG, "fetchdata: "+responseGetOrderData);
                        }
                        ,throwable -> {
                            mutableLiveDataError_fetch.setValue(throwable.getMessage());
                            Log.d("Error_fetchda",throwable.getMessage());
                        });
    }


    private MutableLiveData<RecievePackedModule> OrderDataAndSMSDataLiveData = new MutableLiveData<>();
    public MutableLiveData<RecievePackedModule> getOrderDataAndSMSDataLiveData() {
        return OrderDataAndSMSDataLiveData;
    }

    public  MutableLiveData<String> OrderDataAndSMSDatamutableLiveDataError_fetch = new MutableLiveData<>();

    public void fetchdataAndSMSData(String OrderNumber) {

        HashMap<String, String> map = new HashMap<>();
        map.put("ORDER_NO", OrderNumber);

        ApiClient.build().GetOrderNumberDataAndSMSData(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe( (responseGetOrderData) -> {
                            OrderDataAndSMSDataLiveData.setValue(responseGetOrderData);
                              Log.d(TAG, "fetchdataSMSData: "+responseGetOrderData.getNameArabic());
                        }
                        ,throwable -> {
                            OrderDataAndSMSDatamutableLiveDataError_fetch.setValue(throwable.getMessage());
                            Log.d("Error_fetchdaSMSData",throwable.getMessage());
                        });
    }

    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus = new MutableLiveData<>();
    public MutableLiveData<ResponseUpdateStatus> getmutableLiveData_UpdateStatus() {
        return  mutableLiveData_UpdateStatus;
    }
    public  MutableLiveData<String> mutableLiveDataError_rou = new MutableLiveData<>();

    public void UpdateStatus(String ORDER_NO, String status) {
        HashMap<String, String> map = new HashMap<>();
        map.put("number", ORDER_NO);
        map.put("status", status);
        Log.e(TAG, "UpdateStatus: "+ ORDER_NO);
        ApiClient.build().UpdateOrderStatus(
//                "Bearer 0xqbwza6gbcmupei31qhwex07prjyis6",
                ORDER_NO,status,"Bearer lnv0klr00jkprbugmojf3smj4i5gnn71"

        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error_roub",throwable.getMessage());

                        });

       /* HashMap<String, String> map = new HashMap<>();
        map.put("status", status);

        Log.e(TAG, "UpdateStatus: "+ ORDER_NO);

        ApiClient.buildRo().UpdateOrderStatus(
                "Bearer lnv0klr00jkprbugmojf3smj4i5gnn71",
//                "Bearer 0xqbwza6gbcmupei31qhwex07prjyis6",
                ORDER_NO ,
                map
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error_rou",throwable.getMessage());
                            mutableLiveDataError_rou.setValue(throwable.getMessage());

                        });*/

    }



    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus_ON_83 = new MutableLiveData<>();
    public MutableLiveData<ResponseUpdateStatus> getmutableLiveData_UpdateStatus_ON_83() {
        return mutableLiveData_UpdateStatus_ON_83;
    }

    public void UpdateStatus_ON_83(String ORDER_NO, String Status,String ModifyedBy) {

        String text=ORDER_NO+"/"+Status+"/"+ModifyedBy;
        ApiClient.build().UpdateOrderStatus_ON_83(text)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus_ON_83.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });

    }

    private MutableLiveData<ResponseSms> smsLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseSms> getSmsLiveData() {
        return smsLiveData;
    }
    public MutableLiveData<String> mutableLiveDataError_SendSms = new MutableLiveData<>();
    public MutableLiveData<String> getmutableLiveDataError_SendSms() {
        return mutableLiveDataError_SendSms;
    }

    public void SendSms(String number, String message) {
        ApiClient.build().sendSms(number,message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            smsLiveData.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error_Vof ",throwable.getMessage());
                            mutableLiveDataError_SendSms.setValue(throwable.getMessage());
                        });

    }


}
