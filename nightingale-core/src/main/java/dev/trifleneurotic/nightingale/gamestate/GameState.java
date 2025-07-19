
package dev.trifleneurotic.nightingale.gamestate;

import dev.trifleneurotic.nightingale.core.InputState;
import dev.trifleneurotic.nightingale.resources.Loadable;
import dev.trifleneurotic.nightingale.resources.Loader;
import dev.trifleneurotic.nightingale.resources.ResourceManager;
import dev.trifleneurotic.nightingale.sound.AudioPlayer;

import java.awt.*;
import java.util.ArrayList;

/**
 * The Class GameState. Represents a State of an application. The abstract methods {@link #paintScene(Graphics)} and
 * {@link #updateState(InputState, int)} are called rotatory by the {@link GameStateEngine}. GameStates can load their 
 * specific resources. If the state_finished-Flag is set, the {@link GameStateEngine} activates the next GameState,
 * depending on the implemented {@link GameStateLogic}. Finished GameStates can be resumed later.
 */
public abstract class GameState {
	
	private final static int DEFAULT_INV_MAX_FRAME_RATE = 50;  // ms
	
	private boolean stateFinished;
	
	/** The exit-code. */
	protected int exitCode;
	
	/** Indicates if the state is loading. */
	protected boolean loading;

	/** Indicates if the init-methods have already been called. */
	protected boolean isInited = false;

	/** The ResourceManager. */
	private ResourceManager resourceManager;

	/** The audio-player. */
	protected AudioPlayer audioPlayer = null;

	private InputState inputState;

	private long lastTime = -1;
	
	private Loader loader = null;
	
	private int inv_frame_rate = DEFAULT_INV_MAX_FRAME_RATE;
	
	/**
	 * Instantiates a new GameState.
	 * 
	 * @param _res_mng the ResourceManager
	 */
	public GameState(ResourceManager _res_mng) {
		resourceManager = _res_mng;
		stateFinished = false;
		inputState = new InputState();
	}
	
	/**
	 * Sets the audio-player.
	 * 
	 * @param _ap the new audio-player
	 */
	public void setAudioPlayer(AudioPlayer _ap) {
		audioPlayer = _ap;
	}
	
	/**
	 * Calls the init-method. This method is called by the {@link GameStateEngine}.
	 */
	protected void initState() {
		init();
		isInited = true;
	}
	
	/**
	 * Inits the state.
	 */
	protected abstract void init();

	/**
	 * This method is called when the state will be inited by the {@link GameStateEngine} 
	 * and {@link #isInited()} returns true.
	 */
	public void resumeState() {
		stateFinished = false;
		inputState.reset();
		lastTime = System.currentTimeMillis();
		resume();
	}
	
	/**
	 * Called by the {@link GameStateEngine} when the State resumes.
	 */
	protected abstract void resume();
	
	/**
	 * Sets a loader.
	 * 
	 * @param _loader the new loader
	 */
	public void setLoader(Loader _loader) {
		loader = _loader;
	}
	
	/**
	 * Checks if State is loading.
	 * 
	 * @return true, if is loading
	 */
	public boolean isLoading() {
		return loading;
	}
	
	/**
	 * Loads resources with the default-{@link Loader}.
	 * 
	 * @param imgs list of java.awt.Image Objects
	 * @param loadables list Objects implementing the {@link Loadable}-Interface
	 * @param screen_width the screen-width
	 * @param screen_height the screen-height
	 */
	public void load(ArrayList imgs, ArrayList loadables, int screen_width, int screen_height) {
		if (loader == null) {
			loader = new Loader(imgs, loadables, screen_width, screen_height, resourceManager);
		}
		loading = true;
	}

	/**
	 * Loads resources if a {@link Loader} was defined.
	 */
	public void load() {
		if (loader != null) {
			loading = true;
		}
	}
	
	/**
	 * Paints the State. Called by the {@link GameStateEngine}.
	 * 
	 * @param g the Graphics-Object
	 */
	public void paint(Graphics g) {
		if (loading) {
			loader.paint(g);
			loading = loader.update();
			if (!loading) {
				String error_message = loader.getErrorMessage();
				if (error_message.length() > 0) {
					System.out.println(error_message);
				}
				loader = null;
			}
		}
		else if (isInited()) {
			paintScene(g);
		}
	}
	
	/**
	 * Is called by the {@link #paint(Graphics)}-method, if 
	 * the state is not loading but inited.
	 * 
	 * @param g the Graphics-Object
	 */
	protected abstract void paintScene(Graphics g);
	
	/**
	 * Updates the state. Called by the {@link #update()}.
	 * 
	 * @param keystate the keystate
	 * @param dt the time since last update
	 */
	protected abstract void updateState(InputState inputstate, int dt);

	/**
	 * Updates the state. Called by the {@link GameStateEngine}.
	 */
	public void update() {
		if (lastTime == -1) {
			lastTime = System.currentTimeMillis();
		}
		long t = System.currentTimeMillis();
		int dt = (int)(t - lastTime);
		lastTime = t;
		updateState(inputState, dt);
		inputState.reset();
	}
	
	/**
	 * State finished.
	 * 
	 * @return true, if state is finished
	 */
	public boolean isFinished() {
		return stateFinished;
	}
		
	/**
	 * Tidy up State. Stops sounds.
	 */
	public void tidy() {
		if (audioPlayer != null) {
			audioPlayer.stopSounds();
		}
	}

	/**
	 * Gets the exit-code.
	 * 
	 * @return the exit-code
	 */
	public int getExitCode() {
		return exitCode;
	}
	
	/**
	 * Gets the current {@link KeyState}.
	 * 
	 * @return the InputState
	 */
	public InputState getInputState() {
		return inputState;
	}
	
	/**
	 * Checks if state is inited.
	 * 
	 * @return true, if is inited
	 */
	public boolean isInited() {
		return isInited;
	}
	
	/**
	 * Sets the inverse framerate in milliseconds.
	 * 
	 * @param _rate the new inverse framerate
	 */
	public void setInvFrameRate(int _rate) {
		inv_frame_rate = _rate;
	}
	
	/**
	 * Gets the inverse framerate.
	 * 
	 * @return the inv max frame rate
	 */
	public int getInvFrameRate() {
		return inv_frame_rate;
	}
	
	/**
	 * Resets timer. If this method is called the next dt-parameter of 
	 * the {@link #updateState(InputState, int)}-method will be 0 (or nearly 0).
	 */
	protected void resetTimer() {
		lastTime = -1;
	}
	
	protected ResourceManager getResourceManager() {
		return resourceManager;
	}
	
	protected void finishState() {
		stateFinished = true;
	}
}
