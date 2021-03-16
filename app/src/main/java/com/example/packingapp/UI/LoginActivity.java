package com.example.packingapp.UI;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.Helper.TypefaceUtil;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityLoginBinding;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.ResponseLogin;
import com.example.packingapp.viewmodel.LoginViewModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    LoginViewModel lgoinViewModel;
    AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//       TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Ubuntu-Light.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Cairo-Light.otf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        binding.txtVerion.setText("V "+GetVersionOfApp());

        lgoinViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        database=AppDatabase.getDatabaseInstance(this);
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lgoinViewModel.fetchdata(binding.username.getText().toString(), binding.password.getText().toString());
            }
        });

        ObserverFUN();


        binding.txtForgetpasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPassword();
            }
        });
    }

    private void ObserverFUN() {

        lgoinViewModel.mutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("HTTP 503 Service Unavailable")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.invaliduser), Toast.LENGTH_LONG).show();
                }else {
                    Log.e(TAG, "onChanged:forgetpassword "+s );
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        lgoinViewModel.getSmsLiveData().observe(LoginActivity.this, new Observer<ResponseLogin>() {
            @Override
            public void onChanged(ResponseLogin responseLogin) {
                database.userDao().deleteAll();
                database.userDao().deleteAll_Modules();
                Log.e(TAG, "onChanged: "+responseLogin.getRecords().get(0).getGroupID() );
                Log.e(TAG, "onChanged: "+responseLogin.getModulesIDS().size() );

                database.userDao().insertUser(responseLogin.getRecords().get(0));
                database.userDao().insertModules(responseLogin.getModulesIDS());

                if (responseLogin.getRecords().get(0).getGroupID().equalsIgnoreCase("1")){
                    Intent i = new Intent(getApplicationContext(), GetOrderDatactivity.class);
                    startActivity(i);
                }else if (responseLogin.getRecords().get(0).getGroupID().equalsIgnoreCase("2")){
                    Log.e(TAG, "onChanged: else if 2 " );
                    Intent i = new Intent(getApplicationContext(), RecievedPackedAndSortedOrderForSortingAndDriverActivity.class);
                    i.putExtra("RecievePackedOrConfirmForDriver","RecievePacked");
                    startActivity(i);
                }else if (responseLogin.getRecords().get(0).getGroupID().equalsIgnoreCase("3")){
                    Log.e(TAG, "onChanged: else if 3 " );
                    Intent i = new Intent(getApplicationContext(), AssignPackedOrderForZoneAndDriverActivity.class);
                    startActivity(i);
                }else if (responseLogin.getRecords().get(0).getGroupID().equalsIgnoreCase("4")){
                    Log.e(TAG, "onChanged: else if 4 " );
                    Intent i = new Intent(getApplicationContext(), AddAndEditActivity.class);
                    startActivity(i);
                }else if (responseLogin.getRecords().get(0).getGroupID().equalsIgnoreCase("6")){
                    Log.e(TAG, "onChanged: else if 4 " );

                    Intent i = new Intent(getApplicationContext(), AddAndEditActivity.class);
                    startActivity(i);
                }else if (responseLogin.getRecords().get(0).getGroupID().equalsIgnoreCase("5")){
                    Log.e(TAG, "onChanged: else if 4 " );

                    Intent i = new Intent(getApplicationContext(), AdminstratorActivity.class);
                    startActivity(i);
                }

            }
        });


        lgoinViewModel.forgetpasswordmutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("HTTP 503 Service Unavailable")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.invaliduser), Toast.LENGTH_LONG).show();
                }else {
                    Log.e(TAG, "onChanged:forgetpassword "+s );
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }

            }
        });
        lgoinViewModel.getforgetpasswordLiveData().observe(LoginActivity.this, new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                Log.e(TAG, "onChanged:forgetpassword "+message.getMessage() );
                Toast.makeText(LoginActivity.this, ""+message.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String GetVersionOfApp() {
        String Version = "0.0";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
            Version = pInfo.versionName;
            Log.d(TAG, "checkVersion.DEBUG: App version: " + Version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Version;
    }


    private void ForgetPassword(){
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptsView = li.inflate(R.layout.prompts_resetpassword, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        final EditText edit_username = (EditText) promptsView
                .findViewById(R.id.edit_username);
        final EditText edit_password = (EditText) promptsView
                .findViewById(R.id.edit_password);
        final EditText edit_newpassword = (EditText) promptsView
                .findViewById(R.id.edit_newpassword);
        final EditText edit_confirmnewpassword = (EditText) promptsView
                .findViewById(R.id.edit_confirmnewpassword);

        final Button btn_reset = (Button) promptsView
                .findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // promptsView.

                if (!edit_username.getText().toString().isEmpty()
                && !edit_password.getText().toString().isEmpty()
                        && !edit_newpassword.getText().toString().isEmpty()
                        && !edit_confirmnewpassword.getText().toString().isEmpty()) {

                    if (edit_newpassword.getText().toString()
                            .equalsIgnoreCase(edit_confirmnewpassword.getText().toString())){
                        lgoinViewModel.forgetpassword(edit_username.getText().toString(),
                                edit_password.getText().toString(),
                                edit_newpassword.getText().toString());
                        alertDialog.dismiss();
                    }else {
                        edit_confirmnewpassword.setError(getResources().getString(R.string.confirm_new_password_err));
                    }


                }else{
                    if (edit_username.getText().toString().isEmpty()){
                        edit_username.setError(getResources().getString(R.string.enter_username));
                    }else if (edit_password.getText().toString().isEmpty()){
                        edit_password.setError(getResources().getString(R.string.enter_passcode));
                    }else if (edit_newpassword.getText().toString().isEmpty()){
                        edit_newpassword.setError(getResources().getString(R.string.enter_new_password_err));
                    }else if (edit_confirmnewpassword.getText().toString().isEmpty()){
                        edit_confirmnewpassword.setError(getResources().getString(R.string.enter_confirm_new_password_err));
                    }
                }
            }

        });
        // show it
        alertDialog.show();
    }
}