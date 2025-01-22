
package com.southatlantic.tileengine2d.collision;

import com.southatlantic.tileengine2d.Item;

import java.util.ArrayList;

/**
 * A CollisionGuard manages a list of CollisionListener and a list of Collisionables.
 */
public interface CollisionGuard {
	
	/**
	 * Check position.
	 * 
	 * @param item the item
	 */
	public void checkPosition(Item item);
	
	/**
	 * Adds the collision listener.
	 * 
	 * @param cl the cl
	 */
	public void addCollisionListener(CollisionListener cl);
	
	/**
	 * Adds the collisionable.
	 * 
	 * @param cbl the cbl
	 */
	public void addCollisionable(Collisionable cbl);
	
	/**
	 * Gets the collisionables.
	 * 
	 * @return the collisionables
	 */
	public ArrayList getCollisionables();
}
