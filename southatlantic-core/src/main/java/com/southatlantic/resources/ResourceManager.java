
package com.southatlantic.resources;

import com.southatlantic.SouthatlanticSystem;

import java.applet.AudioClip;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * The Class ResourceManager.
 */
public class ResourceManager {
	private Map<String, Image> images_unloaded;
	private Map<String, Image> imagesLoaded;
			
	/** The SouthatlanticSystem. */
	SouthatlanticSystem saSystem;
	
	/** List of paths to resource direcotories. */
	List<String> resDirs;
	
	/**
	 * Instantiates a new resource manager.
	 * 
	 * @param _sa_system the SouthatlanticSystem
	 * @param paths the resource paths
	 */
	public ResourceManager(SouthatlanticSystem _sa_system, String[] paths) {
		saSystem = _sa_system;
		resDirs = new ArrayList<>();
		for (int i = 0; i < paths.length; i++) {
			resDirs.add(paths[i]);
		}
		images_unloaded = new HashMap();
		imagesLoaded = new HashMap();
	}

	public ResourceManager(SouthatlanticSystem saSystem) {
		this.saSystem = saSystem;
		resDirs = List.of("resources");
		images_unloaded = new HashMap<>();
		imagesLoaded = new HashMap<>();
	}

	/**
	 * Tidy.
	 */
	public void tidy() {
		Iterator it = imagesLoaded.values().iterator();
		while (it.hasNext()) {
			((Image)it.next()).flush();
		}
		imagesLoaded = new HashMap();
		it = images_unloaded.values().iterator();
		while (it.hasNext()) {
			((Image)it.next()).flush();
		}
		images_unloaded = new HashMap();
	}
	
	
	/**
	 * Gets the image.
	 * 
	 * @param name the image-name
	 * 
	 * @return the image, or null, if image not found
	 */
	public Image getImage(String name) {
		Image image = (Image) imagesLoaded.get(name);
		if (image == null) {
			image = (Image)images_unloaded.get(name);
		}
		if (image == null) {
			Iterator it = resDirs.iterator();
			while (it.hasNext()) {
				String dir = (String)it.next();
				image = saSystem.getImageByPath("/" + dir + "/" + name);
				if (image != null) {
					images_unloaded.put(name, image);
					break;
				}
			}
		}
		return image;
	}

	/**
	 * Checks if Image is already loaded.
	 * 
	 * @param image the image
	 * 
	 * @return true, if successful
	 */
	public boolean isAlreadyLoaded(Image image) {
		return imagesLoaded.containsValue(image);
	}
	
	/**
	 * Tells the ResourceManager that 
	 * an image is completely loaded.
	 * 
	 * @param image the image
	 */
	public void loadingComplete(Image image) {
		Set keyset = images_unloaded.keySet();
		Iterator it = keyset.iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			if (images_unloaded.get(key) == image) {
				images_unloaded.remove(key);
				imagesLoaded.put(key, image);
				break;
			}
		}
	}
	
	/**
	 * Gets the font metrics.
	 * 
	 * @param f the font
	 * 
	 * @return the font metrics
	 */
	public FontMetrics getFontMetrics(Font f) {
		return saSystem.getFontMetrics(f);
	}
	
	/**
	 * Gets an input stream by file-name.
	 * 
	 * @param filename the filename
	 * 
	 * @return the input stream by file name
	 * @throws IOException 
	 */
	public InputStream getInputStreamByFileName(String filename) throws IOException {
		Iterator it = resDirs.iterator();
		while (it.hasNext()) {
			String dir = (String)it.next();
			InputStream is = null;
			try {
				is = saSystem.getInputStreamByPath(dir + "/" + filename);
			} catch (IOException e) {
				continue;
			}
			return is;
		}
		throw new IOException("File '" + filename + "' not found in known directories.");
	}

	/**
	 * Gets the GZIP input stream by file name.
	 * 
	 * @param filename the filename
	 * 
	 * @return the GZIP input stream
	 * @throws IOException 
	 */
	public InputStream getGZIPInputStreamByFileName(String filename) throws IOException {
		InputStream is = getInputStreamByFileName(filename);
		if (is != null) {
			is = new GZIPInputStream(is);
		}
		return is;
	}

	/**
	 * Gets an audio clip by file name.
	 * 
	 * @param file_name the file_name
	 * 
	 * @return the audio clip by file name
	 * @throws IOException 
	 */
	public AudioClip getAudioClipByFileName(String file_name) throws IOException {
		AudioClip ac;	
		Iterator it = resDirs.iterator();
		while (it.hasNext()) {
			String dir = (String)it.next();
			ac = saSystem.getAudioClipByPath(dir + "/" + file_name);
			if (ac != null) {
				return ac;
			}
		}
		return null;
	}
	
	public void loadImage(Image img) {
		MediaTracker tracker = new MediaTracker((Component) saSystem);
		tracker.addImage(img, 1);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {}
		loadingComplete(img);
	}

}
