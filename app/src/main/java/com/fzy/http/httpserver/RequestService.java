package com.fzy.http.httpserver;


import com.fzy.http.bean.HttpResultComman;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Admin on 2017/7/14.
 */

public interface RequestService{
    @GET("AppFiftyToneGraph/videoLink")
    Call<HttpResultComman> listVideo();

    @GET("AppFiftyToneGraph/videoLin")
    Observable<HttpResultComman> getAllVedio();
}
