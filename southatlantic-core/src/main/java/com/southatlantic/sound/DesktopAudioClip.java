
package com.southatlantic.sound;

import javax.sound.sampled.*;
import java.applet.AudioClip;
import java.io.InputStream;

/**
 * The Class DesktopAudioClip. Implements the java.applet.AudioClip interface for
 * .wav files in desktop-applications.
 */
public class DesktopAudioClip implements AudioClip, LineListener, Runnable {

	private Clip clip = null;
	private boolean looped = false;
	
	/**
	 * Instantiates a new desktop audio clip.
	 * 
	 * @param sound_stream the sound_stream
	 */
	public DesktopAudioClip(InputStream sound_stream) {
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(sound_stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ais != null) {
			AudioFormat format = ais.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			try {
				clip = (Clip) AudioSystem.getLine(info);
				clip.addLineListener(this);
				clip.open(ais);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.applet.AudioClip#loop()
	 */
	public void loop() {
		if (clip != null) {
			looped = true;
			playSound();
		}
	}

	/* (non-Javadoc)
	 * @see java.applet.AudioClip#play()
	 */
	public void play() {
		if (clip != null) {
			looped = false;
			playSound();
		}
	}

	/* (non-Javadoc)
	 * @see java.applet.AudioClip#stop()
	 */
	public void stop() {
		if (clip != null) {
			looped = false;
			clip.stop();
		}
	}

	/* (non-Javadoc)
	 * @see javax.sound.sampled.LineListener#update(javax.sound.sampled.LineEvent)
	 */
	public void update(LineEvent le) {
		
	}

	private void playSound() {
		new Thread(this).start();
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (clip != null) {
			int counter = 0;
			while (looped || counter < 1) {
				counter ++;
				clip.loop(1);
			}
		}
	}

}
