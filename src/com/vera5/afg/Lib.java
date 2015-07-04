package com.vera5.afgl;

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

	protected static boolean inRange(Context context, String frequency) {
		float min = 1f,
			max = 15000f,
			freq = s2f(frequency);
		if (freq < min || freq > max) {
			Tooltip(context,"Freqency out of bound \n(allowed "+f2s(min)+" ÷ "+f2s(max)+" Hz)");
			return false;
		}
		return true;
	}

	protected static void Tooltip(Context context,String message) {
		Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	}
}
