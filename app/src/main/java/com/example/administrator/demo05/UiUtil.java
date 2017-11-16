package com.example.administrator.demo05;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 界面相关方法静态封装
 * @author All
 *
 */
public class UiUtil {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 简化代码Toast
     * @param string
     */
    public static void toast(String string){
        Toast.makeText(APP.getApp(), string, Toast.LENGTH_SHORT).show();
    }

	public static int dpToPx(int dp, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}

	public static int spToPx(int sp,Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
	}

	/**
	 * 在TextView设置文本时做安全检查
	 * @param str
	 * @return
     */
	public static String getUnNullVal(String str){
		if(str == null){
			return "";
		}
		return str;
	}

	public static void optionSoftInput(Activity context, boolean hide) {
		if(context == null || context.getCurrentFocus() == null){
			return;
		}
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
		if(hide){
			imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
		}else{
			//显示软键盘
			imm.showSoftInputFromInputMethod(context.getCurrentFocus().getWindowToken(), 0);
		}
	}


	/**
	 * 获取当前应用的版本名称
	 * @return
	 */
	public static String getVersionName(){
		try {
			Class<?> cls = Class.forName("com.bankhui.platform.tx.BuildConfig");
			String VERSION_NAME = (String) cls.getDeclaredField("VERSION_NAME").get(null);
			return VERSION_NAME;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}



	/**
	 * 验证单一类型密码  123456   qwert
	 * @param password String
	 * @return int  < 2 是单一类型
     */
	public static int checkPwdStrong(String password){
		int Modes = 0;
		for(int i=0;i<password.length();i++){
			Modes |= charMode(password.charAt(i));//返回密码的Unicode编码
		}
		return bitTotal(Modes);
	}

	/**
	 * 验证单一类型密码
     */
	public static int bitTotal(int num){
		int modes = 0;
		for(int i=0; i<4;i++){
			if((num & 1) > 0) modes++;
			num >>>=1;
		}
		return modes;
	}

	/**
	 * 验证单一类型密码
     */
	public static int charMode(char unicode){
		if(unicode >= 48 && unicode <= 57){//数字
			return 1;
		}else if(unicode >= 65 && unicode <= 90){//大写字母
			return 2;
		}else if(unicode >=97 && unicode <= 122){//小写字母
			return 4;
		}else{
			return 8;
		}
	}


	/**
	 * 获取状态栏的高
	 * @param context
	 * @return
     */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0) {
			result = context.getResources().getDimensionPixelOffset(resId);
		}
		return result;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		/**
		 * getAdapter这个方法主要是为了获取到ListView的数据条数，所以设置之前必须设置Adapter
		 */
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {

			View listItem = listAdapter.getView(i, null, listView);
			//计算每一项的高度
			listItem.measure(0, 0);
			//总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		//真正的高度需要加上分割线的高度
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


	public static String getStrListString(List<String> list){
		StringBuffer sb = new StringBuffer();
		for(String s : list){
			sb.append(s).append(",");
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	/**
	 * 获得屏幕宽度，单位px
	 *
	 * @param context 上下文
	 * @return 屏幕宽度
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
	/**
	 * 获得高度宽度，单位px
	 *
	 * @param context 上下文
	 * @return 屏幕宽度
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	/*
	*
	* */
	public static int pxToDp(Context context, int px) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

	/**
	 * 保险订单状态
	 * @param status int
	 * @return String
     */
	public static String getInsuranceOrderStatus(int status){
		if(status == -1){
			return "购买失败";
		}else if(status == 0){
			return "待支付";
		}else if(status == 1){
			return "已支付";
		}else if(status == 2){
			return "已关闭";
		}else if(status == 3){
			return "退保";
		}
		return "";
	}

	public static void setCircleImg(final Context context, ImageView view, String url){
//		//  圆形图片
//		Glide.with(context).load(url).asBitmap().placeholder(R.mipmap.my_headimg).error(R.mipmap.my_headimg).centerCrop().into(new BitmapImageViewTarget(view) {
//			@Override
//			protected void setResource(Bitmap resource) {
//				RoundedBitmapDrawable circularBitmapDrawable =
//						RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//				circularBitmapDrawable.setCircular(true);
//				view.setImageDrawable(circularBitmapDrawable);
//			}
//		});
	}

}
