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

	String[] dayList = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("欢迎查看课程表");
		mContext = this;
		setContentView(R.layout.activity_show_table);// 加载首页显示一周课程
		listView = (ListView) this.findViewById(R.id.daylist);// 显示一周列表
		data = getData();
		adapter = new SimpleAdapter(ShowTable.this, data,
				R.layout.activity_show_table, new String[] { "day", "morning",
						"afternoon", "evening" }, new int[] { R.id.day,
						R.id.morning, R.id.afternoon, R.id.evening });
		// adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);

		// 点击后传递该行到查看详情表
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int i = position + 1;
				// setTitle("点击第"+i+"个项目");
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

	// 读取数据库
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		MyDatabase dbHelper = new MyDatabase(ShowTable.this, "classList_db",
				null, 1);
		// 得到一个可读的SQLiteDatabase对象
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		boolean flagMorning = false;
		boolean flagAfternoon = false;
		boolean flagEvening = false;
		try {
			// 查询数据
			for (int i = 0; i <= dayList.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				cursor = null;
				cursor = db.rawQuery(
						"Select day,classTitle,classHour from classTable where day="
								+ "'" + dayList[i] + "'", null);

				flagMorning = false;
				flagAfternoon = false;
				flagEvening = false;
				// 把周几放入表格,包含周一到周日
				map.put("day", dayList[i]);
		

				while (cursor.moveToNext()) {
					String classTitle = cursor.getString(cursor
							.getColumnIndex("classTitle"));
					int classHour = cursor.getInt(cursor
							.getColumnIndex("classHour"));
					if (classHour < 12) {
						// 判断早成是否超过一节课，如果是都要显示在早上列中，设置flag帮助判断
						if (flagMorning) {
							map.put("morning", map.get("morning") + ";"
									+ classTitle);
						} else {
							map.put("morning", classTitle); // 放入课程名称
							flagMorning = true;
						}
					} else if (classHour >= 12 && classHour < 18) {
						// 判断早上是否超过一节课，如果是都要显示在早上列中，设置flag帮助判断
						if (flagAfternoon) {
							map.put("afternoon", map.get("afternoon") + ";"
									+ classTitle);
						} else {
							map.put("afternoon", classTitle); // 放入课程名称
							flagAfternoon = true;
						}

					} else {
						// 判断晚上是否超过一节课，如果是都要显示在早上列中，设置flag帮助判断
						if (flagEvening) {
							map.put("evening", map.get("evening") + ";"
									+ classTitle);
						} else {
							map.put("evening", classTitle); // 放入课程名称
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
