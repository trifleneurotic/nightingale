
package com.southatlantic.tileengine2d.collision;

import com.southatlantic.tileengine2d.Item;

/**
 * A Collision between 2 {@link Item}s or an Item and another object.
 */
public class Collision {
	private int x, y;
	private Item item1, item2;
	
	/**
	 * Instantiates a new collision.
	 * 
	 * @param _x the _x
	 * @param _y the _y
	 * @param _item1 the _item1
	 * @param _item2 the _item2
	 */
	public Collision(int _x, int _y, Item _item1, Item _item2) {
		x = _x;
		y = _y;
		item1 = _item1;
		item2 = _item2;
	}
	
	/**
	 * Gets the enemy item.
	 * 
	 * @param it an item
	 * 
	 * @return the item that not equals it
	 */
	public Item getEnemyItem(Item it) {
		if (item1.equals(it)) {
			return item2;
		}
		return item1;
	}

	/**
	 * Gets the item1.
	 * 
	 * @return the item1
	 */
	public Item getItem1() {
		return item1;
	}

	/**
	 * Gets the item2.
	 * 
	 * @return the item2
	 */
	public Item getItem2() {
		return item2;
	}
	
	/**
	 * Gets the x-coordinate.
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y-coordinate.
	 * 
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
