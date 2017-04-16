package com.example.qinzhen.androidhomework.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.qinzhen.androidhomework.R;
import com.example.qinzhen.androidhomework.adapter.ZhihuDailyAdapter;
import com.example.qinzhen.androidhomework.config.Constants;
import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhiHuDaily;
import com.example.qinzhen.androidhomework.model.entity.zhihu.ZhiHuStory;
import com.example.qinzhen.androidhomework.presenter.HomeFraPresenter;
import com.example.qinzhen.androidhomework.ui.ImplView.IHomeFra;
import com.example.qinzhen.androidhomework.ui.activity.ZhihuStoryInfoActivity;
import com.example.qinzhen.androidhomework.ui.base.BaseFragment;
import com.example.qinzhen.androidhomework.utils.DateUtils;
import com.example.qinzhen.androidhomework.utils.TagAnimationUtils;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;
import com.pandaq.pandaqlib.magicrecyclerView.MagicRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @anthor qinzhen
 * @time 2017/3/12 15:18
 */
public class HomeFragment extends BaseFragment implements MagicRecyclerView.OnTagChangeListener,IHomeFra,SwipeRefreshLayout.OnRefreshListener
        ,BaseRecyclerAdapter.OnItemClickListener {


    @BindView(R.id.zhihudaily_list)
    MagicRecyclerView mZhihudailyList;
    @BindView(R.id.tv_tag)
    TextView mTvTag;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.empty_msg)
    TextView mEmptyMsg;
    Unbinder unbinder;


    private LinearLayoutManager mLinearLayoutManager;
    private ZhihuDailyAdapter mZhihuDailyAdapter;
    private ArrayList<BaseItem> mBaseItems;
    private boolean initTag;
    private boolean loading= false;
    private HomeFraPresenter mPresenter = new HomeFraPresenter(this);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home);
        unbinder = ButterKnife.bind(this, getContentView());
        initView();
        return getContentView();
    }

    private void initView() {

        mZhihuDailyAdapter = new ZhihuDailyAdapter(this);
        mZhihudailyList.setAdapter(mZhihuDailyAdapter);
        //实质是是对 adapter 设置点击事件所以需要在设置 adapter 之后调用
        mZhihudailyList.addOnItemClickListener(this);


        mBaseItems = new ArrayList<>();
        mZhihudailyList.setItemAnimator(new DefaultItemAnimator());
         mZhihudailyList.getItemAnimator().setChangeDuration(0);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mZhihudailyList.setLayoutManager(mLinearLayoutManager);
        mZhihudailyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(mZhihudailyList.refreshAble()){
                    mRefresh.setEnabled(true);
                }
                if(mZhihudailyList.loadAble()){
                    loadMoreData();
                }
                if(mZhihudailyList.tagGone() && mTvTag.getVisibility() == View.VISIBLE)
                {
                    hideTagAnim(mTvTag);
                    mTvTag.setVisibility(View.GONE);
                }
            }
        });

        mRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white_FFFFFF));
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshData();
        mZhihudailyList.addOnTagChangeListener(this);

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showRefreshBar() {
        if (!mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideRefreshBar() {
        mRefresh.setRefreshing(false);
    }

    @Override
    public void refreshData() {
        initTag = false;
        mPresenter.refreshZhihuDaily();
    }

    @Override
    public void refreshSuccessed(ZhiHuDaily stories) {
        if (stories == null || stories.getStories().size() <= 0) {
            mEmptyMsg.setVisibility(View.VISIBLE);
            mZhihudailyList.setVisibility(View.INVISIBLE);
            mRefresh.requestFocus();
            return;
        } else {
            mEmptyMsg.setVisibility(View.GONE);
            mZhihudailyList.setVisibility(View.VISIBLE);
        }
        mBaseItems.clear();
        //配置底部列表故事
        for (ZhiHuStory story : stories.getStories()) {
            if (!initTag) {
                initTag = true;
                BaseItem<String> baseItem = new BaseItem<>();
                baseItem.setData(stories.getDate());
                baseItem.setItemType(BaseRecyclerAdapter.RecyclerItemType.TYPE_TAGS);
                mBaseItems.add(baseItem);
            }
            BaseItem<ZhiHuStory> baseItem = new BaseItem<>();
            baseItem.setData(story);
            mBaseItems.add(baseItem);
        }

            if (mBaseItems.size() != 0) {
                mZhihuDailyAdapter.setBaseDatas(mBaseItems);
            }

    }

    @Override
    public void refreshFail(String errMsg) {
        if (mZhihuDailyAdapter == null) {
            mEmptyMsg.setVisibility(View.VISIBLE);
            mZhihudailyList.setVisibility(View.INVISIBLE);
            mRefresh.requestFocus();
        }
    }

    @Override
    public void loadMoreData() {
        if (!loading) {
            loading = true;
            mPresenter.loadMoreData();
        }
    }

    @Override
    public void loadSuccessed(ArrayList<BaseItem> stories) {
        mBaseItems.addAll(stories);
        mZhihuDailyAdapter.addBaseDatas(stories);
        loading = false;
    }

    @Override
    public void loadFail(String errMsg) {
        loading = false;
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onItemClick(int position, BaseItem data, View view) {
        //跳转到其他界面
        ZhiHuStory story = (ZhiHuStory) data.getData();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getActivity(),ZhihuStoryInfoActivity.class);
        bundle.putString(Constants.BUNDLE_KEY_TITLE, story.getTitle());
        bundle.putInt(Constants.BUNDLE_KEY_ID, story.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onChange(String newTag) {
        if (mTvTag.getVisibility() == View.GONE) {
            mTvTag.setVisibility(View.VISIBLE);
            showTagAnim(mTvTag);
        }
        int year = Integer.parseInt(newTag.substring(0, 4));
        int mon = Integer.parseInt(newTag.substring(4, 6));
        int day = Integer.parseInt(newTag.substring(6, 8));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, mon - 1, day);
        mTvTag.setText(DateUtils.formatDate(calendar));
    }


    private void showTagAnim(final View view) {
        Animation animation = TagAnimationUtils.moveToViewLocation();
        view.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void  hideTagAnim(final View view) {
        Animation animation = TagAnimationUtils.moveToViewTop();
        view.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
