package com.example.packingapp.UI.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.R;
import com.example.packingapp.UI.DriverMainActivity;
import com.example.packingapp.UI.OrderDetails_forDriverActivity;
import com.example.packingapp.databinding.FragmentConfirmPasscodeBinding;
import com.example.packingapp.model.DriverModules.DriverPackages_Details_DB;
import com.example.packingapp.model.DriverModules.DriverPackages_Header_DB;
import com.example.packingapp.model.GetOrderResponse.OrderDataModuleDBHeader;
import com.example.packingapp.model.RecievePacked.RecievePackedModule;
import com.example.packingapp.model.ResponseSms;
import com.example.packingapp.model.ResponseUpdateStatus;
import com.example.packingapp.viewmodel.ConfirmPasscodeViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmPasscodeFragment extends Fragment {
FragmentConfirmPasscodeBinding binding;
    private View mLeak;
    ConfirmPasscodeViewModel confirmPasscodeViewModel;
    AppDatabase database;
    String Orderclicked="";
    private static final String TAG = "ConfirmPasscodeFragment";
    String Passcode="";
    CountDownTimer yourCountDownTimer;
    public ConfirmPasscodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() !=null) {
            Orderclicked = getArguments().getString("Orderclicked");
        }else {
            Toast.makeText(getActivity(), "Orderclicked  Not founf", Toast.LENGTH_SHORT).show();
        }
       // LastOrderArry = (ArrayList<String>) getArguments().getSerializable("LastOrderIdArray");
        binding = FragmentConfirmPasscodeBinding.inflate(inflater, container, false);
        mLeak = binding.getRoot();
        confirmPasscodeViewModel= ViewModelProviders.of(this).get(ConfirmPasscodeViewModel.class);

        database= AppDatabase.getDatabaseInstance(getActivity());


        return mLeak;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        btn_edit=view.findViewById(R.id.btn_edit);
       /* btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_PDNEWQTY();
            }
        });
        btn_delete=view.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_PDNEWQTY();
            }
        });*/
         //databaseHelper=new DatabaseHelper(getActivity());

//            Intent goback=new Intent(getActivity(), MainActivity.class);
//            goback.putExtra("UserName",UserName);
//            goback.putExtra("Branch",Branch);
//            goback.putExtra("LastOrderId",LastOrderId);
//
//            startActivity(goback);

        Passcode= database.userDao().getPasscode(Orderclicked);

        binding.imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                fm.popBackStack();

                OrderDetails_forDriverActivity orderDetails_forDriverActivity=(OrderDetails_forDriverActivity) getActivity();
                if (orderDetails_forDriverActivity != null){
                 //   mainActivity.CreateORUpdateRecycleView(2);
                    Log.e("nnnnnnnnn","");
                }
                yourCountDownTimer.cancel();
//                Intent goback=new Intent(getActivity(), MainActivity.class);
//                goback.putExtra("UserName",UserName);
//                goback.putExtra("Branch",Branch);
//                goback.putExtra("LastOrderId",LastOrderId);
//
//                startActivity(goback);
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //  Rejected under inspection
                //Reschedule
                //Has-Been-Delivered
               if (Passcode.equalsIgnoreCase(binding.editPasscode.getText().toString())) {
                   //get list of packages that rejected by checking for reason equal nullir empty
                   List<DriverPackages_Details_DB> driverPackages_details_dbList_rejected  =database.userDao().getAllPckagesForReject(Orderclicked);
                   // update status
                   database.userDao().UpdatestatusForNotRejectWhenClickConfirmed(Orderclicked,"has_been_delivered");

                   List<DriverPackages_Details_DB> driverPackages_details_dbList  =database.userDao().getAllPckagesForUpload(Orderclicked);
                   Log.e(TAG, "onClick: "+driverPackages_details_dbList.size() );

                   if (driverPackages_details_dbList_rejected.size() >0 ){
                       UpdateStatus_Passcode_Header_ON_83("rejected_under_inspection");
                       //TODO list for tracking number and reason and status for details _check file name
//                       UpdateStatus_Reason_Details_ON_83(driverPackages_details_dbList);
//                       UpdateStatus("rejected_under_inspection");
                   }else {
                       UpdateStatus_Passcode_Header_ON_83("has_been_delivered");
                       //TODO list for tracking number and reason and status for details
//                       UpdateStatus_Reason_Details_ON_83(driverPackages_details_dbList);
//                       UpdateStatus("has_been_delivered");
                   }
//                   OrderDetails_forDriverActivity orderDetails_forDriverActivity = (OrderDetails_forDriverActivity) getActivity();
//                   if (orderDetails_forDriverActivity != null) {
//                       //   mainActivity.CreateORUpdateRecycleView(2);
//                       Log.e("nnnnnnnnn", "");
//                   }
               }else {
                   binding.editPasscode.setError("Incorrect passcode");
               }

