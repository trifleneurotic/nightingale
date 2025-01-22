package com.southatlantic.examples.buccaneerbattle;

import com.southatlantic.core.GamePanel;
import com.southatlantic.core.InputState;
import com.southatlantic.core.Layer;
import com.southatlantic.gamestate.GameState;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


import com.southatlantic.menu.DialogLayer;
import com.southatlantic.resources.Loadable;
import com.southatlantic.resources.ResourceManager;
import com.southatlantic.sound.AudioPlayer;
import com.southatlantic.tileengine2d.Item;
import com.southatlantic.tileengine2d.ItemLayer;
import com.southatlantic.tileengine2d.Sprite;
import com.southatlantic.tileengine2d.TileLayer;
import com.southatlantic.tileengine2d.collision.CollisionGuard;
import com.southatlantic.tileengine2d.collision.CollisionListener;
import com.southatlantic.tileengine2d.collision.Collisionable;
import com.southatlantic.tileengine2d.collision.DefaultCollisionGuard;
import com.southatlantic.tileengine2d.collision.GeomTable;
import com.southatlantic.tileengine2d.level.Level;
import com.southatlantic.tileengine2d.level.LevelFactory;
import com.southatlantic.util.GameMath;
import com.southatlantic.util.ImageUtil;

class BattleField extends GameState implements Weather, Cannon {

	private final static int INVERSE_FRAME_RATE = 30; // in msec
	
	private final static String SOUND_WIND_CHANGE = "wind5.mp3";
	private static final String SOUND_SHOT = "shot2.mp3";
	private static final String SOUND_SPLASH = "splash2.mp3";
	private static final String TILESET_IMAGE = "water5.png";
	private static final String TILESET_BOUNDING_GEOM_FILE = "water5_cb.xml";
	private static final String SHIP_IMAGE = "ship5.png";
	private static final String SHIP_BOUNDING_GEOM_FILE = "ship5_cb.xml";
	private static final String WINDROSE_IMAGE = "windrose2.png";
	private static final String ARROW_IMAGE = "pfeil.png";
	private static final String CANNONBALL_IMAGE = "cannonball4.png";
	private static final String CANNONBALL_BOUNDING_GEOM_FILE = "cannonball4_cb.xml";
	
	private final static int MAIN_PANEL_X = 0; //in pixel
	private final static int MAIN_PANEL_Y = 50;
	private final static int MAIN_PANEL_WIDTH = 600; //in pixel
	private final static int MAIN_PANEL_HEIGHT = 350;
	private final static int MAIN_PANEL_OFFSET_X = 60; // in pixel
	private final static int MAIN_PANEL_OFFSET_Y = 60;	
	private static final int TILE_SIZE = 32;  // how many pixel is 1000 millitiles (scale here)
	
	// tileset
	private static final int TILESET_FRAME_NUMBER_X = 6;
	private static final int TILESET_FRAME_NUMBER_Y = 2;
	private static final int DESTINATION_TILESIZE_X = 2000;  // how many millitiles is a tile of this tileset 
	private static final int DESTINATION_TILESIZE_Y = 1500;  // (it need not be 1000x1000)
	
	private final static int SHIP1_START_X = 4000;  // in mt
	private final static int SHIP1_START_Y = 7000;
	private final static int SHIP1_START_DIRECTION = 90;  // in degree

	private final static int SHIP2_START_X = 18000;  // in mt
	private final static int SHIP2_START_Y = 7000;
	private final static int SHIP2_START_DIRECTION = 270;  // in degree

	private final static int SHOT_MAX_TIME = 3500;  // in msec
	private final static int SHOT_SPEED = 1500;  // in mt/sec
	
	private final static int WIND_START_STRENGTH = 15;  // in knots
	private final static int WIND_STRENGTH_MAX = 30;   
	private final static int WIND_STRENGTH_MIN = 7;
	
	private final static int NUMBER_OF_MAPS = 10;  // called 'battle_mapX.tmx'
	
