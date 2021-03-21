package com.example.packingapp.Retrofit;

import android.net.TrafficStats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//    public static final String BASE_URL = "http://10.0.4.175/PackingApp/Packing_Api_Development_V1/";
        public static final String BASE_URL = "http://192.168.1.83/Packing_Api_Development_V1/";
    //   public static final String BASE_URL = "http://192.168.1.50:81/";
    private static Gson gson = new GsonBuilder()
         .setLenient()
         .create();

    public static APIRetrofit build() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
               // .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        TrafficStats.setThreadStatsTag(0x1000);
        return retrofit.create(APIRetrofit.class);
    }


   /* public static final String BASE_URL_Roubsta = "https://next.json-generator.com/";

    public static RoubstaAPIRetrofit buildRo() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_Roubsta)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        TrafficStats.setThreadStatsTag(0x1000);
        return retrofit.create(RoubstaAPIRetrofit.class);
    }*/
   public static final String BASE_URL_Roubsta = "https://mcstaging.hyperone.com.eg/rest/";
//    public static final String BASE_URL_Roubsta = "https://mcprod.hyperone.com.eg/rest/";

    public static RoubstaAPIRetrofit buildRo() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_Roubsta)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        TrafficStats.setThreadStatsTag(0x1000);
        return retrofit.create(RoubstaAPIRetrofit.class);
    }

}
