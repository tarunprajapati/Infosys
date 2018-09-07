package com.infosys.webservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;


import org.json.JSONObject;

import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitHelper {
    static Callback<ResponseBody> responseBodyCallback;

    static ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    static Context context;

    static ProgressDialog progressDialog;

    private static RetrofitCallback retrofitCallback;

    private static void setRetrofitCallback(RetrofitCallback retrofitCallback, Context con, String message) {
        context = con;
        if (Utils.isNetworkAvailable(context)) {
            try {
                if (!TextUtils.isEmpty(message)) {
                    progressDialog = Utils.showProgressDialog(context, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            RetrofitHelper.retrofitCallback = retrofitCallback;
        } else {
            RetrofitHelper.retrofitCallback = null;
            showNoInternetDialog(context);
        }
    }

    private static void showNoInternetDialog(Context context) {
        new AlertDialog.Builder(context).setMessage("No internet available!").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }


    private static Callback<ResponseBody> getResponseBodyCallback(final RetrofitCallback callback) {
        responseBodyCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // hide
                try {
                    getProgressDialog().dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.onRetroResponse(Utils.getJsonFromResponse(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // hide
                try {
                    getProgressDialog().dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    throw t;
                } catch (UnknownHostException throwable) {
                    throwable.printStackTrace();
                    showNoInternetDialog(context);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


            }
        };
        return responseBodyCallback;
    }


    public interface RetrofitCallback {
        void onRetroResponse(JSONObject jsonFromResponse);
    }

}
