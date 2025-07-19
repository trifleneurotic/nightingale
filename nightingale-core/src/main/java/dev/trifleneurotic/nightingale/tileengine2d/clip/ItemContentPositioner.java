
package dev.trifleneurotic.nightingale.tileengine2d.clip;

import dev.trifleneurotic.nightingale.core.ContentPositioner;
import dev.trifleneurotic.nightingale.tileengine2d.Item;
import dev.trifleneurotic.nightingale.tileengine2d.util.TileMath;

import java.awt.*;

/**
 * The Class ItemContentPositioner. Moves the visible part of a {@link southatlantic.tileengine2d.TileLayer}
 * depending on the position of an {@link Item}. 
 */
public class ItemContentPositioner implements ContentPositioner {

	private Dimension clip_dimension;
	private Item item;
	private int horizontal_gap;
	private int vertical_gap;
	
	private int current_x;
	private int current_y;
	
	
	/**
	 * Instantiates a new item content positioner.
	 * 
	 * @param _clip_dimension the _clip_dimension
	 * @param _item the _item
	 * @param _horizontal_gap the _horizontal_gap
	 * @param _vertical_gap the _vertical_gap
	 * @param _start_x the _start_x
	 * @param _start_y the _start_y
	 */
	public ItemContentPositioner(Dimension _clip_dimension, Item _item,
                                 int _horizontal_gap, int _vertical_gap, int _start_x, int _start_y) {
		clip_dimension = _clip_dimension;
		item = _item;
		horizontal_gap = _horizontal_gap;
		vertical_gap = _vertical_gap;
		current_x = _start_x;
		current_y = _start_y;
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.core.ContentPositioner#getOffsetX(int)
	 */
	public int getOffsetX(int px_per_tile) {
		int item_pix_x = TileMath.Mt2Px(item.getPosX(), px_per_tile);
		int horizontal_gap_pix = TileMath.Mt2Px(horizontal_gap, px_per_tile);
		current_x = ( item_pix_x < current_x + horizontal_gap_pix ? item_pix_x - horizontal_gap_pix :
			(item_pix_x > current_x - horizontal_gap_pix + clip_dimension.width ? 
						item_pix_x+horizontal_gap_pix - clip_dimension.width : current_x));
		return current_x;
	}

	/* (non-Javadoc)
	 * @see southatlantic.core.ContentPositioner#getOffsetY(int)
	 */
	public int getOffsetY(int px_per_tile) {
		int item_pix_y = TileMath.Mt2Px(item.getPosY(), px_per_tile);
		int vertical_gap_pix = TileMath.Mt2Px(vertical_gap, px_per_tile);
		current_y = ( item_pix_y < current_y + vertical_gap_pix ? item_pix_y - vertical_gap_pix :
			(item_pix_y > current_y - vertical_gap_pix + clip_dimension.height ? 
						item_pix_y+vertical_gap_pix - clip_dimension.height : current_y));
		return current_y;
	}

}
