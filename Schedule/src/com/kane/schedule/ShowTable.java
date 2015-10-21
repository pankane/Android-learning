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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kane.memo.MemoNew;

public class ShowTable extends Activity implements android.view.View.OnClickListener {
	private ListView listView, memoList;
	private List<View> mViews = new ArrayList<View>();// �������Tab01-03
	private TextView tvMemoTitleDate, tvMemoTitleWeek, tvMemoTitleTime;
	private LinearLayout lvMemoTitle;
	private Cursor cursor = null;
	private Cursor memoCursor = null;
	private Context mContext = null;
	private int setViewPageNumber = 0;
	private String[] dayList = { "��һ", "�ܶ�", "����", "����", "����", "����", "����" };
	private SpecialAdapter memoAdapter = null;
	private int selectedPosition;
	private ViewPager mViewPager;// �������ý����л�
	private PagerAdapter mPagerAdapter;// ��ʼ��View������
	private String memoItemSelected;
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
	private final static String MEMO_TABLE_NAME = "memoTable";
	public final static String MEMO_TABLE_SELECT_ALL = "SELECT * FROM " + MEMO_TABLE_NAME;
	List<Map<String, Object>> memoData = null;
	private SetPageTitle setTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_show_table);// ������ҳ��ʾһ�ܿγ�
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.memo_title);
		mLayoutInflater = getLayoutInflater();
		initView();
		setTitle = new SetPageTitle(tvMemoTitleDate, tvMemoTitleWeek, tvMemoTitleTime, lvMemoTitle);
		setTitle.setClassTableTitle();
		listViewClick();
		mClassTableImg.setImageResource(R.drawable.tab_classtable_pressed);
	}

	// getData to fill classTable
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		MyDatabase dbHelper = new MyDatabase(this);
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
				cursor = db.rawQuery("Select day,classTitle,classHour from classTable where day=" + "'" + dayList[i] + "'", null);
				flagMorning = false;
				flagAfternoon = false;
				flagEvening = false;
				// ���ܼ�������,������һ������
				map.put("day", dayList[i]);
				while (cursor.moveToNext()) {
					String classTitle = cursor.getString(cursor.getColumnIndex("classTitle"));
					int classHour = cursor.getInt(cursor.getColumnIndex("classHour"));
					if (classHour < 12) {
						// �ж�����Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagMorning) {
							map.put("morning", map.get("morning") + "\n" + classTitle);
						} else {
							map.put("morning", classTitle); // ����γ�����
							flagMorning = true;
						}
					} else if (classHour >= 12 && classHour < 18) {
						// �ж������Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagAfternoon) {
							map.put("afternoon", map.get("afternoon") + "\n" + classTitle);
						} else {
							map.put("afternoon", classTitle); // ����γ�����
							flagAfternoon = true;
						}
					} else {
						// �ж������Ƿ񳬹�һ�ڿΣ�����Ƕ�Ҫ��ʾ���������У�����flag�����ж�
						if (flagEvening) {
							map.put("evening", map.get("evening") + "\n" + classTitle);
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
		if (setViewPageNumber == 1) {
			mViewPager.setCurrentItem(setViewPageNumber);
			setViewPageNumber = 0;
			memoListLoad();
			setTitle.setMemoTitle();
		} else {
			initView();
			listViewClick();
			setTitle.setClassTableTitle();
		}
	}

	/**
	 * ListView onItemClicklistener
	 */
	private void listViewClick() {
		initViewPage();
		// ����󴫵ݸ��е��鿴�����
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

	private void memoListClick() {
		// ����󴫵ݸ��е��鿴�����
		memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (parent.getId() == R.id.memo_list) {
					Intent intent = new Intent();
					intent.setClass(ShowTable.this, MemoNew.class);
					memoCursor.moveToPosition(position); // ��cursorָ��position
					/* ���ݼ��µ�id�����⣬����,��Щ���ݴ����ݿ�����Ӧ���ֶ���ȡ�� */
					intent.putExtra(MyDatabase.MEMO_ID, memoCursor.getString(memoCursor.getColumnIndexOrThrow(MyDatabase.MEMO_ID)));
					intent.putExtra(MyDatabase.MEMO_DATE, memoCursor.getString(memoCursor.getColumnIndexOrThrow(MyDatabase.MEMO_DATE)));
					intent.putExtra(MyDatabase.MEMO_CONTENT, memoCursor.getString(memoCursor.getColumnIndexOrThrow(MyDatabase.MEMO_CONTENT)));
					intent.putExtra(MyDatabase.MEMO_TIME, memoCursor.getString(memoCursor.getColumnIndexOrThrow(MyDatabase.MEMO_TIME)));
					setViewPageNumber = 1;// a flag to show now is on page
											// 1(memopage), when return from the
											// memoNew and in onResume need to
											// move the page to page 1
					startActivity(intent);
				}
			}
		});
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
					classListLoad();
					resetImg();
					mPagerAdapter.notifyDataSetChanged();
					setTitle.setClassTableTitle();
					mClassTableImg.setImageResource(R.drawable.tab_classtable_pressed);
					break;
				case 1:
					memoListClick();
					resetImg();
					mMemoImg.setImageResource(R.drawable.tab_memo_pressed);
					setTitle.setMemoTitle();
					memoListLoad();
					memoListLongClickListener();
					break;
				case 2:
					resetImg();
					mSettingImg.setImageResource(R.drawable.tab_setting_pressed);
					setTitle.setSystemTitle();
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
		// initialize titles
		tvMemoTitleDate = (TextView) findViewById(R.id.memo_title_date);
		tvMemoTitleWeek = (TextView) findViewById(R.id.memo_title_week);
		tvMemoTitleTime = (TextView) findViewById(R.id.memo_title_time);
		lvMemoTitle = (LinearLayout) findViewById(R.id.memo_title_background);
	}

	/**
	 * ��ʼ��ViewPage
	 */
	private void initViewPage() {
		// Initialize 3 tabs and listview and button, don't it in the oncreate
		View tab01 = mLayoutInflater.inflate(R.layout.tab01, null);
		View tab02 = mLayoutInflater.inflate(R.layout.tab02, null);
		View tab03 = mLayoutInflater.inflate(R.layout.tab03, null);
		listView = (ListView) tab01.findViewById(R.id.daylist);// ��ʾһ���б�
		memoList = (ListView) tab02.findViewById(R.id.memo_list);
		classListLoad();
		memoListLoad();
		backup = (Button) tab03.findViewById(R.id.dataBackup);
		restore = (Button) tab03.findViewById(R.id.dataRestore);
		mMemoNewImg = (ImageButton) tab02.findViewById(R.id.memo_new);
		mViews.clear();
		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab03);
		// ��������ʼ��������
		mPagerAdapter = new PagerAdapter() {
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
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
			// setMyTitleClass();
			setTitle.setClassTableTitle();
			classListLoad();
			resetImg();
			mClassTableImg.setImageResource(R.drawable.tab_classtable_pressed);
			break;
		case R.id.id_tab_setting:
			mViewPager.setCurrentItem(2);
			// setMyTitleSystem();
			setTitle.setSystemTitle();
			resetImg();
			mSettingImg.setImageResource(R.drawable.tab_setting_pressed);
			break;
		case R.id.id_tab_memo:
			mViewPager.setCurrentItem(1);
			memoListClick();
			// setMyTitleMemo();
			setTitle.setMemoTitle();
			resetImg();
			mMemoImg.setImageResource(R.drawable.tab_memo_pressed);
			memoListLoad();
			memoListLongClickListener();
			break;
		// Data backup
		case R.id.dataBackup:
			dataBackup();
			View list = mViewPager.findViewWithTag("classTable");
			Toast.makeText(ShowTable.this, "�����ѱ��ݵ�ScheduleBackupĿ¼��", Toast.LENGTH_SHORT).show();
			break;
		case R.id.dataRestore:
			// data Recovery
			dataRecover();
			initView();
			listViewClick();
			Toast.makeText(ShowTable.this, "�����ѻָ�", Toast.LENGTH_SHORT).show();
			break;
		case R.id.memo_new:
			Intent intent = new Intent(mContext, MemoNew.class);
			startActivity(intent);
			setViewPageNumber = 1;
			// ShowTable.this.finish();
		case R.id.memo_list:
			System.out.print("I am in");
			memoListClick();
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

	private List<Map<String, Object>> memoListData() {
		List<Map<String, Object>> memoList = new ArrayList<Map<String, Object>>();
		MyDatabase dbHelper = new MyDatabase(this); // ������ݿ����
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			memoCursor = db.rawQuery(MEMO_TABLE_SELECT_ALL, null);
			while (memoCursor.moveToNext()) {
				Map<String, Object> memoMap = new HashMap<String, Object>();
				String memoContent = memoCursor.getString(memoCursor.getColumnIndex("content"));
				String memoDate = memoCursor.getString(memoCursor.getColumnIndex("date"));
				String memoID = memoCursor.getString(memoCursor.getColumnIndex("_id"));
				memoMap.put("content", memoContent);
				memoMap.put("date", memoDate);
				memoMap.put("_id", memoID);
				memoList.add(memoMap);
			}
		} catch (Exception e) {
			Log.i("opendatabase", e.toString());
		}
		db.close();
		return memoList;
	}

	private void classListLoad() {
		// Show classtable
		SpecialAdapter adapter;
		List<Map<String, Object>> data = null;
		data = getData();
		adapter = new SpecialAdapter(this, data, R.layout.main_list_item, new String[] { "day", "morning", "afternoon", "evening" }, new int[] { R.id.day,
				R.id.morning, R.id.afternoon, R.id.evening });
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.invalidate();
	}

	private void memoListLoad() {
		// show memolist
		memoData = memoListData();
		memoAdapter = new SpecialAdapter(this, memoData, R.layout.memo_list_item, new String[] { "content", "date", "_id" }, new int[] {
				R.id.memo_list_content, R.id.memo_list_date, R.id.memo_list_id });
		memoList.setAdapter(memoAdapter);
		memoAdapter.notifyDataSetChanged();
	}

	/**
	 * ���ó����������������Ĳ˵�
	 */
	public void memoListLongClickListener() {
		memoList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView c = (TextView) view.findViewById(R.id.memo_list_id);
				memoItemSelected = c.getText().toString();
				selectedPosition = position;
				memoList.showContextMenu();
				return true;
			}
		});
		/**
		 * ���õ����������Ĳ˵�,�����������Ӧ�����Ĳ˵�onContextItemSelected
		 */
		memoList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("�Ƿ�ɾ�� ?");
				menu.add(0, 0, 0, "ɾ��");
				menu.add(0, 1, 1, "ȡ��");
			}
		});
	}

	/**
	 * ��Ӧ�����Ĳ˵���ִ����Ӧ����
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// setTitle("����˳����˵�����ĵ�" + item.getItemId() + "����Ŀ");
		System.out.println(selectedPosition);
		switch (item.getItemId()) {
		case 0:
			// ɾ������
			memoData.remove(selectedPosition);
			memoAdapter.notifyDataSetChanged();
			memoList.invalidate();
			MyDatabase db = new MyDatabase(this); // ������ݿ����
			db.deleteMemo(memoItemSelected);
			Toast.makeText(ShowTable.this, "ɾ�����", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			// ȡ������
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
}
