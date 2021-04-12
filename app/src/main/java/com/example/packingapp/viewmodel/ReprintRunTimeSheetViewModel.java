package com.example.packingapp.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.TimeSheet.Response;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReprintRunTimeSheetViewModel extends ViewModel {
    public MutableLiveData<Response> runTimeSheetData = new MutableLiveData<>();
    public MutableLiveData<Response> getSheetLiveData() {
        return runTimeSheetData ;
    }
    public MutableLiveData<String> mutableLiveDataError_SheetData = new MutableLiveData<>();

    public void SheetData(String id  ) {

        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
//        map.put("Query", "select ORDER_NO_H as ORDER_NO ,CUSTOMER_NAME , GRAND_TOTAL,\n" +
//                " ADDRESS_DETAILS  , OUTBOUND_DELIVERY , ZONE ,STATUS, NO_OF_PACKAGES , CUSTOMER_PHONE  , ITEM_PRICE , TRACKING_NO\n" +
//                " from (select ORDER_NO as ORDER_NO_H , CUSTOMER_NAME , GRAND_TOTAL,\n" +
//                " ADDRESS_DETAILS  , OUTBOUND_DELIVERY , ZONE ,STATUS,NO_OF_PACKAGES,CUSTOMER_PHONE \n" +
//                " from "+ApiClient.DataBasename+".HEADER group by ORDER_NO ) as h\n" +
//                " left outer join \n" +
//                " (SELECT ORDER_NO as ORDER_NO_D  , sum(ITEM_PRICE) ITEM_PRICE , TRACKING_NO from  "+ApiClient.DataBasename+".DETAILS group by TRACKING_NO) as d \n" +
//                " on ORDER_NO_H=ORDER_NO_D\n" +
//                "where  ORDER_NO_H in( "+ORDER_NOs +")");
//

//        map.put("DRIVER_ID", DRIVER_ID);
//        map.put("Username", Username);
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


}
