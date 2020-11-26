package com.example.packingapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.Helper.Constant;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityGetOrderDataBinding;
import com.example.packingapp.databinding.ActivityRecievePackedOrderForSortingBinding;
import com.example.packingapp.model.GetOrderResponse.OrderDataModuleDBHeader;
import com.example.packingapp.model.GetOrderResponse.ResponseGetOrderData;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.viewmodel.GetOrderDataViewModel;
import com.example.packingapp.viewmodel.RecievePackedOrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecievePackedOrderForSortingActivity extends AppCompatActivity {
ActivityRecievePackedOrderForSortingBinding binding;
    private static final String TAG = "RecievePackedOrderForSo";
    RecievePackedOrderViewModel recievePackedOrderViewModel;
    AppDatabase database;
    final Context context = this;
    String Zone ,trackingnumberIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRecievePackedOrderForSortingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=AppDatabase.getDatabaseInstance(this);

        recievePackedOrderViewModel= ViewModelProviders.of(this).get(RecievePackedOrderViewModel.class);

        binding.btnLoadingNewPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OrderNumber;

                if (!binding.editTrackingnumber.getText().toString().isEmpty() &&
                        binding.editTrackingnumber.getText().toString().contains("-")) {
                  //  if (!my_match.find()) {
                    if (Constant.RegulerExpre_forTrackingNumbeer(binding.editTrackingnumber.getText().toString())) {

                        Toast.makeText(context, "Special character not found in the string", Toast.LENGTH_SHORT).show();

                        OrderNumber =
                                binding.editTrackingnumber.getText().toString().substring(0,
                                        binding.editTrackingnumber.getText().toString().indexOf("-"));
                        String NOtrackingnumber = binding.editTrackingnumber.getText().toString().substring(
                                binding.editTrackingnumber.getText().toString().indexOf("-") + 1);
                        Log.e(TAG, "onClick: " + NOtrackingnumber);

                        List<RecievePackedModule> recievePackedlist = database.userDao().getRecievePacked_ORDER_NO(OrderNumber);
                        if (recievePackedlist.size() == 0) {
                            binding.editTrackingnumber.setError(null);

                            GETOrderData(OrderNumber, binding.editTrackingnumber.getText().toString());
                            Log.e(TAG, "onClick: Ord " + OrderNumber);
                            //    Toast.makeText(RecievePackedOrderForSortingActivity.this, "تم", Toast.LENGTH_SHORT).show();
                        } else {
                            if (database.userDao().getRecievePacked_Tracking_Number(binding.editTrackingnumber.getText().toString())
                                    .size() == 0 && Integer.valueOf(recievePackedlist.get(0).getNO_OF_PACKAGES()) >=
                                    Integer.valueOf(NOtrackingnumber)) {
                                binding.editTrackingnumber.setError(null);
                                database.userDao().insertRecievePacked(new RecievePackedModule(
                                        recievePackedlist.get(0).getORDER_NO(), recievePackedlist.get(0).getNO_OF_PACKAGES(),
                                        binding.editTrackingnumber.getText().toString()));
                                //    GETOrderData(binding.editTrackingnumber.getText().toString());
                                Log.e(TAG, "onClick: Trac " + binding.editTrackingnumber.getText().toString());
                                Toast.makeText(RecievePackedOrderForSortingActivity.this, "تم", Toast.LENGTH_SHORT).show();
                            } else {
                                binding.editTrackingnumber.setError("تم أدخال هذا من قبل ");
                                binding.editTrackingnumber.setText("");
                            }
                        }
                    }else {
                        Toast.makeText(context, "Special character found in the string", Toast.LENGTH_SHORT).show();
                    }
                }else {

                    binding.editTrackingnumber.setError(getString(R.string.enter_tracking_number));
                    binding.editTrackingnumber.setText("");
                }
            }
        });


        binding.btnUpdateOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecievePackedModule>  recievePackedORDER_NO_Distinctlist=  database.userDao().getRecievePacked_ORDER_NO_Distinct();
                List<RecievePackedModule>  NOTrecievedPackedORDERlist=  new ArrayList<>();
                if (recievePackedORDER_NO_Distinctlist.size()>0) {
                    for (int i = 0; i>recievePackedORDER_NO_Distinctlist.size();i++){
                        List<String>  recievePacked_Tracking_Number_countlist=
                                database.userDao().getRecievePacked_Tracking_Number_count(recievePackedORDER_NO_Distinctlist.get(i).getORDER_NO());
                        if (!recievePacked_Tracking_Number_countlist.get(i).
                                equalsIgnoreCase(recievePackedORDER_NO_Distinctlist.get(i).getNO_OF_PACKAGES().toString())){
                            NOTrecievedPackedORDERlist.add(recievePackedORDER_NO_Distinctlist.get(i));
                        }
                    }

                    if (NOTrecievedPackedORDERlist.size()==0){
                        //TODO UPDATE STATUS
                       // Toast.makeText(RecievePackedOrderForSortingActivity.this, String.format("%s",getString(R.string.message_equalfornoofpaclkageandcountoftrackingnumbers)), Toast.LENGTH_SHORT).show();
                        UpdateStatus_ON_83();
                        UpdateStatus();
                    }else {
                        Toast.makeText(RecievePackedOrderForSortingActivity.this, String.format("%s",
                                getString(R.string.message_not_equalfornoofpaclkageandcountoftrackingnumbers)), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RecievePackedOrderForSortingActivity.this, String.format("%s",
                            getString(R.string.there_is_no_trackednumber_scanned)), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnDeleteLastTrackingnumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecievePackedModule>  recievePackedORDER_NO_Distinctlist=  database.userDao().getRecievePacked_ORDER_NO_Distinct();
                if (recievePackedORDER_NO_Distinctlist.size()>0) {
                    new AlertDialog.Builder(RecievePackedOrderForSortingActivity.this)
                            .setTitle(getString(R.string.delete_dialoge))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.userDao().deleteRecievePackedModule();
                                    binding.editTrackingnumber.setError(null);
                                    binding.editTrackingnumber.setText("");
                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
                }else {
                    Toast.makeText(RecievePackedOrderForSortingActivity.this, "لايوجد بيانات للحذف", Toast.LENGTH_SHORT).show();
                }
            }
        });

      /*  binding.btnAssignOrdersToZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText zoneInput = (EditText) promptsView
                        .findViewById(R.id.edit_zone);
                final EditText trackingnumberInput = (EditText) promptsView
                        .findViewById(R.id.edit_trackingnumber);

                Zone = zoneInput.getText().toString();
                trackingnumberIn = trackingnumberInput.getText().toString();


                final Button btn_assign_zone = (Button) promptsView
                        .findViewById(R.id.btn_assign_to_zone);

                /*final Button btn_Dismiss = (Button) promptsView
                        .findViewById(R.id.btn_dismiss);
                btn_Dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                    }
                });

                btn_assign_zone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (trackingnumberInput.getText().toString().isEmpty()
                                && zoneInput.getText().toString().isEmpty()) {
                            AssignToZone(trackingnumberIn, Zone);
                        }else{
                            if (trackingnumberInput.getText().toString().isEmpty()){
                                trackingnumberInput.setError("Enter tracking number");
                            }else if (zoneInput.getText().toString().isEmpty())
                                zoneInput.setError("enter zone");
                        }
                    }
                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });*/
    }


    private void GETOrderData(String ordernumber , String trackingnumber){
        recievePackedOrderViewModel.fetchdata(ordernumber);
        recievePackedOrderViewModel.getOrderDataLiveData().observe(RecievePackedOrderForSortingActivity.this, new Observer<RecievePackedModule>() {
            @Override
            public void onChanged(RecievePackedModule responseGetOrderData) {
                Log.e(TAG, "onChanged: "+ responseGetOrderData.getNO_OF_PACKAGES());
                String NOtrackingnumber= binding.editTrackingnumber.getText().toString().substring(
                        binding.editTrackingnumber.getText().toString().indexOf("-")+1);

                if (Integer.valueOf(responseGetOrderData.getNO_OF_PACKAGES()) >=
                        Integer.valueOf(NOtrackingnumber)) {
                    database.userDao().insertRecievePacked(new RecievePackedModule(
                            responseGetOrderData.getORDER_NO(), responseGetOrderData.getNO_OF_PACKAGES(),
                            trackingnumber));
                    Toast.makeText(RecievePackedOrderForSortingActivity.this, "تم", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context, "تم أدخال رقم غير صحيح", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "onChanged: insertR"+trackingnumber );
            }

        });

        recievePackedOrderViewModel.mutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: "+s );
                Toast.makeText(RecievePackedOrderForSortingActivity.this, s, Toast.LENGTH_LONG).show();

                if (s.equals("HTTP 503 Service Unavailable")) {
                    Toast.makeText(context, "تم أدخال رقم غير صحيح", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void UpdateStatus_ON_83(){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
        List<RecievePackedModule> orderDataModuleDBHeaderkist = database.userDao().getorderNORecievePackedModule();
        if (orderDataModuleDBHeaderkist.size() > 0) {
            recievePackedOrderViewModel.UpdateStatus_ON_83(
                    orderDataModuleDBHeaderkist.get(0).getORDER_NO(),
                    "in sorting"
            );
        }else {
            Toast.makeText(context, "لم يتم أدخال ", Toast.LENGTH_SHORT).show();
        }
        recievePackedOrderViewModel.mutableLiveData_UpdateStatus_ON_83.observe(
                RecievePackedOrderForSortingActivity.this, new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                Toast.makeText(RecievePackedOrderForSortingActivity.this, ""+message.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onChanged:update "+message.getMessage() );
            }
        });
//        }else {
//            Toast.makeText(GetOrderDatactivity.this, "توجد عناصر لم يتم تعبئتها", Toast.LENGTH_SHORT).show();
//        }
    }


    public void UpdateStatus(){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){

        List<RecievePackedModule> orderDataModuleDBHeaderkist = database.userDao().getorderNORecievePackedModule();

        recievePackedOrderViewModel.UpdateStatus(
                orderDataModuleDBHeaderkist.get(0).getORDER_NO(),
                "in sorting"
        );
        recievePackedOrderViewModel.mutableLiveData_UpdateStatus.observe(RecievePackedOrderForSortingActivity.this, new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                Toast.makeText(RecievePackedOrderForSortingActivity.this, ""+message.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onChanged:UpdateStatus "+message.getMessage() );
            }
        });
        recievePackedOrderViewModel.mutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: "+s );
                Toast.makeText(RecievePackedOrderForSortingActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });


    }

    /*
    private void AssignToZone(String trackingnumber1 ,String Zone1){
        String OrderNumber=
                trackingnumber1.substring(0,
                        trackingnumber1.indexOf("-"));

        List<RecievePackedModule>  recievePackedlist=  database.userDao().getRecievePacked_ORDER_NO(OrderNumber);

        if (recievePackedlist.size() == 0) {
            GETOrderData(binding.editTrackingnumber.getText().toString());
        //    database.userDao().UpdatezoneForORDER_NO(OrderNumber,Zone1);
            Toast.makeText(context, "تم", Toast.LENGTH_SHORT).show();
        }else if (recievePackedlist.size() >= 0){
            if (recievePackedlist.get(0).getZone().isEmpty()) {
                database.userDao().UpdatezoneForORDER_NO(OrderNumber,Zone1);
                Toast.makeText(context, "تم", Toast.LENGTH_SHORT).show();
            }else if (!recievePackedlist.get(0).getZone().isEmpty()){
                new AlertDialog.Builder(RecievePackedOrderForSortingActivity.this)
                        .setTitle(getString(R.string.updte_zone_if_exist))
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                database.userDao().UpdatezoneForORDER_NO(OrderNumber,Zone1);
                                Toast.makeText(context, "تم", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }).show();
            }
        }
    }
     */
}