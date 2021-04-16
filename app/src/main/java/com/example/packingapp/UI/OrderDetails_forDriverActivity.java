package com.example.packingapp.UI;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.packingapp.Adapter.DriverOrderpackagesAdapter;
import com.example.packingapp.Adapter.DriverOrderpackages_rejectAdapter;
import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.R;
import com.example.packingapp.UI.Fragments.ConfirmPasscodeFragment;
import com.example.packingapp.databinding.ActivityOrderDetailsForDriverBinding;
import com.example.packingapp.model.DriverModules.DriverPackages_Details_DB;
import com.example.packingapp.model.DriverModules.DriverPackages_Header_DB;
import com.example.packingapp.model.DriverModules.DriverPackages_Respones_Details_recycler;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.ResponseSms;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.viewmodel.OrderDetailsForDriverViewModel;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetails_forDriverActivity extends AppCompatActivity {
    private static final String TAG = "OrderDetails_forDriverA";
ActivityOrderDetailsForDriverBinding binding;
private static final int REQUEST_PHONE_CALL = 1;
    OrderDetailsForDriverViewModel orderDetailsForDriverViewModel;
    String CustomerPhone="01065551910";
    String CustomerPhoneZoiperCall="501065551910";
    Context context=OrderDetails_forDriverActivity.this;
    List<DriverPackages_Details_DB> driverPackages_details_dbList;
    List<DriverPackages_Details_DB> driverPackages_details_dbList_Reject;
    String Orderclicked="";
    AppDatabase database;
    DriverOrderpackagesAdapter driverOrderpackagesAdapter;
    DriverOrderpackages_rejectAdapter driverOrderpackagesAdapter_Reject;
    int mHOUR ,mMINUTE;
    int mAM_PM;
List<String> Reject_Resons_list ,Reschedule_Resons_list;
    int CountChecked;
    String TrackingnumberToReject;
    List<String> TrackingnumberToReject_list;
    int postion_ToReject;
    Boolean SendPasscode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderDetailsForDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        orderDetailsForDriverViewModel= ViewModelProviders.of(this).get(OrderDetailsForDriverViewModel.class);
        PermissionForCall();

        database= AppDatabase.getDatabaseInstance(this);
        setTitle(R.string.OrderDetails_forDriverActivity_label);
        Reject_Resons_list=new ArrayList<>();
        Reschedule_Resons_list=new ArrayList<>();
        Reject_Resons_list_fun_add();
        Reschedule_Resons_list_fun_add();
        if (getIntent().getExtras() != null){
            Orderclicked=getIntent().getExtras().getString("Orderclicked");
        }
        Log.e(TAG, "onItemClicked: "+ Orderclicked);

        GetCustomerDate_to_Text();
        PhoneAndSmsActions();
        Random random = new Random();
        int randomNumber = random.nextInt(1280 - 65) + 65;

        binding.btnRescheduleDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reschedule();
/*
                UpdateStatus_ON_83();
                UpdateStatus();*/
            }
        });

        binding.btnSendPasscodeToConfirmDeleivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onClick:randomNumber  "+ String.valueOf(randomNumber) );
                SendPasscode =true; 
                //ToDo Don't SendPasscode For Test Get asscode From log
                SendSMS(CustomerPhone, "Your OTP Is "+String.valueOf(randomNumber));

                database.userDao().UpdatePasscode(Orderclicked,String.valueOf(randomNumber));

            }
        });

        binding.btnRejectDeleivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DriverPackages_Details_DB> Trackingnumbers_List = driverOrderpackagesAdapter.ReturnListOfPackages();
                Log.e("btn_editChecked",""+Trackingnumbers_List.size());
                TrackingnumberToReject_list=new ArrayList<>();
                CountChecked =0;
                if (Trackingnumbers_List.size() != 0) {
                    for (int i = 0; i < Trackingnumbers_List.size(); i++) {
                        Boolean Checked = Trackingnumbers_List.get(i).getChecked_Item();
                        //Log.e("btn_editChecked",""+Checked);
                        if (Checked == true) {
                            //Log.e("btn_editCheckedif",""+Checked);
                            CountChecked += 1;
                         //   TrackingnumberToReject = Trackingnumbers_List.get(i).getTRACKING_NO();
                            TrackingnumberToReject_list.add(Trackingnumbers_List.get(i).getTRACKING_NO());
                            postion_ToReject=i;
                        }
                        if (i == (Trackingnumbers_List.size() - 1)) {
                            if (CountChecked < 1 ) {
                                Toast.makeText(OrderDetails_forDriverActivity.this, R.string.you_choice_noting, Toast.LENGTH_LONG).show();
                            } else if (CountChecked >= 1) {  //&& !BarCodeChecked.isEmpty()
                               RejectDialog(TrackingnumberToReject_list,postion_ToReject);

                            }

                        } /*else
                    Toast.makeText(EditPackagesActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();
                    */
                    }
                }
            }
        });
        //Get Order Details if it's not in local database
        if (database.userDao().getAllPckagesForUpload(Orderclicked).size() ==0) {
            Log.e(TAG, "onCreate:getAllPckagesForUpload = zero ");
            orderDetailsForDriverViewModel.ReadDriverRunsheetOrdersData(Orderclicked);


        }
        Log.e(TAG, "onCreate: "+ Orderclicked);
        driverPackages_details_dbList=new ArrayList<>();
        driverPackages_details_dbList_Reject=new ArrayList<>();

        OberverFUN();
        CreateORUpdateRecycleView();
        CreateORUpdateRecycleView_Reject();
    }

    private void GetCustomerDate_to_Text() {
        DriverPackages_Header_DB driverPackages_header_db = database.userDao().getDriverorder(Orderclicked);
        binding.txtCustomername.setText("الأسم: "+driverPackages_header_db.getCustomer_name());
        binding.txtCustomeraddress.setText("العنوان: "+driverPackages_header_db.getCustomer_address_detail());
        binding.txtCustomerphone.setText("رقم التلفون: "+driverPackages_header_db.getCustomer_phone());
    }

    private void OberverFUN() {
        orderDetailsForDriverViewModel.GetDriverOrdersReadyDetailsDataLiveData().observe(OrderDetails_forDriverActivity.this,
                new Observer<DriverPackages_Respones_Details_recycler>() {
                    @Override
                    public void onChanged(DriverPackages_Respones_Details_recycler driverPackages_respones_details_recycler) {

//                        DriverPackages_DB driverPackages_respones_recycler1=
//                                new  DriverPackages_DB(driverPackages_respones_recycler.getOrderNumber(),
//                                        driverPackages_respones_recycler.getCUSTOMER_PHONE());
                        //       database.userDao().deleteDriverPackages_Details_DB();
                        database.userDao().insertDriverPackages(driverPackages_respones_details_recycler.getRecords());
                        Log.e(TAG, "onChangedgetdata: "+driverPackages_respones_details_recycler
                                .getRecords().get(0).getTRACKING_NO() );
                        Log.e(TAG, "onChangedgetdatasi: "+driverPackages_respones_details_recycler
                                .getRecords().size() );

                        driverPackages_details_dbList.addAll(driverPackages_respones_details_recycler.getRecords());
//                        driverOrderpackagesAdapter.notifyDataSetChanged();
                        CreateORUpdateRecycleView();
                        CreateORUpdateRecycleView_Reject();
                       // totalPriceFUN(driverPackages_respones_details_recycler.getRecords());
                    }
                });

        orderDetailsForDriverViewModel.mutableLiveData_UpdateStatus_RescheduleTime_ON_83.observe(
                OrderDetails_forDriverActivity.this, new Observer<ResponseUpdateStatus>() {
                    @Override
                    public void onChanged(ResponseUpdateStatus message) {
                        Toast.makeText(OrderDetails_forDriverActivity.this, ""+message.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onChanged:update "+message.getMessage() );
                        UpdateStatus();

                    }
                });
        orderDetailsForDriverViewModel.mutable_UpdateStatus_RescheduleTime_ON_83LiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged:updateerror "+s );
                Toast.makeText(OrderDetails_forDriverActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
        orderDetailsForDriverViewModel.mutableLiveData_UpdateStatus.observe(OrderDetails_forDriverActivity.this, new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                Toast.makeText(OrderDetails_forDriverActivity.this, ""+message.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onChanged:UpdateStatusroub "+message.getMessage() );
                Intent GoBackafterSuccess=new Intent(OrderDetails_forDriverActivity.this,DriverMainActivity.class);
                startActivity(GoBackafterSuccess);
                finish();
            }
        });

        orderDetailsForDriverViewModel.mutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged:roub "+s );
                Toast.makeText(OrderDetails_forDriverActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void Reject_Resons_list_fun_add() {
        Reject_Resons_list.add(getResources().getString(R.string.choice_reason));

        Reject_Resons_list.add("العميل طلب الإلغاء دون ذكر أسباب");
        Reject_Resons_list.add("العميل طلب الإلغاء لمشكلة مع هايبروان");
        Reject_Resons_list.add("العميل طلب الإلغاء غير متاح في العنوان");
        Reject_Resons_list.add("عنوان العميل الصحيح خارج نطاق الخدمة");
        Reject_Resons_list.add("العميل طلب الإلغاء لعدم السماح بفتح الشحنة");
        Reject_Resons_list.add("العميل طلب التأجيل لمدة خارج النطاق المسموح");
        Reject_Resons_list.add("سوء الأحوال الجوية .. الطريق المؤدي لعنوان العميل");
        Reject_Resons_list.add("تأخرت عن  ميعاد التسليم");
        Reject_Resons_list.add("تلف الشحنة أثناء التوصيل");
        Reject_Resons_list.add("شك في تصرف العميل");
        Reject_Resons_list.add("التغليف غير مرضي للعميل");
        Reject_Resons_list.add("توفر المنتج الآن بسعر أقل عن وقت الطلب");
        Reject_Resons_list.add("تلف الشحنة من المخازن");
        Reject_Resons_list.add("إلغاء من قبل هايبروان");
        Reject_Resons_list.add("الشحنة..المنتج خطأ");

    }

    private void Reschedule_Resons_list_fun_add() {
        Reschedule_Resons_list.add(getResources().getString(R.string.choice_reason));
        //this reasons for reschedule
        Reschedule_Resons_list.add("عطل سيارة.. هاتفي المحمول .. ماكينة الدفع");
        Reschedule_Resons_list.add("سوء الأحوال الجوية .. الطريق المؤدي لعنوان العميل");
        Reschedule_Resons_list.add("العميل طلب التأجيل دون ذكر أسباب");
        Reschedule_Resons_list.add("العميل طلب التأجيل لحين حل مشكلة مع هايبروان");
        Reschedule_Resons_list.add("العميل يريد الطلب في عنوان في نطاق منطقة أخرى");
        Reschedule_Resons_list.add("عنوان العميل غير واضح ولا يرد على الهاتف");
        Reschedule_Resons_list.add("العميل طلب تجميع الطلبات");
        Reschedule_Resons_list.add("ظرف خاص");
        Reschedule_Resons_list.add("تأخرت عن  ميعاد التسليم");
        Reschedule_Resons_list.add("إعادة تغليف");
        Reschedule_Resons_list.add("شك في تصرف العميل");
        Reschedule_Resons_list.add("التأجيل من قبل هايبروان");
        Reschedule_Resons_list.add("عطل سيارة.. هاتفي المحمول .. ماكينة الدفع");

    }
    private void PhoneAndSmsActions() {
        DriverPackages_Header_DB driverPackages_header_db=
                database.userDao().getDriverorder(Orderclicked);
        //TODO adding my number to send sms and make call
        CustomerPhone=driverPackages_header_db.getCustomer_phone();
        CustomerPhone=CustomerPhone.replace("+2","");
        CustomerPhoneZoiperCall="5"+CustomerPhone;
        Log.e(TAG, "onCreate:vvv "+ CustomerPhone);

        binding.imgBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +CustomerPhoneZoiperCall ));
                startActivity(intent);
            }
        });

        binding.imgBtnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms();
            }
        });
    }

    public void PermissionForCall(){
        if (ActivityCompat.checkSelfPermission(OrderDetails_forDriverActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderDetails_forDriverActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
    }

    public void sms(){

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts_send_sms, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();



        final EditText edit_smsInput = (EditText) promptsView
                .findViewById(R.id.edit_smsInput);

        final Button btn_send_sms = (Button) promptsView
                .findViewById(R.id.btn_send_sms);

        btn_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // promptsView.

                if (!edit_smsInput.getText().toString().isEmpty()) {
                    SendPasscode=false;
                    SendSMS(CustomerPhone, edit_smsInput.getText().toString());
                    alertDialog.dismiss();

                }else{
                    if (edit_smsInput.getText().toString().isEmpty()){
                        edit_smsInput.setError(getResources().getString(R.string.enter_sms_body));
                    }
                }
            }

        });
        // show it
        alertDialog.show();

    }

    private void SendSMS(String CustomerPhone ,String SMSBody) {
        Log.e(TAG, "SendSMS: "+CustomerPhone );
        Log.e(TAG, "SendSMS:SMSBody "+SMSBody );
        orderDetailsForDriverViewModel.SendSms(CustomerPhone , SMSBody);

        orderDetailsForDriverViewModel.getSmsLiveData().observe(OrderDetails_forDriverActivity.this, new Observer<ResponseSms>() {
            @Override
            public void onChanged(ResponseSms responseSms) {
                Toast.makeText(OrderDetails_forDriverActivity.this,
                        responseSms.getSMSStatus().toString(), Toast.LENGTH_SHORT).show();
                if (SendPasscode ==true) {
                    ConfirmPasscodeFragment detialsfragment = new ConfirmPasscodeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Orderclicked", Orderclicked);
//                bundle.putString("UserName",UserName);
//                bundle.putString("Branch",Branch);
                    // bundle.putSerializable("LastOrderIdArray",LastOrderArry);

                    detialsfragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, detialsfragment);

                    // databaseHelper.update_PDNEWQTY(Po_Item_List.get(position).
                    // getBarcode1(),String.valueOf(Double.valueOf(Po_Item_List.get(position).getQuantity1())-1));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                SendPasscode = false;
            }
        });
        orderDetailsForDriverViewModel.mutableLiveData_sendSMS_Error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged:sensms "+s );
                Toast.makeText(OrderDetails_forDriverActivity.this, s, Toast.LENGTH_LONG).show();

//                if (s.equals("HTTP 503 Service Unavailable")) {
//                    Toast.makeText(OrderDetails_forDriverActivity.this, getResources().getString(R.string.invaliduser), Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    public void CreateORUpdateRecycleView(){

        driverPackages_details_dbList =database.userDao().getAllPckagesForNotReject(Orderclicked);

        driverOrderpackagesAdapter =new DriverOrderpackagesAdapter(driverPackages_details_dbList);
        binding.rvCustomerItems.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        binding.rvCustomerItems.setLayoutManager(mLayoutManager);
        binding.rvCustomerItems.setAdapter(driverOrderpackagesAdapter);

        totalPriceFUN(driverPackages_details_dbList);

//        ItemclickforRecycler.addTo(binding.rvCustomerItems).setOnItemClickListener(new ItemclickforRecycler.OnItemClickListener() {
//
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                //driverOrderpackagesAdapter.ReturnListOfPackages();
//                driverPackages_details_dbList =database.userDao().getAllPckagesForNotReject(Orderclicked);
//
//                RejectDialog(driverPackages_details_dbList.get(position).getTRACKING_NO(),position);
//            }
//        });
    }


    public void CreateORUpdateRecycleView_Reject(){

        driverPackages_details_dbList_Reject =database.userDao().getAllPckagesForReject(Orderclicked);
        Log.e(TAG, "CreateORUpdateRecycleView_Reject:size "+driverPackages_details_dbList_Reject.size() );
        driverOrderpackagesAdapter_Reject =new DriverOrderpackages_rejectAdapter(driverPackages_details_dbList_Reject);
        binding.rvCustomerRejectItems.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        binding.rvCustomerRejectItems.setLayoutManager(mLayoutManager);
        binding.rvCustomerRejectItems.setAdapter(driverOrderpackagesAdapter_Reject);

    }

    public void RejectDialog(List<String> RejectTrackingNumberList , int position){

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts_reason_of_reject, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        final TextView txt_tracking_number_reject = (TextView) promptsView
                .findViewById(R.id.txt_tracking_number_reject);
        txt_tracking_number_reject.setText("سوف يتم رفض"+RejectTrackingNumberList.size());

        final SearchableSpinner spiner_rejectinput = (SearchableSpinner) promptsView
                .findViewById(R.id.spiner_rejectinput);
        //TODO TO ASSIGN ID TO SPINNER
        ArrayAdapter<String> spinnerAdapterDriver=new ArrayAdapter<String>(OrderDetails_forDriverActivity.this,
                android.R.layout.simple_spinner_item,Reject_Resons_list);
        spinnerAdapterDriver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_rejectinput.setAdapter(spinnerAdapterDriver);

        final Button btn_reject = (Button) promptsView
                .findViewById(R.id.btn_reject);

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // promptsView.

                if (spiner_rejectinput.getSelectedItemPosition()!=0) {
                    //SendSMS(CustomerPhone, edit_rejectinput.getText().toString());
                    database.userDao().UpdatestatusAndReason_ForTrackingnumber(RejectTrackingNumberList,
                            "rejected_under_inspection",Reject_Resons_list.get(spiner_rejectinput.getSelectedItemPosition()));
                    driverPackages_details_dbList_Reject.add(driverPackages_details_dbList.get(position));
                    Log.e(TAG, "onClick: "+ driverPackages_details_dbList.size());
                    driverPackages_details_dbList.remove(position);
                    Log.e(TAG, "onClickre: "+ driverPackages_details_dbList_Reject.size());
//                    driverOrderpackagesAdapter.notifyDataSetChanged();
//                    driverOrderpackagesAdapter_Reject.notifyDataSetChanged();
                    CreateORUpdateRecycleView();
                    CreateORUpdateRecycleView_Reject();


                    alertDialog.dismiss();

                }else{
                    Toast.makeText(context, getResources().getString(R.string.choice_reason), Toast.LENGTH_SHORT).show();

                }
            }
        });
        // show it
        alertDialog.show();
    }

    public void UpdateStatus_Reschedule_ON_83(String Time ,String RescheduleReasone){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
                       Log.e(TAG, "UpdateStatus_ON_83: "+Orderclicked);
                Log.e(TAG, "UpdateStatus_ON_83: "+Time );
                Log.e(TAG, "UpdateStatus_ON_83: "+"ffff " );
                orderDetailsForDriverViewModel.UpdateStatus_RescheduleTime_ON_83(
                        Orderclicked,
                        "reschedule" ,Time,database.userDao().getUserData_MU().getUser_id(),
                        RescheduleReasone
                );


    }

    public void UpdateStatus(){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){

        List<RecievePackedModule> orderDataModuleDBHeaderkist = database.userDao().getorderNORecievePackedModule();


            orderDetailsForDriverViewModel.UpdateStatus(
                    Orderclicked,
                    "reschedule"
            );



    }
    void totalPriceFUN(    List<DriverPackages_Details_DB> driverPackages_details_dbList_price ){
        Double TotalPrice=0.0;
        for(int i=0 ; i<driverPackages_details_dbList.size();i++){
            TotalPrice+=Double.valueOf(driverPackages_details_dbList_price.get(i).getPACKAGE_PRICE());
            Log.e(TAG, "totalPriceFUN: "+ TotalPrice);
        }
        Log.e(TAG, "totalPriceFUN:driverPackages_details_dbList_pricesize "+ driverPackages_details_dbList_price.size());
        Log.e(TAG, "totalPriceFUN:driverPackages_details_dbListsize "+ driverPackages_details_dbList.size());

        binding.txtTotalPrice.setText(String.valueOf(Double.valueOf(new DecimalFormat("##0.00").format(TotalPrice))));
    }

    public void Reschedule(){

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts_reschedule, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText edit_rescheduleinput = (EditText) promptsView
                .findViewById(R.id.edit_reschedule);
//        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
//        edit_rescheduleinput.setText(timeStamp);

        final SearchableSpinner spiner_rescheduleinput = (SearchableSpinner) promptsView
                .findViewById(R.id.spiner_rescheduleinput);
        //TODO TO ASSIGN ID TO SPINNER
        ArrayAdapter<String> spinnerAdapterDriver=new ArrayAdapter<String>(OrderDetails_forDriverActivity.this,
                android.R.layout.simple_spinner_item,Reschedule_Resons_list);
        spinnerAdapterDriver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_rescheduleinput.setAdapter(spinnerAdapterDriver);

        edit_rescheduleinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO upgrate Api to 24 for this
                final Calendar c = Calendar.getInstance();
                mHOUR = c.get(Calendar.HOUR);
                mMINUTE = c.get(Calendar.MINUTE);
                mAM_PM = c.get(Calendar.AM_PM);  // 1 pm   0 am
                Log.e(TAG, "onClick:mHOUR "+mHOUR );
                Log.e(TAG, "onClick:mAM_PM "+mAM_PM );

                TimePickerDialog timePickerDialog= new TimePickerDialog(OrderDetails_forDriverActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (mAM_PM == 1 ) {
                        mHOUR+=12;
                        Log.e(TAG, "onClick:hourOfDay " + hourOfDay);
                        Log.e(TAG, "onClick:mHOUR " + mHOUR);
                        if (hourOfDay < 23 && hourOfDay != 00 && hourOfDay > mHOUR) {
                            edit_rescheduleinput.setError(null);
                            edit_rescheduleinput.setText(hourOfDay + ":" + minute);
                        } else {
                            if (hourOfDay > 23) {
                                edit_rescheduleinput.setError(getResources().getString(R.string.enter_reschedule_time_lesethan11));
                                edit_rescheduleinput.setText("");
                            }else if (hourOfDay < mHOUR){
                                edit_rescheduleinput.setError(getResources().getString(R.string.enter_reschedule_time_morethannow));
                                edit_rescheduleinput.setText("");
                            }
                        }
                    }else {
                        Log.e(TAG, "onClick:hourOfDay " + hourOfDay);
                        Log.e(TAG, "onClick:hourOfDay " + mHOUR);
                        if (/* hourOfDay < 23 &&*/ hourOfDay != 00 && hourOfDay < mHOUR) {
                            edit_rescheduleinput.setError(null);
                            edit_rescheduleinput.setText(hourOfDay + ":" + minute);
                        } else {
                            edit_rescheduleinput.setError(getResources().getString(R.string.enter_reschedule_time_morethannow));
                        }
                    }
                    }
                }, mHOUR, mMINUTE ,false);
                timePickerDialog.show();
               /* DatePickerDialog datePickerDialog = new DatePickerDialog(OrderDetails_forDriverActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int hour,
                                                  int minute) {
                                edit_rescheduleinput.setText(hour+ ":" + minute);
                            }
                        }, mHOUR, mMINUTE);
                datePickerDialog.show();*/
            }
        });


        final Button btn_reschedule = (Button) promptsView
                .findViewById(R.id.btn_reschedule);
        btn_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // promptsView.
                if (!edit_rescheduleinput.getText().toString().isEmpty() &&
                        spiner_rescheduleinput.getSelectedItemPosition() !=0 ) {
                    Log.e(TAG, "onClick:eee "+edit_rescheduleinput.getText().toString() );
                    UpdateStatus_Reschedule_ON_83(edit_rescheduleinput.getText().toString() ,
                            Reschedule_Resons_list.get(spiner_rescheduleinput.getSelectedItemPosition()));
                    //UpdateStatus();
                    alertDialog.dismiss();

                }else{
                    if (edit_rescheduleinput.getText().toString().isEmpty()){
                        edit_rescheduleinput.setError(getResources().getString(R.string.enter_reschedule_time));
                        edit_rescheduleinput.requestFocus();
                    }else if (spiner_rescheduleinput.getSelectedItemPosition() ==0){
                        edit_rescheduleinput.setError(getResources().getString(R.string.enter_reschedule_reason));
                        edit_rescheduleinput.requestFocus();
                    }
                }
            }
        });
        // show it
        alertDialog.show();
    }


}