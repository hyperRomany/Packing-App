package com.example.packingapp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packingapp.Adapter.ItemAdapter;
import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.Helper.Constant;
import com.example.packingapp.Helper.ItemclickforRecycler;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityAssignItemsToPackageBinding;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.GetOrderResponse.OrderDataModuleDBHeader;
import com.example.packingapp.model.Product;
import com.example.packingapp.model.TrackingnumbersListDB;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AssignItemToPackagesActivity extends AppCompatActivity {
    Context context=AssignItemToPackagesActivity.this;
    private static final String TAG = "ItemActivity";
    ActivityAssignItemsToPackageBinding binding;
    ItemAdapter itemAdapter;
    Product product;
    AppDatabase database;
    String AddNewPackageORAddForExistPackage="" ,OrderNumber="";
    List<String> ListOfBarcodesToAssign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignItemsToPackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=AppDatabase.getDatabaseInstance(this);

        ListOfBarcodesToAssign=new ArrayList<>();
        if (getIntent().getExtras() !=null) {
            AddNewPackageORAddForExistPackage = getIntent().getExtras().getString("AddNewPackageORAddForExistPackage");
            OrderNumber=getIntent().getExtras().getString("OrderNumber");
        }else {
            Toast.makeText(this, "AddNewPackageORAddForExistPackage is null", Toast.LENGTH_SHORT).show();
            OrderNumber = "000001101";
            AddNewPackageORAddForExistPackage ="New";
        }

       CreateORUpdateRecycleView();

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SearchOfBarcode();
            }
        });

        binding.editBarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER){
                    SearchOfBarcode();
                }
                return false;
            }
        });


        binding.btnAssignItemsToPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List <ItemsOrderDataDBDetails_Scanned> Adapterlist = itemAdapter.ReturnListOfAdapter();

                if (Adapterlist.size() >0 && ListOfBarcodesToAssign.size() >0) {
                    if (AddNewPackageORAddForExistPackage.equalsIgnoreCase("New")) {
//                    Log.e(TAG, "onClick: " + database.userDao().getLastTrackingnumber().getTrackingNumber());
                        if (database.userDao().getLastTrackingnumber(OrderNumber) == null) {
                            Log.e(TAG, "onClick:ordernum  " + OrderNumber + "-01");
                            String Trackingnumber = OrderNumber + "-01";
                            database.userDao().Insertrackingnumber(new TrackingnumbersListDB(OrderNumber, OrderNumber + "-01"));
                          //  database.userDao().updatetrackingnumberforListOfItems(Trackingnumber, ListOfBarcodesToAssign);
                            for (int i=0;i<Adapterlist.size();i++) {
                                float price=Adapterlist.get(i).getPrice()*Adapterlist.get(i).getQuantity();
                                Log.e(TAG, "onClick:price "+price );
                                price =Float.valueOf(new DecimalFormat("##0.000").format(price));
                                Log.e(TAG, "onClick:price "+price );
                                Log.e(TAG, "onClick:getQuantity "+Adapterlist.get(i).getQuantity() );

                                float QTY=0;
                                QTY=Float.valueOf(new DecimalFormat("##0.000").format(Adapterlist.get(i).getQuantity()));

                                ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                        = new ItemsOrderDataDBDetails_Scanned(OrderNumber,Adapterlist.get(i).getName(),price,
                                        QTY,Adapterlist.get(i).getSku(),Adapterlist.get(i).getUnite(),Trackingnumber);
                                Log.e(TAG, "onClick:insert getScanned_quantity1: "+Adapterlist.get(i).getQuantity() );

                                database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);
                            }
                        } else {
                            String LastTrackingNumber = database.userDao().getLastTrackingnumber(OrderNumber).getTrackingNumber();
                            Log.e(TAG, "onClick:else " + LastTrackingNumber.substring(LastTrackingNumber.indexOf("-") + 1));
                            int num = Integer.valueOf(LastTrackingNumber.substring(LastTrackingNumber.indexOf("-") + 1));
                            ++num;
                            Log.e(TAG, "onClick:else+1  " + num);
                            String Trackingnumber;
                            if (num < 10) {
                                Trackingnumber = OrderNumber + "-0" + num;
                                database.userDao().Insertrackingnumber(new TrackingnumbersListDB(OrderNumber, Trackingnumber));
//                                database.userDao().updatetrackingnumberforListOfItems(Trackingnumber, ListOfBarcodesToAssign);
                                for (int i=0;i<Adapterlist.size();i++) {
                                    float price=Adapterlist.get(i).getPrice()*Adapterlist.get(i).getQuantity();
                                    price=Float.valueOf(new DecimalFormat("##0.000").format(price));

                                    float QTY=0;
                                    QTY=Float.valueOf(new DecimalFormat("##0.000").format(Adapterlist.get(i).getQuantity()));

                                    ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                            = new ItemsOrderDataDBDetails_Scanned(OrderNumber,
                                            Adapterlist.get(i).getName(),
                                            price,
                                            QTY,
                                            Adapterlist.get(i).getSku(),
                                            Adapterlist.get(i).getUnite(),Trackingnumber);
                                    Log.e(TAG, "onClick:insert getScanned_quantity2: "+Adapterlist.get(i).getQuantity() );

                                    database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);

                                }
                            } else {
                                Trackingnumber = OrderNumber + "-" + num;
                                database.userDao().Insertrackingnumber(new TrackingnumbersListDB(OrderNumber, Trackingnumber));
//                                database.userDao().updatetrackingnumberforListOfItems(Trackingnumber, ListOfBarcodesToAssign);
                                for (int i=0;i<Adapterlist.size();i++) {
                                    float price=Adapterlist.get(i).getPrice()*Adapterlist.get(i).getQuantity();
                                    price=Float.valueOf(new DecimalFormat("##0.000").format(price));

                                    float QTY=0;
                                    QTY=Float.valueOf(new DecimalFormat("##0.000").format(Adapterlist.get(i).getQuantity()));

                                    ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                            = new ItemsOrderDataDBDetails_Scanned(OrderNumber,Adapterlist.get(i).getName(),price,
                                            QTY ,Adapterlist.get(i).getSku(),Adapterlist.get(i).getUnite(),Trackingnumber);
                                    Log.e(TAG, "onClick:insert getScanned_quantity3: "+Adapterlist.get(i).getQuantity() );

                                    database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);
                                }
                            }
                            Log.e(TAG, "onClick:Gen  " + Trackingnumber);

                        }
                        //ToDo Clear all lists after assign
                        Constant.ToastDialoge(getResources().getString(R.string.Added) , AssignItemToPackagesActivity.this);

                        itemAdapter.ClearRVAfterAssign();
                        ListOfBarcodesToAssign.clear();
                    }else {

                        for (int i=0;i<Adapterlist.size();i++) {
                            List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList_scanned =new ArrayList<>();
                            itemsOrderDataDBDetailsList_scanned = database.userDao().getItem_scanned(OrderNumber ,Adapterlist.get(i).getSku());
                            if (itemsOrderDataDBDetailsList_scanned.size() ==0 ) {
                                Log.e(TAG, "onClick: insert "+ Adapterlist.get(i).getSku() );
                                float price=Adapterlist.get(i).getPrice()*Adapterlist.get(i).getQuantity();
                                price=Float.valueOf(new DecimalFormat("##0.000").format(price));
                                float QTY=0;
                                QTY=Float.valueOf(new DecimalFormat("##0.000").format(Adapterlist.get(i).getQuantity()));
                                ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                        = new ItemsOrderDataDBDetails_Scanned(OrderNumber, Adapterlist.get(i).getName(), price,
                                        QTY , Adapterlist.get(i).getSku(), Adapterlist.get(i).getUnite(),
                                        AddNewPackageORAddForExistPackage);
                                Log.e(TAG, "onClick:insert getScanned_quantity1: " + Adapterlist.get(i).getQuantity());

                                database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);
                            }else {
                                Log.e(TAG, "onClick: update "+ Adapterlist.get(i).getSku() );
                                float price_forOneQTY=itemsOrderDataDBDetailsList_scanned.get(0).getPrice()/
                                        ( itemsOrderDataDBDetailsList_scanned.get(0).getQuantity() );
                                Log.e(TAG, "ForSearch: priceforoneQTY"+price_forOneQTY );

                                float QTY=(itemsOrderDataDBDetailsList_scanned.get(0).getQuantity()
                                        + Adapterlist.get(i).getQuantity());
                                Log.e(TAG, "ForSearch: totalQTY"+QTY );

                                float price=price_forOneQTY * (QTY);
                                Log.e(TAG, "ForSearch: priceforQTY"+price );
                                price=Float.valueOf(new DecimalFormat("##0.000").format(price));
                                QTY=Float.valueOf(new DecimalFormat("##0.000").format(QTY));

                                database.userDao().updateQTY_PriceforListOfItems(OrderNumber , AddNewPackageORAddForExistPackage ,
                                        QTY , price, Adapterlist.get(i).getSku() );

                                database.userDao().updateQTYforListOfItems(OrderNumber , AddNewPackageORAddForExistPackage ,
                                        (itemsOrderDataDBDetailsList_scanned.get(0).getQuantity()
                                        + Adapterlist.get(i).getQuantity()) , Adapterlist.get(i).getSku() );


                            }
                        }

                        Toast.makeText(AssignItemToPackagesActivity.this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
//                        Constant.ToastDialoge(getResources().getString(R.string.done) , AssignItemToPackagesActivity.this);

//                        database.userDao().updatetrackingnumberforListOfItems(AddNewPackageORAddForExistPackage, ListOfBarcodesToAssign);

                        //ToDo Clear all lists after assign
                        Intent GoBackToEditItemsOfPackage=new Intent(AssignItemToPackagesActivity.this,EditPackageItemsActivity.class);
                        GoBackToEditItemsOfPackage.putExtra("TrackingNumber",AddNewPackageORAddForExistPackage);
                        startActivity(GoBackToEditItemsOfPackage);
                        finish();
                    }
                }else {
                    Toast.makeText(AssignItemToPackagesActivity.this, getResources().getString(R.string.notadd), Toast.LENGTH_SHORT).show();
//                    Constant.ToastDialoge(getResources().getString(R.string.notadd) , AssignItemToPackagesActivity.this);

                }
                binding.editBarcode.requestFocus();
            }
        });
    }

    public void CreateORUpdateRecycleView(){

        itemAdapter = new ItemAdapter();
        binding.recyclerView.setAdapter(itemAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemclickforRecycler.addTo(binding.recyclerView).setOnItemClickListener(new ItemclickforRecycler.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                EnterQTY(itemAdapter.ReturnListOfAdapter().get(position).getSku());
            }
        });
    }


    private void SearchOfBarcode() {
        float adapterQTY=0,QTY=0;
        int postion=-1;
        if (!binding.editBarcode.getText().toString().isEmpty()) {
            List<ItemsOrderDataDBDetails> itemsOrderDataDBDetailsList =new ArrayList<>();
            String KQTY,GQTY,TotalQTYFor23 , BarcodeFor23  , Depart;
            KQTY="00";
            GQTY="000";
            TotalQTYFor23="";
            BarcodeFor23="";
            Depart=binding.editBarcode.getText().toString().substring(0,2);
            if (Depart.equalsIgnoreCase("23")
                    && binding.editBarcode.getText().toString().length() ==13) {

                KQTY = binding.editBarcode.getText().toString().substring(7, 9);
                GQTY = binding.editBarcode.getText().toString().substring(9, 12);
                if (Depart.equalsIgnoreCase("23") && Double.valueOf(KQTY+GQTY) <10){
                    binding.editBarcode.setError("???? ?????????? ???????? ?????? ???? 10 ????????");
                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();
                }else {
                    TotalQTYFor23 = KQTY + "." + GQTY;
                    //BarcodeFor23 = et_Barcode.getText().toString().replace(TotalQTYFor23.replace(".", ""), "00000");
                    BarcodeFor23 = binding.editBarcode.getText().toString().substring(0, 7);
                    itemsOrderDataDBDetailsList = database.userDao().getItem_scales(OrderNumber , BarcodeFor23 + "%");
                    Calculatcheckdigitforscales(binding.editBarcode.getText().toString().substring(0, 7) + "00000");
                    //    itemsOrderDataDBDetailsList = database.userDao().getItem(OrderNumber , binding.editBarcode.getText().toString());


                if (itemsOrderDataDBDetailsList.size() > 0) {

                    List<ItemsOrderDataDBDetails_Scanned> Adapterlist = itemAdapter.ReturnListOfAdapter();
                    List<String> listClone = new ArrayList<>();
                    //TODO Hint>>> listClone is take barcode and store it in it for if it added it before in adpter list
                    // for (ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetailsSE_sca_adap : Adapterlist) {

                    List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList_scanned =new ArrayList<>();
                    itemsOrderDataDBDetailsList_scanned = database.userDao().getItem_scanned_scales(OrderNumber , BarcodeFor23 + "%");
                    float SumofScannedqty= database.userDao().getSumofScannedqty_scales(OrderNumber ,BarcodeFor23 + "%" );
                    float TotalQTY_ALL=0f;
                    for (int i=0 ;i<Adapterlist.size() ;i++){
                        //matches("(?i)"+binding.editBarcode.getText().toString()+".*")
                        if (Adapterlist.get(i).getSku().toString().substring(0, 7)
                                .equalsIgnoreCase(binding.editBarcode.getText().toString() .substring(0, 7))) {
                            listClone.add(Adapterlist.get(i).getSku());
                            adapterQTY=Adapterlist.get(i).getQuantity();
                            Log.e(TAG, "SearchOfBarcode:sca_adap "+Adapterlist.get(i).getSku() );
                            Log.e(TAG, "SearchOfBarcode:adapterQTY "+adapterQTY );
                            // delete it from here
//                        postion=i;
                            TotalQTY_ALL=Float.valueOf(SumofScannedqty+adapterQTY +Float.valueOf(TotalQTYFor23));
                            TotalQTY_ALL=Float.valueOf(new DecimalFormat("##0.000").format(TotalQTY_ALL));
                            Log.e(TAG, "SearchOfBarcode:summ+++TotalQTY_ALL  "+TotalQTY_ALL );

                            if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= TotalQTY_ALL){
                                Adapterlist.remove(i);
                            }

                        }
                    }
//                    Log.e(TAG, "SearchOfBarcode:TotalQTYFor23Doublesc " + Double.valueOf(TotalQTYFor23));
                    Log.e(TAG, "SearchOfBarcode:TotalQTYFor23Float " + Float.valueOf(TotalQTYFor23));
                    Log.e(TAG, "SearchOfBarcode:getQuantity " + itemsOrderDataDBDetailsList.get(0).getQuantity());
                    Log.e(TAG, "SearchOfBarcode:adapterQTY " + adapterQTY);
                    Log.e(TAG, "SearchOfBarcode:summ "+SumofScannedqty );
                    Log.e(TAG, "SearchOfBarcode:summ+++  "+(SumofScannedqty+adapterQTY +Double.valueOf(TotalQTYFor23) ));
                    Log.e(TAG, "SearchOfBarcode:summ+++TotalQTY_ALL  "+TotalQTY_ALL);
                    Log.e(TAG, "SearchOfBarcode:summ+++getQuantity()  "+itemsOrderDataDBDetailsList.get(0).getQuantity());

                    TotalQTY_ALL=Float.valueOf(SumofScannedqty+adapterQTY +Float.valueOf(TotalQTYFor23));
                    TotalQTY_ALL=Float.valueOf(new DecimalFormat("##0.000").format(TotalQTY_ALL));
                    Log.e(TAG, "SearchOfBarcode:summ+++TotalQTY_ALL_ag  "+TotalQTY_ALL);


                    //  if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= (Double.valueOf(TotalQTYFor23))) {
                    //TODO for scales i think that there is will be issue for = this man that it can add more than for second time
                        if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= TotalQTY_ALL) {

                            float price=0;
                            QTY = adapterQTY+ Float.valueOf(TotalQTYFor23);  //.

                            OrderDataModuleDBHeader orderDataModuleDBHeader = database.userDao().getordernumberData(OrderNumber);

                            float SumOfQTY = database.userDao().SumOfQTYFromDetials(OrderNumber);
                            Log.e(TAG, "UploadDetails:SumOfQTY " + SumOfQTY);
                            float Shippingfees = orderDataModuleDBHeader.getShipping_fees();
                            Log.e(TAG, "UploadDetails:Shippingfees " + Shippingfees);
                            float ShippingfeesPerItem = Shippingfees / SumOfQTY;
                            Log.e(TAG, "UploadDetails:ShippingfeesPerItem " + ShippingfeesPerItem);
                            Log.e(TAG, "UploadDetails:ShippingfeesPerItem " + itemsOrderDataDBDetailsList.get(0).getQuantity());
                            Log.e(TAG, "UploadDetails:ShippingfeesPerItem " + (itemsOrderDataDBDetailsList.get(0).getPrice()/itemsOrderDataDBDetailsList.get(0).getQuantity()));

                            price=(itemsOrderDataDBDetailsList.get(0).getPrice()/itemsOrderDataDBDetailsList.get(0).getQuantity())+ShippingfeesPerItem;
                            price=Float.valueOf(new DecimalFormat("##0.000").format(price));
                            QTY=Float.valueOf(new DecimalFormat("##0.000").format(QTY));
                            Log.e(TAG, "SearchOfBarcode:QTY " + QTY);
                            Log.e(TAG, "SearchOfBarcode:price " + price);

                            ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                    = new ItemsOrderDataDBDetails_Scanned(OrderNumber, itemsOrderDataDBDetailsList.get(0).getName(),
                                    price,
                                    QTY, itemsOrderDataDBDetailsList.get(0).getSku(), itemsOrderDataDBDetailsList.get(0).getUnite());

                            itemAdapter.fillAdapterData(itemsOrderDataDBDetails_scanned);

                            ListOfBarcodesToAssign.add(binding.editBarcode.getText().toString());
                            //   }
                            //   QTY= itemsOrderDataDBDetailsList.get(0).getQuantity()+1 ;
                            binding.editBarcode.setText("");
                            binding.editBarcode.requestFocus();
                        } else if (itemsOrderDataDBDetailsList.get(0).getQuantity() < Float.valueOf(SumofScannedqty+adapterQTY +Float.valueOf(TotalQTYFor23))
                      //  || itemsOrderDataDBDetailsList.get(0).getQuantity() != (SumofScannedqty+adapterQTY +Double.valueOf(TotalQTYFor23))
                        ) {
                            binding.editBarcode.setError("???? ?????????? ???????????? ???????????????? ???? ?????????? ???? ??????????????");
                            binding.editBarcode.setText("");
                            binding.editBarcode.requestFocus();
                            Log.e(TAG, "SearchOfBarcode: setError : this is more than required ");
                        }
                    } else {
                        Toast.makeText(AssignItemToPackagesActivity.this, getResources().getString(R.string.enter_valid_barcode), Toast.LENGTH_SHORT).show();
                        binding.editBarcode.setError(getResources().getString(R.string.enter_valid_barcode));
                        binding.editBarcode.setText("");
                        binding.editBarcode.requestFocus();
                    }
                }
            }else {
            //TODO need to add search with order nuber for if there is repeated barcode in orders (pending orders)
                itemsOrderDataDBDetailsList = database.userDao().getItem(OrderNumber , binding.editBarcode.getText().toString());
//                    product = new Product("laptop", "5");
                Log.e(TAG, "SearchOfBarcode:OrderNumber "+OrderNumber );
                Log.e(TAG, "SearchOfBarcode:binding.editBarcode.getText().toString() "+binding.editBarcode.getText().toString() );
            if (itemsOrderDataDBDetailsList.size() >0 ) {
                List<ItemsOrderDataDBDetails_Scanned> Adapterlist = itemAdapter.ReturnListOfAdapter();
                List<String> listClone = new ArrayList<>();
                //TODO Hint>>> listClone is take barcode and store it in it for if it added it before in adpter list
                // for (ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetailsSE_sca_adap : Adapterlist) {

                List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList_scanned =new ArrayList<>();
                itemsOrderDataDBDetailsList_scanned = database.userDao().getItem_scanned(OrderNumber , binding.editBarcode.getText().toString());
                float SumofScannedqty= database.userDao().getSumofScannedqty(OrderNumber , binding.editBarcode.getText().toString());

                for (int i=0 ;i<Adapterlist.size() ;i++){
                    //matches("(?i)"+binding.editBarcode.getText().toString()+".*")
                    if (Adapterlist.get(i).getSku().toString().equalsIgnoreCase(binding.editBarcode.getText().toString())) {
                        listClone.add(Adapterlist.get(i).getSku());
                        adapterQTY=Adapterlist.get(i).getQuantity();
                        Log.e(TAG, "SearchOfBarcode:sca_adap "+Adapterlist.get(i).getSku() );
                        Log.e(TAG, "SearchOfBarcode:adapterQTY "+adapterQTY );
                        // delete it from here
//                        postion=i;
                        if (itemsOrderDataDBDetailsList.get(0).getQuantity() > (SumofScannedqty+adapterQTY)){
                            Adapterlist.remove(i);
                        }

                    }
                }
               /* if (itemsOrderDataDBDetailsList_scanned.size() ==0){
                    QTY=1;
                }else*/
               // if (itemsOrderDataDBDetailsList_scanned.size() >0){
                Log.e(TAG, "SearchOfBarcode:getQuantity "+itemsOrderDataDBDetailsList.get(0).getQuantity() );
                Log.e(TAG, "SearchOfBarcode:SumofScannedqty "+SumofScannedqty );
                    if (itemsOrderDataDBDetailsList.get(0).getQuantity() > (SumofScannedqty+adapterQTY)){
                       // if (adapterQTY>0){
//                          QTY=adapterQTY+0.2f;
                        float price=0;


                        QTY=adapterQTY+1;

                      //  if (adapterQTY == 0){
                            OrderDataModuleDBHeader orderDataModuleDBHeader = database.userDao().getordernumberData(OrderNumber);

                            float SumOfQTY = database.userDao().SumOfQTYFromDetials(OrderNumber);
                            Log.e(TAG, "UploadDetails:SumOfQTY " + SumOfQTY);
                            float Shippingfees = orderDataModuleDBHeader.getShipping_fees();
                            Log.e(TAG, "UploadDetails:Shippingfees " + Shippingfees);
                            float ShippingfeesPerItem = Shippingfees / SumOfQTY;
                            Log.e(TAG, "UploadDetails:ShippingfeesPerItem " + ShippingfeesPerItem);

                            price=(itemsOrderDataDBDetailsList.get(0).getPrice()/itemsOrderDataDBDetailsList.get(0).getQuantity())+ShippingfeesPerItem;
                      //  }
                        Log.e(TAG, "SearchOfBarcode:price for item "+price );
                        price=Float.valueOf(new DecimalFormat("##0.000").format(price));
                        QTY=Float.valueOf(new DecimalFormat("##0.000").format(QTY));
                        ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                = new ItemsOrderDataDBDetails_Scanned(OrderNumber,itemsOrderDataDBDetailsList.get(0).getName(),
                                price,
                                QTY,itemsOrderDataDBDetailsList.get(0).getSku(),itemsOrderDataDBDetailsList.get(0).getUnite());

                        Log.e(TAG, "SearchOfBarcode:QTY "+QTY );
//                        if (Adapterlist.size()>0) {
//                            Adapterlist.remove(postion);
//                        }
                        itemAdapter.fillAdapterData(itemsOrderDataDBDetails_scanned);

                        ListOfBarcodesToAssign.add(binding.editBarcode.getText().toString());
                     //   }
                     //   QTY= itemsOrderDataDBDetailsList.get(0).getQuantity()+1 ;
                        binding.editBarcode.setText("");
                        binding.editBarcode.requestFocus();
                    }else if (itemsOrderDataDBDetailsList.get(0).getQuantity() <= (SumofScannedqty+adapterQTY)){
                        binding.editBarcode.setError("???? ?????????? ???????????? ????????????????");
                        binding.editBarcode.setText("");
                        binding.editBarcode.requestFocus();
                        Log.e(TAG, "SearchOfBarcode: setError : this is more than required " );
                    }
               // }
                        /* ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                = new ItemsOrderDataDBDetails_Scanned(OrderNumber,Adapterlist.get(0).getName(),Adapterlist.get(0).getPrice(),
                                Adapterlist.get(0).getQuantity(),Adapterlist.get(0).getSku(),Adapterlist.get(0).getUnite(),ListOfBarcodesToAssign.get(0));
                        database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);*/
//                binding.editBarcode.setText("");
//                binding.editBarcode.requestFocus();

                //Log.e(TAG, "onClick: ", "" + binding.editBarcode.getText().toString() );
                /*if (listClone.size() == 0 && itemsOrderDataDBDetailsList.get(0).getTrackingNumber() ==null ) {
                    itemAdapter.fillAdapterData(itemsOrderDataDBDetailsList.get(0));
                    ListOfBarcodesToAssign.add(binding.editBarcode.getText().toString());

                    ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                            = new ItemsOrderDataDBDetails_Scanned(OrderNumber,Adapterlist.get(0).getName(),Adapterlist.get(0).getPrice(),
                            Adapterlist.get(0).getQuantity(),Adapterlist.get(0).getSku(),Adapterlist.get(0).getUnite(),ListOfBarcodesToAssign.get(0));
                    database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);


                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();
                } else {
                    binding.editBarcode.setError(getResources().getString(R.string.enterbefor));
                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();

                }*/

            }else {

                Toast.makeText(AssignItemToPackagesActivity.this, getResources().getString(R.string.enter_valid_barcode), Toast.LENGTH_SHORT).show();
                binding.editBarcode.setError(getResources().getString(R.string.enter_valid_barcode));
                binding.editBarcode.setText("");
                binding.editBarcode.requestFocus();
            }
            binding.editBarcode.requestFocus();
            }
        } else {
            binding.editBarcode.setError(getResources().getString(R.string.enter_barcode));
            binding.editBarcode.requestFocus();
        }
        binding.editBarcode.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        itemAdapter.clearAdapterData();
    }

    public void EnterQTY(String barcode){

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts_enter_qty, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        final TextView txt_barcode = (TextView) promptsView
                .findViewById(R.id.txt_barcode);
        txt_barcode.setText(barcode);

        final EditText edit_qty = (EditText) promptsView
                .findViewById(R.id.edit_qty);

        final Button btn_save_qty = (Button) promptsView
                .findViewById(R.id.btn_save_qty);

        btn_save_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // promptsView.

                if (!edit_qty.getText().toString().isEmpty() &&
                        Double.valueOf(edit_qty.getText().toString())>0 ) {

                    if (!barcode.substring(0,2).equalsIgnoreCase("23")) {
                            ForSearch(barcode,
                                    Float.valueOf(
                                            edit_qty.getText().toString()
                                    ));
                    }else {
                        Toast.makeText(context, getResources().getString(R.string.this_is_weight_Item_cant_edit), Toast.LENGTH_SHORT).show();
//                        Constant.ToastDialoge(getResources().getString(R.string.this_is_weight_Item_cant_edit) , AssignItemToPackagesActivity.this);

                    }
                    alertDialog.dismiss();

                }else{
                    if (edit_qty.getText().toString().isEmpty()){
                        edit_qty.setError(getResources().getString(R.string.enter_qyt));
                    }else if (Double.valueOf(edit_qty.getText().toString())<=0){
                        edit_qty.setError(getResources().getString(R.string.addqtymorethanzero));
                    }
                }
            }

        });
        // show it
        alertDialog.show();

    }

    private void ForSearch(String Barcode, float Qty_add){
        float adapterQTY=0,QTY=0;
        List<ItemsOrderDataDBDetails> itemsOrderDataDBDetailsList =new ArrayList<>();
        String KQTY,GQTY,TotalQTYFor23 , BarcodeFor23  , Depart;
        KQTY="00";
        GQTY="000";
        TotalQTYFor23="";
        BarcodeFor23="";
        Depart=Barcode.substring(0,2);
        if (Depart.equalsIgnoreCase("23")
                && Barcode.length() ==13) {
            /*
            KQTY = Barcode.substring(7, 9);
            GQTY = Barcode.substring(9, 12);
            if (Depart.equalsIgnoreCase("23") && Double.valueOf(KQTY+GQTY) <10){
                binding.editBarcode.setError("???? ?????????? ???????? ?????? ???? 10 ????????");
                binding.editBarcode.setText("");
                binding.editBarcode.requestFocus();
            }else {
                TotalQTYFor23 = KQTY + "." + GQTY;
                //BarcodeFor23 = et_Barcode.getText().toString().replace(TotalQTYFor23.replace(".", ""), "00000");
                BarcodeFor23 = Barcode.substring(0, 7);
                itemsOrderDataDBDetailsList = database.userDao().getItem_scales(BarcodeFor23 + "%");
                Calculatcheckdigitforscales(Barcode.substring(0, 7) + "00000");
                //    itemsOrderDataDBDetailsList = database.userDao().getItem(OrderNumber , Barcode);
                Log.e(TAG, "SearchOfBarcode:TotalQTYFor23 " + Double.valueOf(TotalQTYFor23));

                if (itemsOrderDataDBDetailsList.size() > 0) {

                    List<ItemsOrderDataDBDetails_Scanned> Adapterlist = itemAdapter.ReturnListOfAdapter();
                    List<String> listClone = new ArrayList<>();
                    //TODO Hint>>> listClone is take barcode and store it in it for if it added it before in adpter list
                    // for (ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetailsSE_sca_adap : Adapterlist) {

                    List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList_scanned =new ArrayList<>();
                    itemsOrderDataDBDetailsList_scanned = database.userDao().getItem_scanned(Barcode);
                    float SumofScannedqty= database.userDao().getSumofScannedqty(Barcode);

                    for (int i=0 ;i<Adapterlist.size() ;i++){
                        //matches("(?i)"+Barcode+".*")
                        if (Adapterlist.get(i).getSku().toString().equalsIgnoreCase(Barcode)) {
                            listClone.add(Adapterlist.get(i).getSku());
                            adapterQTY=Adapterlist.get(i).getQuantity();
                            Log.e(TAG, "SearchOfBarcode:sca_adap "+Adapterlist.get(i).getSku() );
                            Log.e(TAG, "SearchOfBarcode:adapterQTY "+adapterQTY );
                            // delete it from here
//                        postion=i;
                            if (itemsOrderDataDBDetailsList.get(0).getQuantity() > (SumofScannedqty+adapterQTY)){
                                Adapterlist.remove(i);
                            }

                        }
                    }


                  //  if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= (Double.valueOf(TotalQTYFor23))) {
                    if (itemsOrderDataDBDetailsList.get(0).getQuantity() > (SumofScannedqty+adapterQTY +Double.valueOf(TotalQTYFor23) )){

                        QTY = Float.valueOf(TotalQTYFor23);
                        Log.e(TAG, "SearchOfBarcode:QTY " + QTY);
                        ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                = new ItemsOrderDataDBDetails_Scanned(OrderNumber, itemsOrderDataDBDetailsList.get(0).getName(),
                                itemsOrderDataDBDetailsList.get(0).getPrice(),
                                QTY, itemsOrderDataDBDetailsList.get(0).getSku(), itemsOrderDataDBDetailsList.get(0).getUnite());

                        itemAdapter.fillAdapterData(itemsOrderDataDBDetails_scanned);

                        ListOfBarcodesToAssign.add(Barcode);
                        //   }
                        //   QTY= itemsOrderDataDBDetailsList.get(0).getQuantity()+1 ;
                        binding.editBarcode.setText("");
                        binding.editBarcode.requestFocus();
                    } else if (itemsOrderDataDBDetailsList.get(0).getQuantity() <= (SumofScannedqty+adapterQTY +Double.valueOf(TotalQTYFor23))) {
                        binding.editBarcode.setError("???? ?????????? ???????????? ?????????????? ???? ?????????? ???? ??????????????");
                        binding.editBarcode.setText("");
                        binding.editBarcode.requestFocus();
                        Log.e(TAG, "SearchOfBarcode: setError : this is more than required ");
                    }
                } else {
                    Toast.makeText(AssignItemToPackagesActivity.this, getResources().getString(R.string.enter_valid_barcode), Toast.LENGTH_SHORT).show();
                    binding.editBarcode.setError(getResources().getString(R.string.enter_valid_barcode));
                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();
                }
            }
        */
            Toast.makeText(context, "?????? ?????????? ??????????", Toast.LENGTH_SHORT).show();
        }else {
            //TODO need to add search with order nuber for if there is repeated barcode in orders (pending orders)
            itemsOrderDataDBDetailsList = database.userDao().getItem(OrderNumber , Barcode);
//                    product = new Product("laptop", "5");

            if (itemsOrderDataDBDetailsList.size() >0 ) {
                List<ItemsOrderDataDBDetails_Scanned> Adapterlist = itemAdapter.ReturnListOfAdapter();
                List<String> listClone = new ArrayList<>();
                //TODO Hint>>> listClone is take barcode and store it in it for if it added it before in adpter list
                // for (ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetailsSE_sca_adap : Adapterlist) {

                List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList_scanned =new ArrayList<>();
                itemsOrderDataDBDetailsList_scanned = database.userDao().getItem_scanned(OrderNumber ,Barcode);
                float SumofScannedqty= database.userDao().getSumofScannedqty(OrderNumber , Barcode);

                for (int i=0 ;i<Adapterlist.size() ;i++){
                    //matches("(?i)"+Barcode+".*")
                    if (Adapterlist.get(i).getSku().toString().equalsIgnoreCase(Barcode)) {
                        listClone.add(Adapterlist.get(i).getSku());
                        adapterQTY=Adapterlist.get(i).getQuantity();
                        Log.e(TAG, "SearchOfBarcode:sca_adap "+Adapterlist.get(i).getSku() );
                        Log.e(TAG, "SearchOfBarcode:adapterQTY "+adapterQTY );
                        // delete it from here
//                        postion=i;
                        if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= (SumofScannedqty+Qty_add)){
                            Adapterlist.remove(i);
                        }

                    }
                }

               /* if (itemsOrderDataDBDetailsList_scanned.size() ==0){
                    QTY=1;
                }else*/
                // if (itemsOrderDataDBDetailsList_scanned.size() >0){
                Log.e(TAG, "SearchOfBarcode:getQuantity "+itemsOrderDataDBDetailsList.get(0).getQuantity() );
                Log.e(TAG, "SearchOfBarcode:SumofScannedqty "+SumofScannedqty );
                if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= (SumofScannedqty+Qty_add)){
                    // if (adapterQTY>0){
//                          QTY=adapterQTY+0.2f;
                    //TODO
//                    QTY=adapterQTY+Qty_add;
                    QTY=Qty_add;
                    float price=0f;
                    OrderDataModuleDBHeader orderDataModuleDBHeader = database.userDao().getordernumberData(OrderNumber);

                    float SumOfQTY = database.userDao().SumOfQTYFromDetials(OrderNumber);
                    Log.e(TAG, "UploadDetails:SumOfQTY " + SumOfQTY);
                    float Shippingfees = orderDataModuleDBHeader.getShipping_fees();
                    Log.e(TAG, "UploadDetails:Shippingfees " + Shippingfees);
                    float ShippingfeesPerItem = Shippingfees / SumOfQTY;
                    Log.e(TAG, "UploadDetails:ShippingfeesPerItem " + ShippingfeesPerItem);

                    price=(itemsOrderDataDBDetailsList.get(0).getPrice()/itemsOrderDataDBDetailsList.get(0).getQuantity())+ShippingfeesPerItem;
                    Log.e(TAG, "UploadDetails:price " + price);
                    price=Float.valueOf(new DecimalFormat("##0.000").format(price));
                    QTY=Float.valueOf(new DecimalFormat("##0.000").format(QTY));
                    ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                            = new ItemsOrderDataDBDetails_Scanned(OrderNumber,itemsOrderDataDBDetailsList.get(0).getName(),
                            price ,
                            QTY,itemsOrderDataDBDetailsList.get(0).getSku(),itemsOrderDataDBDetailsList.get(0).getUnite());
                    Log.e(TAG, "SearchOfBarcode:QTY "+QTY );
//                        if (Adapterlist.size()>0) {
//                            Adapterlist.remove(postion);
//                        }
                    itemAdapter.fillAdapterData(itemsOrderDataDBDetails_scanned);

                    ListOfBarcodesToAssign.add(Barcode);
                    //   }
                    //   QTY= itemsOrderDataDBDetailsList.get(0).getQuantity()+1 ;
                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();
                }else if (itemsOrderDataDBDetailsList.get(0).getQuantity() < (SumofScannedqty+Qty_add)){
                    binding.editBarcode.setError("???? ?????????? ???????????? ???????????????? ???? ?????????? ?????????? ???????? ???? ??????????????");
                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();
                    Log.e(TAG, "SearchOfBarcode: setError : this is more than required " );
                }
                // }
                        /* ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                                = new ItemsOrderDataDBDetails_Scanned(OrderNumber,Adapterlist.get(0).getName(),Adapterlist.get(0).getPrice(),
                                Adapterlist.get(0).getQuantity(),Adapterlist.get(0).getSku(),Adapterlist.get(0).getUnite(),ListOfBarcodesToAssign.get(0));
                        database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);*/
//                binding.editBarcode.setText("");
//                binding.editBarcode.requestFocus();

                //Log.e(TAG, "onClick: ", "" + Barcode );
                /*if (listClone.size() == 0 && itemsOrderDataDBDetailsList.get(0).getTrackingNumber() ==null ) {
                    itemAdapter.fillAdapterData(itemsOrderDataDBDetailsList.get(0));
                    ListOfBarcodesToAssign.add(Barcode);

                    ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                            = new ItemsOrderDataDBDetails_Scanned(OrderNumber,Adapterlist.get(0).getName(),Adapterlist.get(0).getPrice(),
                            Adapterlist.get(0).getQuantity(),Adapterlist.get(0).getSku(),Adapterlist.get(0).getUnite(),ListOfBarcodesToAssign.get(0));
                    database.userDao().InsertItemsDetails_scanned(itemsOrderDataDBDetails_scanned);


                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();
                } else {
                    binding.editBarcode.setError(getResources().getString(R.string.enterbefor));
                    binding.editBarcode.setText("");
                    binding.editBarcode.requestFocus();

                }*/

            }else {

                Toast.makeText(AssignItemToPackagesActivity.this, getResources().getString(R.string.enter_valid_barcode), Toast.LENGTH_SHORT).show();
                binding.editBarcode.setError(getResources().getString(R.string.enter_valid_barcode));
                binding.editBarcode.setText("");
                binding.editBarcode.requestFocus();

            }
            binding.editBarcode.requestFocus();
        }
    }

    private String Calculatcheckdigitforscales(String toString) {
        String Barcode;
        int  chkdigit;
        Log.e("zzzbarodd1 ",""+toString.charAt(1));
        Log.e("zzzbarodd1 ",""+Integer.valueOf(toString.charAt(1)));
        Log.e("zzzbarodd1.2 ",""+Integer.valueOf(toString.substring(1,2)));
        Log.e("zzzbarodd11 ",""+toString.charAt(11));
        Log.e("zzzbarodd11.12 ",""+Integer.valueOf(toString.substring(11,12)));
        int odd  = Integer.valueOf(toString.substring(1,2)) + Integer.valueOf(toString.substring(3,4)) + Integer.valueOf(toString.substring(5,6)) + Integer.valueOf(toString.substring(7,8)) + Integer.valueOf(toString.substring(9,10)) + Integer.valueOf(toString.substring(11,12));
        int eveen  = Integer.valueOf(toString.substring(0,1)) + Integer.valueOf(toString.substring(2,3)) + Integer.valueOf(toString.substring(4,5)) + Integer.valueOf(toString.substring(6,7)) + Integer.valueOf(toString.substring(8,9)) + Integer.valueOf(toString.substring(10,11));

        Log.e("zzzbarodd",""+odd);
        Log.e("zzzbareveen",""+eveen);
        if ((((odd * 3) + eveen) % 10) != 0 )
            chkdigit = 10 - (((odd * 3) + eveen) % 10) ;
        else
            chkdigit = 0 ;

        Barcode=toString +chkdigit;
        Log.e("zzzbarcode",""+Barcode);
        return Barcode;
    }

}