
package dev.trifleneurotic.nightingale.menu;

import java.awt.*;

/**
 * The Class MessageLayer.
 */
public class MessageLayer extends DialogLayer {

	private String[] message = null;
	private Font message_font;
	private FontMetrics message_fm;
	private Color message_color;
	
	private int gap_right = 0;
	
	private int interspace = 0;
	
	/**
	 * Instantiates a new message layer.
	 * 
	 * @param _dimension the _dimension
	 */
	public MessageLayer(Dimension _dimension) {
		super(_dimension);
	}
	
	/**
	 * Sets the message.
	 * 
	 * @param _text the _text
	 * @param _font the _font
	 * @param _fm the _fm
	 * @param _color the _color
	 */
	public void setMessage(String _text, Font _font, FontMetrics _fm, Color _color) {
		message = _text.split(" ");
		message_font = _font;
		message_fm = _fm;
		message_color = _color;
	}
	
	/**
	 * Sets the interspace.
	 * 
	 * @param _interspace the new interspace
	 */
	public void setInterspace(int _interspace) {
		interspace = _interspace;
	}
	
	/**
	 * Sets the right gap.
	 * 
	 * @param gap the new gap right
	 */
	public void setGapRight(int gap) {
		gap_right = gap;
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.menu.DialogLayer#paint(java.awt.Graphics, int, int, int)
	 */
	public void paint(Graphics g, int ignored_offset_x, int ignored_offset_y, int ignored_scale_base) {
		super.paint(g, ignored_offset_x, ignored_offset_y, ignored_scale_base);
		g.setFont(message_font);
		g.setColor(message_color);
		if (message != null) {
			String line = "";
			int i = 0;
			while (i < message.length) {
				if (message_fm.stringWidth(message[i]) >= (dim.width- gapLeft -gap_right)) { // if single word is too big
					line += " " + message[i];  
					i++;
				} else {
					while (i < message.length && !message[i].equals(new Character('\n').toString()) && 
							message_fm.stringWidth(line + " " + message[i]) < (dim.width- gapLeft -gap_right)){
						line += " " + message[i];
						i++;
					}
				}
				if (i < message.length && message[i].equals(new Character('\n').toString())) {
					i++;
				}
				g.drawString(line, gapLeft, paintBottom);
				paintBottom += message_fm.getHeight() + interspace;
				line = "";
			}
		}
	}
}
