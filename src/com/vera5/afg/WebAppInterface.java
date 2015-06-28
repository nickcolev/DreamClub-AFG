package com.vera5.afg;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.widget.Toast;

public class WebAppInterface {

  private final MainActivity mParent;
  private final int duration = 1; 		// seconds
  private final int sampleRate = 44100;
  private final int numSamples = duration * sampleRate;
  private final short[] buffer = new short[numSamples];	// to be even bytes (see 'audioBuffSizeCheck()' body at http://en.sourceforge.jp/projects/gb-231r1-is01/scm/git/Gingerbread_2.3.3_r1_IS01/blobs/master/frameworks/base/media/java/android/media/AudioRecord.java)
  private AudioTrack track;

	WebAppInterface(MainActivity ma) {
        mParent = ma;
	}

	private void Tooltip(String s) {
		Toast.makeText(mParent, s, Toast.LENGTH_SHORT).show();
	}

    public boolean play(String freq,String wave) {
Log.d("***", "wave="+wave);
		boolean ok = false;
		float f = 0;
		try {
			f = Float.parseFloat(freq);
		} catch (Exception e) {
			Tooltip(e.getMessage());
		}
		if (f > 0) {
			genSine(f);
			track.play();
			ok = true;
		}
		return ok;
    }

	public void stop() {
		track.stop();
		track.release();
		track = null;
	}

	void genSine(float freq) {
		// fill out the array
		double angle = 0,
			f = (2 * Math.PI) * freq / sampleRate;
		int end = 0,
			minValue = Short.MAX_VALUE,
			minIdx = 0;
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
        track = new AudioTrack(
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
	}

	public String getSamleFrequencies() {
		// FIXME Read these from db/cache/cookie/etc
		return "100\tTest";
	}

	public String getUserFrequencies() {
		// FIXME These could be in a resource
		return "528\tDNA repair\n"
			+ "1830\tEyesight sharpen\n"
			+ "20\tStiff shoulders\n"
			+ "10K\tMultiple\n"
			+ "1234\tBreathe\n"
			+ "650\tNumbness\n"
			+ "727\tWound healing\n"
			+ "47\tHealing and Regeneration\n"
			+ "646\tHair\n"
			+ "465\tTeeth\n"
			+ "10.4\tTumor benign\n"
			+ "9.19\tStiff neck";
	}

}
