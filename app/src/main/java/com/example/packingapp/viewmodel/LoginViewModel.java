package com.example.packingapp.viewmodel;

import android.util.Log;

import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.ResponseLogin;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<ResponseLogin> smsLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseLogin> getSmsLiveData() {
        return smsLiveData;
    }
    public  MutableLiveData<String> mutableLiveDataError = new MutableLiveData<>();

    public void fetchdata(String username, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        ApiClient.build().loginwithno(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseSms -> {
                            smsLiveData.setValue(responseSms);
                        }
                        ,throwable -> {
                            mutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());
                        });
    }

    private MutableLiveData<Message> forgetpasswordLiveData = new MutableLiveData<>();
    public MutableLiveData<Message> getforgetpasswordLiveData() {
        return forgetpasswordLiveData;
    }
    public  MutableLiveData<String> forgetpasswordmutableLiveDataError = new MutableLiveData<>();

    public void forgetpassword(String username, String password, String Newpassword) {

        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("newpassword", Newpassword);

        ApiClient.build().Forgetpassword(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(message -> {
                            forgetpasswordLiveData.setValue(message);
                        }
                        ,throwable -> {
                            forgetpasswordmutableLiveDataError.setValue(throwable.getMessage());
                            Log.d("Error",throwable.getMessage());

                        });
    }

}
