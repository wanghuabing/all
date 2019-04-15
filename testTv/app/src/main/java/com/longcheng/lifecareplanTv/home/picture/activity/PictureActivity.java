package com.longcheng.lifecareplanTv.home.picture.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longcheng.lifecareplanTv.R;
import com.longcheng.lifecareplanTv.api.BasicResponse;
import com.longcheng.lifecareplanTv.base.BaseActivityMVP;
import com.longcheng.lifecareplanTv.home.picture.adapter.PictureAdapter;
import com.longcheng.lifecareplanTv.home.set.SetActivity;
import com.longcheng.lifecareplanTv.login.bean.LoginAfterBean;
import com.longcheng.lifecareplanTv.utils.ConfigUtils;
import com.longcheng.lifecareplanTv.utils.DatesUtils;
import com.longcheng.lifecareplanTv.utils.ToastUtilsNew;
import com.longcheng.lifecareplanTv.utils.tvrecyclerview.SearchResultModel;
import com.longcheng.lifecareplanTv.utils.tvrecyclerview.TvGridLayoutManager;
import com.longcheng.lifecareplanTv.utils.tvrecyclerview.TvRecyclerViewPic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 图片
 */
public class PictureActivity extends BaseActivityMVP<PictureContract.View, PicturePresenterImp<PictureContract.View>> implements PictureContract.View {

    @BindView(R.id.pageTop_tv_time)
    TextView pageTopTvTime;
    @BindView(R.id.pageTop_tv_date)
    TextView pageTopTvDate;
    @BindView(R.id.pageTop_tv_week)
    TextView pageTopTvWeek;
    @BindView(R.id.pagetop_layout_set)
    LinearLayout pagetopLayoutSet;
    @BindView(R.id.pageTop_tv_name)
    TextView pageTopTvName;
    @BindView(R.id.pageTop_tv_phone)
    TextView pageTopTvPhone;
    @BindView(R.id.pagetop_layout_rigth)
    LinearLayout pagetopLayoutRigth;
    @BindView(R.id.pageTop_iv_thumb)
    ImageView pageTopIvThumb;
    @BindView(R.id.search_result_recyclerView)
    TvRecyclerViewPic mRecyclerView;
    @BindView(R.id.layout_left)
    LinearLayout layoutLeft;
    @BindView(R.id.layout_right)
    LinearLayout layoutRight;
    @BindView(R.id.iv_first_image)
    ImageView ivFirstImage;
    @BindView(R.id.layout_first_img)
    LinearLayout layoutFirstImg;
    @BindView(R.id.tv_first_title)
    TextView tvFirstTitle;
    @BindView(R.id.layout_cont)
    LinearLayout layout_cont;
    private TvGridLayoutManager mLayoutManager;
    private PictureAdapter mAdapter;

