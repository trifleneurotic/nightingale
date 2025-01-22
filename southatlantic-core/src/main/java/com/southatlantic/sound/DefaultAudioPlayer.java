
package com.southatlantic.sound;

import com.southatlantic.resources.ResourceManager;

import java.applet.AudioClip;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * The Class DefaultAudioPlayer. Simple audio player, handles .wav (etc.) and .midi .
 */
public class DefaultAudioPlayer implements AudioPlayer {

	/** The ResourceManager. */
	protected ResourceManager res_mng;
	
	/** The audioclips. */
	protected Hashtable audioclips = new Hashtable();
	
	private boolean disabled = false;
	
	/**
	 * Instantiates a new default audio player.
	 * 
	 * @param _res_mng the ResourceManager
	 */
	public DefaultAudioPlayer(ResourceManager _res_mng) {
		res_mng = _res_mng;
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.sound.AudioPlayer#playSound(java.lang.String, boolean)
	 */
	public void playSound(String file_name, boolean looped) {
		if (!disabled) {
			AudioClip ac = (AudioClip)audioclips.get(file_name);
			if (ac == null) {
				try {
					ac = createAudioClip(file_name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ac != null) {
				if (looped)
					ac.loop();
				else
					ac.play();
			}
		}
	}
	
	/**
	 * Creates the audio clip.
	 * 
	 * @param file_name the file_name
	 * 
	 * @return the audio clip
	 * @throws IOException 
	 */
	protected AudioClip createAudioClip(String file_name) throws IOException {
		AudioClip ac = res_mng.getAudioClipByFileName(file_name);
		if (ac != null) {
			audioclips.put(file_name, res_mng.getAudioClipByFileName(file_name));
		}
		return ((AudioClip)audioclips.get(file_name));
	}

	/* (non-Javadoc)
	 * @see southatlantic.sound.AudioPlayer#stopSounds()
	 */
	public void stopSounds() {
		Iterator it = audioclips.values().iterator();
		while (it.hasNext()) {
			((AudioClip)it.next()).stop();
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.sound.AudioPlayer#stopSound(java.lang.String)
	 */
	public void stopSound(String file_name) {
		AudioClip ac = (AudioClip)audioclips.get(file_name);
		if (ac != null) {
			ac.stop();
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.sound.AudioPlayer#tidy()
	 */
	public void tidy() {
		stopSounds();
		audioclips = new Hashtable();
	}

	/* (non-Javadoc)
	 * @see southatlantic.sound.AudioPlayer#setDisabled(boolean)
	 */
	public void setDisabled(boolean d) {
		disabled = d;
		if (disabled) {
			stopSounds();
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.sound.AudioPlayer#disabled()
	 */
	public boolean disabled() {
		return disabled;
	}

	/* (non-Javadoc)
	 * @see southatlantic.sound.AudioPlayer#getAudioClip(java.lang.String)
	 */
	public AudioClip getAudioClip(String file_name) {
		AudioClip ac = (AudioClip)audioclips.get(file_name);
		if (ac == null) {
			try {
				ac = createAudioClip(file_name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ac;
	}
}
