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

        ApiClient.build().ReprintRunTimeSheet(map)
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