//                FragmentManager fm=getActivity().getSupportFragmentManager();
//                fm.popBackStack();
//
//                OrderDetails_forDriverActivity orderDetails_forDriverActivity=(OrderDetails_forDriverActivity) getActivity();
//                if (orderDetails_forDriverActivity != null){
//                    //   mainActivity.CreateORUpdateRecycleView(2);
//                    Log.e("nnnnnnnnn","");
//                }
//                Intent goback=new Intent(getActivity(), MainActivity.class);
//                goback.putExtra("UserName",UserName);
//                goback.putExtra("Branch",Branch);
//                goback.putExtra("LastOrderId",LastOrderId);
//
//                startActivity(goback);
            }
        });

        ObserveFUN();
        ResendPaascodWithCounter();
    }

    private void ResendPaascodWithCounter() {
        binding.txtResend.setVisibility(View.GONE);
        binding.txtCounter.setVisibility(View.VISIBLE);
        yourCountDownTimer=  new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.txtCounter.setText(getResources().getString(R.string.resend_within) + millisUntilFinished / 1000);
                binding.txtCounter.setTextColor(getResources().getColor(R.color.txt_color));
            }

            public void onFinish() {
                binding.txtResend.setVisibility(View.VISIBLE);
                binding.txtCounter.setVisibility(View.GONE);
            }
        }.start();

        binding.txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DriverPackages_Header_DB> driverPackages_header_dbs= database.userDao().CheckifThereIsPasscodeORNot(Orderclicked);
                Log.e(TAG, "onClick:getCustomer_phone "+driverPackages_header_dbs.get(0).getCustomer_phone().replace("+2","") );
                Log.e(TAG, "onClick:getPasscode "+driverPackages_header_dbs.get(0).getPasscode() );
                SendSMS(driverPackages_header_dbs.get(0).getCustomer_phone().replace("+2","") , "Your OTP Is " + driverPackages_header_dbs.get(0).getPasscode());
                ResendPaascodWithCounter();
            }
        });
    }

    private void ObserveFUN() {
        confirmPasscodeViewModel.getmutableLiveData_UpdateStatus().observe(getViewLifecycleOwner(), new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                Toast.makeText(getActivity(), ""+message.getMessage(), Toast.LENGTH_SHORT).show();
//                Constant.ToastDialoge( message.getMessage() , getActivity());
                Log.e(TAG, "onChanged:ro "+message.getMessage() );
                //ToDo Last Function
                Log.e(TAG, "onChanged:goback " );

                AlertDialog a = new AlertDialog.Builder( getContext())
                        .setTitle(getString(R.string.order_status_updated_last))
                        .setPositiveButton("??????????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                // TODO will not delete order data with scane new one and delete will be by order number
                                OrderDetails_forDriverActivity orderDetails_forDriverActivity=(OrderDetails_forDriverActivity) getActivity();
                                if (orderDetails_forDriverActivity != null) {
                                    Intent GoToDriverMain = new Intent(orderDetails_forDriverActivity, DriverMainActivity.class);
                                    startActivity(GoToDriverMain);
                                    orderDetails_forDriverActivity.finish();
                                }else {
                                    Log.e(TAG, "onClick:orderDetails_forDriverActivity == null " );

                                }
                            }
                        })
//                                .setNegativeButton("??????????", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        dialog.cancel();
//                                    }
//                                })
                        .show();

                a.setCanceledOnTouchOutside(false);
                a.setCancelable(false);

            }
        });




        confirmPasscodeViewModel.mutableLiveDataError_rou.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged:rou _error "+s );

