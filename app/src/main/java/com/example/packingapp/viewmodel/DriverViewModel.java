package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.ResponseDriver;
import com.example.packingapp.model.ResponseVehicle;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DriverViewModel extends ViewModel {
    private static final String TAG = "DriverViewModel";
    public  MutableLiveData<Message> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Message> getDriverLiveData() {
        return mutableLiveData;
    }

    public  MutableLiveData<String> mutableLiveData_error = new MutableLiveData<>();
    public MutableLiveData<String> getDriverLiveData_error() {
        return mutableLiveData_error;
    }

    public void fetchdata(String nameArabic, String nameEnglish, String status, String company, String Phone, String address, String Vechile_ID,String National_ID,String EmployeeID) {

        HashMap<String, String> map = new HashMap<>();
        map.put("NameArabic", nameArabic);
        Log.i(TAG, "nameArabic"+nameArabic);
        map.put("NameEnglish", nameEnglish);
        Log.i(TAG, "nameEnglish"+nameEnglish);
        map.put("Status", status);
        Log.i(TAG, "status"+status);
        map.put("Company", company);
        Log.i(TAG, "company"+company);
        map.put("Phone", Phone);
        Log.i(TAG, "Phone"+Phone);
        map.put("Address", address);
        Log.i(TAG, "address"+address);
        map.put("Vechile_ID", Vechile_ID);
        Log.i(TAG, "Vechile_ID"+Vechile_ID);
        map.put("National_ID", National_ID);
        Log.i(TAG, "National_ID"+National_ID);
        map.put("EmployeeID", EmployeeID);
        Log.i(TAG, "EmployeeID"+EmployeeID);
        ApiClient.build().createDriver(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData.setValue(responseSms);
                        }
                        ,throwable -> {
                            mutableLiveData_error.setValue(throwable.getMessage());
                            Log.d("Error_CreateDriver",throwable.getMessage());

                        });

    }

    public void updateData(String id, String nameArabic, String nameEnglish, String status, String company, String Phone, String address, String Vechile_ID, String National_ID,String EmployeeID) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Driver_ID", id);
        map.put("NameArabic", nameArabic);
        map.put("NameEnglish", nameEnglish);
        map.put("Status", status);
        map.put("Company", company);
        map.put("Phone", Phone);
        map.put("Address", address);
        map.put("Vechile_ID", Vechile_ID);
        map.put("National_ID", National_ID);
        map.put("EmployeeID", EmployeeID);

        ApiClient.build().updateDriver(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            mutableLiveData.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });

    }
    public  MutableLiveData<ResponseVehicle> mutableLiveDataVehicle = new MutableLiveData<>();

    public void fetchDataVehicle(){
        ApiClient.build().readVehicle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((ResponseVehicle responseSms) -> {
                            mutableLiveDataVehicle.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });
    }
    public  MutableLiveData<ResponseDriver> mutableLiveDataRead = new MutableLiveData<>();


    public void fetchDataDriver(){
        ApiClient.build().readDriver()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((ResponseDriver responseSms) -> {
                            mutableLiveDataRead.setValue(responseSms);
                        }
                        ,throwable -> {
                            Log.d("Error",throwable.getMessage());

                        });
    }


}
