
package dev.trifleneurotic.nightingale.menu;

import dev.trifleneurotic.nightingale.resources.ResourceManager;

/**
 * The Class MenuState.
 */
public class MenuState extends DialogState implements MenuListener {

	/**
	 * Instantiates a new menu state.
	 * 
	 * @param _res_mng the {@link ResourceManager}
	 * @param _menu_layer the _menu_layer
	 */
	public MenuState(ResourceManager _res_mng, MenuLayer _menu_layer) {
		super(_res_mng, _menu_layer);
		_menu_layer.addMenuListener(this);
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.menu.DialogState#resume()
	 */
	protected void resume() { }
	
	/* (non-Javadoc)
	 * @see southatlantic.menu.DialogState#init()
	 */
	protected void init() {	}

	/* (non-Javadoc)
	 * @see southatlantic.menu.MenuListener#pointSelected(southatlantic.menu.MenuEvent)
	 */
	public void pointSelected(MenuEvent me) { }
	
	/**
	 * Gets the menu layer.
	 * 
	 * @return the menu layer
	 */
	public MenuLayer getMenuLayer() {
		return (MenuLayer)dialog_layer;
	}
}
