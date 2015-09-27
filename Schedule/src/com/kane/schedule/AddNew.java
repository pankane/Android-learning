package com.kane.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

//增加课程
public class AddNew extends Activity {

	private TimePicker classTime;
	Button submitClass, cancelClass;
	private EditText classTitle, classAddress, classStarthour, classEndhour;
	private int classHour, classMinute, endHour, endMinute;
	String id = "";
	public String startTimestr, endTimestr;
	boolean flag = true;
	private Calendar c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new);

		setTitle("添加新课程");
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getString("id");

		// classTime = (TimePicker) findViewById(R.id.timePicker);
		// classTime.setIs24HourView(true);
		classStarthour = (EditText) findViewById(R.id.startHour);
		classEndhour = (EditText) findViewById(R.id.endHour);
		submitClass = (Button) findViewById(R.id.submitClass);

		cancelClass = (Button) findViewById(R.id.cancelClass);

		cancelClass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		classStarthour.setOnTouchListener(new View.OnTouchListener() {

			int touch_flag = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				c = Calendar.getInstance();

				/**
				 * 实例化一个TimePickerDialog的对象
				 * 第二个参数是一个TimePickerDialog.OnTimeSetListener匿名内部类
				 * ，当用户选择好时间后点击done会调用里面的onTimeset方法
				 */
				// 解决时间选择器出现两次的问题
				touch_flag++;
				if (touch_flag == 2) {

					TimePickerDialog timePickerDialog = new TimePickerDialog(
							AddNew.this,
							new TimePickerDialog.OnTimeSetListener() {

								@Override
								public void onTimeSet(TimePicker view,
										int hourOfDay, int minute) {

									//
									c.set(Calendar.HOUR_OF_DAY, hourOfDay);

									c.set(Calendar.MINUTE, minute);

									c.set(Calendar.SECOND, 0);

									c.set(Calendar.MILLISECOND, 0);

									SimpleDateFormat startTime = new SimpleDateFormat(
											"HH:mm");
									startTimestr = startTime.format(c.getTime())
											+ "";
									classHour = hourOfDay;
									classMinute = minute;

									classStarthour.setText("课程开始时间 "
											+ startTimestr);

								}
							}, 12, 0, true);
					touch_flag = 0;
					timePickerDialog.show();
				}

				return false;
			}
		});

		classEndhour.setOnTouchListener(new View.OnTouchListener() {
			int touch_flag = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				c = Calendar.getInstance();
				/**
				 * 实例化一个TimePickerDialog的对象
				 * 第二个参数是一个TimePickerDialog.OnTimeSetListener匿名内部类
				 * ，当用户选择好时间后点击done会调用里面的onTimeset方法
				 */
				// 解决时间选择器出现两次的问题
				touch_flag++;

				if (touch_flag == 2) {
					TimePickerDialog timePickerDialog = new TimePickerDialog(
							AddNew.this,
							new TimePickerDialog.OnTimeSetListener() {
								@Override
								public void onTimeSet(TimePicker view,
										int hourOfDay, int minute) {
									c.set(Calendar.HOUR_OF_DAY, hourOfDay);

									c.set(Calendar.MINUTE, minute);

									c.set(Calendar.SECOND, 0);

									c.set(Calendar.MILLISECOND, 0);

									SimpleDateFormat endTime = new SimpleDateFormat(
											"HH:mm");
									endTimestr = endTime.format(c.getTime())
											+ "";
									endHour = hourOfDay;
									endMinute = minute;
									if (endHour >= classHour) {

										classEndhour.setText("课程结束时间 "
												+ endTimestr);
									} else {
										Toast.makeText(AddNew.this,
												"课程结束时间应晚于开始时间，请重新设置",
												Toast.LENGTH_SHORT).show();
										touch_flag = 0;

									}
								}
							}, classHour, classMinute, true);
					touch_flag = 0;

					timePickerDialog.show();
				}

				return false;
			}
		});

		// 提交按钮并增加数据到数据库
		submitClass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				classTitle = (EditText) findViewById(R.id.subjectname);
				classAddress = (EditText) findViewById(R.id.address);
				// 判断是否4个选项都输入内容，如果为空则提示
				if (classTitle.length() == 0) {
					Toast.makeText(getApplicationContext(), "请输入课程名称",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (classAddress.length() == 0) {
					Toast.makeText(getApplicationContext(), "请输入上课地点或培训机构",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (classStarthour.length() == 0) {
					Toast.makeText(getApplicationContext(), "请选择上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (classEndhour.length() == 0) {
					Toast.makeText(getApplicationContext(), "选择下课时间",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					MyDatabase dbHelper = new MyDatabase(AddNew.this,
							"classList_db", null, 1);
					// 得到一个可写的SQLiteDatabase对象
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					ContentValues classDetail = new ContentValues();
					classDetail.put("day", id);
					classDetail.put("classTitle", classTitle.getText()
							.toString().trim());
					classDetail.put("address", classAddress.getText()
							.toString().trim());
					classDetail.put("classHour", classHour);
					// classDetail.put("classMinute", classMinute);
					// classDetail.put("endHour", endHour);
					// classDetail.put("endMinute", endMinute);
					classDetail.put("startTime", startTimestr);
					classDetail.put("endTime", endTimestr);

					long row = db.insert("classTable", null, classDetail);
					db.close();
					Toast.makeText(getApplicationContext(), "课程已增加",
							Toast.LENGTH_SHORT).show();
					finish();
				}

				// /day text, classTitle text, address text, classHour int,
				// classMinute int

			}

		});
	}

}
