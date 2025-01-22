
package com.southatlantic;

import com.southatlantic.gamestate.GameStateEngine;
import com.southatlantic.resources.ResourceManager;
import com.southatlantic.sound.AudioPlayer;

import java.awt.*;

/**
 * The Class SouthatlanticMain. Manages the central painting and updating.
 */
class SouthatlanticMain implements Runnable {

	private GameStateEngine gse;
	private boolean running;

	private ResourceManager resourceManager;
	private AudioPlayer audioPlayer;

	private Image offScreenImage;
	private Graphics offScreenGraphics;
	
	private boolean paintFinished = true;

	/**
	 * Instantiates a new southatlantic main.
	 * 
	 * @param gse the _gse
	 * @param resourceManager the resourceManager
	 * @param audioPlayer the audioPlayer
	 */
	SouthatlanticMain(GameStateEngine gse, ResourceManager resourceManager, AudioPlayer audioPlayer) {
		this.gse = gse;
		this.resourceManager = resourceManager;
		this.audioPlayer = audioPlayer;
	}
	
	public void init() {
		gse.setStartState();
	}

	public void start() {
		running = true;
		new Thread(this).start(); 
	}

	public void stop() {
		running = false;
		if (audioPlayer != null) {
			audioPlayer.stopSounds();
		}
	}

	public void destroy() {
		if (resourceManager != null)
			resourceManager.tidy();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		gse.initState();
		long before_update = System.currentTimeMillis();
		while (running) {
			if (paintFinished) {
				int time_update_needed = (int)(System.currentTimeMillis() - before_update);
				int wait = gse.getInvFrameRate() - time_update_needed;
//				System.out.println("wait: " + wait);
				if (wait > 0) {
					try {
						Thread.sleep(wait);
					} catch (InterruptedException ignored) {}
				}
				before_update = System.currentTimeMillis();
				gse.animate();
				paintFinished = false;
				gse.repaint();   // update(g, c)-Method is called by another thread
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ignored) {}
			}
		}
	}

	/**
	 * Update.
	 * 
	 * @param g the g
	 * @param component the component
	 */
	public void update(Graphics g, Component component) {
		if (offScreenGraphics == null) {
			offScreenImage = component.createImage(component.getWidth(), component.getHeight());
			offScreenGraphics = offScreenImage.getGraphics();
		}
		gse.paint(offScreenGraphics);
		if (offScreenImage!=null) {
			g.drawImage(offScreenImage,0,0,component);
		}
		paintFinished = true;
	}

	/**
	 * Gets the resource manager.
	 * 
	 * @return the resource manager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * Gets the audio player.
	 * 
	 * @return the audio player
	 */
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

}
