package com.longcheng.web;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.longcheng.web.adapter.TabPageAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseTabActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    TabHost mTabHost;
    @BindView(R.id.rb_bottom_home)
    RadioButton mButtonHome;

    @BindView(R.id.rb_bottom_helpWith)
    RadioButton mButtonHelpWith;

    @BindView(R.id.rg_bottom_main)
    RadioGroup mRadioGroup;

    @BindView(R.id.iv_show)
    Button iv_show;

    @BindView(R.id.iv_refresh)
    LinearLayout iv_refresh;

    @BindView(R.id.group)
    FreeMoveView group;
    /**
     * 是否显示全屏
     */
    boolean showAllStatus = false;
    public static final String TAB_MAIN = "MAIN_ACTIVITY";
    public static final String TAB_SPORTQURAT = "SPORTQURAT_ACTIVITY";

    int index = 0;
    private long startTime = 0;
    private long endTime = 0;

    private boolean isclick;

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show:
                if (showAllStatus) {
                    showAllStatus = false;
                } else {
                    showAllStatus = true;
                }
                setShow();
                break;
            case R.id.iv_refresh:
                setRefresh();
                break;
        }
    }

    private void setShow() {
        if (showAllStatus) {
            mRadioGroup.setVisibility(View.GONE);
            iv_show.setBackgroundResource(R.mipmap.map_shrink);
        } else {
            iv_show.setBackgroundResource(R.mipmap.map_all);
            mRadioGroup.setVisibility(View.VISIBLE);
        }
    }

    private void setRefresh() {
        if (index == 0) {
            // 发布事件
            new Thread("posting") {
                @Override
                public void run() {
                    EventBus.getDefault().post(new MessageEvent("refreshPingTai"));
                }
            }.start();
        } else {
            // 发布事件
            new Thread("posting") {
                @Override
                public void run() {
                    EventBus.getDefault().post(new MessageEvent("refreshShangHu"));
                }
            }.start();
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        setOrChangeTranslucentColor(mToolbar, null);
    }

    @Override
    public void initDataAfter() {
        initView();
    }

    @Override
    public void setListener() {
        iv_show.setOnClickListener(this);
        iv_refresh.setOnClickListener(this);
        iv_show.setOnTouchListener(onTouchListener);
        iv_refresh.setOnTouchListener(onTouchListener);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isclick = false;//当按下的时候设置isclick为false，具体原因看后边的讲解
                    startTime = System.currentTimeMillis();
                    System.out.println("执行顺序down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    isclick = true;//当按钮被移动的时候设置isclick为true
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.1 * 1000L) {
                        isclick = true;
                    } else {
                        isclick = false;
                    }
                    System.out.println("执行顺序up");
                    break;
                default:
            }
            return isclick;
        }
    };
    Intent i_main, i_sportqurat;

    private void initView() {
        mTabHost = getTabHost();
        i_main = new Intent(this, MyDeH5Activity.class);
        i_sportqurat = new Intent(this, MyDe2H5Activity.class);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN).setContent(i_main));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_SPORTQURAT).setIndicator(TAB_SPORTQURAT).setContent(i_sportqurat));

        mTabHost.setCurrentTabByTag(TAB_MAIN);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_bottom_home:
                        index = 0;
                        mTabHost.setCurrentTabByTag(TAB_MAIN);
                        break;
                    case R.id.rb_bottom_helpWith:
                        index = 1;
                        mTabHost.setCurrentTabByTag(TAB_SPORTQURAT);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }
}