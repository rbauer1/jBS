package ships;

import base.Actions;
import base.Player;

public class AircraftCarrier extends Ships {
	/**
	 * Constructor for AircraftCarrier
	 * 
	 * @param x
	 *            : left-most x coordinate of ship
	 * @param y
	 *            : top-most (boardwise) y coordinate of ship (note: y values
	 *            increase as you move DOWN the board)
	 * @param orientation
	 *            of ship. 0 for horizontal, 1 for vertical
	 */
	public AircraftCarrier(int x, int y, int orientation) {
		hp = 5;
		position[0] = x;
		position[1] = y;
		position[2] = orientation;
		position[3] = 5;
		numMissiles = 2;
		type = Ships.ShipType.AIRCRAFTCARRIER;
	}

	public int[][] fireMissile(Player other, int x, int y, int config){
		int[][] returnArr = {{-1, 0, 0, 0, 0, 0},{0,0,0,0,0,0}};
		if(x<2 || x>13 || y<2 || y>9 || config<1 || config>2 || numMissiles==0){
			return returnArr;
		}else{
			returnArr[0][0]=0;
			if(Actions.attack(other, x, y)==1){
				returnArr[0][0]=1;
				returnArr[0][1]=x;
				returnArr[1][1]=y;
			}
			if (config == 1) {
				if(Actions.attack(other, x + 1, y + 1)==1){
					returnArr[0][0]=1;
					returnArr[0][2]=x+1;
					returnArr[1][2]=y+1;
				}
				if(Actions.attack(other, x + 1, y - 1)==1){
					returnArr[0][0]=1;
					returnArr[0][3]=x+1;
					returnArr[1][3]=y-1;
				}
				if(Actions.attack(other, x - 1, y + 1)==1){
					returnArr[0][0]=1;
					returnArr[0][4]=x-1;
					returnArr[1][4]=y+1;
				}
				if(Actions.attack(other, x - 1, y - 1)==1){
					returnArr[0][0]=1;
					returnArr[0][5]=x+1;
					returnArr[1][5]=y+1;;
				}
			}else{			
			if(Actions.attack(other, x + 1, y)==1){
				returnArr[0][0]=1;
				returnArr[0][2]=x+1;
				returnArr[1][2]=y;
			}
			if(Actions.attack(other, x, y - 1)==1){
				returnArr[0][0]=1;
				returnArr[0][3]=x;
				returnArr[1][3]=y-1;
			}
			if(Actions.attack(other, x - 1, y)==1){
				returnArr[0][0]=1;
				returnArr[0][4]=x-1;
				returnArr[1][4]=y;
			}
			if(Actions.attack(other, x, y + 1)==1){
				returnArr[0][0]=1;
				returnArr[0][5]=x;
				returnArr[1][5]=y+1;;
			}
			}
			numMissiles--;
			return returnArr;
		}
	}
}
