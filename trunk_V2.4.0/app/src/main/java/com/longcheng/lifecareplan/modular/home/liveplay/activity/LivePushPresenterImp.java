package com.longcheng.lifecareplan.modular.home.liveplay.activity;

import android.util.Log;

import com.longcheng.lifecareplan.api.Api;
import com.longcheng.lifecareplan.apiLive.ApiLive;
import com.longcheng.lifecareplan.base.ExampleApplication;
import com.longcheng.lifecareplan.http.api.DefaultBackObserver;
import com.longcheng.lifecareplan.http.api.DefaultObserver;
import com.longcheng.lifecareplan.http.basebean.BasicResponse;
import com.longcheng.lifecareplan.modular.home.liveplay.bean.LiveDetailInfo;
import com.longcheng.lifecareplan.modular.home.liveplay.bean.LiveStatusInfo;
import com.longcheng.lifecareplan.modular.home.liveplay.bean.VideoDataInfo;
import com.longcheng.lifecareplan.modular.home.liveplay.bean.VideoItemInfo;
import com.longcheng.lifecareplan.modular.mine.userinfo.bean.EditDataBean;
import com.longcheng.lifecareplan.utils.sharedpreferenceutils.UserUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：MarkShuai
 * 时间：2017/12/8 16:08
 * 邮箱：MarkShuai@163.com
 * 意图：
 */
public class LivePushPresenterImp<T> extends LivePushContract.Presenter<LivePushContract.View> {

    private RxAppCompatActivity mContext;
    private LivePushContract.View mView;
    private LivePushContract.Model mModel;

    public LivePushPresenterImp(RxAppCompatActivity mContext, LivePushContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void fetch() {
    }

    /**
     * 开播支付寿康宝
     */
    public void openRoomPay(String title, String cover_url, String address, double lon, double lat, String price) {
        mView.showDialog();
        ApiLive.getInstance().service.openRoomPay(UserUtils.getUserId(mContext)
                , title, cover_url, address, lon, lat, price)
                .compose(mContext.<BasicResponse<LiveStatusInfo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse<LiveStatusInfo>>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse<LiveStatusInfo> response) {
                        mView.dismissDialog();
                        mView.openRoomPaySuccess(response);
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 用户申请直播
     */
    public void applyLive() {
        mView.showDialog();
        ApiLive.getInstance().service.applyLive(UserUtils.getUserId(mContext))
                .compose(mContext.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        mView.dismissDialog();
                        mView.applyLiveSuccess(response);
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    public void uploadImg(String file) {
        mView.showDialog();
        Log.e("Observable", "file:  " + file);
        Observable<EditDataBean> observable = Api.getInstance().service.uploadImg(UserUtils.getUserId(mContext), file, ExampleApplication.token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<EditDataBean>() {
                    @Override
                    public void accept(EditDataBean responseBean) throws Exception {
                        mView.dismissDialog();
                        mView.editAvatarSuccess(responseBean);
                    }
                }, new io.reactivex.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.dismissDialog();
                        mView.Error();
                        Log.e("Observable", throwable.toString());
                    }
                });

    }

    /**
     * 用户申请直播详情
     */
    public void getUserLiveStatus() {
        mView.showDialog();
        ApiLive.getInstance().service.getUserLiveStatus(UserUtils.getUserId(mContext))
                .compose(mContext.<BasicResponse<LiveStatusInfo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse<LiveStatusInfo>>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse<LiveStatusInfo> response) {
                        mView.dismissDialog();
                        mView.getUserLiveStatusSuccess(response);
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 直播间-评论
     */
    public void setLComment(String live_room_id, String content) {
        mView.showGiftDialog();
        ApiLive.getInstance().service.setLComment(UserUtils.getUserId(mContext), live_room_id, content)
                .compose(mContext.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        mView.dismissDialog();
                        mView.sendLCommentSuccess(response);
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 直播- 关注
     */
    public void setFollowLive(String user_id, String live_room_id) {
        mView.showDialog();
        ApiLive.getInstance().service.setFollowLive(user_id, live_room_id)
                .compose(mContext.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        mView.dismissDialog();
                        mView.setFollowLiveSuccess(response);
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 直播间设在线人数
     */
    public void setLiveOnlineNumber(String live_room_id, int type) {
        mView.showDialog();
        ApiLive.getInstance().service.setLiveOnlineNumber(UserUtils.getUserId(mContext), live_room_id, type)
                .compose(mContext.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        mView.dismissDialog();
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 直播间设置状态
     */
    public void setLiveRoomBroadcastStatus(String live_room_id, int broadcast_status) {
        mView.showDialog();
        ApiLive.getInstance().service.setLiveRoomBroadcastStatus(UserUtils.getUserId(mContext), live_room_id, broadcast_status)
                .compose(mContext.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        mView.dismissDialog();
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 直播添加转发数量
     */
    public void addForwardNumber(String live_room_id) {
        ApiLive.getInstance().service.addForwardNumber(UserUtils.getUserId(mContext), live_room_id)
                .compose(mContext.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        mView.sendLCommentSuccess(response);
                    }

                    @Override
                    public void onError() {
                        mView.Error();
                    }
                });
    }

    /**
     * 直播-送礼物
     */
    public void giveGift(String live_room_id, String live_gift_id, int help_number) {
        mView.showGiftDialog();
        ApiLive.getInstance().service.giveGift(UserUtils.getUserId(mContext), live_room_id, live_gift_id, help_number)
                .compose(mContext.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        mView.giveGiftSuccess(response);
                        mView.dismissDialog();
                    }

                    @Override
                    public void onError() {
                        mView.Error();
                        mView.dismissDialog();
                    }
                });
    }

    /**
     * 获取直播间详情
     */
    public void getLivePlayInfo(String live_room_id) {
        mView.showDialog();
        ApiLive.getInstance().service.getLivePlayInfo(UserUtils.getUserId(mContext), live_room_id)
                .compose(mContext.<BasicResponse<LiveDetailInfo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse<LiveDetailInfo>>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse<LiveDetailInfo> response) {
                        mView.BackLiveDetailSuccess(response);
                        mView.dismissDialog();
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 获取直播列表
     */
    public void getLivePlayList(int page, int page_size) {
        mView.showDialog();
        ApiLive.getInstance().service.getLiveList(UserUtils.getUserId(mContext),
                page, page_size)
                .compose(mContext.<BasicResponse<VideoDataInfo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse<VideoDataInfo>>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse<VideoDataInfo> response) {
                        mView.dismissDialog();
                        mView.BackLiveListSuccess(response, page);
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }

    /**
     * 获取短视频列表
     */
    public void getVideoPlayList(int page, int page_size) {
        mView.showDialog();
        ApiLive.getInstance().service.getVideoList(UserUtils.getUserId(mContext),
                page, page_size)
                .compose(mContext.<BasicResponse<ArrayList<VideoItemInfo>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultBackObserver<BasicResponse<ArrayList<VideoItemInfo>>>(mContext) {
                    @Override
                    public void onSuccess(BasicResponse<ArrayList<VideoItemInfo>> response) {
                        mView.dismissDialog();
                        mView.BackVideoListSuccess(response, page);
                    }

                    @Override
                    public void onError() {
                        mView.dismissDialog();
                        mView.Error();
                    }
                });
    }
}
