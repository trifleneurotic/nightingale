package com.southatlantic.examples.buccaneerbattle;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;

import com.southatlantic.core.InputState;
import com.southatlantic.sound.AudioPlayer;
import com.southatlantic.tileengine2d.Item;
import com.southatlantic.tileengine2d.Sprite;
import com.southatlantic.tileengine2d.animation.SequenceTable;
import com.southatlantic.tileengine2d.collision.Collision;


class Cannonball extends Sprite {
	private static final int IMAGE_SIZE_X = 500;
	private static final int IMAGE_SIZE_Y = 500;
	
	private static final int FLYING = 0;
	private static final int FLAMING = 1;
	private static final int SPLASHING = 2;
	private static final int POWDERING = 3;

	private static final String SOUND_SPLASH = "splash2.mp3";
	
	int flyingTime;
	int maxTime;
	
	private Dimension movement_vector; //in millitiles per second
	
	private Item source_item;
	
	private static ArrayList shot_sequence_tables = null;
	
	private int tmp_dx, tmp_dy = 0;
	
	private AudioPlayer player;
	
	private static ArrayList createSequenceTables() {
		if (shot_sequence_tables == null) {
			shot_sequence_tables = new ArrayList();
			int[] i1 = new int[1];
			i1[0] = 0;
			shot_sequence_tables.add(new SequenceTable(FLYING, 0, 0, i1 ));
			int[] i2 = new int[1];
			i2[0] = 2;
			shot_sequence_tables.add(new SequenceTable(FLAMING, 1, 12, i2 ));
			int[] i3 = new int[2];
			i3[0] = 3;
			i3[1] = 4;
			shot_sequence_tables.add(new SequenceTable(SPLASHING, 1, 40, i3 ));
			int[] i4 = new int[1];
			i4[0] = 1;
			shot_sequence_tables.add(new SequenceTable(POWDERING, 1, 7, i4 ));
		}
		return shot_sequence_tables;
	}
	
	public Cannonball(Image img, int posX, int posY,  
			Dimension _movement_vector,
			int _maxTime, Item _source_item, AudioPlayer _player){
		super(img, new Dimension(IMAGE_SIZE_X, IMAGE_SIZE_Y), posX, posY, 
				5, 1, Cannonball.createSequenceTables());
		movement_vector = _movement_vector;
		maxTime = _maxTime;
		setSequence(0, POWDERING, 0);
		flyingTime = 0;
		source_item = _source_item;
		player = _player;
		
		// some gap to the ship
		if (movement_vector != null) {
			int dx = (300 * movement_vector.width)>>10;
			int dy = (300 * movement_vector.height)>>10;
			move(dx, dy);
		}
	}
	
	protected void computeAnimation(InputState inputstate, int dt) {
		int seq_id = getCurrentSequenceId(0);
		if (seq_id == POWDERING && getCurrentSequence(0).isFinished()) {
			setSequence(0, FLYING,0);
			move(tmp_dx, tmp_dy);
			// size of dest_image
			image_dim.setSize(IMAGE_SIZE_X - (Math.abs(flyingTime-(maxTime>>1))*100/(maxTime>>1))-200, 
					IMAGE_SIZE_Y - (Math.abs(flyingTime-(maxTime>>1))*100/(maxTime>>1))-200);
		}
		else if (seq_id == FLYING) {
			flyingTime += dt;
			if (flyingTime > maxTime) {
				setSequence(0, SPLASHING,0);
				// size of dest_image
				image_dim.setSize(IMAGE_SIZE_X, 
						IMAGE_SIZE_Y);
				if (player != null) {
					player.playSound(SOUND_SPLASH, false);
				}
			} else {
				// size of dest_image
				image_dim.setSize(IMAGE_SIZE_X - (Math.abs(flyingTime-(maxTime>>1))*100/(maxTime>>1))-200, 
						IMAGE_SIZE_Y - (Math.abs(flyingTime-(maxTime>>1))*100/(maxTime>>1))-200);
			}
		}
		else if ((seq_id == SPLASHING || seq_id == FLAMING) && getCurrentSequence(0).isFinished()) {
			setGarbageFlag();
		}
	}
	
	protected void computeMotion(InputState inputstate, int dt) {
		int seq_id = getCurrentSequenceId(0);
		if (seq_id == POWDERING || seq_id == FLYING) {
			int dx = (movement_vector.width * dt) >> 10;
			int dy = (movement_vector.height * dt) >> 10;
			if (getCurrentSequenceId(0) == POWDERING) {
				tmp_dx += dx;
				tmp_dy += dy;
			}
			else if (getCurrentSequenceId(0) == FLYING) {
				move(dx, dy);
			}
		}
	}
	
	public void collisionOccurred(Collision collision) {
		if (!(collision.getEnemyItem(this) == getSourceItem()) && !(collision.getEnemyItem(this) instanceof Cannonball)) {
			if (collision.getEnemyItem(this) instanceof PirateShip) {
				setSequence(0, FLAMING, 0);
				// size of dest_image
				image_dim.setSize(IMAGE_SIZE_X, 
						IMAGE_SIZE_Y);
			} else {
				setGarbageFlag();
			}
		}
	}
	
	// for testing
//	public void paint(Graphics g, int winx, int winy, int tilesize) {
//		super.paint(g, winx, winy, tilesize);
//		ArrayList boxes = this.getBoundingBoxMt();
//		g.setColor(Color.red);
//		Iterator it = boxes.iterator();
//		while (it.hasNext()) {
//			Rectangle box = (Rectangle)it.next();
//			g.drawRect(TileMath.Mt2Px(box.x, tilesize)-winx, TileMath.Mt2Px(box.y, tilesize)-winy, 
//					TileMath.Mt2Px(box.width, tilesize), TileMath.Mt2Px(box.height, tilesize));
//		}
//	}

	public Item getSourceItem() {
		return source_item;
	}
}
