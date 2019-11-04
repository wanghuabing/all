package com.longcheng.lifecareplan.modular.home.liveplay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePushNetworkListener;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.SurfaceStatus;
import com.longcheng.lifecareplan.R;
import com.longcheng.lifecareplan.base.BaseActivity;
import com.longcheng.lifecareplan.modular.home.fragment.HomeFragment;
import com.longcheng.lifecareplan.utils.ConfigUtils;
import com.longcheng.lifecareplan.utils.ToastUtils;
import com.longcheng.lifecareplan.utils.myview.SupplierEditText;
import com.longcheng.lifecareplan.utils.network.LocationUtils;
import com.longcheng.lifecareplan.utils.network.NetUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;

import static com.alivc.live.pusher.AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK;
import static com.alivc.live.pusher.AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT;

public class LivePushActivity extends BaseActivity {
    private static final String URL_KEY = "url_key";
    private static final String ASYNC_KEY = "async_key";
    private static final String AUDIO_ONLY_KEY = "audio_only_key";
    private static final String VIDEO_ONLY_KEY = "video_only_key";
    private static final String ORIENTATION_KEY = "orientation_key";
    private static final String CAMERA_ID = "camera_id";
    private static final String FLASH_ON = "flash_on";
    private static final String AUTH_TIME = "auth_time";
    private static final String PRIVACY_KEY = "privacy_key";
    private static final String MIX_EXTERN = "mix_extern";
    private static final String MIX_MAIN = "mix_main";
    private static final int REQ_CODE_PUSH = 0x1112;
    @BindView(R.id.preview_view)
    SurfaceView previewView;
    @BindView(R.id.frag_tv_playstatus)
    TextView fragTvPlaystatus;
    @BindView(R.id.frag_tv_jieqi)
    TextView fragTvJieqi;
    @BindView(R.id.frag_tv_city)
    TextView fragTvCity;
    @BindView(R.id.frag_layout_city)
    LinearLayout fragLayoutCity;
    @BindView(R.id.frag_layout_rank)
    LinearLayout fragLayoutRank;
    @BindView(R.id.btn_exit)
    ImageView btnExit;
    @BindView(R.id.lv_rankdata)
    ListView lvRankdata;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.frag_layout_name)
    LinearLayout fragLayoutName;
    @BindView(R.id.lv_msg)
    ListView lvMsg;
    @BindView(R.id.edt_content)
    SupplierEditText edtContent;
    @BindView(R.id.btn_liwu)
    ImageView btnLiwu;
    @BindView(R.id.btn_camera)
    ImageView btnCamera;
    @BindView(R.id.relat_push)
    RelativeLayout relatPush;


    private AlivcLivePushConfig mAlivcLivePushConfig;

    private AlivcLivePusher mAlivcLivePusher = null;
    private String mPushUrl = null;

    private boolean mAsync = false;
    private boolean mAudioOnly = false;
    private boolean mVideoOnly = false;

    private SurfaceStatus mSurfaceStatus = SurfaceStatus.UNINITED;
    private boolean isPause = false;

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean mFlash = false;
    private boolean mMixExtern = false;
    private boolean mMixMain = false;

    private String mAuthTime = "";
    private String mPrivacyKey = "";

    private boolean videoThreadOn = false;
    private boolean audioThreadOn = false;

    private int mNetWork = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAllowFullScreen(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.live_push;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void setListener() {
        previewView.getHolder().addCallback(mCallback);
        btnLiwu.setVisibility(View.GONE);
        btnCamera.setOnClickListener(onClickListener);
        btnExit.setOnClickListener(onClickListener);
        fragLayoutName.setOnClickListener(onClickListener);
        edtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(edtContent.getText().toString().trim())) {
                        String content = edtContent.getText().toString().trim();
                        edtContent.setText("");
                        ToastUtils.showToast("功能开发中...");
                    }
                    ConfigUtils.getINSTANCE().closeSoftInput(mActivity);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initDataAfter() {
        initData();
        String city = new LocationUtils().getAddressCity(this);
        fragTvCity.setText("" + city);
        fragTvJieqi.setText(HomeFragment.jieqi_name + "节气");
        Intent intent = getIntent();
        String playTitle = intent.getStringExtra("playTitle");
        fragTvPlaystatus.setText("直播中: " + playTitle);
        String live_name = intent.getStringExtra("live_name");
        tvName.setText("祝福" + live_name);
    }


    private void initData() {
        mPushUrl = getIntent().getStringExtra(URL_KEY);
        mAsync = getIntent().getBooleanExtra(ASYNC_KEY, false);
        mAudioOnly = getIntent().getBooleanExtra(AUDIO_ONLY_KEY, false);
        mVideoOnly = getIntent().getBooleanExtra(VIDEO_ONLY_KEY, false);
        mCameraId = getIntent().getIntExtra(CAMERA_ID, Camera.CameraInfo.CAMERA_FACING_FRONT);
        mFlash = getIntent().getBooleanExtra(FLASH_ON, false);
        mAuthTime = getIntent().getStringExtra(AUTH_TIME);
        mPrivacyKey = getIntent().getStringExtra(PRIVACY_KEY);
        mMixExtern = getIntent().getBooleanExtra(MIX_EXTERN, false);
        mMixMain = getIntent().getBooleanExtra(MIX_MAIN, false);
        mAlivcLivePushConfig = (AlivcLivePushConfig) getIntent().getSerializableExtra(AlivcLivePushConfig.CONFIG);
        mAlivcLivePusher = new AlivcLivePusher();
        try {
            mAlivcLivePusher.init(getApplicationContext(), mAlivcLivePushConfig);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mNetWork = NetUtils.getAPNType(this);
        mAlivcLivePusher.setLivePushInfoListener(mPushInfoListener);
        mAlivcLivePusher.setLivePushNetworkListener(mPushNetworkListener);
    }

    public static final int SENDLIWU = 11;
    public static final int CAMERA = 22;
    public static final int EXIT = 33;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SENDLIWU:
                    break;
                case CAMERA:
                    break;
                case EXIT:
                    break;
            }
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_exit:
                    finish();
                    break;
                case R.id.btn_camera:
                    if (mCameraId == CAMERA_TYPE_FRONT.getCameraId()) {
                        mCameraId = CAMERA_TYPE_BACK.getCameraId();
                    } else {
                        mCameraId = CAMERA_TYPE_FRONT.getCameraId();
                    }
                    mAlivcLivePusher.switchCamera();
                    break;
                case R.id.btn_liwu:
                    ToastUtils.showToast("功能开发中...");
                    break;
                case R.id.frag_layout_name:
                    ToastUtils.showToast("功能开发中...");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 开启
     */
    public void startPlay() {
        if (mAsync) {
            mAlivcLivePusher.startPushAysnc(mPushUrl);
        } else {
            mAlivcLivePusher.startPush(mPushUrl);
        }
    }

    SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (mSurfaceStatus == SurfaceStatus.UNINITED) {
                mSurfaceStatus = SurfaceStatus.CREATED;
                if (mAlivcLivePusher != null) {
                    try {
                        if (mAsync) {
                            mAlivcLivePusher.startPreviewAysnc(previewView);
                        } else {
                            mAlivcLivePusher.startPreview(previewView);
                        }
                        if (mAlivcLivePushConfig.isExternMainStream()) {
                            startYUV(getApplicationContext());
                            startPCM(getApplicationContext());
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startPlay();
                            }
                        }, 2200);
                    } catch (IllegalArgumentException e) {
                        e.toString();
                    } catch (IllegalStateException e) {
                        e.toString();
                    }
                }
            } else if (mSurfaceStatus == SurfaceStatus.DESTROYED) {
                mSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            mSurfaceStatus = SurfaceStatus.CHANGED;
            Log.d("MyCallBack", "surfaceChangedsurfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };

    public static void startActivity(Activity activity, AlivcLivePushConfig alivcLivePushConfig,
                                     String url, boolean async, boolean audioOnly, boolean videoOnly,
                                     AlivcPreviewOrientationEnum orientation, int cameraId,
                                     boolean isFlash, String authTime, String privacyKey,
                                     boolean mixExtern, boolean mixMain, String playTitle, String live_name) {
        Intent intent = new Intent(activity, LivePushActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AlivcLivePushConfig.CONFIG, alivcLivePushConfig);
        bundle.putString(URL_KEY, url);
        bundle.putBoolean(ASYNC_KEY, async);
        bundle.putBoolean(AUDIO_ONLY_KEY, audioOnly);
        bundle.putBoolean(VIDEO_ONLY_KEY, videoOnly);
        bundle.putInt(ORIENTATION_KEY, orientation.ordinal());
        bundle.putInt(CAMERA_ID, cameraId);
        bundle.putBoolean(FLASH_ON, isFlash);
        bundle.putString(AUTH_TIME, authTime);
        bundle.putString(PRIVACY_KEY, privacyKey);
        bundle.putBoolean(MIX_EXTERN, mixExtern);
        bundle.putBoolean(MIX_MAIN, mixMain);
        bundle.putString("playTitle", playTitle);
        bundle.putString("live_name", live_name);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQ_CODE_PUSH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAlivcLivePusher != null) {
            try {
                if (!isPause) {
                    if (mAsync) {
                        mAlivcLivePusher.resumeAsync();
                    } else {
                        mAlivcLivePusher.resume();
                    }
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAlivcLivePusher != null) {
            try {
                if (mAlivcLivePusher != null/*.isPushing()*/) {
                    mAlivcLivePusher.pause();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        videoThreadOn = false;
        audioThreadOn = false;
        if (mAlivcLivePusher != null) {
            try {
                mAlivcLivePusher.destroy();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        previewView = null;
        mAlivcLivePushConfig = null;
        mAlivcLivePusher = null;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        AlivcPreviewOrientationEnum orientationEnum;
        if (mAlivcLivePusher != null) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientationEnum = ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientationEnum = ORIENTATION_LANDSCAPE_HOME_RIGHT;
                    break;
                case Surface.ROTATION_270:
                    orientationEnum = ORIENTATION_LANDSCAPE_HOME_LEFT;
                    break;
                default:
                    orientationEnum = ORIENTATION_PORTRAIT;
                    break;
            }
            try {
                mAlivcLivePusher.setPreviewOrientation(orientationEnum);
            } catch (IllegalStateException e) {

            }
        }
    }

    public AlivcLivePusher getLivePusher() {
        return this.mAlivcLivePusher;
    }

    public SurfaceView getPreviewView() {
        return this.previewView;
    }

    public interface PauseState {
        void updatePause(boolean state);
    }

    private PauseState mStateListener = new PauseState() {
        @Override
        public void updatePause(boolean state) {
            isPause = state;
        }
    };

    class ConnectivityChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                if (mNetWork != NetUtils.getAPNType(context)) {
                    mNetWork = NetUtils.getAPNType(context);
                    if (mAlivcLivePusher != null) {
                        if (mAlivcLivePusher.isPushing()) {
                            try {
                                mAlivcLivePusher.reconnectPushAsync(null);
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }
    }

    private void showToast(final String text) {
        if (text == null) {
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(LivePushActivity.this, text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    AlivcLivePushInfoListener mPushInfoListener = new AlivcLivePushInfoListener() {
        @Override
        public void onPreviewStarted(AlivcLivePusher pusher) {
        }

        @Override
        public void onPreviewStoped(AlivcLivePusher pusher) {
        }

        @Override
        public void onPushStarted(AlivcLivePusher pusher) {
            showToast(getString(R.string.start_button));
        }

        @Override
        public void onPushStoped(AlivcLivePusher pusher) {
            showToast(getString(R.string.stop_button));
        }

        @Override
        public void onFirstAVFramePushed(AlivcLivePusher pusher) {
        }

        @Override
        public void onPushPauesed(AlivcLivePusher pusher) {
            showToast(getString(R.string.pause_button));
        }

        @Override
        public void onPushResumed(AlivcLivePusher pusher) {
            showToast(getString(R.string.resume_button));
        }


        /**
         * 推流重启通知
         *
         * @param pusher AlivcLivePusher实例
         */
        @Override
        public void onPushRestarted(AlivcLivePusher pusher) {
//            showToast(getString(R.string.restart_success));
        }

        @Override
        public void onFirstFramePreviewed(AlivcLivePusher pusher) {
        }

        @Override
        public void onDropFrame(AlivcLivePusher pusher, int countBef, int countAft) {
        }

        @Override
        public void onAdjustBitRate(AlivcLivePusher pusher, int curBr, int targetBr) {
        }

        @Override
        public void onAdjustFps(AlivcLivePusher pusher, int curFps, int targetFps) {
        }
    };

    AlivcLivePushNetworkListener mPushNetworkListener = new AlivcLivePushNetworkListener() {
        @Override
        public void onNetworkPoor(AlivcLivePusher pusher) {
            showToast(getString(R.string.network_poor));
        }

        @Override
        public void onNetworkRecovery(AlivcLivePusher pusher) {
            showToast(getString(R.string.network_recovery));
            mAlivcLivePusher.reconnectPushAsync(null);
        }

        @Override
        public void onReconnectStart(AlivcLivePusher pusher) {
//            showToastShort(getString(R.string.reconnect_start));
        }

        @Override
        public void onReconnectFail(AlivcLivePusher pusher) {

//            showDialog(getString(R.string.reconnect_fail));
        }

        @Override
        public void onReconnectSucceed(AlivcLivePusher pusher) {
//            showToast(getString(R.string.reconnect_success));
        }

        @Override
        public void onSendDataTimeout(AlivcLivePusher pusher) {
//            showDialog(getString(R.string.senddata_timeout));
        }

        @Override
        public void onConnectFail(AlivcLivePusher pusher) {
//            showDialog(getString(R.string.connect_fail));
        }

        @Override
        public String onPushURLAuthenticationOverdue(AlivcLivePusher alivcLivePusher) {
            return null;
        }

        @Override
        public void onConnectionLost(AlivcLivePusher pusher) {
//            showToast("推流已断开");
        }


        @Override
        public void onSendMessage(AlivcLivePusher pusher) {
//            showToast(getString(R.string.send_message));
        }

        @Override
        public void onPacketsLost(AlivcLivePusher pusher) {
//            showToast("推流丢包通知");
        }
    };

    public void startYUV(final Context context) {
        new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private AtomicInteger atoInteger = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("LivePushActivity-readYUV-Thread" + atoInteger.getAndIncrement());
                return t;
            }
        }).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                videoThreadOn = true;
                byte[] yuv;
                InputStream myInput = null;
                try {
                    File f = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/capture0.yuv");
                    myInput = new FileInputStream(f);
                    byte[] buffer = new byte[1280 * 720 * 3 / 2];
                    int length = myInput.read(buffer);
                    //发数据
                    while (length > 0 && videoThreadOn) {
                        mAlivcLivePusher.inputStreamVideoData(buffer, 720, 1280, 720, 1280 * 720 * 3 / 2, System.nanoTime() / 1000, 0);
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //发数据
                        length = myInput.read(buffer);
                        if (length <= 0) {
                            myInput.close();
                            myInput = new FileInputStream(f);
                            length = myInput.read(buffer);
                        }
                    }
                    myInput.close();
                    videoThreadOn = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void stopYUV() {
        videoThreadOn = false;
    }

    private void startPCM(final Context context) {
        new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private AtomicInteger atoInteger = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("LivePushActivity-readPCM-Thread" + atoInteger.getAndIncrement());
                return t;
            }
        }).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                audioThreadOn = true;
                byte[] pcm;
                int allSended = 0;
                int sizePerSecond = 44100 * 2;
                InputStream myInput = null;
                OutputStream myOutput = null;
                boolean reUse = false;
                long startPts = System.nanoTime() / 1000;
                try {
                    File f = new File("/sdcard/alivc_resource/441.pcm");
                    myInput = new FileInputStream(f);
                    byte[] buffer = new byte[2048];
                    int length = myInput.read(buffer, 0, 2048);
                    while (length > 0 && audioThreadOn) {
                        long pts = System.nanoTime() / 1000;
                        mAlivcLivePusher.inputStreamAudioData(buffer, length, 44100, 1, pts);
                        allSended += length;
                        if ((allSended * 1000000L / sizePerSecond - 50000) > (pts - startPts)) {
                            try {
                                Thread.sleep(45);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        length = myInput.read(buffer);
                        if (length < 2048) {
                            myInput.close();
                            myInput = new FileInputStream(f);
                            length = myInput.read(buffer);
                        }
                        try {
                            Thread.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myInput.close();
                    audioThreadOn = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void stopPcm() {
        audioThreadOn = false;
    }

}