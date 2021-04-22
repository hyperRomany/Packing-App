package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.DriverModules.DriverPackages_Details_DB;
import com.example.packingapp.model.DriverModules.DriverPackages_Respones_Details_recycler;
import com.example.packingapp.model.ResponseSms;
import com.example.packingapp.model.ResponseUpdateStatus;

import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailsForDriverViewModel extends ViewModel {



    private MutableLiveData<ResponseSms> smsLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseSms> getSmsLiveData() {
        return smsLiveData;
    }
    public  MutableLiveData<String> mutableLiveData_sendSMS_Error = new MutableLiveData<>();

    public void SendSms(String number, String message) {
       ApiClient.build().sendSms(number,message)
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribe(responseSms -> {
                   smsLiveData.setValue(responseSms);
                       }
               ,throwable -> {
                           Log.d("Error_Vof ",throwable.getMessage());
                           mutableLiveData_sendSMS_Error.setValue(throwable.getMessage());
                       }
               );
    }

    private MutableLiveData<DriverPackages_Respones_Details_recycler> DriverOrderReadyDetailsDataLiveData = new MutableLiveData<>();
    public MutableLiveData<DriverPackages_Respones_Details_recycler> GetDriverOrdersReadyDetailsDataLiveData() {
        return DriverOrderReadyDetailsDataLiveData;
    }

    public  MutableLiveData<String> mutableDetailsLiveDataError = new MutableLiveData<>();

    public void ReadDriverRunsheetOrdersData(String ORDER_NO) {

        HashMap<String, String> map = new HashMap<>();
        map.put("ORDER_NO", ORDER_NO);


        ApiClient.build().ReadDriverTrackingnumbersOfOrders_83(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe( (responseGetOrderData) -> {
                            DriverOrderReadyDetailsDataLiveData.setValue(responseGetOrderData);
                            //  Log.d(TAG, "fetchdata: "+responseGetOrderData);
                        }
                        ,throwable -> {
                            mutableDetailsLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());

                        });
    }

    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus_ON_83 = new MutableLiveData<>();

    public void UpdateStatus_ON_83(String ORDER_NO, String Status,String ModifyedBy) {

        String text=ORDER_NO+"/"+Status+"/"+ModifyedBy ;
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

    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus_RescheduleTime_ON_83 = new MutableLiveData<>();
    public  MutableLiveData<String> mutable_UpdateStatus_RescheduleTime_ON_83LiveDataError = new MutableLiveData<>();

    public void UpdateStatus_RescheduleTime_ON_83(String ORDER_NO, String STATUS , String  RescheduleTime,String ModifyedBy, String  RescheduleReasone ) {

        String text=ORDER_NO +"/"+STATUS +"/"+ RescheduleTime +"/"+ModifyedBy +"/"+ RescheduleReasone ;
        Log.e( "UpdateStatus_Resche", text);
        ApiClient.build().UpdateOrderStatus_RescheduleTime_ON_83(text)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus_RescheduleTime_ON_83.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error_83",throwable.getMessage());
                            mutable_UpdateStatus_RescheduleTime_ON_83LiveDataError.setValue(throwable.getMessage());
                        });
    }
    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus = new MutableLiveData<>();
    public  MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

    public void UpdateStatus(String ORDER_NO, String Status) {
        ORDER_NO =ORDER_NO.replace("*","-");
        HashMap<String, String> map = new HashMap<>();
        map.put("number", ORDER_NO);
        Log.e("TAG", "UpdateStatus:ro "+ORDER_NO );
        map.put("status", Status);
        ApiClient.build().UpdateOrderStatus(
                ORDER_NO,Status,ApiClient.MAgentoToken

        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error_roub",throwable.getMessage());

                        });
/*
        HashMap<String, String> map = new HashMap<>();
        map.put("status", Status);

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
                            mutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Errorroub",throwable.getMessage());

                        });*/

    }
    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus_PASSCODE_ON_83 = new MutableLiveData<>();

    public MutableLiveData<ResponseUpdateStatus> getmutableLiveData_UpdateStatus_PASSCODE_ON_83LiveData() {
        return mutableLiveData_UpdateStatus_PASSCODE_ON_83;
    }
    public void UpdateOrderStatus_Passcode_Header_ON_83(String ORDER_NO, String PASSCODE, String Status,String ModifyedBy) {


        HashMap<String, String> map = new HashMap<>();
        map.put("ORDER_NO", ORDER_NO);
        map.put("PASSCODE", PASSCODE);
        map.put("STATUS", Status);
        map.put("ModifyedBy", ModifyedBy);

        ApiClient.build().UpdateOrderStatus_PASSCODE_ON_83(map)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus_PASSCODE_ON_83.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("ErrorHeader",throwable.getMessage());

                        });

    }

    public MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus_Reason_ON_83 = new MutableLiveData<>();
    public MutableLiveData<ResponseUpdateStatus> getmutableLiveData_UpdateStatus_Reason_ON_83() {
        return mutableLiveData_UpdateStatus_Reason_ON_83;
    }
    public  MutableLiveData<String> mutableLiveDataError_UpdateStatus_Reason_ON_83 = new MutableLiveData<>();

    public void UpdateOrderStatus_Reason_Details_ON_83(List<DriverPackages_Details_DB> driverPackages_details_dbList) {
        HashMap<String, String> map = new HashMap<>();
//        map.put("ORDER_NO", ORDER_NO);
//        map.put("PASSCODE", PASSCODE);
//        map.put("STATUS", Status);

        if (driverPackages_details_dbList.size()>0) {
            for (int i = 0; i < driverPackages_details_dbList.size(); i++)
            {
                String text=driverPackages_details_dbList.get(i).getTRACKING_NO()
                        +"/"+driverPackages_details_dbList.get(i).getREASON()
                        +"/"+driverPackages_details_dbList.get(i).getSTATUS();
                Log.e("TAG", "UpdateOrderStatus_Reason_Details_ON_83: "+text );
                ApiClient.build().UpdateOrderStatus_Reasone_ON_83(text)

                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(responseSms -> {
                                    mutableLiveData_UpdateStatus_Reason_ON_83.setValue(responseSms);

                                }
                                ,throwable -> {
                                    Log.d("ErrorDetails",throwable.getMessage());
                                    mutableLiveDataError_UpdateStatus_Reason_ON_83.setValue(throwable.getMessage());
                                });
            }
        }
    }

}
