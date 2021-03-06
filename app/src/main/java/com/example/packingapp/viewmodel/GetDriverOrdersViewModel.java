package com.example.packingapp.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.DriverModules.DriverPackages_Respones_Header_recycler;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetDriverOrdersViewModel extends ViewModel {
    private static final String TAG = "GetDriverOrdersViewModel";
    private MutableLiveData<DriverPackages_Respones_Header_recycler> DriverOrdersReadyDataLiveData = new MutableLiveData<>();
    public MutableLiveData<DriverPackages_Respones_Header_recycler> DriverOrdersReadyDataLiveData() {
        return DriverOrdersReadyDataLiveData;
    }

    public  MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

    @SuppressLint("LongLogTag")
    public void ReadDriverRunsheetOrdersData(String DRIVER_ID, String ASSIGN_DATE, String STATUS) {

        HashMap<String, String> map = new HashMap<>();
        map.put("DRIVER_ID", DRIVER_ID);
        map.put("ASSIGN_DATE", ASSIGN_DATE);
        map.put("STATUS", STATUS);
        Log.e(TAG, "ReadDriverRunsheetOrdersData: "+DRIVER_ID );
        Log.e(TAG, "ReadDriverRunsheetOrdersData: "+ASSIGN_DATE );
        Log.e(TAG, "ReadDriverRunsheetOrdersData: "+ STATUS);

        ApiClient.build().ReadDriverRunsheetOrders_83(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe( (responseGetOrderData) -> {
                            DriverOrdersReadyDataLiveData.setValue(responseGetOrderData);
                          //  Log.d(TAG, "fetchdata: "+responseGetOrderData);
                        }
                        ,throwable -> {
                            mutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());

                        });
    }






  /*  public static MutableLiveData<Message> mutableLiveData = new MutableLiveData<>();

    public void InsertOrderdataHeader(String ORDER_NO, String OUTBOUND_DELIVERY, String CUSTOMER_NAME,
                                      String CUSTOMER_PHONE, String CUSTOMER_CODE, String ADDRESS_GOVERN, String ADDRESS_CITY,
                                      String ADDRESS_DISTRICT, String ADDRESS_DETAILS, String ORDER_DELIVERY_DATE, String ORDER_DELIVERY_TIME,
                                      String PICKER_CONFIMATION_TIME, String GRAND_TOTAL, String CURRENCY, String SHIPPING_FEES,
                                      String STORAGE_LOCATION) {

        HashMap<String, String> map = new HashMap<>();
        map.put("ORDER_NO", ORDER_NO);
        map.put("OUTBOUND_DELIVERY", OUTBOUND_DELIVERY);
        map.put("CUSTOMER_NAME", CUSTOMER_NAME);
        map.put("CUSTOMER_PHONE", CUSTOMER_PHONE);
        map.put("CUSTOMER_CODE", CUSTOMER_CODE);
        map.put("ADDRESS_GOVERN", ADDRESS_GOVERN);
        map.put("ADDRESS_CITY", ADDRESS_CITY);
        map.put("ADDRESS_DISTRICT", ADDRESS_DISTRICT);
        map.put("ADDRESS_DETAILS", ADDRESS_DETAILS);
        map.put("ORDER_DELIVERY_DATE", ORDER_DELIVERY_DATE);
        map.put("ORDER_DELIVERY_TIME", ORDER_DELIVERY_TIME);
        map.put("PICKER_CONFIMATION_TIME", PICKER_CONFIMATION_TIME);
        map.put("GRAND_TOTAL", GRAND_TOTAL);
        map.put("CURRENCY", CURRENCY);
        map.put("SHIPPING_FEES", SHIPPING_FEES);
        map.put("STORAGE_LOCATION", STORAGE_LOCATION);

        ApiClient.build().InsertOrderDataHeader(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());
                        });

    }

    public static MutableLiveData<Message> mutableLiveData_Details = new MutableLiveData<>();

    public void InsertOrderdataDetails(String OrderNumber , List<ItemsOrderDataDBDetails> itemsOrderDataDBDetailsList) {
        HashMap<String, String> map = new HashMap<>();

        Gson gson = new GsonBuilder()
                .create();
        JsonArray equipmentJsonArray = gson.toJsonTree(itemsOrderDataDBDetailsList).getAsJsonArray();

        //From_Sap_Or_Not=false;
//        map.put("ItemsOrderDataDBDetailsList", equipmentJsonArray.toString());
//        map.put("ORDER_NO", OrderNumber);

        for (int i =0;i<itemsOrderDataDBDetailsList.size();i++) {
            //"\u200e"

           String name= itemsOrderDataDBDetailsList.get(i).getName();

            String itemsOrder=itemsOrderDataDBDetailsList.get(i).getTrackingNumber()+"/"+name+"/"+itemsOrderDataDBDetailsList.get(i).getSku()
                    +"/"+itemsOrderDataDBDetailsList.get(i).getPrice()+"/"+itemsOrderDataDBDetailsList.get(i).getQuantity()+"/"+itemsOrderDataDBDetailsList.get(i).getUnite()+"/"+"DOne"+"/"+"Go";
            Log.e("data",itemsOrder);
            Log.e("OrderNumber",OrderNumber);
                ApiClient.build().InsertOrderDataDetails(OrderNumber,itemsOrder)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(responseSms -> {
                                    mutableLiveData_Details.setValue(responseSms);
                                }
                                , throwable -> {
                                    Log.d("Errorr ", throwable.getMessage());

                                });

        }
    }

    public static MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus = new MutableLiveData<>();

    public void UpdateStatus(String ORDER_NO, String Status) {


        HashMap<String, String> map = new HashMap<>();
        map.put("status", Status);

        ApiClient.RoubstaAPIRetrofit_UpdateStatus().UpdateOrderStatus(ORDER_NO ,
                "Bearer lnv0klr00jkprbugmojf3smj4i5gnn71",
                 Status
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });

    }*/

}
