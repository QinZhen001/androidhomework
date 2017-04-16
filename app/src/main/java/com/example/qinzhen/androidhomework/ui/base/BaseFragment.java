package com.example.qinzhen.androidhomework.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BaseFragment<T> extends Fragment {

    private View view ;
    private LayoutInflater inflater;
    private ViewGroup container ;


    public View setContentView(int resourceId) {
        view = inflater.inflate(resourceId, container,false);
        return view;
    }


    public View getContentView(){
        return  this.view ;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater ;
        this.container = container ;
        return super.onCreateView(inflater,container,savedInstanceState);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public View findViewById(int id){
        return view.findViewById(id) ;
    }


}
