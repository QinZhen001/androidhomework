package com.example.qinzhen.androidhomework.ui.ImplView;


import com.example.qinzhen.androidhomework.model.entity.NewsContent;



public interface ITopNewsInfoActivity {
    void showProgressBar();

    void hideProgressBar();

    void loadTopNewsInfo(String newsId);

    void loadFail(String errmsg);

    void loadSuccess(NewsContent topNewsContent);
}
