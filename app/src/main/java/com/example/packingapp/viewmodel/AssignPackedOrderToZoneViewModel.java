package com.example.packingapp.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.RecievePacked.ResponseFetchRuntimesheetID;
import com.example.packingapp.model.ResponseDriver;
import com.example.packingapp.model.ResponseSms;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.model.ResponseZoneName;
import com.example.packingapp.model.TimeSheet.Response;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AssignPackedOrderToZoneViewModel extends ViewModel {
    private static final String TAG = "AssignPackedOrderToZone";
    private MutableLiveData<RecievePackedModule> OrderDataLiveData = new MutableLiveData<>();
    public MutableLiveData<RecievePackedModule> getOrderDataLiveData() {
        return OrderDataLiveData;
    }

    public  MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

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

    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus = new MutableLiveData<>();
    public MutableLiveData<ResponseUpdateStatus> getmutableLiveData_UpdateStatus(){
        return mutableLiveData_UpdateStatus;
    }

    public void UpdateStatus(String ORDER_NO, String Status) {
        HashMap<String, String> map = new HashMap<>();
        map.put("number", ORDER_NO);
        map.put("status", Status);

        ApiClient.build().UpdateOrderStatus(
//                "Bearer 0xqbwza6gbcmupei31qhwex07prjyis6",
                ORDER_NO,Status,"Bearer lnv0klr00jkprbugmojf3smj4i5gnn71"
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
                            Log.d("Error_roub",throwable.getMessage());

                        });*/

    }

    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus_Zone_ON_83 = new MutableLiveData<>();
    public  MutableLiveData<ResponseUpdateStatus> getmutableLiveData_UpdateStatus_Zone_ON_83 (){
        return mutableLiveData_UpdateStatus_Zone_ON_83;
    };


    public  MutableLiveData<String> mutableLiveDataError_Zone_ON_83 = new MutableLiveData<>();

    public void UpdateOrderStatus_Zone_ON_83(String ORDER_NO, String ZONE, String Status,String ModifyedBy) {


        String text=ZONE+"/"+ORDER_NO+"/"+Status+"/"+ModifyedBy;
        ApiClient.build().UpdateOrderStatus_Zone_ON_83(text)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus_Zone_ON_83.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());
                            mutableLiveDataError_Zone_ON_83.setValue(throwable.getMessage());
                        });

    }
    public MutableLiveData<ResponseDriver> mutableLiveData_ReadDriverIDS = new MutableLiveData<>();

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


    public  MutableLiveData<ResponseZoneName> mutableLiveData_readZones = new MutableLiveData<>();

    public void GetZonessID(){
        ApiClient.build().readZone()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((ResponseZoneName responseZoneName) -> {

                            mutableLiveData_readZones.setValue(responseZoneName);
                        }
                        ,throwable -> {
                            Log.d("Error_zones",throwable.getMessage());

                        });
    }

    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateDriverID_ON_83 = new MutableLiveData<>();

    public void UpdateOrder_DriverID_ON_83(String ORDER_NO, String DriverID,String ModifyedBy) {


        String text=ORDER_NO+"/"+DriverID+"/"+ModifyedBy+"/ready_to_go";
        Log.e(TAG, "zzUpdateOrder_DriverID_ON_83:txt "+text );
        ApiClient.build().UpdateOrder_DriverID_83(text)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateDriverID_ON_83.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error_UpdateOrder_Dri",throwable.getMessage());

                        });

    }


    private MutableLiveData<ResponseSms> smsLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseSms> getSmsLiveData() {
        return smsLiveData;
    }
    public  MutableLiveData<String> mutableLiveDataError_SendSms = new MutableLiveData<>();

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


    public MutableLiveData<Response> runTimeSheetData = new MutableLiveData<>();
    public MutableLiveData<Response> getSheetLiveData() {
        return runTimeSheetData ;
    }
    public MutableLiveData<String> mutableLiveDataError_SheetData = new MutableLiveData<>();

    public void SheetData(String id ,String ORDER_NOs ,String DRIVER_ID , String Username  ) {

        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("Query", "select ORDER_NO_H as ORDER_NO ,CUSTOMER_NAME , GRAND_TOTAL,\n" +
                        " ADDRESS_DETAILS  , OUTBOUND_DELIVERY , ZONE ,STATUS, NO_OF_PACKAGES , CUSTOMER_PHONE  , ITEM_PRICE , TRACKING_NO\n" +
                        " from (select ORDER_NO as ORDER_NO_H , CUSTOMER_NAME , GRAND_TOTAL,\n" +
                        " ADDRESS_DETAILS  , OUTBOUND_DELIVERY , ZONE ,STATUS,NO_OF_PACKAGES,CUSTOMER_PHONE \n" +
                        " from PackingApp.HEADER group by ORDER_NO ) as h\n" +
                        " left outer join \n" +
                        " (SELECT ORDER_NO as ORDER_NO_D  , sum(ITEM_PRICE) ITEM_PRICE , TRACKING_NO from PackingApp.DETAILS group by TRACKING_NO) as d \n" +
                        " on ORDER_NO_H=ORDER_NO_D\n" +
                        "where  ORDER_NO_H in( "+ORDER_NOs +")");

        Log.e(TAG, "SheetData: "+"select ORDER_NO_H as ORDER_NO ,CUSTOMER_NAME , GRAND_TOTAL,\n" +
                " ADDRESS_DETAILS  , OUTBOUND_DELIVERY , ZONE ,STATUS, NO_OF_PACKAGES , CUSTOMER_PHONE  , ITEM_PRICE , TRACKING_NO\n" +
                " from (select ORDER_NO as ORDER_NO_H , CUSTOMER_NAME , GRAND_TOTAL,\n" +
                " ADDRESS_DETAILS  , OUTBOUND_DELIVERY , ZONE ,STATUS,NO_OF_PACKAGES,CUSTOMER_PHONE \n" +
                " from PackingApp.HEADER group by ORDER_NO ) as h\n" +
                " left outer join \n" +
                " (SELECT ORDER_NO as ORDER_NO_D  , sum(ITEM_PRICE) ITEM_PRICE , TRACKING_NO from PackingApp.DETAILS group by TRACKING_NO) as d \n" +
                " on ORDER_NO_H=ORDER_NO_D\n" +
                "where  ORDER_NO_H in( "+ORDER_NOs +")");

        map.put("DRIVER_ID", DRIVER_ID);
        map.put("Username", Username);
        ApiClient.build().ReadRunTimeSheet(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((@SuppressLint("CheckResult") Response responseSms) -> {
                            runTimeSheetData.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error_SheetData ",throwable.getMessage());
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
