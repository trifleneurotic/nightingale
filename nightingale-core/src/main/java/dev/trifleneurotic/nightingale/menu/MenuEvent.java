
package dev.trifleneurotic.nightingale.menu;

/**
 * The Class MenuEvent.
 */
public class MenuEvent {
	private MenuLayer menu_layer;
	private int index;
	private String text;
	
	/**
	 * Instantiates a new menu event.
	 * 
	 * @param _menu_layer the _menu_layer
	 * @param _index the index of the selected {@link MenuPoint}
	 * @param _text the text of the selected {@link MenuPoint}
	 */
	public MenuEvent(MenuLayer _menu_layer, int _index, String _text) {
		menu_layer = _menu_layer;
		index = _index;
		text = _text;
	}
	
	/**
	 * Gets the menu layer.
	 * 
	 * @return the menu layer
	 */
	public MenuLayer getMenuLayer() {
		return menu_layer;
	}

	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}
