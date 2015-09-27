package com.kane.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabase extends SQLiteOpenHelper {

	public MyDatabase(Context context, String name, CursorFactory factory,
			int version) {
		// ����һ����ΪclassList_db�����ݿ�
		super(context, "classList_db", null, 1);
		// TODO Auto-generated constructor stub
	}

	public MyDatabase(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// ִ��ʱ���������ڣ��򴴽�֮��ע��SQLite���ݿ��б�����һ��_id���ֶ���Ϊ�����������ѯʱ������
		String sql = "create table classTable (_id integer primary key autoincrement, day text, classTitle text, " +
				"address text, startTime varchar(20), endTime varchar(20), classHour int)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	
	

}
