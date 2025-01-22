
package com.southatlantic.sound;

import java.applet.AudioClip;

/**
 * The Interface AudioPlayer.
 */
public interface AudioPlayer {
	
	/**
	 * Play sound.
	 * 
	 * @param file_name the file_name
	 * @param looped the looped
	 */
	public void playSound(String file_name, boolean looped);
	
	/**
	 * Stop sounds.
	 */
	public void stopSounds();
	
	/**
	 * Stop sound.
	 * 
	 * @param file_name the file_name
	 */
	public void stopSound(String file_name);
	
	/**
	 * Sets the disabled.
	 * 
	 * @param d the new disabled
	 */
	public void setDisabled(boolean d);
	
	/**
	 * Gets the audio clip. This method is only used for 
	 * preloading {@link southatlantic.resources.Loadable} clips.
	 * 
	 * @param file_name the file_name
	 * 
	 * @return the audio clip
	 */
	public AudioClip getAudioClip(String file_name); 
	
	/**
	 * Disabled.
	 * 
	 * @return true, if successful
	 */
	public boolean disabled();
	
	/**
	 * Tidy.
	 */
	public void tidy();
}
