package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class OrderDetailsForDriverViewModel_testArrayList extends ViewModel {
    private static final String TAG = "OrderDetailsForDriverVi";
    public static MutableLiveData<Message> mutableLiveData_Details = new MutableLiveData<>();
    public static MutableLiveData<String> mutableLiveData_error = new MutableLiveData<>();

    public void InsertOrderdataDetails(String OrderNumber ,
                                       List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList ,
                                       float ShippingfeesPerItem ) {
        HashMap<String, String> map = new HashMap<>();

        Gson gson = new GsonBuilder()
                .create();
        JsonArray equipmentJsonArray = gson.toJsonTree(itemsOrderDataDBDetailsList).getAsJsonArray();

        //From_Sap_Or_Not=false;
        map.put("ItemsOrderDataDBDetailsList", equipmentJsonArray.toString());
        Log.e(TAG, "InsertOrderdataDetails:size "+equipmentJsonArray.size() );
//        map.put("ORDER_NO", OrderNumber);
//        Log.e(TAG, "InsertOrderdataDetails: sss "+ ShippingfeesPerItem );
//        Log.e(TAG, "InsertOrderdataDetails: "+itemsOrderDataDBDetailsList.size() );
//        for (int i =0;i<itemsOrderDataDBDetailsList.size();i++) {
//            //"\u200e"
////TODO itemsOrderDataDBDetailsList.get(i).getPrice() + (itemsOrderDataDBDetailsList.get(i).getQuantity()*ShippingfeesPerItem)
//            Float TotalPriceForBarcode=0f;
//            if (itemsOrderDataDBDetailsList.get(i).getSku().substring(0,2).equalsIgnoreCase("23")){
//                TotalPriceForBarcode = itemsOrderDataDBDetailsList.get(i).getPrice() +
//                        ShippingfeesPerItem;
//            }else {
//                TotalPriceForBarcode = itemsOrderDataDBDetailsList.get(i).getPrice() +
//                        (itemsOrderDataDBDetailsList.get(i).getQuantity() * ShippingfeesPerItem);
//            }
//            String name= itemsOrderDataDBDetailsList.get(i).getName();
//
//            String itemsOrder=itemsOrderDataDBDetailsList.get(i).getTrackingNumber()+"/"+name+"/"+itemsOrderDataDBDetailsList.get(i).getSku()
//                    +"/"+(TotalPriceForBarcode)+"/"+itemsOrderDataDBDetailsList.get(i).getQuantity()
//                    +"/"+itemsOrderDataDBDetailsList.get(i).getUnite();
//            Log.e("data",itemsOrder);
//            Log.e("OrderNumber",OrderNumber);
            ApiClient.build().InsertOrderDataDetails_testarrylist(equipmentJsonArray)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(responseSms -> {
                                mutableLiveData_Details.setValue(responseSms);
                            }
                            , throwable -> {
                                Log.d("Errorr ", throwable.getMessage());
                                mutableLiveData_error.setValue(throwable.getMessage());
                            });

       // }
    }

}
