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
		/* FIXME Pick good set of frequencies
			Top 10 according to 'words.php' are
			880 (330)	General antiseptic 
			787 (288)	Depression
			727 (284)
			20 (229)
			1550 (171)	Bone_trauma
			802 (168)
			10000 (164)
			465 (122)
			776 (117)	Toothache
			728 (98)	Dental general
		*/
		// Insert frequencies in reverse order, so the last to appear at the top initially
		add(db,10025,"Cancer (general)",0);
		add(db,3176,"Hypertension",0);
		add(db,727,"Stiff shoulders",0);
		add(db,728,"Teeth",0);
		add(db,646,"Hair",0);
		add(db,190,"Burns",0);
		add(db,1550,"Bone trauma",0);
		add(db,2720,"Wound healing",0);
		add(db,880,"General antiseptic",0);
		add(db,787,"Depression",0);
		add(db,10000,"Multiple",0);
		add(db,6000,"Calming",0);
		add(db,1234,"Breathe (deep)",0);
		add(db,1830,"Eyesight sharpen",0);
		add(db,528,"DNA repair",0);
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
