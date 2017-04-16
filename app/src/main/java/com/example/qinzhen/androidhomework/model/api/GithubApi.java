package com.example.qinzhen.androidhomework.model.api;


import com.example.qinzhen.androidhomework.model.entity.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @anthor qinzhen
 * @time 2017/4/13 23:37
 */

public interface GithubApi {

    //获取用户信息
    @GET("users/{user}")
   Observable<UserInfo> getMyinfo(@Path("user") String path);

}
