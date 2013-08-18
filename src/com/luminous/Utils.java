package com.luminous;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * Class that will handle the functionality related to the user profile creation
 * 
 */
public class Utils {

	/**
	 * Set the service start up at midnight 12 and will start the service named
	 * TurnonService Repeating daily at 12 midnight
	 */

	public static String getmonth(int m) {
		switch (m) {
		case 0:
			return "JAN";
		case 1:
			return "FEB";
		case 2:
			return "MAR";
		case 3:
			return "APR";
		case 4:
			return "MAY";
		case 5:
			return "JUN";
		case 6:
			return "JULY";
		case 7:
			return "AUG";
		case 8:
			return "SEPT";
		case 9:
			return "OCT";
		case 10:
			return "NOV";
		case 11:
			return "DEC";
		}
		return null;
	}

	/**
	 * @return the String which will have the time in the format of hh:mm am/pm
	 * 
	 * @param : long time = The full time milliseconds
	 */
	public static String getTime(Long d) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(d);
		int hour = c.get(Calendar.HOUR);
		String ampm = "AM";
		if (c.get(Calendar.AM_PM) == Calendar.PM)
			ampm = "PM";
		String min = "" + c.get(Calendar.MINUTE);
		if (Integer.parseInt(min) < 10)
			min = "0" + min;

