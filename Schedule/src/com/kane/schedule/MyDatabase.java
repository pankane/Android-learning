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
		// 创建一个名为classList_db的数据库
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

		// 执行时，若表不存在，则创建之，注意SQLite数据库中必须有一个_id的字段作为主键，否则查询时将报错
		String sql = "create table classTable (_id integer primary key autoincrement, day text, classTitle text, " +
				"address text, startTime varchar(20), endTime varchar(20), classHour int)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	
	

}
