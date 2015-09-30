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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ShowTable extends Activity implements
		android.view.View.OnClickListener {
	private ListView listView;
private List<View> mViews = new ArrayList<View>();// 用来存放Tab01-03

	
	private Cursor cursor = null;
	Context mContext = null;

	String[] dayList = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

	private ViewPager mViewPager;// 用来放置界面切换
	private PagerAdapter mPagerAdapter;// 初始化View适配器
	
	// 3个Tab，每个Tab包含一个按钮
	private LinearLayout mTabClassTable;
	private LinearLayout mTabSetting;
	private LinearLayout mTabAbout;
	private Button restore, backup;
	// 3个按钮
	private ImageButton mClassTableImg;
	private ImageButton mSettingImg;
	private ImageButton mAboutImg;
private LayoutInflater mLayoutInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("欢迎查看课程表");
		mContext = this;

		setContentView(R.layout.activity_show_table);// 加载首页显示一周课程
		 mLayoutInflater = getLayoutInflater();
		 
		initView();
		
	listViewClick();

		

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
	initView();
	listViewClick();
		


	}
/**
 * ListView onItemClicklistener
 */
	private void listViewClick(){
		
		initViewPage();
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

				
					}
				});
		initEvent();
		

	}
	
	
	
	// 数据恢复
	private void dataRecover() {
	
		new BackupTask(this).execute("restroeDatabase");
	}

	// 数据备份
	private void dataBackup() {
	
		new BackupTask(this).execute("backupDatabase");
	}

	/**
	 * Initialize view/bottom menu
	 */

	private void initEvent() {
		mTabClassTable.setOnClickListener(this);
		mTabSetting.setOnClickListener(this);
		mTabAbout.setOnClickListener(this);
		backup.setOnClickListener(this);
		restore.setOnClickListener(this);
		
	

		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * ViewPage左右滑动时
			 */
			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				switch (currentItem) {
				case 0:
					resetImg();
					mPagerAdapter.notifyDataSetChanged();
					
					mClassTableImg
							.setImageResource(R.drawable.tab_classtable_pressed);

					break;
				case 1:
					resetImg();
					mSettingImg
							.setImageResource(R.drawable.tab_setting_pressed);

					break;
				case 2:
					resetImg();
					mAboutImg.setImageResource(R.drawable.tab_about_pressed);
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
	 * 初始化设置
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpage);
		// 初始化四个LinearLayout
		mTabClassTable = (LinearLayout) findViewById(R.id.id_tab_class);

		mTabAbout = (LinearLayout) findViewById(R.id.id_tab_about);
		mTabSetting = (LinearLayout) findViewById(R.id.id_tab_setting);
		// 初始化四个按钮
		mClassTableImg = (ImageButton) findViewById(R.id.id_tab_classtable_img);
		mSettingImg = (ImageButton) findViewById(R.id.id_tab_setting_img);
		mAboutImg = (ImageButton) findViewById(R.id.id_tab_about_img);

	}

	/**
	 * 初始化ViewPage
	 */
	private void initViewPage() {
		
		// Initialize 3 tabs and listview and button, don't it in the oncreate
	
	SpecialAdapter adapter;
		View tab01 = mLayoutInflater.inflate(R.layout.tab01, null);
		View tab02 = mLayoutInflater.inflate(R.layout.tab02, null);
		View tab03 = mLayoutInflater.inflate(R.layout.tab03, null);
		listView = (ListView) tab01.findViewById(R.id.daylist);// 显示一周列表
		
		List<Map<String, Object>> data=null;
		data = getData();
		
		
		
		
		adapter = new SpecialAdapter(this, data, R.layout.main_list_item,
				new String[] { "day", "morning", "afternoon", "evening" },
				new int[] { R.id.day, R.id.morning, R.id.afternoon,
						R.id.evening });
	
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.invalidate();
		backup = (Button) tab02.findViewById(R.id.dataBackup);
		restore = (Button) tab02.findViewById(R.id.dataRestore);
		mViews.clear();
		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab03);
		
		// 适配器初始化并设置
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
	 * 判断哪个要显示，及设置按钮图片
	 */
	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.id_tab_class:
			mViewPager.setCurrentItem(0);
			resetImg();
			mClassTableImg.setImageResource(R.drawable.tab_classtable_pressed);
			break;
		case R.id.id_tab_about:
			mViewPager.setCurrentItem(2);
			resetImg();
			mAboutImg.setImageResource(R.drawable.tab_about_pressed);
			break;
		case R.id.id_tab_setting:
			mViewPager.setCurrentItem(1);
			resetImg();
			mSettingImg.setImageResource(R.drawable.tab_setting_pressed);
			break;
			//Data backup
		case R.id.dataBackup:
			dataBackup();	
			View list=mViewPager.findViewWithTag("classTable");

			
			
			
			Toast.makeText(ShowTable.this, "数据已备份到ScheduleBackup目录下",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.dataRestore:
//data Recovery
			dataRecover();
		
			
			initView();
			listViewClick();

			Toast.makeText(ShowTable.this, "数据已恢复", Toast.LENGTH_SHORT).show();
			break;
		default:
			System.out.println(arg0.getId());
			break;
		}
	}

	/**
	 * 把所有图片变暗
	 */
	private void resetImg() {
		mClassTableImg.setImageResource(R.drawable.tab_classtable);
		mSettingImg.setImageResource(R.drawable.tab_setting);
		mAboutImg.setImageResource(R.drawable.tab_about);

	}

}
