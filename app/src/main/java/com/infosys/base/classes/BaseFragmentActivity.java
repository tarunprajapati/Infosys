package com.infosys.base.classes;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infosys.R;
import com.infosys.base.MyApplication;
import com.infosys.base.interfaces.ServiceCallerInterface;
import com.infosys.managers.CheckInternet;
import com.infosys.webservices.RetroCallBack;
import com.infosys.webservices.ToastCustomClass;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseFragmentActivity extends AppCompatActivity implements ServiceCallerInterface {
    protected boolean isGranted = true;
    private Snackbar snack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MyApplication getMyApplication() {
        return ((MyApplication) getApplication());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @SuppressLint("NewApi")
    public void setTitleOnAction(String title, String subT, boolean back) {
        ActionBar actionBar = getSupportActionBar();
        try {
            if (actionBar != null) {
                actionBar.setTitle(title);
                actionBar.setSubtitle(subT);

                actionBar.setDisplayShowTitleEnabled(true);
                if (back) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                } else {
                    actionBar.setHomeButtonEnabled(false);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public FragmentTransaction getFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
        // transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up, R.anim.slide_out_down, R.anim.slide_in_up);
        return transaction;
    }


    public void replaceFragment(Fragment fragment, String tagValue, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentTransaction();
        if (addToBackStack)
            transaction.addToBackStack(tagValue);
        try {
            transaction.replace(R.id.content, fragment, tagValue).commit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    public void serviceCaller(BaseFragment fragment, String urlsArr[], HashMap<String, String> params, Boolean isPost, Boolean isFileUploading, int requestCode, boolean isShowProgress, int requestType) {
        CommonServiceCaller.newInstance(fragment.getActivity()).serviceCaller(fragment, urlsArr, params, isPost, isFileUploading, requestCode, isShowProgress, requestType);
    }


    public void serviceCaller(BaseFragment fragment, Call<ResponseBody> resBody, int requestCode, boolean progress, int requestType) {
        CommonServiceCaller.newInstance(fragment.getActivity()).serviceCaller(fragment, resBody, requestCode, progress, requestType);
    }

    public Boolean callBackFromApi(Object object, int requstCode) {
        return CommonServiceCaller.newInstance(this).isOk(object);
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
            askUserToUpdateApplicationSettings();
        }
    }

    public void askUserToUpdateApplicationSettings() {
        getSnackBar("You have denied required permissions, please update settings to continue work", Snackbar.LENGTH_INDEFINITE)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isGranted = true;
                        startInstalledAppDetailsActivity(BaseFragmentActivity.this);
                    }
                }).setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent)).show();
    }

    public Snackbar getSnackBar(String msg, int length) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), msg, length);
        return snack;
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }


    public void onShowNoInternetSnackBar(Call apiCall, RetroCallBack<ResponseBody> retroCallBack) {
        onShowNoInternetSnackBar(apiCall, retroCallBack, "No Internet connection");
    }

    public void onShowNoInternetSnackBar(final Call apiCall, final RetroCallBack<ResponseBody> retroCallBack, final String msg) {
        try {
            snack = getSnackBar(msg, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (apiCall != null && !apiCall.isCanceled())
                                apiCall.cancel();
                            if (retroCallBack != null && retroCallBack.getCall() != null && !retroCallBack.getCall().isCanceled())
                                retroCallBack.getCall().cancel();
                            dismissSnackBar();
                        }
                    })
                    .setAction(getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (apiCall != null && retroCallBack != null) {
                                apiCall.clone().enqueue(retroCallBack);
                            }
                            dismissSnackBar();
                        }
                    });
            snack.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowNoInternetSnackBar(final RequestQueue queue, final JsonObjectRequest reqJson) {
        try {
            snack = getSnackBar("No Internet connection", Snackbar.LENGTH_LONG).setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckInternet.newInstance().isConnectingToInternet(BaseFragmentActivity.this, queue, reqJson)) {
                        if (queue != null && reqJson != null) {
                            queue.add(reqJson);
                        }
                    }
                }
            });
            snack.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissSnackBar() {
        if (snack != null && snack.isShown())
            snack.dismiss();

        snack = null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyBoard(Context context, View myEditText) {
        if (myEditText == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
    }
}
