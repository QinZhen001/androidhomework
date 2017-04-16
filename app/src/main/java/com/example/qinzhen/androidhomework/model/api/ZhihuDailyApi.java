package com.example.qinzhen.androidhomework.model.api;

import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhiHuDaily;
import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhihuStoryContent;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @anthor qinzhen
 * @time 2017/4/15 14:38
 */

public interface ZhihuDailyApi {

    //获取最近的日报
    @GET("news/latest")
    Observable<ZhiHuDaily> getLatestZhihuDaily();

    //获取某一时间之前的日报（加载更多）
    @GET("news/before/{data}")
    Observable<ZhiHuDaily> getZhihuDaily(@Path("data") String data);


    @GET("news/{id}")
    Observable<ZhihuStoryContent> getStoryContent(@Path("id") String id);


}
