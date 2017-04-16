package com.example.qinzhen.androidhomework.presenter;

import android.util.Log;

import com.example.qinzhen.androidhomework.model.api.ApiManger;
import com.example.qinzhen.androidhomework.model.entity.UserInfo;
import com.example.qinzhen.androidhomework.ui.fragment.PersonFragment;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @anthor qinzhen
 * @time 2017/4/13 23:20
 */

public class PersonFraPresenter extends BasePresenter{

     private PersonFragment mFragment;


    public PersonFraPresenter(PersonFragment personFragment) {
        mFragment =  personFragment;
    }

    /**
     *
     * @param username
     */
    public void loadinfo(String username) {
        ApiManger.getInstance()
                .getGithubService()
                .getMyinfo(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(UserInfo value) {
                        Log.e("UserInfo",value.toString());
                             mFragment.showMyInfo(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                            mFragment.loadMyInfoFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
