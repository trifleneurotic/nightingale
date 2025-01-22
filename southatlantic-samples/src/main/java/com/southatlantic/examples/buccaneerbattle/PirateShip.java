package com.southatlantic.examples.buccaneerbattle;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.southatlantic.core.InputState;
import com.southatlantic.sound.AudioPlayer;
import com.southatlantic.tileengine2d.Item;
import com.southatlantic.tileengine2d.Sprite;
import com.southatlantic.tileengine2d.animation.Sequence;
import com.southatlantic.tileengine2d.animation.SequenceTable;
import com.southatlantic.tileengine2d.collision.Collision;
import com.southatlantic.tileengine2d.util.TileMath;
import com.southatlantic.util.GameMath;

class PirateShip extends Sprite {
	private static final int IMAGE_SIZE_X = 2000; // destination image size in mt
	private static final int IMAGE_SIZE_Y = 2000;
	private static final int IMAGE_FRAME_NUMBER_X = 9;
	private static final int IMAGE_FRAME_NUMBER_Y = 8;

	// states
	private static final int WITH_SAIL_WHITE = 0; 
	private static final int WITH_SAIL_BLACK = 3; 
	private static final int WITHOUT_SAIL = 1; 
	private static final int SINKING = 2; 
	
	public static final byte WHITE = 0;
	public static final byte BLACK = 1;
	
	private byte sailcolor = WHITE;
	
	public static final byte AHEAD = 0;
	public static final byte STARBORD = 2;
	public static final byte PORTSIDE = 1;
	
	public static final String SOUND_CRASH = "crash2.mp3";
	private AudioPlayer audioPlayer = null;
	
	private int ship_direction;   //in degree, 0=north, 90=east
	private int real_direction;   
	private final int ANGLE_INTERVALLSIZE = 20;  // in degree
	private int num_of_intervals = 360 / ANGLE_INTERVALLSIZE; 
	
	private static int[] ANGLE_INTERVALL; 
	private static int[] STATE_DIRECTION; 
		
	private final int TURNAROUND_SPEED = 45; // 0.25*pi (per second)

	private static final int LAYER_SHIP = 0;
	
	private static final int CANNON_LOAD_TIME = 4000; // in ms
	
	private Weather weather;
	private Cannon cannon;
	private int cannon_load_progress = CANNON_LOAD_TIME+1;
	
	private int vel;
	private int wind_angle;
	private byte side_of_wind;
	
	private ShipControl control;
	
	private int nav_state = AHEAD;
	
	// SequenceTable(id, loops, duration, frames)
	private static ArrayList createSequenceTables() {
			ArrayList tables = new ArrayList();
			int[] i1 = new int[1];
			i1[0] = 0;
			tables.add(new SequenceTable(WITH_SAIL_WHITE, 0, 0, i1 ));
			int[] i2 = new int[1];
			i2[0] = 36;
			tables.add(new SequenceTable(WITHOUT_SAIL, 0, 0, i2 ));
			int[] i4 = new int[1];
			i4[0] = 18;
			tables.add(new SequenceTable(WITH_SAIL_BLACK, 0, 0, i4 ));
			int[] i3 = new int[4];
			i3[0] = 54;
			i3[1] = 55;
			i3[2] = 56;
			i3[3] = 57;
			tables.add(new SequenceTable(SINKING, 1, 60, i3 ));
			return tables;
	}

	public PirateShip (Image ship_img, int posX, int posY, int startDirection, 
			Weather _weather, Cannon _cannon, ShipControl _control, AudioPlayer _audioPlayer) {
		super(ship_img, new Dimension(IMAGE_SIZE_X, IMAGE_SIZE_Y), posX, posY,
				IMAGE_FRAME_NUMBER_X, IMAGE_FRAME_NUMBER_Y, PirateShip.createSequenceTables());
		weather = _weather;
		cannon = _cannon;
		control = _control;
		audioPlayer = _audioPlayer;
		ANGLE_INTERVALL = new int[num_of_intervals];
		STATE_DIRECTION = new int[num_of_intervals];
		for (int i=0; i<num_of_intervals; i++) {
			ANGLE_INTERVALL[i] = i*(ANGLE_INTERVALLSIZE) + ANGLE_INTERVALLSIZE/2;
			STATE_DIRECTION[i] = i*(ANGLE_INTERVALLSIZE); 
		}
		setSequence(LAYER_SHIP, WITHOUT_SAIL, 0);
		anchor();
		vel = 0;
		setDirection(startDirection);
	}
	

