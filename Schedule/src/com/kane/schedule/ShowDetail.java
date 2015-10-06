package com.kane.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kane.memo.GetCurrentDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetail extends Activity {
	private String classTitle, classAddress;
	private SpecialAdapter adapter = null;
	private List<Map<String, Object>> data, listAdapter;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;
	private String setDay = null;
	public String startTime, endTime;
	private Context mContext = null;
	private String day = "";
	private ListView list;
	private String classSelected, timeSelected;
	private TextView tvMemoTitleDate, tvMemoTitleWeek, tvMemoTitleTime;
	private LinearLayout lvMemoTitle;
	private GetCurrentDate getDate;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	
		setContentView(R.layout.activity_show_detail);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.memo_title);
		
		
		Bundle bundle = this.getIntent().getExtras();
		id=bundle.getInt("id");
		setMyTitleDetail(id);
		listViewShow();
	
		
	

		/**
		 * ���ó����������������Ĳ˵�
		 */
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView c = (TextView) view.findViewById(R.id.classTitle);
				classSelected = c.getText().toString();
				c = (TextView) view.findViewById(R.id.classTime);
				timeSelected = c.getText().toString().substring(5, 10);

				list.showContextMenu();
				return true;
			}
		});

		/**
		 * ���õ����������Ĳ˵�,�����������Ӧ�����Ĳ˵�onContextItemSelected
		 */

		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub

				menu.setHeaderTitle("�Ƿ�ɾ�� [" + classSelected + "] ?");

				menu.add(0, 0, 0, "ɾ��");

				menu.add(0, 1, 1, "ȡ��");

			}

		});
		
		/**
		 * ��������¿γ̵İ�ť
		 */
		Button addnew = (Button) findViewById(R.id.addnew);
		addnew.setOnClickListener(new OnClickListener() {

			// �����ܼ������¸�����
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String i = "��" + day;
				Intent intent = new Intent(mContext, AddNew.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", i);

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
	}

	/**
	 * ��Ӧ�����Ĳ˵���ִ����Ӧ����
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)

	{

//		setTitle("����˳����˵�����ĵ�" + item.getItemId() + "����Ŀ");

		int selectedPosition = item.getItemId();// ��ȡ����˵ڼ���

		switch (item.getItemId()) {
		case 0:
			// ɾ������

			data.remove(selectedPosition);

			adapter.notifyDataSetChanged();
			list.invalidate();
			deleteData(classSelected, timeSelected, day);
			Toast.makeText(ShowDetail.this, "ɾ�����", Toast.LENGTH_SHORT).show();
			break;

		case 1:
			// ȡ������

			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);

	}

	/**
	 * ɾ��ѡ��Ŀγ�
	 * 
	 * @param classTitle
	 * @param startTime
	 * @param day
	 */
	private void deleteData(String classTitle, String startTime, String day) {
		MyDatabase dbHelper = new MyDatabase(ShowDetail.this);
		// �õ�һ����д��SQLiteDatabase����
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		day = "��" + day;
		db.delete("classTable", "day='" + day + "' and classTitle='"
				+ classTitle + "' and startTime='" + startTime + "'", null);
		db.close();
	}

	/**
	 * 
	 * ��ȡ���ݿ��е�����
	 */
	private List<Map<String, Object>> getData() {

		List<Map<String, Object>> listAdapter = new ArrayList<Map<String, Object>>();
		MyDatabase dbHelper = new MyDatabase(ShowDetail.this);
		// �õ�һ���ɶ���SQLiteDatabase����
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		cursor = null;
		setDay = "��" + day;
		cursor = db.rawQuery("Select * from classTable where day=" + "'"
				+ setDay + "'" + "order by classHour", null);

		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			classTitle = cursor.getString(cursor.getColumnIndex("classTitle"));
			classAddress = cursor.getString(cursor.getColumnIndex("address"));
			startTime = cursor.getString(cursor.getColumnIndex("startTime"));
			endTime = cursor.getString(cursor.getColumnIndex("endTime"));

			map.put("classTitle", classTitle);
			map.put("classAddress", "�Ͽεص㣺" + classAddress);
			map.put("classTime", "�Ͽ�ʱ��:" + startTime + "~" + endTime);

			listAdapter.add(map);

		}
		db.close();
		return listAdapter;
	}

	/**
	 * �����ص���ҳ����ˢ������
	 */
	@Override
	protected void onResume() {
		super.onResume();
listViewShow();
		

	}

	public void setMyTitleDetail(int id) {

		tvMemoTitleDate = (TextView) findViewById(R.id.memo_title_date);
		
//		tvMemoTitleWeek = (TextView) findViewById(R.id.memo_title_week);
//		
//		tvMemoTitleTime = (TextView) findViewById(R.id.memo_title_time);
		
		lvMemoTitle = (LinearLayout) findViewById(R.id.memo_title_background);
		getDate = new GetCurrentDate();
		
		// Show the title as below format
		day=getDate.getWeekByID(id);
		tvMemoTitleDate.setText("��"+day + "�Ŀγ���ϸ����");
	
		// tvMemoTitleWeek.setText(getDate.getDate());
		tvMemoTitleDate.setGravity(Gravity.CENTER);
	

		lvMemoTitle.setBackgroundColor(Color.parseColor("#000000"));
		tvMemoTitleDate.setTextColor(Color.parseColor("#ffffff"));
//		tvMemoTitleWeek.setVisibility(View.INVISIBLE);
//
//		tvMemoTitleTime.setVisibility(View.INVISIBLE);
		
	}
	public void listViewShow(){
		/**
		 * ����listview��ʾ���տγ�
		 * 
		 */
		// ��Layout�����ListView
		list = (ListView) findViewById(R.id.classDetaillist);
		// ���ɶ�̬���飬��������
		data = getData();
	
		adapter = new SpecialAdapter(
				ShowDetail.this,
				data,
				R.layout.list_item,
				new String[] { "classTitle", "classAddress", "classTime" },
				new int[] { R.id.classTitle, R.id.classAddress, R.id.classTime });

		list.setAdapter(adapter);
	}

}
