
package com.southatlantic.menu;

import com.southatlantic.core.InputState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Class MenuLayer. Represents a Menu.
 */
public class MenuLayer extends DialogLayer {

	private ArrayList menu_listener = null;
	
	/** List of {@link MenuPoint}s. */
	protected ArrayList menu_points = null; 

	/** The index of the currently active {@link MenuPoint}. */
	protected int index = -1;
	private Color index_color = null;
	private Color index_bg_color = null;
	private Image index_image = null;
	private Dimension index_offset;
	private Dimension index_size;

	/** The interspace between {@link MenuPoint}s. */
	protected int interspace = 10;

	/**
	 * Instantiates a new MenuLayer.
	 * 
	 * @param _dimension the _dimension
	 */
	public MenuLayer(Dimension _dimension) {
		super(_dimension);
	}
	
	/**
	 * Adds a {@link MenuListener}.
	 * 
	 * @param ml the MenuListener
	 */
	public void addMenuListener(MenuListener ml) {
		if (menu_listener == null) {
			menu_listener = new ArrayList();
		} 
		menu_listener.add(ml);
	}

	/**
	 * Sets the interspace.
	 * 
	 * @param is the new interspace
	 */
	public void setInterspace(int is) {
		interspace = is;
	}

	/**
	 * Adds the point.
	 * 
	 * @param mp the mp
	 */
	public void addPoint(MenuPoint mp) {
		if (menu_points == null) {
			menu_points = new ArrayList();
		}
		menu_points.add(mp);
	}
	
	/**
	 * Gets the {@link MenuPoint} at the specified index.
	 * 
	 * @param i the index
	 * 
	 * @return the MenuPoint
	 */
	public MenuPoint getMenuPoint(int i) {
		return (MenuPoint)menu_points.get(i);
	}

	/**
	 * Sets an index.
	 * 
	 * @param _index_img the _index_img
	 * @param _index_offset the _index_offset
	 * @param _index_size the _index_size
	 * @param startvalue the startvalue
	 */
	public void setIndex(Image _index_img, Dimension _index_offset, Dimension _index_size, int startvalue) {
		index_image = _index_img;
		index_offset = _index_offset;
		index_size = _index_size;
		index = startvalue;
	}

	/**
	 * Sets an index.
	 * 
	 * @param textcolor the textcolor
	 * @param bg_color the bg_color
	 * @param startvalue the startvalue
	 */
	public void setIndex(Color textcolor, Color bg_color, int startvalue) {
		index_color = textcolor;
		index_bg_color = bg_color;
		index = startvalue;
	}

	private void pointSelected(int i) {
		if (menu_listener != null) {
			MenuEvent me = new MenuEvent(this, i, ((MenuPoint)menu_points.get(i)).getText());
			Iterator it = menu_listener.iterator();
			while (it.hasNext()) {
				((MenuListener)it.next()).pointSelected(me);
			}
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.menu.DialogLayer#paint(java.awt.Graphics, int, int, int)
	 */
	public void paint(Graphics g, int ignored_offset_x, int ignored_offset_y, int ignored_scale_base) {
		super.paint(g, ignored_offset_x, ignored_offset_y, ignored_scale_base);
		if (menu_points != null) {
			Iterator it = menu_points.iterator();
			int i = 0;
			while (it.hasNext()) {
				MenuPoint mp = (MenuPoint) it.next();
				if (!isDisabled() && i == index) {
					if (index_bg_color != null) {
						g.setColor(index_bg_color);
						g.fillRect(gapLeft, paintBottom +2, mp.getWidth(), mp.getHeight());
					}
					if (index_image != null) {
						g.drawImage(index_image, gapLeft + index_offset.width, paintBottom + index_offset.height,
							index_size.width, index_size.height, null);
					} 
				}
				if (!isDisabled() && i == index && index_color != null) {
					mp.paintIndexed(g, gapLeft, paintBottom, index_color);
				} else {
					mp.paint(g, gapLeft, paintBottom);
				}
				paintBottom += mp.getHeight()+interspace;
				i++;
			}
		}	
	}

	/* (non-Javadoc)
	 * @see southatlantic.menu.DialogLayer#update(southatlantic.tileengine2d.InputState, int)
	 */
	public void update(InputState inputstate, int dt) {
		if (isDisabled()) return;
		// indexbehandlung
		if (index != -1) {
			if (inputstate.isOncePressed(InputState.KEY_DOWN)) {
				if (index >= menu_points.size()-1) {
					index = 0;
				} else {
					index++;
				}
			}
			if (inputstate.isOncePressed(InputState.KEY_UP)) {
				if (index == 0) {
					index = menu_points.size()-1;
				} else {
					index--;
				}
			}
			if (inputstate.isOncePressed(InputState.KEY_SPACE) || inputstate.isOncePressed(InputState.KEY_ENTER)) {
				pointSelected(index);
			}
		}
		if (inputstate.isMouseOncePressed(InputState.BUTTON_LEFT)) {
			if (inputstate.isMouseIn(0,0,dim.width, dim.height)) {
				if (isDisabled()) {
					setDisabled(false);
				}
				if (menu_points != null) {
					Iterator it = menu_points.iterator();
					int i = 0;
					int point_height = getHeadlineHeight() + headlineGap;
					while (it.hasNext()) {
						MenuPoint mp = (MenuPoint) it.next();
						if (inputstate.isMouseIn(gapLeft, gapTop + i*interspace + point_height,
								mp.getWidth(), mp.getHeight())) {
							index = i;
							pointSelected(i);
						}
						point_height += mp.getHeight();
						i++;
					}
				}	
			}
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.menu.DialogLayer#getHeight()
	 */
	public int getHeight() {
		return super.getHeight() + menu_points.size()*interspace + getAllMenuPointsHeight();
	}
	
	private int getAllMenuPointsHeight() {
		int h = 0;
		Iterator it = menu_points.iterator();
		while (it.hasNext()) {
			h += ((MenuPoint)it.next()).getHeight();
		}
		return h;
	}

}
