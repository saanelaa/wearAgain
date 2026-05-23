package com.example.wearagain.data.remote;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImgBBServis {

    @Multipart
    @POST("upload")
    Call<ImgBBOdgovor> uploadujSliku(
            @Query("key") String apiKey,
            @Part MultipartBody.Part slika
    );
}
