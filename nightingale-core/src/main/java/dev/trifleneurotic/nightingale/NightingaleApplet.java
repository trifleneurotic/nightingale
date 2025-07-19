
package dev.trifleneurotic.nightingale;

import dev.trifleneurotic.nightingale.gamestate.GameStateEngine;
import dev.trifleneurotic.nightingale.gamestate.GameStateLogic;
import dev.trifleneurotic.nightingale.resources.ResourceManager;
import dev.trifleneurotic.nightingale.sound.AudioPlayer;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * The Class SouthatlanticApplet. Applets that use the southatlantic-engine are derived 
 * from this class instead of java.applet.Applet .
 */
public abstract class NightingaleApplet extends Applet implements NightingaleSystem {
	private NightingaleMain sa_main;
	
	/**
	 * Instantiates a new southatlantic applet.
	 */
	public NightingaleApplet() {
		ResourceManager rm = createResourceManager();
		AudioPlayer ap = createAudioPlayer(rm);
		sa_main = new NightingaleMain(new GameStateEngine(createGameStateLogic(), this), rm, ap);
	}
	
	/**
	 * Creates the resource manager.
	 * 
	 * @return the resource manager
	 */
	protected abstract ResourceManager createResourceManager();
	
	/**
	 * Creates the audio player.
	 * 
	 * @param rm the rm
	 * 
	 * @return the audio player
	 */
	protected abstract AudioPlayer createAudioPlayer(ResourceManager rm);
	
	/**
	 * Creates the game state logic.
	 * 
	 * @return the game state logic
	 */
	protected abstract GameStateLogic createGameStateLogic();

	/**
	 * Gets the resource manager.
	 * 
	 * @return the resource manager
	 */
	protected ResourceManager getResourceManager() {
		return sa_main.getResourceManager();
	}

	/**
	 * Gets the audio player.
	 * 
	 * @return the audio player
	 */
	protected AudioPlayer getAudioPlayer() {
		return sa_main.getAudioPlayer();
	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#init()
	 */
	public void init() {
		sa_main.init();
	}

	/* (non-Javadoc)
	 * @see java.applet.Applet#start()
	 */
	public void start() {
		sa_main.start();
	}

	/* (non-Javadoc)
	 * @see java.applet.Applet#stop()
	 */
	public void stop() {
		sa_main.stop();
	}

	/* (non-Javadoc)
	 * @see java.applet.Applet#destroy()
	 */
	public void destroy() {
		sa_main.destroy();
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#update(java.awt.Graphics)
	 */
	public void update(Graphics g) {
		sa_main.update(g, this);
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.SouthatlanticSystem#getImageByPath(java.lang.String)
	 */
	public Image getImageByPath(String path) {
		return getImage(getDocumentBase(), path);	
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.SouthatlanticSystem#getInputStreamByPath(java.lang.String)
	 */
	public InputStream getInputStreamByPath(String path) throws IOException {
		URL url;
		url = new URL (getCodeBase(), path);
		return url.openStream();
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.SouthatlanticSystem#getAudioClipByPath(java.lang.String)
	 */
	public AudioClip getAudioClipByPath(String path) {
		return getAudioClip(getDocumentBase(), path);
	}

}
