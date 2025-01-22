package com.southatlantic.examples.buccaneerbattle;

import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


import com.southatlantic.core.InputState;
import com.southatlantic.tileengine2d.Item;
import com.southatlantic.tileengine2d.ItemLayer;
import com.southatlantic.tileengine2d.Sprite;
import com.southatlantic.tileengine2d.TileLayer;
import com.southatlantic.tileengine2d.animation.SequenceTable;
import com.southatlantic.tileengine2d.collision.Collision;
import com.southatlantic.tileengine2d.collision.CollisionGeom;
import com.southatlantic.tileengine2d.collision.Collisionable;
import com.southatlantic.tileengine2d.collision.GeomTable;
import com.southatlantic.util.GameMath;

class AIShipControl implements ShipControl {

	ArrayList collisionables = null;
	
	private Sprite dummy = null;
	
	private Stack sensor_stack = new Stack();

	private final static byte WATCH_AHEAD1 = 0;
	private final static byte WATCH_AHEAD2 = 5;
	private final static byte WATCH_LEFT = 1;
	private final static byte WATCH_RIGHT = 2;
	private final static byte WATCH_LEFT_EVASION = 3;
	private final static byte WATCH_RIGHT_EVASION = 4;
	private final static byte CHECK_SPEED = 6;
	private final static byte WATCH_LEFT_FALL_OFF = 7;
	private final static byte WATCH_RIGHT_FALL_OFF = 8;

	private final static byte MODE_STOP = 0;
	private final static byte MODE_EVASION_LEFT = 1;
	private final static byte MODE_EVASION_RIGHT = 2;
	private final static byte MODE_NOTHING = 3;
	private final static byte MODE_FALL_OFF_LEFT = 4;
	private final static byte MODE_FALL_OFF_RIGHT = 5;
	private final static byte MODE_BREAK = 6;
	private final static byte MODE_TRY_FALL_OFF_RIGHT = 7;
	private final static byte MODE_TRY_FALL_OFF_LEFT = 8;
	
	
	byte mode = MODE_NOTHING;
	int dest_direction = 0;
	int src_direction = 0;
	
	private final static int WANTED_SPEED = 2500;
	private final static int MIN_SPEED = 800;
	private final static int WANTED_WIND_DIRECTION = 140;
	
	PirateShip enemy = null;
	
	//for testing
	Line2D line;
	public Line2D getLine() {
		return line;
	}
		
	public void setCollisionables(ArrayList _collisionables) {
		collisionables = _collisionables;
	}
	
	public void setEnemy(PirateShip ship) {
		enemy = ship;
	}
	
