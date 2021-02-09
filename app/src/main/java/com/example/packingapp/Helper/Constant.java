package com.example.packingapp.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant {
    private static final String TAG = "Constant";
    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();

    }

    public static boolean RegulerExpre_forTrackingNumbeer(String Trackingnumber) {
        Pattern my_pattern = Pattern.compile("[a-z ]");
        Matcher my_match = my_pattern.matcher(Trackingnumber);
        Log.e(TAG, "onClick: "+my_match.find() );
        if (!my_match.find()) {
            return true;
        }else {
            return false;
        }


    }


    public static void ToastDialoge(String status , Context context) {
        new androidx.appcompat.app.AlertDialog.Builder( context)
                .setTitle(status)
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // TODO will not delete order data with scane new one and delete will be by order number

                    }
                })
//                                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        dialog.cancel();
//                                    }
//                                })
                .show();
    }
}