	// navigation-methods
	public void starbord () {
		nav_state = STARBORD;
	}
	
	public void portside () {
		nav_state = PORTSIDE;
	}
	
	public void setSails() {
		setSequence(LAYER_SHIP, sailcolor==WHITE?WITH_SAIL_WHITE:WITH_SAIL_BLACK, getCurrentSequence(LAYER_SHIP).getOffset());
	}
	
	public void anchor() {
		setSequence(LAYER_SHIP, WITHOUT_SAIL, getCurrentSequence(LAYER_SHIP).getOffset());
	}

	public void setSailColor(byte color) {
		sailcolor = color;
	}
	

	//cannons
	public void fireLeft() {
		if (cannon_load_progress > CANNON_LOAD_TIME) {
			int index = getCurrentSequence(LAYER_SHIP).getCurrentFrame();
			index -= (onSails()?(sailcolor==WHITE?0:num_of_intervals):num_of_intervals*2);
			int shotDirection = STATE_DIRECTION[index] - 90;
			if (shotDirection < 0)
				shotDirection += 360;
			cannon.shoot(shotDirection, getPosX(), getPosY(), (Item)this);
			cannon_load_progress = 0;
		} 
	}

	public void fireRight() {
		if (cannon_load_progress > CANNON_LOAD_TIME) {
			int index = getCurrentSequence(LAYER_SHIP).getCurrentFrame();
			index -= (onSails()?(sailcolor==WHITE?0:num_of_intervals):num_of_intervals*2);
			int shotDirection = STATE_DIRECTION[index] + 90;
			if (shotDirection > 360)
				shotDirection -= 360;
			cannon.shoot(shotDirection, getPosX(), getPosY(), (Item)this);
			cannon_load_progress = 0;
		} 
	}


	// collision-method
	public void collisionOccurred(Collision collision) {
		if (!(collision.getEnemyItem(this) instanceof Cannonball && ((Cannonball)collision.getEnemyItem(this)).getSourceItem() == this)) {
				int offset = onSails()?(sailcolor==WHITE?0:9):4;
				setSequence(LAYER_SHIP, SINKING, offset);
				if (audioPlayer != null) {
					audioPlayer.playSound(SOUND_CRASH, false);
				}
		}
	}

	
	// info-methods
	public int getDirection() {
		return ship_direction;
	}

	private void setDirection(int direction) {
		ship_direction = direction;
        int i = 0;
		while (true) {
			if (ANGLE_INTERVALL[i] > direction) {
				break;
			}
			i++;
			if (i==num_of_intervals) {
				i=0;
				break;
			}
		}
		getCurrentSequence(LAYER_SHIP).setOffset(i);
	}

	public Dimension getMovementVector(double scale_factor) {
//		int index = ((Sequence)layer.get(0)).getCurrentFrame()-(onSails()?(sailcolor==WHITE?0:num_of_intervals):num_of_intervals*2);
//		if (index >= STATE_DIRECTION.length) {
//			return new Dimension(0, 0);
//		}
//		int direction = STATE_DIRECTION[index];
		return new Dimension((((int)scale_factor*vel)*GameMath.sin(real_direction))>>10, -(((int)scale_factor*vel)*GameMath.cos(real_direction))>>10);
	}

	public Dimension getDirectionVector(int length) {
//		int direction = STATE_DIRECTION[((Sequence)layer.get(0)).getCurrentFrame()-(onSails()?(sailcolor==WHITE?0:num_of_intervals):num_of_intervals*2)];
		return new Dimension((length*GameMath.sin(real_direction))>>10, -(length*GameMath.cos(real_direction))>>10);
	}

	public int getWindAngle() {
		return wind_angle;
	}
	
	public int getSideOfWind() {
		return side_of_wind;
	}
	
	public int getVelocity() {
		return vel;
	}

	public boolean onSails() {
		return getCurrentSequenceId(LAYER_SHIP) == WITH_SAIL_WHITE || getCurrentSequenceId(LAYER_SHIP) == WITH_SAIL_BLACK;
	}
	
	public boolean isSinking() {
		return getCurrentSequenceId(LAYER_SHIP) == SINKING;
	}


