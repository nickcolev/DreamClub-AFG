package com.vera5.afg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySettings extends Activity {

  private EditText mSleep;
  private RadioGroup mWave;
  private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		mWave = (RadioGroup) findViewById(R.id.waves);
		int iwave = sp.getInt("wave",R.id.sine);
		mWave.check(iwave == -1 ? R.id.sine : iwave);
        mSleep = (EditText) findViewById(R.id.sleep);
		mSleep.setText(sp.getString("sleep", "90"));
	}

	@Override
	public void onPause() {
		// Save settings
		Editor edit = sp.edit();
		edit.putString("sleep",mSleep.getText().toString());
		edit.putInt("wave",mWave.getCheckedRadioButtonId());
		edit.commit();
		super.onPause();
	}
}