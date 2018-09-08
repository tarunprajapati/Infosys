package com.infosys.base.classes;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.infosys.base.MyApplication;
import com.infosys.base.interfaces.ServiceCallerInterface;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseFragment extends android.support.v4.app.Fragment implements ServiceCallerInterface {
    private String bundleArg = "argBundle";
    protected boolean isGranted = true;

    public boolean onBackPressedListener() {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //dismissSnackBar();
    }

    public MyApplication getMyApplication() {
        return ((MyApplication) getActivity().getApplication());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }


    @SuppressLint("NewApi")
    public void setTitleOnAction(String title, String subT, boolean back) {
        ((BaseFragmentActivity) getActivity()).setTitleOnAction(title, subT, back);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }


    public Boolean callBackFromApi(Object object, int requestCode) {
        return CommonServiceCaller.newInstance(getActivity()).isOk(object);
    }

    public void serviceCaller(BaseFragment fr, Call<ResponseBody> resBody, int requestCode, boolean progress, int requestType) {
        if(fr == null || fr.getActivity() == null)
            return;
        ((BaseFragmentActivity) fr.getActivity()).serviceCaller(fr, resBody, requestCode, progress, requestType);
    }

    public void serviceCaller(BaseFragment fr, String urlsArr[], HashMap<String, String> params, Boolean isPost, Boolean isFileUploading, int requestCode, boolean isShowProgress, int requestType) {
        ((BaseFragmentActivity) fr.getActivity()).serviceCaller(fr, urlsArr, params, isPost, isFileUploading, requestCode, isShowProgress, requestType);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        if (!isGranted) {
            //askUserToUpdateApplicationSettings();
        }
    }

    public void hideKeyBoard(Context context, View myEditText) {
        ((BaseFragmentActivity) getActivity()).hideKeyBoard(context, myEditText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}