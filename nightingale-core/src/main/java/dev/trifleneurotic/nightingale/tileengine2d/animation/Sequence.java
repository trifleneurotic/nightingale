
package dev.trifleneurotic.nightingale.tileengine2d.animation;

/**
 * The Class Sequence.
 */
public class Sequence {
	private SequenceTable sequence_table;

	private int current_index;
	private boolean sequence_finished;
	private int timer;
	private int loops;
	
	private boolean visible;
	private int offset = 0;
	
	/**
	 * Instantiates a new sequence.
	 * 
	 * @param _sequence_table the _sequence_table
	 * @param _visible the _visible
	 * @param _offset the _offset
	 */
	public Sequence (SequenceTable _sequence_table, boolean _visible, int _offset) {
		sequence_table = _sequence_table;
		visible = _visible;
		offset = _offset;
		timer = 0;
		loops = 0;
		current_index = 0;
		sequence_finished = false;
	}

	/**
	 * Compute image frame.
	 * 
	 * @param dt the dt
	 */
	public void computeImageFrame(int dt) {
		if (sequence_finished) {
			return;
		}
		if (sequence_table.getFrameNumber() == 1) {
			// sequence besteht nur aus einem frame
			if (sequence_table.getDuration() == 0) {
				// statische sequence
				return;
			}
		}
		timer += dt/10;  // in hundertstel sec
		if (timer > sequence_table.getDuration()) {
        // frame has to change
			current_index++;
			if (current_index >= sequence_table.getFrameNumber()) {
				// 	end of animation is reached
				loops++;
				if (loops < sequence_table.getRepeatNumber() || sequence_table.getRepeatNumber() == 0) {
					// animation is not finished and starts over
					current_index = 0;
				}
				else {
					// stay on last frame	
					current_index--;
					sequence_finished = true;
				}
			}
			timer = 0;
		}
	}

	/**
	 * Sets the visibility.
	 * 
	 * @param v the new visibility
	 */
	public void setVisibility(boolean v) {
		visible = v;
	}

	/**
	 * Checks if is visible.
	 * 
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Gets the current frame.
	 * 
	 * @return the current frame
	 */
	public int getCurrentFrame() {
		return sequence_table.getFrame(current_index) + offset;
	}
	
	/**
	 * Sets the offset.
	 * 
	 * @param o the new offset
	 */
	public void setOffset(int o) {
		offset = o;
	}
	
	/**
	 * Gets the offset.
	 * 
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public int getSequenceId() {
		return sequence_table.getId();
	}
	
	/**
	 * Checks if is finished.
	 * 
	 * @return true, if is finished
	 */
	public boolean isFinished() {
		return sequence_finished;
	}
}
