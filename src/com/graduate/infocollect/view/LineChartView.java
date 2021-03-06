/**
 * Copyright 2014 XCL-Charts Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/ Apache v2 License
 * @version 1.0
 */
package com.graduate.infocollect.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.graduate.infocollect.entity.MedicalData;

public class LineChartView extends BaseChartView implements Runnable {
	
	private String TAG = "LineChartView";
	private LineChart chart = new LineChart();
	
	// 标签集合
	private LinkedList<String> labels = new LinkedList<String>();
	private LinkedList<LineData> chartData = new LinkedList<LineData>();
	private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();
	
	private List<MedicalData> mList = new ArrayList<MedicalData>();
	// 批注
	List<AnchorDataPoint> mAnchorSet = new ArrayList<AnchorDataPoint>();
	
	public LineChartView(Context context, List<MedicalData> mList) {
		super(context);
		// TODO Auto-generated constructor stub
		if(mList != null)
			this.mList = mList;
		initView();
	}
	
	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();
		new Thread(this).start();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		chart.setChartRange(w, h);
	}
	
	private void chartRender() {
		try {
			
			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(DensityUtil.dip2px(getContext(), 45), ltrb[1], ltrb[2], ltrb[3]);
			
			// 设定数据源
			chart.setCategories(labels);
			// chart.setDataSource(chartData);
			chart.setCustomLines(mCustomLineDataset);
			
			// 数据轴最大值
			chart.getDataAxis().setAxisMax(200);
			// 数据轴刻度间隔
			chart.getDataAxis().setAxisSteps(5);
			// 指隔多少个轴刻度(即细刻度)后为主刻度
			chart.getDataAxis().setDetailModeSteps(5);
			
			// 背景网格
			chart.getPlotGrid().showHorizontalLines();
			
			// 标题
			chart.setTitle("个人病历情况一览");
			// chart.addSubtitle("(XCL-Charts)");
			
			// 隐藏顶轴和右边轴
			// chart.hideTopAxis();
			// chart.hideRightAxis();
			
			// 设置轴风格
			
			// chart.getDataAxis().setTickMarksVisible(false);
			chart.getDataAxis().getAxisPaint().setStrokeWidth(2);
			chart.getDataAxis().getTickMarksPaint().setStrokeWidth(2);
			chart.getDataAxis().showAxisLabels();
			
			chart.getCategoryAxis().getAxisPaint().setStrokeWidth(2);
			chart.getCategoryAxis().hideTickMarks();
			
			// 定义数据轴标签显示格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
				
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					Double tmp = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(tmp).toString();
					return(label);
				}
				
			});
			
			// 定义线上交叉点标签显示格式
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(value).toString();
					return label;
				}
			});
			
			
			// 允许线与轴交叉时，线会断开
			chart.setLineAxisIntersectVisible(false);
			
			// chart.setDataSource(chartData);
			// 动态线
			chart.showDyLine();
			
			// 不封闭
			chart.setAxesClosed(false);
			
			// 扩展绘图区右边分割的范围，让定制线的说明文字能显示出来
			chart.getClipExt().setExtRight(150.f);
			
			// chart.getDataAxis().hide();
		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	
	private void chartDataSet() {
		// Line 1
		LinkedList<Double> dataSeries1 = new LinkedList<Double>();
		dataSeries1.add(0d);
		for(MedicalData md : mList) {
			dataSeries1.add(Double.valueOf(md.getPSA()));
		}
		LineData lineData1 = new LineData("血清PSA", dataSeries1, Color.rgb(234, 83, 71));
		lineData1.setDotStyle(XEnum.DotStyle.DOT);
		
		// Line 2
		LinkedList<Double> dataSeries2 = new LinkedList<Double>();
		dataSeries2.add(0d);
		for(MedicalData md : mList) {
			dataSeries2.add(Double.valueOf(md.getCA()));
		}
		
		LineData lineData2 = new LineData("CA 19-9", dataSeries2, Color.rgb(75, 166, 51));
		lineData2.setDotStyle(XEnum.DotStyle.PRISMATIC);
		
		// Line 3
		LinkedList<Double> dataSeries3 = new LinkedList<Double>();
		dataSeries3.add(0d);
		for(MedicalData md : mList) {
			dataSeries3.add(Double.valueOf(md.getAFP()));
		}
		LineData lineData3 = new LineData("AFP", dataSeries3, Color.rgb(123, 89, 168));
		lineData3.setDotStyle(XEnum.DotStyle.DOT);
		
		chartData.add(lineData1);
		chartData.add(lineData2);
		chartData.add(lineData3);
		
		chart.setAnchorDataPoint(mAnchorSet);
	}
	
	private void chartLabels() {
		int i = 0;
		labels.add(String.valueOf(0));
		for(MedicalData md : mList) {
			labels.add(String.valueOf(++i));
		}
		labels.add(String.valueOf(++i));
		
	}
	
	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		}
		catch(Exception e) {
			Log.e(TAG, e.toString());
		}
	}
	
	@Override
	public List<XChart> bindChart() {
		// TODO Auto-generated method stub
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);
		return lst;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			chartAnimation();
		}
		catch(Exception e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private void chartAnimation() {
		try {
			int count = chartData.size();
			for(int i = 0; i < count; i++) {
				Thread.sleep(150);
				LinkedList<LineData> animationData = new LinkedList<LineData>();
				for(int j = 0; j <= i; j++) {
					animationData.add(chartData.get(j));
				}
				
				// Log.e(TAG,"size = "+animationData.size());
				chart.setDataSource(animationData);
				if(i == count - 1) {
					chart.getDataAxis().show();
					chart.getDataAxis().showAxisLabels();
					
					chart.setCustomLines(mCustomLineDataset);
				}
				postInvalidate();
			}
		}
		catch(Exception e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		super.onTouchEvent(event);
		
		if(event.getAction() == MotionEvent.ACTION_UP) {
			// 交叉线
			if(chart.getDyLineVisible()) {
				chart.getDyLine().setCurrentXY(event.getX(), event.getY());
				if(chart.getDyLine().isInvalidate())
					this.invalidate();
			}
		}
		return true;
	}
	
}
