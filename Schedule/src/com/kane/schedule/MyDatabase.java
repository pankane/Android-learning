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
		// ����һ����ΪclassList_db�����ݿ�
		super(context, DB_NAME, null, 1);

	}

	public MyDatabase(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// ִ��ʱ���������ڣ��򴴽�֮��ע��SQLite���ݿ��б�����һ��_id���ֶ���Ϊ�����������ѯʱ������

		db.execSQL(CREATE_CLASS_TABLE_SQL);
		
		db.execSQL(CREATE_MEMO_TABLE_SQL);
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+MEMO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+CLASS_TABLE_NAME);

	}

	/**���뱸��¼
	 * */
	
	public long insertMemo(String content, String date, String time) {
		// ʵ����һ�� SQLiteDatabase ����
		SQLiteDatabase db = this.getWritableDatabase();
		/*
		 * ����Ҫ�޸ĵ����ݷ��� ContentValues ������ ContentValues
		 * ���Լ�ֵ����ʽ�������ݣ����м������ݿ��������ֵ��������Ӧ������
		 */
		ContentValues cv = new ContentValues();
		cv.put(MEMO_TIME, time);
		cv.put(MEMO_CONTENT, content);
		cv.put(MEMO_DATE, date);
		// insert()�������������ݣ��ɹ��������������򷵻�-1
		long rowid = db.insert(MEMO_TABLE_NAME, null, cv);
		db.close();
		return rowid;
	}
	public Cursor selectMemo(){
		// ʵ����һ�� SQLiteDatabase ����
		SQLiteDatabase db = this.getReadableDatabase();				
		// ��ȡһ��ָ�����ݿ���α꣬������ѯ���ݿ�
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
		
		// update()���������������������ݿ⣬cv������º�����ݣ�whereΪ��������
		int numRow = db.update(MEMO_TABLE_NAME, cv, where, whereValues);
		db.close();
		return numRow;
	}
	
	public boolean deleteMemo(String id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = MEMO_ID+"=?";
		String[] whereValues = {id};
		// delete��������������ɾ�����ݣ�where��ʾɾ��������
		System.out.println(id);
		boolean is =  (db.delete(MEMO_TABLE_NAME, where, whereValues) > 0);
		db.close();
		return is;
	}
	
}
