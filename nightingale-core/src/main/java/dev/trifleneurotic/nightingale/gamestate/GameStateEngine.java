
package dev.trifleneurotic.nightingale.gamestate;


import java.awt.*;

/**
 * The Class GameStateEngine. Manages the {@link GameState}s.
 */
public class GameStateEngine {
	private GameState state;

	private final GameStateLogic gsLogic;

	private final Component component;

	/**
	 * Instantiates a new GameStateEngine.
	 * 
	 * @param gsLogic the {@link GameStateLogic}
	 * @param component the Component that visualizes the {@link GameState}s. (For example an Applet)
	 */
	public GameStateEngine(GameStateLogic gsLogic, Component component) {
		this.gsLogic = gsLogic;
		this.component = component;
	}

	/**
	 * Sets and inits a state.
	 * 
	 * @param new_state the new state
	 */
	private void setState(GameState new_state) {
		if (state != null) {
			state.tidy();
			component.removeKeyListener(state.getInputState());
			component.removeMouseListener(state.getInputState());
			component.removeMouseMotionListener(state.getInputState());
		}
		state = new_state;
		initState();
	}

	/**
	 * Sets the start state.
	 */
	public void setStartState() {
		if (state != null) {
			state.tidy();
			component.removeKeyListener(state.getInputState());
			component.removeMouseListener(state.getInputState());
			component.removeMouseMotionListener(state.getInputState());
		}
		state = gsLogic.getStartGameState();
	}

	/**
	 * This method is called in 2 cases:
	 * - when the main thread starts/restarts
	 * - when the gamestate changes.
	 */
	public void initState() {
		component.addKeyListener(state.getInputState());
		component.addMouseListener(state.getInputState());
		component.addMouseMotionListener(state.getInputState());
		if (!state.isInited()) {
			while (state.isLoading()) {
				component.repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException ignored) {}
			}
			state.initState();
		} else  {
			state.resumeState();
		}
	}

	/**
	 * Repaints the component.
	 */
	public void repaint() {
		component.repaint();
	}

	/**
	 * Paints the current {@link GameState}.
	 * 
	 * @param offScreenGraphics the off screen graphics
	 */
	public void paint(Graphics offScreenGraphics) {
		state.paint(offScreenGraphics);
	}

	/**
	 * Updates the current {@link GameState}.
	 */
	public void animate() {
		if(state.isFinished()) {
			setState(gsLogic.nextGameState(state.getExitCode(), state));
		}
		state.update();
	}

	/**
	 * Gets the inverse framerate.
	 * 
	 * @return the inverse framerate
	 */
	public int getInvFrameRate() {
		return state.getInvFrameRate();
	}

}
