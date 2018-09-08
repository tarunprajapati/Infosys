package com.infosys.webservices;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Tarun on 08/09/18.
 */
public interface ApiInterface {
    /**
     * services
     * have to be called with
     */
    @GET("s/2iodh4vg0eortkl/facts.json")
    Call<ResponseBody> getFeeds();
}
