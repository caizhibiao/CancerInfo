package com.graduate.infocollect.activity;

import com.graduate.cancerinfocollect.R;
import com.graduate.infocollect.db.DBHelper;
import com.graduate.infocollect.view.LineChartView;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

/**
 * @包名：com.graduate.infocollect.activity
 * @类名：ChartActivity
 * @描述：chart界面
 * @作者：cmcc
 * @时间：May 7, 20159:40:01 PM
 * @版本：1.0.0
 * 
*/
public class ChartActivity extends BaseActivity {
	// private ZoomControls mZoomControls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater factory = LayoutInflater.from(this);
		View content = (View)factory.inflate(R.layout.activity_chart, null);
		
		// 缩放控件放置在FrameLayout的上层，用于放大缩小图表
		FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		
		// 图表显示范围在占屏幕大小的90%的区域内
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scrWidth = (int)(dm.widthPixels * 0.95);
		int scrHeight = (int)(dm.heightPixels * 0.90);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(scrWidth, scrHeight);
		
		// 居中显示
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// 图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
		final RelativeLayout chartLayout = new RelativeLayout(this);
		
		chartLayout.addView(new LineChartView(this,DBHelper.getInstance().getMedicalList(getIntent().getStringExtra("id"))), layoutParams);
		
		// 增加控件
		((ViewGroup)content).addView(chartLayout);
		setContentView(content);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void onBack(View v) {
		finish();
	}
	
}
