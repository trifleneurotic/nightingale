
package com.southatlantic.tileengine2d.util;

/**
 * The Class TileMath.
 */
public final class TileMath {
	
	/**
	 * Converts millitiles to pixel.
	 * 
	 * @param millitiles the millitiles
	 * @param tilesize the tilesize
	 * 
	 * @return the int
	 */
	public static int Mt2Px(int millitiles, int tilesize) {
		return (millitiles*tilesize)/1000;
	}
	
	/**
	 * Converts pixel to millitiles.
	 * 
	 * @param pixel the pixel
	 * @param tilesize the tilesize
	 * 
	 * @return the int
	 */
	public static int Px2Mt(int pixel, int tilesize) {
		return (1000*pixel)/tilesize;
	}

	/**
	 * Gets the position within a tile.
	 * 
	 * @param pos the pos
	 * @param tile_size the tile_size
	 * 
	 * @return the relative position
	 */
	public static int getRelativePosition(int pos, int tile_size) {
		return pos % tile_size;
	}

}
