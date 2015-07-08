package com.vera5.afgl;

import android.app.ListActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;

public class MainActivity extends ListActivity {

  protected EditText freq;
  protected static final float F_MIN = 0.5f;		// min allowed frequency
  protected static final float F_MAX = 15000f;	// max -"-
  private SharedPreferences sp;
  private MyAdapter adapter;
  private MyDatabase db;
  private MyGenerator gen;
  private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		freq = (EditText) findViewById(R.id.freq);
		freq.setText(sp.getString("freq", "528"));
		gen = new MyGenerator(this);
        lv = getListView();
        db = new MyDatabase(this);
        Cursor curs = db.query("SELECT rowid AS _id,freq,tag FROM frequency ORDER BY cnt DESC");
        startManagingCursor(curs);
        adapter = new MyAdapter(this,curs);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
				Cursor curs = (Cursor) adapter.getItem(position);
				freq.setText(Lib.f2s(curs.getFloat(1)));
			}
		});
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long id) {
				Cursor curs = (Cursor) adapter.getItem(position);
				Remove(Lib.f2s(curs.getFloat(1)));
				return true;
			}
		});
		lv.setAdapter(adapter);
    }

	@Override
	public void onPause() {
		// Save frequency
		Editor edit = sp.edit();
		edit.putString("freq",freq.getText().toString());
		edit.commit();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.add:
				startActivity(new Intent(".AddFrequency"));
				return true;
			case R.id.del:
				Remove(freq.getText().toString());
				return true;
			case R.id.settings:
				startActivity(new Intent(".MySettings"));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void Remove(final String freq) {
		new AlertDialog.Builder(this)
			//.setTitle("Remove "+freq+" Hz?")
			.setMessage("Remove "+freq+"?")
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					db.del(freq);
					adapter.getCursor().requery();
				}
			}).show();
	}

	public void Settings(View view) {
		startActivity(new Intent(this, MySettings.class));
	}

	public void Ctrl(View view) {
		if(gen.running)
			stop();
		else
			if (Lib.inRange(this,freq.getText().toString())) play();
	}

	private void setCtrl(boolean mode) {
		ImageButton btn = (ImageButton) findViewById(R.id.ctrl);
		btn.setImageResource(mode ? R.drawable.ic_action_stop : R.drawable.ic_action_play);
	}

	private void setSleep() {
		if(sp.getString("sleep", "").length() == 0) return;
		try {
			final int seconds = 1000 * Integer.parseInt(sp.getString("sleep", "90"));
			new CountDownTimer(seconds,seconds) {
				public void onTick(long millisUntilFinished) {}
				public void onFinish() { stop(); }
			}.start();
		} catch (NumberFormatException e) {
		}
	}

	private void play() {
		setSleep();
		if (gen.play(freq.getText().toString(),sp.getInt("wave",R.id.sine))) {
			setCtrl(true);
			// Touch cnt
			db.incCount(freq.getText().toString());
			adapter.getCursor().requery();
		}
	}

	private void stop() {
		gen.stop();
		setCtrl(false);
	}

}
