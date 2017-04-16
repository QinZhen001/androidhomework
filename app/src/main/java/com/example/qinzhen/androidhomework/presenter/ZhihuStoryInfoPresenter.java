package com.example.qinzhen.androidhomework.presenter;


import com.example.qinzhen.androidhomework.model.api.ApiManger;
import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhihuStoryContent;
import com.example.qinzhen.androidhomework.ui.ImplView.IZhihuStoryInfoActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by PandaQ on 2016/10/10.
 * email : 767807368@qq.com
 */

public class ZhihuStoryInfoPresenter extends BasePresenter {

    private IZhihuStoryInfoActivity mActivity;

    public ZhihuStoryInfoPresenter(IZhihuStoryInfoActivity iZhihuStoryInfoActivity) {
        mActivity = iZhihuStoryInfoActivity;
    }

    public void loadStory(String id) {
        mActivity.showProgressBar();
        ApiManger.getInstance().getZhihuService()
                .getStoryContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuStoryContent>() {

                    @Override
                    public void onError(Throwable e) {
                        mActivity.loadFail(e.getMessage());
                        mActivity.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mActivity.hideProgressBar();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ZhihuStoryContent zhihuStoryContent) {
                        mActivity.loadSuccess(zhihuStoryContent);
                        mActivity.hideProgressBar();
                    }
                });

    }
}
