package com.hal.mfgsystems;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Global {

    public static String LOGD_USER = "Logd_User";

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getCurrentDateTime(){
        return sdf.format(new Date());
    }

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static String getCurrentDateTimeNoSpace(){
        return sdf2.format(new Date());
    }

    private static SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
    public static String getTodayForUI() {
        return dt1.format(new Date());
    }


    public static boolean checkNetwork(Context context){
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        }catch (Exception ee){
            Toast.makeText(context, "network connection failed "+ee.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        return connected;
    }

    public static void ShowSnackBar(ViewGroup lvparent, Context ctx, String message) {
        Snackbar.make(lvparent, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(ctx.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }


}
