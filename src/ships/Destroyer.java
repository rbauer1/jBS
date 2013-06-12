package ships;

import base.Actions;
import base.Player;

public class Destroyer extends Ships {
	public Destroyer (int x, int y, int orientation){
		hp = 3;
		position[0] = x;
		position[1] = y;
		position[2] = orientation;
		position[3] = 3;
		numMissiles=2;
		type = Ships.ShipType.DESTROYER;
	}
	public boolean fireMissile(Player other, int x, int y, int config){
		if(config<1 || config>2 || numMissiles==0){
			return false;
		}else{
			if (config == 1) {
				if(y<2 || y>9){
					return false;
				}
				Actions.attack(other, x, y);
				Actions.attack(other, x, y + 1);
				Actions.attack(other, x, y - 1);
			}else{			
				if(x<2 || x>13){
					return false;
				}
			Actions.attack(other, x, y);
			Actions.attack(other, x + 1, y);
			Actions.attack(other, x - 1, y);
			}
			numMissiles--;
			return true;
		}
	}
}

