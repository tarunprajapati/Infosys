
package com.infosys.managers;

import com.infosys.base.classes.BaseFragment;

import com.infosys.webservices.ApiClient;
import com.infosys.webservices.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.infosys.webservices.Constants.RETROFIT;


public class ApiManager {
    private static final ApiManager manager = new ApiManager();
    private final int android = 0;

    private ApiManager() {
    }

    public static ApiManager getInstance() {
        return manager;
    }

    public void getFeeds(BaseFragment fragment) {
        if(CheckInternet.newInstance().internetIsAvailable(fragment.getContext())) {
            Call<ResponseBody> responseBodyCall = ApiClient.newApiClient().getInterfaceSToken(fragment.getActivity()).getFeeds();
            fragment.serviceCaller(fragment, responseBodyCall, Constants.FEEDS, true, RETROFIT);
        }
    }
}
