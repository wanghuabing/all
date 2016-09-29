package com.zongyu.elderlycommunity.business;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.zongyu.elderlycommunity.utils.Constans;
import com.zongyu.elderlycommunity.utils.parser.UserLoginParser;
import com.zongyu.elderlycommunity.utils.parser.UserRegistCheckCodeParser;
import com.zongyu.elderlycommunity.utils.parser.UserRegistCheckPhoneParser;
import com.zongyu.elderlycommunity.utils.volley.RequestUtils;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @author 作者：jun
 * @date 创建时间：2016-1-15 下午6:06:33
 * @Description 类描述：用户注册-添加信息
 */
public class UserRegistSaveInfoBusiness {

	public interface GetRegistSaveInfoCallback {
		public void afterDataGet(HashMap<String, Object> dataMap);
	}

	private GetRegistSaveInfoCallback mGetDataCallback;
	HashMap<String, String> mhashmap;
	Context mContext;

	public UserRegistSaveInfoBusiness(Context mContext,
			HashMap<String, String> mhashmap, GetRegistSaveInfoCallback mGetDataCallback) {
		this.mGetDataCallback = mGetDataCallback;
		this.mhashmap = mhashmap;
		this.mContext = mContext;
		getDate();
	}

	private void getDate() {
		String path = Constans.REGISTSETEDIT;
		StringRequest mJsonObjectRequest = new StringRequest(Method.POST, path,
				new Listener<String>() {
					public void onResponse(String response) {
						Log.e("JsonObjectRequest----", response);
						HashMap<String, Object> dataMap = new HashMap<String, Object>();
						UserLoginParser mParser = new UserLoginParser();
						JSONObject jsonObject = RequestUtils
								.String2JSON(response);
						dataMap = mParser.parseJSON(jsonObject);

						mGetDataCallback.afterDataGet(dataMap);
						mParser = null;
						jsonObject = null;
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						mGetDataCallback.afterDataGet(null);
					}
				}) {
			@Override
			protected HashMap<String, String> getParams() {
				return mhashmap;
			}
		};
		RequestUtils.addRequest(mJsonObjectRequest, mContext);
	}
}
