package com.hand.handtruck.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.ui.home.MainFragmentActivity;
import com.hand.handtruck.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.Logger;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			//			DLog.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				DLog.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				DLog.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				DLog.e(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
				String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
				String title=bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
				DLog.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId+"/通知内容:"+alert+"/附加参数:"+extra+"/通知标题:"+title);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				DLog.e(TAG, "[MyReceiver] 用户点击打开了通知");

				SharedPreferences sp = context.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
				String token = sp.getString("token", "");//判断是否登录

				if(!Tools.isEmpty(token)){
					Bundle extras = new Bundle();
					extras.putInt("tab_num",1);
					Intent i = new Intent(context, MainFragmentActivity.class);
					i.putExtras(extras);
					context.startActivity(i);
				}else{
					LoginActivity.start(context);//登录页面
				}
				//				//打开自定义的Activity
				//				Intent i = new Intent(context, TestActivity.class);
				//				i.putExtras(bundle);
				//				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				//				context.startActivity(i);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				DLog.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				DLog.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.get(key));
			}
		}
		return sb.toString();
	}

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		//		if (MainActivity.isForeground) {
		//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
		//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
		//			if (!ExampleUtil.isEmpty(extras)) {
		//				try {
		//					JSONObject extraJson = new JSONObject(extras);
		//					if (extraJson.length() > 0) {
		//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
		//					}
		//				} catch (JSONException e) {
		//
		//				}
		//
		//			}
		//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		//		}
	}
}
