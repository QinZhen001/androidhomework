package com.example.qinzhen.androidhomework.ui.ImplView;

import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;

import java.util.ArrayList;

/**
 * @anthor qinzhen
 * @time 2017/4/15 23:19
 */

public interface INewsFra {
    void showRefreshBar();

    void hideRefreshBar();

    void refreshNews();

    void refreshNewsFail(String errorMsg);

    void refreshNewsSuccessed(ArrayList<BaseItem> news);

    void loadMoreNews();

    void loadMoreFail(String errorMsg);

    void loadMoreSuccessed(ArrayList<BaseItem> news);

    void loadAll();


}
