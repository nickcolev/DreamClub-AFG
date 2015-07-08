package com.vera5.afgl;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import java.lang.IllegalStateException;

public class MyGenerator {

  private final Context context;
  private final int sampleRate = 48000;
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
		this.wave = wave;
		float f = Lib.s2f(freq);
		setTrack(getSamples(f));
		track.play();
		running = true;
		return running;
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
		track.setPlaybackHeadPosition(0);
		track.setLoopPoints(0, loopEnd(buffer), -1);
	}

	private int loopEnd(short[] buffer) {
		int size = buffer.length / 2,
			end = (size / periodSamples) * periodSamples;
Log.d("***", "end="+end);
		if (end == 0) end = size;
		return end;
	}

	// @return Next oscillator sample
	protected double getSample() {
		double value, x = sampleNumber / (float) periodSamples;
		switch (wave) {
			default:
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
		int size = buffSize(frequency);
		//int size = 2 * AudioTrack.getMinBufferSize(sampleRate,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
Log.d("***", "f: "+frequency+", periodSamples: "+periodSamples+", bufSize: "+size);
		short[] buffer = new short[size];
		int index = 0;
		for (int i=0; i<size; i++) {
			double ds = getSample() * Short.MAX_VALUE;
			short ss = (short) Math.round(ds);
			buffer[index++] = ss;
		}
		return buffer;
	}

	private int buffSize(float freq) {
		if(freq > 4) return sampleRate;
		if(freq > 2) return 2*sampleRate;
		return 4*sampleRate;
	}

}