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
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetail extends Activity {
	private String classTitle, classAddress;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data,listAdapter;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;
	String setDay = null;
	public String startTime, endTime;
	Context mContext = null;
	String day = "";
	private ListView list;
	private String classSelected;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;
		setTitle("单日课程明细");
		setContentView(R.layout.activity_show_detail);
		TextView title = (TextView) this.findViewById(R.id.title);
		Bundle bundle = this.getIntent().getExtras();

		// 设定标题星期几
		int id = bundle.getInt("id");

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
		title.setText("周" + day + "的课程明细如下");

		/**
		 * 建立listview显示当日课程
		 * 
		 */
		// 绑定Layout里面的ListView
		list = (ListView) findViewById(R.id.classDetaillist);
		// 生成动态数组，加入数据
		data = getData();
		adapter = new SimpleAdapter(
				ShowDetail.this,
				data,
				R.layout.list_item,
				new String[] { "classTitle", "classAddress", "classTime" },
				new int[] { R.id.classTitle, R.id.classAddress, R.id.classTime });

		list.setAdapter(adapter);
		
		
		
		/**
		 * 设置长按监听弹出上下文菜单
		 */
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView c = (TextView) view.findViewById(R.id.classTitle);
				classSelected = c.getText().toString() + position;
				list.showContextMenu();
				return true;
			}
		});

		/**
		 * 设置弹出的上下文菜单,具体操作在响应上下文菜单onContextItemSelected
		 */

		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub

				menu.setHeaderTitle("请选择您要对" + classSelected + "完成的操作");

				menu.add(0, 0, 0, "删除");

				menu.add(0, 1, 1, "修改");
				menu.add(0, 2, 2, "取消并返回课程列表");

			}

		});

		/**
		 * 点击增加新课程的按钮
		 */
		Button addnew = (Button) findViewById(R.id.addnew);
		addnew.setOnClickListener(new OnClickListener() {

			// 传递周几给到下个界面
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String i = "周" + day;
				Intent intent = new Intent(mContext, AddNew.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", i);

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	/**
	 * 响应上下文菜单并执行响应操作
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)

	{

		setTitle("点击了长按菜单里面的第" + item.getItemId() + "个项目");
		
		int selectedPosition = item.getItemId();// 获取点击了第几行

		System.out.println(selectedPosition);
		
		switch (item.getItemId()) {
		case 0:
			// 删除数据
			Toast.makeText(ShowDetail.this, "删除", Toast.LENGTH_SHORT).show();
			data.remove(selectedPosition);
			adapter.notifyDataSetChanged();
			list.invalidate();
			break;

		case 1:
			// 修改操作
			Toast.makeText(ShowDetail.this, "修改", Toast.LENGTH_SHORT).show();
			break;

		case 2:
			// 取消操作
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

	/**
	 * 
	 * 获取数据库中的数据
	 */
	private List<Map<String, Object>> getData() {
		
		List<Map<String, Object>> listAdapter = new ArrayList<Map<String, Object>>();
		MyDatabase dbHelper = new MyDatabase(ShowDetail.this, "classList_db",
				null, 1);
		// 得到一个可读的SQLiteDatabase对象
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		cursor = null;
		setDay = "周" + day;
		cursor = db.rawQuery("Select * from classTable where day=" + "'"
				+ setDay + "'" + "order by classHour", null);

		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			classTitle = cursor.getString(cursor.getColumnIndex("classTitle"));
			classAddress = cursor.getString(cursor.getColumnIndex("address"));
			startTime = cursor.getString(cursor.getColumnIndex("startTime"));
			endTime = cursor.getString(cursor.getColumnIndex("endTime"));

			map.put("classTitle", classTitle);
			map.put("classAddress", "上课地点：" + classAddress);
			map.put("classTime", "上课时间:" + startTime + "~" + endTime);

			listAdapter.add(map);

		}
		db.close();
		return listAdapter;
	}

	/**
	 * 当返回到该页面是刷新数据
	 */

	@Override
	protected void onResume() {
		super.onResume();

		onCreate(null);

	}

}