	// animation- and motion-methods
	protected void computeAnimation(InputState inputstate, int dt) {
		if (isSinking()) {
			if (getCurrentSequence(LAYER_SHIP).isFinished()) {
				setGarbageFlag();
			}
			return;
		}
	}

	protected void computeMotion (InputState inputstate, int dt) {
		if (cannon_load_progress <= CANNON_LOAD_TIME) {
			cannon_load_progress += dt;
		}
		if (isSinking()) return; 
		control.performCommands(this, inputstate);
		
		// rudder
		if (nav_state != AHEAD) {
			int dA = (dt*TURNAROUND_SPEED)/1000; 
			int direction = getDirection();
			if (nav_state == STARBORD) {
				direction += dA;
				direction = direction >= 360 ? 
						direction-360 : direction;
			} else if (nav_state == PORTSIDE) {
				direction -= dA;
				direction = direction < 0 ? 360+direction :
					direction;
			}
			setDirection(direction);
			nav_state = AHEAD;
		}
		
		//comopute force, only if sails are set
		int force = 0;
		if (onSails())	{	
			int wind_direction = weather.getWindDirection();
			int wind_strength = weather.getWindStrength();  // wind in knots
			//angle of wind on ship (0 = headwind, 180 = tailwind)
			wind_angle = Math.abs(wind_direction - ship_direction);
			wind_angle = (wind_angle > 180 ? 360 - wind_angle : wind_angle);
			side_of_wind = wind_direction - ship_direction > 0 ? STARBORD : PORTSIDE;

			// compute force
			if (wind_angle > 30) {
				force = wind_strength * 80 * wind_angle / 180;
			} else {
				force = - wind_strength * 50;
			}
		}
		
		// acceleration in delta-knots per sec
		int waterresistance = vel/2;
		int acceleration = force - waterresistance; 

		//compute velocity
		vel += (acceleration*dt)>>10;
		if (vel < 0) vel = 0;

		// move
		real_direction = STATE_DIRECTION[((Sequence)layer.get(0)).getCurrentFrame()-(onSails()?(sailcolor==WHITE?0:num_of_intervals):num_of_intervals*2)];
		int velx = (GameMath.cos((real_direction > 90 ? 450 - real_direction : 90 - real_direction)) * vel) >> 10;
		int vely = -((GameMath.cos(real_direction)*vel) >> 10);
		int dx  = (velx * dt) / 1000;
		int dy  = (vely * dt) / 1000;
		move(dx, dy);
	}
		
	public void processClipBorder(Rectangle clip, int tilesize) {
		int half_image_x = (getDestinationImageDimension().width>>1);
		int half_image_y = (getDestinationImageDimension().height>>1);
		Rectangle clip_mt = new Rectangle(TileMath.Px2Mt(clip.x, tilesize), TileMath.Px2Mt(clip.y, tilesize), 
				TileMath.Px2Mt(clip.width, tilesize), TileMath.Px2Mt(clip.height, tilesize));
		if (getPosX() < clip_mt.x - half_image_x) {
			this.setPosX(clip_mt.x+clip_mt.width+half_image_x);
		} else if (getPosX() > clip_mt.x+clip_mt.width+half_image_x) {
			this.setPosX(clip_mt.x-half_image_x);
		}
		if (getPosY() < clip_mt.y - half_image_y) {
			this.setPosY(clip_mt.y+clip_mt.height+half_image_y);
		} else if (getPosY() > clip_mt.y+clip_mt.height+half_image_y) {
			this.setPosY(clip_mt.y-half_image_y);
		}
	}
	
	
	//for testing
//	public void paint(Graphics g, int winx, int winy, int tilesize) {
//		super.paint(g, winx, winy, tilesize);
//		if (control instanceof AIShipControl) {
//			Line2D line = ((AIShipControl)control).getLine();
//			if (line != null) {
//				line = CollisionGeom.getAbsoluteGeom(new CollisionGeom(line), getPosX(), getPosY()).getLine();
//				g.setColor(Color.red);
//				g.drawLine(TileMath.Mt2Px((int)line.getX1(), tilesize)-winx, TileMath.Mt2Px((int)line.getY1(), tilesize)-winy, 
//						TileMath.Mt2Px((int)line.getX2(), tilesize)-winx, TileMath.Mt2Px((int)line.getY2(), tilesize)-winy);
//			}
//		}
//	}
}
