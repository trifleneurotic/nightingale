
package com.southatlantic.tileengine2d.collision;


/**
 * The listener interface for receiving collision events.
 * The class that is interested in processing a collision
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCollisionListener<code> method. When
 * the collision occurs, that object's appropriate
 * method is invoked.
 */
public interface CollisionListener {
	
	/**
	 * Collision occurred.
	 * 
	 * @param collision the collision
	 */
	public void collisionOccurred(Collision collision);
}
