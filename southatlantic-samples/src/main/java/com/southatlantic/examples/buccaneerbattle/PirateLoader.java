package com.southatlantic.examples.buccaneerbattle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import com.southatlantic.resources.Loader;
import com.southatlantic.resources.ResourceManager;

class PirateLoader extends Loader {

	private static final String bg_image_file = "load_screen.png";
	private static final String bubbles_image_file = "bubbles2.png";

	private Image background;
	private Image bubbles;
	
	public PirateLoader(ArrayList _imgs, ArrayList _loadables, int _screen_width, int _screen_height, ResourceManager _res_mng) {
		super(_imgs, _loadables, _screen_width, _screen_height, _res_mng);
		ArrayList preload = new ArrayList();
		background = _res_mng.getImage(bg_image_file);
		bubbles = _res_mng.getImage(bubbles_image_file);
		preload.add(background);
		preload.add(bubbles);
		addPreloadImages(preload);
	}

	protected void paintLoadScreen(java.awt.Graphics g) {
		if (counter < 2) {
			g.setColor(Color.black);
			g.fillRect(0,0,screen_width,
					screen_height);
		} else {
			g.drawImage(background, 0, 0, null);
			g.setColor(Color.white);
			g.setFont(new Font("Serif", Font.ITALIC, 20));
			g.drawString("Loading...", 20, 50);
			int bubbles_y = 250 - (((counter-2)*250)/total);
			g.drawImage(bubbles, 530, bubbles_y, null);
		}
	}

}
