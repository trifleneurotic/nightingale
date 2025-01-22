/*
 * Created on 10.09.2007
 *
 */
package cb_painter;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

public class BboxSet {
	ArrayList[] bboxes; // array of lists of rectangles
	
	boolean pressed = false;
	int start_x;
	int start_y;
	
	public BboxSet(int size) {
		bboxes = new ArrayList[size];
	}
	
	public void mousePressed(MouseEvent me) {
		start_x = me.getX();
		start_y = me.getY();
		pressed = true;
	}

	public void mouseExited() {
		pressed = false;
	}
	
	public void mouseReleased(MouseEvent me, int index) {
		if (pressed) {
			addRect(start_x, start_y, me.getX(), me.getY(), index);
		}
	}

	private void addRect(int x1, int y1, int x2, int y2, int index) {
		if (bboxes[index] == null) {
			bboxes[index] = new ArrayList();
		}
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int w = Math.abs(x1-x2);
		int h = Math.abs(y1-y2);
		bboxes[index].add(new Rectangle(x, y, w, h));
	}
	
	public ArrayList getRectangleList(int index) {
		return bboxes[index];
	}
	
	public int size() {
		return bboxes.length;
	}
	
	public ArrayList getFrames() {
		ArrayList bbox_set = new ArrayList();
		for (int i = 0; i < bboxes.length; i++) {
			bbox_set.add(bboxes[i]);
		}
		return bbox_set;
	}

	public void clearFrame(int _index) {
		bboxes[_index] = null;
	}
}
