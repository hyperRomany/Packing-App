package com.example.packingapp.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packingapp.Adapter.OrdersnumberAdapter;
import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.Helper.Constant;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityAssignPackedOrderForZoneDriverBinding;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.RecievePacked.RecievePackedModule_For_selection_loop_for_update;
import com.example.packingapp.model.RecievePacked.ResponseFetchRuntimesheetID;
import com.example.packingapp.model.RecordItemDriver;
import com.example.packingapp.model.ResponseDriver;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.model.ResponseZoneName;
import com.example.packingapp.model.TimeSheet.RecordsHeader;
import com.example.packingapp.model.TimeSheet.RecordsItem;
import com.example.packingapp.model.TimeSheet.Response;
import com.example.packingapp.viewmodel.AssignPackedOrderToZoneViewModel;
import com.onbarcode.barcode.android.AndroidColor;
import com.onbarcode.barcode.android.AndroidFont;
import com.onbarcode.barcode.android.Code93;
import com.onbarcode.barcode.android.IBarcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AssignPackedOrderForZoneAndDriverActivity extends AppCompatActivity {
    private static final String TAG = "AssignPackedOrderForZon";
    ActivityAssignPackedOrderForZoneDriverBinding binding;
    AssignPackedOrderToZoneViewModel assignPackedOrderToZoneViewModel;
    AppDatabase database;
    final Context context = this;
    String Zone_public ,trackingnumber_public;
    ArrayList<String> Drivers_IDs_list ;
    ArrayList<RecordItemDriver> Drivers_Data_list ;
    ResponseDriver responseDriver;
    ResponseZoneName responseZoneName;
    String trackingNo="";
    List<String> Response_id_for_runtimesheet_Orders;
    List<RecordsItem> Response_Recordsitems_list_for_runtimesheet_Orders;
    List<RecordsHeader> Response_RecordsHeader_list_for_runtimesheet_Orders;
   // List<Response> Response_list_for_runtimesheet_Orders;
    ArrayAdapter<String> spinnerAdapterDriver;
    ArrayAdapter<String> spinnerAdapterZones;
    ArrayList<String> zones_list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAssignPackedOrderForZoneDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=AppDatabase.getDatabaseInstance(this);

        setTitle(getResources().getString(R.string.assignpackedorderforzoneanddriver_label));
        assignPackedOrderToZoneViewModel = ViewModelProviders.of(this)
                .get(AssignPackedOrderToZoneViewModel.class);
//        Response_list_for_runtimesheet_Orders=new ArrayList<>();
        Response_id_for_runtimesheet_Orders=new ArrayList<>();
        Response_Recordsitems_list_for_runtimesheet_Orders=new ArrayList<>();
        Response_RecordsHeader_list_for_runtimesheet_Orders =new ArrayList<>();

        ObserveFU();

        GETDriverID();
        GETZones();
       ControlLayout();

        ButtonsClickListnerForAssignToZone();
        ButtonsClickListnerForAssignToDriver();
    }

    private void ObserveFU() {


        assignPackedOrderToZoneViewModel.getOrderDataLiveData().observe(AssignPackedOrderForZoneAndDriverActivity.this, new Observer<RecievePackedModule>() {
            @Override
            public void onChanged(RecievePackedModule responseGetOrderData) {
                //|| responseGetOrderData.getSTATUS().equalsIgnoreCase("sorted")
                Log.e(TAG, "onChanged:stat_GETOrderData "+ responseGetOrderData.getSTATUS());
                ForOberveOfGetOrderData(responseGetOrderData);
            }

        });

        assignPackedOrderToZoneViewModel.mutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: "+s );

                if (s.equals("HTTP 503 Service Unavailable")) {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, getResources().getString(R.string.tracking_number_server), Toast.LENGTH_SHORT).show();
//                    Constant.ToastDialoge(getResources().getString(R.string.tracking_number_server) , AssignPackedOrderForZoneAndDriverActivity.this);

                }else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, s, Toast.LENGTH_LONG).show();
//                    Constant.ToastDialoge(s, AssignPackedOrderForZoneAndDriverActivity.this);

                }
            }
        });


        assignPackedOrderToZoneViewModel.RetrieveSheetLiveData().observe(AssignPackedOrderForZoneAndDriverActivity.this, new Observer<ResponseFetchRuntimesheetID>() {
            @Override
            public void onChanged(ResponseFetchRuntimesheetID responseFetchRuntimesheetID) {
                Log.e(TAG, "onChanged:FetchRunt " + responseFetchRuntimesheetID.getRecords().size());
                for (int i = 0; i < responseFetchRuntimesheetID.getRecords().size(); i++) {
                    database.userDao().insertRecievePacked(new RecievePackedModule(
                            responseFetchRuntimesheetID.getRecords().get(i).getORDER_NO(), responseFetchRuntimesheetID.getRecords().get(i).getNO_OF_PACKAGES(),
                            responseFetchRuntimesheetID.getRecords().get(i).getTracking_Number(), "null"/*,responseGetOrderData.getCUSTOMER_NAME(),responseGetOrderData.getADDRESS_CITY()
                ,responseGetOrderData.getITEM_PRICE(),responseGetOrderData.getOUTBOUND_DELIVERY() */));

                }
                Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "تم", Toast.LENGTH_SHORT).show();
//                Constant.ToastDialoge(getResources().getString(R.string.confirm), AssignPackedOrderForZoneAndDriverActivity.this);

            }
        });

        assignPackedOrderToZoneViewModel.getSheetLiveData().observe(AssignPackedOrderForZoneAndDriverActivity.this, new Observer<Response>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(Response response) {
                if (response.getRecords().size() > 0) {

                    Log.e("response",response.toString());
                    List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop =  database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
                   // for (int i=0;i<recievePackedORDER_NO_Distinctlist_for_for_loop.size();i++) {
                    //if (){
//                    Response_list_for_runtimesheet_Orders.clear();
//                    Response_id_for_runtimesheet_Orders.clear();
//                    Response_Recordsitems_list_for_runtimesheet_Orders.clear();
//                    Response_phonenumber_for_runtimesheet_Orders.clear();

                    //    Response_list_for_runtimesheet_Orders.add(response);
                        Response_id_for_runtimesheet_Orders.add(response.getId());
                       // for (int i = 0; i < response.getRecords().size(); i++) {
                            Response_Recordsitems_list_for_runtimesheet_Orders.addAll(response.getRecords());
                      //  }
                        Response_RecordsHeader_list_for_runtimesheet_Orders.addAll(response.getRecordsHeader());
                        Log.e(TAG, "onChanged:response.getId() " + response.getId());
                    //    Log.e(TAG, "UpdateDriverID_ON_83:Resposize_onChang " + Response_list_for_runtimesheet_Orders.size());
                        if (Response_Recordsitems_list_for_runtimesheet_Orders.size() !=0 && Response_id_for_runtimesheet_Orders.size() !=0&&
                                Drivers_IDs_list.size()!=0) {
                            UpdateDriverID_ON_83(
                                    Response_Recordsitems_list_for_runtimesheet_Orders.get(
                                            Response_Recordsitems_list_for_runtimesheet_Orders.size() - 1).getORDER_NO(),
                                    Response_RecordsHeader_list_for_runtimesheet_Orders,
//                                    Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getDriverID()
                                    Drivers_IDs_list.get(binding.spinerDriverId.getSelectedItemPosition())
                                            .substring(0,Drivers_IDs_list.get(binding.spinerDriverId.getSelectedItemPosition()).indexOf("&"))
                            );
                            UpdateStatus("ready_to_go");
                            PrintRunTimeSheet(Response_id_for_runtimesheet_Orders.get(0), Response_Recordsitems_list_for_runtimesheet_Orders);

                        }else {
                        //    Toast.makeText(context, "Lists size equal zero", Toast.LENGTH_SHORT).show();
                        }
                //    }
                    //response.
                } else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, R.string.nodata, Toast.LENGTH_SHORT).show();
//                    Constant.ToastDialoge(getResources().getString(R.string.nodata) , AssignPackedOrderForZoneAndDriverActivity.this);

                }

            }
        });

        assignPackedOrderToZoneViewModel.mutableLiveDataError_SheetData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: "+s );

                if (s.equals("HTTP 503 Service Unavailable")) {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, getResources().getString(R.string.order_not_found), Toast.LENGTH_SHORT).show();
//               Constant.ToastDialoge(getResources().getString(R.string.order_not_found) , AssignPackedOrderForZoneAndDriverActivity.this);
                }else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, s, Toast.LENGTH_LONG).show();
//                    Constant.ToastDialoge(s , AssignPackedOrderForZoneAndDriverActivity.this);

                }
            }
        });

        assignPackedOrderToZoneViewModel.getmutableLiveData_UpdateStatus_Zone_ON_83().observe(AssignPackedOrderForZoneAndDriverActivity.this, new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop =  database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
                if (recievePackedORDER_NO_Distinctlist_for_for_loop.size()>0) {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onChanged:update " + message.getMessage());
//                    Constant.ToastDialoge(message.getMessage(), AssignPackedOrderForZoneAndDriverActivity.this);
                }
            }
        });

        assignPackedOrderToZoneViewModel.mutableLiveData_UpdateDriverID_ON_83.observe(AssignPackedOrderForZoneAndDriverActivity.this, new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop =  database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
                if (recievePackedORDER_NO_Distinctlist_for_for_loop.size()>0) {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "" + message.getMessage(), Toast.LENGTH_SHORT).show();
//                    Constant.ToastDialoge(message.getMessage(), AssignPackedOrderForZoneAndDriverActivity.this);

                    Log.e(TAG, "onChanged:update " + message.getMessage());
                }
            }
        });

        assignPackedOrderToZoneViewModel.getmutableLiveData_UpdateStatus().observe(AssignPackedOrderForZoneAndDriverActivity.this, new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop =  database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
                if (recievePackedORDER_NO_Distinctlist_for_for_loop.size()>0) {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                    // Constant.ToastDialoge(message.getMessage() , AssignPackedOrderForZoneAndDriverActivity.this);

                    Log.e(TAG, "onChanged:UpdateStatus " + message.getMessage());
                }
            }
        });
        assignPackedOrderToZoneViewModel.mutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: "+s );
                Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, s, Toast.LENGTH_LONG).show();
