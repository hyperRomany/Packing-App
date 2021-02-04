package com.example.packingapp.UI;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.packingapp.Adapter.PackedPackageItemsAdapter;
import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityEditPackageBinding;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.PackedPackageItemsModule;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.packingapp.R.string;

public class EditPackageItemsActivity extends AppCompatActivity {
    AppDatabase database;
    List<PackedPackageItemsModule> Po_Item_For_Recycly;
    private PackedPackageItemsAdapter packedPackageItemsAdapter;
    int CountChecked;
    String BarcodeToEditORDelete;
    ActivityEditPackageBinding binding;
    String TrackingNumber="";
    String ordernumber="";
    private static final String TAG = "EditPackageItemsActivit";
    Context context=EditPackageItemsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditPackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database= AppDatabase.getDatabaseInstance(this);
        Intent getData= getIntent();
        if (getData.getExtras().getString("TrackingNumber") != null){
            TrackingNumber = getData.getExtras().getString("TrackingNumber");
            ordernumber =TrackingNumber.substring(0,TrackingNumber.indexOf("-"));
        }else {
            TrackingNumber="";
        }
        Log.e(TAG, "onCreate: "+ TrackingNumber);
        /*database.userDao().getAllItem(binding.item.getText().toString()).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ItemsOrderDataDBDetails>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<ItemsOrderDataDBDetails> orderDataModuleDBS) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });*/

