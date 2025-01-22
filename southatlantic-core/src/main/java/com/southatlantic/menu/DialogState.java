
package com.southatlantic.menu;

import com.southatlantic.core.InputState;
import com.southatlantic.gamestate.GameState;
import com.southatlantic.resources.ResourceManager;

import java.awt.*;

/**
 * The Class DialogState. A GameState that manages a {@link DialogLayer}.
 */
public class DialogState extends GameState {
	
	/** The dialog_layer. */
	protected DialogLayer dialog_layer = null;
	
	/**
	 * Instantiates a new DialogState.
	 * 
	 * @param _res_mng the _res_mng
	 * @param _dialog_layer the _dialog_layer
	 */
	public DialogState(ResourceManager _res_mng, DialogLayer _dialog_layer) {
		super(_res_mng);
		dialog_layer = _dialog_layer;
	}

	/* (non-Javadoc)
	 * @see southatlantic.gamestate.GameState#paintScene(java.awt.Graphics)
	 */
	protected void paintScene(Graphics g) {
		dialog_layer.paint(g, 0, 0, 1);
	}
	
	/* (non-Javadoc)
	 * @see southatlantic.gamestate.GameState#updateState(southatlantic.tileengine2d.InputState, int)
	 */
	protected void updateState(InputState inputstate, int dt) {
		dialog_layer.update(inputstate, dt);
	}	

	/* (non-Javadoc)
	 * @see southatlantic.gamestate.GameState#resume()
	 */
	protected void resume() { }
	
	/* (non-Javadoc)
	 * @see southatlantic.gamestate.GameState#init()
	 */
	protected void init() {	}
}
