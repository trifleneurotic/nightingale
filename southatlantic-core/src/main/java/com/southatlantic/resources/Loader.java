
package com.southatlantic.resources;

import java.awt.*;
import java.util.ArrayList;

/**
 * The Class Loader. Loads Image- and {@link Loadable}-Resources in the 
 * context of a {@link southatlantic.gamestate.GameState}.
 */
public class Loader /*implements ImageObserver*/ {
	
	private ArrayList imgs;
	private ArrayList loadables;
	
	/** The counter. */
	protected int counter = 0;
	
	/** The total. */
	protected int total = 0;
	
	/** The screen_width. */
	protected int screen_width;
	
	/** The screen_height. */
	protected int screen_height;
	
	private String error_string = "";
	
	private ResourceManager res_mng;
	
	/**
	 * Instantiates a new loader.
	 * 
	 * @param _imgs the Images
	 * @param _loadables the Loadables
	 * @param _screen_width the screen-width
	 * @param _screen_height the screen-height
	 * @param _res_mng the ResourceManager
	 */
	public Loader(ArrayList _imgs, ArrayList _loadables, int _screen_width, int _screen_height, ResourceManager _res_mng) {
		imgs = _imgs;
		if (imgs == null) {
			imgs = new ArrayList();
		}
		loadables = _loadables;
		if (loadables == null) {
			loadables = new ArrayList();
		}
		total = imgs.size() + loadables.size();
		screen_width = _screen_width;
		screen_height = _screen_height;
		res_mng = _res_mng;
	}
	
	/**
	 * Update.
	 * 
	 * @return false, if loading complete
	 */
	public boolean update() {
		if (counter < imgs.size()) {
			res_mng.loadImage(((Image)imgs.get(counter)));
			counter++;
//			while (counter < imgs.size() && res_mng.isAlreadyLoaded((Image)imgs.get(counter))) {
//				counter++;
//			}
		}
		else if (counter < loadables.size()+imgs.size()) {
			((Loadable)loadables.get(counter-imgs.size())).load();
			counter++;
		}
		if (counter < imgs.size() + loadables.size()) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Paint.
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
//		if (counter < imgs.size()) {
//			g.drawImage((Image)imgs.get(counter),0,0,this);
//		}
		paintLoadScreen(g);
	}
	
	/**
	 * Paints the screen that is visible while loading. 
	 * 
	 * @param g
	 */
	protected void paintLoadScreen(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,screen_width,
				screen_height);
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif", Font.PLAIN, 10));
		if (error_string != null) {
			g.drawString(error_string, 10, 10);
		}
		String status_string = null;
		if (counter < imgs.size()) {
			status_string = "Loading image " + (counter+1) + " of " + imgs.size() + "...";
		} else {
			status_string = "Loading stream " + (counter+1-imgs.size()) + " of " + loadables.size() + "...";
		}
		g.drawString(status_string, 20, screen_height-50);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int, int)
	 */
//	public boolean imageUpdate(Image img, int info, int x, int y, int width, int height) {
//		if ((info & (ImageObserver.ERROR | ImageObserver.ABORT)) > 0) {
//			error_string += "Error occurred while loading image '" + imgs.get(counter) + "'\n";
//			res_mng.loadingComplete(img);
//			return false;
//		}
//		if ((info & ImageObserver.ALLBITS) > 0) {
//			res_mng.loadingComplete(img);
//			return false;
//		}
//		else 
//			return true;
//	}

	/**
	 * Adds Images to the list that are needer to paint the load-screen.
	 * 
	 * @param preload_imgs the preload_imgs
	 */
	protected void addPreloadImages(ArrayList preload_imgs) {
		if (imgs == null) {
			imgs = new ArrayList();
		}
		imgs.addAll(0, preload_imgs);
	}

	public String getErrorMessage() {
		return error_string;
	}
	
}
