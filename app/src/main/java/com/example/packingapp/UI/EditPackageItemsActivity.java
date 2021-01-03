package com.example.packingapp.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.packingapp.Adapter.PackedPackageItemsAdapter;
import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.databinding.ActivityEditPackageBinding;
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

                                        // List<String> sku_to_remove =database.userDao().getskuOfTrackingNumber(ordernumber , TrackingNumber);
                                       // Log.e(TAG, "onClick:sku_to_remove "+sku_to_remove.size() );

                                        List<String> AfterTrackingNumberDeleted_list=database.userDao().
                                                GetTrackingNumbersAfterDeleteOne(ordernumber , TrackingNumber);
                                        Log.e(TAG, "onClick:LastTrackingNumber "+AfterTrackingNumberDeleted_list.size() );

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

                                        database.userDao().DeleteTrackingNumberFromtrackingtable_using_uid(ordernumber , UID_to_delete);
//                                        database.userDao().DeleteTrackingNumberFromDetailstable_using_sku(ordernumber ,  BarcodeToEditORDelete);
                                        database.userDao().DeleteItemfromScanned(ordernumber , BarcodeToEditORDelete);

                                        CreateORUpdateRecycleView();
//                                      packedPackagesAdapter.notifyDataSetChanged();

                                        CreateORUpdateRecycleView();
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


    public void Edit_PDNEWQTY(View view) {
        Intent gotoaddItemToPackage=new Intent(EditPackageItemsActivity.this,AssignItemToPackagesActivity.class);
        gotoaddItemToPackage.putExtra("AddNewPackageORAddForExistPackage",TrackingNumber);
        startActivity(gotoaddItemToPackage);
        finish();
    }
}