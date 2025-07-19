
package dev.trifleneurotic.nightingale.tileengine2d.collision;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * The Class CollisionGeom. A geometrical figure that can intersect
 * another CollisionGeom.
 */
public class CollisionGeom {
	private final static byte REC = 0;
	private final static byte LINE = 1;
	
	private byte mode = -1;
	
	private Rectangle rec = null;
	private Line2D line = null;
	
	/**
	 * Instantiates a new collision geom.
	 * 
	 * @param _rec the _rec
	 */
	public CollisionGeom(Rectangle _rec) {
		rec = _rec;
		mode = REC;
	}

	/**
	 * Instantiates a new collision geom.
	 * 
	 * @param _line the _line
	 */
	public CollisionGeom(Line2D _line) {
		line = _line;
		mode = LINE;
	}
	
	/**
	 * Intersects.
	 * 
	 * @param cg the cg
	 * 
	 * @return true, if successful
	 */
	public boolean intersects(CollisionGeom cg) {
		if (mode == REC) {
			if (cg.getMode() == REC) {
				return rec.intersects(cg.getRectangle());
			} else if (cg.getMode() == LINE) {
				return rec.intersectsLine(cg.getLine());
			}
		} else if (line != null) {
			if (cg.getMode() == REC) {
				return line.intersects(cg.getRectangle());
			} else if (cg.getMode() == LINE) {
				return line.intersectsLine(cg.getLine());
			}
		}
		return false;
	}

	/**
	 * Position of an intersection. Works only with CollisionGeoms
	 * that are both representing Rectangles.
	 * 
	 * @param cg the cg
	 * 
	 * @return the position of an intersection
	 */
	public Dimension intersection(CollisionGeom cg) {
		if (mode == REC) {
			if (cg.getMode() == REC) {
				Rectangle i = rec.intersection(cg.rec);
				return new Dimension((int)i.getCenterX(), (int)i.getCenterY());
			} 
		} 
		return new Dimension(0, 0);
	}
	
	private byte getMode() {
		return mode;
	}

	private Rectangle getRectangle() {
		return rec;
	}

	private Line2D getLine() {
		return line;
	}
	
	/**
	 * Gets the touched grid elements.
	 * 
	 * @param grid_size_x the grid_size_x in mt
	 * @param grid_size_y the grid_size_y in mt
	 * 
	 * @return the touched grid elements
	 */
	public ArrayList getTouchedGridElements(int grid_size_x, int grid_size_y) {
		ArrayList list = new ArrayList();
		if (mode == REC) {
			Dimension p1 = new Dimension (rec.x/grid_size_x, rec.y/grid_size_y);
			Dimension p2 = new Dimension ((rec.x+rec.width)/grid_size_x, (rec.y+rec.height)/grid_size_y);
			for (int i = p1.width; i <= p2.width; i++) {
				for (int j = p1.height; j <= p2.height; j++) {
					int[] tile = {i, j};
					list.add(tile);
				}				
			}
		} else if (mode == LINE) {
			Dimension p1 = new Dimension (((int)line.getX1())/grid_size_x, ((int)line.getY1())/grid_size_y);
			Dimension p2 = new Dimension (((int)line.getX2())/grid_size_x, ((int)line.getY2())/grid_size_y);
			for (int i = Math.min(p1.width, p2.width); i <= Math.max(p1.width, p2.width); i++) {
				for (int j = Math.min(p1.height, p2.height); j <= Math.max(p1.height, p2.height); j++) {
					int[] tile = {i, j};
					list.add(tile);
				}				
			}
		}
		return list;
	}

	/**
	 * Gets the absolute geom.
	 * 
	 * @param geom the geom
	 * @param x the x
	 * @param y the y
	 * 
	 * @return the absolute geom
	 */
	public static CollisionGeom getAbsoluteGeom(CollisionGeom geom, int x, int y) {
		if (geom.getMode() == REC) {
			Rectangle rec = geom.getRectangle();
			return new CollisionGeom(new Rectangle(rec.x + x, rec.y + y, rec.width, rec.height));
		} else if (geom.getMode() == LINE) {
			Line2D line = geom.getLine();
			return new CollisionGeom(new Line2D.Float((float)line.getX1()+x, (float)line.getY1()+y, 
					(float)line.getX2()+x, (float)line.getY2()+y));
		}
		return null;
	}

	/**
	 * Gets the relative geom.
	 * 
	 * @param geom the geom
	 * @param x the x
	 * @param y the y
	 * 
	 * @return the relative geom
	 */
	public static CollisionGeom getRelativeGeom(CollisionGeom geom, int x, int y) {
		if (geom.getMode() == REC) {
			Rectangle rec = geom.getRectangle();
			return new CollisionGeom(new Rectangle(rec.x - x, rec.y - y, rec.width, rec.height));
		} else if (geom.getMode() == LINE) {
			Line2D line = geom.getLine();
			return new CollisionGeom(new Line2D.Float((float)line.getX1()-x, (float)line.getY1()-y, 
					(float)line.getX2()-x, (float)line.getY2()-y));
		}
		return null;
	}

}
