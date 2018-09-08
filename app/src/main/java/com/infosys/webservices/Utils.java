package com.infosys.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.infosys.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Utils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        try {
            progressDialog.show();
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return progressDialog;
    }



    public static JSONObject getJsonFromResponse(Response<ResponseBody> response) {

        JSONObject responseJsonObject = null;
        try {
            if (response.body() != null) {
                responseJsonObject = new JSONObject(response.body().string());
            } else if (response.errorBody() != null) {
                responseJsonObject = new JSONObject(response.errorBody().string());
            }
            if (BuildConfig.DEBUG) {
                Log.d("Service response: ", responseJsonObject.toString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                responseJsonObject = new JSONObject(response.errorBody().string());
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return responseJsonObject;
    }

}
