package com.kane.memo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.kane.schedule.R;
import com.kane.schedule.R.layout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MemoNew extends Activity {
	private TextView tvMemoTitleDate, tvMemoTitleWeek, tvMemoTitleTime;
	private GetCurrentDate getDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setup title of this page to show the current date with time

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_memo_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.memo_title);

		tvMemoTitleDate = (TextView) findViewById(R.id.memo_title_date);
		tvMemoTitleWeek = (TextView) findViewById(R.id.memo_title_week);
		tvMemoTitleTime = (TextView) findViewById(R.id.memo_title_time);

		getDate = new GetCurrentDate();
		tvMemoTitleDate.setText(getDate.getDate());
		tvMemoTitleWeek.setText(getDate.getWeek());
		tvMemoTitleTime.setText(getDate.getTime());
		tvMemoTitleDate.setTextColor(Color.parseColor("#98FB98"));
		tvMemoTitleWeek.setTextColor(Color.parseColor("#20B2AA"));
		tvMemoTitleTime.setTextColor(Color.parseColor("#20B2AA"));

		// setup Backup ground color
		tvMemoTitleWeek.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		tvMemoTitleTime.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 7));

		tvMemoTitleDate.setBackgroundColor(Color.parseColor("#C1CDC1"));
		tvMemoTitleWeek.setBackgroundColor(Color.parseColor("#C1CDC1"));
		tvMemoTitleTime.setBackgroundColor(Color.parseColor("#C1CDC1"));

	}

}
