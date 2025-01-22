package com.southatlantic.examples.buccaneerbattle;

import com.southatlantic.SouthatlanticApplet;
import com.southatlantic.SouthatlanticDesktop;
import com.southatlantic.core.InputState;
import com.southatlantic.gamestate.GameState;
import com.southatlantic.gamestate.GameStateLogic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;


import com.southatlantic.menu.DialogState;
import com.southatlantic.menu.MenuEvent;
import com.southatlantic.menu.MenuLayer;
import com.southatlantic.menu.MenuPoint;
import com.southatlantic.menu.MenuState;
import com.southatlantic.menu.MessageLayer;
import com.southatlantic.resources.ResourceManager;
import com.southatlantic.sound.AudioPlayer;
import com.southatlantic.sound.JlAudioPlayer;



public class BuccaneerBattle extends SouthatlanticDesktop implements GameStateLogic {

/* use this class header for desktop-application */
//public class Piraten extends SouthatlanticDesktop implements GameStateLogic {
//	public static void main(String[] args) {
//		main(new BuccaneerBattle(), new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
//	}
/* */

	public static void main(String[] args) {
		main(new BuccaneerBattle(), new Dimension(600, 400));
	}

	private static final String GAME_TITLE = "Buccaneer Battle"; //
	
	// menu-constants
	private static final String MENU_BACKGROUND_IMAGE = "menu_bg.png";
	private static final String MENU_INDEX_IMAGE = "flagge.png";
	private static final String MENU_SOUND_FILE = "1.mid";	
	private static final String TEXT_1_PLAYER_GAME = "1 Player Game";
	private static final String TEXT_2_PLAYER_GAME = "2 Player Game";
	private static final String TEXT_SOUND_ON = "Sound: On";
	private static final String TEXT_SOUND_OFF = "Sound: Off";
	private static final String TEXT_RESUME_GAME = "Resume";
	private static final String TEXT_QUIT_GAME = "Quit";
	private static final String TEXT_DEMO_GAME = "Demo";
	private static final String TEXT_HELP = "Help";
	
	// help-message
	private static final String HELP_MESSAGE = "Try to wreck the enemies' battleship. Use 'a', 'd' to "
		+ "navigate, 's' to set/unset sails and 'q', 'e' to fire left/right. "
		+ "Good luck, but be aware of the rocks! \n \n In 2 player mode the black ship is controlled with "
		+ "arrow-keys and '9','0' to fire. Press 'p' to break a battle.";
	
	// state exitcodes
	public static final int EC_START_1_PLAYER_GAME = 3;
	public static final int EC_START_2_PLAYER_GAME = 0;
	public static final int EC_START_DEMO_GAME = 7;
	public static final int EC_RESUME_GAME = 1;
	public static final int EC_QUIT_GAME = 2;
	public static final int EC_GAME_PAUSED = 4;
	public static final int EC_GAME_OVER = 5;
	public static final int EC_HELP_MESSAGE = 6;

	// screen-dimensions
	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT = 400;

	BattleField battlefield_state = null;

	protected GameStateLogic createGameStateLogic() {
		return this;
	}

	protected ResourceManager createResourceManager() {
		String[] resource_directories = {"resources/res", "resources/external_res"};
		return new ResourceManager(this);
	}
	
	protected AudioPlayer createAudioPlayer(ResourceManager rm) {
//		return new DefaultAudioPlayer(res_mng);
		AudioPlayer ap = new JlAudioPlayer(rm);
		ap.setDisabled(true);
		return ap;
	}
	
	public GameState getStartGameState() {
		return createMenu(false);
	}

	public GameState nextGameState(int exit_code, GameState old_state) {
		if (old_state instanceof DialogState) {
			switch (exit_code) {
			case EC_START_1_PLAYER_GAME:
				battlefield_state = new BattleField(getResourceManager(), getAudioPlayer());
				battlefield_state.setMode(EC_START_1_PLAYER_GAME);
				return battlefield_state;
			case EC_START_2_PLAYER_GAME:
				battlefield_state = new BattleField(getResourceManager(), getAudioPlayer());
				return battlefield_state;
			case EC_START_DEMO_GAME:
				battlefield_state = new BattleField(getResourceManager(), getAudioPlayer());
				battlefield_state.setMode(EC_START_DEMO_GAME);
				return battlefield_state;
			case EC_RESUME_GAME:
				if (battlefield_state == null)
					battlefield_state = new BattleField(getResourceManager(), getAudioPlayer());
				else
					return battlefield_state;
			case EC_QUIT_GAME:
				return createMenu(false);
			case EC_HELP_MESSAGE:
				int ec = ((MenuState)old_state).getMenuLayer().getHeadlineText().equals(GAME_TITLE)?EC_QUIT_GAME:EC_GAME_PAUSED;
				return createHelpMessage(ec);
			case EC_GAME_PAUSED:
				return createMenu(true);  // pause-menu
			}
		} else if (old_state instanceof BattleField) {
			switch (exit_code) {
			case EC_GAME_PAUSED:
				return createMenu(true);  // pause-menu
//				return new DummyMenu(getResourceManager());
			case EC_GAME_OVER:
				battlefield_state = null;
				return createMenu(false);
			}
		}
		return createMenu(false);
	}

