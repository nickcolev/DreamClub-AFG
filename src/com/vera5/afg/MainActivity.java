package com.vera5.afg;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MainActivity extends Activity {

  private WebView myWebView;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		myWebView = (WebView) findViewById(R.id.webview);
		// Enable JavaScript
		myWebView.getSettings().setJavaScriptEnabled(true);
		// Enable Alert
		myWebView.setWebChromeClient(new WebChromeClient());
		// Bind JS and Android code
		myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
		// Load a HTML from assets
        myWebView.loadUrl("file:///android_asset/main.htm");
	}
}
