
package com.southatlantic.sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import com.southatlantic.resources.Loadable;

import java.applet.AudioClip;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Class JlAudioClip. Implements the AudioClip-Interface for .mp3-files .
 * Uses javazoom.jl .
 */
public class JlAudioClip implements AudioClip, Runnable, Loadable {

	private InputStream is;
	private byte[] buffer = null;
	private Player player = null;
	
	/** The looped. */
	boolean looped = false;
	
	/**
	 * Instantiates a new jl audio clip.
	 * 
	 * @param _is the InputStream
	 */
	public JlAudioClip(InputStream _is) {
		is = _is;
	}
	
	/* (non-Javadoc)
	 * @see java.applet.AudioClip#loop()
	 */
	public void loop() {
		looped = true;
		playSound();
	}

	/* (non-Javadoc)
	 * @see java.applet.AudioClip#play()
	 */
	public void play() {
		looped = false;
		playSound();
	}
	
	private void playSound() {
		new Thread(this).start();
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {}
	}

	/* (non-Javadoc)
	 * @see java.applet.AudioClip#stop()
	 */
	public void stop() {
		if (player != null) {
			looped = false;
			player.close();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
//		int priority = Thread.currentThread().getPriority();
//		Thread.currentThread().setPriority(priority + 10);
		if (buffer == null) {
			load();
		}
		int counter = 0;
		while (looped || counter < 1) {
			counter ++;
			InputStream is = null;
			is = new ByteArrayInputStream(buffer);
			try {
				player = new Player(is);
//				Thread.currentThread().setPriority(priority);
				player.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.resources.Loadable#load()
	 */
	public void load() {
		if (buffer != null || is == null) {
			return;
		}
		try {
			ByteArrayOutputStream baos =  new ByteArrayOutputStream();
			int b;
			while ((b = is.read()) != -1) {
				baos.write(b);
			}
			buffer = baos.toByteArray();
			is = null;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
