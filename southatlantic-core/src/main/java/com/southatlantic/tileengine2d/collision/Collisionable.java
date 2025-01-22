
package com.southatlantic.tileengine2d.collision;

import com.southatlantic.tileengine2d.Item;

/**
 * Collisionables are things that can collide with an item.
 */
public interface Collisionable {
	
	/**
	 * Check for collision. 
	 * Todo: this method should return a list instead
	 * of a single Collision
	 * 
	 * @param item the item
	 * 
	 * @return the collision or null
	 */
	public Collision checkForCollision(Item item);
}