	private MenuState createMenu(boolean pause_menu) {
		MenuLayer ml = new MenuLayer(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		ml.setBackgroundImage(getResourceManager().getImage(MENU_BACKGROUND_IMAGE));
		ml.setGap(150, 40);
		ml.setInterspace(20);
		Font font = new Font("Serif", Font.BOLD, 30);
		FontMetrics fm = getResourceManager().getFontMetrics(font);
		Color color = Color.white;
		ml.setHeadline(pause_menu?"Pause":GAME_TITLE, font, fm, color, 30);
		font = new Font("Serif", Font.ITALIC, 28);
		fm = getResourceManager().getFontMetrics(font);
		if (pause_menu) {
			ml.addPoint(new MenuPoint(TEXT_RESUME_GAME, font, fm, color));
			ml.addPoint(new MenuPoint(TEXT_QUIT_GAME, font, fm, color));
		} else {
			ml.addPoint(new MenuPoint(TEXT_1_PLAYER_GAME, font, fm, color));
			ml.addPoint(new MenuPoint(TEXT_2_PLAYER_GAME, font, fm, color));
			ml.addPoint(new MenuPoint(TEXT_DEMO_GAME, font, fm, color));
		}
		ml.addPoint(new MenuPoint(TEXT_HELP, font, fm, color));
		String sound_string = (getAudioPlayer()!= null && !getAudioPlayer().disabled())?TEXT_SOUND_ON:TEXT_SOUND_OFF;
		ml.addPoint(new MenuPoint(sound_string, font, fm, color));
		ml.setIndex(getResourceManager().getImage(MENU_INDEX_IMAGE), new Dimension(-80, -10), new Dimension(80, 60), 0);
		MenuState ms = new MenuState(getResourceManager(), ml) {
			public void init() {
				if (audioPlayer != null) {
					audioPlayer.playSound(MENU_SOUND_FILE, true);
				}
			}
			public void resume() {
				if (audioPlayer != null) {
					audioPlayer.playSound(MENU_SOUND_FILE, true);
				}
			}
			public void pointSelected(MenuEvent me) { 
				if (me.getText().equals(TEXT_1_PLAYER_GAME)) {
					finishState();
					exitCode = EC_START_1_PLAYER_GAME;
				}
				else if (me.getText().equals(TEXT_2_PLAYER_GAME)) {
					finishState();
					exitCode = EC_START_2_PLAYER_GAME;
				}
				else if (me.getText().equals(TEXT_DEMO_GAME)) {
					finishState();
					exitCode = EC_START_DEMO_GAME;
				}
				else if (me.getText().equals(TEXT_SOUND_ON)) {
					if (audioPlayer != null) {
						audioPlayer.stopSounds();
						audioPlayer.setDisabled(true);
						me.getMenuLayer().getMenuPoint(me.getIndex()).setText(TEXT_SOUND_OFF);
					}
				}
				else if (me.getText().equals(TEXT_SOUND_OFF)) {
					if (audioPlayer != null) {
						audioPlayer.stopSounds();
						audioPlayer.setDisabled(false);
						audioPlayer.playSound(MENU_SOUND_FILE, true);
						me.getMenuLayer().getMenuPoint(me.getIndex()).setText(TEXT_SOUND_ON);
					}
				}
				else if (me.getText().equals(TEXT_RESUME_GAME)) {
					finishState();
					exitCode = EC_RESUME_GAME;
				}
				else if (me.getText().equals(TEXT_QUIT_GAME)) {
					finishState();
					exitCode = EC_QUIT_GAME;
				}
				else if (me.getText().equals(TEXT_HELP)) {
					finishState();
					exitCode = EC_HELP_MESSAGE;
				}
			}
		};
		ms.setAudioPlayer(getAudioPlayer());
		ArrayList imgs = new ArrayList();
		imgs.add(getResourceManager().getImage(MENU_BACKGROUND_IMAGE));
		imgs.add(getResourceManager().getImage(MENU_INDEX_IMAGE));
		ms.setLoader(new PirateLoader(imgs, null, SCREEN_WIDTH, SCREEN_HEIGHT, getResourceManager()));
		ms.load();
		ms.setInvFrameRate(100);
		return ms;
	}
	
	private DialogState createHelpMessage(final int state_exit_code) {
		MessageLayer message_layer= new MessageLayer(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		message_layer.setBackgroundColor(Color.black);
		Font font = new Font("Serif", Font.BOLD, 30);
		message_layer.setHeadline(TEXT_HELP, font, getResourceManager().getFontMetrics(font), Color.white, 50);
		font = new Font("Serif", Font.PLAIN, 18);
		message_layer.setMessage(HELP_MESSAGE, font, getResourceManager().getFontMetrics(font), Color.white);
		message_layer.setGap(30, 30);
		message_layer.setGapRight(30);
		return new DialogState(getResourceManager(), message_layer) {
			private int ec = state_exit_code;
			protected void updateState(InputState inputstate, int dt) {
				if (inputstate.isOncePressed(InputState.KEY_SPACE) || inputstate.isOncePressed(InputState.KEY_ENTER)
						|| inputstate.isMouseOncePressed(InputState.BUTTON_LEFT)) {
					finishState();
					exitCode = ec;
				}
			}	
		};
	}
}
