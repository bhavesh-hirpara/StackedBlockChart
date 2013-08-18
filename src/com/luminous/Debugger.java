package com.luminous;

import android.util.Log;

public class Debugger {

	public static String TAG = "Moodlytics";

	public static void debugE(String Text) {

		Log.e(TAG, Text);
	}

	public static void debugI(String Text) {

		Log.i(TAG, Text);
	}

	public static void debugE(String tag, String Text) {

		Log.e(TAG, tag + " " + Text);
	}

}
