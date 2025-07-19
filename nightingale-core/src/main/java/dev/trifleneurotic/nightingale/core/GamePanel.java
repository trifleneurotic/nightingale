
package dev.trifleneurotic.nightingale.core;


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * GamePanel is a {@link Layer} that manages a list of other Layers, a clip for the managed Layers
 * (for example left half of the panel), content-coordinates (which define the point of the Layers' 
 * content that match (0,0) of the clip) optionally managed by a ContentPositioner, and a scale_base.
 * 
 */
public class GamePanel implements Layer {
	// clip position and size
	private Rectangle clip;
	
	// content-position
	private int offset_x, offset_y;
	private ContentPositioner content_positioner = null;

	// scale_base
	private int scale_base = 1;
	
	private ArrayList layer_list = new ArrayList();
	
	/**
	 * Instantiates a new game panel.
	 * 
	 * @param _clip the _clip
	 */
	public GamePanel(Rectangle _clip) {
		clip = _clip;
		offset_x = 0;
		offset_y = 0;
	}
	
	/**
	 * Sets the clip.
	 * 
	 * @param _clip the new clip
	 */
	public void setClip(Rectangle _clip) {
		clip = _clip;
	}

	/**
	 * Gets the clip.
	 * 
	 * @return the panel rec
	 */
	public Rectangle getClip() {
		return clip;
	}
	
	/**
	 * Sets the scale base.
	 * 
	 * @param sb the new scale base
	 */
	public void setScaleBase(int sb) {
		scale_base = sb;
	}
	
	/**
	 * Sets the clip positioner.
	 * 
	 * @param cp the new clip positioner
	 */
	public void setContentPositioner(ContentPositioner cp) {
		content_positioner = cp;
	}

	/**
	 * Adds a layer. Layers are painted on top of each other, in the order
	 * they have been added.
	 * 
	 * @param _layer the layer
	 */
	public void addLayer(Layer _layer) {
		layer_list.add(_layer);
	}
		
	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#paint(java.awt.Graphics, int, int, int)
	 */
	public void paint (Graphics g, int ignored_offset_x, int ignored_offset_y, int ignored_scale_base) {
		Graphics g_clip = g.create(clip.x, clip.y, 
				clip.width, clip.height);
		Iterator it = layer_list.iterator();
		while (it.hasNext()) {
			((Layer)it.next()).paint(g_clip, offset_x, offset_y, scale_base);
		}
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.core.Layer#update(southatlantic.tileengine2d.InputState, int)
	 */
	public void update(InputState inputstate, int dt) {
		inputstate.addOffset(clip.x, clip.y);
		Iterator it = layer_list.iterator();
		while (it.hasNext()) {
			((Layer)it.next()).update(inputstate, dt);
		}
		inputstate.addOffset(-clip.x, -clip.y);
		if (content_positioner != null) {
			offset_x = content_positioner.getOffsetX(scale_base);
			offset_y = content_positioner.getOffsetY(scale_base);
		}
	}
	
	/**
	 * Gets the layer at index position.
	 * 
	 * @param index the index
	 * 
	 * @return the layer
	 */
	public Layer getLayer(int index) {
		return ((Layer)layer_list.get(index));
	}

	/**
	 * Gets the panel clip dimension.
	 * 
	 * @return the panel clip dimension
	 */
	public Dimension getClipDimension() {
		return new Dimension(clip.width, clip.height);
	}
	
	/**
	 * Gets the content-position.
	 * 
	 * @return the content-position
	 */
	public Dimension getContentPosition() {
		return new Dimension(offset_x, offset_y);
	}
	
	/**
	 * Sets the content-position.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setContentPosition(int x, int y) {
		offset_x = x;
		offset_y = y;
	}

	/**
	 * Removes all layers.
	 */
	public void removeLayers() {
		layer_list = new ArrayList();
	}
}
