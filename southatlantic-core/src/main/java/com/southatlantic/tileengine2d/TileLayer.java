
package com.southatlantic.tileengine2d;

import com.southatlantic.core.InputState;
import com.southatlantic.core.Layer;
import com.southatlantic.tileengine2d.animation.TileAnimation;
import com.southatlantic.tileengine2d.collision.*;
import com.southatlantic.tileengine2d.level.Level;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Class TileLayer. Manages a tileset and a Map. The size of a tile can be greater of lesser than 
 * (1000 millitiles x 1000 millitiles). 'Millitiles' is generally used as scale basis.
 */
public class TileLayer implements Layer, Collisionable, CollisionListener {
	protected Level level;
	private Image tile_image;
	private int tile_number_x, tile_number_y;  // how many images in tileset-image
	private int image_frame_width, image_frame_height;  // size of one tile in tileset-image (in pixel)
	private int destination_tile_size_x = 1000;
	private int destination_tile_size_y = 1000;  // size of one tile as painted (in millitiles)
	private GeomTable geom_table;
	private TileAnimation[] animations;  // 0 - n-1
	private int[] sx;
	private int[] sy;
	
	/**
	 * Instantiates a new tile layer.
	 * 
	 * @param _tile_image the _tile_image
	 * @param _tile_number_x the _tile_number_x
	 * @param _tile_number_y the _tile_number_y
	 * @param _level the _level
	 */
	public TileLayer(Image _tile_image, int _tile_number_x, int _tile_number_y, Level _level) {
		level = _level;
		tile_image = _tile_image;
		tile_number_x = _tile_number_x;
		tile_number_y = _tile_number_y;
		image_frame_width = tile_image.getWidth(null)/tile_number_x;
		image_frame_height = tile_image.getHeight(null)/tile_number_y;
		sx = new int[tile_number_x*tile_number_y];
		sy = new int[tile_number_x*tile_number_y];
		animations = new TileAnimation[tile_number_x*tile_number_y];
		for (int p = 0; p < tile_number_x*tile_number_y; p++) {
				sy[p] = (p/tile_number_x)*image_frame_height;
				sx[p] = (p%tile_number_x)*image_frame_width;
				animations[p] = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#paint(java.awt.Graphics, int, int, int)
	 */
	public void paint(Graphics g, int win_x, int win_y, int tilesize) {
		int tilesize_x = (tilesize * destination_tile_size_x) / 1000;
		int tilesize_y = (tilesize * destination_tile_size_y) / 1000;		
		g.setColor(Color.blue);
		Rectangle clip = g.getClipBounds();
		if (clip != null) {
			g.fillRect(clip.x, clip.y, clip.width, clip.height);
		}
		int leftColumn = win_x/tilesize_x;
		if (leftColumn < 0) {
			leftColumn = 0;
		}
		int rightColumn = level.getColumnNumber();
		if (clip != null) {
			rightColumn = ((win_x + clip.width) / tilesize_x)+1;
		}
		if (rightColumn > level.getColumnNumber() - 1) { 
			rightColumn = level.getColumnNumber();
		}
		int xOverlaps = win_x - leftColumn*tilesize_x;
		int upperRow = win_y/tilesize_y;
		if (upperRow < 0) {
			upperRow = 0;	
		}
		int downerRow = level.getRowNumber();
		if (clip != null) {
			downerRow = ((win_y + clip.height) / tilesize_y)+1;
		}
		if (downerRow > level.getRowNumber() - 1) {
			downerRow = level.getRowNumber(); 
		}	
		int yOverlaps = win_y - upperRow*tilesize_y;
		int layer_number = level.getLayerNumber();
		int pixX = - xOverlaps;
		int pixY = - yOverlaps;
		byte[] animationcache = computeAnimationCache();
		int image_number = tile_number_x*tile_number_y;
		for (int i = leftColumn; i < rightColumn; i++) {
			for (int j = upperRow; j < downerRow; j++) {
				for (int layer = 0; layer < layer_number; layer++) {
					int tile_index = level.getTile(layer, i, j);
					if (tile_index > 0 && tile_index <= image_number) {
						tile_index = animationcache[tile_index];
						g.drawImage(tile_image, pixX, pixY, 
								pixX+tilesize_x, pixY+tilesize_y,
								sx[tile_index-1], sy[tile_index-1], 
								sx[tile_index-1]+image_frame_width, sy[tile_index-1]+image_frame_height, null);
//						for testing
//						Line2D line = ((SeaTileCollisions)tile_collision_manager).getLine(level.getTile(i, j));
//						if (line != null) {
//						g.setColor(Color.red);
//						g.drawLine(pixX+TileMath.Mt2Px((int)line.getP1().getX(), tilesize), pixY+TileMath.Mt2Px((int)line.getP1().getY(), tilesize), 
//						pixX+TileMath.Mt2Px((int)line.getP2().getX(), tilesize), pixY+TileMath.Mt2Px((int)line.getP2().getY(), tilesize));					
//						}
						// for testing
//						ArrayList recs = ((DefaultTileCollisionManager)tile_collision_manager).getRectangles(tile_index);
//						Iterator it = recs.iterator();
//						g.setColor(Color.red);
//						while (it.hasNext()) {
//							Rectangle rec = (Rectangle) it.next();
//							g.drawRect(pixX+TileMath.Mt2Px(rec.x, tilesize), pixY+TileMath.Mt2Px(rec.y, tilesize), TileMath.Mt2Px(rec.width, tilesize), TileMath.Mt2Px(rec.height, tilesize));
//						}
					}
				}
				pixY += tilesize_y;
			}
			pixY = - yOverlaps;
			pixX += tilesize_x;
		}
	}
	
	/**
	 * Sets the destination tile size (in millitiles).
	 * 
	 * @param x the x
	 * @param y the y
	 */
	public void setDestinationTileSize(int x, int y) {
		destination_tile_size_x = x;
		destination_tile_size_y = y;
	}
	
	/**
	 * Gets the source image tile size.
	 * (how many pixel is 1000 mt in the source)
	 * 
	 * @return the source image tile size
	 */
	public Dimension getSourceImageTileSize() {
		return new Dimension((image_frame_width*1000)/destination_tile_size_x, (image_frame_height*1000)/destination_tile_size_y);
	}


	private byte getFrame(byte index) {
		if (animations[index-1] != null)
			return animations[index-1].getCurrentFrame();
		return index;
	}
	
	private byte[] computeAnimationCache() {
		byte[] cache = new byte[(tile_number_x*tile_number_y)+1];
		for (byte i = (byte)1; i < (tile_number_x*tile_number_y)+1; i++) {
			cache[i] = getFrame((byte)(i));
		}
		return cache;
	}
	
	/**
	 * Sets an animation.
	 * 
	 * @param index the id of the tile for which the animation is used
	 * @param frames a list of tile-ids
	 * @param frame_duration the duration of every frame in msec
	 */
	public void setAnimation(byte index, byte[] frames, int frame_duration) {
		animations[index-1] = new TileAnimation(frames, frame_duration);
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#update(southatlantic.tileengine2d.KeyState, int)
	 */
	public void update(InputState inputstate, int dt) {
		// update animated tiles
		for (int i = 0; i < animations.length; i++) {
			if (animations[i] != null) animations[i].update(dt);
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.Collisionable#checkForCollision(southatlantic.tileengine2d.Item)
	 */
	public Collision checkForCollision(Item item) {
		ArrayList geoms = item.getCollisionGeoms();
		if (geoms == null) return null;
		Iterator it = geoms.iterator();
		int layer_number = level.getLayerNumber();
		while (it.hasNext()) {
			CollisionGeom geom = (CollisionGeom)it.next();
			ArrayList tile_list = geom.getTouchedGridElements(this.destination_tile_size_x, this.destination_tile_size_y);
			Iterator it2 = tile_list.iterator();
			while (it2.hasNext()) {
				int[] coordinates = (int[])it2.next();
				for (int layer = 0; layer < layer_number; layer++) {
					if (!isPassable(level.getTile(layer, coordinates[0], coordinates[1]), 
							coordinates[0]*destination_tile_size_x, coordinates[1]*destination_tile_size_y, geom)) {
						return new Collision(coordinates[0]*destination_tile_size_x + (destination_tile_size_x>>1), 
								coordinates[1]*destination_tile_size_y + (destination_tile_size_y>>1), item, null);
					}
				}
			}
		}
		return null;	
	}

	private boolean isPassable(byte tile, int pos_x, int pos_y, CollisionGeom cg) {
		if (tile == 0) {
			return true;
		}
		ArrayList boxes = geom_table.getGeomsOfFrame(tile-1);
		if (boxes != null) {
			Iterator it = boxes.iterator();
			while (it.hasNext()) {
				CollisionGeom cg2 = CollisionGeom.getAbsoluteGeom((CollisionGeom)it.next(), pos_x, pos_y);
				if (cg2.intersects(cg)) {
					return false;
				}
			}
		}
		return true;
	}

	
	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.CollisionListener#collisionOccurred(southatlantic.tileengine2d.collision.Collision)
	 */
	public void collisionOccurred(Collision collision) {
	}

	/**
	 * Sets the geom table.
	 * 
	 * @param table the new geom table
	 */
	public void setGeomTable(GeomTable table) {
		geom_table = table;
	}

}
