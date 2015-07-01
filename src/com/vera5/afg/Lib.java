package com.vera5.afg;

import android.content.Context;
import android.widget.Toast;
import java.text.DecimalFormat;

public class Lib {

	protected static String f2s(float f) {
		return new DecimalFormat("#.##").format(f);
	}

	protected static float s2f(String s) {
		float f;
		try {
			f = Float.parseFloat(s);
		} catch (Exception e) {
			f = 150;	// Default
		}
		return f;
	}

	protected static boolean inRange(String freq) {
		// TODO to be continued
		return true;
	}

	protected static void Tooltip(Context context,String message) {
		Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	}
}
