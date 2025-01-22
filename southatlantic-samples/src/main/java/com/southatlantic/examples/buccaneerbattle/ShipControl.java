package com.southatlantic.examples.buccaneerbattle;

import com.southatlantic.core.InputState;

public interface ShipControl {
	public void performCommands(PirateShip ship, InputState inputstate);
}
