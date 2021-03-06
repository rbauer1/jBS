package ships;

import base.Player;

public abstract class Ships {
	ShipType type;
	int hp;
	// for position: {top y, left x, orientation (0 = horizontal, 1 = vertical),
	// ship length}
	int[] position = new int[4];
	int numMissiles;
	boolean isSunk = false;

	/**
	 * called by <b>Board</b>, <i>updateShipStatus()</i>, while <b>Board</b> is
	 * updating. this is where the actual damage is done to the ship.
	 * <i>updateShipStatus()</i> is called by another broader Board method,
	 * <i>updateBoard()</i>, which is called by each <b>Player</b> at the end of
	 * each <i>progressTurn</i> in the <b>Driver</b> class. </br> This method
	 * reduces the <b>hp</b> of selected ship, if hp==0, and changes <b>isSunk</b> to <b>true</b>
	 * 
	 * @return ShipType.NULL unless the ship is sunk in which case it's ShipType
	 *         is returned
	 */
	public ShipType hitUpdate() {
		if (!isSunk) {
			hp--;
			if (hp == 0) {
				isSunk = true;
				return type;
			}
		}
		return ShipType.NULL;
	}

	/**
	 * USE THIS METHOD TO DETERMINE IF A SHIP IS ACCESSIBLE FOR MISSILES
	 * 
	 * @return true if alive and has missiles, false if sunk or out of missiles
	 */
	public boolean canFireMissile() {
		return (checkMissile() && !isSunk);
	}

	/**
	 * is this ship sunk?
	 * 
	 * @return true if yes, false if no
	 */
	public boolean isThisShipSunk() {
		return isSunk;
	}

	/**
	 * @param other
	 *            opponent Player
	 * @param x
	 *            coord (center) except for sub, which then specifies top-left
	 *            most
	 * @param y
	 *            coord (center) "
	 * @param config
	 *            missile firing pattern
	 * @return true if missile fires, false if invalid for some reason
	 */
	abstract public int[][] fireMissile(Player other, int x, int y, int config);

	// I don't think this is used
	public void missileUpdate(int i) {
		numMissiles--;
	}

	/**
	 * returns true if ship still has missiles
	 * 
	 * @return numMissiles!=0
	 */
	public boolean checkMissile() {
		return !(numMissiles == 0);
	}

	// not sure if this is used either
	public int[] getPosition() {
		return position;
	}

	/**
	 * returns the hitpoints of given ship
	 * 
	 * @return int hp
	 */
	public int getHP() {
		return hp;
	}

	// probably not used at this point, just for checks
	public String toString() {
		String s = "";
		for (int i = 0; i < position.length; i++) {
			s += String.valueOf(position[i]) + " ";
		}
		return s;
	}

	/**
	 * @return the enumerator ShipType value of the selected ship
	 */
	public ShipType getType() {
		return type;
	}

	public enum ShipType {
		AIRCRAFTCARRIER, BATTLESHIP, DESTROYER, SUBMARINE, PATROLBOAT, AIRCRAFT1, AIRCRAFT2, NULL
	}
}
