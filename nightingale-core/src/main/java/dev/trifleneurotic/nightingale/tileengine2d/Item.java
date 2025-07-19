
package dev.trifleneurotic.nightingale.tileengine2d;

import dev.trifleneurotic.nightingale.core.InputState;
import dev.trifleneurotic.nightingale.core.Layer;
import com.southatlantic.tileengine2d.collision.*;
import dev.trifleneurotic.nightingale.tileengine2d.collision.*;
import dev.trifleneurotic.tileengine2d.collision.*;
import dev.trifleneurotic.nightingale.tileengine2d.util.TileMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * The Class Item.
 */
public class Item implements Layer, CollisionListener, Collisionable {
	
	/** The image. */
	protected Image image;
	
	/** The image_dim, in millitiles. */
	protected Dimension image_dim;
	
	/** The coordinates, in millitiles. */
	protected int posX, posY;
	
	/** True if item is unvisible. */
	protected boolean unvisible = false;
	
	private boolean is_garbage = false;

	private CollisionGuard collision_guard = null;
	
	private int lastX;
	private int lastY;

	/**
	 * Instantiates a new item.
	 * 
	 * @param _image the image
	 * @param _image_size_mt the image-size in mt
	 * @param _posX the x-coordinate
	 * @param _posY the y-coordinate
	 */
	public Item(Image _image, Dimension _image_size_mt, int _posX, int _posY) {
		image = _image;
		image_dim = _image_size_mt;
		posX = _posX;
		posY = _posY;
	}
	
	/**
	 * Paints the item.
	 * 
	 * @param g
	 * @param x_offset the x_offset
	 * @param y_offset the y_offset
	 * @param tilesize the scalefactor
	 */
	public void paint(Graphics g, int x_offset, int y_offset, int tilesize) {
		if (unvisible) return;
		int pixX = TileMath.Mt2Px(posX, tilesize) - x_offset;
		int pixY = TileMath.Mt2Px(posY, tilesize) - y_offset;
		Dimension image_size_pix = new Dimension(TileMath.Mt2Px(image_dim.width, tilesize), TileMath.Mt2Px(image_dim.height, tilesize));
		pixX -= (image_size_pix.width >> 1); // paint at center
		pixY -= (image_size_pix.height >> 1);
		g.drawImage(image, pixX, pixY, image_size_pix.width, image_size_pix.height, null);
	}

	/**
	 * Moves the item.
	 * 
	 * @param dx the dx
	 * @param dy the dy
	 */
	public void move(int dx, int dy) {
		posX += dx; 
		posY += dy;
	}		

	/**
	 * Gets the pos x.
	 * 
	 * @return the pos x
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Gets the pos y.
	 * 
	 * @return the pos y
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Sets the pos x.
	 * 
	 * @param x the new pos x
	 */
	public void setPosX(int x) {
		posX = x;
	}

	/**
	 * Sets the pos y.
	 * 
	 * @param y the new pos y
	 */
	public void setPosY(int y) {
		posY = y;
	}

	/**
	 * Updates the item.
	 * 
	 * @param keystate the current keystate
	 * @param dt the time since last update
	 */
	public void update(InputState inputstate, int dt){
		lastX = posX;
		lastY = posY;
		computeMotion(inputstate, dt);
		if (collision_guard != null && (lastX != posX || lastY != posY))
			collision_guard.checkPosition(this);
	}

	/**
	 * Compute motion. To be implemented by subclasses.
	 * 
	 * @param keystate the keystate
	 * @param dt the dt
	 */
	protected void computeMotion(InputState inputstate, int dt){}

	/**
	 * Gets the {@link CollisionGeom}s of this item.
	 * 
	 * @return the collision geoms
	 */
	protected ArrayList getCollisionGeoms() {
		ArrayList boxes = new ArrayList();
		boxes.add(new CollisionGeom(new Rectangle(posX- (image_dim.width>>1), posY- (image_dim.height>>1), image_dim.width, image_dim.height)));
		return boxes;
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.Collisionable#checkForCollision(southatlantic.tileengine2d.Item)
	 */
	public Collision checkForCollision(Item item) {
		ArrayList geoms1 = getCollisionGeoms();
		if (geoms1 == null ) return null;
		ArrayList geoms2 = item.getCollisionGeoms();
		if (geoms2 == null ) return null;
		Iterator it = geoms1.iterator();
		while (it.hasNext()) {
			CollisionGeom cg1 = (CollisionGeom)it.next();
			Iterator it2 = geoms2.iterator();
			while (it2.hasNext()) {
				CollisionGeom cg2 = (CollisionGeom)it2.next();
				if (cg1.intersects(cg2)) {
					Dimension point_of_collision = cg1.intersection(cg2);
					return new Collision(point_of_collision.width, point_of_collision.height, item, this);
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.CollisionListener#collisionOccurred(southatlantic.tileengine2d.collision.Collision)
	 */
	public void collisionOccurred(Collision collision) {}

	/**
	 * Checks if is this item can be removed.
	 * 
	 * @return true, if is garbage
	 */
	public boolean isGarbage() {
		return is_garbage;
	}
	
	/**
	 * Sets the garbage flag.
	 */
	protected void setGarbageFlag() {
		is_garbage = true;
	}

	/**
	 * Sets the unvisible flag.
	 */
	public void setUnvisible() {
		unvisible = true;
	}
	
	/**
	 * Gets the destination image dimension.
	 * 
	 * @return the destination image dimension
	 */
	protected Dimension getDestinationImageDimension() {
		return image_dim;
	}
	
	/**
	 * Sets the collision guard.
	 * 
	 * @param cg the new collision guard
	 */
	public void setCollisionGuard(CollisionGuard cg) {
		collision_guard = cg;
	}
	
	protected void resetLastPos() {
		posX = lastX;
		posY = lastY;
	}
}
