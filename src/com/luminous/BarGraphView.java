package com.luminous;

/** 
 * @author      Kundan Singh Thakur 
 * @Site	www.myprotocol.in
 * @Description	The BarGraphView class implements all variation of Bar Graph, like 
 * 		Simple Bar Graph, Stacked Bar Graph and Grouped Bar Graph.
 * 
 * @param style:0- Simple bar graph
 * 		1- Stacked bar graph
 * 		2- Grouped bar graph
 * 
 * @param grouping - not applicable for simple bar grph
 * 			
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class BarGraphView extends View {
	private ArrayList<Long> data = new ArrayList<Long>();

	private int graphWidth = 200; // Default Width of graph
	private int graphHeight = 200; // Default graph height
	private int graphOriginX = 50; // Default Origin coordinates X and Y
	private int graphOriginY = 30;
	private String yAxisLabel = "sample"; // Default label of Y axis
	private ArrayList<String> barLabels = new ArrayList<String>();
	Calendar cal;
	Calendar cal_tmp_ref;

	private int lableXsize = 15;
	private int lableYsize = 15;

	private boolean isValueDrawOnBlock = false;
	private boolean isDrawGrid = true;

	private Context mContext;

	private ArrayList<ArrayList<BarBlock>> chartdata = new ArrayList<ArrayList<BarBlock>>();

	public BarGraphView(Context context) {
		super(context);
		mContext = context;

		loadSampleData();

		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		cal_tmp_ref = (Calendar) cal.clone();

		generateBarLables();
		init();

	}

	public BarGraphView(Context context, ArrayList<Long> _data,
			int _graphWidth, int _graphHeight, int _graphOriginX,
			int _graphOriginY, String _yAxisLabel, String _barLabels[]) {
		super(context);
		mContext = context;

		data.addAll(_data);
		graphWidth = _graphWidth;
		graphHeight = _graphHeight;
		graphOriginX = _graphOriginX;
		graphOriginY = _graphOriginY;
		yAxisLabel = _yAxisLabel;
	}

	private void init() {
		sortByMoodTime();
		resetData();
	}

	@SuppressWarnings("deprecation")
	private void adjustAccordingToResolution() {
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int width = display.getWidth();
		int height = display.getHeight();

		int orientation = display.getOrientation();
		Debugger.debugE("orientation : " + orientation);

		DisplayMetrics matrix = getResources().getDisplayMetrics();

		if (matrix.densityDpi == DisplayMetrics.DENSITY_HIGH) {

			int perX = 10;
			int perY = 5;

			if (orientation == 0) {
				lableXsize = 15;
				lableYsize = 15;

			} else if (orientation == 1) {
				perX = 8;

				lableXsize = 17;
				lableYsize = 16;

			}

			graphOriginX = (width * perX) / 100;
			graphOriginY = (height * perY) / 100;

		} else if (matrix.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {

			int perX = 10;
			int perY = 7;

			if (orientation == 0) {

				lableXsize = 10;
				lableYsize = 8;
			} else if (orientation == 1) {

				lableXsize = 15;
				lableYsize = 13;
			}

			graphOriginX = (width * perX) / 100;
			graphOriginY = (height * perY) / 100;

		} else if (matrix.densityDpi == DisplayMetrics.DENSITY_LOW) {

			int perX = 10;
			int perY = 7;

			if (orientation == 0) {

				lableXsize = 8;
				lableYsize = 6;

			} else if (orientation == 1) {

				lableXsize = 10;
				lableYsize = 8;
			}

			graphOriginX = (width * perX) / 100;
			graphOriginY = (height * perY) / 100;

		}

	}

	private void resetReferenceCal() {
		cal_tmp_ref.setTimeInMillis(cal.getTimeInMillis());
	}

	@Override
	public void onDraw(Canvas canvas) {
		resetReferenceCal();
		drawCanvas(canvas);
	}

	private void drawCanvas(Canvas canvas) {

		try {
			adjustAccordingToResolution();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {

			Paint paint = new Paint();

			if (graphHeight < 200)
				paint.setTextSize(10);
			else
				paint.setTextSize(20);

			int _width = (graphOriginX + graphWidth); // set the right hand
														// extent
														// of graph
			if (getWidth() - _width < 10) {
				_width = getWidth() - 10;
			}

			int _height = (int) (getHeight() - graphOriginY - graphHeight);// set
																			// the
																			// top
																			// extent
																			// of
																			// graph
			if (_height < 20) {
				_height = 20;
			}
			int originX = graphOriginX; // set the left hand extent
			int originY = getHeight() - graphOriginY; // set the bottom extent

			paint.setColor(Color.BLACK);
			float padding;

			padding = (float) (((float) (_width - originX) / (float) ((float) (chartdata
					.size()))) * 0.20);

			// Drawing Y Axis
			drawYAxis(canvas, originX, _height, originY, paint);

			// Draw grid parallel to X-Axis
			int maxVal = ((int) (getMaximum() / 10) + 1) * 10;
			Debugger.debugE("maxVal : " + maxVal);
			paint.setColor(Color.BLACK);

			if (maxVal < 60) {
				maxVal = 60;
			}

			float ratio = (float) ((float) (originY - _height) / (float) maxVal); // i
																					// value
																					// is
																					// equal
																					// to
																					// this
																					// much
																					// height
			float scale = (float) maxVal * ratio / 10;

			drawYlables(canvas, maxVal, scale, ratio, originX, originY, _width,
					paint);

			// Draw Custom Mood Bars
			drawBar(canvas, padding, originX, originY, _height, ratio, paint);

			// Drawing X Axis
			drawXAxis(canvas, originX, _width, originY, paint);

			// Drawing Y-axis label
			// drawYAxisTitle(canvas, maxVal, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void drawBar(Canvas canvas, float padding, int originX,
			int originY, int _height, float ratio, Paint paint) {

		paint.setTextAlign(Align.CENTER);

		int tempX = originX + (int) padding;
		int heightBar;
		int j = 0;
		// Draw Bars

		// int temp = 0;

		boolean isNewBar = true;

		for (int barIndex = 0; barIndex < chartdata.size(); barIndex++) {

			ArrayList<BarBlock> singleBar = chartdata.get(barIndex);

			for (int barBlockIndex = 0; barBlockIndex < singleBar.size(); barBlockIndex++) {
				if (barIndex > 0 && isNewBar) {
					tempX = (int) (tempX + padding * 5);
					// temp = 0;
				}
				// paint.setColor(color[(i % grouping) % color.length]);
				paint.setColor(singleBar.get(barBlockIndex).color);

				// if (isNewBar) {
				// heightBar = (int) ((originY) - (singleBar
				// .get(barBlockIndex).mood_time * ratio));
				// } else {
				// heightBar = (int) ((temp) -
				// (singleBar.get(barBlockIndex).mood_time * ratio));
				// }

				heightBar = (int) ((originY) - (singleBar.get(barBlockIndex).time * ratio));

				// Rect bar = new Rect(tempX, heightBar,
				// (int) (tempX + padding * 4), (temp == 0) ? originY
				// : temp);

				int feel_time = (int) (singleBar.get(barBlockIndex).interval * ratio);

				Rect bar = null;
				if (singleBar.get(barBlockIndex).interval == -1
						&& singleBar.get(barBlockIndex).time == -1) {

					bar = new Rect(tempX, _height, (int) (tempX + padding * 4),
							(int) (originY));

				} else {
					bar = new Rect(tempX, heightBar,
							(int) (tempX + padding * 4),
							(int) (heightBar + feel_time));
				}

				if (bar.bottom > originY) {
					bar.bottom = originY;
				}

				printRect(bar, ratio);

				// temp = heightBar;
				canvas.drawRect(bar, paint);
				paint.setColor(Color.BLACK);

				float y = heightBar + 20;

				// draw value on bar block
				if (y <= originY) {
					if (isValueDrawOnBlock) {
						canvas.drawText(""
								+ singleBar.get(barBlockIndex).interval, tempX
								+ padding * 2, y, paint);
					}
				}

				if (isNewBar) {
					drawXLables(canvas, barLabels.get(j++), padding, tempX,
							originY, paint);
				}

				isNewBar = false;
			}

			isNewBar = true;

		}
	}

	private void drawXAxis(Canvas canvas, int originX, int _width, int originY,
			Paint paint) {

		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		Rect xAxis = new Rect(originX, originY - 2, _width, originY);
		canvas.drawRect(xAxis, paint);

	}

	private void drawXLables(Canvas canvas, String title, float padding,
			int tempX, int originY, Paint paint) {

		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(lableXsize);
		canvas.drawText(title, (float) (tempX + 2 * padding), originY + 15,
				paint);
	}

	private void drawYAxisTitle(Canvas canvas, int maxVal, Paint paint) {
		Rect rect = new Rect();
		paint.getTextBounds(yAxisLabel, 0, yAxisLabel.length(), rect);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);

		paint.setStyle(Paint.Style.FILL);
		if (maxVal < 10) {
			canvas.rotate(-90, graphOriginX - 25, getHeight() - graphOriginY
					- graphHeight / 2); // rotating the text
			canvas.drawText(yAxisLabel, graphOriginX - 20, getHeight()
					- graphOriginY - graphHeight / 2, paint);
		} else if (maxVal < 100) {
			canvas.rotate(-90, graphOriginX - 35, getHeight() - graphOriginY
					- graphHeight / 2); // rotating the text
			canvas.drawText(yAxisLabel, graphOriginX - 30, getHeight()
					- graphOriginY - graphHeight / 2, paint);
		} else {
			canvas.rotate(-90, graphOriginX - 40, getHeight() - graphOriginY
					- graphHeight / 2); // rotating the text
			canvas.drawText(yAxisLabel, graphOriginX - 40, getHeight()
					- graphOriginY - graphHeight / 2, paint);
		}
	}

	private void drawYAxis(Canvas canvas, int originX, int _height,
			int originY, Paint paint) {

		paint.setAntiAlias(true);
		Rect yAxis = new Rect(originX, (int) (_height), originX + 2, originY);
		canvas.drawRect(yAxis, paint);
	}

	private void drawYlables(Canvas canvas, int maxVal, float scale,
			float ratio, int originX, int originY, int _width, Paint paint) {

		paint.setAntiAlias(true);
		paint.setTextSize(lableYsize);
		paint.setTextAlign(Align.RIGHT);

		int gap = 60;

		// if (maxVal < gap) {
		// maxVal = gap;
		// }

		int range = maxVal / gap;

		boolean isNoLables = false;

		if (range == 0) {
			range = 1;
			isNoLables = false;
		}

		Debugger.debugE("scale : " + scale + " Total lables : " + range);

		for (int i = 0; i <= range; i++) {
			paint.setColor(Color.BLACK);

			// canvas.drawText((maxVal * i / 10) + "", originX - 5, originY
			// - (scale * i) + 5, paint);

			int top = (int) ((originY) - (gap * i * ratio));
			top = Math.abs(top);

			Debugger.debugE("top : " + top + " ratio : " + ratio);

			boolean add = true;
			if (i == 0) {
				add = false;
			}

			if (isNoLables) {
				canvas.drawText(formatTime(gap, add), originX - 5, originY
						- (scale * i) + 5, paint);

				Rect grid = new Rect(originX,
						(int) (originY - 1 - (scale * i)), _width,
						(int) (originY - (scale * i)));

				if (isDrawGrid && i <= range) {
					drawGrid(canvas, grid, paint);
				}
			} else {
				canvas.drawText(formatTime(gap, add), originX - 5, top + 5,
						paint);
				Rect grid = new Rect(originX, top - 1, _width, top);

				if (isDrawGrid && i <= range) {
					drawGrid(canvas, grid, paint);
				}
			}

		}
	}

	private void drawGrid(Canvas canvas, Rect grid, Paint paint) {

		paint.setColor(Color.LTGRAY);
		canvas.drawRect(grid, paint);
	}

	public Calendar getCalendar() {
		return cal;
	}

	public void genBarLables(Date d) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(d);

		barLabels.clear();

		for (int i = 0; i < chartdata.size(); i++) {
			barLabels.add(formatDate(cal.getTimeInMillis(), "dd MMM"));
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

	}

	private String formatTime(int mins, boolean add) {
		if (add) {
			cal_tmp_ref.add(Calendar.MINUTE, mins);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("hh a");
		return sdf.format(cal_tmp_ref.getTime());
	}

	// private void drawYlables(Canvas canvas, int maxVal, float scale,
	// int originX, int originY, int _width, Paint paint) {
	//
	// paint.setAntiAlias(true);
	// paint.setTextSize(15);
	//
	// for (int i = 1; i <= 10; i++) {
	// paint.setColor(Color.BLACK);
	//
	// if (maxVal < 10)
	// canvas.drawText((maxVal * i / 10) + "", originX - 15, originY
	// - (scale * i) + 5, paint);
	// else if (maxVal < 100)
	// canvas.drawText((maxVal * i / 10) + "", originX - 25, originY
	// - (scale * i) + 5, paint);
	// else
	// canvas.drawText((maxVal * i / 10) + "", originX - 35, originY
	// - (scale * i) + 5, paint);
	// Rect grid = new Rect(originX, (int) (originY - 1 - (scale * i)),
	// _width, (int) (originY - (scale * i)));
	// if (i < 10) {
	// paint.setColor(Color.LTGRAY);
	// canvas.drawRect(grid, paint);
	// }
	// }
	// }

	private void sortByMoodTime() {
		for (ArrayList<BarBlock> singleBar : chartdata) {
			Collections.sort(singleBar, new Comparator<BarBlock>() {

				@Override
				public int compare(BarBlock object1, BarBlock object2) {

					if (object1.time < object2.time) {
						return 1;
					} else if (object1.time > object2.time) {
						return -1;
					} else {
						return 0;
					}

				}
			});
		}

		BarBlock mood = new BarBlock();
		mood.time = -1;
		mood.interval = -1;
		mood.color = Color.parseColor("#F2F2F2");

		for (int i = 0; i < chartdata.size(); i++) {
			chartdata.get(i).add(0, mood);
		}
	}

	private void printRect(Rect rect, float ratio) {
		// Log.e("Bar Rect", "left : " + rect.left + " top : " + rect.top
		// + " right : " + rect.right + " bottom : " + rect.bottom
		// + " ratio : " + ratio);
	}

	public long getMaximum() {

		long max = 0;

		for (ArrayList<BarBlock> singleBar : chartdata) {

			long temp = 0;
			for (int i = 0; i < singleBar.size(); i++) {
				temp = singleBar.get(i).time;

				if (max < temp) {
					max = temp;
				}
			}

		}

		return max;
	}

	private void resetData() {
		data.clear();

		for (ArrayList<BarBlock> singleBar : chartdata) {

			for (BarBlock singleBarBlock : singleBar) {
				data.add(singleBarBlock.time);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		Debugger.debugE("left : " + left + " top : " + top + " right : "
				+ right + " bottom : " + bottom);

		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		graphWidth = MeasureSpec.getSize(widthMeasureSpec);
		graphHeight = MeasureSpec.getSize(heightMeasureSpec);

		Debugger.debugE("onMeasure() is called.." + graphWidth + " : "
				+ graphHeight);

	}

	private void generateBarLables() {
		barLabels.clear();

		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			barLabels.add("" + cal.get(Calendar.DAY_OF_MONTH) + " JUN");

			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		invalidate();
	}

	public void addAll(ArrayList<ArrayList<BarBlock>> chartdata) {

		Calendar cal_ref = getCalendar();

		for (int i = 0; i < chartdata.size(); i++) {

			for (int j = 0; j < chartdata.get(i).size(); j++) {

				BarBlock mood_block = chartdata.get(i).get(j);

				Calendar cal_tmp = Calendar.getInstance();
				cal_tmp.setTimeInMillis(mood_block.time);
				cal_tmp.set(Calendar.HOUR_OF_DAY,
						cal_ref.get(Calendar.HOUR_OF_DAY));
				cal_tmp.set(Calendar.MINUTE, 0);
				cal_tmp.set(Calendar.SECOND, 0);

				chartdata.get(i).get(j).time = Utils
						.getMinOfRepeates(
								mood_block.time
										- cal_tmp.getTimeInMillis(), true);

				chartdata.get(i).get(j).interval = Utils
						.getMinOfRepeates(mood_block.interval, true);

				Debugger.debugE("mood_time : " + mood_block.time
						+ " feel_time : " + mood_block.interval);
			}

		}

		Debugger.debugE("chartdata.size : " + chartdata.size());

		this.chartdata.clear();
		this.chartdata.addAll(chartdata);

		init();

		invalidate();
	}

	public void addAll(ArrayList<ArrayList<BarBlock>> chartdata, Date d) {
		refreshStartYLable(chartdata);
		genBarLables(d);
		addAll(chartdata);
	}

	private void refreshStartYLable(ArrayList<ArrayList<BarBlock>> chartdata) {

		int hour_min = 24;

		for (ArrayList<BarBlock> singleBar : chartdata) {

			for (BarBlock singleBarBlock : singleBar) {

				if (singleBarBlock.time > 0
						&& singleBarBlock.interval > 0) {

					Calendar cal_tmp = Calendar.getInstance();
					cal_tmp.setTimeInMillis(singleBarBlock.time
							- singleBarBlock.interval);

					Calendar tmp = Calendar.getInstance();
					tmp.setTimeInMillis(singleBarBlock.time);

					if (tmp.get(Calendar.DAY_OF_MONTH) != cal_tmp
							.get(Calendar.DAY_OF_MONTH)) {

						cal_tmp.set(Calendar.HOUR_OF_DAY, 0);
					}

					Debugger.debugE("hour_min : " + hour_min + " mood_time : "
							+ formatDate(singleBarBlock.time, "hh a"));

					if (cal_tmp.get(Calendar.HOUR_OF_DAY) < hour_min) {
						hour_min = cal_tmp.get(Calendar.HOUR_OF_DAY);
					}
				} else {
					Debugger.debugE("mood_time or feel_time is 0");
				}
			}
		}

		cal.set(Calendar.HOUR_OF_DAY, hour_min);

		Debugger.debugE("starting hour : "
				+ formatDate(cal.getTimeInMillis(), "hh a"));
	}

	public static String formatDate(long timeStamp, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(timeStamp));
	}

	private void loadSampleData() {
		ArrayList<BarBlock> singleBar = new ArrayList<BarBlock>();
		BarBlock mood_1 = new BarBlock();
		mood_1.time = 60;
		mood_1.interval = 60;
		mood_1.color = Color.RED;
		singleBar.add(mood_1);

		BarBlock mood_2 = new BarBlock();
		mood_2.time = 120;
		mood_2.interval = 60;
		mood_2.color = Color.GREEN;
		singleBar.add(mood_2);

		BarBlock mood_3 = new BarBlock();
		mood_3.time = 180;
		mood_3.interval = 60;
		mood_3.color = Color.MAGENTA;
		singleBar.add(mood_3);

		mood_3 = new BarBlock();
		mood_3.time = 720;
		mood_3.interval = 210;
		mood_3.color = Color.MAGENTA;
		singleBar.add(mood_3);

		chartdata.add(singleBar);

		singleBar = new ArrayList<BarBlock>();

		BarBlock mood = new BarBlock();
		mood.time = 800;
		mood.interval = 20;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 250;
		mood.interval = 40;
		mood.color = Color.BLACK;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 400;
		mood.interval = 50;
		mood.color = Color.CYAN;
		singleBar.add(mood);

		mood_3 = new BarBlock();
		mood_3.time = 600;
		mood_3.interval = 29;
		mood_3.color = Color.GREEN;
		singleBar.add(mood_3);

		chartdata.add(singleBar);

		singleBar = new ArrayList<BarBlock>();

		mood = new BarBlock();
		mood.time = 200;
		mood.interval = 20;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 250;
		mood.interval = 40;
		mood.color = Color.BLACK;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 400;
		mood.interval = 50;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		chartdata.add(singleBar);

		singleBar = new ArrayList<BarBlock>();

		mood = new BarBlock();
		mood.time = 200;
		mood.interval = 20;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 600;
		mood.interval = 10;
		mood.color = Color.RED;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 400;
		mood.interval = 50;
		mood.color = Color.BLUE;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 200;
		mood.interval = 20;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 600;
		mood.interval = 10;
		mood.color = Color.BLACK;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 400;
		mood.interval = 50;
		mood.color = Color.RED;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 200;
		mood.interval = 20;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 600;
		mood.interval = 10;
		mood.color = Color.BLACK;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 400;
		mood.interval = 50;
		mood.color = Color.BLUE;
		singleBar.add(mood);

		chartdata.add(singleBar);

		singleBar = new ArrayList<BarBlock>();

		mood = new BarBlock();
		mood.time = 251;
		mood.interval = 134;
		mood.color = Color.GREEN;
		singleBar.add(mood);

		chartdata.add(singleBar);
		singleBar = new ArrayList<BarBlock>();

		mood = new BarBlock();
		mood.time = 142;
		mood.interval = 20;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 250;
		mood.interval = 40;
		mood.color = Color.BLACK;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 254;
		mood.interval = 50;
		mood.color = Color.GREEN;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 400;
		mood.interval = 60;
		mood.color = Color.MAGENTA;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 400;
		mood.interval = 50;
		mood.color = Color.BLUE;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 800;
		mood.interval = 165;
		mood.color = Color.YELLOW;
		singleBar.add(mood);

		chartdata.add(singleBar);
		singleBar = new ArrayList<BarBlock>();

		mood = new BarBlock();
		mood.time = 300;
		mood.interval = 40;
		mood.color = Color.BLACK;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 100;
		mood.interval = 50;
		mood.color = Color.BLUE;
		singleBar.add(mood);

		mood = new BarBlock();
		mood.time = 700;
		mood.interval = 210;
		mood.color = Color.DKGRAY;
		singleBar.add(mood);

		chartdata.add(singleBar);
	}
}