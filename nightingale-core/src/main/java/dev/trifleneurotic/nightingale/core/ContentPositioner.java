
package dev.trifleneurotic.nightingale.core;

/**
 * The Interface ContentPositioner. A ContentPositioner provides the current coordinates
 * of the visible part of a {@link GamePanel}.
 */
public interface ContentPositioner {
	
	/**
	 * Gets the x-offset.
	 * 
	 * @param scale_base the scale_base
	 * 
	 * @return the x-offset
	 */
	public int getOffsetX(int scale_base);
	
	/**
	 * Gets the y-offset.
	 * 
	 * @param scale_base the scale_base
	 * 
	 * @return the y-offset
	 */
	public int getOffsetY(int scale_base);
}
