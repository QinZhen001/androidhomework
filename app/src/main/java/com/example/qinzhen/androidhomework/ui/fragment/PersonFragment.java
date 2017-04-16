package com.example.qinzhen.androidhomework.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qinzhen.androidhomework.R;
import com.example.qinzhen.androidhomework.model.entity.UserInfo;
import com.example.qinzhen.androidhomework.presenter.PersonFraPresenter;
import com.example.qinzhen.androidhomework.ui.ImplView.IPersonFrg;
import com.example.qinzhen.androidhomework.ui.base.BaseFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @anthor qinzhen
 * @time 2017/3/12 15:22
 */
public class PersonFragment extends BaseFragment implements IPersonFrg {


    Unbinder unbinder;
    @BindView(R.id.civ_my_avatar)
    CircleImageView mCivMyAvatar;
    @BindView(R.id.tv_name)
    TextView mTvMyname;

    private PersonFraPresenter mPresenter = new PersonFraPresenter(this);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_person);
        unbinder = ButterKnife.bind(this, getContentView());
        loadMyInfo();
        return getContentView();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadMyInfo() {
        mPresenter.loadinfo("QinZhen001");
    }

    @Override
    public void showMyInfo(UserInfo userInfo) {
        if (userInfo != null) {
            Picasso.with(getContext())
                    .load(userInfo.getAvatar_url())
                    .into(mCivMyAvatar);

            mTvMyname.setText(userInfo.getName());
        }

    }

    @Override
    public void loadMyInfoFail(String message) {
        Toast.makeText(getContext(), "获取用户失败" + message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @OnClick(R.id.civ_my_avatar)
    public void onViewClicked(View view) {
        Toast.makeText(getContext(), "重新获取用户~"  , Toast.LENGTH_SHORT).show();
        loadMyInfo();
    }
}
