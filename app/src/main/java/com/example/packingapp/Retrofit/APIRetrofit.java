package com.example.packingapp.Retrofit;

import com.example.packingapp.model.APKVersion;
import com.example.packingapp.model.DriverModules.DriverPackages_Respones_Details_recycler;
import com.example.packingapp.model.DriverModules.DriverPackages_Respones_Header_recycler;
import com.example.packingapp.model.DriverModules.ResponeEndOfDay;
import com.example.packingapp.model.GetOrderResponse.ResponseGetOrderData;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.RecievePacked.ResponseFetchRuntimesheetID;
import com.example.packingapp.model.ReprintAWBModules.ResponseReprintAWB;
import com.example.packingapp.model.ResponseDriver;
import com.example.packingapp.model.ResponseLogin;
import com.example.packingapp.model.ResponseSms;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.model.ResponseVehicle;
import com.example.packingapp.model.ResponseWay;
import com.example.packingapp.model.ResponseZoneName;
import com.example.packingapp.model.TimeSheet.Response;
import com.google.gson.JsonArray;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRetrofit {
    //To Get orderdata
    //To Get orderdata
    @FormUrlEncoded
    @POST("GetMagentoOrderDetails.php")
    Observable<ResponseGetOrderData> GetOrderData(@Field("number") String phone,
                                                  @Field("token") String token);

    @FormUrlEncoded
    @POST("UpdateMagentoOrder.php")
    Observable<ResponseUpdateStatus> UpdateOrderStatus(@Field("number") String phone,
                                                       @Field("status") String status,
                                                       @Field("token") String token);



    @POST("Login/Auth.php")
    Observable<ResponseLogin> loginwithno(@Body Map<String, String> mobile);

    @POST("Login/ForgetPassword.php")
    Observable<Message> Forgetpassword(@Body Map<String, String> mobile);

    @POST("Version/GetVersion.php")
    Observable<APKVersion> GetVersion();

    @FormUrlEncoded
    @POST("Vodafone/SendSMS.php")
    Observable<ResponseSms> sendSms(@Field("number") String phone, @Field("message") String message);

    @POST("Vechile/Create.php")
    Observable<Message> createVehicle(@Body Map<String, String> mobile);

    @POST("Driver/Create.php")
    Observable<Message> createDriver(@Body Map<String, String> mobile);

    @POST("Direction/Create.php")
    Observable<Message> createDirection(@Body Map<String, String> mobile);

    @GET("Driver/Read.php")
    Observable<ResponseDriver> readDriver();

    @GET("Vechile/Read.php")
    Observable<ResponseVehicle> readVehicle();

    @GET("Direction/Read.php")
    Observable<ResponseWay> readWay();

    @POST("Vechile/Update.php")
    Observable<Message> updateVehicle(@Body Map<String, String> mobile);

    @POST("Driver/Update.php")
    Observable<Message> updateDriver(@Body Map<String, String> mobile);

    @POST("Direction/Update.php")
    Observable<Message> updateWay(@Body Map<String, String> mobile);

    @POST("Inout/create.php")
    Observable<Message> createInOut(@Body Map<String, String> mobile);

    @POST("Ordernumber/CreateHeader.php")
    Observable<Message> InsertOrderDataHeader(@Body Map<String, String> mobile);

    @FormUrlEncoded
    @POST("Ordernumber/CreateDetails.php")
    Observable<Message> InsertOrderDataDetails(@Field("ORDER_NO") String ORDER_NO,
                                               @Field("ItemsOrderDataDBDetailsList[0]") String name);


//TODO test arraylist
   // @FormUrlEncoded
    @POST("Ordernumber/writeinLogs_sap_ITEMStable.php")
    Observable<Message> InsertOrderDataDetails_testarrylist(
                                               @Body JsonArray name );

    @POST("Ordernumber/Read.php")
    Observable<RecievePackedModule> GetOrderNumberAndNumPackage(@Body Map<String, String> mobile);

    @POST("Ordernumber/Read_getSMSData.php")
    Observable<RecievePackedModule> GetOrderNumberDataAndSMSData(@Body Map<String, String> mobile);

    @FormUrlEncoded
    @POST("Ordernumber/UpdateStatus.php")
    Observable<ResponseUpdateStatus> UpdateOrderStatus_ON_83(@Field("OrderNumberHeader[0]") String name);
    @FormUrlEncoded
    @POST("Ordernumber/UpdateStatusAndRescheduleTime.php")
    Observable<ResponseUpdateStatus> UpdateOrderStatus_RescheduleTime_ON_83(@Field("OrderNumberHeader[0]") String name);
    @FormUrlEncoded
    @POST("Ordernumber/UpdateStatusAndZone.php")
    Observable<ResponseUpdateStatus> UpdateOrderStatus_Zone_ON_83(@Field("OrderNumberHeader[0]") String name);

    @GET("Driver/Read.php")
    Observable<ResponseDriver> GetDrivers_IDS();

    @GET("Direction/Read_zone.php")
    Observable<ResponseZoneName> readZone();

    @FormUrlEncoded
    @POST("Ordernumber/UpdateDriverID.php")
    Observable<ResponseUpdateStatus> UpdateOrder_DriverID_83(@Field("OrderNumberHeader[0]") String name);

    @POST("Ordernumber/ReadDriverRunsheetOrders.php")
    Observable<DriverPackages_Respones_Header_recycler> ReadDriverRunsheetOrders_83(@Body Map<String, String> mobile);

    @POST("Ordernumber/ReadDriverTrackingnumbersOfOrders.php")
    Observable<DriverPackages_Respones_Details_recycler> ReadDriverTrackingnumbersOfOrders_83(@Body Map<String, String> mobile);

    @POST("Ordernumber/UpdateStatusAndPasscode.php")
    Observable<ResponseUpdateStatus> UpdateOrderStatus_PASSCODE_ON_83(@Body Map<String, String> mobile);
    
    @FormUrlEncoded
    @POST("Ordernumber/UpdateStatusAndReason.php")
    Observable<ResponseUpdateStatus> UpdateOrderStatus_Reasone_ON_83(@Field("TrackingNumberDetails[0]") String name);

    @POST("Ordernumber/GetOrderForEndOfDay.php")
    Observable<ResponeEndOfDay> GetOrderForEndOfDay_ON_83(@Body Map<String, String> mobile);

    @POST("Ordernumber/Read_ForRunTimeSheet.php")
    Observable<Response> ReadRunTimeSheet(@Body Map<String, String> mobile);

    @POST("Ordernumber/Reprint_ForRunTimeSheet.php")
    Observable<Response> ReprintRunTimeSheet(@Body Map<String, String> mobile);

    @POST("Ordernumber/Retrieve_ForRunTimeSheet.php")
    Observable<ResponseFetchRuntimesheetID> RetrieveRunTimeSheet(@Body Map<String, String> mobile);


    @POST("Ordernumber/Read_ForAWB.php")
    Observable<ResponseReprintAWB> Reprint_AWB(@Body Map<String, String> mobile);

    @POST("Ordernumber/CreateAWBLog.php")
    Observable<Message> Reprint_AWB_insertlog(@Body Map<String, String> mobile);
}
