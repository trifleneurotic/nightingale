
package com.southatlantic.sound;

import com.southatlantic.resources.ResourceManager;

import java.applet.AudioClip;
import java.io.IOException;

/**
 * Extends DefaultAudioplayer, supports .mp3 by using javazoom.jl
 */
public class JlAudioPlayer extends DefaultAudioPlayer {

	/**
	 * Instantiates a new jl audio player.
	 * 
	 * @param _res_mng the _res_mng
	 */
	public JlAudioPlayer(ResourceManager _res_mng) {
		super(_res_mng);
	}
		
	/* (non-Javadoc)
	 * @see southatlantic.sound.DefaultAudioPlayer#createAudioClip(java.lang.String)
	 */
	protected AudioClip createAudioClip(String file_name) throws IOException {
		if (file_name.endsWith(".mp3")) {
			audioclips.put(file_name, new JlAudioClip(res_mng.getInputStreamByFileName(file_name)));
			return ((AudioClip)audioclips.get(file_name));
		} else {
			return super.createAudioClip(file_name);
		}
	}

}
