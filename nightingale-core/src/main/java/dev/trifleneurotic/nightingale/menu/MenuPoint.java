
package dev.trifleneurotic.nightingale.menu;

import java.awt.*;

/**
 * The Class MenuPoint.
 */
public class MenuPoint {
	private Image image = null;
	private Dimension dim = null;
	
	private String string = null;
	private Font font = null;
	private FontMetrics fm = null;
	private Color color = null;

	/**
	 * Instantiates a new MenuPoint.
	 * 
	 * @param img the Image
	 * @param _dim the Dimension
	 */
	public MenuPoint(Image img, Dimension _dim) {
		image = img;
		dim = _dim;
	}

	/**
	 * Instantiates a new MenuPoint.
	 * 
	 * @param _string the _string
	 * @param _font the _font
	 * @param _fm the _fm
	 * @param _color the _color
	 */
	public MenuPoint(String _string, Font _font, FontMetrics _fm, Color _color) {
		string = _string;
		font = _font;
		fm = _fm; 
		color = _color;
	}
	
	/**
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		if (image != null) {
			return dim.width;
		}
		return fm.stringWidth(string); 
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		if (image != null) {
			return dim.height;
		}
		return fm.getHeight(); 
	}

	/**
	 * Paints the MenuPoint.
	 * 
	 * @param g
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public void paint(Graphics g, int x, int y) {
		if (image != null) {
			g.drawImage(image, x, y, dim.width, dim.height, null);
		} else {
			g.setFont(font);
			g.setColor(color);
			g.drawString(string, x, y + this.getHeight());
		}
	}

	/**
	 * This method is called instead of {@link #paint(Graphics, int, int)}, if
	 * the MenuPoint is the current-active MenuPoint in the {@link MenuLayer}-context.
	 * 
	 * @param g 
	 * @param x 
	 * @param y 
	 * @param index_color the index-color
	 */
	public void paintIndexed(Graphics g, int x, int y, Color index_color) {
		Color tmp = color;
		color = index_color;
		paint(g, x, y);
		color = tmp;
	}
	
	/**
	 * Sets the text.
	 * 
	 * @param _str the new text
	 */
	public void setText(String _str) {
		string = _str;
	}
	
	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return string;
	}

	/**
	 * Gets the font metrics.
	 * 
	 * @return the font metrics
	 */
	public FontMetrics getFontMetrics() {
		return fm;
	}
}
