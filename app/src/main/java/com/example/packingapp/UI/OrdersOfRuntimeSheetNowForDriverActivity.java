package com.example.packingapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.packingapp.Adapter.DriverOrdersAdapter;
import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.Helper.Constant;
import com.example.packingapp.Helper.ItemclickforRecycler;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityOrdersOfRuntimeSheetNowForDriverBinding;
import com.example.packingapp.model.DriverModules.DriverPackages_Header_DB;
import com.example.packingapp.model.DriverModules.DriverPackages_Respones_Header_recycler;
import com.example.packingapp.model.RecordsItem;
import com.example.packingapp.viewmodel.GetDriverOrdersViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersOfRuntimeSheetNowForDriverActivity extends AppCompatActivity {
    private static final String TAG = "OrdersOfRuntimeSheetNow";
    ActivityOrdersOfRuntimeSheetNowForDriverBinding binding;
    DriverOrdersAdapter driverOrdersAdapter;
    List<DriverPackages_Header_DB> driverPackages_Header_dbList;
    GetDriverOrdersViewModel getDriverOrdersViewModel ;
    AppDatabase database;
    RecordsItem recordsItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrdersOfRuntimeSheetNowForDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CreateORUpdateRecycleView();
        getDriverOrdersViewModel= ViewModelProviders.of(this).get(GetDriverOrdersViewModel.class);

        setTitle(R.string.OrdersOfRuntimeSheetNowForDriverActivity_label);


        database=AppDatabase.getDatabaseInstance(this);
         recordsItem = database.userDao().getUserData_MU();
        Log.e(TAG, "onCreate: "+recordsItem.getUser_id() );

        GetOrdersFUN();
        getDriverOrdersViewModel.DriverOrdersReadyDataLiveData().observe(OrdersOfRuntimeSheetNowForDriverActivity.this,
                new Observer<DriverPackages_Respones_Header_recycler>() {
                    @Override
                    public void onChanged(DriverPackages_Respones_Header_recycler driverPackages_respones_Header_recycler) {

//                        DriverPackages_DB driverPackages_respones_recycler1=
//                                new  DriverPackages_DB(driverPackages_respones_recycler.getOrderNumber(),
//                                        driverPackages_respones_recycler.getCUSTOMER_PHONE());
                        database.userDao().deleteDriverPackages_Header_DB();
                        driverPackages_Header_dbList.clear();
                        database.userDao().insertDriverOrders(driverPackages_respones_Header_recycler.getRecords());
                        driverPackages_Header_dbList.addAll(driverPackages_respones_Header_recycler.getRecords());
                        driverOrdersAdapter.notifyDataSetChanged();
                    }
                });


        binding.retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrdersFUN();
            }
        });
    }

    private void GetOrdersFUN() {
        if (Constant.isOnline(OrdersOfRuntimeSheetNowForDriverActivity.this)) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            binding.linearShoworders.setVisibility(View.VISIBLE);
            binding.linearNoInternetConnection.setVisibility(View.GONE);
            getDriverOrdersViewModel.ReadDriverRunsheetOrdersData(recordsItem.getUser_id(), timeStamp, "out_for_delivery");
        }else {
            binding.linearShoworders.setVisibility(View.GONE);
            binding.linearNoInternetConnection.setVisibility(View.VISIBLE);
        }
    }

    public void CreateORUpdateRecycleView(){

        driverPackages_Header_dbList =new ArrayList<>();

     //   Po_Item_For_Recycly=database.userDao().getAllPckages();

        driverOrdersAdapter =new DriverOrdersAdapter(driverPackages_Header_dbList);
        binding.recycleItemsView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        binding.recycleItemsView.setLayoutManager(mLayoutManager);
        binding.recycleItemsView.setAdapter(driverOrdersAdapter);


        ItemclickforRecycler.addTo(binding.recycleItemsView).setOnItemClickListener(new ItemclickforRecycler.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent g = new Intent(OrdersOfRuntimeSheetNowForDriverActivity.this,
                        OrderDetails_forDriverActivity.class);
                String Orderclicked= driverPackages_Header_dbList.get(position).getOrder_number();
                Log.e(TAG, "zzonItemClicked: "+ Orderclicked);
                g.putExtra("Orderclicked",Orderclicked);
                startActivity(g);
            }
        });
    }
}