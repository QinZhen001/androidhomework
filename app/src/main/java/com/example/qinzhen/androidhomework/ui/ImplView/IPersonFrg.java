package com.example.qinzhen.androidhomework.ui.ImplView;

import com.example.qinzhen.androidhomework.model.entity.UserInfo;

/**
 * @anthor qinzhen
 * @time 2017/4/13 23:17
 */

public interface IPersonFrg {
    void loadMyInfo();

    void showMyInfo(UserInfo userInfo);

    void loadMyInfoFail(String message);
}
