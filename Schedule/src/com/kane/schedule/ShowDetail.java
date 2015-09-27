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
		setTitle("���տγ���ϸ");
		setContentView(R.layout.activity_show_detail);
		TextView title = (TextView) this.findViewById(R.id.title);
		Bundle bundle = this.getIntent().getExtras();

		// �趨�������ڼ�
		int id = bundle.getInt("id");

		switch (id) {
		case 1:
			day = "һ";
			break;
		case 2:
			day = "��";
			break;
		case 3:
			day = "��";
			break;
		case 4:
			day = "��";
			break;
		case 5:
			day = "��";
			break;
		case 6:
			day = "��";
			break;
		case 7:
			day = "��";

			break;

		default:
			break;
		}
		title.setText("��" + day + "�Ŀγ���ϸ����");

		/**
		 * ����listview��ʾ���տγ�
		 * 
		 */
		// ��Layout�����ListView
		list = (ListView) findViewById(R.id.classDetaillist);
		// ���ɶ�̬���飬��������
		data = getData();
		adapter = new SimpleAdapter(
				ShowDetail.this,
				data,
				R.layout.list_item,
				new String[] { "classTitle", "classAddress", "classTime" },
				new int[] { R.id.classTitle, R.id.classAddress, R.id.classTime });

		list.setAdapter(adapter);
		
		
		
		/**
		 * ���ó����������������Ĳ˵�
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
		 * ���õ����������Ĳ˵�,�����������Ӧ�����Ĳ˵�onContextItemSelected
		 */

		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub

				menu.setHeaderTitle("��ѡ����Ҫ��" + classSelected + "��ɵĲ���");

				menu.add(0, 0, 0, "ɾ��");

				menu.add(0, 1, 1, "�޸�");
				menu.add(0, 2, 2, "ȡ�������ؿγ��б�");

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

		setTitle("����˳����˵�����ĵ�" + item.getItemId() + "����Ŀ");
		
		int selectedPosition = item.getItemId();// ��ȡ����˵ڼ���

		System.out.println(selectedPosition);
		
		switch (item.getItemId()) {
		case 0:
			// ɾ������
			Toast.makeText(ShowDetail.this, "ɾ��", Toast.LENGTH_SHORT).show();
			data.remove(selectedPosition);
			adapter.notifyDataSetChanged();
			list.invalidate();
			break;

		case 1:
			// �޸Ĳ���
			Toast.makeText(ShowDetail.this, "�޸�", Toast.LENGTH_SHORT).show();
			break;

		case 2:
			// ȡ������
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

	/**
	 * 
	 * ��ȡ���ݿ��е�����
	 */
	private List<Map<String, Object>> getData() {
		
		List<Map<String, Object>> listAdapter = new ArrayList<Map<String, Object>>();
		MyDatabase dbHelper = new MyDatabase(ShowDetail.this, "classList_db",
				null, 1);
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

		onCreate(null);

	}

}
