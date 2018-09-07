package com.infosys.managers;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;

import java.util.HashMap;
import okhttp3.ResponseBody;
import retrofit2.Call;
import static com.infosys.webservices.Constants.RETROFIT;
import static com.infosys.webservices.Constants.VOLLEY_JSON;

//import com.wang.avi.AVLoadingIndicatorView;

public class AsyncTaskManager {
    private Call<ResponseBody> reqCall = null;
    private CallBackListener callBackListener = null;
    private ParsingManager parsingClass = null;
    public boolean isProgressBarShow = true;
    private boolean isPost = true;
    private HashMap<String, String> params = null;
    private Object paramsObject = null;
    private Context context = null;
    private String[] urls = null;
    private int requestType;
    private int requestCode;

    public CallBackListener getCallBackListener() {
        return callBackListener;
    }

    public void setCallBackListener(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public AsyncTaskManager(@NonNull Context context, Call<ResponseBody> reqCall, int requestCode, int requestType) {
        this.context = context;
        this.requestCode = requestCode;
        this.requestType = requestType;
        this.reqCall = reqCall;
        parsingClass = ParsingManager.newInstance();
    }


    public AsyncTaskManager(@NonNull Context context, @NonNull String[] urls, HashMap<String, String> params, int requestCode, boolean isPost, int requestType) {
        this.context = context;
        this.urls = urls;
        this.params = params;
        this.requestCode = requestCode;
        this.isPost = isPost;

        parsingClass = ParsingManager.newInstance();
        this.requestType = requestType;
    }

    /*public AsyncTaskManager(@NonNull Context context, @NonNull String[] urls, HashMap<String, String> params, String progressMsg, boolean isPost, int requestType) {
        this.context = context;
        this.urls = urls;
        this.params = params;
        this.progressMsg = progressMsg;
        this.isPost = isPost;

        parsingClass = ParsingManager.newInstance(context);
        this.requestType = requestType;
    }*/

    public void execute() {
        Dialog progressDialog = null;
        try {
            if (isProgressBarShow) {
                // progressDialog = new CustomProgressDialog(context);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(false);

                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        doInBackground(progressDialog);
    }

    protected void doInBackground(Dialog progressDialog) {
        ApiResponse apiResponse = new ApiResponse();
        switch (requestType) {
            case RETROFIT:
                parsingClass.callRetrofit(context, reqCall, apiResponse, callBackListener, progressDialog, requestCode);
                break;
            case VOLLEY_JSON:
                for (String url : urls)
                    if (params != null)
                        parsingClass.doJsonObjectRequest(context, url, params, apiResponse, isPost, callBackListener, progressDialog, requestCode);
                break;
        }
    }

    public interface CallBackListener {
        public void callback(Object object);
    }

    public void cancelRequest() {
        switch (requestType) {
            case RETROFIT:
                if (reqCall != null && !reqCall.isCanceled())
                    reqCall.cancel();
                if (parsingClass != null && parsingClass.retroCallBack != null)
                    parsingClass.retroCallBack.cancelCall();
                break;
            case VOLLEY_JSON:
                for (String url : urls)
                   /* if (params != null)
                        parsingClass.doJsonObjectRequest(context, url, params, apiResponse, isPost, callBackListener, progressDialog, requestCode);*/
                    break;
        }

    }
}