
package com.southatlantic.tileengine2d;

import com.southatlantic.core.InputState;
import com.southatlantic.tileengine2d.animation.Sequence;
import com.southatlantic.tileengine2d.animation.SequenceTable;
import com.southatlantic.tileengine2d.collision.CollisionGeom;
import com.southatlantic.tileengine2d.collision.GeomTable;
import com.southatlantic.tileengine2d.util.TileMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * The Class Sprite. A Sprite is an Item that can have one or more 
 * animationlayers defined in {@link Sequence}s.
 */
public class Sprite extends Item {

	private int image_frame_width, image_frame_height;    // in pix
	
	/** The number of image frames. */
	protected int num_image_frames_x, num_image_frames_y; 

	/** The sequence_tables. */
	protected ArrayList sequence_tables;
	
	/** The layers. */
	protected ArrayList layer;
		
	private GeomTable geom_table = null;
	
	/**
	 * Instantiates a new sprite.
	 * 
	 * @param img the img
	 * @param image_size the image-size when painted in mt
	 * @param posX the pos x
	 * @param posY the pos y
	 * @param _num_image_frames_x the number of image frames x
	 * @param _num_image_frames_y the number of image frames y
	 * @param _sequence_tables the _sequence_tables
	 */
	public Sprite(Image img, Dimension image_size, int posX, int posY, 
			int _num_image_frames_x, int _num_image_frames_y, ArrayList _sequence_tables) {
		super(img, image_size, posX, posY);
		num_image_frames_x = _num_image_frames_x;
		num_image_frames_y = _num_image_frames_y;
		if (img != null) {
			image_frame_width = img.getWidth(null)/num_image_frames_x;
			image_frame_height = img.getHeight(null)/num_image_frames_y;
		}
		sequence_tables = _sequence_tables;
		if (sequence_tables == null) {
			sequence_tables = new ArrayList();
			sequence_tables.add(SequenceTable.getDefaultSequenceTable());
		}
		layer = new ArrayList();
	}
	
	/**
	 * Sets the geom table.
	 * 
	 * @param _geom_table the new geom table
	 */
	public void setGeomTable(GeomTable _geom_table) {
		geom_table = _geom_table;
	}
	
	/**
	 * Gets the source image tile size.
	 * (how many pixel is 1000 millitiles in the source)
	 * 
	 * @return the source image tile size
	 */
	public Dimension getSourceImageTileSize() {
		return new Dimension((image_frame_width*1000)/image_dim.width, (image_frame_height*1000)/image_dim.height);
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.Item#getCollisionGeoms()
	 */
	protected ArrayList getCollisionGeoms() {
		GeomTable table = getGeomTable();
		if (table != null) {
			ArrayList geoms = new ArrayList();
			Iterator it = layer.iterator();
			while (it.hasNext()) {
				Sequence sq = (Sequence)it.next();
				Iterator it2 = table.getGeomsOfFrame(sq.getCurrentFrame()).iterator();
				while (it2.hasNext()) {
					CollisionGeom geom = (CollisionGeom)it2.next();
					geoms.add(CollisionGeom.getAbsoluteGeom(geom, posX-(image_dim.width>>1), posY-(image_dim.height>>1)));
				}
			}
			return geoms;
		}
		return super.getCollisionGeoms();
	}
	
	/**
	 * Gets the geom table.
	 * 
	 * @return the geom table
	 */
	protected GeomTable getGeomTable() {
		return geom_table;
	}
		
	/**
	 * Sets the visibility of a specified layer.
	 * 
	 * @param v the v
	 * @param layer_index the layer_index
	 */
	public void setVisibility(boolean v, int layer_index) {
		((Sequence)layer.get(layer_index)).setVisibility(v);
	}

	/**
	 * Checks if a layer is visible.
	 * 
	 * @param layer_index the layer_index
	 * 
	 * @return true, if is visible
	 */
	public boolean isVisible(int layer_index) {
		return ((Sequence)layer.get(layer_index)).isVisible();
	}
	
	private void animate(int dt) {
		Iterator it = layer.iterator();
		while (it.hasNext()) {
			((Sequence)it.next()).computeImageFrame(dt);
		}
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.Item#update(southatlantic.tileengine2d.InputState, int)
	 */
	public void update(InputState inputstate, int dt) {
		computeAnimation(inputstate, dt);
		animate(dt);
		super.update(inputstate, dt);
	}
	
	/**
	 * Compute animation. To be implemented by subclasses.
	 * 
	 * @param keystate the keystate
	 * @param dt the dt
	 */
	protected void computeAnimation(InputState inputstate, int dt){}
	
	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.Item#paint(java.awt.Graphics, int, int, int)
	 */
	public void paint(Graphics g, int winx, int winy, int tilesize) {
		if (unvisible) return;
		int pixX = TileMath.Mt2Px(posX, tilesize) - winx;
		int pixY = TileMath.Mt2Px(posY, tilesize) - winy;
		Dimension image_size_pix = new Dimension(TileMath.Mt2Px(image_dim.width, tilesize), TileMath.Mt2Px(image_dim.height, tilesize));
		pixX -= (image_size_pix.width >> 1); // paint at center
		pixY -= (image_size_pix.height >> 1);
		Iterator it = layer.iterator();
		while (it.hasNext()) {
			Sequence seq = (Sequence)it.next();
			if (seq.isVisible()) {
				int sx = (seq.getCurrentFrame()%num_image_frames_x)*image_frame_width;
				int sy = (seq.getCurrentFrame()/num_image_frames_x)*image_frame_height;
				g.drawImage(image, pixX, pixY, pixX+image_size_pix.width, pixY+image_size_pix.height,
						sx, sy, sx+image_frame_width, sy+image_frame_height, null); 
			}
		}
	}	
	
	/**
	 * Gets the sequence table.
	 * 
	 * @param id the id
	 * 
	 * @return the sequence table
	 */
	protected SequenceTable getSequenceTable(int id) {
		Iterator it = sequence_tables.iterator();
		while (it.hasNext()) {
			SequenceTable st = ((SequenceTable)it.next());
			if (st.getId() == id) {
				return st;
			}
		}
		return null;
	}
	
	/**
	 * Gets the current sequence id.
	 * 
	 * @param layer_idx the layer_idx
	 * 
	 * @return the current sequence id
	 */
	protected int getCurrentSequenceId(int layer_idx) {
		return getCurrentSequence(layer_idx).getSequenceId();
	}

	/**
	 * Gets the current sequence.
	 * 
	 * @param layer_index the layer_index
	 * 
	 * @return the sequence
	 */
	public Sequence getCurrentSequence(int layer_index) {
		return (Sequence)layer.get(layer_index);
	}
	
	/**
	 * Sets the sequence.
	 * 
	 * @param layer_index the layer_index
	 * @param sequence_id the sequence_id
	 * @param offset the offset
	 */
	public void setSequence(int layer_index, int sequence_id, int offset) {
		Sequence sequence = new Sequence(getSequenceTable(sequence_id), true, offset); 
		if (layer.size() > layer_index) {
			layer.set(layer_index, sequence);
		} else {
			layer.add(sequence);
		}
	}

	/**
	 * Sequence finished.
	 * 
	 * @param layer_index the layer_index
	 * 
	 * @return true, if sequence is finished
	 */
	protected boolean sequenceFinished(int layer_index) {
		return ((Sequence)layer.get(layer_index)).isFinished();
	}
	
}