	// statusbar constants
	private static final Color STATUSBAR_BG_COLOR = Color.black;
	private static final Color STATUSBAR_FG_COLOR = Color.white;	
	private static final int WINDROSE_POS_X = 5;
	private static final int WINDROSE_POS_Y = 0;
	private static final int WINDROSE_WIDTH = 50;
	private static final int WINDROSE_HEIGHT = 50;
	private static final Font WIND_FONT = new Font("Serif", Font.ITALIC, 20);
	private static final Font SCORE_FONT = new Font("Serif", Font.ITALIC, 15);


	private GamePanel main_gamepanel;
	private GamePanel status_gamepanel;
	private Layer message_layer;
	private CollisionGuard cg;

	private PirateShip pirateship1;
	private PirateShip pirateship2;
	private ShipControl ship_control1;
	private ShipControl ship_control2;
	
	GeomTable geom_table_cannonball;  // for collision-detection
	
	int mode = BuccaneerBattle.EC_START_2_PLAYER_GAME;

	private ArrayList itemsToAdd = new ArrayList();

	private int info_timer = 0;

	private int wind_direction;
	private int wind_strength;
	private int wind_direction_change = 0;
	private int wind_strength_change = 0;
	private int change_timer1 = 0;
	
	private int score_black;
	private int score_white;

	public BattleField(ResourceManager res_mng, AudioPlayer ap) {
		super(res_mng);
		setAudioPlayer(ap);
		ArrayList imgs = new ArrayList();
		imgs.add(res_mng.getImage(SHIP_IMAGE));
		imgs.add(res_mng.getImage(TILESET_IMAGE));
		imgs.add(res_mng.getImage(WINDROSE_IMAGE));
		imgs.add(res_mng.getImage(ARROW_IMAGE));
		imgs.add(res_mng.getImage(CANNONBALL_IMAGE));
		ArrayList loadables = new ArrayList();
		AudioClip ac = audioPlayer.getAudioClip(SOUND_SHOT);
		if (ac instanceof Loadable) {
			loadables.add(ac);
		}
		ac = audioPlayer.getAudioClip(SOUND_SPLASH);
		if (ac instanceof Loadable) {
			loadables.add(ac);
		}
		ac = audioPlayer.getAudioClip(PirateShip.SOUND_CRASH);
		if (ac instanceof Loadable) {
			loadables.add(ac);
		}
		ac = audioPlayer.getAudioClip(SOUND_WIND_CHANGE);
		if (ac instanceof Loadable) {
			loadables.add(ac);
		}
		setLoader(new PirateLoader(imgs, loadables, BuccaneerBattle.SCREEN_WIDTH, BuccaneerBattle.SCREEN_HEIGHT, res_mng));
		load();
		setInvFrameRate(INVERSE_FRAME_RATE);
	}
	
	protected void init() {
			// init tilepanel
			main_gamepanel = new GamePanel(new Rectangle(MAIN_PANEL_X, MAIN_PANEL_Y, MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT));
			main_gamepanel.setScaleBase(TILE_SIZE);
			main_gamepanel.setContentPosition(MAIN_PANEL_OFFSET_X, MAIN_PANEL_OFFSET_Y);
			cg = new DefaultCollisionGuard();
			Layer bl = createBackgroundLayer();
			Layer il = createItemLayer(cg);
			main_gamepanel.addLayer(bl);
			main_gamepanel.addLayer(il);
			cg.addCollisionable((Collisionable)il);
			cg.addCollisionable((Collisionable)bl);
			cg.addCollisionListener((CollisionListener)il);
			if (ship_control2 instanceof AIShipControl) {
				// assign collisionables to ai
				((AIShipControl)ship_control2).setCollisionables(cg.getCollisionables());
				((AIShipControl)ship_control2).setEnemy(pirateship1);
			}
			if (ship_control1 instanceof AIShipControl) {
				// assign collisionables to ai
				((AIShipControl)ship_control1).setCollisionables(cg.getCollisionables());
				((AIShipControl)ship_control1).setEnemy(pirateship2);
			}
			
			// init statusbar
			status_gamepanel = new GamePanel(new Rectangle(0, 0, MAIN_PANEL_WIDTH, MAIN_PANEL_Y));
			status_gamepanel.addLayer(createStatusLayer());
			
			// init weather
			Random rand = new Random();
			setWindDirection(rand.nextInt(359));
			setWindStrength(WIND_START_STRENGTH);
			
			// init cannonball-collision-geoms
			Sprite dummy = new Cannonball(getResourceManager().getImage(CANNONBALL_IMAGE), 0, 0, null, 0, null, audioPlayer);
			Dimension s_src_img_ts = dummy.getSourceImageTileSize();
			try {
				geom_table_cannonball = new GeomTable(getResourceManager().getInputStreamByFileName(CANNONBALL_BOUNDING_GEOM_FILE), 
						s_src_img_ts.width, s_src_img_ts.height);
			} catch (IOException e) {
				e.printStackTrace();
			}
			resetTimer();
	}
	
