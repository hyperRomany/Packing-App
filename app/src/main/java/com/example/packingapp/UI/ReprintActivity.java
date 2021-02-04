package com.example.packingapp.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityReprintBinding;
import com.example.packingapp.model.ReprintAWBModules.ItemsOrderDataDetails_Scanned_Reprint;
import com.example.packingapp.model.ReprintAWBModules.OrderDataModuleHeader_Reprint;
import com.example.packingapp.model.ReprintAWBModules.ResponseReprintAWB;
import com.example.packingapp.viewmodel.ReprintAWBViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ReprintActivity extends AppCompatActivity {
    private static final String TAG = "ReprintActivity";
    ActivityReprintBinding binding;
    AppDatabase database;
    ReprintAWBViewModel reprintAWBViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReprintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reprintAWBViewModel= ViewModelProviders.of(this)
                .get(ReprintAWBViewModel.class);
        database= AppDatabase.getDatabaseInstance(this);

        reprintAWBViewModel.getOrderDataLiveData().observe(ReprintActivity.this, new Observer<ResponseReprintAWB>() {
            @Override
            public void onChanged(ResponseReprintAWB responseReprintAWB) {
                List<ItemsOrderDataDetails_Scanned_Reprint> itemsOrderDataDBDetailslist =new  ArrayList<>();
                itemsOrderDataDBDetailslist=responseReprintAWB.getDetails();
                List<OrderDataModuleHeader_Reprint> header_reprintList=new ArrayList<>();
                header_reprintList=responseReprintAWB.getHeader();
                Log.e(TAG, "onChanged:header_reprintList "+header_reprintList.size());
                Log.e(TAG, "onChanged:itemsOrderDataDBDetailslist "+itemsOrderDataDBDetailslist.size());

               ViewDialog_Reprint alert = new ViewDialog_Reprint();
                alert.showDialog(ReprintActivity.this , header_reprintList , itemsOrderDataDBDetailslist
                ,binding.editTrackingnumber.getText().toString()
                                .substring(0,binding.editTrackingnumber.getText().toString().indexOf("-")),
                        binding.editTrackingnumber.getText().toString()
                );

            }
        });
        reprintAWBViewModel.mutableLiveDataError.observe(ReprintActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ReprintActivity.this, ""+s, Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnPrintAwb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editTrackingnumber.getText().toString().isEmpty() &&
                        binding.editTrackingnumber.getText().toString().contains("-")) {
                    
                    reprintAWBViewModel.fetchdata(binding.editTrackingnumber.getText().toString()
                                    .substring(0,binding.editTrackingnumber.getText().toString().indexOf("-")),
                            binding.editTrackingnumber.getText().toString()
                            );
               
                }else {
                    if (binding.editTrackingnumber.getText().toString().isEmpty()) {
                        binding.editTrackingnumber.setError(getString(R.string.enter));
                        binding.editTrackingnumber.requestFocus();
                    }else if (!binding.editTrackingnumber.getText().toString().contains("-")){
                        binding.editTrackingnumber.setError(getString(R.string.enter_valid_tracking_number));
                        binding.editTrackingnumber.requestFocus();
                    }
                }
            }
        });

    }
}