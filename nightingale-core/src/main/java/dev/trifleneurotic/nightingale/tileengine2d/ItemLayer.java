
package dev.trifleneurotic.nightingale.tileengine2d;

import dev.trifleneurotic.nightingale.core.InputState;
import dev.trifleneurotic.nightingale.core.Layer;
import dev.trifleneurotic.nightingale.tileengine2d.collision.Collision;
import dev.trifleneurotic.nightingale.tileengine2d.collision.CollisionListener;
import dev.trifleneurotic.nightingale.tileengine2d.collision.Collisionable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Class ItemLayer. A Layer that manages {@link Item}s.
 */
public class ItemLayer implements Layer, CollisionListener, Collisionable {
	private ArrayList items;
	
	private ArrayList items_to_add;
	private ArrayList items_to_remove;

	/**
	 * Instantiates a new item layer.
	 * 
	 * @param _items the items
	 */
	public ItemLayer(ArrayList _items) {
		items = _items;
		if (items == null) {
			items = new ArrayList();
		}
		items_to_add = new ArrayList();
		items_to_remove = new ArrayList();
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#paint(java.awt.Graphics, int, int, int)
	 */
	public void paint(Graphics clip, int win_x, int win_y, int px_per_tile) {
		Iterator it = items.iterator();
		while (it.hasNext()) {
			((Item)it.next()).paint(clip, win_x, win_y, px_per_tile);
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#update(southatlantic.tileengine2d.InputState, int)
	 */
	public void update(InputState inputstate, int dt) {
		// add and remove items
		if (items_to_add.size() > 0) {
			Iterator it_add = items_to_add.iterator();
			while (it_add.hasNext()) {
				items.add(it_add.next());
			}
			items_to_add = new ArrayList();
		}
		if (items_to_remove.size() > 0) {
			Iterator it_rem = items_to_remove.iterator();
			while (it_rem.hasNext()) {
				items.remove(it_rem.next());
			}
			items_to_remove = new ArrayList();
		}
		// update items
		Iterator it = items.iterator();
		while (it.hasNext()) {
			Item item = ((Item)it.next());
			item.update(inputstate, dt);
			if (item.isGarbage()) {
				item.setUnvisible();
				removeItem(item);
			}
		}
	}
	
	/**
	 * Adds an item.
	 * Todo: this method should take a
	 * 'priority'-parameter that affects 
	 * the paint-order.
	 * 
	 * @param item the item
	 */
	public void addItem(Item item) {
		items_to_add.add(item);
	}
	
	/**
	 * Gets list of items.
	 * 
	 * @return the items
	 */
	public ArrayList getItems() {
		return items;
	}
	
	/**
	 * Removes an item.
	 * 
	 * @param item the item
	 */
	public void removeItem(Item item) {
		items_to_remove.add(item);
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.Collisionable#checkForCollision(southatlantic.tileengine2d.Item)
	 */
	public Collision checkForCollision(Item item) {
		// todo: optimize
		Iterator it = items.iterator();
		Collision c = null;
		while (it.hasNext()) {
			Item otherItem = (Item) it.next();
			if (!item.equals(otherItem)) {
				c = otherItem.checkForCollision(item);
			}
			if (c != null) {
				return c;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.CollisionListener#collisionOccurred(southatlantic.tileengine2d.collision.Collision)
	 */
	public void collisionOccurred(Collision collision) {
		// todo: optimize
		Item item1 = collision.getItem1();
		if (item1 != null && items.contains(item1)) {
			item1.collisionOccurred(collision);
		}
		Item item2 = collision.getItem2();
		if (item2 != null && items.contains(item2)) {
			item2.collisionOccurred(collision);
		}
	}
}
