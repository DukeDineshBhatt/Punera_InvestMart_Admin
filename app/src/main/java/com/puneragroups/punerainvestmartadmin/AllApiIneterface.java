package com.puneragroups.punerainvestmartadmin;

import com.puneragroups.punerainvestmartadmin.NOtificationPOJO.DataBean;
import com.puneragroups.punerainvestmartadmin.NOtificationPOJO.ResultBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {


    @Headers({"Authorization:key=AAAAhYTx0hE:APA91bG24zffvXbxLuiT3CkjkUJXs8FfX5OIWmKzbzhCOMXEJgelmd0PSdQB_Lwdw8hTTy6TlRCOuCDX_JWKDRtKqHU-UwaC1i6wJg75Qo-YeSO61s0D5AGYPeliArPIz8Y8u5EDh1Y9",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResultBean> sendNotification(@Body DataBean requestNotificaton
    );

}
