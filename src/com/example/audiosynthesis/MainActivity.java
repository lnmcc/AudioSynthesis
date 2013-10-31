package com.example.audiosynthesis;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button startSound;
	Button endSound;

	AudioSynthesisTask audioSynth;
	boolean keepGoing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startSound = (Button)findViewById(R.id.StartSound);
		endSound = (Button)findViewById(R.id.EndSound);
		
		startSound.setOnClickListener(this);
		endSound.setOnClickListener(this);
		
		endSound.setEnabled(false);
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		keepGoing = false;
		endSound.setEnabled(false);
		startSound.setEnabled(true);
	}
	
	@Override
	public void onClick(View v) {

		if(v == startSound) {
			keepGoing = true;
			audioSynth = new AudioSynthesisTask();
			audioSynth.execute();
			
			endSound.setEnabled(false);
			startSound.setEnabled(true);
		} else if(v == endSound) {
			keepGoing = false;
			endSound.setEnabled(false);
			startSound.setEnabled(true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class AudioSynthesisTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			final int SAMPLE_RATE = 11025;

			int minSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);

			AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, minSize,
					AudioTrack.MODE_STREAM);

			audioTrack.play();
			
			short[] buffer = {
					8130, 15752, 22389, 27625, 31134, 32695, 32210, 29711,
					25354, 19410, 12253, 4329, -3865, -11818, -19032, -25055
			};
			
			while(keepGoing) {
				audioTrack.write(buffer, 0, buffer.length);
			}
			
			return null;
		}
	}

}
