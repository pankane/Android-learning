package com.kane.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SpecialAdapter extends SimpleAdapter {
	 private Context context;
	
    
	private int[] colors = new int[] { 0x30FF0000, 0x300000FF };// 这里没有引用进去使用,只是简单引用数组运算

	public SpecialAdapter(Context context, List<? extends Map<String, ?>> data,

	int resource, String[] from, int[] to) {

		super(context, data, resource, from, to);
		 this.context = context;
     
       

        
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = super.getView(position, convertView, parent);
		
		setViewColor(position,view);
		

		return view;
	}
	
	
	
	
	/**
	 * set 2 colors for every other rows.
	 * @param position
	 * @param view
	 */
	
	public void setViewColor(int position,View view){
		
		int colorPos = position % colors.length;

		if (colorPos == 1)

			view.setBackgroundColor(Color.argb(30, 192, 192, 192)); // 颜色设置

		else

			view.setBackgroundColor(Color.argb(50, 192, 192, 192));// 颜色设置
		/**
		 * To get what date is today
		 */

		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		try {
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

			/**
			 * to get the day text in the dayText
			 */
			/**
			 * For today, give a highlighted color
			 */
			TextView dayText = (TextView) view.findViewById(R.id.day);

			if (dayText.getText() == day) {

				view.setBackgroundColor(Color.argb(50, 192, 192, 192));

			}

		} catch (Exception e) {

		}
		
		
	}
	
	
	
	
	
	
	
}
