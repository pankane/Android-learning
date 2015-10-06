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
			day = "����";
			break;
		case 2:
			day = "��һ";
			break;
		case 3:
			day = "�ܶ�";
			break;
		case 4:
			day = "����";
			break;
		case 5:
			day = "����";
			break;
		case 6:
			day = "����";
			break;
		case 7:
			day = "����";
			break;

		}

		return day;

	}
	
	public String getWeekByID(int id){
		String day=null;
		
		switch (id) {
		case 1:
			day = "һ";
			break;
		case 2:
			day = "��";
			break;
		case 3:
			day = "��";
			break;
		case 4:
			day = "��";
			break;
		case 5:
			day = "��";
			break;
		case 6:
			day = "��";
			break;
		case 7:
			day = "��";

			break;

		default:
			break;
			
		}
		return day;
		
	}
	

	public String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		
		return str;

	}

	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		
		return str.substring(11, 19);
	
		
	}


}
