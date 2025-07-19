
package dev.trifleneurotic.nightingale.tileengine2d.level;

/**
 * The Interface Level. Represents a tilemap.
 */
public interface Level {
	
	/**
	 * Gets the column number.
	 * 
	 * @return the column number
	 */
	public int getColumnNumber();
	
	/**
	 * Gets the row number.
	 * 
	 * @return the row number
	 */
	public int getRowNumber();
	
	/**
	 * Gets the tile at the specified position.
	 * 
	 * @param layer the layer
	 * @param i the i
	 * @param j the j
	 * 
	 * @return the tile
	 */
	public byte getTile(int layer, int i, int j);
	
	/**
	 * Gets the image-name of the tileset.
	 * 
	 * @return the tilesets image name
	 */
	public String getTilesetImageName();
	
	/**
	 * Gets the number of layers.
	 * 
	 * @return the layer number
	 */
	public int getLayerNumber();

	public void setTile(int layer, int i, int j, byte value);
}
