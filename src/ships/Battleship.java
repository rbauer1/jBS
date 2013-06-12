package ships;

import base.Actions;
import base.Player;

public class Battleship extends Ships {
	public Battleship(int x, int y, int orientation){
		hp = 4;
		position[0] = x;
		position[1] = y;
		position[2] = orientation;
		position[3] = 4;
		numMissiles=1;
		type = Ships.ShipType.BATTLESHIP;
	}
	
	
	
	public boolean fireMissile(Player other, int x, int y, int config){
		if(x<2 || x>13 || y<2 || y>9 || config<1 || config>2 || numMissiles==0){
			return false;
		}else{
			Actions.attack(other, x, y);
			Actions.attack(other, x + 1, y + 1);
			Actions.attack(other, x + 1, y - 1);
			Actions.attack(other, x - 1, y + 1);
			Actions.attack(other, x - 1, y - 1);
			Actions.attack(other, x + 1, y);
			Actions.attack(other, x, y - 1);
			Actions.attack(other, x - 1, y);
			Actions.attack(other, x, y + 1);
			numMissiles--;
			return true;
		}
	}
}
