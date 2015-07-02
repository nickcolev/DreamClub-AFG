package com.vera5.afg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class AddFrequency extends Activity {

  private EditText etFreq, etTag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		etFreq = (EditText) findViewById(R.id.freq);
		etTag  = (EditText) findViewById(R.id.tag);
	}

	@Override
	public void onBackPressed() {
		float freq = Lib.s2f(etFreq.getText().toString());
		String s = etFreq.getText().toString();
		if (s.length() > 0 && etTag.getText().toString().length() > 0) {
			if (!Lib.inRange(s)) {
				Lib.eRange(this);
				return;
			}
			// Add the frequency if both freq and tag entered
			MyDatabase db = new MyDatabase(this);
			db.add(etFreq.getText().toString(),etTag.getText().toString(),0);
		}
		super.onBackPressed();
	}

}
