package com.infosys.managers;

import android.app.Dialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.infosys.R;
import com.infosys.base.classes.BaseFragmentActivity;
import com.infosys.webservices.Constants;
import com.infosys.webservices.LoadType;
import com.infosys.webservices.ResponseModel;
import com.infosys.webservices.RetroCallBack;
import com.infosys.webservices.ToastCustomClass;
import com.infosys.webservices.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ParsingManager {
    private int MY_SOCKET_TIMEOUT_MS = 30000;
    RetroCallBack retroCallBack = null;

    public static ParsingManager newInstance() {
        ParsingManager parsingClass = new ParsingManager();
        return parsingClass;
    }

    private ParsingManager() {

    }


    public void callBack(Dialog progressDialog, AsyncTaskManager.CallBackListener callBackListener, ApiResponse apiResponce) {
        dismissProgress(progressDialog);
        if (callBackListener != null)
            callBackListener.callback(apiResponce);
    }

    public void dismissProgress(Dialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }



    public void callRetrofit(final Context context, final Call<ResponseBody> resBodyCall, final ApiResponse apiResponse, final AsyncTaskManager.CallBackListener callBackListener, final Dialog progressDialog, final int reqCode) {
        if (resBodyCall != null) {
            retroCallBack = new RetroCallBack((BaseFragmentActivity) context, LoadType.SILENT) {
                @Override
                public void onSuccess(ResponseModel responseModel, JSONObject jsonObject) {
                    super.onSuccess(responseModel, jsonObject);
                    resBodyCall.cancel();
                    //Log.i("Res", jsonObject.toString());
                    apiResponse.addResponce(reqCode, jsonObject);
                    callBack(progressDialog, callBackListener, apiResponse);
                }

                @Override
                public void onFailed(ResponseModel model) {
                    super.onFailed(model);
                    resBodyCall.cancel();
                    if (model != null && model.getMsg() != null) {
                        apiResponse.addResponce(reqCode, "");
                        callBack(progressDialog, callBackListener, apiResponse);
                    } else {
                        ((BaseFragmentActivity) context).onShowNoInternetSnackBar(resBodyCall, retroCallBack, context.getString(R.string.tryAgain));
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        callBack(progressDialog, callBackListener, apiResponse);
                    }
                }
            };

            if (Utils.isNetworkAvailable(context)) {
                retroCallBack.setCall(resBodyCall);
                resBodyCall.enqueue(retroCallBack);
            } else {
                ((BaseFragmentActivity) context).onShowNoInternetSnackBar(resBodyCall, retroCallBack);
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        } else {
            ToastCustomClass.showToast(context, context.getString(R.string.didntWrk));
        }
    }
}




