package com.example.quip.Fragments;

import com.example.quip.Notifications.MyResponse;
import com.example.quip.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAYFJwp68:APA91bHzH6YCqUWh4tdS5m7poiRvumPzHwUocCKPlVEPC_dvuRllxvgY0PEM8jjrXkKfsve2Hkd1d-DLdex_8YWqJxI84lsrPDVI7akifBVswiA_h2OtNIL6-9xa2sYcabf6dM5bCL7E"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