    @Override
    public void showDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.picture;
    }

    @Override
    public void initView(View view) {
        initTimer();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setListener() {
        pagetopLayoutRigth.setVisibility(View.VISIBLE);
        pagetopLayoutSet.setOnClickListener(this);
        layoutLeft.setOnClickListener(this);
        layoutRight.setOnClickListener(this);
        layoutFirstImg.setOnClickListener(this);
        ConfigUtils.getINSTANCE().setSelectFouseText(pagetopLayoutSet);
        ConfigUtils.getINSTANCE().setSelectFouseText(layoutRight);
        ConfigUtils.getINSTANCE().setSelectFouseImg(layoutFirstImg);
        pagetopLayoutSet.setNextFocusLeftId(R.id.layout_left);
        pagetopLayoutSet.setNextFocusRightId(R.id.layout_right);
        pagetopLayoutSet.setNextFocusDownId(R.id.layout_left);
        pagetopLayoutSet.setFocusable(false);
        layoutLeft.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layoutLeft.setBackgroundResource(R.drawable.corners_bg_selectred);
                } else {
                    layoutLeft.setBackgroundResource(R.drawable.corners_bg_transparent);
                }
                pagetopLayoutSet.setFocusable(true);
            }
        });
    }

    @Override
    public void initDataAfter() {
        mRecyclerView.setBtnFouse(pagetopLayoutSet);
        //去掉动画,否则当notify数据的时候,焦点会丢失
        mRecyclerView.setItemAnimator(null);
        mLayoutManager = new TvGridLayoutManager(this, 4);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setSelectedItemOffset(0, 0);
        mRecyclerView.setSelectedItemAtCentered(true);
        initData(1);
    }

    int page = 1;
    int pageSize = 9;
    int totalPage = 10;
    SearchResultModel firstInfo;

    public void initData(int page_) {
        this.page = page_;
        List<SearchResultModel> dataList = new ArrayList<>();
        for (int i = (page - 1) * pageSize; i < (page) * pageSize; i++) {
            dataList.add(new SearchResultModel(i + 1, "title : " + (i + 1)));
        }
        firstInfo = dataList.get(0);
        tvFirstTitle.setText("节气" + firstInfo.id);
        List<SearchResultModel> dataListnew = new ArrayList<>();
        for (int i = 1; i < dataList.size(); i++) {
            dataListnew.add(dataList.get(i));
        }
        mAdapter = new PictureAdapter(this, mRecyclerView, mLayoutManager, dataListnew);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setDateInfo() {
        String date = DatesUtils.getInstance().getNowTime("yyyy-MM-dd");
        pageTopTvDate.setText(date);
        String week = DatesUtils.getInstance().getNowTime("EE");
        pageTopTvWeek.setText(week);
        String time = DatesUtils.getInstance().getNowTime("HH:mm");
        pageTopTvTime.setText(time);
    }

    Animation myleftAnim;
    Animation myrightAnim;

    /**
     * 淡入淡出动画方法
     */
    public void alpha() {
        // 创建透明度动画，第一个参数是开始的透明度，第二个参数是要转换到的透明度
        AlphaAnimation alphaAni = new AlphaAnimation(0.4f, 1f);
        //设置动画执行的时间，单位是毫秒
        alphaAni.setDuration(800);
        // 设置动画结束后停止在哪个状态（true表示动画完成后的状态）
//        alphaAni.setFillAfter(false);
        // true动画结束后回到开始状态
        alphaAni.setFillBefore(true);
        // 设置动画重复次数 // -1或者Animation.INFINITE表示无限重复，正数表示重复次数，0表示不重复只播放一次
        alphaAni.setRepeatCount(0);
        // 设置动画模式（Animation.REVERSE设置循环反转播放动画,Animation.RESTART每次都从头开始）
        alphaAni.setRepeatMode(Animation.REVERSE); // 启动动画
        layout_cont.startAnimation(alphaAni);
    }

    /**
     * 设置从左向右移动
     */
    private void setMoveToRight() {
        if (myrightAnim == null) {
            myrightAnim = AnimationUtils.loadAnimation(mActivity, R.anim.out_to_rigth);
            myrightAnim.setFillAfter(false);//android动画结束后停在结束位置
        }
        layout_cont.startAnimation(myrightAnim);
    }

    /**
     * 设置从右向左移动
     */
    private void setMoveToLeft() {
        if (myleftAnim == null) {
            myleftAnim = AnimationUtils.loadAnimation(mActivity, R.anim.out_to_left);
            myleftAnim.setFillAfter(false);//android动画结束后停在结束位置
        }
        layout_cont.startAnimation(myleftAnim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pagetop_layout_set:
                Intent intent = new Intent(this, SetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.layout_left:
                if (page > 1) {
                    setMoveToRight();
                    initData(page - 1);
                } else {
                    ToastUtilsNew.showToast("已到第一页");
                }
                break;
            case R.id.layout_right:
                if (page < totalPage) {
                    setMoveToLeft();
                    initData(page + 1);
                } else {
                    ToastUtilsNew.showToast("已到最后一页");
                }
                break;
            case R.id.layout_first_img:
                ToastUtilsNew.showToast("点击了  " + firstInfo.id);
                break;
            default:
                break;
        }
    }

    @Override
    protected PicturePresenterImp<PictureContract.View> createPresent() {
        return new PicturePresenterImp<>(mActivity, this);
    }


    @Override
    public void getMenuInfoSuccess(BasicResponse<LoginAfterBean> responseBean) {

    }


    @Override
    public void onError() {
        ToastUtilsNew.showToast(getString(R.string.net_tishi));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 重写onkeydown 用于监听返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return false;
    }
}
