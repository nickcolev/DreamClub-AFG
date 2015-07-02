package com.vera5.afg;

import android.content.Context;
import android.util.Log;
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
			f = -1;
		}
		return f;
	}

	// FIXME Next 2 fn could be simplified, like 'chkRange'
	protected static void eRange(Context context) {
		Tooltip(context,"Freq out of bound \n(allowed "+Lib.f2s(MainActivity.F_MIN)+" to "+Lib.f2s(MainActivity.F_MAX)+" Hz)");
	}

	protected static boolean inRange(String frequency) {
		float freq = s2f(frequency);
Log.d("***", "freq="+freq+", min: "+MainActivity.F_MIN+", max: "+MainActivity.F_MAX);
		return (MainActivity.F_MIN > freq || freq > MainActivity.F_MAX ?
			false : true);
	}

	protected static void Tooltip(Context context,String message) {
		Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	}
}
