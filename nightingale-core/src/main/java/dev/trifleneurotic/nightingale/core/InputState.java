
package dev.trifleneurotic.nightingale.core;

import java.awt.event.*;
import java.util.Hashtable;

/**
 * The Class InputState.
 */
public class InputState implements KeyListener, MouseListener, MouseMotionListener {

	private int keycode;
	private int once_pressed;
	private int once_released;
	private int once_typed;
	
	private int mouse_x = 0, mouse_y = 0;
	private boolean mouse_down_left = false;
	private boolean mouse_once_clicked_left = false;
	private boolean mouse_once_pressed_left = false;
	private boolean mouse_once_released_left = false;
	private boolean mouse_down_right = false;
	private boolean mouse_once_clicked_right = false;
	private boolean mouse_once_pressed_right = false;
	private boolean mouse_once_released_right = false;
	private boolean mouse_once_doubleclicked = false;
	private boolean inside = false;
	
	private int offset_x = 0, offset_y = 0;

	/** The Constant KEY_UP. */
	public static final int KEY_UP = 0X0001;
	
	/** The Constant KEY_DOWN. */
	public static final int KEY_DOWN = 0X0002;
	
	/** The Constant KEY_LEFT. */
	public static final int KEY_LEFT = 0X0004;
	
	/** The Constant KEY_RIGHT. */
	public static final int KEY_RIGHT = 0X0008;
	
	/** The Constant KEY_SPACE. */
	public static final int KEY_SPACE = 0X0010;
	
	/** The Constant KEY_W. */
	public static final int KEY_W = 0X0020;
	
	/** The Constant KEY_S. */
	public static final int KEY_S = 0X0040;
	
	/** The Constant KEY_A. */
	public static final int KEY_A = 0X0080;
	
	/** The Constant KEY_D. */
	public static final int KEY_D = 0X0100;
	
	/** The Constant KEY_I. */
	public static final int KEY_I = 0X0200;
	
	/** The Constant KEY_K. */
	public static final int KEY_K = 0X0400;
	
	/** The Constant KEY_J. */
	public static final int KEY_J = 0X0800;
	
	/** The Constant KEY_L. */
	public static final int KEY_L = 0X1000;
	
	/** The Constant KEY_P. */
	public static final int KEY_P = 0X2000;
	
	/** The Constant KEY_ENTER. */
	public static final int KEY_ENTER = 0X4000;
	
	/** The Constant KEY_PLUS. */
	public static final int KEY_PLUS = 0X8000;
	
	/** The Constant KEY_MINUS. */
	public static final int KEY_MINUS = 0X10000;

	/** The KE y_ e. */
	public static int KEY_E = 0X20000;
	
	/** The KE y_ q. */
	public static int KEY_Q = 0X40000;
	
	/** The Constant KEY_4. */
	public static final int KEY_4 = 0X80000;
	
	/** The Constant KEY_3. */
	public static final int KEY_3 = 0X100000;
	
	/** The Constant KEY_1. */
	public static final int KEY_1 = 0X200000;
	
	/** The Constant KEY_2. */
	public static final int KEY_2 = 0X400000;
	
	/** The Constant KEY_6. */
	public static final int KEY_6 = 0X800000;
	
	/** The Constant KEY_0. */
	public static final int KEY_0 = 0X1000000;
	
	/** The Constant KEY_9. */
	public static final int KEY_9 = 0X2000000;
	
	/** The Constant KEY_SHIFT. */
	public static final int KEY_SHIFT = 0X4000000;


	/** The Constant BUTTON_LEFT. */
	public static final byte BUTTON_LEFT = 1;
	
	/** The Constant BUTTON_RIGHT. */
	public static final byte BUTTON_RIGHT = 2;

	
	private Hashtable keytable = null;
	
	/**
	 * Instantiates a new inputstate.
	 */
	public InputState() {
		keycode = 0;
		once_pressed = 0;
		once_released = 0;
		once_typed = 0;
		initKeyTable();
	}
	
