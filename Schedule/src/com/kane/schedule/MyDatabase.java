package com.kane.schedule;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabase extends SQLiteOpenHelper {
	private final static String MEMO_TABLE_NAME = "memoTable";
	private final static String CLASS_TABLE_NAME = "classTable";
	private final static String DB_NAME = "classList_db";
	public final static String MEMO_ID = "_id";
	public final static String MEMO_CONTENT = "content";
	public final static String MEMO_TIME = "time";
	public final static String MEMO_DATE = "date";
	public final static String CLASS_TABLE_ID = "_id";
	public final static String CLASS_TABLE_DAY = "day";
	public final static String CLASS_TABLE_TITLE = "classTitle";
	public final static String CLASS_TABLE_ADDRESS = "address";
	public final static String CLASS_TABLE_STARTTIME = "startTime";
	public final static String CLASS_TABLE_ENDTIME = "endTime";
	public final static String CLASS_TABLE_CLASSHOUR = "classHour";
	private static final String CREATE_MEMO_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
			+ MEMO_TABLE_NAME + " (" + MEMO_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + MEMO_CONTENT + " TEXT,"
			+ MEMO_TIME + " int," + MEMO_DATE + " int)";

	private static final String CREATE_CLASS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
			+ CLASS_TABLE_NAME + " (" + CLASS_TABLE_ID
			+ "ID INTEGER PRIMARY KEY AUTOINCREMENT," + CLASS_TABLE_DAY
			+ " TEXT,"+ CLASS_TABLE_TITLE+"  TEXT, " + CLASS_TABLE_ADDRESS + " TEXT,"
			+ CLASS_TABLE_STARTTIME + " VARCHAR(20)," + CLASS_TABLE_ENDTIME
			+ " VARCHAR(20), " + CLASS_TABLE_CLASSHOUR + " INT)";

	public MyDatabase(Context context) {
		// 创建一个名为classList_db的数据库
		super(context, DB_NAME, null, 1);

	}

	public MyDatabase(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// 执行时，若表不存在，则创建之，注意SQLite数据库中必须有一个_id的字段作为主键，否则查询时将报错

		db.execSQL(CREATE_CLASS_TABLE_SQL);
		
		db.execSQL(CREATE_MEMO_TABLE_SQL);
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+MEMO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+CLASS_TABLE_NAME);

	}

	/**插入备忘录
	 * */
	
	public long insertMemo(String content, String date, String time) {
		// 实例化一个 SQLiteDatabase 对象
		SQLiteDatabase db = this.getWritableDatabase();
		/*
		 * 将需要修改的数据放在 ContentValues 对象中 ContentValues
		 * 是以键值对形式储存数据，其中键是数据库的列名，值是列名对应的数据
		 */
		ContentValues cv = new ContentValues();
		cv.put(MEMO_TIME, time);
		cv.put(MEMO_CONTENT, content);
		cv.put(MEMO_DATE, date);
		// insert()方法：插入数据，成功返回行数，否则返回-1
		long rowid = db.insert(MEMO_TABLE_NAME, null, cv);
		db.close();
		return rowid;
	}
	public Cursor selectMemo(){
		// 实例化一个 SQLiteDatabase 对象
		SQLiteDatabase db = this.getReadableDatabase();				
		// 获取一个指向数据库的游标，用来查询数据库
		Cursor cursor = db.query(MEMO_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
	
	public int updateMemo(String id,String date, String content,String time){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = MEMO_ID+"=?";
		String[] whereValues = {id};
		ContentValues cv = new ContentValues();
		cv.put(MEMO_DATE, date);
		cv.put(MEMO_CONTENT, content);
		cv.put(MEMO_TIME, time);
		
		// update()方法：根据条件更新数据库，cv保存更新后的数据，where为更新条件
		int numRow = db.update(MEMO_TABLE_NAME, cv, where, whereValues);
		db.close();
		return numRow;
	}
	
	public boolean deleteMemo(String id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = MEMO_ID+"=?";
		String[] whereValues = {id};
		// delete方法：根据条件删除数据，where表示删除的条件
		System.out.println(id);
		boolean is =  (db.delete(MEMO_TABLE_NAME, where, whereValues) > 0);
		db.close();
		return is;
	}
	
}
