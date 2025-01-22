package samplanoid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

import southatlantic.SouthatlanticApplet;
import southatlantic.core.InputState;
import southatlantic.gamestate.GameState;
import southatlantic.gamestate.GameStateLogic;
import southatlantic.resources.ResourceManager;
import southatlantic.sound.AudioPlayer;
import southatlantic.tileengine2d.Item;
import southatlantic.tileengine2d.ItemLayer;
import southatlantic.tileengine2d.TileLayer;
import southatlantic.tileengine2d.collision.Collision;
import southatlantic.tileengine2d.collision.CollisionGeom;
import southatlantic.tileengine2d.collision.CollisionGuard;
import southatlantic.tileengine2d.collision.DefaultCollisionGuard;
import southatlantic.tileengine2d.collision.GeomTable;
import southatlantic.tileengine2d.level.Level;
import southatlantic.tileengine2d.level.LevelFactory;
import southatlantic.tileengine2d.util.TileMath;

public class Samplanoid extends SouthatlanticApplet implements GameStateLogic {

	protected static final int PANEL_WIDTH = 200;
	protected static final int PANEL_HEIGHT = 200;
	protected static final int TILE_SIZE = 32;  // scale-all parameter

	protected boolean game_on = false;
	protected Item cylinder = null;

	protected GameStateLogic createGameStateLogic() {
		return this;
	}

	public GameState getStartGameState() {
		game_on = true;
		return createState();
	}

	public GameState nextGameState(int exit_code, GameState old_state) { 
		game_on = true;
		return createState();
	}

