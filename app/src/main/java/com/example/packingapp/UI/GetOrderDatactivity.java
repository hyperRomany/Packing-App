package com.example.packingapp.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packingapp.Adapter.OrdersnumberAdapter;
import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.Helper.ItemclickforRecycler;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityGetOrderDataBinding;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails;
import com.example.packingapp.model.GetOrderResponse.ItemsOrderDataDBDetails_Scanned;
import com.example.packingapp.model.GetOrderResponse.OrderDataModuleDBHeader;
import com.example.packingapp.model.GetOrderResponse.ResponseGetOrderData;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.viewmodel.GetOrderDataViewModel;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GetOrderDatactivity extends AppCompatActivity {
    ActivityGetOrderDataBinding binding;
    GetOrderDataViewModel getOrderDataViewModel;
    private static final String TAG = "GetOrderDatactivity";
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetOrderDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = AppDatabase.getDatabaseInstance(this);

        getOrderDataViewModel = ViewModelProviders.of(this).get(GetOrderDataViewModel.class);

        binding.editMagentoorder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER) {
                    LoadNewPurchaseOrder();
                }
                return false;
            }
        });


        binding.btnLoadingNewPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadNewPurchaseOrder();
            }
        });
//        getOrderDataViewModel.getOrderDataLiveData().observe(GetOrderDatactivity.this,
//                new Observer<ResponseGetOrderData>() {
//                    @Override
//                    public void onChanged(ResponseGetOrderData responseGetOrderData) {
//                        Log.e(TAG, "onChanged: "+responseGetOrderData.getStatus() );
//                        if (responseGetOrderData.getStatus().equalsIgnoreCase("closed")) {
//                            ActionAfterGetData(responseGetOrderData);
//                        }else {
//                            Toast.makeText(GetOrderDatactivity.this, "This Order in "+responseGetOrderData.getStatus()+" State", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        getOrderDataViewModel.getOrderDataLiveData().observe(GetOrderDatactivity.this,
                new Observer<ResponseGetOrderData>() {
                    @Override
                    public void onChanged(ResponseGetOrderData responseGetOrderData) {
                        Log.e(TAG, "onChanged: " + responseGetOrderData.getStatus());
                        if (responseGetOrderData.getStatus().equalsIgnoreCase("closed")) {
                            ActionAfterGetData(responseGetOrderData);
                        } else {
                            Toast.makeText(GetOrderDatactivity.this, getResources().getString(R.string.order_status) + responseGetOrderData.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        getOrderDataViewModel.mutableLiveDataError.observe(GetOrderDatactivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged:mutableLiveD  " + s);
                if (s.contains("HTTP 400")) {
                    Toast.makeText(GetOrderDatactivity.this, String.format("%s", getString(R.string.order_not_found)), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GetOrderDatactivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnLoadingLastPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (!binding.editMagentoorder.getText().toString().isEmpty()) {
                    //binding.editMagentoorder.getText().toString()
                    //TODO SEARCH IF LAST ORDER NUMBER IS IN DB OR not
                    List<ItemsOrderDataDBDetails> itemsOrderDataDBDetailsList = database.userDao()
                            .CheckordernumberData_inlast(binding.editMagentoorder.getText().toString());
                    if (itemsOrderDataDBDetailsList.size() > 0) {
                        Intent i = new Intent(getApplicationContext(), AssignItemToPackagesActivity.class);
                        i.putExtra("AddNewPackageORAddForExistPackage", "New");
                        i.putExtra("OrderNumber", binding.editMagentoorder.getText().toString());
                        startActivity(i);
                    } else {
                        Toast.makeText(GetOrderDatactivity.this, "لايوجد أمر بيع سابق", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.editMagentoorder.setError(getResources().getString(R.string.enter));
                    binding.editMagentoorder.requestFocus();
                }*/

                LayoutInflater li = LayoutInflater.from(GetOrderDatactivity.this);
                View promptsView = li.inflate(R.layout.prompts_showordersnumber, null);

                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                        GetOrderDatactivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // create alert dialog
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

                final RecyclerView rv_ordernumbers = (RecyclerView) promptsView
                        .findViewById(R.id.rv_ordernmber);
                final TextView txt_title=(TextView) promptsView.findViewById(R.id.txt_title);
                txt_title.setText(R.string.choice_lastordernumber_that_youneed_to_load);
                OrdersnumberAdapter ordersnumberAdapter = new OrdersnumberAdapter(database.userDao().getOrdersAllordersNumberDB());
                Log.e(TAG, "onClick:listoforders "+database.userDao().getOrdersAllordersNumberDB().size() );
                rv_ordernumbers.setAdapter(ordersnumberAdapter);
                rv_ordernumbers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                ItemclickforRecycler.addTo(rv_ordernumbers).setOnItemClickListener(new ItemclickforRecycler.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // promptsView.
                        String Ordernumber=ordersnumberAdapter.ReturnListOfPackages().get(position);
                        Intent i = new Intent(getApplicationContext(), AssignItemToPackagesActivity.class);
                        i.putExtra("AddNewPackageORAddForExistPackage", "New");
                        i.putExtra("OrderNumber", Ordernumber);
                        startActivity(i);

                        alertDialog.dismiss();

                    }
                });
                // show it
                alertDialog.show();

            }


        });

        binding.btnPrintAwb.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {

                                                       LayoutInflater li = LayoutInflater.from(GetOrderDatactivity.this);
                                                       View promptsView = li.inflate(R.layout.prompts_showordersnumber, null);

                                                       androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                                                               GetOrderDatactivity.this);

                                                       // set prompts.xml to alertdialog builder
                                                       alertDialogBuilder.setView(promptsView);

                                                       // create alert dialog
                                                       androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

                                                       final RecyclerView rv_ordernumbers = (RecyclerView) promptsView
                                                               .findViewById(R.id.rv_ordernmber);
                                                        final TextView txt_title=(TextView) promptsView.findViewById(R.id.txt_title);
                                                       txt_title.setText(R.string.choice_ordernumber_that_youneed_to_print);
                                                       OrdersnumberAdapter ordersnumberAdapter = new OrdersnumberAdapter(database.userDao().getOrdersAllordersNumberDB());
                                                       Log.e(TAG, "onClick:listoforders "+database.userDao().getOrdersAllordersNumberDB().size() );
                                                       rv_ordernumbers.setAdapter(ordersnumberAdapter);
                                                       rv_ordernumbers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                                                       ItemclickforRecycler.addTo(rv_ordernumbers).setOnItemClickListener(new ItemclickforRecycler.OnItemClickListener() {
                                                           @Override
                                                           public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                                               // promptsView.
                                                               String Ordernumber=ordersnumberAdapter.ReturnListOfPackages().get(position);
                                                               UploadHeader(Ordernumber);
                                                               //UploadDetails(Ordernumber);

                                                               alertDialog.dismiss();

                                                           }
                                                       });
                                                       // show it
                                                       alertDialog.show();

                                                   }
                                               }
        );

        binding.btnEditPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (database.userDao().CheckItemsWithTrackingnumber().size() > 0) {
                    Intent GoTopackedPackages = new Intent(GetOrderDatactivity.this, EditPackagesActivity.class);
                    startActivity(GoTopackedPackages);
                } else {
                    Toast.makeText(GetOrderDatactivity.this, getResources().getString(R.string.noitem), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void LoadNewPurchaseOrder() {
        if (!binding.editMagentoorder.getText().toString().isEmpty()) {

           // List<ItemsOrderDataDBDetails> itemsOrderDataDBDetailsList = database.userDao().getDetailsTrackingnumberToUpload();
            List<OrderDataModuleDBHeader> orderDataModuleDBHeaderlist = database.userDao()
                    .getHeaderToUpload_list(binding.editMagentoorder.getText().toString());

            if (orderDataModuleDBHeaderlist.size() > 0) {
                new AlertDialog.Builder(GetOrderDatactivity.this)
                        .setTitle(getString(R.string.will_delete_last_order))
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // TODO will not delete order data with scane new one and delete will be by order number
                                database.userDao().deleteAllHeader(binding.editMagentoorder.getText().toString());
                                database.userDao().deleteAllOrderItems(binding.editMagentoorder.getText().toString());
                                database.userDao().deleteAllTrckingNumber(binding.editMagentoorder.getText().toString());
                                database.userDao().deleteAllOrderItems_scanned(binding.editMagentoorder.getText().toString());

                                GETOrderData();
                            }
                        })
                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }).show();
            } else {
                GETOrderData();
            }

        } else {
            binding.editMagentoorder.setError(getResources().getString(R.string.enter));
            binding.editMagentoorder.requestFocus();

        }
    }

    private void GETOrderData() {
        getOrderDataViewModel.fetchdata(binding.editMagentoorder.getText().toString());
    }


    private void ActionAfterGetData(ResponseGetOrderData responseGetOrderData) {

        OrderDataModuleDBHeader orderDataModuleDBHeader = new OrderDataModuleDBHeader(
                responseGetOrderData.getOrder_number(),
                responseGetOrderData.getOutBound_delivery(),
                responseGetOrderData.getCustomer().getName(),
                responseGetOrderData.getCustomer().getPhone_number(),
                responseGetOrderData.getCustomer().getCustomer_code(),
                responseGetOrderData.getCustomer().getAddress().getGovern(),
                responseGetOrderData.getCustomer().getAddress().getCity(),
                responseGetOrderData.getCustomer().getAddress().getDistrict(),
                responseGetOrderData.getCustomer().getAddress().getCustomer_address_detail(),
                responseGetOrderData.getDelivery().getDate(),
                responseGetOrderData.getDelivery().getTime(),
                responseGetOrderData.getGrand_total(),
                responseGetOrderData.getShipping_fees(),
                responseGetOrderData.getPicker_confirmation_time(),
                responseGetOrderData.getCurrency(), responseGetOrderData.getOut_From_Loc()
        );

        database.userDao().insertOrderHeader(orderDataModuleDBHeader);
        //  database.userDao().UpdateOutBoundDelievery(binding.editOutbounddelievery.getText().toString(),responseGetOrderData.getOrder_number());
        // TODO Need to insert order number in details table
        for (int i = 0; i < responseGetOrderData.getItemsOrderDataDBDetails().size(); i++) {
            //responseGetOrderData.getOrder_number()
            ItemsOrderDataDBDetails itemsOrderDataDBDetails = new ItemsOrderDataDBDetails(responseGetOrderData.getOrder_number(),
                    responseGetOrderData.getItemsOrderDataDBDetails().get(i).getName(),
                    responseGetOrderData.getItemsOrderDataDBDetails().get(i).getPrice(),
                    responseGetOrderData.getItemsOrderDataDBDetails().get(i).getQuantity(),
                    responseGetOrderData.getItemsOrderDataDBDetails().get(i).getSku(),
                    responseGetOrderData.getItemsOrderDataDBDetails().get(i).getUnite());

            Log.e(TAG, "zzz>> getPrice " + responseGetOrderData.getItemsOrderDataDBDetails().get(i).getPrice());

            database.userDao().insertOrderItem(itemsOrderDataDBDetails);
        }
        Log.e(TAG, "zzz>> currency " + responseGetOrderData.getItemsOrderDataDBDetails().size());
        Log.e(TAG, "zzz>> items size " + responseGetOrderData.getItemsOrderDataDBDetails().size());
        Log.e(TAG, "zzz>> Qty " + responseGetOrderData.getItemsOrderDataDBDetails().get(0).getQuantity());
        Log.e(TAG, "zzz>> sku " + responseGetOrderData.getItemsOrderDataDBDetails().get(0).getSku());
        // Toast.makeText(GetOrderDatactivity.this, responseGetOrderData.getOrder_number(), Toast.LENGTH_SHORT).show();
        //  Toast.makeText(GetOrderDatactivity.this, database.userDao().getHeader().size(), Toast.LENGTH_SHORT).show();
        // Log.e(TAG, "onChanged: ","ccc "+ database.userDao().getHeader().size());
//                        Toast.makeText(GetOrderDatactivity.this, database.userDao().getHeader().get(0).getOrder_number(), Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "onChanged: ","cccdd "+ database.userDao().getHeader().get(0).getOrder_number().toString());

        Intent i = new Intent(getApplicationContext(), AssignItemToPackagesActivity.class);
        i.putExtra("AddNewPackageORAddForExistPackage", "New");
        i.putExtra("OrderNumber", responseGetOrderData.getOrder_number());
        startActivity(i);
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

            getOrderDataViewModel.InsertOrderdataDetails(ordernumberselected, itemsOrderDataDBDetailsList, ShippingfeesPerItem);

            getOrderDataViewModel.mutableLiveData_Details.observe(GetOrderDatactivity.this, new Observer<Message>() {
                @Override
                public void onChanged(Message message) {
                    Toast.makeText(GetOrderDatactivity.this, "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onChanged: " + message.getMessage());
                }
            });
        } else {
            Toast.makeText(GetOrderDatactivity.this, getResources().getString(R.string.item_notselected), Toast.LENGTH_SHORT).show();
        }
    }

    public void UploadHeader(String ordernumberselected) {
//todo  we need quere to get list if quantity scanned not equaled to required quantity
        Log.e(TAG, "UploadHeader:ordernumberselected "+ordernumberselected );
        if (database.userDao().getAllItemsNotScannedORLessRequiredQTY(ordernumberselected).size() == 0) {

            OrderDataModuleDBHeader orderDataModuleDBHeader = database.userDao().getHeaderToUpload(ordernumberselected);
            List<String> NO_OF_PACKAGES =
                    database.userDao().getNoOfPackagesToUpload(orderDataModuleDBHeader.getOrder_number() + "%");
            Log.e(TAG, "UploadHeader:NO_OF_P " + NO_OF_PACKAGES.size());
            Log.e(TAG, "UploadHeader:shipping fees " + orderDataModuleDBHeader.getShipping_fees());
            Log.e(TAG, "UploadHeader:shipping feesDB " + database.userDao().getHeaderToUpload(ordernumberselected).getShipping_fees());

            /*Log.e(TAG, "zzUploadHeader:NO_OF_PAC: "+NO_OF_PACKAGES );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getOutBound_delivery() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCustomer_name() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCustomer_phone() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCustomer_code() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCustomer_address_govern() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCustomer_address_city() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCustomer_address_district() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCustomer_address_detail() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getDelivery_date() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getDelivery_time() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getPicker_confirmation_time() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getGrand_total() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getCurrency() );

            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getShipping_fees() );
            Log.e(TAG, "zzUploadHeader:OutBo: "+NO_OF_PACKAGES );
            Log.e(TAG, "zzUploadHeader:OutBo: "+orderDataModuleDBHeader.getOut_From_Loc() );*/
            getOrderDataViewModel.InsertOrderdataHeader(
                    orderDataModuleDBHeader.getOrder_number(),
                    orderDataModuleDBHeader.getOutBound_delivery(),
                    orderDataModuleDBHeader.getCustomer_name(),
                    orderDataModuleDBHeader.getCustomer_phone(),
                    orderDataModuleDBHeader.getCustomer_code(),
                    orderDataModuleDBHeader.getCustomer_address_govern(),
                    orderDataModuleDBHeader.getCustomer_address_city(),
                    orderDataModuleDBHeader.getCustomer_address_district(),
                    orderDataModuleDBHeader.getCustomer_address_detail(),
                    orderDataModuleDBHeader.getDelivery_date(),
                    orderDataModuleDBHeader.getDelivery_time(),
                    orderDataModuleDBHeader.getPicker_confirmation_time(),
                    orderDataModuleDBHeader.getGrand_total(),
                    orderDataModuleDBHeader.getCurrency(),
                    orderDataModuleDBHeader.getShipping_fees(),
                    String.valueOf(NO_OF_PACKAGES.size()),
                    orderDataModuleDBHeader.getOut_From_Loc()
                    ,database.userDao().getUserData_MU().getUser_id()
            );

            getOrderDataViewModel.mutableLiveData.observe(GetOrderDatactivity.this, new Observer<Message>() {
                @Override
                public void onChanged(Message message) {
                    Toast.makeText(GetOrderDatactivity.this, "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onChanged: " + message.getMessage());
                    Toast.makeText(GetOrderDatactivity.this, getResources().getString(R.string.doneforheader), Toast.LENGTH_SHORT).show();

                    UploadDetails(ordernumberselected);
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(GetOrderDatactivity.this, orderDataModuleDBHeader.getOrder_number());
                    //TODO Update status on magento
                    UpdateStatus(ordernumberselected);

                }
            });
            //TODO insert header error
            getOrderDataViewModel.mutableLiveData_InsertH_Error.observe(GetOrderDatactivity.this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Log.e(TAG, "onChanged:mutableLiveD  " + s);
                    if (s.contains("HTTP 400")) {
                        Toast.makeText(GetOrderDatactivity.this, String.format("%s",
                                getString(R.string.missingdata)), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GetOrderDatactivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(GetOrderDatactivity.this, getResources().getString(R.string.item_notselected), Toast.LENGTH_SHORT).show();
            ShowMissedBarcodesFun(ordernumberselected);
        }
    }

    private void ShowMissedBarcodesFun(String orderselected) {
        LayoutInflater li = LayoutInflater.from(GetOrderDatactivity.this);
        View promptsView = li.inflate(R.layout.prompts_showordersnumber, null);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                GetOrderDatactivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final RecyclerView rv_ordernumbers = (RecyclerView) promptsView
                .findViewById(R.id.rv_ordernmber);

        OrdersnumberAdapter ordersnumberAdapter = new OrdersnumberAdapter(database.userDao().getBarcodesAllItemsNotScannedORLessRequiredQTY(orderselected));
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

    public void UpdateStatus(String ordernumberselected) {
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
        OrderDataModuleDBHeader orderDataModuleDBHeader = database.userDao().getHeaderToUpload(ordernumberselected);
        getOrderDataViewModel.UpdateStatus(
                orderDataModuleDBHeader.getOrder_number(),
                "packed"
        );
        getOrderDataViewModel.mutableLiveData_UpdateStatus.observe(GetOrderDatactivity.this, new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                Toast.makeText(GetOrderDatactivity.this, "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onChanged: " + message.getMessage());

            }
        });
//        }else {
//            Toast.makeText(GetOrderDatactivity.this, "توجد عناصر لم يتم تعبئتها", Toast.LENGTH_SHORT).show();
//        }
    }


}