//                new androidx.appcompat.app.AlertDialog.Builder( getContext())
//                        .setTitle(getString(R.string.order_status_updated_last))
//                        .setPositiveButton("??????????", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                // TODO will not delete order data with scane new one and delete will be by order number
//                                Intent GoToDriverMain=new Intent(getActivity(), DriverMainActivity.class);
//                                startActivity(GoToDriverMain);
//                                getActivity().finish();
//                            }
//                        })
////                                .setNegativeButton("??????????", new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int whichButton) {
////                                        dialog.cancel();
////                                    }
////                                })
//                        .show();
            }
        });

        confirmPasscodeViewModel.getmutableLiveData_UpdateStatus_PASSCODE_ON_83LiveData().observe(getViewLifecycleOwner(), new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
                //    Toast.makeText(getActivity(), "ConfirmedH", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), ""+message.getMessage(), Toast.LENGTH_SHORT).show();
//                Constant.ToastDialoge( message.getMessage() , getActivity());

//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.popBackStack();
                Log.e(TAG, "onChanged:update passcode for header " + message.getMessage());
                List<DriverPackages_Details_DB> driverPackages_details_dbList_rejected  =database.userDao().getAllPckagesForReject(Orderclicked);
                List<DriverPackages_Details_DB> driverPackages_details_dbList  =database.userDao().getAllPckagesForUpload(Orderclicked);

                if (driverPackages_details_dbList_rejected.size() >0 ){
//                    UpdateStatus_Passcode_Header_ON_83("Rejected under inspection");
                    //TODO list for tracking number and reason and status for details _check file name
                    UpdateStatus_Reason_Details_ON_83(driverPackages_details_dbList);
                    UpdateStatus("rejected_under_inspection");
                }else {
//                    UpdateStatus_Passcode_Header_ON_83("Has-Been-Delivered");
                    //TODO list for tracking number and reason and status for details
                    UpdateStatus_Reason_Details_ON_83(driverPackages_details_dbList);
                    UpdateStatus("has_been_delivered");
                }

            }
        });

        confirmPasscodeViewModel.mutableLiveDataError_UpdateStatus_Reason_ON_83.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged:UpdateStatus_Reason_ON_83_errror "+s );
            }
        });

        confirmPasscodeViewModel.getmutableLiveData_UpdateStatus_Reason_ON_83().observe(getViewLifecycleOwner(), new Observer<ResponseUpdateStatus>() {
            @Override
            public void onChanged(ResponseUpdateStatus message) {
//                Toast.makeText(getActivity(), "ConfirmedD", Toast.LENGTH_SHORT).show();
//                Constant.ToastDialoge( message.getMessage() , getActivity());

//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.popBackStack();
                Log.e(TAG, "onChanged:updateErrorDet " + message.getMessage());
                List<DriverPackages_Details_DB> driverPackages_details_dbList_rejected  =database.userDao().getAllPckagesForReject(Orderclicked);

//                if (driverPackages_details_dbList_rejected.size() >0 ){
////                    UpdateStatus_Passcode_Header_ON_83("Rejected under inspection");
//                    //TODO list for tracking number and reason and status for details _check file name
////                    UpdateStatus_Reason_Details_ON_83(driverPackages_details_dbList);
//                    UpdateStatus("rejected_under_inspection");
//                }else {
////                    UpdateStatus_Passcode_Header_ON_83("Has-Been-Delivered");
//                    //TODO list for tracking number and reason and status for details
////                    UpdateStatus_Reason_Details_ON_83(driverPackages_details_dbList);
//                    UpdateStatus("has_been_delivered");
//                }
            }
        });

        confirmPasscodeViewModel.getSmsLiveData().observe(getViewLifecycleOwner(), new Observer<ResponseSms>() {
            @Override
            public void onChanged(ResponseSms responseSms) {
                Toast.makeText(getActivity(),
                        responseSms.getSMSStatus().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        confirmPasscodeViewModel.mutableLiveData_sendSMS_Error.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged:sensms "+s );
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

//                if (s.equals("HTTP 503 Service Unavailable")) {
//                    Toast.makeText(OrderDetails_forDriverActivity.this, getResources().getString(R.string.invaliduser), Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

    private void SendSMS(String CustomerPhone ,String SMSBody) {
        Log.e(TAG, "SendSMS: "+CustomerPhone );
        Log.e(TAG, "SendSMS:SMSBody "+SMSBody );
        confirmPasscodeViewModel.SendSms(CustomerPhone , SMSBody);
    }


    public void UpdateStatus_Passcode_Header_ON_83(String Status) {
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
        List<RecievePackedModule> orderDataModuleDBHeaderkist = database.userDao().getorderNORecievePackedModule();
        if (Orderclicked != null) {
            confirmPasscodeViewModel.UpdateOrderStatus_Passcode_Header_ON_83(
                    Orderclicked,
                    Passcode, Status,database.userDao().getUserData_MU().getUser_id()
            );
            Log.e(TAG, "UpdateStatus_zone_ON_83 zzzo : " + Orderclicked);
            Log.e(TAG, "UpdateStatus_zone_ON_83 zzzpa : " + Passcode);
            Log.e(TAG, "UpdateStatus_zone_ON_83 zzzsta : " + Status);


        } else {
            Toast.makeText(getActivity(), "???? ?????????? .. ???????? ?????? ???????? ", Toast.LENGTH_SHORT).show();
        }

    }

    public void UpdateStatus_Reason_Details_ON_83(List<DriverPackages_Details_DB> driverPackages_details_dbList) {
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
        List<RecievePackedModule> orderDataModuleDBHeaderkist = database.userDao().getorderNORecievePackedModule();

        if (Orderclicked != null) {
            confirmPasscodeViewModel.UpdateOrderStatus_Reason_Details_ON_83(driverPackages_details_dbList);
            Log.e(TAG, "UpdateStatus_zone_ON_83 ErrorDet : " + driverPackages_details_dbList.size());


        } else {
            Toast.makeText(getActivity(), "???? ?????? ?????????? .. ???????? ?????? ???????? ", Toast.LENGTH_SHORT).show();
        }

    }


    public void UpdateStatus(String status){
//        if (database.userDao().getAllItemsWithoutTrackingnumber().size() == 0){
        OrderDataModuleDBHeader orderDataModuleDBHeader = database.userDao().getHeaderToUpload(Orderclicked);
        confirmPasscodeViewModel.UpdateStatus(
                Orderclicked,
                status
        );

//        }else {
//            Toast.makeText(GetOrderDatactivity.this, "???????? ?????????? ???? ?????? ??????????????", Toast.LENGTH_SHORT).show();
//        }
    }

}
