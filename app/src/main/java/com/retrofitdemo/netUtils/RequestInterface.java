package com.retrofitdemo.netUtils;


import org.json.JSONArray;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RequestInterface {


    String secure_field = "keys";
    String secure_value = "1222";

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

    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&" + secure_value + "=" + 21)
    Call<ResponseBody> addPostImage(@Query("user_id") String user_id,
                                    @Query("post_id") String post_id,
                                    @Query("count") int count,
                                    @Part List<MultipartBody.Part> file);

    // todo search Reciver Person
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=16&type=Android")
    io.reactivex.Observable<ResponseBody> searchReciverPerson(@Query("name") String name
    );

}