	/**
	 * Reset the inputstate. Called every cyclus.
	 */
	public void reset() {
		once_pressed = 0;
		once_released = 0;
		once_typed = 0;
		mouse_once_clicked_left = false;
		mouse_once_pressed_left = false;
		mouse_once_released_left = false;
		mouse_once_clicked_right = false;
		mouse_once_pressed_right = false;
		mouse_once_released_right = false;
		mouse_once_doubleclicked = false;
	}
	
	private void initKeyTable() {
		keytable = new Hashtable();
		keytable.put(new Integer(KeyEvent.VK_UP), new Integer(KEY_UP));
		keytable.put(new Integer(KeyEvent.VK_DOWN), new Integer(KEY_DOWN));
		keytable.put(new Integer(KeyEvent.VK_LEFT), new Integer(KEY_LEFT));
		keytable.put(new Integer(KeyEvent.VK_RIGHT), new Integer(KEY_RIGHT));
		keytable.put(new Integer(KeyEvent.VK_SPACE), new Integer(KEY_SPACE));
		keytable.put(new Integer(KeyEvent.VK_ENTER), new Integer(KEY_ENTER));
		keytable.put(new Integer(KeyEvent.VK_PLUS), new Integer(KEY_PLUS));
		keytable.put(new Integer(KeyEvent.VK_MINUS), new Integer(KEY_MINUS));
		keytable.put(new Integer(KeyEvent.VK_SHIFT), new Integer(KEY_SHIFT));
		keytable.put(new Integer(KeyEvent.VK_W), new Integer(KEY_W));
		keytable.put(new Integer(KeyEvent.VK_S), new Integer(KEY_S));
		keytable.put(new Integer(KeyEvent.VK_A), new Integer(KEY_A));
		keytable.put(new Integer(KeyEvent.VK_D), new Integer(KEY_D));
		keytable.put(new Integer(KeyEvent.VK_I), new Integer(KEY_I));
		keytable.put(new Integer(KeyEvent.VK_K), new Integer(KEY_K));
		keytable.put(new Integer(KeyEvent.VK_J), new Integer(KEY_J));
		keytable.put(new Integer(KeyEvent.VK_L), new Integer(KEY_L));
		keytable.put(new Integer(KeyEvent.VK_E), new Integer(KEY_E));
		keytable.put(new Integer(KeyEvent.VK_Q), new Integer(KEY_Q));
		keytable.put(new Integer(KeyEvent.VK_P), new Integer(KEY_P));
		keytable.put(new Integer(KeyEvent.VK_0), new Integer(KEY_0));
		keytable.put(new Integer(KeyEvent.VK_1), new Integer(KEY_1));
		keytable.put(new Integer(KeyEvent.VK_2), new Integer(KEY_2));
		keytable.put(new Integer(KeyEvent.VK_3), new Integer(KEY_3));
		keytable.put(new Integer(KeyEvent.VK_4), new Integer(KEY_4));
		keytable.put(new Integer(KeyEvent.VK_6), new Integer(KEY_6));
		keytable.put(new Integer(KeyEvent.VK_9), new Integer(KEY_9));
	}
	
	/**
	 * Checks if is pressed.
	 * 
	 * @param key the key
	 * 
	 * @return true, if is pressed
	 */
	public boolean isPressed(int key) {
		return (keycode & key) != 0;
	}
	
	/**
	 * Checks if is once pressed.
	 * 
	 * @param key the key
	 * 
	 * @return true, if is once pressed
	 */
	public boolean isOncePressed(int key) {
		return (once_pressed & key) != 0;
	}
	
	/**
	 * Checks if is once released.
	 * 
	 * @param key the key
	 * 
	 * @return true, if is once released
	 */
	public boolean isOnceReleased(int key) {
		return (once_released & key) != 0;
	}