	public void performCommands(PirateShip ship, InputState inputstate) {
		// dummy
		if (dummy == null) {
			ArrayList seq_tableset = new ArrayList();
			seq_tableset.add(SequenceTable.getDefaultSequenceTable());
			dummy = new Sprite(null, new Dimension(0, 0), 0, 0, 0, 0, seq_tableset);
			dummy.setSequence(0, 0, 0);			
		}
		dummy.setPosX(ship.getPosX());
		dummy.setPosY(ship.getPosY());
		
		// sensoring
		if (sensor_stack.empty()) {
			sensor_stack.push(new Byte(CHECK_SPEED));
			sensor_stack.push(new Byte(WATCH_LEFT));
			sensor_stack.push(new Byte(WATCH_RIGHT));
			sensor_stack.push(new Byte(WATCH_AHEAD2));
			sensor_stack.push(new Byte(WATCH_AHEAD1));
			if (mode == MODE_STOP) {
				sensor_stack.push(new Byte(WATCH_LEFT_EVASION));
				sensor_stack.push(new Byte(WATCH_RIGHT_EVASION));
			}
		}
		byte sensor_action = ((Byte)sensor_stack.pop()).byteValue();
		switch (sensor_action) {
		case WATCH_AHEAD1:
			if (mode != MODE_EVASION_LEFT && mode != MODE_EVASION_RIGHT && mode != MODE_STOP) {
				Dimension ahead_vector_1 = GameMath.vector_rotate(ship.getMovementVector(4), -5);
				if (checkForBarrier(ship.getPosX(), ship.getPosY(), ahead_vector_1)) {
					ship.anchor();
					mode = MODE_STOP;
					sensor_stack.push(new Byte(WATCH_RIGHT_EVASION));
					sensor_stack.push(new Byte(WATCH_LEFT_EVASION));
				}
			}
			break;
		case WATCH_AHEAD2:
			if (mode != MODE_EVASION_LEFT && mode != MODE_EVASION_RIGHT && mode != MODE_STOP) {
				Dimension ahead_vector_2 = GameMath.vector_rotate(ship.getMovementVector(4), 5);
				if (checkForBarrier(ship.getPosX(), ship.getPosY(), ahead_vector_2)) {
					ship.anchor();
					mode = MODE_STOP;
					sensor_stack.push(new Byte(WATCH_RIGHT_EVASION));
					sensor_stack.push(new Byte(WATCH_LEFT_EVASION));
				}
			}
			break;
		case WATCH_LEFT_EVASION:
			if (mode == MODE_STOP) {
				Dimension evasion_vector_left = GameMath.vector_rotate(ship.getMovementVector(4), -45);
				if (!checkForBarrier(ship.getPosX(), ship.getPosY(), evasion_vector_left)) {
					mode = MODE_EVASION_LEFT;
					dest_direction = ship.getDirection() - 45;
					dest_direction = dest_direction < 0 ? 360+dest_direction :
						dest_direction;
					src_direction = ship.getDirection();
				}
			}
			break;
		case WATCH_RIGHT_EVASION:
			if (mode == MODE_STOP) {
				Dimension evasion_vector_right = GameMath.vector_rotate(ship.getMovementVector(4), 45);
				if (!checkForBarrier(ship.getPosX(), ship.getPosY(), evasion_vector_right)) {
					mode = MODE_EVASION_RIGHT;
					dest_direction = ship.getDirection() + 45;
					dest_direction = dest_direction >= 360 ? 
							dest_direction-360 : dest_direction;
					src_direction = ship.getDirection();
				}
			}
			break;
		case WATCH_RIGHT_FALL_OFF:
			if (mode == MODE_TRY_FALL_OFF_RIGHT) {
				Dimension fall_off_vector_right = GameMath.vector_rotate(ship.getMovementVector(8), 30);
				if (!checkForBarrier(ship.getPosX(), ship.getPosY(), fall_off_vector_right)) {
					mode = MODE_FALL_OFF_RIGHT;
					dest_direction = ship.getDirection() + 30;
					dest_direction = dest_direction >= 360 ? 
							dest_direction-360 : dest_direction;
					src_direction = ship.getDirection();
				} else {
					mode = MODE_NOTHING;
				}
			}
			break;
		case WATCH_LEFT_FALL_OFF:
			if (mode == MODE_TRY_FALL_OFF_LEFT) {
				Dimension fall_off_vector_left = GameMath.vector_rotate(ship.getMovementVector(8), -30);
				if (!checkForBarrier(ship.getPosX(), ship.getPosY(), fall_off_vector_left)) {
					mode = MODE_FALL_OFF_LEFT;
					dest_direction = ship.getDirection() - 30;
					dest_direction = dest_direction < 0 ? 360+dest_direction :
						dest_direction;
					src_direction = ship.getDirection();
				} else {
					mode = MODE_NOTHING;
				}
			}
			break;
		case WATCH_LEFT:
			Dimension vectorl = ship.getDirectionVector(7000);
			Dimension vector_left = new Dimension(vectorl.height, vectorl.width);
			vector_left.height = vector_left.height * -1;
			Dimension vector_movement = ship.getMovementVector(2);
			vector_left.height += (vector_movement.height);
			vector_left.width += (vector_movement.width);
			if (enemy != null) {
				int z = Math.abs(Math.abs(enemy.getDirection()-ship.getDirection())-180);
				if (z < 70 || z > 110) {
					Dimension enemy_mov_vec = enemy.getMovementVector(3);
					vector_left.height -= (enemy_mov_vec.height);
					vector_left.width -= (enemy_mov_vec.width);
				}
			}
			if (checkForEnemy(ship.getPosX(), ship.getPosY(), vector_left, ship)) {
				ship.fireLeft();
			}
			break;
		case WATCH_RIGHT:
			Dimension vectorr = ship.getDirectionVector(7000);
			Dimension vector_right = new Dimension(vectorr.height, vectorr.width);
			vector_right.width = vector_right.width * -1;
			Dimension vector_movement_r = ship.getMovementVector(2);
			vector_right.height += (vector_movement_r.height);
			vector_right.width += (vector_movement_r.width);
			if (enemy != null) {
				int z = Math.abs(Math.abs(enemy.getDirection()-ship.getDirection())-180);
				if (z < 70 || z > 110) {
					Dimension enemy_mov_vec = enemy.getMovementVector(3);
					vector_right.height -= (enemy_mov_vec.height);
					vector_right.width -= (enemy_mov_vec.width);
				}
			}
			if (checkForEnemy(ship.getPosX(), ship.getPosY(), vector_right, ship)) {
				ship.fireRight();
			}
			break;
		case CHECK_SPEED:
			if (mode == MODE_NOTHING) {
				if (ship.getVelocity() < MIN_SPEED) {
					int wind_angle = ship.getWindAngle();
					if (wind_angle < WANTED_WIND_DIRECTION) {
						src_direction = ship.getDirection();
						int side = ship.getSideOfWind();
						if (side == PirateShip.STARBORD) {
							mode = MODE_TRY_FALL_OFF_LEFT;
							sensor_stack.push(new Byte(WATCH_LEFT_FALL_OFF));
						} else {
							mode = MODE_TRY_FALL_OFF_RIGHT;
							sensor_stack.push(new Byte(WATCH_RIGHT_FALL_OFF));
						}
					}
				} else if (ship.getVelocity() > WANTED_SPEED) {
					ship.anchor();
					mode = MODE_BREAK;
				}
			} else if (mode == MODE_BREAK) {
				if (ship.getVelocity() < WANTED_SPEED) {
					mode = MODE_NOTHING;
				}
			}
			break;
		}

		// navigation
		if (mode != MODE_STOP && mode != MODE_BREAK && mode != MODE_EVASION_RIGHT && mode != MODE_EVASION_LEFT && !ship.onSails()) {
			ship.setSails();
		}
		if (mode == MODE_EVASION_RIGHT || mode == MODE_FALL_OFF_RIGHT) {
			int direction = ship.getDirection();
			if (direction >= dest_direction && dest_direction > src_direction ||
					direction >= dest_direction && direction < src_direction) {
				//manoever beendet
				mode = MODE_NOTHING;
			} else {
				ship.starbord();
			}
		}
		if (mode == MODE_EVASION_LEFT || mode == MODE_FALL_OFF_LEFT) {
			int direction = ship.getDirection();
			if ((direction <= dest_direction && dest_direction < src_direction) ||
					direction <= dest_direction && direction > src_direction) {
				//manoever beendet
				mode = MODE_NOTHING;
			} else {
				ship.portside();
			}
		}

	}
	
