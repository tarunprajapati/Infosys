package com.infosys.webservices;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infosys.R;
import com.infosys.activities.Feed;
import com.infosys.base.classes.BaseFragmentActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Custom call back for retrofit
 * <p/>
 * Created by ayush on 1/9/16.
 */
public abstract class RetroCallBack<T> implements Callback<T> {
    LoadType loadType;
    Gson gson;
    RetroCallBack retroCallBack;
    private Call<T> call = null;
    Context context;
    private String message;

    public Call<T> getCall() {
        return call;
    }

    public void setCall(Call<T> call) {
        this.call = call;
    }

    public void cancelCall() {
        try {
            if (this.call != null && !this.call.isCanceled())
                this.call.cancel();
            call = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public RetroCallBack(Context context) {
        this(context, null);
    }

    /**
     * takes only loadtype because it will either show no or normal progress bar with message
     *
     * @param loadType
     */
    public RetroCallBack(Context context, LoadType loadType) {
        this(context, loadType, null);
    }

    /**
     * takes loadtype and message and may be it will show progress dialog with message
     *
     * @param loadType
     * @param message
     */
    public RetroCallBack(Context context, LoadType loadType, String message) {
        // show loading indicator when calling API
        this.context = context;
        gson = new Gson();
        setCall(null);

        if (loadType == null && message == null)
            return;

        setLoadType(loadType);

        if (message == null) {
            setMessage(null);
//            ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), true));
        } else {
            setMessage(message);
            //          ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), true, getMessage()));
        }
    }


    /**
     * takes loadtype and message and may be it will show progress dialog with message
     *
     * @param loadType
     * @param message
     * @param showLoading
     */
    public RetroCallBack(LoadType loadType, String message, boolean showLoading) {
        // show loading indicator when calling API
        gson = new Gson();
        setCall(null);

        setLoadType(loadType);

        if (message == null) {
            setMessage(null);
            // ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), true));
        } else {
            setMessage(message);
            // ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), true, getMessage()));
        }
    }


    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return context.getString(R.string.didntWrk);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    //    public void onSuccess(Response<T> response) {
//        LogWrapper.v("onResponse called");
//        EventBus.getDefault().post(new ShowLoadingView(getLoadType(), false));
//    }
    public void onSuccess(ResponseModel responseModel, JSONObject jsonObject) {
        LogWrapper.v("onResponse called");
        //  ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), false));
    }

    /**
     * Called when either API call fails and response has status 0.
     *
     * @param model
     */
    public void onFailed(ResponseModel model) {
        //LogWrapper.v("onFailure called");

        if (model.getMsg() != null) {
            try {
                ToastCustomClass.showToast((Activity) context, model.getMsg());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void onFailed(JSONObject model) {
        LogWrapper.v("onFailure called");
        //  ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), false));
    }

    @Override
    public void onResponse(final Call<T> call, Response<T> response) {
        // hide loading view when API call completes
//        onResponse(call, response);
        JSONObject jsonObject = null;
        try {
            jsonObject = Utils.getJsonFromResponse((Response<ResponseBody>) response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            ResponseModel responseModel = null;
            try {
                //responseModel = gson.fromJson(String.valueOf(jsonObject), ResponseModel.class);
                //ChannelSearchEnum[] enums = gson.fromJson(String.valueOf(jsonObject), ChannelSearchEnum[].class);
                responseModel = new ResponseModel();
                responseModel.setRows(jsonObject.getJSONArray("rows"));
                responseModel.setTitle(jsonObject.getString("title"));
                //Collection<Feed> ints2 = gson.fromJson(String.valueOf(jsonObject.getJSONArray("rows")), collectionType);
                //Log.i("REs", ints2.toString());
            } catch (NumberFormatException e) {
                onJsonReceived(jsonObject);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                onJsonReceived(jsonObject);
                return;
            }
            if (responseModel != null) {
                if (responseModel.getStatus() == 1) {
                    onSuccess(responseModel, jsonObject);
                    // TODO: 7/15/2017 use this when you need response from cache
                    saveResponseInCache(call, jsonObject);
                } else
                    onFailed(responseModel);
            }
            // call token service
            // get token
//            call.enqueue(this);
        } else
            onFailed(new ResponseModel());
    }

    /**
     * * Override this when you want to use json data received from onResponse
     * <p/>
     * returns json received in onResponse, it may appear tempting to use onRespose to parse Json yourself but response holidayObj once used then it becomes null
     * so we are using another abstract method to pass on the json data received from service response.
     * <p/>
     *
     * @param jsonObject
     */
    protected void onJsonReceived(JSONObject jsonObject) {

    }

    /**
     * Save response if cacheResponse was called
     */
    private void saveResponseInCache(Call<T> call, JSONObject jsonObject) {
        if (getCall() != null && getCall().request().url().equals(call.request().url())) {
            String url = call.request().url().toString();
            String params = bodyToString(call.request().body());
            String key = url + "#" + params;
            MySharedPreferences.newInstance().putStringKeyValue(context, key, jsonObject.toString());
        }
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        // hide loading view when API call completes
        Call<ResponseBody> bodyCall = (Call<ResponseBody>) call;
        try {
            if (t.getMessage().contains("Unable to resolve host")) {
                //  ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), false));
                ((BaseFragmentActivity) context).onShowNoInternetSnackBar(call, retroCallBack);
            } else {
                onApiFailed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            onApiFailed();
            e.printStackTrace();
        }

//        LogWrapper.v("Reason of api failure: " + t.getMessage());
    }

    /**
     * Called when API call failed due to any reason, will display a snackbar notification
     */
    public void onApiFailed() {
        //   ((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), false));

        onFailed(new ResponseModel());
    }

    public void cacheResponse(Context context, Call<T> call) {
        this.context = context;
        setCall(call);

        String url = call.request().url().toString();
        String params = bodyToString(call.request().body());
        String key = url + "#" + params;

//        ShpUtil.saveToPrefs(getContext(), key, );

        String response = MySharedPreferences.newInstance().getString(context, key, "");

        if (!TextUtils.isEmpty(response)) {
            ResponseModel responseModel = gson.fromJson(response, ResponseModel.class);

            if (responseModel.getStatus() == 1) {
                try {
                    onCachedResponseReceived(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void onCachedResponseReceived(JSONObject response) {
        //((BaseFragmentActivity)context).onShowLoadingView(new ShowLoadingView(getLoadType(), false));
    }
}


