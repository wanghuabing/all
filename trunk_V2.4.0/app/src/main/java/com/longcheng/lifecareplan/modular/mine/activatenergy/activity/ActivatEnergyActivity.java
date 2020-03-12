package com.longcheng.lifecareplan.modular.mine.activatenergy.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longcheng.lifecareplan.R;
import com.longcheng.lifecareplan.api.Api;
import com.longcheng.lifecareplan.base.BaseActivity;
import com.longcheng.lifecareplan.base.ExampleApplication;
import com.longcheng.lifecareplan.config.Config;
import com.longcheng.lifecareplan.modular.bottommenu.ColorChangeByTime;
import com.longcheng.lifecareplan.modular.helpwith.autohelp.activity.AutoHelpH5Activity;
import com.longcheng.lifecareplan.modular.helpwith.connonEngineering.activity.BaoZhangActitvty;
import com.longcheng.lifecareplan.modular.index.login.activity.UserLoginBack403Utils;
import com.longcheng.lifecareplan.modular.mine.activatenergy.adapter.MoneyAdapter;
import com.longcheng.lifecareplan.modular.mine.activatenergy.bean.EnergyAfterBean;
import com.longcheng.lifecareplan.modular.mine.activatenergy.bean.EnergyItemBean;
import com.longcheng.lifecareplan.modular.mine.activatenergy.bean.GetEnergyListDataBean;
import com.longcheng.lifecareplan.modular.mine.bill.activity.EngryRecordActivity;
import com.longcheng.lifecareplan.modular.mine.userinfo.bean.EditDataBean;
import com.longcheng.lifecareplan.modular.mine.youzan.YouZanActivity;
import com.longcheng.lifecareplan.utils.ConfigUtils;
import com.longcheng.lifecareplan.utils.ConstantManager;
import com.longcheng.lifecareplan.utils.PriceUtils;
import com.longcheng.lifecareplan.utils.ToastUtils;
import com.longcheng.lifecareplan.utils.myview.MyGridView;
import com.longcheng.lifecareplan.utils.sharedpreferenceutils.SharedPreferencesHelper;
import com.longcheng.lifecareplan.utils.sharedpreferenceutils.UserUtils;
import com.longcheng.lifecareplan.widget.dialog.LoadingDialogAnim;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 激活能量
 */
