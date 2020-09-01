/**
 *
 */
package com.longcheng.lifecareplan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.longcheng.lifecareplan.R;
import com.longcheng.lifecareplan.utils.myview.MyDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author 作者：zoc
 * @Description 类描述：上传图片
 */
public class AblumUtils {

    private Activity mContext;
    Handler mHandler;
    int mHandlerId;

    public AblumUtils(Activity mContext, Handler mHandler, int mHandlerId) {
        super();
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mHandlerId = mHandlerId;
        cacheDir = new File(Environment.getExternalStorageDirectory(), mkdirsName);

    }

    /**
     *
     */
    public AblumUtils() {
        super();
    }

    /**
     * **************************上传头像*******************************************
     * **
     */
    MyDialog selectDialog;

    public void onAddAblumClick() {
        if (selectDialog == null) {
            selectDialog = new MyDialog(mContext, R.style.dialog, R.layout.dialog_ablum);// 创建Dialog并设置样式主题
            selectDialog.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
            Window window = selectDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.showBottomDialog);
            selectDialog.show();
            WindowManager m = mContext.getWindowManager();
            Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = selectDialog.getWindow().getAttributes(); //获取对话框当前的参数值
            p.width = d.getWidth(); //宽度设置为屏幕
            selectDialog.getWindow().setAttributes(p); //设置生效
            TextView ablum_tv_photograph = (TextView) selectDialog.findViewById(R.id.ablum_tv_photograph);
            TextView ablum_tv_select = (TextView) selectDialog.findViewById(R.id.ablum_tv_select);
            TextView ablum_tv_cancel = (TextView) selectDialog.findViewById(R.id.ablum_tv_cancel);
            ablum_tv_cancel.setOnClickListener(mABlumClickListener);
            ablum_tv_photograph.setOnClickListener(mABlumClickListener);
            ablum_tv_select.setOnClickListener(mABlumClickListener);
        } else {
            selectDialog.show();
        }
    }

    OnClickListener mABlumClickListener = new OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ablum_tv_cancel:
                    break;
                case R.id.ablum_tv_photograph:
                    camera(v);
                    break;
                case R.id.ablum_tv_select:
                    gallery(v);
                    break;
                default:
                    break;
            }
            selectDialog.dismiss();
        }
    };
    /**
     * 头像名称
     */
    private static String file_name;
    public static File tempFile;
    public static String mkdirsName = "longcheng";
    File cacheDir;
    Uri mUriPath;

    /**
     * 手机相册
     *
     * @param view
     */
    public void gallery(View view) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mContext.startActivityForResult(intent, RESULTGALLERY);
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void camera(View view) {
        file_name = ConfigUtils.getINSTANCE().getRandomNumber(16) + ".jpg";
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            tempFile = new File(cacheDir, file_name);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                mUriPath = Uri.fromFile(tempFile);
            } else {
                /**
                 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                 * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                 */
                mUriPath = FileProvider.getUriForFile(mContext, "com.longcheng.lifecareplan.fileprovider", tempFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPath);
        }
        mContext.startActivityForResult(intent, RESULTCAMERA);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 剪切图片
     *
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        // 图片格式
//        intent.putExtra("outputFormat", "JPEG");
//        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
//        mContext.startActivityForResult(intent, RESULTCROP);


        /**
         * **************************修改1******************************************
         */
        //startActivityForResult(intent, CODE_RESULT_REQUEST); //直接调用此代码在小米手机有异常，换以下代码
        String mLinshi = System.currentTimeMillis() + "bala_crop.jpg";
        File mFile = new File(cacheDir, mLinshi);
        //mHeadCachePath = mHeadCacheFile.getAbsolutePath();
        mUriPath = Uri.parse("file://" + mFile.getAbsolutePath());
        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //注意：此处应设置return-data为false，如果设置为true，是直接返回bitmap格式的数据，耗费内存。设置为false，然后，设置裁剪完之后保存的路径，即：intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
        //intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivityForResult(intent, RESULTCROP);
        //https://blog.csdn.net/weixin_37165769/article/details/79892584?utm_source=copy
        /**
         * ********************************************************************
         */
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap toturn(Bitmap img, int jioadu) {
        Matrix matrix = new Matrix();
        matrix.postRotate(jioadu); /* +90 翻转90度 */
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    /**
     * 是否剪切
     */
    boolean cropStaus = false;

    public void setCropStaus(boolean cropStaus) {
//        this.cropStaus = cropStaus;
    }

    public final int RESULTCAMERA = 11;// 拍照
    public final int RESULTGALLERY = 22;// 相册
    public final int RESULTCROP = 33;// 剪切

    public void setResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTGALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                System.out.println("uri = " + uri);
                String picturePath = FilePathResolver.getPath(mContext, uri);
                tempFile = new File(picturePath);
                if (cropStaus) {
                    crop(uri);
                } else {
                    mUriPath = uri;
                    sendHandler();
                }
            }
        } else if (requestCode == RESULTCAMERA && resultCode == Activity.RESULT_OK) {
            if (hasSdcard()) {
                tempFile = new File(cacheDir, file_name);
                mUriPath = Uri.fromFile(tempFile);
                updateMedia(mContext, mUriPath.getPath());
                if (cropStaus) {
                    crop(mUriPath);
                } else {
                    sendHandler();
                }
            } else {
                ToastUtils.showToast("未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == RESULTCROP) {
            sendHandler();
        }
    }


    private void sendHandler() {
        try {
            /**
             * **************************修改1***关于小米手机问题***************************************
             */
            Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(mUriPath));
            if (bitmap == null) {
                String picturePath = FilePathResolver.getPath(mContext, mUriPath);
                if (TextUtils.isEmpty(picturePath)) {
                    picturePath = tempFile.getPath();
                }
                bitmap = BitmapFactory.decodeFile(picturePath);
            }
            if (!cropStaus) {
                String picturePath = FilePathResolver.getPath(mContext, mUriPath);
                if (TextUtils.isEmpty(picturePath)) {
                    picturePath = tempFile.getPath();
                }
                bitmap = getSmallBitmap(picturePath);
            }
            Log.e("Observable", "file:  " + bitmap.getByteCount());
            Message msgMessage = new Message();
            msgMessage.obj = bitmap;
            msgMessage.what = mHandlerId;
            mHandler.sendMessage(msgMessage);
            msgMessage = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaScannerConnection mScanner = null;

    public void updateMedia(Context fContext, final String fPath) {
        MimeTypeMap tMimeTypeMap = MimeTypeMap.getSingleton();
        String tExtension = fPath.substring(fPath.lastIndexOf(".") + 1);
        final String tType = tMimeTypeMap.getMimeTypeFromExtension(tExtension);

        if (mScanner == null) {
            MediaScannerConnection connection = new MediaScannerConnection(fContext,
                    new MediaScannerConnection.MediaScannerConnectionClient() {
                        public void onMediaScannerConnected() {
                            mScanner.scanFile(fPath, tType);
                        }

                        public void onScanCompleted(String path1, Uri uri) {
                            if (path1.equals(fPath)) {
                                mScanner.disconnect();
                                mScanner = null;
                            }
                        }
                    });
            try {
                connection.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mScanner = connection;
        } else {
            if (!mScanner.isConnected()) {
                mScanner.connect();
            }
            mScanner.scanFile(fPath, tType);
        }
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 70, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}