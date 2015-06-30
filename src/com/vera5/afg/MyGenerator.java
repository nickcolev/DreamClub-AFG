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
  private int wave = R.id.sine;
  private AudioTrack track;
  private int loopEnd = 0;
  protected boolean running = false;

  // Instance data
  private long periodSamples;
  private long sampleNumber;

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
			this.wave = wave;
			loopEnd = 0;
			setFrequency(f);    
			track = genSine(f);
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

	private AudioTrack setTrack(short[] buffer) {
		AudioTrack track = new AudioTrack(
			AudioManager.STREAM_MUSIC,
			sampleRate,
			AudioFormat.CHANNEL_OUT_MONO,
			AudioFormat.ENCODING_PCM_16BIT,
			numSamples,
			AudioTrack.MODE_STATIC);
		if (loopEnd == 0) loopEnd = buffer.length / 4;
		track.write(buffer, 0, buffer.length);
		track.setLoopPoints(0, (int)periodSamples, -1);
		return track;
	}

	private AudioTrack genSine(float freq) {
		return setTrack(getSamples());
	}

	public void setFrequency(float frequency) {
		periodSamples = (long)(sampleRate / frequency);
	}
 
	// @return Next oscillator sample
	protected double getSample() {
		double value, x = sampleNumber / (double) periodSamples;
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
     
	protected short[] getSamples() {
		short[] buffer = new short[numSamples];
		int index = 0;
		for (int i = 0; i < numSamples; i++) {
			double ds = getSample() * Short.MAX_VALUE;
			short ss = (short) Math.round(ds);
			buffer[index++] = ss;
		}
		return buffer;
  }

}