public class ActivatEnergyActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pagetop_layout_left)
    LinearLayout pagetopLayoutLeft;
    @BindView(R.id.pageTop_tv_name)
    TextView pageTopTvName;
    @BindView(R.id.activat_tv_num)
    TextView activatTvNum;
    @BindView(R.id.activat_tv_cont)
    TextView activatTvCont;
    @BindView(R.id.activat_gv_money)
    MyGridView activatGvMoney;
    @BindView(R.id.activat_tv_account)
    TextView activatTvAccount;
    @BindView(R.id.activat_iv_accountselect)
    ImageView activatIvAccountselect;
    @BindView(R.id.activat_relat_account)
    RelativeLayout activatRelatAccount;
    @BindView(R.id.btn_jihuo)
    TextView btnJihuo;
    @BindView(R.id.activat_iv_yzselect)
    ImageView activatIvYzselect;
    @BindView(R.id.activat_relat_youzan)
    RelativeLayout activatRelatYouzan;
    @BindView(R.id.tv_zfstitle)
    TextView tv_zfstitle;
    @BindView(R.id.tv_youzan)
    TextView tv_youzan;
    @BindView(R.id.detailhelp_iv_zfsselect)
    ImageView detailhelpIvZfsselect;
    @BindView(R.id.detailhelp_relat_zfs)
    RelativeLayout detailhelpRelatZfs;
    @BindView(R.id.iv_youicon)
    ImageView iv_youicon;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_sxf)
    TextView tv_sxf;
    @BindView(R.id.tv_moneytitle)
    TextView tv_moneytitle;
    @BindView(R.id.btn_zfsrecord)
    TextView btn_zfsrecord;
    @BindView(R.id.et_money)
    EditText et_money;
    @BindView(R.id.tv_money_toengry)
    TextView tv_money_toengry;
    @BindView(R.id.layout_money)
    LinearLayout layout_money;
    @BindView(R.id.layout_showengry)
    LinearLayout layout_showengry;

    @BindView(R.id.activat_tv_zhifubao)
    TextView activat_tv_zhifubao;
    @BindView(R.id.activat_tv_mted)
    TextView activat_tv_mted;

    /**
     * 充值渠道激活类型 1现金; 2 有赞
     */
    private int payType = 4;
    /**
     * 祝福师-支付方式激活类型 1微信; 2 支付宝 ；3 秒提额度
     */
    private int zfs_payType = 2;
    private MoneyAdapter mMoneyAdapter;

    private String activate_ability_config_id;
    private String user_id;
    private int type;

    private int identityType;//3 祝福师   2普通用户
    private String moneyCont = "0";
    private String userRechargeListUrl;
    private String cookie_key;
    private String cookie_value;
    private String money_select = "0";
    private String First_energy = "0";
    private String Presenter_energy = "0";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pagetop_layout_left:
                doFinish();
                break;
            case R.id.activat_relat_youzan:
                payType = 2;
                selectPayTypeView();
                break;
            case R.id.activat_relat_account:
                payType = 1;
                selectPayTypeView();
                break;
            case R.id.detailhelp_relat_zfs:
                payType = 4;
                selectPayTypeView();
                break;
            case R.id.btn_jihuo:
                if (payType == 4) {
                    if (Double.parseDouble(moneyCont) > 0) {
                        String html = Config.BASE_HEAD_URL +
                                "home/Zhufubao/userRechargeMatchBless?money=" + moneyCont + "&pay_method=" + zfs_payType;
                        Intent intents = new Intent(mContext, BaoZhangActitvty.class);
                        intents.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intents.putExtra("html_url", html);
                        startActivity(intents);
                        ConfigUtils.getINSTANCE().setPageIntentAnim(intents, mActivity);
                    } else {
                        ToastUtils.showToast("请输入祝福宝数量");
                    }
                } else {
                    assetRecharge(user_id, money_select, activate_ability_config_id, type, "" + payType);
                }
                break;
            case R.id.btn_zfsrecord:
                Intent intent = new Intent(mContext, BaoZhangActitvty.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("html_url", userRechargeListUrl);
                startActivity(intent);
                ConfigUtils.getINSTANCE().setPageIntentAnim(intent, mActivity);
                break;
            case R.id.activat_tv_zhifubao:
                zfs_payType = 2;
                zfbSelectPayType();
                break;
            case R.id.activat_tv_mted:
                zfs_payType = 3;
                zfbSelectPayType();
                break;
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activatenergy;
    }

    @Override
    public void initView(View view) {
        pageTopTvName.setText("激活能量");
        setOrChangeTranslucentColor(toolbar, null);
    }

    @SuppressLint("NewApi")
    @Override
    public void setListener() {
        activat_tv_zhifubao.setOnClickListener(this);
        activat_tv_mted.setOnClickListener(this);
        btn_zfsrecord.setOnClickListener(this);
        pagetopLayoutLeft.setOnClickListener(this);
        activatRelatYouzan.setOnClickListener(this);
        detailhelpRelatZfs.setOnClickListener(this);
        activatRelatAccount.setOnClickListener(this);
        btnJihuo.setOnClickListener(this);
        activatGvMoney.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList != null && mList.size() > 0) {
                    if (payType == 1) {
                        for (int i = 0; i < mList.size(); i++) {
                            if (i == position) {
                                mList.get(position).setIs_default(1);
                            } else {
                                mList.get(i).setIs_default(0);
                            }
                        }
                        setAssetView(mList);
                        mMoneyAdapter.setList(mList);
                        mMoneyAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < YouzanList.size(); i++) {
                            if (i == position) {
                                YouzanList.get(position).setIs_default(1);
                            } else {
                                YouzanList.get(i).setIs_default(0);
                            }
                        }
                        setAssetView(YouzanList);
                        mMoneyAdapter.setList(YouzanList);
                        mMoneyAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        et_money.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {//搜索按键action
                    ConfigUtils.getINSTANCE().closeSoftInput(mActivity);
                    moneyCont = v.getText().toString();
                    if (TextUtils.isEmpty(moneyCont)) {
                        if (tv_money_toengry != null) {
                            moneyCont = "0";
                            tv_money_toengry.setText("获得" + moneyCont);
                        }
                        return true;
                    }
                    if (tv_money_toengry != null) {
                        String showEngry = PriceUtils.getInstance().gteMultiplySumPrice(moneyCont, "9");
                        tv_money_toengry.setText("获得" + showEngry);
                    }
                    return true;
                }
                return false;
            }
        });
        ConfigUtils.getINSTANCE().setEditTextInhibitInputSpace(et_money, 9);
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //还原数据
                if (et_money != null && payType == 4) {
                    moneyCont = et_money.getText().toString();
                    if (TextUtils.isEmpty(moneyCont)) {
                        moneyCont = "0";
                        tv_money_toengry.setText("获得" + moneyCont);
                    } else {
                        String showEngry = PriceUtils.getInstance().gteMultiplySumPrice(moneyCont, "9");
                        tv_money_toengry.setText("获得" + showEngry);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void initDataAfter() {
        user_id = (String) SharedPreferencesHelper.get(mContext, "user_id", "");
        getRechargeInfo(user_id);
        getYouZanCookie();
        zfbSelectPayType();
    }


    /**
     * 获取有赞cookie
     */
    public void getYouZanCookie() {
        Observable<GetEnergyListDataBean> observable = Api.getInstance().service.getYouZanCookie(UserUtils.getUserId(mContext), ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetEnergyListDataBean>() {
                    @Override
                    public void accept(GetEnergyListDataBean responseBean) throws Exception {
                        cookie_key = responseBean.getData().getCookie_key();
                        cookie_value = responseBean.getData().getCookie_value();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });

    }

    /**
     * 祝福宝-切换支付方式
     */
    private void zfbSelectPayType() {
        activat_tv_zhifubao.setTextColor(getResources().getColor(R.color.text_contents_color));
        activat_tv_mted.setTextColor(getResources().getColor(R.color.text_contents_color));
        activat_tv_zhifubao.setBackgroundResource(R.drawable.corners_bg_goodgray);
        activat_tv_mted.setBackgroundResource(R.drawable.corners_bg_goodgray);
        if (zfs_payType == 2) {
            activat_tv_zhifubao.setTextColor(getResources().getColor(R.color.white));
            activat_tv_zhifubao.setBackgroundResource(R.drawable.corners_bg_login);
            ColorChangeByTime.getInstance().changeDrawableToClolor(mActivity, activat_tv_zhifubao, R.color.red);
        } else if (zfs_payType == 3) {
            activat_tv_mted.setTextColor(getResources().getColor(R.color.white));
            activat_tv_mted.setBackgroundResource(R.drawable.corners_bg_login);
            ColorChangeByTime.getInstance().changeDrawableToClolor(mActivity, activat_tv_mted, R.color.red);
        }
    }

    /**
     * 切换充值渠道
     */
    private void selectPayTypeView() {
        if (mList != null && mList.size() > 0) {
            if (payType == 1) {
                mMoneyAdapter.setList(mList);
                mMoneyAdapter.notifyDataSetChanged();
                setAssetView(mList);
            } else {
                mMoneyAdapter.setList(YouzanList);
                mMoneyAdapter.notifyDataSetChanged();
                setAssetView(YouzanList);
            }
        }
        layout_showengry.setVisibility(View.VISIBLE);
        tv_zfstitle.setTextColor(getResources().getColor(R.color.text_contents_color));
        tv_youzan.setTextColor(getResources().getColor(R.color.text_contents_color));
        activatTvAccount.setTextColor(getResources().getColor(R.color.text_contents_color));
        activatTvCont.setVisibility(View.GONE);
        activatIvYzselect.setVisibility(View.GONE);
        activatRelatYouzan.setBackgroundResource(R.drawable.corners_bg_graybian);
        activatIvAccountselect.setVisibility(View.GONE);
        activatRelatAccount.setBackgroundResource(R.drawable.corners_bg_graybian);
        detailhelpIvZfsselect.setVisibility(View.GONE);
        detailhelpRelatZfs.setBackgroundResource(R.drawable.corners_bg_graybian);
        iv_youicon.setBackgroundResource(R.mipmap.superactivat_you_icon);
        tv_title.setText("激活后获取超级生命能量");
        tv_sxf.setVisibility(View.VISIBLE);
        tv_moneytitle.setText("请选择金额");
        btn_zfsrecord.setVisibility(View.GONE);
        layout_money.setVisibility(View.GONE);
        activatGvMoney.setVisibility(View.VISIBLE);
        if (payType == 2) {
            tv_youzan.setTextColor(getResources().getColor(R.color.red));
            activatIvYzselect.setVisibility(View.VISIBLE);
            activatRelatYouzan.setBackgroundResource(R.drawable.corners_bg_redbian);
            activatRelatYouzan.setPadding(0, 0, 0, 0);
            btnJihuo.setBackgroundColor(getResources().getColor(R.color.cyanbluebg));
            if (identityType == 3) {//可代充的祝福师
                iv_youicon.setBackgroundResource(R.mipmap.zhufubao_you_icon);
                btnJihuo.setText("立即激活 (祝福宝)");
                tv_title.setText("激活后获取祝福宝");
            } else {
                iv_youicon.setBackgroundResource(R.mipmap.superactivat_you_icon);
                btnJihuo.setText("立即激活 (超级生命能量)");
                tv_title.setText("激活后获取超级生命能量");
            }
        } else if (payType == 1) {
            activatTvAccount.setTextColor(getResources().getColor(R.color.red));
            activatIvAccountselect.setVisibility(View.VISIBLE);
            activatRelatAccount.setBackgroundResource(R.drawable.corners_bg_redbian);
            activatRelatAccount.setPadding(0, 0, 0, 0);
            btnJihuo.setText("立即激活 (生命能量)");
            btnJihuo.setBackgroundColor(getResources().getColor(R.color.red));
            iv_youicon.setBackgroundResource(R.mipmap.activat_you_icon);
            tv_title.setText("激活后获取生命能量");
            tv_sxf.setVisibility(View.GONE);
            activatTvCont.setVisibility(View.VISIBLE);
        } else if (payType == 4) {
            layout_showengry.setVisibility(View.GONE);
            tv_zfstitle.setTextColor(getResources().getColor(R.color.red));
            detailhelpIvZfsselect.setVisibility(View.VISIBLE);
            detailhelpRelatZfs.setBackgroundResource(R.drawable.corners_bg_redbian);
            detailhelpRelatZfs.setPadding(0, 0, 0, 0);
            btnJihuo.setText("立即激活 (超级生命能量)");
            btnJihuo.setBackgroundColor(getResources().getColor(R.color.cyanbluebg));
            tv_sxf.setVisibility(View.GONE);
            tv_moneytitle.setText("请填写代充祝福宝数量");
            btn_zfsrecord.setVisibility(View.VISIBLE);
            layout_money.setVisibility(View.VISIBLE);
            activatGvMoney.setVisibility(View.GONE);
            String showEngry = PriceUtils.getInstance().gteMultiplySumPrice(moneyCont, "9");
            tv_money_toengry.setText("获得" + showEngry);
        }
    }

    private void setAssetView(List<EnergyItemBean> list) {
        for (EnergyItemBean mEnergyItemBean : list) {
            if (mEnergyItemBean.getIs_default() == 1) {
                money_select = mEnergyItemBean.getMoney();
                First_energy = mEnergyItemBean.getReward_value();
                Presenter_energy = mEnergyItemBean.getGive_value();
                activate_ability_config_id = mEnergyItemBean.getActivate_ability_config_id();
                type = mEnergyItemBean.getType();
                if (payType == 1) {
                    activatTvCont.setText("激活" + First_energy + "+赠送" + Presenter_energy);
                    activatTvNum.setText(PriceUtils.getInstance().gteAddSumPrice(First_energy, Presenter_energy));
                } else {
                    activatTvCont.setText("");
                    activatTvNum.setText(First_energy);
                }
                break;
            }
        }
    }


    /**
     * @param
     * @name 激活生命能量数据
     * @time 2017/12/8 17:56
     * @author MarkShuai
     */
    public void getRechargeInfo(String user_id) {
        showDialog();
        Observable<GetEnergyListDataBean> observable = Api.getInstance().service.getRechargeInfo(user_id, ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetEnergyListDataBean>() {
                    @Override
                    public void accept(GetEnergyListDataBean responseBean) throws Exception {
                        dismissDialog();
                        if (!UserLoginBack403Utils.getInstance().login499Or500(responseBean.getStatus()))
                            ListSuccess(responseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissDialog();
                        ListError();
                    }
                });

    }

    /**
     * 激活生命能量
     *
     * @param user_id
     */
    public void assetRecharge(String user_id, String money, String activate_ability_config_id, int type, String payment_channel) {
        Log.e("Observable", "money=" + money + "  activate_ability_config_id=" + activate_ability_config_id + "  payment_channel=" + payment_channel);
        showDialog();
        Observable<EditDataBean> observable = Api.getInstance().service.assetRecharge(user_id, money,
                activate_ability_config_id, type, "2", payment_channel, ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EditDataBean>() {
                    @Override
                    public void accept(EditDataBean responseBean) throws Exception {
                        dismissDialog();
                        if (!UserLoginBack403Utils.getInstance().login499Or500(responseBean.getStatus())) {
                            GetPayWXSuccess(responseBean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissDialog();
                        ListError();
                    }
                });

    }

    /**
     * 祝福师代充是否有未完成订单
     */
    public void userBlessRecharge() {
        Observable<GetEnergyListDataBean> observable = Api.getInstance().service.userBlessRecharge(user_id, ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetEnergyListDataBean>() {
                    @Override
                    public void accept(GetEnergyListDataBean responseBean) throws Exception {
                        if (responseBean.getStatus().equals("200")) {
                            int status = responseBean.getData().getStatus();
                            if (status == 1) {
                                ToastUtils.showToast("您有未处理的激活申请");
                                Intent intent = new Intent(mContext, BaoZhangActitvty.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("html_url", responseBean.getData().getCallbackUrl());
                                startActivity(intent);
                                ConfigUtils.getINSTANCE().setPageIntentAnim(intent, mActivity);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ListError();
                    }
                });

    }

    /**
     * 祝福师代充
     *
     * @param recharge_money
     */
    public void SubmitActivation(String recharge_money) {
        showDialog();
        Observable<EditDataBean> observable = Api.getInstance().service.SubmitActivation(user_id, recharge_money,
                "2", ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EditDataBean>() {
                    @Override
                    public void accept(EditDataBean responseBean) throws Exception {
                        dismissDialog();
                        if (!UserLoginBack403Utils.getInstance().login499Or500(responseBean.getStatus())) {
                            if (responseBean.getStatus().equals("200")) {
                                Intent intent = new Intent(mContext, BaoZhangActitvty.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("html_url", responseBean.getData());
                                startActivity(intent);
                                ConfigUtils.getINSTANCE().setPageIntentAnim(intent, mActivity);
                            } else {
                                ToastUtils.showToast(responseBean.getData());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissDialog();
                        ListError();
                    }
                });

    }

    public void showDialog() {
        LoadingDialogAnim.show(mContext);
    }

    public void dismissDialog() {
        LoadingDialogAnim.dismiss(mContext);
    }

    List<EnergyItemBean> mList, YouzanList;

    public void ListSuccess(GetEnergyListDataBean responseBean) {
        String status = responseBean.getStatus();
        if (status.equals("400")) {
            ToastUtils.showToast(responseBean.getMsg());
        } else if (status.equals("200")) {
            EnergyAfterBean mEnergyAfterBean = responseBean.getData();
            if (mEnergyAfterBean != null) {
                String serviceCharge = mEnergyAfterBean.getServiceCharge();
                userRechargeListUrl = mEnergyAfterBean.getUserRechargeListUrl();
                identityType = mEnergyAfterBean.getIdentityType();
                if (identityType == 2) {
                    tv_sxf.setText("*使用有赞充值需要扣除" + serviceCharge + "手续费");
                } else {
                    tv_sxf.setText("*使用有赞充值祝福宝暂免手续费");
                }
                String asset = mEnergyAfterBean.getChatuser().getAsset();
                activatTvAccount.setText("现金余额:" + asset);
                mList = mEnergyAfterBean.getEnergys();
                YouzanList = mEnergyAfterBean.getYouzanConfig();
                if (YouzanList != null && YouzanList.size() > 0) {
                    mMoneyAdapter = new MoneyAdapter(mContext, YouzanList);
                    activatGvMoney.setAdapter(mMoneyAdapter);
                    selectPayTypeView();
                }
            }
        }
    }

    public void ListError() {
        ToastUtils.showToast(R.string.net_tishi);
    }

    public void GetPayWXSuccess(EditDataBean responseBean) {
        String status = responseBean.getStatus();
        if (status.equals("400")) {
            ToastUtils.showToast(responseBean.getMsg());
        } else if (status.equals("200")) {
            if (payType == 1) {
                jihuoSuccess();
            } else if (payType == 2) {
                Intent intent = new Intent(mContext, YouZanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("html_url", responseBean.getData());
                intent.putExtra("cookie_key", cookie_key);
                intent.putExtra("cookie_value", cookie_value);

                startActivity(intent);
                ConfigUtils.getINSTANCE().setPageIntentAnim(intent, mActivity);
            } else if (payType == 4) {

            }
        }
    }


    private void autohelpRefresh() {
        //智能互祝----刷新页面数据
        Intent intent = new Intent();
        intent.setAction(ConstantManager.BroadcastReceiver_KNP_ACTION);
        intent.putExtra("errCode", AutoHelpH5Activity.knpPaySuccessBack);
        sendBroadcast(intent);
    }

    private void jihuoSuccess() {
        autohelpRefresh();
        ToastUtils.showToast("激活成功");
        Intent intent = new Intent(mContext, EngryRecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        ConfigUtils.getINSTANCE().setPageIntentAnim(intent, mActivity);
        doFinish();
    }

    /**
     * 重写onkeydown 用于监听返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            doFinish();
        }
        return false;
    }
}