		String temp = " " + ((hour > 9) ? hour : ("0" + hour)) + ":" + min
				+ " " + ampm;
		return temp;

	}

	/**
	 * @return the String which will have the time in the format of hh:mm am/pm
	 * 
	 * @param : long time = The full time milliseconds
	 */
	public static String NewgetTime(Long d) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, getHourOfRepeates(d));
		c.set(Calendar.MINUTE, getMinOfRepeates(d));
		int hour = c.get(Calendar.HOUR);
		String ampm = "AM";
		if (c.get(Calendar.AM_PM) == Calendar.PM)
			ampm = "PM";
		String min = "" + c.get(Calendar.MINUTE);
		if (Integer.parseInt(min) < 10)
			min = "0" + min;

		String temp = " " + ((hour > 9) ? hour : ("0" + hour)) + " : " + min
				+ " " + ampm;
		return temp;

	}

	/*
	 * 
	 */
	public static String getdate(Long d) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(d);

		String day = "" + c.get(Calendar.DAY_OF_MONTH);// time.getDate();
		if (Integer.parseInt(day) < 10)
			day = "0" + day;

		String temp = " " + day + " " + getmonth(c.get(Calendar.MONTH)) + " "
				+ c.get(Calendar.YEAR);

		return temp;
	}

	/**
	 * @retrun time in milliseconds from the calendar
	 */
	public static long getTimeinLong(Calendar mCalendar) {
		return mCalendar.getTimeInMillis();
	}

	/**
	 * @return only the seconds of hour and minute from the calendar object
	 */
	public static long getOnlySeconds(Calendar mCalendar) {
		Debugger.debugE("timing in hour",
				"" + mCalendar.get(Calendar.HOUR_OF_DAY));
		int minute = (mCalendar.get(Calendar.HOUR_OF_DAY) * 60)
				+ (mCalendar.getTime().getMinutes());
		int sec = (minute * 60);
		Debugger.debugE("timing in sec", "" + sec);
		return sec * 1000;

	}

	/**
	 * @return only the minutes of hour and minute from the calendar object
	 */
	public static int getOnlyMinutes(Calendar mCalendar) {
		Debugger.debugE("timing in hour",
				"" + mCalendar.get(Calendar.HOUR_OF_DAY));
		int minute = (mCalendar.get(Calendar.HOUR_OF_DAY) * 60)
				+ (mCalendar.getTime().getMinutes());
		return minute;

	}

	/**
	 * Display the time in hour and minute format
	 * 
	 * @param : long time = only the hh:mm in milliseconds
	 */
	public static String DisplayRepeates(long d) {
		String Repeates = "";
		int sec = (int) d / 1000;
		int min = 0;
		int hour = 0;
		if (sec >= 60) {
			min = sec / 60;
		}
		if (min >= 60) {
			hour = min / 60;
		}

		if (hour > 0) {
			if (hour == 1) {
				Repeates = Repeates + hour + " hour ";
			} else {
				Repeates = Repeates + hour + " hours ";
			}
		} else {
			Repeates = Repeates + "";
		}

		if (min % 60 > 0) {
			if (min % 60 == 1) {
				Repeates = Repeates + (min % 60) + " min";
			} else {
				Repeates = Repeates + (min % 60) + " mins";
			}
		} else {
			Repeates = Repeates + "";
		}

		// Repeates = (hour > 0 ? ((min % 60) > 0 ? hour + " hour " : hour
		// + " hour ") : "")
		// + ((min % 60) > 0 ? (min % 60) + " min" : "");
		return Repeates;
	}

	public static String DisplayRepeatesShort(long d) {
		String Repeates = "";
		int sec = (int) d / 1000;
		int min = 0;
		int hour = 0;
		if (sec >= 60) {
			min = sec / 60;
		}
		if (min >= 60) {
			hour = min / 60;
		}
		Repeates = (hour > 0 ? ((min % 60) > 0 ? hour + " h " : hour + " h ")
				: "") + ((min % 60) > 0 ? (min % 60) + " m" : "");

		return Repeates;
	}

	/**
	 * @return hour of full in milliseconds
	 */
	public static int getHour(long d) {
		Date date = new Date(d);
		return date.getHours();
	}

	/**
	 * @return minute of full in milliseconds
	 */
	public static int getMinute(long d) {
		Date date = new Date(d);
		return date.getMinutes();
	}

	/**
	 * return minutes
	 * 
	 * @param : long time = only the hh:mm in milliseconds
	 */
	public static int getMinOfRepeates(long d) {
		Debugger.debugE("long", "" + d);
		int sec = (int) d / 1000;
		int min = 0;
		if (sec >= 60) {
			min = sec / 60;
		}
		return min % 60;
	}

	/**
	 * return minutes
	 * 
	 * @param : long time = only the hh:mm in milliseconds
	 */
	public static int getMinOfRepeates(long d, boolean flag) {
		// Debugger.debugE("long : " + d);
		int sec = (int) d / 1000;
		int min = 0;
		if (sec >= 60) {
			min = sec / 60;
		}
		return min;
	}

	/**
	 * return hour
	 * 
	 * @param : long time = only the hh:mm in milliseconds
	 */
	public static int getHourOfRepeates(long d) {
		/* Debugger.debugE("long", "" + d); */
		int sec = (int) d / 1000;
		int min = 0;
		int hour = 0;
		if (sec >= 60) {
			min = sec / 60;
		}
		if (min >= 60) {
			hour = min / 60;
		}
		return hour;
	}

	/**
	 * return string that contains days seperating with ","
	 * 
	 * @param : arraylist of days
	 */
	public static String getDaysString(ArrayList<String> list) {
		String list_str = list.toString();
		list_str = list_str.replace("[", "");
		list_str = list_str.replace("]", "");
		list_str.trim();
		return list_str;
	}

	public static int fetchDay(String Days) {
		if (Days.length() > 0) {
			return Integer.parseInt(Days.substring(0, 1));
		}
		return -1;
	}

	/**
	 * @return boolean: if the current profile and old profile is overlapped
	 * 
	 * @param current
	 *            : Reminder object newly created
	 * 
	 * @param old
	 *            : Reminder object ot be comparred
	 * 
	 *            Deciding overlapping... rules for overlapping First check if
	 *            the start itme is same .... if endtime is not there
	 * 
	 *            if endtime is same with some start time if endtime is between
	 *            the one reminder or else start time is between some reminder
	 */

	/**
	 * return hour
	 * 
	 * @param : long time = only the hh:mm in milliseconds
	 */
	public static String getIntervalInHHMM(long d) {
		/* Debugger.debugE("long", "" + d); */
		int sec = (int) d / 1000;
		int min = 0;
		int hour = 0;
		if (sec >= 60) {
			min = sec / 60;
		}
		if (min >= 60) {
			hour = min / 60;
		}
		String temp = "";
		if (hour == 0) {
			if (min == 1) {
				temp = temp + min + " min";
			} else {
				temp = temp + min + " mins";
			}
			// temp = ((min % 60) >= 10 ? (min % 60) : "0" + (min % 60)) +
			// "min";
		} else {
			temp = "" + ((hour > 9) ? hour : ("0" + hour)) + "h : "
					+ ((min % 60) >= 10 ? (min % 60) : "0" + (min % 60)) + "m";
		}
		return temp;
	}

	public static String getIntervalInHHMMFromMin(long d) {
		/* Debugger.debugE("long", "" + d); */

		int min = (int) d;
		int hour = 0;

		if (min >= 60) {
			hour = min / 60;
		}
		String temp = "";
		if (hour == 0) {
			if (min == 1) {
				temp = temp + min + "m";
			} else {
				temp = temp + min + "m";
			}
			// temp = ((min % 60) >= 10 ? (min % 60) : "0" + (min % 60)) +
			// "min";
		} else {
			temp = "" + ((hour > 9) ? hour : ("0" + hour)) + "h:"
					+ ((min % 60) >= 10 ? (min % 60) : "0" + (min % 60)) + "m";
		}
		return temp;
	}
}
