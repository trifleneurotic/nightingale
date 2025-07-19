
package dev.trifleneurotic.nightingale.tileengine2d.animation;

/**
 * The Class SequenceTable.
 */
public class SequenceTable {
	private int id;
	private int number_repeats;
	private int frame_duration; // in s/100
	private int[] frames; // image indexes
	
	/**
	 * Instantiates a new sequence table.
	 * 
	 * @param _id the id
	 * @param _number_repeats the number of repeats (0 means endless)
	 * @param _frame_duration the duration of one frame
	 * @param _frames the frames
	 */
	public SequenceTable (int _id, int _number_repeats, int _frame_duration, int[] _frames) {
		id = _id;
		number_repeats = _number_repeats;
		frame_duration = _frame_duration;
		frames = _frames;
	}

	/**
	 * Gets the frame.
	 * 
	 * @param index the index
	 * 
	 * @return the frame
	 */
	public int getFrame(int index) {
		return frames[index];
	}
	
	/**
	 * Gets the frame number.
	 * 
	 * @return the frame number
	 */
	public int getFrameNumber() {
		return frames.length;
	}
	
	/**
	 * Gets the repeat number.
	 * 
	 * @return the repeat number
	 */
	public int getRepeatNumber() {
		return number_repeats;
	}

	/**
	 * Gets the duration.
	 * 
	 * @return the duration
	 */
	public int getDuration() {
		return frame_duration;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the default sequence table.
	 * 
	 * @return the default sequence table
	 */
	public static SequenceTable getDefaultSequenceTable() {
		int[] i = new int[1];
		i[0] = 0;
		return new SequenceTable(0, 1, 0, i);
	}

}
