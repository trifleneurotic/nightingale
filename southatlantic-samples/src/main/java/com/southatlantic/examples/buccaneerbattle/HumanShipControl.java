package com.southatlantic.examples.buccaneerbattle;

import com.southatlantic.core.InputState;

class HumanShipControl implements ShipControl {
	private int key_left;
	private int key_right;
	private int key_sails;
	private int key_shot_left;
	private int key_shot_right;
	
	public HumanShipControl(int _key_left, int _key_right, int _key_sails, int _key_shot_left, int _key_shot_right) {
		key_left = _key_left;
		key_right = _key_right;
		key_sails = _key_sails;
		key_shot_left = _key_shot_left;
		key_shot_right = _key_shot_right;
	}

	public void performCommands(PirateShip ship, InputState inputstate) {
		if (inputstate.isOncePressed(key_sails)) {
			if (ship.onSails()) {
				ship.anchor();
			}
			else {
				ship.setSails();
			}
		}
		if (inputstate.isOncePressed(key_shot_left)) {
			ship.fireLeft();
		}
		if (inputstate.isOncePressed(key_shot_right)) {
			ship.fireRight();
		}
		if (inputstate.isPressed(key_left)) {  
			ship.portside();
		}
		if (inputstate.isPressed(key_right)) { 
			ship.starbord();
		}
	}
}
