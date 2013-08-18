package com.luminous;

/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.stackedblockchart.R;

public class ChartDemo extends Activity {

	final String TAG = getClass().getName();
	LinearLayout lchart;
	BarGraphView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		lchart = (LinearLayout) findViewById(R.id.chart);

		try {
			// SalesStackedBarChart mChart = new SalesStackedBarChart();
			//
			// GraphicalView v = mChart.getChartView(this);

			view = new BarGraphView(this);

			lchart.addView(view);
			// view.setScaleChangeListener(this);
			// /lchart.addView(v);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		zoom = (LinearLayout) findViewById(R.id.zoom);
		zoom.setVisibility(View.GONE);
		zoom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View paramView) {
				zoom.setVisibility(View.GONE);
				// view.fitToWindow();
			}
		});
	}

	LinearLayout zoom;

	// @Override
	// public void onChange(float mScaleFactor, float mStaticScaleFactor,
	// boolean isTransalation) {
	// if (mScaleFactor == mStaticScaleFactor) {
	// zoom.setVisibility(View.GONE);
	// } else {
	// zoom.setVisibility(View.VISIBLE);
	// }
	//
	// }

}