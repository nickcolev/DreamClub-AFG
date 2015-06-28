package com.vera5.afg;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.widget.Toast;
import java.lang.IllegalStateException;

public class MyGenerator {

  private final Context context;
  private final int duration = 1; 		// seconds
  private final int sampleRate = 44100;
  private final int numSamples = duration * sampleRate;
  private AudioTrack track;
  protected boolean running = false;

	public MyGenerator(Context context) {
		this.context = context;
	}

	private void Tooltip(String s) {
		Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
	}

    public boolean play(String freq,int wave) {
		boolean ok = false;
		float f = 0;
		try {
			f = Float.parseFloat(freq);
		} catch (Exception e) {
			Tooltip(e.getMessage());
		}
		if (f > 0) {
			switch(wave){
				case R.id.square:
					track = genSquare(f);
					break;
				case R.id.triangle:
					track = genTriangle(f);
					break;
				case R.id.sawtooth:
					track = genSawtooth(f);
					break;
				default:
					track = genSine(f);
			}
			track.play();
			ok = running = true;
		}
		return ok;
    }

	public void stop() {
		try {
			track.stop();
			track.release();
			track = null;
			running = false;
		} catch (IllegalStateException e) {
			Tooltip(e.getMessage());
		}
	}

	private AudioTrack setTrack(short[] buffer,int minIdx,int end) {
		AudioTrack track = new AudioTrack(
			AudioManager.STREAM_MUSIC,
			sampleRate,
			AudioFormat.CHANNEL_OUT_MONO,
			AudioFormat.ENCODING_PCM_16BIT,
			numSamples,
			AudioTrack.MODE_STATIC);
		track.write(buffer, 0, buffer.length);
		// Start/End are in frames (see http://developer.android.com/reference/android/media/AudioTrack.html#setLoopPoints(int, int, int))
		if (minIdx > 0) end = minIdx;
		if (end == 0) end = buffer.length / 2;
		track.setLoopPoints(0, end, -1);
		return track;
	}

	private AudioTrack genSine(float freq) {
		// fill out the array
		double angle = 0,
			f = (2 * Math.PI) * freq / sampleRate;
		int end = 0,
			minValue = Short.MAX_VALUE,
			minIdx = 0;
		short[] buffer = new short[numSamples];	// to be even bytes (see 'audioBuffSizeCheck()' body at http://en.sourceforge.jp/projects/gb-231r1-is01/scm/git/Gingerbread_2.3.3_r1_IS01/blobs/master/frameworks/base/media/java/android/media/AudioRecord.java)
		for (int i=0; i<buffer.length; i++) {
			buffer[i] = (short)(Short.MAX_VALUE * ((float) Math.sin(angle)));
			angle += f;
			if (i > 5)	// Spot the lowest amplitude
				if (buffer[i-1] < 0 && buffer[i] > 0) {
					if (minValue > buffer[i]) {
						minValue = buffer[i];
						minIdx = i;
					}
				}
		}
		return setTrack(buffer,minIdx,end);
	}

	// FIXME Under development below
	private AudioTrack genSquare(float f) {
Log.d("***GEN***", "Square under development (using sine)");
		return genSine(f);
	}
	private AudioTrack genTriangle(float f) {
Log.d("***GEN***", "Triangle under development (using sine)");
		return genSine(f);
	}
	private AudioTrack genSawtooth(float f) {
Log.d("***GEN***", "Sawtooth under development (using sine)");
		return genSine(f);
	}
}