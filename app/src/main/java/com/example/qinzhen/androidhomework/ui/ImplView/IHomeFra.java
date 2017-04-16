package com.example.qinzhen.androidhomework.ui.ImplView;

import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhiHuDaily;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;

import java.util.ArrayList;

/**
 * @anthor qinzhen
 * @time 2017/4/14 22:10
 */

public interface IHomeFra {
    void showRefreshBar();

    void hideRefreshBar();

    void refreshData();

    void refreshSuccessed(ZhiHuDaily stories);

    void refreshFail(String errMsg);

    void loadMoreData();

    void loadSuccessed(ArrayList<BaseItem> stories);

    void loadFail(String errMsg);

}