	/**
	 * Checks if is once typed.
	 * 
	 * @param key the key
	 * 
	 * @return true, if is once typed
	 */
	public boolean isOnceTyped(int key) {
		return (once_typed & key) != 0;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		Integer object = ((Integer)keytable.get(new Integer(e.getKeyCode())));
		if (object != null) {
			keycode = (int)(keycode | object.intValue());
			once_pressed = (int)(once_pressed | object.intValue());
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		Integer object = ((Integer)keytable.get(new Integer(e.getKeyCode())));
		if (object != null) {
			keycode = (int)(keycode & (object.intValue() ^ 0Xffffffff));
			once_released = (int)(once_released | object.intValue());
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		Integer object = ((Integer)keytable.get(new Integer(e.getKeyCode())));
		if (object != null) {
			once_typed = (int)(once_typed | object.intValue());
		}
	}


	/**
	 * Checks if is mouse pressed.
	 * 
	 * @param button the button
	 * 
	 * @return true, if is mouse pressed
	 */
	public boolean isMousePressed(byte button) {
		return button==BUTTON_RIGHT?mouse_down_right:mouse_down_left;
	}
	
	/**
	 * Checks if is mouse once clicked.
	 * 
	 * @param button the button
	 * 
	 * @return true, if is mouse once clicked
	 */
	public boolean isMouseOnceClicked(byte button) {
		return button==BUTTON_RIGHT?mouse_once_clicked_right:mouse_once_clicked_left;
	}

	/**
	 * Checks if is mouse once pressed.
	 * 
	 * @param button the button
	 * 
	 * @return true, if is mouse once pressed
	 */
	public boolean isMouseOncePressed(byte button) {
		return button==BUTTON_RIGHT?mouse_once_pressed_right:mouse_once_pressed_left;
	}
	
	/**
	 * Checks if is mouse once released.
	 * 
	 * @param button the button
	 * 
	 * @return true, if is mouse once released
	 */
	public boolean isMouseOnceReleased(byte button) {
		return button==BUTTON_RIGHT?mouse_once_released_right:mouse_once_released_left;
	}

	/**
	 * Checks if is mouse once double clicked.
	 * 
	 * @return true, if is mouse once double clicked
	 */
	public boolean isMouseOnceDoubleClicked() {
		return mouse_once_doubleclicked;
	}

	/**
	 * Gets the mouse position x.
	 * 
	 * @return the mouse position x
	 */
	public int getMousePositionX() {
		return mouse_x - offset_x;
	}
	
	/**
	 * Gets the mouse position y.
	 * 
	 * @return the mouse position y
	 */
	public int getMousePositionY() {
		return mouse_y - offset_y;
	}

	/**
	 * Checks if is mouse in.
	 * 
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * 
	 * @return true, if is mouse in
	 */
	public boolean isMouseIn(int x, int y, int width, int height) {
		if (inside) {
			x = x + offset_x;
			y = y + offset_y;
			return getMousePositionX() >= x && getMousePositionX() <= x+width
				&& getMousePositionY() >= y && getMousePositionY() <= y+height;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			mouse_once_clicked_left = true;
			if (me.getClickCount() == 2) {
				mouse_once_doubleclicked = true;
			}
		}
		if (me.getButton() == MouseEvent.BUTTON3)
			mouse_once_clicked_right = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
		inside = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
		inside = false;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			mouse_down_left = true;
			mouse_once_pressed_left = true;
		}
		if (me.getButton() == MouseEvent.BUTTON3) {
			mouse_down_right = true;
			mouse_once_pressed_right = true;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			mouse_down_left = false;
			mouse_once_released_left = true;
		}
		if (me.getButton() == MouseEvent.BUTTON3) {
			mouse_down_right = false;
			mouse_once_released_right = true;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent me) {
		mouseMoved(me);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent me) {
		mouse_x = me.getX();
		mouse_y = me.getY();
	}

	/**
	 * Adds an offset (for mouse-positions).
	 * 
	 * @param x the x
	 * @param y the y
	 */
	public void addOffset(int x, int y) {
		offset_x = x;
		offset_y = y;
	}
}
