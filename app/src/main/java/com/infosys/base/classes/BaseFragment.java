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
    protected static final String ARG_SECTION_NUMBER = "section_number";
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NewApi")
    public void setTitleOnAction(String title, String subT, boolean back) {
        ((BaseFragmentActivity) getActivity()).setTitleOnAction(title, subT, back);
    }

    @SuppressLint("NewApi")
    public void setTitleOnAction(String title, String subT, int backIcon) {
        ((BaseFragmentActivity) getActivity()).setTitleOnAction(title, subT, backIcon);
    }

    public void startMyActivity(Class targetActivity, Bundle myBundle) {
        Intent intent = new Intent(getActivity(), targetActivity);
        if (myBundle != null)
            intent.putExtra(bundleArg, myBundle);
        startActivity(intent);
    }

    public void startMyActivityForResult(Class targetActivity, Bundle myBundle, int requestCode) {
        Intent intent = new Intent(getActivity(), targetActivity);
        if (myBundle != null)
            intent.putExtra(bundleArg, myBundle);
        startActivityForResult(intent, requestCode);
    }


    public void addFragment(Fragment fragment, String value, boolean addToBackStack) {
        ((BaseFragmentActivity) getActivity()).addFragment(fragment, value, addToBackStack);
    }

    public void replaceFragment(Fragment fragment, String tag) {
        ((BaseFragmentActivity) getActivity()).replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment, String tag, boolean backStack) {
        ((BaseFragmentActivity) getActivity()).replaceFragment(fragment, tag, backStack);
    }

    public void replaceFragment(android.app.ListFragment fragment, String tag) {
        ((BaseFragmentActivity) getActivity()).replaceFragment(fragment);
    }


    protected void removeTopFragment() {
        ((BaseFragmentActivity) getActivity()).removeTopFragement();
    }

    protected void removeFragment(BaseFragment bfr) {
        ((BaseFragmentActivity) getActivity()).removeFragment(bfr);
    }

    protected void clearBackStackAllFragments() {
        ((BaseFragmentActivity) getActivity()).clearBackStackAllFragments();
    }

    public Fragment getFragmentByTag(String tAG) {
        if (getActivity() == null || getActivity().getSupportFragmentManager() == null)
            return null;
        else
            return getActivity().getSupportFragmentManager().findFragmentByTag(tAG);
    }

    protected boolean removeBackButton() {
        ((BaseFragmentActivity) getActivity()).removeBackButton();
        /*ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }*/
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    protected ActionBar getActionBar() {
        return ((BaseFragmentActivity) getActivity()).getSupportActionBar();
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