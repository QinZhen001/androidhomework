package com.example.qinzhen.androidhomework.presenter;

import com.example.qinzhen.androidhomework.model.api.ApiManger;
import com.example.qinzhen.androidhomework.model.entity.NewsBean;
import com.example.qinzhen.androidhomework.model.entity.SportNewsList;
import com.example.qinzhen.androidhomework.ui.fragment.NewsFragment;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * @anthor qinzhen
 * @time 2017/4/15 23:37
 */

public class NewsFraPresenter extends BasePresenter {

    private NewsFragment mFragment;
    private int currentIndex;

    public NewsFraPresenter(NewsFragment newsFragment) {
        super();
        mFragment = newsFragment;
    }


    public void refreshNews() {
        mFragment.showRefreshBar();
        currentIndex = 0;
        ApiManger.getInstance().getNetEasyNewsService()
                .getSportNews(currentIndex + "")
                .map(new Function<SportNewsList, ArrayList<NewsBean>>() {
                    @Override
                    public ArrayList<NewsBean> apply(SportNewsList sportNewsList) {
                        return sportNewsList.getAutoNewsArrayList();
                    }
                })
                .flatMap(new Function<ArrayList<NewsBean>, Observable<NewsBean>>() {
                    @Override
                    public Observable<NewsBean> apply(ArrayList<NewsBean> topNewses) throws Exception {
                        return Observable.fromIterable(topNewses);
                    }
                })
                .filter(new Predicate<NewsBean>() {
                    @Override
                    public boolean test(NewsBean topNews) throws Exception {
                        return topNews.getUrl() != null;
                    }
                })
                .map(new Function<NewsBean, BaseItem>() {
                    @Override
                    public BaseItem apply(NewsBean topNews) {
                        BaseItem<NewsBean> baseItem = new BaseItem<>();
                        baseItem.setData(topNews);
                        return baseItem;
                    }
                })
                .toList()
                //将 List 转为ArrayList 缓存存储 ArrayList Serializable对象
                .map(new Function<List<BaseItem>, ArrayList<BaseItem>>() {
                    @Override
                    public ArrayList<BaseItem> apply(List<BaseItem> baseItems) {
                        ArrayList<BaseItem> items = new ArrayList<>();
                        items.addAll(baseItems);
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<BaseItem>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<BaseItem> value) {
                        //Log.e("onsuccess",((NewsBean)value.get(0).getData()).toString());
                        currentIndex += 20;
                        mFragment.hideRefreshBar();
                        mFragment.refreshNewsSuccessed(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.hideRefreshBar();
                        mFragment.refreshNewsFail(e.getMessage());
                    }

                });
    }





    //两个方法没区别,只是刷新会重新赋值
    public void loadMore() {
        ApiManger.getInstance().getNetEasyNewsService()
                .getSportNews(currentIndex + "")
                .map(new Function<SportNewsList, ArrayList<NewsBean>>() {
                    @Override
                    public ArrayList<NewsBean> apply(SportNewsList sportNewsList) {
                        return sportNewsList.getAutoNewsArrayList();
                    }
                })
                .flatMap(new Function<ArrayList<NewsBean>, Observable<NewsBean>>() {
                    @Override
                    public Observable<NewsBean> apply(ArrayList<NewsBean> topNewses) {
                        return Observable.fromIterable(topNewses);
                    }
                })
                .filter(new Predicate<NewsBean>() {
                    @Override
                    public boolean test(NewsBean topNews) {
                        return topNews.getUrl() != null;
                    }
                })
                .map(new Function<NewsBean, BaseItem>() {
                    @Override
                    public BaseItem apply(NewsBean topNews) {
                        BaseItem<NewsBean> baseItem = new BaseItem<>();
                        baseItem.setData(topNews);
                        return baseItem;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BaseItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<BaseItem> value) {
                        if (value != null && value.size() > 0) {
                            //每刷新成功一次多加载20条
                            currentIndex += 20;
                            mFragment.loadMoreSuccessed((ArrayList<BaseItem>) value);
                        } else {
                            mFragment.loadAll();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.loadMoreFail(e.getMessage());
                    }

                });

    }
}
