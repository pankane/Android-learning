package com.kane.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kane.memo.GetCurrentDate;
import com.kane.memo.MemoNew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowTable extends Activity implements
		android.view.View.OnClickListener {
	private ListView listView;
	private List<View> mViews = new ArrayList<View>();// �������Tab01-03
	private TextView tvMemoTitleDate, tvMemoTitleWeek,tvMemoTitleTime;
	private Cursor cursor = null;
	private Context mContext = null;

	private String[] dayList = { "��һ", "�ܶ�", "����", "����", "����", "����", "����" };

	private ViewPager mViewPager;// �������ý����л�
	private PagerAdapter mPagerAdapter;// ��ʼ��View������

	// 3��Tab��ÿ��Tab����һ����ť
	private LinearLayout mTabClassTable;
	private LinearLayout mTabMemo;
	private LinearLayout mSettingAbout;
	private Button restore, backup;
	// 3����ť
	private ImageButton mClassTableImg;
	private ImageButton mMemoImg;
	private ImageButton mSettingImg;
	private ImageButton mMemoNewImg;
	private LayoutInflater mLayoutInflater;
	private GetCurrentDate getDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_show_table);// ������ҳ��ʾһ�ܿγ�
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.memo_title);
		mLayoutInflater = getLayoutInflater();
		setMyTitleClass();
		

		
		
		initView();

		listViewClick();
		mClassTableImg.setImageResource(R.drawable.tab_classtable_pressed);

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
							map.put("morning", map.get("morning") + "\n"
									+ classTitle);
						} else {
							map.put("morning", classTitle); // ����γ�����
							flagMorning = true;
						}
					} else if (classHour >= 12 && classHour < 18) {
						// �ж������Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagAfternoon) {
							map.put("afternoon", map.get("afternoon") + "\n"
									+ classTitle);
						} else {
							map.put("afternoon", classTitle); // ����γ�����
							flagAfternoon = true;
						}

					} else {
						// �ж������Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagEvening) {
							map.put("evening", map.get("evening") + "\n"
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
		initView();
		listViewClick();

	}

	/**
	 * ListView onItemClicklistener
	 */
	private void listViewClick() {

		initViewPage();
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

			}
		});
		initEvent();

	}

	// ���ݻָ�
	private void dataRecover() {

		new BackupTask(this).execute("restroeDatabase");
	}

	// ���ݱ���
	private void dataBackup() {

		new BackupTask(this).execute("backupDatabase");
	}

	/**
	 * Initialize view/bottom menu
	 */

	private void initEvent() {
		mTabClassTable.setOnClickListener(this);
		mTabMemo.setOnClickListener(this);
		mSettingAbout.setOnClickListener(this);
		backup.setOnClickListener(this);
		restore.setOnClickListener(this);
		mMemoNewImg.setOnClickListener(this);

		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * ViewPage���һ���ʱ
			 */
			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				switch (currentItem) {
				case 0:
					resetImg();
					mPagerAdapter.notifyDataSetChanged();
				setMyTitleClass();
					mClassTableImg
							.setImageResource(R.drawable.tab_classtable_pressed);

					break;
				case 1:
					resetImg();
					mMemoImg.setImageResource(R.drawable.tab_memo_pressed);
					setMyTitleMemo();
					break;
				case 2:
					resetImg();
					mSettingImg
							.setImageResource(R.drawable.tab_setting_pressed);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpage);
		// ��ʼ���ĸ�LinearLayout
		mTabClassTable = (LinearLayout) findViewById(R.id.id_tab_class);

		mSettingAbout = (LinearLayout) findViewById(R.id.id_tab_setting);
		mTabMemo = (LinearLayout) findViewById(R.id.id_tab_memo);
		// ��ʼ���ĸ���ť
		mClassTableImg = (ImageButton) findViewById(R.id.id_tab_classtable_img);
		mMemoImg = (ImageButton) findViewById(R.id.id_tab_memo_img);
		mSettingImg = (ImageButton) findViewById(R.id.id_tab_setting_img);

	}

	/**
	 * ��ʼ��ViewPage
	 */
	private void initViewPage() {

		// Initialize 3 tabs and listview and button, don't it in the oncreate

		SpecialAdapter adapter;
		View tab01 = mLayoutInflater.inflate(R.layout.tab01, null);
		View tab02 = mLayoutInflater.inflate(R.layout.tab02, null);
		View tab03 = mLayoutInflater.inflate(R.layout.tab03, null);
		listView = (ListView) tab01.findViewById(R.id.daylist);// ��ʾһ���б�

		List<Map<String, Object>> data = null;
		data = getData();

		adapter = new SpecialAdapter(this, data, R.layout.main_list_item,
				new String[] { "day", "morning", "afternoon", "evening" },
				new int[] { R.id.day, R.id.morning, R.id.afternoon,
						R.id.evening });

		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.invalidate();
		backup = (Button) tab03.findViewById(R.id.dataBackup);
		restore = (Button) tab03.findViewById(R.id.dataRestore);
		mMemoNewImg=(ImageButton)tab02.findViewById(R.id.memo_new);
		mViews.clear();
		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab03);

		// ��������ʼ��������
		mPagerAdapter = new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {

				container.removeView((View) object);

			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {

				container.addView(mViews.get(position));

				return mViews.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}

			@Override
			public int getCount() {

				return mViews.size();
			}

			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
			}

		};

		mViewPager.setAdapter(mPagerAdapter);

	}

	/**
	 * �ж��ĸ�Ҫ��ʾ�������ð�ťͼƬ
	 */
	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.id_tab_class:
			mViewPager.setCurrentItem(0);
			
			setMyTitleClass();
			resetImg();
			mClassTableImg.setImageResource(R.drawable.tab_classtable_pressed);
			break;
		case R.id.id_tab_setting:
			mViewPager.setCurrentItem(2);
			resetImg();
			mSettingImg.setImageResource(R.drawable.tab_setting_pressed);
			break;
		case R.id.id_tab_memo:
			mViewPager.setCurrentItem(1);
			setMyTitleMemo();
			resetImg();
			mMemoImg.setImageResource(R.drawable.tab_memo_pressed);
			break;
		// Data backup
		case R.id.dataBackup:
			dataBackup();
			View list = mViewPager.findViewWithTag("classTable");

			Toast.makeText(ShowTable.this, "�����ѱ��ݵ�ScheduleBackupĿ¼��",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.dataRestore:
			// data Recovery
			dataRecover();

			initView();
			listViewClick();

			Toast.makeText(ShowTable.this, "�����ѻָ�", Toast.LENGTH_SHORT).show();
			break;
		case R.id.memo_new:
			Intent intent=new Intent(mContext,MemoNew.class);
			startActivity(intent);
//			ShowTable.this.finish();
			
		default:				
			
			break;
		}
	}

	/**
	 * ������ͼƬ�䰵
	 */
	private void resetImg() {
		mClassTableImg.setImageResource(R.drawable.tab_classtable);
		mMemoImg.setImageResource(R.drawable.tab_memo);
		mSettingImg.setImageResource(R.drawable.tab_setting);

	}
	
	/**
	 * setMyTitle for the classtable page to change the tile by using a customized tile
	 */

	public void setMyTitleClass(){
		tvMemoTitleDate = (TextView) findViewById(R.id.memo_title_date);
		tvMemoTitleWeek = (TextView) findViewById(R.id.memo_title_week);
		tvMemoTitleTime = (TextView) findViewById(R.id.memo_title_time);
		getDate=new GetCurrentDate();
		//Show the title as below format
		tvMemoTitleDate.setText("������"+getDate.getWeek());
		tvMemoTitleWeek.setText(getDate.getDate());
		tvMemoTitleDate.setGravity(Gravity.LEFT);
		tvMemoTitleWeek.setGravity(Gravity.LEFT);
		
		tvMemoTitleDate.setTextColor(Color.parseColor("#00bfa5"));
		tvMemoTitleWeek.setVisibility(View.VISIBLE);
		tvMemoTitleDate.setTextSize(20);
		tvMemoTitleTime.setVisibility(View.INVISIBLE);
		
		
	}
	/**
	 * set title for the memo page
	 */
public void setMyTitleMemo(){
	tvMemoTitleDate = (TextView) findViewById(R.id.memo_title_date);
	tvMemoTitleWeek = (TextView) findViewById(R.id.memo_title_week);
	tvMemoTitleDate.setText("����¼");
	tvMemoTitleDate.setGravity(Gravity.CENTER);
	tvMemoTitleDate.setTextColor(Color.parseColor("#00bfa5"));
	tvMemoTitleWeek.setVisibility(View.INVISIBLE);
	tvMemoTitleDate.setTextSize(30);
	
	
	
}
}
