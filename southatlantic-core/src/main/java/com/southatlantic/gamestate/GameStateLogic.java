
package com.southatlantic.gamestate;


/**
 * The Interface GameStateLogic.
 */
public interface GameStateLogic {
	
	/**
	 * Gets the start-{@link GameState}.
	 * 
	 * @return the game state
	 */
	public GameState getStartGameState();
	
	/**
	 * Next {@link GameState}.
	 * 
	 * @param exit_code the exit_code
	 * @param old_state the old_state
	 * 
	 * @return the new GameState
	 */
	public GameState nextGameState(int exit_code, GameState old_state);
}
