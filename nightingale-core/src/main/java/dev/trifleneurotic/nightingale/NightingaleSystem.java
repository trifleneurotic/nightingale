
package dev.trifleneurotic.nightingale;

import java.applet.AudioClip;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Interface SouthatlanticSystem. An implementation of SouthatlanticSystem provides methods
 * for accessing Images, AudioClips, InputStreams and FontMetrics.
 */
public interface NightingaleSystem {
	
	/**
	 * Gets the image by a path.
	 * 
	 * @param path the path
	 * 
	 * @return the image by path
	 */
	public Image getImageByPath(String path);
	
	/**
	 * Gets the font metrics.
	 * 
	 * @param f the f
	 * 
	 * @return the font metrics
	 */
	public FontMetrics getFontMetrics(Font f);
	
	/**
	 * Gets the input stream by a path.
	 * 
	 * @param path the path
	 * 
	 * @return the input stream by path
	 */
	public InputStream getInputStreamByPath(String path) throws IOException;
	
	/**
	 * Gets the audio clip by a path.
	 * 
	 * @param path the path
	 * 
	 * @return the audio clip by path
	 */
	public AudioClip getAudioClipByPath(String path) throws IOException;
}
