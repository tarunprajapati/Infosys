package com.infosys.webservices;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ayush on 28/6/16.
 */
public interface ApiInterface {
    /**
     * services
     * have to be called with
     */
    @GET("s/2iodh4vg0eortkl/facts.json")
    Call<ResponseBody> getFeeds();
}