//                Constant.ToastDialoge(s, AssignPackedOrderForZoneAndDriverActivity.this);

            }
        });

      /*  assignPackedOrderToZoneViewModel.getSmsLiveData().observe(AssignPackedOrderForZoneAndDriverActivity.this, new Observer<ResponseSms>() {
            @Override
            public void onChanged(ResponseSms responseSms) {
                Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this,
                        responseSms.getSMSStatus().toString(), Toast.LENGTH_SHORT).show();
               // Constant.ToastDialoge(responseSms.getSMSStatus().toString() , AssignPackedOrderForZoneAndDriverActivity.this);

            }
        });
        assignPackedOrderToZoneViewModel.mutableLiveDataError_SendSms.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: "+s );

//                if (s.equals("HTTP 503 Service Unavailable")) {
//                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, getResources().getString(R.string.tracking_number_server), Toast.LENGTH_SHORT).show();
//                }else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "Send SMS Error"+s, Toast.LENGTH_LONG).show();
              //  Constant.ToastDialoge("Send SMS Error"+s , AssignPackedOrderForZoneAndDriverActivity.this);

//                }
            }
        });*/


        assignPackedOrderToZoneViewModel.mutableLiveData_ReadDriverIDS.observe(AssignPackedOrderForZoneAndDriverActivity.this
                , (ResponseDriver responseDriver) -> {
                    if (Drivers_IDs_list.size() == 0) {
                        for (int i = 0; i < responseDriver.getRecords().size(); i++) {
                            if (i == 0)
                                Drivers_IDs_list.add(getResources().getString(R.string.choice_driver_id));

                            Drivers_IDs_list.add(responseDriver.getRecords().get(i).getDriverID()+"&"+
                                    responseDriver.getRecords().get(i).getNameArabic());
                        }
                        Drivers_Data_list.addAll(responseDriver.getRecords());
                    }
                    this.responseDriver = responseDriver;
                    spinnerAdapterDriver.notifyDataSetChanged();
                });

        assignPackedOrderToZoneViewModel.mutableLiveData_readZones.observe(AssignPackedOrderForZoneAndDriverActivity.this
                , (ResponseZoneName responseZoneName) -> {
                    Log.e("zone",String.valueOf(zones_list.size()));
                    if (zones_list.size() == 0) {
                        for (int i = 0; i < responseZoneName.getRecords().size(); i++) {
                            if (i == 0)
                                zones_list.add(getResources().getString(R.string.choice_zone));
                            zones_list.add(responseZoneName.getRecords().get(i).getNameEnglish());
                        }
                    }
                    this.responseZoneName = responseZoneName;
                    spinnerAdapterZones.notifyDataSetChanged();
                });
    }


    private void ForOberveOfGetOrderData(RecievePackedModule responseGetOrderData) {

        if (Zone_public != null) {
            if (responseGetOrderData.getSTATUS().equalsIgnoreCase("in_sorting")
            ) {
                AfterGetOrderData(responseGetOrderData, trackingnumber_public, Zone_public);
            } else {
                Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, getResources().getString(R.string.order_status) + responseGetOrderData.getSTATUS() , Toast.LENGTH_SHORT).show();
           Constant.ToastDialoge(getResources().getString(R.string.order_status) + responseGetOrderData.getSTATUS() , AssignPackedOrderForZoneAndDriverActivity.this);
            }
        }else {
            if (responseGetOrderData.getSTATUS().equalsIgnoreCase("sorted")) {
                AfterGetOrderData(responseGetOrderData, trackingnumber_public, Zone_public);
            } else {
                Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, getResources().getString(R.string.order_status) + responseGetOrderData.getSTATUS() , Toast.LENGTH_SHORT).show();
                Constant.ToastDialoge(getResources().getString(R.string.order_status) + responseGetOrderData.getSTATUS() , AssignPackedOrderForZoneAndDriverActivity.this);

            }
        }
    }

    private void ButtonsClickListnerForAssignToZone() {
        binding.editTrackingnumberZone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                    LoadingNewPurchaseOrderZone();
                }

                return false;
            }
        });
        binding.btnLoadingNewPurchaseOrderZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingNewPurchaseOrderZone();
            }
        });
        binding.btnShowLastTrackingnumbersZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecievePackedModule_For_selection_loop_for_update>  recievePackedORDER_NO_Distinctlist=  database.userDao().GetDistinctordernumbersFromRecievePackedModule();

                if (recievePackedORDER_NO_Distinctlist.size()>0) {
                    Intent GoToEditRecievedPackages=new Intent(AssignPackedOrderForZoneAndDriverActivity.this,
                            EditPackagesForRecievingActivity.class);
                    startActivity(GoToEditRecievedPackages);
                }else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, R.string.thereisno_data_to, Toast.LENGTH_SHORT).show();
