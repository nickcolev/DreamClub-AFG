package com.vera5.afgl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class About extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		WebView wv = (WebView) findViewById(R.id.about);
		wv.loadUrl("file:///android_asset/about.htm");
	}

}
