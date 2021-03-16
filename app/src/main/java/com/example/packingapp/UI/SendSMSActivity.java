package com.example.packingapp.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivitySendSMSBinding;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.GetOrderResponse.OrderDataModuleDBHeader;
import com.example.packingapp.model.Message;
import com.example.packingapp.viewmodel.OrderDetailsForDriverViewModel_testArrayList;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SendSMSActivity extends AppCompatActivity {
    private static final String TAG = "SendSMSActivity";
    ActivitySendSMSBinding binding;
    //OrderDetailsForDriverViewModel SmsViewModel;
    OrderDetailsForDriverViewModel_testArrayList orderDetailsForDriverViewModel_testArrayList;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendSMSBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        SmsViewModel = ViewModelProviders.of(this).get(OrderDetailsForDriverViewModel.class);
        orderDetailsForDriverViewModel_testArrayList = ViewModelProviders.of(this)
                .get(OrderDetailsForDriverViewModel_testArrayList.class);

        database = AppDatabase.getDatabaseInstance(this);

       /* binding.send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SmsViewModel.SendSms(binding.phone.getText().toString(), binding.message.getText().toString());
                SmsViewModel.getSmsLiveData().observe(SendSMSActivity.this, new Observer<ResponseSms>() {
                    @Override
                    public void onChanged(ResponseSms responseSms) {
                        Toast.makeText(SendSMSActivity.this,
                                responseSms.getSMSStatus().toString(), Toast.LENGTH_SHORT).show();
                        Constant.ToastDialoge( responseSms.getSMSStatus().toString() , SendSMSActivity.this);

                    }
                });
            }
        });*/

        binding.uploadarraylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // database.userDao().getHeader_test_arraylist().get(0).getOrder_number();
                UploadDetails(database.userDao().getHeader_test_arraylist().get(0).getOrder_number());
            }
        });

        binding.printpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintPDF();
            }
        });


        orderDetailsForDriverViewModel_testArrayList.mutableLiveData_Details.observe(SendSMSActivity.this, new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                Toast.makeText(SendSMSActivity.this, "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onChanged: " + message.getMessage());
            }
        });
        orderDetailsForDriverViewModel_testArrayList.mutableLiveData_error.observe(SendSMSActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: " + s);
            }
        });
    }

    private void PrintPDF() {
        /*
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"HyperOne.pdf");//File path

        if (pdfFile.exists()) //Checking if the file exists or not
        {
            Uri path = Uri.fromFile(pdfFile);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
//            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setDataAndType(Uri.parse("content:///storage/emulated/0/HyperOne.pdf"), "application/pdf");
            objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(objIntent);//Starting the pdf viewer
        } else {
            Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();
        }
         */

        
    }

    private void UploadDetails(String ordernumberselected) {
        //todo  we need quere to get list if quantity scanned not equaled to required quantity

        if (database.userDao().getAllItemsNotScannedORLessRequiredQTY(ordernumberselected).size() == 0) {

            List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList = database.userDao().getDetailsTrackingnumberToUpload_scannedbyordernumber(ordernumberselected);
            // String OrderNumber = database.userDao().getOrderNumber();
            OrderDataModuleDBHeader orderDataModuleDBHeader = database.userDao().getordernumberData(ordernumberselected);

            float SumOfQTY = database.userDao().SumOfQTYFromDetials();
            Log.e(TAG, "UploadDetails:SumOfQTY " + SumOfQTY);
            float Shippingfees = orderDataModuleDBHeader.getShipping_fees();
            Log.e(TAG, "UploadDetails:Shippingfees " + Shippingfees);
            float ShippingfeesPerItem = Shippingfees / SumOfQTY;
            Log.e(TAG, "UploadDetails:ShippingfeesPerItem " + ShippingfeesPerItem);

            orderDetailsForDriverViewModel_testArrayList.InsertOrderdataDetails(ordernumberselected, itemsOrderDataDBDetailsList, ShippingfeesPerItem);
        } else {
            Toast.makeText(SendSMSActivity.this, getResources().getString(R.string.item_notselected), Toast.LENGTH_SHORT).show();
        }
    }

}