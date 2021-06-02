package com.example.packingapp.Retrofit;

import android.net.TrafficStats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //public static final String BASE_URL = "http://10.0.6.106/PackingApp/Packing_Api_Development_V2/";

    public static final String MAgentoType = "mcprod";
//    public static final String MAgentoType = "mcstaging";
    public static final String DataBasename = "PackingApp_PROD";
//    public static final String DataBasename = "PackingApp";

// 
//
    public static final String MAgentoToken = "Bearer riohauug3nhyxtztgn2i8fkwuk3zzpjs";
//    public static final String MAgentoToken = "Bearer lnv0klr00jkprbugmojf3smj4i5gnn71";
//
    public static final String BASE_URL = "http://10.0.6.106/PackingApp/Packing_Api_Development_V2_prod/";
    public static String ApksURL_ًWithoutName="http://10.0.6.106/PackingApp/Packing_Api_Development_V1_prod/Version/";
//
//    public static final String BASE_URL = "http://10.0.6.106/PackingApp/Packing_Api_Development_V2/";
//    public static String ApksURL_ًWithoutName="http://10.0.6.106/PackingApp/Packing_Api_Development_V1/Version/";

//    public final static String ApksURL_ًWithoutName ="http://10.2.1.220:82/PPCAPK/";

//    public static final String BASE_URL = "http://192.168.1.83/Packing_Api_Development_V1/";

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
