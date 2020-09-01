package com.longcheng.lifecareplan.modular.mine.pioneercenter.financialmanage.fqborder.activity;

import com.longcheng.lifecareplan.api.Api;
import com.longcheng.lifecareplan.base.ExampleApplication;
import com.longcheng.lifecareplan.bean.ResponseBean;
import com.longcheng.lifecareplan.modular.index.login.activity.UserLoginBack403Utils;
import com.longcheng.lifecareplan.modular.mine.pioneercenter.financialmanage.fqborder.bean.PionRewardListDataBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：MarkShuai
 * 时间：2017/12/8 16:08
 * 邮箱：MarkShuai@163.com
 * 意图：
 */

public class PionRewardPresenterImp<T> extends PionRewardContract.Presenter<PionRewardContract.View> {

    private PionRewardContract.View mView;

    public PionRewardPresenterImp(PionRewardContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void fetch() {
    }

    /**
     * 订单类型 默认 0，全部  1，进行中 2，待发货 3，已完成
     *
     * @param user_id
     * @param page
     * @param page_size
     */
    public void getOrderList(String user_id, int type, int page, int page_size) {
        mView.showDialog();
        Observable<PionRewardListDataBean> observable = Api.getInstance().service.PionrewardCustomer(user_id, type,
                page, page_size, ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<PionRewardListDataBean>() {
                    @Override
                    public void accept(PionRewardListDataBean responseBean) throws Exception {
                        mView.dismissDialog();
                        if (!UserLoginBack403Utils.getInstance().login499Or500(responseBean.getStatus()))
                            mView.ListSuccess(responseBean, page);
                    }
                }, new io.reactivex.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.dismissDialog();
                        mView.ListError();
                    }
                });

    }

    /**
     * 确认打款
     */
    public void customerConfirmPayment(String user_id, String entrepreneurs_fuqibao_sell_order_id) {
        mView.showDialog();
        Observable<ResponseBean> observable = Api.getInstance().service.PioncustomerConfirmPayment(user_id, entrepreneurs_fuqibao_sell_order_id, ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<ResponseBean>() {
                    @Override
                    public void accept(ResponseBean responseBean) throws Exception {
                        mView.dismissDialog();
                        if (!UserLoginBack403Utils.getInstance().login499Or500(responseBean.getStatus()))
                            mView.editSuccess(responseBean);
                    }
                }, new io.reactivex.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.dismissDialog();
                        mView.ListError();
                    }
                });

    }

    /**
     * 驳回
     */
    public void PioncustomerBoHui(String user_id, String entrepreneurs_fuqibao_sell_order_id) {
        mView.showDialog();
        Observable<ResponseBean> observable = Api.getInstance().service.PioncustomerBoHui(user_id, entrepreneurs_fuqibao_sell_order_id, ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<ResponseBean>() {
                    @Override
                    public void accept(ResponseBean responseBean) throws Exception {
                        mView.dismissDialog();
                        if (!UserLoginBack403Utils.getInstance().login499Or500(responseBean.getStatus()))
                            mView.editSuccess(responseBean);
                    }
                }, new io.reactivex.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.dismissDialog();
                        mView.ListError();
                    }
                });

    }
}
