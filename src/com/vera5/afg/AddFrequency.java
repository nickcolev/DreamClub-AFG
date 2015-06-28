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