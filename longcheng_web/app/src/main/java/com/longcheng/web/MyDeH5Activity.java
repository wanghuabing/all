package com.longcheng.web;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 *
 */

public class MyDeH5Activity extends WebAct {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_web;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        setOrChangeTranslucentColor(toolbar, null);
    }

    @Override
    public void setListener() {
        super.setListener();
    }


    @Override
    public void initDataAfter() {
        super.initDataAfter();
        loadUrl("http://t.admin.asdyf.com/admin/");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEventBackground(MessageEvent event) {
        if (event != null) {
            String mesg = event.getMessage();
            if (!TextUtils.isEmpty(mesg) && mesg.equals("refreshPingTai")) {
                Log.e("mesg",""+mesg);
                loadUrl("http://t.admin.asdyf.com/admin/index");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 退出监听
     */
    public void onBackPressed() {
        exitDialog();
    }
}
