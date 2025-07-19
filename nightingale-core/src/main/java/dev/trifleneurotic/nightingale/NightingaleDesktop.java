
package dev.trifleneurotic.nightingale;

import dev.trifleneurotic.nightingale.gamestate.GameStateEngine;
import dev.trifleneurotic.nightingale.gamestate.GameStateLogic;
import dev.trifleneurotic.nightingale.resources.ResourceManager;
import dev.trifleneurotic.nightingale.sound.AudioPlayer;
import dev.trifleneurotic.nightingale.sound.DesktopAudioClip;
import dev.trifleneurotic.nightingale.sound.MidiAudioClip;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * The Class SouthatlanticDesktop. Entry-Classes of Desktop-Applications that use the southatlantic-engine are derived 
 * from this class and must define a main(String argv[])-Method that simply calls the 
 * main(SoutatlanticDesktop sd, Dimension window-dimension)-method of this class.
 * For example <br/>
 * <code>
 * public static void main(String[] args) {
 *		main(new MyEntryClass(), new Dimension(800, 600));
 * }
 * </code>
 */
public abstract class NightingaleDesktop extends Panel implements NightingaleSystem {
	private NightingaleMain sa_main;

	/**
	 * Instantiates a new southatlantic desktop application.
	 */
	public NightingaleDesktop() {
		ResourceManager rm = createResourceManager();
		AudioPlayer ap = createAudioPlayer(rm);
		sa_main = new NightingaleMain(new GameStateEngine(createGameStateLogic(), this), rm, ap);
	}
		
	/**
	 * Method that must be called to start the application.
	 * 
	 * @param sd the sd
	 * @param panel_dimension the panel_dimension
	 */
	protected static void main(NightingaleDesktop sd, Dimension panel_dimension) {
		sd.setPreferredSize(panel_dimension);
		sd.init();
		sd.start();
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
	
	/**
	 * Inits the application.
	 */
	public void init() {
		sa_main.init();
		JFrame frame = new JFrame();
		frame.setContentPane(this);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sa_main.stop();
				sa_main.destroy();
			}
		});
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Starts the application.
	 */
	public void start() {
		sa_main.start();
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
		try {
			return ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.SouthatlanticSystem#getInputStreamByPath(java.lang.String)
	 */
	public InputStream getInputStreamByPath(String path) throws FileNotFoundException {
		return new FileInputStream(new File(path));
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.SouthatlanticSystem#getAudioClipByPath(java.lang.String)
	 */
	public AudioClip getAudioClipByPath(String path) throws FileNotFoundException {
		FileInputStream fis;
		fis = new FileInputStream(new File(path));
		if (path.endsWith(".mid")) {
			return new MidiAudioClip(fis);
		}
		return new DesktopAudioClip(fis);
	}
}