        CreateORUpdateRecycleView();
    }

    public void CreateORUpdateRecycleView(){
        Po_Item_For_Recycly = new ArrayList<PackedPackageItemsModule>();

        Po_Item_For_Recycly=database.userDao().getItemsOfTrackingNumber(TrackingNumber);
        packedPackageItemsAdapter = new PackedPackageItemsAdapter(Po_Item_For_Recycly);

        binding.recycleItemsView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        binding.recycleItemsView.setLayoutManager(mLayoutManager);
        binding.recycleItemsView.setAdapter(packedPackageItemsAdapter);

    }

    public void Delete_PDNEWQTY(View view) {

        List<PackedPackageItemsModule> Barcodes_List = packedPackageItemsAdapter.ReturnListOfPackages();
        Log.e("btn_editChecked",""+Barcodes_List.size());

        CountChecked =0;
        if (Barcodes_List.size() != 0) {
            for (int i = 0; i < Barcodes_List.size(); i++) {
                Boolean Checked = Barcodes_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarcodeToEditORDelete = Barcodes_List.get(i).getSku();

                }
                if (i == (Barcodes_List.size() - 1)) {
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(EditPackageItemsActivity.this, string.you_choice_mulite_or_choice_noting, Toast.LENGTH_LONG).show();
                    } else if (CountChecked == 1) {  //&& !BarCodeChecked.isEmpty()
                        new AlertDialog.Builder(this)
                                .setTitle(getString(string.delete_dialoge))
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        int UID_to_delete =database.userDao().GetUID(ordernumber ,TrackingNumber);
                                        Log.e( TAG, "onClick: UID_to_delete1 " + UID_to_delete );

                                        int UID_to_delete2 =database.userDao().GetUID(ordernumber , TrackingNumber);
                                        Log.e( TAG, "onClick:UID_to_delete2 "+UID_to_delete2 );

                                        // TODO >>> use id instanse of using barcode because barcode can be repeat
                                        List<Integer> Uid_for_sku_to_remove =database.userDao().getuidOfskuandTrackingNumber(
                                                ordernumber , TrackingNumber , BarcodeToEditORDelete);
                                        Log.e(TAG, "onClick:sku_to_remove "+Uid_for_sku_to_remove.size() );


                                        // List<String> sku_to_remove =database.userDao().getskuOfTrackingNumber(ordernumber , TrackingNumber);
                                       // Log.e(TAG, "onClick:sku_to_remove "+sku_to_remove.size() );

                                        List<String> AfterTrackingNumberDeleted_list=database.userDao().
                                                GetTrackingNumbersAfterDeleteOne(ordernumber , TrackingNumber);
                                        Log.e(TAG, "onClick:LastTrackingNumber "+AfterTrackingNumberDeleted_list.size() );

                                        //to get tracking number after what need to delete to minus 1
                                        List<Integer> NewTrackingNumber_NUM=database.userDao().
                                                GetNewTrackingNumbersAfterDeleteOne(ordernumber , TrackingNumber);
                                        Log.e(TAG, "onClick:NewTrackingNumber_NUM "+NewTrackingNumber_NUM.size() );
                                        List<String> NewTrackingNumber=new ArrayList<>();

                                        for (int i=0;i<NewTrackingNumber_NUM.size();i++) {
                                            int num=NewTrackingNumber_NUM.get(i);
                                            if (num < 10) {
                                                String Trackingnumber = ordernumber + "-0" + num;
                                                NewTrackingNumber.add(Trackingnumber);
                                                Log.e(TAG, "onClick:NewTrackingNumber "+NewTrackingNumber.get(i));
//                                                database.userDao().Insertrackingnumber(new TrackingnumbersListDB(Trackingnumber));
//                                                database.userDao().updatetrackingnumberforListOfItems(Trackingnumber, ListOfBarcodesToAssign);
                                                database.userDao().updatetrackingnumberAfterDeleteOne_Details(ordernumber , Trackingnumber, AfterTrackingNumberDeleted_list.get(i));
                                                database.userDao().updatetrackingnumberAfterDeleteOne_ListDB(ordernumber , Trackingnumber, AfterTrackingNumberDeleted_list.get(i));
                                                Log.e(TAG, "onClick:Trackingnumber "+Trackingnumber );
                                                Log.e(TAG, "onClick:LastTrackingNumber "+AfterTrackingNumberDeleted_list.get(i) );
                                            } else {
                                                String Trackingnumber = database.userDao().getOrderNumber() + "-" + num;
                                                NewTrackingNumber.add(Trackingnumber);
                                                Log.e(TAG, "onClick:NewTrackingNumber "+NewTrackingNumber.get(i) );
                                                //TODO can we do update for list after finish using NewTrackingNumber list
                                                database.userDao().updatetrackingnumberAfterDeleteOne_Details(ordernumber , Trackingnumber, AfterTrackingNumberDeleted_list.get(i));
                                                database.userDao().updatetrackingnumberAfterDeleteOne_ListDB(ordernumber , Trackingnumber, AfterTrackingNumberDeleted_list.get(i));
                                                Log.e(TAG, "onClick:Trackingnumber "+Trackingnumber );
                                                Log.e(TAG, "onClick:LastTrackingNumber "+AfterTrackingNumberDeleted_list.get(i) );

//                                                database.userDao().Insertrackingnumber(new TrackingnumbersListDB(Trackingnumber));
//                                                database.userDao().updatetrackingnumberforListOfItems(Trackingnumber, ListOfBarcodesToAssign);
                                            }
                                        }

                                        Log.e(TAG, "onClick:UID_to_delete "+UID_to_delete );
                                        database.userDao().DeleteTrackingNumberFromtrackingtable_using_uid(ordernumber , UID_to_delete);
//                                        database.userDao().DeleteTrackingNumberFromDetailstable_using_sku(ordernumber ,  BarcodeToEditORDelete);
                                        database.userDao().DeleteTrackingNumberFromDetailstable_using_uid(ordernumber ,  Uid_for_sku_to_remove);

                                        CreateORUpdateRecycleView();
//                                      packedPackagesAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }

                }/* else
                    Toast.makeText(EditPackageItemsActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();
            */
            }
        }

    }


    public void Add_PDNEWQTY(View view) {
        Intent gotoaddItemToPackage=new Intent(EditPackageItemsActivity.this,AssignItemToPackagesActivity.class);
        gotoaddItemToPackage.putExtra("AddNewPackageORAddForExistPackage",TrackingNumber);
        gotoaddItemToPackage.putExtra("OrderNumber",ordernumber);
        startActivity(gotoaddItemToPackage);
        finish();
    }

    public void Edit_PDNEWQTY(View view) {
        List<PackedPackageItemsModule> Barcodes_List = packedPackageItemsAdapter.ReturnListOfPackages();
        Log.e("btn_editChecked",""+Barcodes_List.size());

        CountChecked =0;
        if (Barcodes_List.size() != 0) {
            for (int i = 0; i < Barcodes_List.size(); i++) {
                Boolean Checked = Barcodes_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarcodeToEditORDelete = Barcodes_List.get(i).getSku();

                }
                if (i == (Barcodes_List.size() - 1)) {
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(EditPackageItemsActivity.this, string.you_choice_mulite_or_choice_noting, Toast.LENGTH_LONG).show();
                    } else if (CountChecked == 1) {
                        EnterQTY(BarcodeToEditORDelete);
                    }

                }/* else
                    Toast.makeText(EditPackageItemsActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();
            */
            }
        }
    }

    public void EnterQTY(String barcode){

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts_enter_qty, null);


        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

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

                if (!edit_qty.getText().toString().isEmpty()) {

                    if (!barcode.substring(0,2).equalsIgnoreCase("23")) {
                        ForSearch(barcode,
                                Float.valueOf(
                                        edit_qty.getText().toString()
                                ));
                    }else {
                        Toast.makeText(context, "هذا الصنف موزون .. لايمكن تعديله", Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();

                }else{
                    if (edit_qty.getText().toString().isEmpty()){
                        edit_qty.setError(getResources().getString(R.string.enter_qyt));
                    }
                }
            }

        });
        // show it
        alertDialog.show();

    }

    private void ForSearch(String Barcode, float Qty_add){
        float QTY=0;
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
                binding.editBarcode.setError("تم إدخال قيمه أقل من 10 جرام");
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
                        binding.editBarcode.setError("تم أضافه الكميه المطلبه او زياده عن المطلوب");
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
            Toast.makeText(context, "هذا الصنف موزون", Toast.LENGTH_SHORT).show();
        }else {
            //TODO need to add search with order nuber for if there is repeated barcode in orders (pending orders)
            itemsOrderDataDBDetailsList = database.userDao().getItem(ordernumber , Barcode);
//                    product = new Product("laptop", "5");

            if (itemsOrderDataDBDetailsList.size() >0 ) {
                //TODO Hint>>> listClone is take barcode and store it in it for if it added it before in adpter list
                // for (ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetailsSE_sca_adap : Adapterlist) {

                List<ItemsOrderDataDBDetails_Scanned> itemsOrderDataDBDetailsList_scanned =new ArrayList<>();
                itemsOrderDataDBDetailsList_scanned = database.userDao().getItem_scanned(ordernumber ,Barcode);
                float SumofScannedqty= database.userDao().getSumofScannedqty(ordernumber , Barcode);

                Log.e(TAG, "SearchOfBarcode:getQuantity "+itemsOrderDataDBDetailsList.get(0).getQuantity() );
              //  Log.e(TAG, "SearchOfBarcode:SumofScannedqty "+SumofScannedqty );
                if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= (SumofScannedqty+Qty_add)){
//                if (itemsOrderDataDBDetailsList.get(0).getQuantity() >= (Qty_add)){

                    // if (adapterQTY>0){
//                          QTY=adapterQTY+0.2f;
                    QTY=Qty_add;
                    ItemsOrderDataDBDetails_Scanned itemsOrderDataDBDetails_scanned
                            = new ItemsOrderDataDBDetails_Scanned(ordernumber,itemsOrderDataDBDetailsList.get(0).getName(),
                            itemsOrderDataDBDetailsList.get(0).getPrice(),
                            QTY,itemsOrderDataDBDetailsList.get(0).getSku(),itemsOrderDataDBDetailsList.get(0).getUnite());
                    Log.e(TAG, "SearchOfBarcode:QTY "+QTY );
                    database.userDao().updateQTYforListOfItems(ordernumber , TrackingNumber ,  QTY ,itemsOrderDataDBDetailsList.get(0).getSku() );

//                    packedPackageItemsAdapter.notifyDataSetChanged();
                    CreateORUpdateRecycleView();

                }else if (itemsOrderDataDBDetailsList.get(0).getQuantity() < (SumofScannedqty+Qty_add)){
                    Toast.makeText(EditPackageItemsActivity.this, getResources().getString(R.string.enter_more_thanrequired), Toast.LENGTH_SHORT).show();
                }


            }else {

                Toast.makeText(EditPackageItemsActivity.this, getResources().getString(R.string.enter_valid_barcode), Toast.LENGTH_SHORT).show();
            }
        }
    }

}