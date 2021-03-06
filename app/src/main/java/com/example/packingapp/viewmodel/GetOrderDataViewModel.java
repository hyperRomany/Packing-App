package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.GetOrderResponse.ResponseGetOrderData;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetOrderDataViewModel extends ViewModel {
    private static final String TAG = "GetOrderDataViewModel";
    private MutableLiveData<ResponseGetOrderData> OrderDataLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseGetOrderData> getOrderDataLiveData() {
        return OrderDataLiveData;
    }

    public MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();
    public MutableLiveData<String> getmutableLiveDataError() {
        return mutableLiveDataError;
    }
    public void fetchdata(String ORDER_NO) {
        HashMap<String, String> map = new HashMap<>();
        map.put("number", ORDER_NO);
        //lnv0klr00jkprbugmojf3smj4i5gnn71
        //riohauug3nhyxtztgn2i8fkwuk3zzpjs
        ApiClient.build().GetOrderData(ORDER_NO,ApiClient.MAgentoToken
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseGetOrderData -> {
                            OrderDataLiveData.setValue(responseGetOrderData);
                            Log.e("responseGetOrderData",responseGetOrderData.getOrder_number());
                        }
                        ,throwable -> {
                            mutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());
                        });

       /* HashMap<String, String> map = new HashMap<>();
        map.put("OrderNumber", ORDER_NO);

        ApiClient.buildRo().GetOrderData(
                "Bearer lnv0klr00jkprbugmojf3smj4i5gnn71",
                // "Bearer 0xqbwza6gbcmupei31qhwex07prjyis6",
                ORDER_NO
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseGetOrderData -> {
                    OrderDataLiveData.setValue(responseGetOrderData);
                    Log.d(TAG, "fetchdata: "+responseGetOrderData);
                },throwable -> {
                    mutableLiveDataError.setValue(throwable.getMessage());
                    Log.d("Error",throwable.getMessage());

                });*/
    }

    public  MutableLiveData<Message> mutableLiveData = new MutableLiveData<>();
    public  MutableLiveData<Message> getmutableLiveData(){
        return mutableLiveData;
    }

    public  MutableLiveData<String> mutableLiveData_InsertH_Error = new MutableLiveData<>();

    public void InsertOrderdataHeader(String ORDER_NO, String OUTBOUND_DELIVERY, String CUSTOMER_NAME,
                                      String CUSTOMER_PHONE, String CUSTOMER_CODE, String ADDRESS_GOVERN, String ADDRESS_CITY,
                                      String ADDRESS_DISTRICT, String ADDRESS_DETAILS, String ORDER_DELIVERY_DATE, String ORDER_DELIVERY_TIME,
                                      String PICKER_CONFIMATION_TIME, String GRAND_TOTAL, String CURRENCY, float SHIPPING_FEES,String NO_OF_PACKAGES,
                                      String STORAGE_LOCATION,String CreatedBy,String reedemed_point_amount ,String Delivery_method,String Payment_method) {

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
        map.put("SHIPPING_FEES", String.valueOf(SHIPPING_FEES));
        map.put("NO_OF_PACKAGES", NO_OF_PACKAGES);
        Log.e(TAG, "InsertOrderdataHeader:NO_OF_PACKAGES "+NO_OF_PACKAGES );
        map.put("STORAGE_LOCATION", STORAGE_LOCATION);
        map.put("STATUS", "packed");
        map.put("CreatedBy", CreatedBy);
        map.put("Reedemed_Points_Amount",reedemed_point_amount+".0");
        map.put("Delivery_Method",Delivery_method);
        map.put("Payment_Method",Payment_method);

        ApiClient.build().InsertOrderDataHeader(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error_H ",throwable.getMessage());
                            mutableLiveData_InsertH_Error.setValue(throwable.getMessage());
                        });

    }

    public  MutableLiveData<Message> mutableLiveData_Details = new MutableLiveData<>();
    public MutableLiveData<Message> getmutableLiveData_Details() {
        return mutableLiveData_Details;
    }
    public  MutableLiveData<String> mutableLiveData_InsertD_Error = new MutableLiveData<>();

    public void InsertOrderdataDetails(String OrderNumber ,
                                       List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList ,
                                       float ShippingfeesPerItem ) {
        HashMap<String, String> map = new HashMap<>();

        Gson gson = new GsonBuilder()
                .create();
        JsonArray equipmentJsonArray = gson.toJsonTree(itemsOrderDataDBDetailsList).getAsJsonArray();

        //From_Sap_Or_Not=false;
//        map.put("ItemsOrderDataDBDetailsList", equipmentJsonArray.toString());
//        map.put("ORDER_NO", OrderNumber);
        Log.e(TAG, "InsertOrderdataDetails: sss "+ ShippingfeesPerItem );
        Log.e(TAG, "InsertOrderdataDetails: "+itemsOrderDataDBDetailsList.size() );
        for (int i =0;i<itemsOrderDataDBDetailsList.size();i++) {
            //"\u200e"
//TODO itemsOrderDataDBDetailsList.get(i).getPrice() + (itemsOrderDataDBDetailsList.get(i).getQuantity()*ShippingfeesPerItem)
            Float TotalPriceForBarcode=0f;
            if (itemsOrderDataDBDetailsList.get(i).getSku().substring(0,2).equalsIgnoreCase("23")){
                TotalPriceForBarcode = itemsOrderDataDBDetailsList.get(i).getPrice() /*+
                        ShippingfeesPerItem*/;
            }else {
                 TotalPriceForBarcode = itemsOrderDataDBDetailsList.get(i).getPrice() ;
               //         (itemsOrderDataDBDetailsList.get(i).getQuantity() /* ShippingfeesPerItem*/);
            }
           String name= itemsOrderDataDBDetailsList.get(i).getName();

            String itemsOrder=itemsOrderDataDBDetailsList.get(i).getTrackingNumber()+"%"+name+"%"+itemsOrderDataDBDetailsList.get(i).getSku()
                    +"%"+(TotalPriceForBarcode)+"%"+itemsOrderDataDBDetailsList.get(i).getQuantity()
                    +"%"+itemsOrderDataDBDetailsList.get(i).getUnite();



            Log.e("data",itemsOrder);
            Log.e("OrderNumber",OrderNumber);
                ApiClient.build().InsertOrderDataDetails(OrderNumber,itemsOrder)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(responseSms -> {
                                    mutableLiveData_Details.setValue(responseSms);
                                }
                                , throwable -> {
                                    Log.d("Errorr_D ", throwable.getMessage());
                                    mutableLiveData_InsertD_Error.setValue(throwable.getMessage());
                                });

        }
    }

    public  MutableLiveData<ResponseUpdateStatus> mutableLiveData_UpdateStatus = new MutableLiveData<>();

    public void UpdateStatus(String ORDER_NO, String status) {
        ORDER_NO =ORDER_NO.replace("*","-");

        HashMap<String, String> map = new HashMap<>();
        map.put("number", ORDER_NO);
        map.put("status", status);

        Log.e(TAG, "UpdateStatus: "+ ORDER_NO);

        ApiClient.build().UpdateOrderStatus(
                ORDER_NO,status,ApiClient.MAgentoToken
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData_UpdateStatus.setValue(responseSms);

                        }
                        ,throwable -> {
                            Log.d("Error_roub",throwable.getMessage());

                        });

      /*  HashMap<String, String> map = new HashMap<>();
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

                        });*/

    }

}
