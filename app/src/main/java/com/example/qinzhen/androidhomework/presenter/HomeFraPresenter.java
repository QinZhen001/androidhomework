package com.example.qinzhen.androidhomework.presenter;

import com.example.qinzhen.androidhomework.model.api.ApiManger;
import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhiHuDaily;
import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhiHuStory;
import com.example.qinzhen.androidhomework.ui.fragment.HomeFragment;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;



/**
 * @anthor qinzhen
 * @time 2017/4/14 22:52
 */

public class HomeFraPresenter extends BasePresenter{
    private HomeFragment mFragment;
    private String d;


    public HomeFraPresenter(HomeFragment homeFragment) {
        mFragment = homeFragment;
    }

    public void refreshZhihuDaily() {
        mFragment.showRefreshBar();
        ApiManger.getInstance()
                .getZhihuService()
                .getLatestZhihuDaily()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ZhiHuDaily value) {
                        d = value.getDate();
                        mFragment.hideRefreshBar();
                        mFragment.refreshSuccessed(value);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.hideRefreshBar();
                        mFragment.refreshFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                          mFragment.hideRefreshBar();
                    }
                });

        }

    public void loadMoreData() {
        ApiManger.getInstance()
                .getZhihuService()
                .getZhihuDaily(d)
                .map(new Function<ZhiHuDaily,ArrayList<ZhiHuStory>>() {
                    @Override
                    public ArrayList<ZhiHuStory> apply(ZhiHuDaily zhiHuDaily) throws Exception {
                       d = zhiHuDaily.getDate();
                        return zhiHuDaily.getStories();
                    }
                })
                .flatMap(new Function<ArrayList<ZhiHuStory>, ObservableSource<ZhiHuStory>>() {
                    @Override
                    public ObservableSource<ZhiHuStory> apply(ArrayList<ZhiHuStory> zhiHuStories) throws Exception {
                        return Observable.fromIterable(zhiHuStories);
                    }
                })
                .map(new Function<ZhiHuStory, BaseItem>() {
                    @Override
                    public BaseItem apply(ZhiHuStory zhiHuStory) throws Exception {
                        //将日期值设置到 story 中
                        zhiHuStory.setDate(d);
                        BaseItem<ZhiHuStory> baseItem = new BaseItem<>();
                        baseItem.setData(zhiHuStory);
                        return baseItem;
                    }
                }) .toList()
                // 在所有的数据 list 前面加上当天的 tag
                .map(new Function<List<BaseItem>, List<BaseItem>>() {
                    @Override
                    public List<BaseItem> apply(List<BaseItem> baseItems) {
                        BaseItem<String> baseItem = new BaseItem<>();
                        baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_TAGS);
                        baseItem.setData(d);
                        baseItems.add(0, baseItem);
                        return baseItems;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BaseItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<BaseItem> value) {
                        mFragment.loadSuccessed((ArrayList<BaseItem>) value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.loadFail(e.getMessage());
                    }

                });

    }
}

