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

//���ӿγ�
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

		setTitle("����¿γ�");
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
				 * ʵ����һ��TimePickerDialog�Ķ���
				 * �ڶ���������һ��TimePickerDialog.OnTimeSetListener�����ڲ���
				 * �����û�ѡ���ʱ�����done����������onTimeset����
				 */
				// ���ʱ��ѡ�����������ε�����
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

									classStarthour.setText("�γ̿�ʼʱ�� "
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
				 * ʵ����һ��TimePickerDialog�Ķ���
				 * �ڶ���������һ��TimePickerDialog.OnTimeSetListener�����ڲ���
				 * �����û�ѡ���ʱ�����done����������onTimeset����
				 */
				// ���ʱ��ѡ�����������ε�����
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

										classEndhour.setText("�γ̽���ʱ�� "
												+ endTimestr);
									} else {
										Toast.makeText(AddNew.this,
												"�γ̽���ʱ��Ӧ���ڿ�ʼʱ�䣬����������",
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

		// �ύ��ť���������ݵ����ݿ�
		submitClass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				classTitle = (EditText) findViewById(R.id.subjectname);
				classAddress = (EditText) findViewById(R.id.address);
				// �ж��Ƿ�4��ѡ��������ݣ����Ϊ������ʾ
				if (classTitle.length() == 0) {
					Toast.makeText(getApplicationContext(), "������γ�����",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (classAddress.length() == 0) {
					Toast.makeText(getApplicationContext(), "�������Ͽεص����ѵ����",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (classStarthour.length() == 0) {
					Toast.makeText(getApplicationContext(), "��ѡ���Ͽ�ʱ��",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (classEndhour.length() == 0) {
					Toast.makeText(getApplicationContext(), "ѡ���¿�ʱ��",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					MyDatabase dbHelper = new MyDatabase(AddNew.this,
							"classList_db", null, 1);
					// �õ�һ����д��SQLiteDatabase����
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
					Toast.makeText(getApplicationContext(), "�γ�������",
							Toast.LENGTH_SHORT).show();
					finish();
				}

				// /day text, classTitle text, address text, classHour int,
				// classMinute int

			}

		});
	}

}
