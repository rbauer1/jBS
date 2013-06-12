package ships;

import base.Player;

public class PatrolBoat extends Ships {
	public PatrolBoat(int x, int y, int orientation){
		hp =2;
		position[0] = x;
		position[1] = y;
		position[2] = orientation;
		position[3] = 2;
		numMissiles=0;
		type = Ships.ShipType.PATROLBOAT;
	}
	public boolean fireMissile(Player other, int x, int y, int config){
		return false;
	}
}
