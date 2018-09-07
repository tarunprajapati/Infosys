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
    public static ParsingManager newInstance() {
        ParsingManager parsingClass = new ParsingManager();
        return parsingClass;
    }

    private ParsingManager() {

    }

    public void doJsonObjectRequest(Context context, final String strUrl, final HashMap<String, String> params, final ApiResponse apiResponce, boolean isPost, final AsyncTaskManager.CallBackListener callBackListener, final Dialog progressDialog, int requestCode) {
        JSONObject jsonObject = new JSONObject(params);
        requestJsonVolley(context, strUrl, jsonObject, apiResponce, isPost, callBackListener, progressDialog, requestCode);
    }

    private void requestJsonVolley(Context context, final String strUrl, final JSONObject params, final ApiResponse apiResponce, boolean isPost, final AsyncTaskManager.CallBackListener callBackListener, final Dialog progressDialog, final int requestCode) {

        final RequestQueue[] queue = {Volley.newRequestQueue(context)};
        // Post Method HttpPost
        try {
            int method;
            if (isPost)
                method = Request.Method.POST;
            else
                method = Request.Method.GET;

            //Log.d("Url :=", strUrl);
            if (params != null) {
                //Log.d("Params :=", params.toString());
                final Response.Listener<JSONObject> resListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*try {*/
                            queue[0].stop();
                            queue[0] = null;
                           // VolleyLog.v("Response:%n %s", response.toString(4));
                           //Log.i("Res", response.toString());
                            apiResponce.addResponce(requestCode, response);
                            callBack(progressDialog, callBackListener, apiResponce);
                        /*} catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        System.gc();
                    }
                };

                JsonObjectRequest reqJson = new JsonObjectRequest(method, strUrl, params, resListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*VolleyLog.e("Error: ", error.getMessage());
                        Log.i("Error", error.toString());*/
                        JSONObject jRes = new JSONObject();
                        try {
                            jRes.put(Constants.status, 500);
                        } catch (JSONException e) {

                        }
                        //apiResponce.setErrorMsg(error.getMessage());
                        apiResponce.setErrorMsg("Something wrong");
                        apiResponce.addResponce(strUrl, "");
                        callBack(progressDialog, callBackListener, apiResponce);
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "text/json");
                        //if(header.isEmpty())
                        headers.put("Content-Type", "application/json");
                        headers.put("Accept-Version", "1.0.0");
                        headers.put("Accept-Language", "en");
                        headers.put("Device-Os", "android");
                        return headers;
                    }
                };

                reqJson.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                if (CheckInternet.newInstance().isConnectingToInternet(context, queue[0], reqJson)) {
                    queue[0].add(reqJson);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            apiResponce.setErrorMsg(e2.getMessage());
        }
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

    RetroCallBack retroCallBack = null;

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




