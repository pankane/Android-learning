package com.kane.memo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.kane.schedule.MyDatabase;
import com.kane.schedule.R;
import com.kane.schedule.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MemoNew extends Activity {
	private TextView tvMemoTitleDate, tvMemoTitleWeek, tvMemoTitleTime;
	private GetCurrentDate getDate;
	private LinearLayout lvMemoTitle;
	private EditText mContent;
	private String mOrgContent, mOrgDate, mOrgTime, mID;
	static final public String sDATE = "date";
	static final public String sTIME = "time";
	static final public String sCONTENT = "content";
	static final public String sID = "_id";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setup title of this page to show the current date with time

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_memo_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.memo_title);
		mContent = (EditText) findViewById(R.id.memo_edit_editcontent);
		Intent intent = getIntent();
		
		// ������½��ʼǣ�content��id,date,time��Ϊnull
		mOrgContent = intent.getStringExtra(sCONTENT);
		// ֻ�����ݿⴢ�淽ʽ�Ŵ�����ֵ�����ݺ�id
		mOrgDate = intent.getStringExtra(sDATE);
		mOrgTime = intent.getStringExtra(sTIME);
		mID = intent.getStringExtra(sID);
		// ���title��content��ΪΪ�գ�������ʾ�ڿؼ���
		if (!TextUtils.isEmpty(mOrgContent)) {
			mContent.setText(mOrgContent);
		}
		if (!TextUtils.isEmpty(mOrgDate)) {
			initTitle(mOrgDate, mOrgTime);
		} else {
			getDate = new GetCurrentDate();

			initTitle(getDate.getDate(), getDate.getTime());
		}

	}

	public void initTitle(String setDate, String setTime) {
		// setup title style
		tvMemoTitleDate = (TextView) findViewById(R.id.memo_title_date);
		tvMemoTitleWeek = (TextView) findViewById(R.id.memo_title_week);
		tvMemoTitleTime = (TextView) findViewById(R.id.memo_title_time);
		lvMemoTitle = (LinearLayout) findViewById(R.id.memo_title_background);

		tvMemoTitleDate.setText(setDate);
		// tvMemoTitleWeek.setText(getDate.getWeek());
		tvMemoTitleTime.setText(setTime);
		tvMemoTitleDate.setTextColor(Color.parseColor("#ffffff"));
		tvMemoTitleWeek.setTextColor(Color.parseColor("#ecf1f2"));
		tvMemoTitleTime.setTextColor(Color.parseColor("#c3c7c8"));

		// setup title Backup ground color
		tvMemoTitleWeek.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		tvMemoTitleTime.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 7));
		lvMemoTitle.setBackgroundColor(Color.parseColor("#000000"));
		tvMemoTitleWeek.setVisibility(View.GONE);

	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   
        if (keyCode == KeyEvent.KEYCODE_BACK
                  && event.getRepeatCount() == 0) {
        	if( saveMemo()==1 ){
				Toast.makeText(MemoNew.this, 		// ��ʾ����ɹ�
						"����¼�Ѹ���", Toast.LENGTH_SHORT).show();
				finish();									// �رյ�ǰActivity
			} else if(saveMemo()==2){
				Toast.makeText(MemoNew.this, 		// ��ʾ����ʧ��
						"����¼����ʧ��", Toast.LENGTH_SHORT).show();
			}else if (saveMemo()==0)
			{Toast.makeText(MemoNew.this, //��ʾû������
					"δ�����κ����ݣ�", Toast.LENGTH_SHORT).show();
				}
        	finish();
			              return true;
			            
          }else
          {
          return super.onKeyDown(keyCode, event);}
    
      }
	
	
	private int saveMemo() {
		int isSucceed = 1;
		String date = tvMemoTitleDate.getText()+"";
		String time=tvMemoTitleTime.getText()+"";
		String content = mContent.getText()+"";
		if(TextUtils.isEmpty(mContent.getText())) {
			
			return 0;
		}
		MyDatabase dbHelper = new MyDatabase(MemoNew.this);
		
		if(TextUtils.isEmpty(mID)) {		// �ж��ǲ��룬���Ǳ༭�ʼ�
			if( dbHelper.insertMemo(content, date,time) == -1) {
				isSucceed = 2;
			}
		}else{
			if( dbHelper.updateMemo(mID, date, content,time) < 1) {
				isSucceed = 2;
			}
		}

		return isSucceed;
		}


}
