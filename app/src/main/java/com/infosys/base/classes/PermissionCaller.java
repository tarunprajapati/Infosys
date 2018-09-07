package com.infosys.base.classes;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionCaller {

    BaseFragment fragment;
    Activity context;

    private PermissionCaller(Activity context) {
        this.context = context;
    }

    private PermissionCaller(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public static PermissionCaller getInstance(Activity context) {
        PermissionCaller com = new PermissionCaller(context);
        return com;
    }

    public static PermissionCaller getInstance(BaseFragment fragment) {
        PermissionCaller com = new PermissionCaller(fragment);
        return com;
    }

    public Boolean isListOfPermission(String fineReq[], int reqAll) {
        boolean isOk = true;
        StringBuilder perNeed = new StringBuilder();
        for (String per : fineReq) {
            Context con;
            if(context == null)
                con = fragment.getActivity();
            else
                con = context;
            if (!(ActivityCompat.checkSelfPermission(con, per) == PackageManager.PERMISSION_GRANTED)) {
                if (isOk)
                    isOk = false;
                perNeed.append(per);
                perNeed.append(",");
            }
        }

        if (isOk) {
            return true;
        }

        String permissions = (perNeed.length() > 1 ? perNeed.substring(0, perNeed.length() - 1).toString() : "");
        if (!permissions.isEmpty()) {
            String arrPer[] = permissions.split(",");
            if (context != null)
                reqAllPermissions(arrPer, reqAll);
            else
                reqAllPermissions(fragment, arrPer, reqAll);
        }

        return false;
    }

    public void reqAllPermissions(String permissions[], int fineReq) {
        ActivityCompat.requestPermissions(context, permissions, fineReq);
    }

    public void reqAllPermissions(BaseFragment fragment, String permissions[], int fineReq) {
        fragment.requestPermissions(permissions, fineReq);
    }
}
