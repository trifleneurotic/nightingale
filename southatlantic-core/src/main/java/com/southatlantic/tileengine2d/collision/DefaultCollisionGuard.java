
package com.southatlantic.tileengine2d.collision;

import com.southatlantic.tileengine2d.Item;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A CollisionGuard.
 */
public class DefaultCollisionGuard implements CollisionGuard {

	private ArrayList collision_listener_list;
	private ArrayList collisionable_list;

	/**
	 * Instantiates a new DefaultCollisionGuard.
	 * 
	 */
	public DefaultCollisionGuard() {
		collision_listener_list = new ArrayList();
		collisionable_list = new ArrayList();
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.CollisionGuard#addCollisionListener(southatlantic.tileengine2d.collision.CollisionListener)
	 */
	public void addCollisionListener(CollisionListener cl) {
		collision_listener_list.add(cl);
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.CollisionGuard#addCollisionable(southatlantic.tileengine2d.collision.Collisionable)
	 */
	public void addCollisionable(Collisionable cl) {
		collisionable_list.add(cl);
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.CollisionGuard#getCollisionables()
	 */
	public ArrayList getCollisionables() {
		return collisionable_list;
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.collision.CollisionGuard#checkPosition(southatlantic.tileengine2d.Item)
	 */
	public void checkPosition(Item item) {
		Collision c = null;
		Iterator it = collisionable_list.iterator();
		while (it.hasNext()) {
			c = ((Collisionable)it.next()).checkForCollision(item);
			if (c != null) {
				Iterator it2 = collision_listener_list.iterator();
				while (it2.hasNext()) {
					((CollisionListener)it2.next()).collisionOccurred(c);
				}
			}
		}
	}

}
