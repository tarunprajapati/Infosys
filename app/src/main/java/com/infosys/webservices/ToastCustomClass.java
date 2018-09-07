package com.infosys.webservices;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastCustomClass {
    public static void showToast(Context activity, String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastShort(Context activity, String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
