package com.zh.education.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {
	private static ProgressDialog mProgressDialog;

	/**
	 * 显示ProgressDialog
	 * 
	 * @param context
	 * @param message
	 */
	public static void showProgressDialog(Context context, CharSequence message,String status) {
		if(status.equals("userLogin")){
			if (mProgressDialog == null) {
				mProgressDialog = ProgressDialog.show(context, "", message);
			} else {
				mProgressDialog.show();
			}
		}
	}

	/**
	 * 关闭ProgressDialog
	 */
	public static void dismissProgressDialog(String status) {
		if(status.equals("userLogin")){
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}
	}
	
}
