package com.retrofitdemo.netUtils;


import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestInterface {


    // todo get state list
    @GET("all")
    Call<ResponseBody> getState();

    // todo Login
    @POST("service_user.php?")
    Call<ResponseBody> LoginUser(@Query("username") String username,
                                 @Query("password") String password,
                                 @Query("imei") String imei,
                                 @Query("refreshToken") String refreshToken
    );

}
