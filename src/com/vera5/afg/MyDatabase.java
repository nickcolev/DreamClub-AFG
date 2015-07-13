package com.vera5.afg;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {

  private final static int DB_VERSION = 1;
  private final static String DB_NAME = "afg.db";
  private Context context;

	public MyDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS frequency (freq FLOAT,cnt INT DEFAULT 0,tag VARCHAR(40))");
		db.execSQL("CREATE INDEX IF NOT EXISTS frequency_cnt ON frequency(cnt)");
		// Insert frequencies in reverse order, so the last to appear at the top initially
		add(db,10025,"Cancer (general)");
		add(db,3176,"Hypertension");
		add(db,727,"Stiff shoulders");
		add(db,728,"Teeth");
		add(db,646,"Hair");
		add(db,190,"Burns");
		add(db,1550,"Bone trauma");
		add(db,2720,"Wound healing");
		add(db,880,"General antiseptic");
		add(db,787,"Depression");
		add(db,10000,"Multiple");
		add(db,6000,"Calming");
		add(db,1234,"Breathe (deep)");
		add(db,1830,"Eyesight sharpen");
		add(db,528,"DNA repair");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public Cursor query(String sql) {
		return getWritableDatabase().rawQuery(sql,null);
	}

	public void add(String frequency,String tag,int cnt) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("INSERT INTO frequency (freq,tag,cnt) VALUES ("+frequency+",'"+escape(tag)+"',"+cnt+")");
		db.close();
	}

	// Overloaded
	public void add(SQLiteDatabase db,float frequency,String tag) {
		db.execSQL("INSERT INTO frequency (freq,tag) VALUES ("+frequency+",'"+escape(tag)+"')");
	}

	public void del(String frequency) {
		getWritableDatabase().execSQL("DELETE FROM frequency WHERE freq="+frequency);
	}

	protected void incCount(String freq) {
		getWritableDatabase().execSQL("UPDATE frequency SET cnt=cnt+1 WHERE freq="+freq);
	}

	private String escape(String s) {
		return s.replace("'","''");
	}

}
