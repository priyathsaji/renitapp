package com.rentit.priyath.rentitlayout;

import com.rentit.priyath.rentitlayout.models.RequestBody;
import com.rentit.priyath.rentitlayout.models.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("devices")
    Call<ResponseBody> registerDevice(@Body RequestBody body);
}