	private Layer createBackgroundLayer() {
		// get random-map
		Random rand = new Random();
		Level level = null;
		try {
			level = (LevelFactory.newInstance(getResourceManager())).getTMXLevel("battle_map" + (rand.nextInt(NUMBER_OF_MAPS)+1) + ".tmx");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TileLayer tl = new TileLayer(getResourceManager().getImage(TILESET_IMAGE), 
				TILESET_FRAME_NUMBER_X, TILESET_FRAME_NUMBER_Y, level);
		tl.setDestinationTileSize(DESTINATION_TILESIZE_X, DESTINATION_TILESIZE_Y);
		// create tile animation
		byte[] frames = new byte[8];
		for (byte i = 0; i < 8; i++) {
			frames[i] = (byte)(i+1);
		}
		tl.setAnimation((byte)1, frames, 150);  // index, frames, frame_duration(msec)
		Dimension tp_src_img_size = tl.getSourceImageTileSize(); // how many pixel is 1000 mt in the source-image 
		try {
			tl.setGeomTable(new GeomTable(getResourceManager().getInputStreamByFileName(TILESET_BOUNDING_GEOM_FILE), 
							tp_src_img_size.width, tp_src_img_size.height)
						);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tl;
	}
	
	private Layer createStatusLayer() {
		return new Layer() {
			private Image windrose = getResourceManager().getImage(WINDROSE_IMAGE);
			private Image arrow = getResourceManager().getImage(ARROW_IMAGE);
			public void paint(Graphics g, int win_x, int win_y, int tilesize) {
				g.setColor(STATUSBAR_BG_COLOR);
				Rectangle clip = g.getClipBounds();
				g.fillRect(clip.x, clip.y, clip.width, clip.height);
				g.drawImage(windrose, WINDROSE_POS_X, WINDROSE_POS_Y, WINDROSE_WIDTH, WINDROSE_HEIGHT, null);
				int mx = 5+25;
				int my = 25;
				Image r_arrow = ImageUtil.rotateImage(arrow, getWindDirection());
				g.drawImage(r_arrow, mx-r_arrow.getWidth(null)/2, my-r_arrow.getHeight(null)/2, null);
				g.setColor(STATUSBAR_FG_COLOR);
				g.fillOval(mx-2,my-2, 4, 4);
				g.setFont(WIND_FONT);
				g.drawString(getWindStrength() + " kn.", WINDROSE_POS_X+WINDROSE_WIDTH+15, 40);
				g.setFont(SCORE_FONT);
//				g.fillRect(450, 20, 20, 15);
				g.drawString(getScoreWhite()+" : "+ getScoreBlack(), 480, 33);
//				g.drawRect(523, 19, 22, 17);
			}
			public void update(InputState keystate, int dt) {}
		};
	}

	private Layer createItemLayer(CollisionGuard cg) {
		ArrayList items = new ArrayList();
		if (mode == BuccaneerBattle.EC_START_DEMO_GAME) {
			ship_control1 = new AIShipControl();
		} else {
			ship_control1 = new HumanShipControl(InputState.KEY_A, InputState.KEY_D, InputState.KEY_S, 
					InputState.KEY_Q, InputState.KEY_E);
		}
		pirateship1 = new PirateShip(getResourceManager().getImage(SHIP_IMAGE), SHIP1_START_X, SHIP1_START_Y, 
				SHIP1_START_DIRECTION, this, this, ship_control1, audioPlayer);
		if (mode == BuccaneerBattle.EC_START_2_PLAYER_GAME) {
			ship_control2 = new HumanShipControl(InputState.KEY_LEFT, InputState.KEY_RIGHT, 
					InputState.KEY_DOWN, InputState.KEY_9, InputState.KEY_0);
		} else {
			ship_control2 = new AIShipControl();
		}
		pirateship2 = new PirateShip(getResourceManager().getImage(SHIP_IMAGE), SHIP2_START_X, SHIP2_START_Y, 
				SHIP2_START_DIRECTION, this, this, ship_control2, audioPlayer);
		Dimension ps_src_img_ts = pirateship1.getSourceImageTileSize();
		try {
			pirateship1.setGeomTable(new GeomTable(getResourceManager().getInputStreamByFileName(SHIP_BOUNDING_GEOM_FILE), 
					ps_src_img_ts.width, ps_src_img_ts.height));
			pirateship2.setGeomTable(new GeomTable(getResourceManager().getInputStreamByFileName(SHIP_BOUNDING_GEOM_FILE), 
					ps_src_img_ts.width, ps_src_img_ts.height));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pirateship2.setSailColor(PirateShip.BLACK);
		pirateship1.setCollisionGuard(cg);
		pirateship2.setCollisionGuard(cg);
		items.add(pirateship1);
		items.add(pirateship2);
		return new ItemLayer(items);
	}

	private Layer createMessageLayer(String message) {
		DialogLayer dl = new DialogLayer(new Dimension(BuccaneerBattle.SCREEN_WIDTH, BuccaneerBattle.SCREEN_HEIGHT));
		Font font = new Font("Serif", Font.ITALIC, 20);
		FontMetrics fm = getResourceManager().getFontMetrics(font);
		dl.setGap(0, BuccaneerBattle.SCREEN_HEIGHT>>1);
		dl.setHeadline(message, font, fm, Color.black, 0);
		return dl;
	}
	
	public void paintScene(Graphics g) {
		status_gamepanel.paint(g, 0, 0, 1);
		main_gamepanel.paint(g, 0, 0, 1);
		if (info_timer > 0 && message_layer != null) {
			message_layer.paint(g,0,0,1);
		}
	}

	protected void updateState(InputState inputstate, int dt) {
		updateWeather(dt);
		if (info_timer > 0) {
			info_timer -= dt;
			if (info_timer <= 0) {
				if (score_white == 5 || score_black == 5) {
					exitCode = BuccaneerBattle.EC_GAME_OVER;
					finishState();
				} else {
					init();
				}
			}
			return;
		}
		if (itemsToAdd.size()>0) {
			Iterator it = itemsToAdd.iterator();
			while (it.hasNext()) {
				Item item = (Item)it.next();
					((ItemLayer)main_gamepanel.getLayer(1)).addItem(item);
					((Sprite)item).setCollisionGuard(cg);
			}
			itemsToAdd = new ArrayList();
		}
		main_gamepanel.update(inputstate, dt);
		Dimension offset = main_gamepanel.getContentPosition();
		Dimension clip = main_gamepanel.getClipDimension();
		Rectangle r = new Rectangle(offset.width, offset.height, clip.width, clip.height);
		pirateship1.processClipBorder(r, TILE_SIZE);
		pirateship2.processClipBorder(r, TILE_SIZE);
		if (inputstate.isOncePressed(InputState.KEY_P)) {
			exitCode = BuccaneerBattle.EC_GAME_PAUSED;
			finishState();
		}
		if (pirateship1.isGarbage() || pirateship2.isGarbage()) {
			info_timer = 2000;
			if (pirateship1.isGarbage() && !pirateship2.isGarbage() && !pirateship2.isSinking()) {
				score_black++;
				message_layer = createMessageLayer("Black wins!");
			} else if (pirateship2.isGarbage() && !pirateship1.isGarbage() && !pirateship1.isSinking()) {
				score_white++;
				message_layer = createMessageLayer("White wins!");
			} else {
				message_layer = createMessageLayer("Tie!");
			}
			if (score_black == 5) {
				info_timer = 3000;
				message_layer = createMessageLayer("Game Over, Black wins!");
			}
			if (score_white == 5) {
				info_timer = 3000;
				message_layer = createMessageLayer("Game Over, White wins!");
			}
		}
	}

	public void setMode(int m) {
		mode = m;
	}

	private void updateWeather(int dt) {
		change_timer1 += dt;
		Random rand = new Random();
		if (wind_direction_change == 0) {
			if (rand.nextInt(100)*change_timer1>1500000) {
				audioPlayer.playSound(SOUND_WIND_CHANGE, false);
				wind_direction_change = rand.nextInt(101)-50;
				wind_strength_change = (rand.nextInt(13)-6)<<10;
				change_timer1 = rand.nextInt(5000);
			}
		}
		if (wind_direction_change != 0) {
			int change = (dt * 30)>>10;
			change = Math.min(change, Math.abs(wind_direction_change));
			change *= (wind_direction_change>0?1:-1); 
			setWindDirection(getWindDirection()+change);
			wind_direction_change -= change;
		}
		if (wind_strength_change != 0) {
			int change = dt*1;
			change = Math.min(change, Math.abs(wind_strength_change));
			change *= (wind_strength_change>0?1:-1); 
			wind_strength += change;
			if (wind_strength > WIND_STRENGTH_MAX<<10) wind_strength = WIND_STRENGTH_MAX<<10;
			if (wind_strength < WIND_STRENGTH_MIN<<10) wind_strength = WIND_STRENGTH_MIN<<10;
			wind_strength_change -= change;
		}
	}

	private void setWindDirection(int _wind_direction) {
		wind_direction = _wind_direction;
		while (wind_direction > 359) wind_direction -= 360;
		while (wind_direction < 0) wind_direction += 360;
	}

	private void setWindStrength(int knots) {
		if (knots > WIND_STRENGTH_MAX) knots = WIND_STRENGTH_MAX;
		if (knots < WIND_STRENGTH_MIN) knots = WIND_STRENGTH_MIN;
		wind_strength = knots<<10;
	}

	public int getScoreBlack() {
		return score_black;
	}

	public int getScoreWhite() {
		return score_white;
	}

	// methods implementing the Cannon-interface
	public void shoot(int direction, int posX, int posY, Item source_item) {
		Dimension vector_shot = new Dimension((SHOT_SPEED*GameMath.sin(direction))>>10, -(SHOT_SPEED*GameMath.cos(direction))>>10);
		if (source_item instanceof PirateShip) {
			Dimension vector_ship = ((PirateShip)source_item).getMovementVector(1);
			vector_shot.width += vector_ship.width/2;
			vector_shot.height += vector_ship.height/2;
		}
		Sprite shot = new Cannonball(getResourceManager().getImage("cannonball4.png"), 
				posX, posY, vector_shot, SHOT_MAX_TIME, source_item, audioPlayer);
		shot.setGeomTable(geom_table_cannonball);
		itemsToAdd.add(shot);
		if (audioPlayer != null) {
			audioPlayer.playSound(SOUND_SHOT, false);
		}
	}

	
	// methods implementing the Weather-interface
	public int getWindDirection() {
		return wind_direction;
	}
	
	public int getWindStrength() {
		return wind_strength>>10;
	}
	
	
	// abstract methods from GameState
	public void resume() { }
}

