package com.kane.memo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetCurrentDate {

	public GetCurrentDate() {
		
	}
	
	public String getWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));

		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String day = null;
		switch (dayOfWeek) {

		case 1:
			day = "周日";
			break;
		case 2:
			day = "周一";
			break;
		case 3:
			day = "周二";
			break;
		case 4:
			day = "周三";
			break;
		case 5:
			day = "周四";
			break;
		case 6:
			day = "周五";
			break;
		case 7:
			day = "周六";
			break;

		}

		return day;

	}
	
	public String getWeekByID(int id){
		String day=null;
		
		switch (id) {
		case 1:
			day = "一";
			break;
		case 2:
			day = "二";
			break;
		case 3:
			day = "三";
			break;
		case 4:
			day = "四";
			break;
		case 5:
			day = "五";
			break;
		case 6:
			day = "六";
			break;
		case 7:
			day = "日";

			break;

		default:
			break;
			
		}
		return day;
		
	}
	

	public String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		
		return str;

	}

	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		
		return str.substring(11, 19);
	
		
	}


}
