package com.vera5.afgl;

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
		// FIXME Pick good set of frequencies
		add(db,528,"DNA repair",15);
		add(db,1830,"Eyesight sharpen",14);
		add(db,20,"Stiff shoulders",13);
		add(db,10000,"Multiple",12);
		add(db,1234,"Breathe",11);
		add(db,727,"Wound healing",10);
		add(db,47,"Regeneration and healing",9);
		add(db,646,"Hair",8);
		add(db,465,"Teeth",7);
		add(db,6000,"Calming",6);
		add(db,3176,"Hypertension",5);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public Cursor query(String sql) {
		return getWritableDatabase().rawQuery(sql,null);
	}

	public void add(String frequency,String tag,int cnt) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("INSERT INTO frequency (freq,tag,cnt) VALUES ("+frequency+",'"+tag+"',"+cnt+")");
		db.close();
	}

	// Overloaded
	public void add(SQLiteDatabase db,float frequency,String tag,int cnt) {
		// FIXME Avoid ' in the tag (will cause SQL error)
		db.execSQL("INSERT INTO frequency (freq,tag,cnt) VALUES ("+frequency+",'"+tag+"',"+cnt+")");
	}

	public void del(String frequency) {
		getWritableDatabase().execSQL("DELETE FROM frequency WHERE freq="+frequency);
	}

	protected void incCount(String freq) {
		getWritableDatabase().execSQL("UPDATE frequency SET cnt=cnt+1 WHERE freq="+freq);
	}
}
