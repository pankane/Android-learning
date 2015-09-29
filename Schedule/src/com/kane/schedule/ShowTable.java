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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ShowTable extends Activity {
	private ListView listView;
	private SpecialAdapter adapter=null;
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
		adapter = new SpecialAdapter(ShowTable.this, data,
				R.layout.main_list_item, new String[] { "day", "morning",
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

		/**
		 * 点击备份数据
		 */
		Button backup = (Button) findViewById(R.id.dataBackup);
		backup.setOnClickListener(new OnClickListener() {

			// 传递周几给到下个界面
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			dataBackup();
			Toast.makeText(ShowTable.this, "数据已备份到ScheduleBackup目录下", Toast.LENGTH_SHORT).show();
				 
			}
		});
		
		/**
		 * 点击恢复数据
		 */
		Button restore = (Button) findViewById(R.id.dataRestore);
		restore.setOnClickListener(new OnClickListener() {

			// 传递周几给到下个界面
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			dataRecover();
			
	
			ShowTable.this.onCreate(null);
			Toast.makeText(ShowTable.this, "数据已恢复", Toast.LENGTH_SHORT).show();
				 
			}
		});

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
							map.put("morning", map.get("morning") + "\n"
									+ classTitle);
						} else {
							map.put("morning", classTitle); // 放入课程名称
							flagMorning = true;
						}
					} else if (classHour >= 12 && classHour < 18) {
						// 判断早上是否超过一节课，如果是都要显示在早上列中，设置flag帮助判断
						if (flagAfternoon) {
							map.put("afternoon", map.get("afternoon") + "\n"
									+ classTitle);
						} else {
							map.put("afternoon", classTitle); // 放入课程名称
							flagAfternoon = true;
						}

					} else {
						// 判断晚上是否超过一节课，如果是都要显示在早上列中，设置flag帮助判断
						if (flagEvening) {
							map.put("evening", map.get("evening") + "\n"
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
	




	

//数据恢复
 private void dataRecover() {
     // TODO Auto-generated method stub
     new BackupTask(this).execute("restroeDatabase");
 }

 // 数据备份
 private void dataBackup() {
     // TODO Auto-generated method stub
     new BackupTask(this).execute("backupDatabase");
 }

	
}
