package com.vera5.afgl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {
  private final Context context;

	public MyAdapter(Context context, Cursor cursor) {
		super(context,cursor);
		this.context = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.row, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tvFreq = (TextView) view.findViewById(R.id.frequency);
		TextView tvTag  = (TextView) view.findViewById(R.id.tag);
		float freq = cursor.getFloat(cursor.getColumnIndexOrThrow("freq"));
		String tag = cursor.getString(cursor.getColumnIndexOrThrow("tag"));
		tvFreq.setText(Lib.f2s(freq));
		tvTag.setText(tag);
	}

}