
package com.southatlantic.menu;

import com.southatlantic.core.InputState;
import com.southatlantic.core.Layer;

import java.awt.*;

/**
 * The Class DialogLayer. 
 */
public class DialogLayer implements Layer {

	/** The bg_color. */
	protected Color bgColor = null;
	
	/** The background. */
	protected Image background = null;

	/** The gap_top. */
	protected int gapTop = 20;
	
	/** The gap_left. */
	protected int gapLeft = 20;

	/** The dim. */
	protected Dimension dim;

	/** The paint_bottom. */
	protected int paintBottom;
	
	/** The headline_image. */
	protected Image headlineImage = null;
	
	/** The headline_size. */
	protected Dimension headlineSize = null;
	
	/** The headline_string. */
	protected String headlineString = null;
	
	/** The headline_font. */
	protected Font headlineFont = null;
	
	/** The headline_fontmetrics. */
	protected FontMetrics headlineFontmetrics = null;
	
	/** The headline_color. */
	protected Color headlineColor = null;
	
	/** The headline_gap. */
	protected int headlineGap;
	
	private boolean disabled = false;

	/**
	 * Instantiates a new dialog layer.
	 * 
	 * @param _dimension the _dimension
	 */
	public DialogLayer(Dimension _dimension) {
		dim = _dimension;
	}
	
	/**
	 * Sets the dimension.
	 * 
	 * @param d the new dimension
	 */
	public void setDimension(Dimension d) {
		dim = d;
	}
	
	/**
	 * Sets the background image.
	 * 
	 * @param bg_image the new background image
	 */
	public void setBackgroundImage(Image bg_image) {
		background = bg_image;
	}
	
	/**
	 * Sets the background color.
	 * 
	 * @param bg the new background color
	 */
	public void setBackgroundColor(Color bg) {
		bgColor = bg;
	}
	
	/**
	 * Sets the gap.
	 * 
	 * @param x the x
	 * @param y the y
	 */
	public void setGap(int x, int y) {
		gapLeft = x;
		gapTop = y;
	}
	
	/**
	 * Sets the headline.
	 * 
	 * @param _img the _img
	 * @param _size the _size
	 * @param _gap the _gap
	 */
	public void setHeadline(Image _img, Dimension _size, int _gap) {
		if (headlineString != null) {
			headlineString = null;
			headlineFont = null;
			headlineColor = null;
		}
		headlineImage = _img;
		headlineSize = _size;
		headlineGap = _gap;
	}

	/**
	 * Sets the headline.
	 * 
	 * @param _str the _str
	 * @param _font the _font
	 * @param _fontmetrics the _fontmetrics
	 * @param _color the _color
	 * @param _gap the _gap
	 */
	public void setHeadline(String _str, Font _font, FontMetrics _fontmetrics, Color _color, int _gap) {
		if (headlineImage != null) {
			headlineImage = null;
			headlineSize = null;
		}
		headlineString = _str;
		headlineFont = _font;
		headlineFontmetrics = _fontmetrics;
		headlineColor = _color;
		headlineGap = _gap;
	}
	
	/**
	 * Gets the headline text.
	 * 
	 * @return the headline text
	 */
	public String getHeadlineText() {
		return headlineString;
	}

	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#paint(java.awt.Graphics, int, int, int)
	 */
	public void paint(Graphics g, int ignored_offset_x, int ignored_offset_y, int ignored_scale_base) {
		if (background != null) {
			g.drawImage(background, 0,0, dim.width,
					dim.height, null);
		} else if (bgColor != null){
			g.setColor(bgColor);
			g.fillRect(0, 0, dim.width, dim.height);
		}
		paintBottom = gapTop;
		if (headlineImage != null) {
			g.drawImage(headlineImage, (dim.width>>1)-(headlineImage.getWidth(null)>>1),
					paintBottom, headlineSize.width, headlineSize.height, null);
			paintBottom += headlineSize.height;
			paintBottom += headlineGap;
		} else if (headlineString != null) {
			paintBottom += headlineFontmetrics.getHeight();
			g.setFont(headlineFont);
			g.setColor(headlineColor);
			g.drawString(headlineString,
					(dim.width>>1)-((((int) headlineFontmetrics.getStringBounds(headlineString, g).getWidth())>>1)),
					paintBottom);
			paintBottom += headlineGap;
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#update(southatlantic.tileengine2d.InputState, int)
	 */
	public void update(InputState inputstate, int dt) {}

	/**
	 * Disables/Enables the layer.
	 * 
	 * @param b 
	 */
	public void setDisabled(boolean b) {
		disabled = b;
	}

	/**
	 * Checks if is disabled.
	 * 
	 * @return true, if is disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * Gets the height of the dialog-content.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return gapTop + getHeadlineHeight() + headlineGap;
	}
	
	/**
	 * Gets the headline height.
	 * 
	 * @return the headline height
	 */
	protected int getHeadlineHeight() {
		int hh = 0;
		if (headlineImage != null) {
			hh = headlineSize.height;
		} else if (headlineString != null) {
			hh = headlineFontmetrics.getHeight();
		}
		return hh;
	}

}
