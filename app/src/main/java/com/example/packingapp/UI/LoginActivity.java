package com.example.packingapp.UI;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.Helper.Constant;
import com.example.packingapp.Helper.TypefaceUtil;
import com.example.packingapp.R;
import com.example.packingapp.Retrofit.ApiClient;
import com.example.packingapp.databinding.ActivityLoginBinding;
import com.example.packingapp.model.APKVersion;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.ResponseLogin;
import com.example.packingapp.viewmodel.LoginViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    LoginViewModel lgoinViewModel;
    AppDatabase database;
    List<String> VersionDataarray=new ArrayList<>();
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    Boolean UpdateDownloadORNot=false;
    private ProgressDialog mProgressDialog;
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
                if (Constant.isOnline(LoginActivity.this)) {
                    if (VersionDataarray.size() !=0) {
                        if (Double.valueOf(GetVersionOfApp()) < Double.valueOf(VersionDataarray.get(1))) {
                            Constant.ToastDialoge(getString(R.string.thereisnewversion), LoginActivity.this);
                        } else {
                            lgoinViewModel.fetchdata(binding.username.getText().toString(), binding.password.getText().toString());
                        }
                    }else {
                        Constant.ToastDialoge(getString(R.string.cantgetlastversionfromserver) ,LoginActivity.this );
                    }
                }else {
                    Constant.ToastDialoge(getString(R.string.no_internetconnection) ,LoginActivity.this );
                }
            }
        });

        GetVersion();
        Log.e("zzzVersionDataarray","oncreate "+VersionDataarray.size());

        ObserverFUN();


        binding.txtForgetpasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isOnline(LoginActivity.this)) {
                    ForgetPassword();
                }else {
                    Constant.ToastDialoge("No Internet connection" ,LoginActivity.this );
                }
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
/*
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
*/
                    Intent i = new Intent(getApplicationContext(), AdminstratorActivity.class);
                    startActivity(i);
 //               }

            }
        });


        lgoinViewModel.VersionmutableLiveDataError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("HTTP 503 Service Unavailable")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.unenabletogetlastversion), Toast.LENGTH_LONG).show();
                }else {
                    Log.e(TAG, "onChanged:version "+s );
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        lgoinViewModel.getversionLiveData().observe(LoginActivity.this, new Observer<APKVersion>() {
            @Override
            public void onChanged(APKVersion apkVersion) {
                VersionDataarray.add(apkVersion.getVersion_Code());
                Log.d("onResponse0:VersionCode", VersionDataarray.get(0));
                VersionDataarray.add(apkVersion.getVersion_Name()) ;
                Log.d("onResponse1:VersioName", VersionDataarray.get(1));
                RequestRunTimePermission();
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

    private void GetVersion(){
        lgoinViewModel.GetVersion();
    }

    private String GetVersionOfApp() {
        String Version = "0.0";
        int VersionCode=0;
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
            Version = pInfo.versionName;
            VersionCode = pInfo.versionCode;
            Log.d(TAG, "checkVersion.DEBUG: App versionname: " + Version);
            Log.d(TAG, "checkVersion.DEBUG: App versionCode: " + VersionCode );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Version;
    }
    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ///  Toast.makeText(LoginActivity.this,"أذن كتابه الى الكارت", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {
//                    GetVersionFromServer();

                    Toast.makeText(LoginActivity.this, "تم أعطاء الأذن", Toast.LENGTH_LONG).show();
                    Log.e("zzzVersionDataarray", "dfdf" + VersionDataarray.size());
                    Log.e("zzzVersionDataarray", "  " + Double.valueOf(GetVersionOfApp()));
                    Log.e("zzzVersionDataarray", "  " + Double.valueOf(VersionDataarray.get(0)));
                    if (VersionDataarray.size() != 0) {
                        //TODO check more than
                        //   if (!GetVersionOfApp().equalsIgnoreCase(VersionDataarray.get(0))) {
                        if (Double.valueOf(GetVersionOfApp()) < Double.valueOf(VersionDataarray.get(1))) {

                            Toast.makeText(this, "هناك تحديث", Toast.LENGTH_SHORT).show();
//                        DownloadData(Uri.parse(Constant.ApksURL));
//                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//                        registerReceiver(downloadReceiver, filter);

//                            InstallApk();
                            if (!VersionDataarray.get(1).equalsIgnoreCase("")) {
                                Log.e(TAG, "onRequestPermissionsResult:URL "+ ApiClient.ApksURL_ًWithoutName+"PackingApp_V"+VersionDataarray.get(1)+".apk" );
                                new DownloadFileFromURL().execute(ApiClient.ApksURL_ًWithoutName+"PackingApp_V"+VersionDataarray.get(1)+".apk","PackingApp_V"+VersionDataarray.get(1)+".apk");
                            } else {
                                Toast.makeText(this, "لم يتم الحصول على الاسم الاصدار من السيرفر", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.e("zzzVersionDataarray", "else VersionDataarray.size() =0");
                    }
                } else {

                    Toast.makeText(LoginActivity.this, "تم إلغاء الأذن", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void InstallApk() {

        PackageManager m = getPackageManager();

        String s = getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        s = p.applicationInfo.dataDir;
        Log.e("zzapplication", "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        File toInstall = new File(Environment.getExternalStorageDirectory() + File.separator + "PackingApp_V"+VersionDataarray.get(1)+".apk" );
        Log.e(TAG, "InstallApk:path "+toInstall.getPath() );
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(this, "com.example.packingapp.fileprovider", toInstall);

            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, false);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setData(apkUri);

        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri,
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            Log.e("zzzf_urldoInBac", "" + f_url[0]);
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/" + f_url[1]);

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                UpdateDownloadORNot = true;

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                UpdateDownloadORNot = false;
//                Toast.makeText(LoginActivity.this, "لم يتم التتصال بالسيرفر لتحميل اخر تحديث", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            Log.e("zzzonPostExecute", "onPostExecute");
            if (UpdateDownloadORNot == true) {
                InstallApk();
            } else if (UpdateDownloadORNot == false) {
                Toast.makeText(LoginActivity.this, "لم يتم الأتصال بالسيرفر لتحميل اخر تحديث", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
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