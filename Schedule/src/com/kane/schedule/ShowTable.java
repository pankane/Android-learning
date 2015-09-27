package com.kane.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ShowTable extends Activity {
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;
	Context mContext = null;

	String[] dayList = { "��һ", "�ܶ�", "����", "����", "����", "����", "����" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("��ӭ�鿴�γ̱�");
		mContext = this;
		setContentView(R.layout.activity_show_table);// ������ҳ��ʾһ�ܿγ�
		listView = (ListView) this.findViewById(R.id.daylist);// ��ʾһ���б�
		data = getData();
		adapter = new SimpleAdapter(ShowTable.this, data,
				R.layout.activity_show_table, new String[] { "day", "morning",
						"afternoon", "evening" }, new int[] { R.id.day,
						R.id.morning, R.id.afternoon, R.id.evening });
		// adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);

		// ����󴫵ݸ��е��鿴�����
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int i = position + 1;
				// setTitle("�����"+i+"����Ŀ");
				Intent intent = new Intent(mContext, ShowDetail.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", i);

				intent.putExtras(bundle);
				startActivity(intent);

				// TODO Auto-generated method stub
			}
		});

		//

	}

	// ��ȡ���ݿ�
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		MyDatabase dbHelper = new MyDatabase(ShowTable.this, "classList_db",
				null, 1);
		// �õ�һ���ɶ���SQLiteDatabase����
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		boolean flagMorning = false;
		boolean flagAfternoon = false;
		boolean flagEvening = false;
		try {
			// ��ѯ����
			for (int i = 0; i <= dayList.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				cursor = null;
				cursor = db.rawQuery(
						"Select day,classTitle,classHour from classTable where day="
								+ "'" + dayList[i] + "'", null);

				flagMorning = false;
				flagAfternoon = false;
				flagEvening = false;
				// ���ܼ�������,������һ������
				map.put("day", dayList[i]);
		

				while (cursor.moveToNext()) {
					String classTitle = cursor.getString(cursor
							.getColumnIndex("classTitle"));
					int classHour = cursor.getInt(cursor
							.getColumnIndex("classHour"));
					if (classHour < 12) {
						// �ж�����Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagMorning) {
							map.put("morning", map.get("morning") + ";"
									+ classTitle);
						} else {
							map.put("morning", classTitle); // ����γ�����
							flagMorning = true;
						}
					} else if (classHour >= 12 && classHour < 18) {
						// �ж������Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagAfternoon) {
							map.put("afternoon", map.get("afternoon") + ";"
									+ classTitle);
						} else {
							map.put("afternoon", classTitle); // ����γ�����
							flagAfternoon = true;
						}

					} else {
						// �ж������Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagEvening) {
							map.put("evening", map.get("evening") + ";"
									+ classTitle);
						} else {
							map.put("evening", classTitle); // ����γ�����
							flagEvening = true;
						}
					}

				}
				list.add(map);

			}
		} catch (Exception e) {
			Log.i("opendatabase", e.toString());
		}
		db.close();
		return list;

	}

	@Override
	protected void onResume() {
		super.onResume();

		onCreate(null);

	}
}