	public boolean checkForBarrier(int x, int y, Dimension vector) {
		GeomTable gt = new GeomTable();
		line = new Line2D.Float(0, 0, (vector.width), (vector.height));
		gt.addGeom(0, new CollisionGeom(line));
		dummy.setGeomTable(gt);
		Collision c = null;
		Iterator it = collisionables.iterator();
		while (it.hasNext()) {
			Collisionable col = ((Collisionable)it.next());
			if (!(col instanceof TileLayer)) {
				continue;
			}
			c = col.checkForCollision(dummy);
			if (c != null) {
				return true;
			}
		}
		return false;
	}

	public boolean checkForEnemy(int x, int y, Dimension vector, PirateShip me) {
		GeomTable gt = new GeomTable();
		line = new Line2D.Float(0, 0, (vector.width), (vector.height));
		gt.addGeom(0, new CollisionGeom(line));
		dummy.setGeomTable(gt);
		Collision c = null;
		Iterator it = collisionables.iterator();
		while (it.hasNext()) {
			Collisionable col = ((Collisionable)it.next());
			if (!(col instanceof ItemLayer)) {
				continue;
			}
			ArrayList items = ((ItemLayer)col).getItems();
			Iterator it2 = items.iterator();
			while (it2.hasNext()) {
				Item item = (Item)it2.next();
				if (item instanceof PirateShip && item != me) {
					c = item.checkForCollision(dummy);
					if (c != null) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
