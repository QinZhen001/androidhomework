package com.example.qinzhen.androidhomework.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.qinzhen.androidhomework.R;
import com.example.qinzhen.androidhomework.tab.BarEntity;
import com.example.qinzhen.androidhomework.tab.BottomTabBar;
import com.example.qinzhen.androidhomework.ui.fragment.NewsFragment;
import com.example.qinzhen.androidhomework.ui.fragment.HomeFragment;
import com.example.qinzhen.androidhomework.ui.fragment.PersonFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomTabBar.OnSelectListener {

    private static final String TAG = "MainActivity";

    private BottomTabBar tb;
    private List<BarEntity> bars;
    private HomeFragment homeFragment;
    private NewsFragment mNewsFragment;
    private PersonFragment personFragment;
    private FragmentManager manager;
    private long exitTime = 0;


    public static List<Activity> activityList = new LinkedList<Activity>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //finish_toash.cancel();
    }

    private void initView() {
        manager = getSupportFragmentManager();
        tb = (BottomTabBar) findViewById(R.id.tb);
        tb.setManager(manager);
        tb.setOnSelectListener(this);
        bars = new ArrayList<>();
        bars.add(new BarEntity("主页", R.drawable.ic_home_select, R.drawable.ic_home_unselect));
        bars.add(new BarEntity("新闻", R.drawable.ic_imagejoke_select, R.drawable.ic_imagejoke_unselect));
        bars.add(new BarEntity("我", R.drawable.ic_my_select, R.drawable.ic_my_unselect));
        tb.setBars(bars);
    }

    @Override
    public void onSelect(int position) {
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                tb.switchContent(homeFragment);
                break;
            case 1:
                if (mNewsFragment == null) {
                    mNewsFragment = new NewsFragment();
                }
                tb.switchContent(mNewsFragment);
                break;
            case 2:
                if (personFragment == null) {
                    personFragment = new PersonFragment();
                }
                tb.switchContent(personFragment);
                break;
            default:
                break;
        }
    }









    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(MainActivity.this,"再按一次退出app",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                //System.exit(0);   //System.exit(0)结束了进程   toast就不会消失 这里坑
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}
