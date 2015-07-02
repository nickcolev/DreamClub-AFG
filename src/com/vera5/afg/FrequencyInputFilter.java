package com.vera5.afg;
// FIXME Not necessary anymore
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class FrequencyInputFilter implements OnFocusChangeListener {

  private final EditText freq;

	public FrequencyInputFilter(Context context, EditText et) {
		this.freq = et;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
	// When focus is lost check that the text field has valid values.
		if (!hasFocus) {
			// Validate input
Log.d("***", "Focus lost. Text: "+freq.getText().toString());
		}
	}

}
