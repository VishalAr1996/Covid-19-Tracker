package com.example.covid_19tracker.MessageNotification;

import com.example.covid_19tracker.ModelClass.Chats;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAiuHyqLs:APA91bFx24JmAnA08SX3Fgii7N88hMvduTh_LJaDVYewwZoDxxQhtCdmFVgWTlxC4xzB7MIeJqPoWO5c1s_co6mzzhvJ9QwBpj2mM1WnHKYGF1eIa3xVc4WDDNKourzTDryogWUb_hnI"
    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
