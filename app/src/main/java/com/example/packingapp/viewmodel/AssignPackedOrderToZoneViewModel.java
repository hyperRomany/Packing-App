package com.example.packingapp.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.RecievePacked.ResponseFetchRuntimesheetID;
import com.example.packingapp.model.ResponseDriver;
import com.example.packingapp.model.ResponseSms;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.model.TimeSheet.Response;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AssignPackedOrderToZoneViewModel extends ViewModel {
    private MutableLiveData<RecievePackedModule> OrderDataLiveData = new MutableLiveData<>();
    public MutableLiveData<RecievePackedModule> getOrderDataLiveData() {
        return OrderDataLiveData;
    }

    public static MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

    public void fetchdata(String OrderNumber ) {

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
                            mutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());
                        });
    }

    public static MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus = new MutableLiveData<>();

    public void UpdateStatus(String ORDER_NO, String Status) {


        HashMap<String, String> map = new HashMap<>();
        map.put("status", Status);

        ApiClient.buildRo().UpdateOrderStatus(
               // "Bearer lnv0klr00jkprbugmojf3smj4i5gnn71",
                "Bearer 0xqbwza6gbcmupei31qhwex07prjyis6",
                ORDER_NO ,
                map
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });

    }

    public static MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus_Zone_ON_83 = new MutableLiveData<>();

    public void UpdateOrderStatus_Zone_ON_83(String ORDER_NO, String ZONE, String Status) {


        String text=ZONE+"/"+ORDER_NO+"/"+Status;
        ApiClient.build().UpdateOrderStatus_Zone_ON_83(text)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus_Zone_ON_83.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });

    }
    public static MutableLiveData<ResponseDriver> mutableLiveData_ReadDriverIDS = new MutableLiveData<>();

    public void GetDriversID(){
        ApiClient.build().GetDrivers_IDS()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((ResponseDriver responseSms) -> {
                            mutableLiveData_ReadDriverIDS.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });
    }

    public static MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateDriverID_ON_83 = new MutableLiveData<>();

    public void UpdateOrder_DriverID_ON_83(String ORDER_NO, String DriverID) {


        String text=ORDER_NO+"/"+DriverID;
        ApiClient.build().UpdateOrder_DriverID_83(text)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateDriverID_ON_83.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });

    }


    private MutableLiveData<ResponseSms> smsLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseSms> getSmsLiveData() {
        return smsLiveData;
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

                        });

    }


    public static MutableLiveData<Response> runTimeSheetData = new MutableLiveData<>();
    public MutableLiveData<Response> getSheetLiveData() {
        return runTimeSheetData ;
    }

    public void SheetData(String id ,String ORDER_NO ,String DRIVER_ID , String Username  ) {

        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("ORDER_NO", ORDER_NO);
        map.put("DRIVER_ID", DRIVER_ID);
        map.put("Username", Username);
        ApiClient.build().ReadRunTimeSheet(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((@SuppressLint("CheckResult") Response responseSms) -> {
                            runTimeSheetData.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error_Vof ",throwable.getMessage());
                        });

    }


    private MutableLiveData<ResponseFetchRuntimesheetID> RetrieverunTimeSheetData = new MutableLiveData<>();
    public MutableLiveData<ResponseFetchRuntimesheetID> RetrieveSheetLiveData() {
        return RetrieverunTimeSheetData ;
    }

    public void RetieveSheetData(String Runsheet_id ){

        HashMap<String, String> map = new HashMap<>();
        map.put("id", Runsheet_id);

        ApiClient.build().RetrieveRunTimeSheet(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((@SuppressLint("CheckResult") ResponseFetchRuntimesheetID responseSms) -> {
                            RetrieverunTimeSheetData.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error_Vof ",throwable.getMessage());
                        });

    }

}
