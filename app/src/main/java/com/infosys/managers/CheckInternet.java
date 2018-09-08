package com.infosys.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infosys.base.classes.BaseFragmentActivity;


public class CheckInternet {
    private CheckInternet() {
    }

    public static CheckInternet newInstance() {
        return new CheckInternet();
    }

    public boolean isConnectingToInternet(Context context, RequestQueue queue, JsonObjectRequest reqJson) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        connectivity = null;
                        context = null;
                        info[i] = null;
                        return true;
                    }
                }
            }
        }
        ((BaseFragmentActivity) context).onShowNoInternetSnackBar(queue, reqJson);
        return false;
    }

    public synchronized boolean internetIsAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING) {
                return true;
            } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) {

                return true;
            } else {
                alertWarningMsg(context);
                return false;
            }
        }
    }

    public void alertWarningMsg(final Context context) {
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alert = new AlertDialog.Builder(context).create();
                    alert.setTitle("Warning !!!");
                    alert.setMessage("Please start internet first!");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    // Setting OK Button
                    alert.setButton(AlertDialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alert.setButton(AlertDialog.BUTTON_POSITIVE, "Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        }
                    });
                    // Showing Alert Message
                    alert.show();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
