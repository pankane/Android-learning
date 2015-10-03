package com.kane.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SpecialAdapter extends SimpleAdapter {

	private int[] colors = new int[] { 0x30FF0000, 0x300000FF };// ����û�����ý�ȥʹ��,ֻ�Ǽ�������������

	public SpecialAdapter(Context context, List<? extends Map<String, ?>> data,

	int resource, String[] from, int[] to) {

		super(context, data, resource, from, to);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = super.getView(position, convertView, parent);

		int colorPos = position % colors.length;

		if (colorPos == 1)

			view.setBackgroundColor(Color.argb(50, 90, 185, 99)); // ��ɫ����

		else

			view.setBackgroundColor(Color.argb(50, 136, 195, 74));// ��ɫ����
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

			/**
			 * to get the day text in the dayText
			 */
			/**
			 * For today, give a highlighted color
			 */
			TextView dayText = (TextView) view.findViewById(R.id.day);

			if (dayText.getText() == day) {

				view.setBackgroundColor(Color.argb(150, 136, 195, 74));

			}

		} catch (Exception e) {

		}

		return view;
	}
}
