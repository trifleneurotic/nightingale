package com.southatlantic.examples.buccaneerbattle;

import com.southatlantic.tileengine2d.Item;

interface Cannon {
	public void shoot(int direction, int posX, int posY, Item source_item);
}