	private GameState createState() {
		getResourceManager().loadImage(getResourceManager().getImage("ball.png"));
		Item ball = new Item(getResourceManager().getImage("ball.png"), 
				new Dimension(250, 250) /* size in millitiles */,
				3000, 5700 /* start-position in millitiles */) {  
			int vel_x = 0;
			int vel_y = 0;
			boolean flying = false;
			protected void computeMotion(InputState inputstate, int dt){ 
				if (!flying) {
					if (inputstate.isOncePressed(InputState.KEY_SPACE)) { // if space was pressed
						flying = true;
						vel_x = 3000;  // velocity in millitiles per second
						vel_y = -3000;
					} else {
						setPosX(cylinder.getPosX()); // follow cylinder
					}
				} else {
					int d_x = (vel_x * dt)/1000;
					int d_y = (vel_y * dt)/1000;
					move(d_x, d_y);
					// collision detection of panel borders is done manually with pixel-values 
					int pix_X = TileMath.Mt2Px(getPosX(), TILE_SIZE);
					int pix_Y = TileMath.Mt2Px(getPosY(), TILE_SIZE);
					if (pix_X < 4 || pix_X > PANEL_WIDTH-4) {  // left or right
						move(-d_x, 0);
						vel_x *= -1;
					}
					if (pix_Y < 4) {  // top
						move(0, -d_y);
						vel_y *= -1;
					}
					if (pix_Y > PANEL_HEIGHT-4) { // bottom
						game_on = false;
					}
				}
			}
			public void collisionOccurred(Collision collision) {
				if (collision.getEnemyItem(this) != null) {  // collided object is no tile -> must be the cylinder
					if (vel_y > 0) { 
						vel_y *= -1;  // change direction
						vel_x = (getPosX()-cylinder.getPosX()) * 5; // affect angle
//						resetLastPos();  // reset position 
					}
				} else {  // collided object is a tile
					// compute new direction of ball
					int a_x = 1, a_y = 1;
					if (Math.abs(collision.getX() - getPosX()) < Math.abs(collision.getY() - getPosY())) a_y *= -1;
					else a_x *= -1;
					if (a_y == -1 && 
							( (getPosY() - collision.getY() > 0 &&  vel_y > 0) ||
									(getPosY() - collision.getY() < 0 &&  vel_y < 0) ) ) {a_y = 1; a_x = -1;}
					if (a_x == -1 && 
							( (getPosX() - collision.getX() > 0 &&  vel_x > 0) ||
									(getPosX() - collision.getX() < 0 &&  vel_x < 0) ) ) {a_y = -1; a_x = 1;}
					vel_x *= a_x;
					vel_y *= a_y;
					resetLastPos();
				}
			}
		};
		getResourceManager().loadImage(getResourceManager().getImage("cylinder.png"));
		cylinder = new Item(getResourceManager().getImage("cylinder.png"), new Dimension(1407, 313),
				3000, 6000) {
			protected void computeMotion(InputState inputstate, int dt){
				// move left/right up to panel borders
				int d_x = 0;
				if (inputstate.isPressed(InputState.KEY_RIGHT))
					d_x = dt*5;
				else if (inputstate.isPressed(InputState.KEY_LEFT))
					d_x = - dt*5;
				move(d_x, 0);
				int pix_X = TileMath.Mt2Px(getPosX(), TILE_SIZE);
				if (pix_X < 22 || pix_X > PANEL_WIDTH-22) {
					move(-d_x, 0);
				}
			}
		};
		final ItemLayer item_layer = new ItemLayer(null);
		item_layer.addItem(ball);  // register ball
		item_layer.addItem(cylinder);  // register cylinder
		Level level = null;
		try {
			// read level
			level = LevelFactory.newInstance(getResourceManager()).getTMXLevel("map1.tmx");
		} catch (IOException e) {
			e.printStackTrace();
		}
		getResourceManager().loadImage(getResourceManager().getImage("boxes.png"));
		final TileLayer tile_layer = new TileLayer(getResourceManager().getImage("boxes.png"), 
				2, 2/* image grid */, level) {
			public void collisionOccurred(Collision collision) {
				if (collision.getItem1() == null || collision.getItem2() == null) { // one item is a tile
					// delete that tile
					level.setTile(0, collision.getX() / 626 /* tilesize in millitiles */, collision.getY() / 313, (byte)0);
				}
			}
		};
		tile_layer.setDestinationTileSize(626, 313); // set displayed tilesize in millitiles
		GeomTable gt = new GeomTable();  // object for collision-detection
		CollisionGeom box_geom = new CollisionGeom(new Rectangle(0,0,626,313));
		gt.addGeom(0, box_geom);  // register collision-box for all tiles
		gt.addGeom(1, box_geom);  
		gt.addGeom(2, box_geom);
		gt.addGeom(3, box_geom);
		tile_layer.setGeomTable(gt);
		CollisionGuard cg = new DefaultCollisionGuard();  // this object manages collisiondetection
		ball.setCollisionGuard(cg); // -> if ball moves cg will be informed...
		cg.addCollisionable(tile_layer); // ... cg then tests for collisions with tile_layer and cylinder ...
		cg.addCollisionable(cylinder);
		cg.addCollisionListener(ball); // ... and informs ball and tile_layer if collision occurs. 
		cg.addCollisionListener(tile_layer);
		GameState state = new GameState(getResourceManager()) {
			protected void init() {}
			protected void resume() {}
			protected void paintScene(Graphics g) {
				g.setColor(Color.lightGray);
				g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
				tile_layer.paint(g, 0, 0, TILE_SIZE);
				item_layer.paint(g, 0, 0, TILE_SIZE);
			}
			protected void updateState(InputState inputstate, int dt) {
				item_layer.update(inputstate, dt);
				if (!game_on) {
					finishState();
				}
			}
		};
		state.setInvFrameRate(30);  // update state every 30 milliseconds
		return state;
	}

	protected ResourceManager createResourceManager() { 
		String[] dirs = {"images", "maps"};  // directories to search for resources
		return new ResourceManager(this, dirs); 
	}

	protected AudioPlayer createAudioPlayer(ResourceManager rm) { return null; }  // no sound used
}
