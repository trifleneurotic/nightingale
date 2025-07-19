
package dev.trifleneurotic.nightingale.tileengine2d.level;

import dev.trifleneurotic.nightingale.resources.ResourceManager;

import java.io.IOException;

/**
 * A factory for creating Level objects.
 */
public class LevelFactory {
	private ResourceManager res_mng;
	
	private static LevelFactory singleton = null;
	
	/**
	 * Instantiates a new level factory.
	 * 
	 * @param _rs_mng the ResourceManager
	 */
	protected LevelFactory(ResourceManager _rs_mng) {
		res_mng = _rs_mng;
	}
	
	/**
	 * New instance.
	 * 
	 * @param _rs_mng the ResourceManager
	 * 
	 * @return the level factory
	 */
	public static LevelFactory newInstance(ResourceManager _rs_mng) {
		if (singleton == null) {
			singleton = new LevelFactory(_rs_mng); 
		}
		return singleton;
	}
	
	/**
	 * Gets a TMX level.
	 * 
	 * @param filename the filename
	 * 
	 * @return the level
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Level getTMXLevel(String filename) throws IOException {
		return new LevelTMX (res_mng.getInputStreamByFileName(filename));
	}
	
	/**
	 * Gets the GZIPTMX level.
	 * 
	 * @param filename the filename
	 * 
	 * @return the level
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Level getGZIPTMXLevel(String filename) throws IOException {
		return new LevelTMX (res_mng.getGZIPInputStreamByFileName(filename));
	}
}
