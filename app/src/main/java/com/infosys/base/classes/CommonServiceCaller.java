package com.infosys.base.classes;

import android.content.Context;

import com.infosys.base.interfaces.ServiceCallerInterface;
import com.infosys.managers.ApiResponse;
import com.infosys.managers.AsyncTaskManager;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class CommonServiceCaller {
    private static CommonServiceCaller com;
    public final int noMsg = 1;
    public final int showToast = 2;
    public final int showAlter = 3;
    public int showNotiType = showToast;
    AsyncTaskManager asy = null;

    private CommonServiceCaller() {
    }

    public static CommonServiceCaller newInstance(Context context) {
        com = new CommonServiceCaller();
        return com;
    }

    public static CommonServiceCaller getInstance() {
        return com;
    }

    public void serviceCaller(final BaseFragment fragment, Call<ResponseBody> responseBodyCall, final int requstCode, boolean isShowProgress, int requestType) {
        //context = fragment.getActivity();
        AsyncTaskManager.CallBackListener callBackListener = getCallBackListener(fragment, requstCode);

        asy = new AsyncTaskManager(fragment.getActivity(), responseBodyCall, requstCode, requestType);
        asy.setCallBackListener(callBackListener);
        asy.isProgressBarShow = isShowProgress;
        asy.execute();
    }

    public void serviceCaller(final BaseFragmentActivity act, Call<ResponseBody> resCall, final int requstCode, boolean isShowProgress, int requestType) {
        //context = act;
        AsyncTaskManager.CallBackListener callBackListener = getCallBackListener(act, requstCode);

        asy = new AsyncTaskManager(act, resCall, requstCode, requestType);
        asy.setCallBackListener(callBackListener);
        asy.isProgressBarShow = isShowProgress;
        asy.execute();
    }

    public void serviceCaller(final BaseFragmentActivity act, String urlsArr[], HashMap<String, String> params, boolean isPost, boolean isFileUploading, final int requstCode, boolean isShowProgress, int requestType) {
        //context = act;
        final String urls[] = urlsArr;
        AsyncTaskManager.CallBackListener callBackListener = getCallBackListener(act, requstCode);

        asy = new AsyncTaskManager(act, urls, params, requstCode, isPost, requestType);
        asy.setCallBackListener(callBackListener);
        asy.isProgressBarShow = isShowProgress;
        asy.execute();
    }

    public void serviceCaller(final BaseFragment act, String urlsArr[], HashMap<String, String> params, boolean isPost, boolean isFileUploading, final int requstCode, boolean isShowProgress, int requestType) {
        //context = act.getActivity();
        final String urls[] = urlsArr;
        AsyncTaskManager.CallBackListener callBackListener = getCallBackListener(act, requstCode);

        asy = new AsyncTaskManager(act.getActivity(), urls, params, requstCode, isPost, requestType);
        asy.setCallBackListener(callBackListener);
        asy.isProgressBarShow = isShowProgress;
        asy.execute();
    }

    public void cancelRequest() {
        if (asy != null) {
            asy.cancelRequest();
            asy = null;
        }
        com = null;
    }

    private AsyncTaskManager.CallBackListener getCallBackListener(final ServiceCallerInterface anInterface, final int requstCode) {
        AsyncTaskManager.CallBackListener callBackListener = new AsyncTaskManager.CallBackListener() {
            @Override
            public void callback(Object object) {
                //===================
                //context = null;
                //Live Code
                if (object == null)
                    return;
                if (object instanceof ApiResponse) {
                    ApiResponse apiResponce = (ApiResponse) object;
                    HashMap<Integer, JSONObject> mapOfResponce = apiResponce.getResMapInt();
                    if (mapOfResponce != null && mapOfResponce.size() > 0) {
                        JSONObject jOb = mapOfResponce.get(requstCode);
                        if (jOb != null) {
                            anInterface.callBackFromApi(jOb, requstCode);
                        } else {
                            anInterface.callBackFromApi(null, requstCode);
                        }
                    } else {
                        anInterface.callBackFromApi(apiResponce, requstCode);
                    }
                }
            }
        };

        return callBackListener;
    }


    public boolean isOk(Object object) {
        boolean isOnline = false;
        if (object != null) {
            try {
                if (object instanceof JSONObject) {
                    isOnline = true;
                    showNotiType = showToast;
                } else if (object instanceof ApiResponse) {
                    isOnline = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isOnline = false;
            }
        } else {
            isOnline = false;
        }
        return isOnline;
    }
}
