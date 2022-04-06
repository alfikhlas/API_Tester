package com.example.apitester;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface jsonPlaceHolderApi {

    @GET()
    Call<Void> callApi(
            @Url String url
    );
}
