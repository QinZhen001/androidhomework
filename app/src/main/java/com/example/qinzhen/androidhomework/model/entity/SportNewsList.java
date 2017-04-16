package com.example.qinzhen.androidhomework.model.entity;

import com.example.qinzhen.androidhomework.config.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @anthor qinzhen
 * @time 2017/4/15 23:23
 */

public class SportNewsList {

    @SerializedName(Constants.NETEASY_NEWS_SPORT)
    private ArrayList<NewsBean> mAutoNewsArrayList;

    public ArrayList<NewsBean> getAutoNewsArrayList(){
        return  mAutoNewsArrayList;
    }
}
