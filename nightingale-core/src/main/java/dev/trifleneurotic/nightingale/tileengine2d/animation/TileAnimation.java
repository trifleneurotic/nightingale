
package dev.trifleneurotic.nightingale.tileengine2d.animation;

/**
 * The Class TileAnimation.
 */
public class TileAnimation {

	/** The index. */
	byte index = 0;
	
	/** The frames. */
	byte[] frames;
	
	/** The frame_duration. */
	int frame_duration;  //in ms
	
	/** The time. */
	int time = 0;
	
	/**
	 * Instantiates a new tile animation.
	 * 
	 * @param _frames the _frames
	 * @param _frame_duration the _frame_duration
	 */
	public TileAnimation(byte[] _frames, int _frame_duration) {
		frames = _frames;
		frame_duration = _frame_duration;
	}

	/**
	 * Update.
	 * 
	 * @param dt the dt
	 */
	public void update(int dt) {
		time += dt;
		if (time > frame_duration) {
			time -= frame_duration;
			index ++;
			if (index >= frames.length) {
				index = 0;
			}
		}
	}

	/**
	 * Gets the current frame.
	 * 
	 * @return the current frame
	 */
	public byte getCurrentFrame() {
		return frames[index];
	}

}
