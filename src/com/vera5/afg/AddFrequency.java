package com.vera5.afg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class AddFrequency extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
	}

	@Override
	public void onPause() {
		
		EditText etFreq = (EditText) findViewById(R.id.freq);
		float freq = Lib.s2f(etFreq.getText().toString());
		if (freq < MainActivity.F_MIN || freq > MainActivity.F_MAX) {
			Lib.Tooltip(this,"Freq out of bound \n(allowed "+Lib.f2s(MainActivity.F_MIN)+" to "+Lib.f2s(MainActivity.F_MAX)+" Hz)");
		}
		EditText etTag  = (EditText) findViewById(R.id.tag);
		if (etFreq.getText().toString().length() > 0 &&
			etTag.getText().toString().length() > 0) {
			// Add the frequency if both freq and tag entered
			MyDatabase db = new MyDatabase(this);
			db.add(etFreq.getText().toString(),etTag.getText().toString(),0);
		}
		super.onPause();
	}

}
