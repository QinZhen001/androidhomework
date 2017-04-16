package com.example.qinzhen.androidhomework.model.api;

import com.example.qinzhen.androidhomework.config.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 集中处理Api的相关配置
 * @anthor qinzhen
 * @time 2017/4/13 23:24
 */

public class ApiManger {

    private static ApiManger apiManger;
    private static OkHttpClient mClient;
    private GithubApi mGithubApi;
    private NetEasyNewsApi mNetEasyNewsApi;
    private ZhihuDailyApi mDailyApi;

    private ApiManger(){

    }

    //单例模式
    public static ApiManger getInstance(){
        if(apiManger == null){
            synchronized (ApiManger.class){
                if(apiManger == null){
                    apiManger = new ApiManger();
                }
            }

        }

        mClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        return apiManger;
    }




    public GithubApi getGithubService(){
        if(mGithubApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.GITHUB_API_URL)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            mGithubApi = retrofit.create(GithubApi.class);
        }
        return mGithubApi;
    }

    //配置知乎的api
    public ZhihuDailyApi getZhihuService(){
        if(mDailyApi == null){
            Retrofit retrofit =new Retrofit.Builder()
                    .baseUrl(Constants.ZHIHU_API_URL)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
             mDailyApi = retrofit.create(ZhihuDailyApi.class);
        }
        return  mDailyApi;
    }


    //配置网易新闻的API
    public NetEasyNewsApi getNetEasyNewsService(){
        if(mNetEasyNewsApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.NETEASY_NEWS_API)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mNetEasyNewsApi = retrofit.create(NetEasyNewsApi.class);
        }
        return mNetEasyNewsApi;
    }


}