//                    Constant.ToastDialoge(getResources().getString(R.string.thereisno_data_to)  , AssignPackedOrderForZoneAndDriverActivity.this);

                }
            }
        });

        /*binding.btnDeleteLastTrackingnumbersZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecievePackedModule>  recievePackedORDER_NO_Distinctlist=  database.userDao().getRecievePacked_ORDER_NO_Distinct();
                if (recievePackedORDER_NO_Distinctlist.size()>0) {
                    new AlertDialog.Builder(AssignPackedOrderForZoneAndDriverActivity.this)
                            .setTitle(getString(R.string.delete_dialoge))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.userDao().deleteRecievePackedModule();
                                    binding.editTrackingnumberZone.setError(null);
                                    binding.editZone.setError(null);
                                    binding.editZone.setText("");
                                    binding.editTrackingnumberZone.requestFocus();
                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
                }else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "لايوجد بيانات للحذف", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        binding.btnConfirmAssignOrdersToZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RecievePackedModule_For_selection_loop_for_update>  recievePackedORDER_NO_Distinctlist=  database.userDao().GetDistinctordernumbersFromRecievePackedModule();
                List<RecievePackedModule>  NOTrecievedPackedORDERlist=  new ArrayList<>();
                if (recievePackedORDER_NO_Distinctlist.size()>0) {
//                    for (int i = 0; i < recievePackedORDER_NO_Distinctlist.size();i++){
//                        List<String>  recievePacked_Tracking_Number_countlist=
//                                database.userDao().getRecievePacked_Tracking_Number_count(recievePackedORDER_NO_Distinctlist.get(i).getORDER_NO());
//                        if (!recievePacked_Tracking_Number_countlist.get(0).
//                                equalsIgnoreCase(recievePackedORDER_NO_Distinctlist.get(i).getNO_OF_PACKAGES().toString())){
//                            NOTrecievedPackedORDERlist.add(recievePackedORDER_NO_Distinctlist.get(i));
//                        }
//                    }

                    if (database.userDao().getTrackingnumber_of_ordersThatNotcompleteAllpackages().size()==0){
                        //TODO UPDATE STATUS
                        // Toast.makeText(RecievePackedOrderForSortingActivity.this, String.format("%s"
                        // ,getString(R.string.message_equalfornoofpaclkageandcountoftrackingnumbers)), Toast.LENGTH_SHORT).show();
//                        List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop=  database.userDao()
//                                .GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
//                        for (int i=0;i<recievePackedORDER_NO_Distinctlist_for_for_loop.size();i++) {
                            UpdateStatus_zone_ON_83("sorted");
                            UpdateStatus("sorted");
//                        }
                    }else {
                        Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, String.format("%s",
                                getString(R.string.message_not_equalfornoofpaclkageandcountoftrackingnumbers_zone)), Toast.LENGTH_SHORT).show();
//                        Constant.ToastDialoge(getResources().getString(R.string.message_not_equalfornoofpaclkageandcountoftrackingnumbers_zone)  , AssignPackedOrderForZoneAndDriverActivity.this);

                        ShowMissedBarcodesFun();
                    }
                }else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, String.format("%s",
                            getString(R.string.there_is_no_trackednumber_scanned)), Toast.LENGTH_SHORT).show();
//                    Constant.ToastDialoge(getResources().getString(R.string.there_is_no_trackednumber_scanned)  , AssignPackedOrderForZoneAndDriverActivity.this);

                }

            }
        });
    }

    private void LoadingNewPurchaseOrderZone() {
        if (!binding.editTrackingnumberZone.getText().toString().isEmpty() &&
                //!binding.editZone.getText().toString().isEmpty()
                binding.spinerZones.getSelectedItemPosition()>0 ) {
            if (Constant.RegulerExpre_forTrackingNumbeer(binding.editTrackingnumberDriver.getText().toString())) {
                // Toast.makeText(context, "Special character not found in the string", Toast.LENGTH_SHORT).show();
                LoadNewPurchaseOrderBTN_Zone();
            }else {
                Toast.makeText(context, "Special character found in the string", Toast.LENGTH_SHORT).show();
            }
        }else {
            if (binding.editTrackingnumberZone.getText().toString().isEmpty()) {
                binding.editTrackingnumberZone.setError("أدخل رقم تتبع الشحنه");
                binding.editTrackingnumberZone.requestFocus();
            }else if (binding.spinerZones.getSelectedItemPosition()<=0
                  //  binding.editZone.getText().toString().isEmpty()
            ){
//                binding.editZone.setError("أدخل المنطقه");
//                binding.editZone.requestFocus();
                Toast.makeText(context, getResources().getString(R.string.choice_zone), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ButtonsClickListnerForAssignToDriver() {
        binding.editTrackingnumberDriver.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                    LoadingNewPurchaseOrderDriver();
                }
                return false;
            }
        });

        binding.btnLoadingNewPurchaseOrderDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingNewPurchaseOrderDriver();
            }
        });

        binding.btnShowLastTrackingnumbersDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnShowLastTrackingnumbersDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<RecievePackedModule_For_selection_loop_for_update>  recievePackedORDER_NO_Distinctlist=  database.userDao().GetDistinctordernumbersFromRecievePackedModule();
                        if (recievePackedORDER_NO_Distinctlist.size()>0) {
                            Intent GoToEditRecievedPackages=new Intent(AssignPackedOrderForZoneAndDriverActivity.this,
                                    EditPackagesForRecievingActivity.class);
                            startActivity(GoToEditRecievedPackages);
                        }else {
                            Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "لايوجد بيانات للعرض", Toast.LENGTH_SHORT).show();
//                            Constant.ToastDialoge(getResources().getString(R.string.thereisno_data_to)  , AssignPackedOrderForZoneAndDriverActivity.this);

                        }
                    }
                });
            }
        });
        /*binding.btnDeleteLastTrackingnumbersDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecievePackedModule>  recievePackedORDER_NO_Distinctlist=  database.userDao().getRecievePacked_ORDER_NO_Distinct();
                if (recievePackedORDER_NO_Distinctlist.size()>0) {
                    new AlertDialog.Builder(AssignPackedOrderForZoneAndDriverActivity.this)
                            .setTitle(getString(R.string.delete_dialoge))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.userDao().deleteRecievePackedModule();
                                    binding.editTrackingnumberDriver.setError(null);
                                    binding.editTrackingnumberDriver.requestFocus();
                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
                }else {
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "لايوجد بيانات للحذف", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        binding.btnConfirmAssignOrdersToDriver.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                Log.e(TAG, "onClick:spi "+binding.spinerDriverId.getSelectedItemPosition());
//                Log.e(TAG, "onClick:spic  "+binding.spinerDriverId.getSelectedItem().toString());
//                Response_list_for_runtimesheet_Orders.clear();
                Response_id_for_runtimesheet_Orders.clear();
                Response_Recordsitems_list_for_runtimesheet_Orders.clear();
                Response_RecordsHeader_list_for_runtimesheet_Orders.clear();

                //TODO apply validation to spinner driver id
               if (binding.spinerDriverId.getSelectedItemPosition()!=0) {
                    List<RecievePackedModule_For_selection_loop_for_update> recievePackedORDER_NO_Distinctlist = database.userDao().GetDistinctordernumbersFromRecievePackedModule();
                    List<RecievePackedModule> NOTrecievedPackedORDERlist = new ArrayList<>();
                    if (recievePackedORDER_NO_Distinctlist.size() > 0) {
//                        for (int i = 0; i < recievePackedORDER_NO_Distinctlist.size(); i++) {
//                            List<String> recievePacked_Tracking_Number_countlist =
//                                    database.userDao().getRecievePacked_Tracking_Number_count(recievePackedORDER_NO_Distinctlist.get(i).getORDER_NO());
//                            if (!recievePacked_Tracking_Number_countlist.get(0).
//                                    equalsIgnoreCase(recievePackedORDER_NO_Distinctlist.get(i).getNO_OF_PACKAGES().toString())) {
//                                NOTrecievedPackedORDERlist.add(recievePackedORDER_NO_Distinctlist.get(i));
//                            }
//                        }

                        if (database.userDao().getTrackingnumber_of_ordersThatNotcompleteAllpackages().size() == 0) {
                            //TODO UPDATE STATUS and print Run time sheet
                            // Toast.makeText(RecievePackedOrderForSortingActivity.this, String.format("%s",getString(R.string.message_equalfornoofpaclkageandcountoftrackingnumbers)), Toast.LENGTH_SHORT).show();
                            // UpdateStatus_zone_ON_83("sorted");
                            // UpdateStatus("sorted");


                            //TODO Print RunTime sheet -- get data for list of order (recievePackedORDER_NO_Distinctlist)
                            // List<RecievePackedModule> recievePackedORDER_NO_Distinctlist = database.userDao().getRecievePacked_ORDER_NO_Distinct();
                         //   for (int i = 0; i < recievePackedORDER_NO_Distinctlist.size(); i++) {
//                            List<RecievePackedModule> Distinctordernumberslist = database.userDao().GetDistinctordernumbersFromRecievePackedModule();
                            Log.i(TAG, "onClick:ORDER_NO_Distinctlistsize "+recievePackedORDER_NO_Distinctlist.size() );
                            if (recievePackedORDER_NO_Distinctlist.size() > 0) {
                                List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop=
                                        database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
                                String OdersNumber="";
                                for (int i=0;i<recievePackedORDER_NO_Distinctlist_for_for_loop.size();i++) {
                                    Log.i(TAG, "onClick:for i "+recievePackedORDER_NO_Distinctlist_for_for_loop.get(i) );
                                    Log.i(TAG, "onClick:for i "+Response_id_for_runtimesheet_Orders.size() );
                                    Log.i(TAG, "onClick:for i "+i );

                                    Log.i(TAG, "onClick:for SheetData "+recievePackedORDER_NO_Distinctlist.get(i).getORDER_NO() );
                                    Log.i(TAG, "onClick:for SheetData "+Drivers_IDs_list.get(binding.spinerDriverId.getSelectedItemPosition()) );
                                    Log.i(TAG, "onClick:for SheetData "+database.userDao().getUserData_MU().getUser_id() );

                                    OdersNumber+="'"+recievePackedORDER_NO_Distinctlist.get(i).getORDER_NO()+"',";
                                    Log.e(TAG, "onClick:OdersNumber "+OdersNumber );
                                }
//                                    if (i>0&& Response_id_for_runtimesheet_Orders.size()>0) {
//                                        Log.e(TAG, "onClick: i>0 andsize >0 ");
                                OdersNumber=OdersNumber.substring(0,OdersNumber.length()-1);
                                        assignPackedOrderToZoneViewModel.SheetData("getlast",
                                                OdersNumber,
                                                Drivers_IDs_list.get(binding.spinerDriverId.getSelectedItemPosition())
                                                , database.userDao().getUserData_MU().getUser_id()
                                        );
//                                        assignPackedOrderToZoneViewModel = ViewModelProviders.of(AssignPackedOrderForZoneAndDriverActivity.this)
//                                                .get(AssignPackedOrderToZoneViewModel.class);
//                                        assignPackedOrderToZoneViewModel.
                                      //  assignPackedOrderToZoneViewModel.runTimeSheetData.setValue(null);

//                                    }else {
//                                        Log.e(TAG, "onClick: elsDDDD getlast" );
//                                        assignPackedOrderToZoneViewModel.SheetData("getlast",recievePackedORDER_NO_Distinctlist.get(0).getORDER_NO(),
//                                                Drivers_IDs_list.get(binding.spinerDriverId.getSelectedItemPosition())
//                                                , database.userDao().getUserData_MU().getUser_id()
//                                        );
//                                    }

                            }
                        //    }

                            Log.e(TAG, "onClick: " + binding.spinerDriverId.getSelectedItemPosition());
                        } else {
                            Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, String.format("%s",
                                    getString(R.string.message_not_equalfornoofpaclkageandcountoftrackingnumbers_driver)), Toast.LENGTH_SHORT).show();
//                            Constant.ToastDialoge(getResources().getString(R.string.message_not_equalfornoofpaclkageandcountoftrackingnumbers_driver)  , AssignPackedOrderForZoneAndDriverActivity.this);

                        }
                    } else {
                        Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, String.format("%s",
                                getString(R.string.there_is_no_trackednumber_scanned)), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, getResources().getString(R.string.choice_driver_id), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnRecallRunsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Call runtime sheet and insert it in localDB

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts_runtime_sheet, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                final EditText edit_runtimesheet_idInput = (EditText) promptsView
                        .findViewById(R.id.edit_runtimesheetidInput);

                final Button btn_getruntimesheet = (Button) promptsView
                        .findViewById(R.id.btn_get_runsheet);

                btn_getruntimesheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // promptsView.

                        if (!edit_runtimesheet_idInput.getText().toString().isEmpty()) {
                            Retrieve_Runsheet(edit_runtimesheet_idInput.getText().toString());
                            alertDialog.dismiss();

                        }else{
                            if (edit_runtimesheet_idInput.getText().toString().isEmpty()){
                                edit_runtimesheet_idInput.setError(getResources().getString(R.string.enter_sms_body));
                                edit_runtimesheet_idInput.requestFocus();
                            }
                        }
                    }

                });
                // show it
                alertDialog.show();

            }
        });
    }

    private void OpenPdf_FUN() {
        //                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                browserIntent.setDataAndType(Uri.parse("/storage/emulated/0/HyperOne.pdf"), "application/pdf");
//                startActivity(browserIntent);

        // File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/HyperOne.pdf");
    //    Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("/storage/emulated/0/HyperOne.pdf"), "application/pdf");
       //         intent.setDataAndType(Uri.parse("/storage/emulated/0/HyperOne.pdf"), "*/*");
        //   intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    //    startActivity(intent);

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

    }

    private void Retrieve_Runsheet(String runsheet_id) {

        List<RecievePackedModule> list=database.userDao().getorderNORecievePackedModule();
        if (list.size()>0) {
            new AlertDialog.Builder(AssignPackedOrderForZoneAndDriverActivity.this)
                    .setTitle(getString(R.string.delete_dialoge))
                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            database.userDao().deleteRecievePackedModule();

                            assignPackedOrderToZoneViewModel.RetieveSheetData(runsheet_id);


                        }
                    })
                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    }).show();
        }else {
            assignPackedOrderToZoneViewModel.RetieveSheetData(runsheet_id);

        }

    }

    private void LoadingNewPurchaseOrderDriver() {
        if (!binding.editTrackingnumberDriver.getText().toString().isEmpty()) {
            if (Constant.RegulerExpre_forTrackingNumbeer(binding.editTrackingnumberDriver.getText().toString())) {
                //  Toast.makeText(context, "Special character not found in the string", Toast.LENGTH_SHORT).show();
                trackingNo=binding.editTrackingnumberDriver.getText().toString();
                LoadNewPurchaseOrderBTN_Driver();
            }else {
                Toast.makeText(context, "Special character found in the string", Toast.LENGTH_SHORT).show();
            }
        }else {
            binding.editTrackingnumberDriver.setError("أدخل السريال");
            binding.editTrackingnumberDriver.requestFocus();
        }
    }

    private void ControlLayout() {
        binding.btnShowAssignOrdersToZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecievePackedModule> list=database.userDao().getorderNORecievePackedModule();
                if (list.size()>0) {
//                    new AlertDialog.Builder(AssignPackedOrderForZoneAndDriverActivity.this)
//                            .setTitle(getString(R.string.delete_dialoge))
//                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.userDao().deleteRecievePackedModule();

                                    if (binding.linearAssignOrderToZone.getVisibility() == View.GONE) {
                                        binding.linearAssignOrderToZone.setVisibility(View.VISIBLE);
                                        binding.linearAssignOrderToDriver.setVisibility(View.GONE);
                                        binding.editTrackingnumberZone.requestFocus();
                                    } else if (binding.linearAssignOrderToZone.getVisibility() == View.VISIBLE) {
                                        binding.linearAssignOrderToZone.setVisibility(View.GONE);
                                        binding.linearAssignOrderToDriver.setVisibility(View.GONE);
                                    }

//                                }
//                            })
//                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.cancel();
//                                }
//                            }).show();
                }else {
                    if (binding.linearAssignOrderToZone.getVisibility() == View.GONE) {
                        binding.linearAssignOrderToZone.setVisibility(View.VISIBLE);
                        binding.linearAssignOrderToDriver.setVisibility(View.GONE);
                        binding.editTrackingnumberZone.requestFocus();
                    } else if (binding.linearAssignOrderToZone.getVisibility() == View.VISIBLE) {
                        binding.linearAssignOrderToZone.setVisibility(View.GONE);
                        binding.linearAssignOrderToDriver.setVisibility(View.GONE);
                    }
                }
            }
        });

        binding.btnShowAssignOrdersToDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecievePackedModule> list=database.userDao().getorderNORecievePackedModule();
                if (list.size()>0) {
//                    new AlertDialog.Builder(AssignPackedOrderForZoneAndDriverActivity.this)
//                            .setTitle(getString(R.string.delete_dialoge))
//                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.userDao().deleteRecievePackedModule();

                                    if (binding.linearAssignOrderToDriver.getVisibility() == View.GONE) {
                                        binding.linearAssignOrderToZone.setVisibility(View.GONE);
                                        binding.linearAssignOrderToDriver.setVisibility(View.VISIBLE);
                                        binding.editTrackingnumberDriver.requestFocus();
                                    } else if (binding.linearAssignOrderToDriver.getVisibility() == View.VISIBLE) {
                                        binding.linearAssignOrderToZone.setVisibility(View.GONE);
                                        binding.linearAssignOrderToDriver.setVisibility(View.GONE);
                                    }

//                                }
//                            })
//                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.cancel();
//                                }
//                            }).show();
                }else {
                    if (binding.linearAssignOrderToDriver.getVisibility() == View.GONE) {
                        binding.linearAssignOrderToZone.setVisibility(View.GONE);
                        binding.linearAssignOrderToDriver.setVisibility(View.VISIBLE);
                        binding.editTrackingnumberDriver.requestFocus();
                    } else if (binding.linearAssignOrderToDriver.getVisibility() == View.VISIBLE) {
                        binding.linearAssignOrderToZone.setVisibility(View.GONE);
                        binding.linearAssignOrderToDriver.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    private void LoadNewPurchaseOrderBTN_Zone() {
        String OrderNumber;
        if (
                //!binding.editTrackingnumberZone.getText().toString().isEmpty() &&
                binding.editTrackingnumberZone.getText().toString().contains("-")
       ) {

            if (Constant.RegulerExpre_forTrackingNumbeer(binding.editTrackingnumberZone.getText().toString())) {
            OrderNumber=
                    binding.editTrackingnumberZone.getText().toString().substring(0,
                            binding.editTrackingnumberZone.getText().toString().indexOf("-"));

            List<RecievePackedModule> recievePackedlist=  database.userDao().getRecievePacked_ORDER_NO(OrderNumber);
            if (recievePackedlist.size() == 0){
                binding.editTrackingnumberZone.setError(null);
                AssignToZone(binding.editTrackingnumberZone.getText().toString()
                        ,zones_list.get(binding.spinerZones.getSelectedItemPosition())
                        /*binding.editZone.getText().toString()*/);
                Log.e(TAG, "onClick: Ord "+OrderNumber );
            //    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "تم", Toast.LENGTH_SHORT).show();
            }else if (database.userDao().getRecievePacked_Tracking_Number(
                    binding.editTrackingnumberZone.getText().toString())
                    .size() ==0){
                binding.editTrackingnumberZone.setError(null);

                Log.e(TAG, "onClick: Trac "+binding.editTrackingnumberZone.getText().toString() );
            //    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "تم", Toast.LENGTH_SHORT).show();
                AssignToZone(binding.editTrackingnumberZone.getText().toString()
                        ,zones_list.get(binding.spinerZones.getSelectedItemPosition())
                        /*binding.editZone.getText().toString()*/);
            }else {
                binding.editTrackingnumberZone.setError("تم أدخال هذا من قبل ");
                binding.editTrackingnumberZone.setText("");
//                binding.editZone.setText("");
                binding.editTrackingnumberZone.requestFocus();
            }
            }else {
                Toast.makeText(context, "Special character found in the string", Toast.LENGTH_SHORT).show();
            }
        }else {
            binding.editTrackingnumberZone.setError(getString(R.string.enter_valid_tracking_number));
            binding.editTrackingnumberZone.setText("");
//            binding.editZone.setText("");
            binding.editTrackingnumberZone.requestFocus();

        }

    }

   private void AssignToZone(String trackingnumber1 ,String Zone1 ){
        String OrderNumber=
                trackingnumber1.substring(0,
                        trackingnumber1.indexOf("-"));
       //TODO CHECK IF TRACKING NUMBER MORE THAN require no of packages
       String NOtrackingnumber =trackingnumber1.substring(
               trackingnumber1.indexOf("-") + 1);

       Log.e(TAG, "onClick: " + NOtrackingnumber);

        List<RecievePackedModule>  recievePackedlist =  database.userDao().getRecievePacked_ORDER_NO(OrderNumber);
        if (recievePackedlist.size() == 0) {
            Zone_public=Zone1;
            trackingnumber_public=trackingnumber1;
            GETOrderData(OrderNumber );
           // Toast.makeText(context, "تم", Toast.LENGTH_SHORT).show();
        }else if (recievePackedlist.size() > 0){
            if (database.userDao().getRecievePacked_Tracking_Number(trackingnumber1)
                    .size() == 0 && Integer.valueOf(recievePackedlist.get(0).getNO_OF_PACKAGES()) >=
                    Integer.valueOf(NOtrackingnumber)) {
                if (recievePackedlist.get(0).getZone().equalsIgnoreCase(Zone1)) {
                    database.userDao().insertRecievePacked(new RecievePackedModule(
                            recievePackedlist.get(0).getORDER_NO(), recievePackedlist.get(0).getNO_OF_PACKAGES(),
                            trackingnumber1,
                            Zone1
                            /*,recievePackedlist.get(0).getCUSTOMER_NAME(),recievePackedlist.get(0).
                            getADDRESS_CITY(),recievePackedlist.get(0).getITEM_PRICE(),recievePackedlist.get(0).
                            getOUTBOUND_DELIVERY()*/));
                    binding.editTrackingnumberZone.setText("");
                    binding.editTrackingnumberZone.setError(null);
                    binding.editTrackingnumberDriver.setText("");
                    binding.editTrackingnumberDriver.setError(null);
//                    binding.editZone.setText("");
//                    binding.editZone.setError(null);
                    Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "تم", Toast.LENGTH_SHORT).show();
//                    Constant.ToastDialoge(getResources().getString(R.string.confirm), AssignPackedOrderForZoneAndDriverActivity.this);

                    binding.editTrackingnumberZone.requestFocus();
                   // Toast.makeText(context, "تم", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(AssignPackedOrderForZoneAndDriverActivity.this)
                            .setTitle(getString(R.string.updte_zone_if_exist))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    database.userDao().insertRecievePacked(new RecievePackedModule(
                                            recievePackedlist.get(0).getORDER_NO(), recievePackedlist.get(0).getNO_OF_PACKAGES(),
                                            trackingnumber1, Zone1
                                            /*,recievePackedlist.get(0).getCUSTOMER_NAME(),recievePackedlist.get(0).getADDRESS_CITY()
                                            ,recievePackedlist.get(0).getITEM_PRICE(),recievePackedlist.get(0).getOUTBOUND_DELIVERY()*/));

                                    binding.editTrackingnumberZone.setText("");
                                    binding.editTrackingnumberZone.setError(null);
                                    binding.editTrackingnumberDriver.setText("");
                                    binding.editTrackingnumberDriver.setError(null);
//                                    binding.editZone.setText("");
                    //                binding.editZone.setError(null);
                                    binding.editTrackingnumberZone.requestFocus();
                                    database.userDao().UpdatezoneForORDER_NO(OrderNumber, Zone1);
                                    Toast.makeText(context,getResources().getString(R.string.confirm), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
                }}else {
                binding.editTrackingnumberZone.setError("تم أدخال رقم غير صحيح ");
                binding.editTrackingnumberZone.setText("");
                binding.editTrackingnumberZone.requestFocus();
            }

        }
    }
//in sorting
    private void GETOrderData(String OrderNumber ){
        assignPackedOrderToZoneViewModel.fetchdata(OrderNumber);
    }

    private void AfterGetOrderData(RecievePackedModule responseGetOrderData, String trackingnumber ,String Zone) {
        Log.e(TAG, "onChanged: " + responseGetOrderData.getNO_OF_PACKAGES());

        if (database.userDao().getRecievePacked_Tracking_Number(
                trackingnumber)
                .size() ==0){
            Log.e(TAG, "AfterGetOrderData:getRecievePacked_Tracking_Number "+
                    database.userDao().getRecievePacked_Tracking_Number(trackingnumber).size() );

            database.userDao().insertRecievePacked(new RecievePackedModule(
                    responseGetOrderData.getORDER_NO(), responseGetOrderData.getNO_OF_PACKAGES(),
                    trackingnumber,Zone/*,responseGetOrderData.getCUSTOMER_NAME(),responseGetOrderData.getADDRESS_CITY()
                ,responseGetOrderData.getITEM_PRICE(),responseGetOrderData.getOUTBOUND_DELIVERY()*/));
            binding.editTrackingnumberZone.setText("");
            binding.editTrackingnumberZone.setError(null);
            binding.editTrackingnumberDriver.setText("");
            binding.editTrackingnumberDriver.setError(null);
//            binding.editZone.setText("");
//            binding.editZone.setError(null);
            Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, getResources().getString(R.string.confirm), Toast.LENGTH_SHORT).show();
//            Constant.ToastDialoge(getResources().getString(R.string.confirm), AssignPackedOrderForZoneAndDriverActivity.this);

            Log.e(TAG, "onChanged: insertAfterGetOrderData " + trackingnumber);
        }else {
           // binding.editTrackingnumberZone.setError("تم أدخال هذا من قبل ");
               binding.editTrackingnumberZone.setText("");
          //  binding.editZone.setText("");
            binding.editTrackingnumberZone.requestFocus();
        }


    }

    public void UpdateStatus_zone_ON_83(String Status){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
        List<RecievePackedModule_For_selection_loop_for_update> Distinctordernumberslist = database.userDao().GetDistinctordernumbersFromRecievePackedModule();
        if (Distinctordernumberslist.size() > 0) {
            List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop=  database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
            for (int i=0;i<recievePackedORDER_NO_Distinctlist_for_for_loop.size();i++) {
                assignPackedOrderToZoneViewModel.UpdateOrderStatus_Zone_ON_83(
                        Distinctordernumberslist.get(i).getORDER_NO(),
                        Distinctordernumberslist.get(i).getZone(),
                        Status,database.userDao().getUserData_MU().getUser_id()
                );
             //   Log.e(TAG, "UpdateStatus_zone_ON_83 zzzo : " + Distinctordernumberslist.get(i).getZone());
            }
        }else {
            Toast.makeText(context,getResources().getString(R.string.not_enter), Toast.LENGTH_SHORT).show();
        }

//        }else {
//            Toast.makeText(GetOrderDatactivity.this, "توجد عناصر لم يتم تعبئتها", Toast.LENGTH_SHORT).show();
//        }
    }

    public void UpdateDriverID_ON_83(String OrderNumber , List<RecordsHeader> response_header_list_for_runtimesheet_Orders2 , String DriverID){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
        List<RecievePackedModule_For_selection_loop_for_update> Distinctordernumberslist
                = database.userDao().GetDistinctordernumbersFromRecievePackedModule();

        if (Distinctordernumberslist.size() > 0) {
//            List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop=  database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
           for (int i=0;i<response_header_list_for_runtimesheet_Orders2.size();i++) {
               Log.e(TAG, "UpdateDriverID_ON_83:UpdateOrder_DriverID "+OrderNumber );
                assignPackedOrderToZoneViewModel.UpdateOrder_DriverID_ON_83(
                        response_header_list_for_runtimesheet_Orders2.get(i).getORDER_NO(),
                        DriverID,database.userDao().getUserData_MU().getUser_id()
                );
                //TODO Update status On magento

//TODO REmove hint to send sms to customer this is last version for sms body
//               SendSMS(response_header_list_for_runtimesheet_Orders2.get(i).getCUSTOMER_PHONE().replace("+2","")
//                     //  "01065286596"
//                       ,"أ/"+response_header_list_for_runtimesheet_Orders2.get(i).getCUSTOMER_NAME()+ " شحنتك رقم"+
//                               response_header_list_for_runtimesheet_Orders2.get(i).getORDER_NO()+"ستصلك خلال الساعات القادمة مع مندوبنا"+
//                               Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getNameArabic() +" رقم هاتفه "+
//                               Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getPhone()+" يرجى تحضير مبلغ "+
//                               response_header_list_for_runtimesheet_Orders2.get(i).getGRAND_TOTAL()
//               );
//
//               Log.e(TAG, "UpdateDriverID_ON_83: "+
//                       "أ/"+response_header_list_for_runtimesheet_Orders2.get(i).getCUSTOMER_NAME()+ " شحنتك رقم"+
//                       response_header_list_for_runtimesheet_Orders2.get(i).getORDER_NO()+"ستصلك خلال الساعات القادمة مع مندوبنا"+
//                       Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getNameArabic() +"رقم هاتفه "+
//                       Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getPhone()+" يرجى تحضير مبلغ "+
//                       response_header_list_for_runtimesheet_Orders2.get(i).getGRAND_TOTAL()
//               );
               Log.e(TAG, "UpdateDriverID_ON_83:phone "+ response_header_list_for_runtimesheet_Orders2.get(i).getCUSTOMER_PHONE().replace("+2","") );
           }
           // Log.e(TAG, "UpdateStatus_zone_ON_83 zzzo : "+orderDataModuleDBHeaderkist.get(0).getZone() );

            //TODO use phone number of customer
            //  String CustomerPhone =items.get(0).getCUSTOMER_PHONE().toString().replace("+2","");
//            String CustomerPhone ="01065286596";
//            SendSMS(CustomerPhone,"Your Order In His Way");
            Log.e(TAG, "UpdateDriverID_ON_83:runtimesheet_Orders.size() "+response_header_list_for_runtimesheet_Orders2.size() );

            Log.e(TAG, "UpdateDriverID_ON_83:Resposize2  "+response_header_list_for_runtimesheet_Orders2.size() );
            /*
           // for (int i = 0; i < response_header_list_for_runtimesheet_Orders2.size(); i++) {
          //      Log.e(TAG, "UpdateDriverID_ON_83:runtimesheet_Orders.oo "+response_header_list_for_runtimesheet_Orders2.get(i).getORDER_NO() );

//                SendSMS(/*Response_list_for_runtimesheet_Orders.get(i).getCUSTOMER_PHONE().replace("+2","")*/
//                        "01065286596"
//                        , Response_list_for_runtimesheet_Orders.get(i).getGRAND_TOTAL()
//                                +"ستصلك خلال الساعات القادمة مع مندوبنا محمد محمود رقم هاتفه 010140123456 يرجى تحضير مبلغ"
//                                +Response_list_for_runtimesheet_Orders.get(i).getORDER_NO()
//                                +Response_list_for_runtimesheet_Orders.get(i).getORDER_NO()+
//                                " شحنتك رقم"+Response_list_for_runtimesheet_Orders.get(i).getCUSTOMER_NAME()+"أ/");

//  Log.e(TAG, "UpdateDriverID_ON_83: "+ Response_list_for_runtimesheet_Orders.get(i).getGRAND_TOTAL()
//                    +"ستصلك خلال الساعات القادمة مع مندوبنا محمد محمود رقم هاتفه 010140123456 يرجى تحضير مبلغ"
//                    +Response_list_for_runtimesheet_Orders.get(i).getORDER_NO()+Response_list_for_runtimesheet_Orders.get(i).getORDER_NO()
//                    + " شحنتك رقم"+Response_list_for_runtimesheet_Orders.get(i).getCUSTOMER_NAME()+"أ/");

//                Log.e(TAG, "UpdateDriverID_ON_83: "+Response_list_for_runtimesheet_Orders.get(i).getGRAND_TOTAL() );
//                Log.e(TAG, "UpdateDriverID_ON_83: "+Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getPhone() );
//                Log.e(TAG, "UpdateDriverID_ON_83: "+Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getNameArabic() );
//                SendSMS(/*Response_list_for_runtimesheet_Orders.get(i).getCUSTOMER_PHONE().replace("+2","")*/
//                        "01065286596"
//                        ,Response_list_for_runtimesheet_Orders.get(i).getGRAND_TOTAL()+
//                                " يرجى تحضير مبلغ "+Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getPhone()
//                                +" رقم هاتفه "+Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getNameArabic()
//                                +"ستصلك خلال الساعات القادمة مع مندوبنا"+Response_list_for_runtimesheet_Orders.get(i).getORDER_NO()
//                                + " شحنتك رقم"+Response_list_for_runtimesheet_Orders.get(i).getCUSTOMER_NAME()+"أ/"
//                );
//                        Log.e(TAG, "UpdateDriverID_ON_83: "+
//                        Response_list_for_runtimesheet_Orders.get(i).getGRAND_TOTAL()+
//                                " يرجى تحضير مبلغ "+Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getPhone()
//                                +"رقم هاتفه "+Drivers_Data_list.get(binding.spinerDriverId.getSelectedItemPosition()).getNameArabic()
//                                +"ستصلك خلال الساعات القادمة مع مندوبنا"+Response_list_for_runtimesheet_Orders.get(i).getORDER_NO()
//                                + " شحنتك رقم"+Response_list_for_runtimesheet_Orders.get(i).getCUSTOMER_NAME()+"أ/"
//                        );

//                Log.e(TAG, "UpdateDriverID_ON_83: "+ Response_list_for_runtimesheet_Orders.get(i).getRecordsHeader().get(i).getORDER_NO() );
//                Log.e(TAG, "UpdateDriverID_ON_83:phone "+ Response_list_for_runtimesheet_Orders.get(0).getRecordsHeader().get(i).getCUSTOMER_PHONE() );


      //      }*/
            }else {
            Toast.makeText(context, getResources().getString(R.string.not_enter), Toast.LENGTH_SHORT).show();
        }

//        }else {
//            Toast.makeText(GetOrderDatactivity.this, "توجد عناصر لم يتم تعبئتها", Toast.LENGTH_SHORT).show();
//        }
    }

    public void UpdateStatus(String Status){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){

        List<RecievePackedModule_For_selection_loop_for_update> orderDataModuleDBHeaderkist = database.userDao().GetDistinctordernumbersFromRecievePackedModule();
        if (orderDataModuleDBHeaderkist.size() > 0) {
            List<String>  recievePackedORDER_NO_Distinctlist_for_for_loop=  database.userDao().GetDistinctordernumbersFromRecievePackedModule_FOR_FORLoop();
            for (int i=0;i<recievePackedORDER_NO_Distinctlist_for_for_loop.size();i++) {
                assignPackedOrderToZoneViewModel.UpdateStatus(
                        orderDataModuleDBHeaderkist.get(i).getORDER_NO(),
                        Status
                );
            }
        }
    }

    private void GETDriverID() {
        Drivers_IDs_list =new ArrayList<>();
        Drivers_Data_list=new ArrayList<>();
        assignPackedOrderToZoneViewModel.GetDriversID();
        //TODO TO ASSIGN ID TO SPINNER
        spinnerAdapterDriver=new ArrayAdapter<String>(AssignPackedOrderForZoneAndDriverActivity.this,
                android.R.layout.simple_spinner_item,Drivers_IDs_list);
        spinnerAdapterDriver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinerDriverId.setAdapter(spinnerAdapterDriver);
        binding.spinerDriverId.setTitle(getResources().getString(R.string.choice_driver_id));

    }

    private void GETZones() {
        zones_list =new ArrayList<>();
        assignPackedOrderToZoneViewModel.GetZonessID();
        //TODO TO ASSIGN ID TO SPINNER
        spinnerAdapterZones=new ArrayAdapter<String>(AssignPackedOrderForZoneAndDriverActivity.this,
                android.R.layout.simple_spinner_item,zones_list);
        spinnerAdapterZones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinerZones.setAdapter(spinnerAdapterZones);
        binding.spinerZones.setTitle(getResources().getString(R.string.choice_zone));

    }
     private void LoadNewPurchaseOrderBTN_Driver() {
        String OrderNumber;
        if (!binding.editTrackingnumberDriver.getText().toString().isEmpty() &&
                binding.editTrackingnumberDriver.getText().toString().contains("-")) {
            OrderNumber=
                    binding.editTrackingnumberDriver.getText().toString().substring(0,
                            binding.editTrackingnumberDriver.getText().toString().indexOf("-"));

            List<RecievePackedModule> recievePackedlist=  database.userDao().getRecievePacked_ORDER_NO(OrderNumber);
            if (recievePackedlist.size() == 0){
                Zone_public=null;
                trackingnumber_public=binding.editTrackingnumberDriver.getText().toString();
                GETOrderData(OrderNumber );
                Log.e(TAG, "onClick: Ord "+OrderNumber );
                binding.editTrackingnumberDriver.setText("");
                binding.editTrackingnumberDriver.setError(null);
               // Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "تم", Toast.LENGTH_SHORT).show();
            }else if (database.userDao().getRecievePacked_Tracking_Number(binding.editTrackingnumberDriver.getText().toString())
                    .size() ==0){
                database.userDao().insertRecievePacked(new RecievePackedModule(
                        recievePackedlist.get(0).getORDER_NO(), recievePackedlist.get(0).getNO_OF_PACKAGES(),
                        binding.editTrackingnumberDriver.getText().toString(),
                        null /*,recievePackedlist.get(0).getCUSTOMER_NAME(),recievePackedlist.get(0).
                            getADDRESS_CITY(),recievePackedlist.get(0).getITEM_PRICE(),recievePackedlist.get(0).
                            getOUTBOUND_DELIVERY()*/));

               // GETOrderData(binding.editTrackingnumberDriver.getText().toString(),null);
                binding.editTrackingnumberDriver.setText("");
                binding.editTrackingnumberDriver.setError(null);
                Log.e(TAG, "onClick: Trac "+binding.editTrackingnumberDriver.getText().toString() );
                Toast.makeText(AssignPackedOrderForZoneAndDriverActivity.this, "تم", Toast.LENGTH_SHORT).show();
//                Constant.ToastDialoge(getResources().getString(R.string.confirm), AssignPackedOrderForZoneAndDriverActivity.this);

            }else {
                binding.editTrackingnumberDriver.setError(getResources().getString(R.string.enterbefor));
                binding.editTrackingnumberDriver.setText("");
                binding.editTrackingnumberDriver.requestFocus();
            }
        }else {

            binding.editTrackingnumberDriver.setError(getString(R.string.enter_tracking_number));
            binding.editTrackingnumberDriver.setText("");
            binding.editTrackingnumberDriver.requestFocus();

        }
    }

    private void SendSMS(String CustomerPhone ,String SMSBody) {
        assignPackedOrderToZoneViewModel.SendSms(CustomerPhone , SMSBody);
    }

    //TODO
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void PrintRunTimeSheet(String id , List<RecordsItem> items) {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        Log.e(TAG, "PrintRunTimeSheet:itemssize "+items.size() );
        Log.e(TAG, "PrintRunTimeSheet: id  "+id );
        createPdf(id , items);
        OpenPdf_FUN();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   /* private void createPdf(String id ,List<RecordsItem> items) {

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        paint.setTextSize(30.0f);
        PdfDocument.Page page = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(2000, 3000, 1).create());
        Canvas canvas = page.getCanvas();
        canvas.drawText("إقرار إستلام /Receiving Avowal", 700.0f, 60.0f, paint);
        canvas.drawText(" رقم  "+id, 500.0f, 60.0f, paint);

        canvas.drawText("التاريخ/Date : " + currentDate + "           الوقت/Time : " + currentTime + " ", 550.0f, 100.0f, paint);
        canvas.drawText("استلمت أنا ....................................... رقم قومي .............................  مندوب (شركة هايبروان للتجارة) البضاعة الموجودة بالشحنات المذكورأرقامها بالأسفل", 30.0f, 140.0f, paint);
        canvas.drawText("وذلك لتسليمها لعملاء الشركة وتحصيل قيمتها منهم على أن ألتزم برد الطلبيات التي لم تسلم للعملاء لمخزن الشركة بنفس حالة إستلامها وتسديد ما أقوم بتحصيله", 30.0f, 180.0f, paint);
        canvas.drawText("من العملاء لخزينة الشركة وتعتبر البضاعة وما أقوم بتحصيله من العملاء هو أمانة في ذمتي أتعهد بتسليمها للشركة, وإذا أخلللت بذلك أكون مبددا وخائنا للأمانة . ", 30.0f, 220.0f, paint);
        canvas.drawText("وأتحمل كافة المسئولية الجنائية والمدنية المترتبة على ذلك. ", 550.0f, 260.0f, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        Paint paint2 = paint;

        canvas.drawRect(30.0f, 2600.0f, 1940.0f, 280.0f, paint2);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawText("S/م", 1925.0f, 310.0f, paint);
        canvas.drawLine(1870.0f, 280.0f, 1870.0f, 2600.0f, paint2);
        canvas.drawText("outBound", 1830.0f, 310.0f, paint);
        canvas.drawLine(1690.0f, 280.0f, 1690.0f, 2600.0f, paint2);
        canvas.drawText("رقم الشحنة", 1500.0f, 310.0f, paint);
        canvas.drawLine(1150.0f, 280.0f, 1150.0f, 2600.0f, paint2);
        canvas.drawText("قيمة الشحنة", 1140.0f, 310.0f, paint);
        canvas.drawLine(997.0f, 280.0f, 997.0f, 2600.0f, paint2);
        canvas.drawText("طريقة الدفع", 990.0f, 310.0f, paint);
        canvas.drawLine(850.0f, 280.0f, 850.0f, 2600.0f, paint2);
        canvas.drawText("إسم العميل", 760.0f, 310.0f, paint);
        canvas.drawLine(500.0f, 280.0f, 500.0f, 2600.0f, paint2);
        canvas.drawText("عنوان العميل", 480.0f, 310.0f, paint);
        canvas.drawLine(300.0f, 280.0f, 300.0f, 2600.0f, paint2);
        canvas.drawText("ملاحظات", 180.0f, 310.0f, paint);

        //bottom of header row  line
        canvas.drawLine(30.0f, 330.0f, 1940.0f, 330.0f, paint2);

        canvas.drawText("توقيع المستلم/Receiver sign", 1500.0f, 2700.0f, paint);
        canvas.drawText("توقيع مسئول أمن المخزن", 1000.0f, 2700.0f, paint);

        canvas.drawText("توقيع منسق التوصيل", 600.0f, 2700.0f, paint);
        int pos=0;
        for (int i=0;i<items.size();i++) {
            canvas.drawText(items.get(i).getADDRESS_CITY(), 480.0f, 390+pos, paint);
            canvas.drawText(items.get(i).getCUSTOMER_NAME(), 760.0f, 390+pos, paint);
            canvas.drawText("كاش", 950.0f, 390+pos, paint);
            canvas.drawText(items.get(i).getOUTBOUND_DELIVERY(), 1830.0f, 390+pos, paint);
            canvas.drawText(String.valueOf(i+1), 1920.0f, 390+pos, paint);

            canvas.drawText(items.get(i).getITEM_PRICE(), 1130.0f, 390+pos, paint);


            try {
                testCODE93(canvas, 1160.0f, 340+pos, items.get(i).getTRACKING_NO());
                canvas.drawLine(30.0f, 430.0f+pos, 1940.0f, 430.0f+pos, paint2);


            } catch (Exception e) {
                e.printStackTrace();
            }
            pos+=100;
        }
        pdfDocument.finishPage(page);
        try {
            pdfDocument.writeTo(new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/HyperOne.pdf")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();

    }*/

    
    // TODO this last version but with one page
    private void createPdf(String id , List<RecordsItem> items) {
        List<RecordsItem> data=items;
        ArrayList<String> outBounds=new ArrayList<>();
        for (int i =0;i<items.size();i++)
        {
            outBounds.add(items.get(i).getOUTBOUND_DELIVERY());
        }

        Float total = 0f;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        PdfDocument pdfDocument = new PdfDocument();
//        int noOfPages=data.size() /14 ;
//        Log.e(TAG, "createPdf:noOfPagesBefore / "+noOfPages);
//        Log.e(TAG, "datasize "+data.size());
//        if (noOfPages==0)
//        {
//            noOfPages=1;
//        }
//        Log.e(TAG, "createPdf:noOfPages "+data.size() %14 );
//
//        if (data.size() %14 > 0){
//            noOfPages=Integer.valueOf(noOfPages)+1;
//        }
        int noOfPages=items.size() /14 ;
        Log.e(TAG, "createPdf:items.size() "+items.size() );
        Log.e(TAG, "createPdf:noOfPagesBefore % "+noOfPages );
        if (items.size() %14 >0){
            noOfPages++;
        }
        Log.e(TAG, "createPdf:noOfPages "+noOfPages );

        for (int j=0;j < noOfPages;j++) {
            Paint paint = new Paint();
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setTextSize(30.0f);
            PdfDocument.Page page = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(3000, 2000, j+1).create());
            Canvas canvas = page.getCanvas();
            canvas.drawText("إقرار إستلام /Receiving Avowal" + " رقم  " + id, 1250.0f, 60.0f, paint);

            canvas.drawText("التاريخ/Date : " + currentDate + "           الوقت/Time : " + currentTime + " ",
                    1150.0f, 100.0f, paint);
            canvas.drawText("استلمت أنا ....................................... رقم قومي .............................  مندوب (شركة هايبروان للتجارة) البضاعة الموجودة بالشحنات المذكورأرقامها بالأسفل",
                    500.0f, 140.0f, paint);
            canvas.drawText("وذلك لتسليمها لعملاء الشركة وتحصيل قيمتها منهم على أن ألتزم برد الطلبيات التي لم تسلم للعملاء لمخزن الشركة بنفس حالة إستلامها وتسديد ما أقوم بتحصيله",
                    550.0f, 180.0f, paint);
            canvas.drawText("من العملاء لخزينة الشركة وتعتبر البضاعة وما أقوم بتحصيله من العملاء هو أمانة في ذمتي أتعهد بتسليمها للشركة, وإذا أخلللت بذلك أكون مبددا وخائنا للأمانة . ",
                    550.0f, 220.0f, paint);
            canvas.drawText("وأتحمل كافة المسئولية الجنائية والمدنية المترتبة على ذلك. ",
                    1000.0f, 260.0f, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2.0f);
            Paint paint2 = paint;


            //bottom potion of header liner
            canvas.drawRect(30.0f, 1800.0f, 2940.0f, 280.0f, paint2);
            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawText("S/م", 2925.0f, 310.0f, paint);
            canvas.drawLine(2870.0f, 280.0f, 2870.0f, 1800.0f, paint2);
            canvas.drawText("outBound", 2852.0f, 310.0f, paint);
            canvas.drawLine(2650.0f, 280.0f, 2650.0f, 1800.0f, paint2);
            canvas.drawText("رقم الشحنة", 2400.0f, 310.0f, paint);
            canvas.drawLine(2100.0f, 280.0f, 2100.0f, 1800.0f, paint2);
            canvas.drawText("قيمة الشحنة", 2095.0f, 310.0f, paint);
            canvas.drawLine(1920.0f, 280.0f, 1920.0f, 1800.0f, paint2);
            canvas.drawText("طريقة الدفع", 1905.0f, 310.0f, paint);
            canvas.drawLine(1755.0f, 280.0f, 1755.0f, 1800.0f, paint2);
            canvas.drawText("نوع الشحنه", 1750.0f, 310.0f, paint);
            canvas.drawLine(1610.0f, 280.0f, 1610.0f, 1800.0f, paint2);
            canvas.drawText("إسم العميل", 1500.0f, 310.0f, paint);
            canvas.drawLine(1260.0f, 280.0f, 1260.0f, 1800.0f, paint2);
            canvas.drawText("تلفون العميل", 1160.0f, 310.0f, paint);
            canvas.drawLine(920.0f, 280.0f, 920.0f, 1800.0f, paint2);
            canvas.drawText("عنوان العميل", 740.0f, 310.0f, paint);

       /* canvas.drawLine(1260.0f, 280.0f, 1260.0f, 1800.0f, paint2);
        canvas.drawText("عنوان العميل", 1160.0f, 310.0f, paint);
        canvas.drawLine(900.0f, 280.0f, 900.0f, 1800.0f, paint2);
        canvas.drawText("تلفون العميل", 780.0f, 310.0f, paint);
*/

            canvas.drawLine(430.0f, 280.0f, 430.0f, 1800.0f, paint2);
            canvas.drawText("ملاحظات", 290.0f, 310.0f, paint);

            //bottom of header row  line
            canvas.drawLine(30.0f, 330.0f, 2940.0f, 330.0f, paint2);

            canvas.drawText("توقيع المستلم/Receiver sign", 2400.0f, 1850.0f, paint);
            canvas.drawText("توقيع مسئول أمن المخزن", 1700.0f, 1850.0f, paint);

            canvas.drawText("توقيع منسق التوصيل", 1000.0f, 1850.0f, paint);
            int pos = 0;


            Map<String, List<RecordsItem>> personByAge = new HashMap<>();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                personByAge = items.stream()
                        .collect(Collectors.groupingBy(RecordsItem::getOUTBOUND_DELIVERY));

            }
            ArrayList<String> list = removeDuplicates(outBounds);
            Log.e("out", list.toString());
            for (int x = 0; x < list.size(); x++) {
                Log.e("x",""+x);
                total = 0f;
                Log.e("out4", personByAge.get(list.get(x)).toString());
                Log.e("out4", ""+personByAge.get(list.get(x)).size());
                items.clear();
                items = personByAge.get(list.get(x));

                //Log.e("createPdf:noOfPages-1")
                Log.e(TAG, "createPdf:noOfPages-1 "+ ((noOfPages-1)*13) );
                Log.e(TAG, "createPdf:noOfPages-1 "+ ((noOfPages-1)*13) );
//            Log.e(TAG, "createPdf:noOfPages-1 "+ (j*17) );
//            for (int i = (j*17); i < items.size(); i++) {

                Log.e(TAG, "createPdf:X "+x );
                Log.e(TAG, "createPdf:personByAge "+ personByAge.size() );
                Log.e(TAG, "createPdf:list.get(x) "+ list.get(x) );

                int NoOfItemsForPages=(personByAge.get(list.get(x)).size())/13 ;
                Log.e(TAG, "createPdf:NoOfItemsForPages "+NoOfItemsForPages );
                Log.e(TAG, "createPdf:NoOfItemsForPages "+NoOfItemsForPages );
                if (NoOfItemsForPages %13 >0){
                    NoOfItemsForPages++;
                }
                Log.e(TAG, "createPdf:NoOfItemsForPages "+NoOfItemsForPages );

                // for (int k = 0; k < NoOfItemsForPages ; k++) {

                for (int i = (j*14); i < (personByAge.get(list.get(x)).size()) ; i++) {
                    if (i == 14 && j==0) {
                        break;
                    }
                        /*else if ((i *j ) ==(14 * j) && j !=0 ){
                            break;
                        }*/
                    Log.e("out2  ", list.get(x));
                    canvas.drawText(items.get(i).getADDRESS_DETAILS().substring(0, items.get(i).getADDRESS_DETAILS().length() / 3), 890.0f, 360 + pos, paint);
                    canvas.drawText(items.get(i).getADDRESS_DETAILS().substring(items.get(i).getADDRESS_DETAILS().length() / 3, (items.get(i).getADDRESS_DETAILS().length() / 3) * 2), 890.0f, 395 + pos, paint);
                    canvas.drawText(items.get(i).getADDRESS_DETAILS().substring((items.get(i).getADDRESS_DETAILS().length() / 3) * 2, items.get(i).getADDRESS_DETAILS().length()), 890.0f, 420 + pos, paint);
                    canvas.drawText(items.get(i).getCUSTOMER_NAME(), 1570.0f, 390 + pos, paint);
                    canvas.drawText(items.get(i).getCUSTOMER_PHONE(), 1200.0f, 390 + pos, paint);
                    paint.setTextSize(22.0f);
                    canvas.drawText(items.get(i).getDelivery_Method(), 1730.0f, 390 + pos, paint);
                    canvas.drawText(items.get(i).getPayment_Method(), 1910.0f, 390 + pos, paint);
                    paint.setTextSize(30.0f);
                    canvas.drawText(items.get(i).getOUTBOUND_DELIVERY(), 2850.0f, 390 + pos, paint);
                    canvas.drawText(String.valueOf(i + 1), 2910.0f, 390 + pos, paint);
                    canvas.drawText(String.valueOf(new DecimalFormat("##.00").format(Float.valueOf(items.get(i).getITEM_PRICE()))), 2070.0f, 390 + pos, paint);
                    total += Float.valueOf(items.get(i).getITEM_PRICE());
                    try {
                        testCODE93(canvas, 2120.0f, 340 + pos, items.get(i).getTRACKING_NO());
                        canvas.drawLine(30.0f, 430.0f + pos, 2940.0f, 430.0f + pos, paint2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    pos += 100;

                }
                // }

                canvas.drawText("اجمالي الطلب:", 2500.0f, 390 + pos, paint);
                canvas.drawText(String.valueOf(new DecimalFormat("##.00").format(Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getGRAND_TOTAL()) + Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getReedemed_Points_Amount()))), 2070.0f, 390 + pos, paint);
                canvas.drawText("قيمه النقاط المستبدلة:", 1550.0f, 390 + pos, paint);
                canvas.drawText(String.valueOf(new DecimalFormat("##0.00").format(Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getReedemed_Points_Amount()))), 1200.0f, 390 + pos, paint);
                canvas.drawText("المطلوب تحصيله:", 890.0f, 390 + pos, paint);
                canvas.drawText(String.valueOf(new DecimalFormat("##.00").format(Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getGRAND_TOTAL()))), 290.0f, 390 + pos, paint);
                canvas.drawLine(30.0f, 400.0f + pos, 2940.0f, 400.0f + pos, paint2);
                pos += 100;
            }


            pdfDocument.finishPage(page);
            try {
                pdfDocument.writeTo(new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/HyperOne.pdf")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pdfDocument.close();

    }


    public String checkPaymentMethod(String name)
    {
        if (name.equals("0") || name.equals("0.000")) {
            return "أون لاين";
        }
        else
        {return "كاش";}
    }
//TODO this for n of pages after calculte
//    private void createPdf(String id , List<RecordsItem> items) {
//
//        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//        PdfDocument pdfDocument = null;
//            pdfDocument = new PdfDocument();
//
//// use mode items list for 17 and
//        //Log.e(TAG, "createPdf:17% "+(17 % 17) );
//        //Log.e(TAG, "createPdf:17/ "+(17 /17) );
//
//        //Log.e(TAG, "createPdf:18% "+(18 % 17) );
//       // Log.e(TAG, "createPdf:18/ "+(18 /17) );
//
//        int noOfPages=items.size() /17 ;
//        Log.e(TAG, "createPdf:noOfPagesBefore % "+noOfPages );
//        if (items.size() %17 >0){
//            noOfPages++;
//        }
//        Log.e(TAG, "createPdf:noOfPages "+noOfPages );
//
//        for (int j=0;j < noOfPages;j++) {
//            Paint paint = new Paint();
//            paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
//            paint.setTextSize(30.0f);
//
//            PdfDocument.Page page = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(3000, 2000, j+1).create());
//            Canvas canvas = page.getCanvas();
//            canvas.drawText("إقرار إستلام /Receiving Avowal" + " رقم  " + id , 1250.0f, 60.0f, paint);
//
//            canvas.drawText("التاريخ/Date : " + currentDate + "           الوقت/Time : " + currentTime + " ",
//                    1150.0f, 100.0f, paint);
//            canvas.drawText("استلمت أنا ....................................... رقم قومي .............................  مندوب (شركة هايبروان للتجارة) البضاعة الموجودة بالشحنات المذكورأرقامها بالأسفل",
//                    500.0f, 140.0f, paint);
//            canvas.drawText("وذلك لتسليمها لعملاء الشركة وتحصيل قيمتها منهم على أن ألتزم برد الطلبيات التي لم تسلم للعملاء لمخزن الشركة بنفس حالة إستلامها وتسديد ما أقوم بتحصيله",
//                    550.0f, 180.0f, paint);
//            canvas.drawText("من العملاء لخزينة الشركة وتعتبر البضاعة وما أقوم بتحصيله من العملاء هو أمانة في ذمتي أتعهد بتسليمها للشركة, وإذا أخلللت بذلك أكون مبددا وخائنا للأمانة . ",
//                    550.0f, 220.0f, paint);
//            canvas.drawText("وأتحمل كافة المسئولية الجنائية والمدنية المترتبة على ذلك. ",
//                    1000.0f, 260.0f, paint);
//
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(2.0f);
//            Paint paint2 = paint;
//
//            //bottom potion of header liner
//            canvas.drawRect(30.0f, 1800.0f, 2940.0f, 280.0f, paint2);
//            paint.setTextAlign(Paint.Align.RIGHT);
//            paint.setStyle(Paint.Style.FILL);
//
//            canvas.drawText("S/م", 2925.0f, 310.0f, paint);
//            canvas.drawLine(2870.0f, 280.0f, 2870.0f, 1800.0f, paint2);
//            canvas.drawText("outBound", 2852.0f, 310.0f, paint);
//            canvas.drawLine(2650.0f, 280.0f, 2650.0f, 1800.0f, paint2);
//            canvas.drawText("رقم الشحنة", 2400.0f, 310.0f, paint);
//            canvas.drawLine(2100.0f, 280.0f, 2100.0f, 1800.0f, paint2);
//            canvas.drawText("قيمة الشحنة", 2095.0f, 310.0f, paint);
//            canvas.drawLine(1920.0f, 280.0f, 1920.0f, 1800.0f, paint2);
//            canvas.drawText("طريقة الدفع", 1905.0f, 310.0f, paint);
//            canvas.drawLine(1755.0f, 280.0f, 1755.0f, 1800.0f, paint2);
//            canvas.drawText("نوع الشحنه", 1750.0f, 310.0f, paint);
//            canvas.drawLine(1610.0f, 280.0f, 1610.0f, 1800.0f, paint2);
//            canvas.drawText("إسم العميل", 1500.0f, 310.0f, paint);
//            canvas.drawLine(1260.0f, 280.0f, 1260.0f, 1800.0f, paint2);
//            canvas.drawText("عنوان العميل", 1160.0f, 310.0f, paint);
//            canvas.drawLine(900.0f, 280.0f, 900.0f, 1800.0f, paint2);
//            canvas.drawText("تلفون العميل", 780.0f, 310.0f, paint);
//
//            canvas.drawLine(550.0f, 280.0f, 550.0f, 1800.0f, paint2);
//            canvas.drawText("ملاحظات", 300.0f, 310.0f, paint);
//
//            //bottom of header row  line
//            canvas.drawLine(30.0f, 330.0f, 2940.0f, 330.0f, paint2);
//
//            canvas.drawText("توقيع المستلم/Receiver sign", 2400.0f, 1850.0f, paint);
//            canvas.drawText("توقيع مسئول أمن المخزن", 1700.0f, 1850.0f, paint);
//
//            canvas.drawText("توقيع منسق التوصيل", 1000.0f, 1850.0f, paint);
//            int pos = 0;
//            Log.e(TAG, "createPdf:noOfPages-1 "+ ((noOfPages-1)*17) );
//            Log.e(TAG, "createPdf:noOfPages-1 "+ (j*17) );
//            for (int i = (j*17); i < items.size(); i++) {
//                canvas.drawText(items.get(i).getADDRESS_CITY(), 1220.0f, 390 + pos, paint);
//                canvas.drawText(items.get(i).getCUSTOMER_NAME(), 1570.0f, 390 + pos, paint);
//                canvas.drawText(items.get(i).getCUSTOMER_PHONE(), 850.0f, 390 + pos, paint);
//
//                canvas.drawText("توصيل", 1750.0f, 390 + pos, paint);
//
//                canvas.drawText("كاش", 1880.0f, 390 + pos, paint);
//                canvas.drawText(items.get(i).getOUTBOUND_DELIVERY(), 2850.0f, 390 + pos, paint);
//                canvas.drawText(String.valueOf(i + 1), 2910.0f, 390 + pos, paint);
//
//                canvas.drawText(items.get(i).getITEM_PRICE(), 2090.0f, 390 + pos, paint);
//
//
//                try {
//                    testCODE93(canvas, 2120.0f, 340 + pos, items.get(i).getTRACKING_NO());
//                    canvas.drawLine(30.0f, 430.0f + pos, 2940.0f, 430.0f + pos, paint2);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                pos += 100;
//            }
//
//
//            pdfDocument.finishPage(page);
//            try {
//                pdfDocument.writeTo(new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/HyperOne.pdf")));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        pdfDocument.close();
//
//    }

    private static void testCODE93(Canvas canvas , float left, float top,String trackingnumber) throws Exception
    {
        Code93 barcode = new Code93();

        /*
           Code 93 Valid data char set:
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9 (Digits)
                A - Z (Uppercase letters)
                - (Dash), $ (Dollar), % (Percentage), (Space), . (Point), / (Slash), + (Plus)
        */
        barcode.setData(trackingnumber);

        // Unit of Measure, pixel, cm, or inch
        barcode.setUom(IBarcode.UOM_PIXEL);
        // barcode bar module width (X) in pixel
        barcode.setX(3f);
        // barcode bar module height (Y) in pixel
        barcode.setY(60f);

        // barcode image margins
//        barcode.setLeftMargin(1f);
//        barcode.setRightMargin(1f);
//        barcode.setTopMargin(1f);
//        barcode.setBottomMargin(1f);

        // barcode image resolution in dpi
        barcode.setResolution(72);

        // disply barcode encoding data below the barcode
         barcode.setShowText(true);
        // barcode encoding data font style
           barcode.setTextFont(new AndroidFont("Arial", Typeface.NORMAL, 26));
        // space between barcode and barcode encoding data
        barcode.setTextMargin(10);
        barcode.setTextColor(AndroidColor.black);

        // barcode bar color and background color in Android device
        barcode.setForeColor(AndroidColor.black);
        barcode.setBackColor(AndroidColor.white);

        /*
        specify your barcode drawing area
	    */
        RectF bounds = new RectF(left, top, 0, 0);
        barcode.drawBarcode(canvas, bounds);
    }

    private void ShowMissedBarcodesFun() {
        LayoutInflater li = LayoutInflater.from(AssignPackedOrderForZoneAndDriverActivity.this);
        View promptsView = li.inflate(R.layout.prompts_showordersnumber, null);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                AssignPackedOrderForZoneAndDriverActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final RecyclerView rv_ordernumbers = (RecyclerView) promptsView
                .findViewById(R.id.rv_ordernmber);

        final TextView txt_title=(TextView) promptsView.findViewById(R.id.txt_title);
        txt_title.setText(R.string.title_dialoge_missuing_trackingnumbers);

        OrdersnumberAdapter ordersnumberAdapter = new OrdersnumberAdapter(database.userDao().getTrackingnumber_of_ordersThatNotcompleteAllpackages());
        Log.e(TAG, "onClick:listoforders "+database.userDao().getOrdersNumberDB().size() );
        rv_ordernumbers.setAdapter(ordersnumberAdapter);
        rv_ordernumbers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        ItemclickforRecycler.addTo(rv_ordernumbers).setOnItemClickListener(new ItemclickforRecycler.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                // promptsView.
//                String Ordernumber=ordersnumberAdapter.ReturnListOfPackages().get(position);
//                UploadHeader(Ordernumber);
//                //UploadDetails(Ordernumber);
//
//                alertDialog.dismiss();
//
//            }
//        });
        // show it
        alertDialog.show();

    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

}