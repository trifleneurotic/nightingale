
package dev.trifleneurotic.nightingale.core;

import java.awt.*;


/**
 * The Interface Layer. A Layer is paintable and updateable object.
 */
public interface Layer {
	
	/**
	 * Paints the layer.
	 * 
	 * @param g the g
	 * @param offset_x the x-offset
	 * @param offset_y the y-offset
	 * @param scale_base the scale_base
	 */
	public void paint(Graphics g, int offset_x, int offset_y, int scale_base);
	
	/**
	 * Updates the layer.
	 * 
	 * @param keystate the current keystate
	 * @param dt the time since last update in msec
	 */
	public void update(InputState inputstate, int dt);
}
