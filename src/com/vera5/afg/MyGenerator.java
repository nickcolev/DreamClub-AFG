package com.vera5.afg;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import java.lang.IllegalStateException;

public class MyGenerator {

  private final Context context;
  private final int duration = 1; 		// seconds
  private final int sampleRate = 44100;
  private final int numSamples = duration * sampleRate;
  private int wave = R.id.sine;
  private AudioTrack track = null;
  protected boolean running = false;

  // Instance data
  private int periodSamples;
  private long sampleNumber;

	public MyGenerator(Context context) {
		this.context = context;
	}

    public boolean play(String freq,int wave) {
		boolean ok = false;
		float f = 0;
		try {
			f = Float.parseFloat(freq);
		} catch (Exception e) {
			Lib.Tooltip(context,e.getMessage());
		}
		if (f > 0) {
			this.wave = wave;
			setTrack(getSamples(f));
			track.play();
			ok = running = true;
		}
		return ok;
    }

	public void stop() {
		if (track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
			try {
				track.stop();
				track.release();
				running = false;
			} catch (IllegalStateException e) {
				Lib.Tooltip(context,e.getMessage());
			}
	}

	private void setTrack(short[] buffer) {
		track = new AudioTrack(
			AudioManager.STREAM_MUSIC,
			sampleRate,
			AudioFormat.CHANNEL_OUT_MONO,
			AudioFormat.ENCODING_PCM_16BIT,
			buffer.length,
			AudioTrack.MODE_STATIC);
		track.write(buffer, 0, buffer.length);
		// Calc loop end point
		int n = 2048 / periodSamples;
		int k = n * periodSamples;
		int end = k == 2048 ? k - periodSamples : k;
//Log.d("***", "n="+n+", k="+k+", end="+end);
		track.setPlaybackHeadPosition(0);
		track.setLoopPoints(0, end, -1);
	}

	// @return Next oscillator sample
	protected double getSample() {
		double value, x = sampleNumber / (float) periodSamples;
		switch (wave) {
			default:
			case R.id.triangle:	// FIXME To be continued
			case R.id.sine:
				value = Math.sin(2.0 * Math.PI * x);
				break;
			case R.id.square:
				value = sampleNumber < (periodSamples / 2) ? 1.0 : -1.0;
				break;
			case R.id.sawtooth:
				value = 2.0 * (x - Math.floor(x + 0.5));
				break;
		}
		sampleNumber = (sampleNumber + 1) % periodSamples;
		return value;
	}
     
	protected short[] getSamples(float frequency) {
		periodSamples = (int)(sampleRate / frequency);
		//int size = periodSamples * 2;
		int size = AudioTrack.getMinBufferSize(sampleRate,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
		short[] buffer = new short[size];
		int index = 0;
		for (int i=0; i<size; i++) {
			double ds = getSample() * Short.MAX_VALUE;
			short ss = (short) Math.round(ds);
			buffer[index++] = ss;
		}
		return buffer;
  }

}