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
	public int[][] fireMissile(Player other, int x, int y, int config){
		int[][] returnArr = {{-1, 0, 0, 0}, {0,0,0,0}};
		if(config<1 || config>2 || numMissiles==0){
			return returnArr;
		}else{
			if (config == 1) {
				if(y<2 || y>9){
					return returnArr;
				}
				returnArr[0][0]=0;
				if(Actions.attack(other, x, y)==1){
					returnArr[0][0]=1;
					returnArr[0][1]=x;
					returnArr[1][1]=y;
				}
				if(Actions.attack(other, x, y + 1)==1){
					returnArr[0][0]=1;
					returnArr[0][2]=x;
					returnArr[1][2]=y+1;
				}
				if(Actions.attack(other, x, y - 1)==1){
					returnArr[0][0]=1;
					returnArr[0][3]=x;
					returnArr[1][3]=y-1;
				}
			}else{			
				if(x<2 || x>13){
					return returnArr;
				}
				returnArr[0][0]=0;
				if(Actions.attack(other, x, y)==1){
					returnArr[0][0]=1;
					returnArr[0][1]=x;
					returnArr[1][1]=y;
				}
				if(Actions.attack(other, x + 1, y)==1){
					returnArr[0][0]=1;
					returnArr[0][2]=x+1;
					returnArr[1][2]=y;
				}
				if(Actions.attack(other, x-1, y)==1){
					returnArr[0][0]=1;
					returnArr[0][3]=x - 1;
					returnArr[1][3]=y;
				}
			}
			numMissiles--;
			return returnArr;
		}
	}
}

