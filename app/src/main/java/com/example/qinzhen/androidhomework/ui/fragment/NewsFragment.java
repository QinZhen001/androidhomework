package com.example.qinzhen.androidhomework.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qinzhen.androidhomework.R;
import com.example.qinzhen.androidhomework.adapter.TopNewsListAdapter;
import com.example.qinzhen.androidhomework.config.Constants;
import com.example.qinzhen.androidhomework.model.entity.NewsBean;
import com.example.qinzhen.androidhomework.presenter.NewsFraPresenter;
import com.example.qinzhen.androidhomework.ui.ImplView.INewsFra;
import com.example.qinzhen.androidhomework.ui.activity.TopNewsInfoActivity;
import com.example.qinzhen.androidhomework.ui.base.BaseFragment;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;
import com.pandaq.pandaqlib.magicrecyclerView.MagicRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * @anthor qinzhen
 * @time 2017/3/12 15:22
 */
public class NewsFragment extends BaseFragment implements INewsFra, BaseRecyclerAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.magiccycler_news)
    MagicRecyclerView mNewsRecycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.empty_msg)
    TextView mEmptyMsg;


    private TopNewsListAdapter mAdapter;
    private boolean loading = false;
    private Disposable mDisposable;
    private LinearLayoutManager mLinearLayoutManager;
    private Unbinder unbinder;
    private NewsFraPresenter mPresenter = new NewsFraPresenter(this);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_news);
        unbinder = ButterKnife.bind(this, getContentView());
        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        mNewsRecycler.setLayoutManager(mLinearLayoutManager);
        //屏蔽掉默认的动画，防止刷新的时候图片闪烁
        mNewsRecycler.getItemAnimator().setChangeDuration(0);
        initView();
        return getContentView();
    }

    private void initView() {
        mAdapter = new TopNewsListAdapter(this);
        mNewsRecycler.setAdapter(mAdapter);
        //实质是是对 adapter 设置点击事件所以需要在设置 adapter 之后调用
        mNewsRecycler.addOnItemClickListener(this);

        mNewsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(mNewsRecycler.refreshAble()){
                    mRefresh.setEnabled(true);
                }
                if(mNewsRecycler.loadAble()){
                    loadMoreNews();
                }
            }
        });

        mRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white_FFFFFF));
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRefresh.setRefreshing(true);
        refreshNews();
        View footer = mNewsRecycler.getFooterView();
        if (footer != null) {
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreNews();
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRefresh.setRefreshing(false);
        mPresenter.dispose();
        onHiddenChanged(true);
    }



    @Override
    public void showRefreshBar() {
        mRefresh.setRefreshing(true);
    }

    @Override
    public void hideRefreshBar() {
      mRefresh.setRefreshing(false);
    }

    @Override
    public void refreshNews() {
      mPresenter.refreshNews();
    }

    @Override
    public void refreshNewsFail(String errorMsg) {
           if(mPresenter == null){
               mEmptyMsg.setVisibility(View.VISIBLE);
               mNewsRecycler.setVisibility(View.INVISIBLE);
               mRefresh.requestFocus();
           }
    }

    @Override
    public void refreshNewsSuccessed(ArrayList<BaseItem> news) {
           if(news == null || news.size()<=0){
               mEmptyMsg.setVisibility(View.VISIBLE);
               mNewsRecycler.setVisibility(View.INVISIBLE);
               mRefresh.requestFocus();
           }else{
               mEmptyMsg.setVisibility(View.GONE);
               mNewsRecycler.setVisibility(View.VISIBLE);
           }

           if(mAdapter != null){
               mAdapter.setBaseDatas(news);
           }
           mNewsRecycler.showFooter();
    }

    @Override
    public void loadMoreNews() {
        if(!loading){
            mPresenter.loadMore();
                    loading = true;
        }
    }

    @Override
    public void loadMoreFail(String errorMsg) {
        loading = false;
    }

    @Override
    public void loadMoreSuccessed(ArrayList<BaseItem> news) {
        loading = false;
        mAdapter.addBaseDatas(news);
    }

    @Override
    public void loadAll() {
        mNewsRecycler.hideFooter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mAdapter = null;
    }

    @Override
    public void onItemClick(int position, BaseItem data, View view) {
        //跳转到其他界面
        NewsBean topNews = (NewsBean) data.getData();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this.getActivity(), TopNewsInfoActivity.class);
        bundle.putString(Constants.BUNDLE_KEY_TITLE, topNews.getTitle());
        bundle.putString(Constants.BUNDLE_KEY_ID, topNews.getDocid());
        bundle.putString(Constants.BUNDLE_KEY_IMG_URL, topNews.getImgsrc());
        bundle.putString(Constants.BUNDLE_KEY_HTML_URL, topNews.getUrl());
        intent.putExtras(bundle);
            startActivity(intent);


    }

    @Override
    public void onRefresh() {
        refreshNews();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
       if(hidden && mRefresh.isRefreshing()){
           //fragment隐藏时候，停止SwipeRefreshLayout的转动
           mRefresh.setRefreshing(false);
       }
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }


    }
}
