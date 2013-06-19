package ships;

import base.Actions;
import base.Board;
import base.Player;
import base.Board.TileStatus;

public class Submarine extends Ships { 
	public Submarine(int x, int y, int orientation){
		hp = 3;
		position[0] = x;
		position[1] = y;
		position[2] = orientation;
		position[3] = 3;
		numMissiles=2;
		type = Ships.ShipType.SUBMARINE;
	}
	public boolean fireMissile(Player other, int x, int y, int config){
		if (numMissiles == 0 || config <1 || config>2 || x<1 || x>14 || y<1 || y>10) {
			return false;
		}
		if (config == 1) {
			for (int i = 0; i < 14; i++) {
				if (Actions.attack(other, x + i, y)==1) {
					break;
				}
			}
		}else{
			for (int i = 0; i < 10; i++) {
				if (Actions.attack(other, x, y + i)==1) {
					break;
				}
			}
		}
		numMissiles--;
		return true;
	}
	/**
	 * checks a 3x3 space centered around the <b>x</b> and <b>y</b> coordinates supplied.
	 * if no ships are found, all 9 spaces are set to <b>MISS</b>es </br>
	 * if any ship is found, all 9 spaces are set to either <b>SUBSCANSHIP</b> or <b>SUBSCANEMPTY</b> </br>
	 * then all 9 spaces in the display are changed to '/'  
	 * @param other opponent Player
	 * @param x coord
	 * @param y coord
	 * @return 1 if ship is found, 0 if none, -1 if invalid scan
	 */
	public int scan(Player other, int x, int y){
		if(x<2 || x>13 || y<2 || y>9){
			return -1;
		}
		int shipFound = 0;
		Board board = other.getPlayerBoard();
		TileStatus[][] ts = board.returnBoard();
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(ts[y+i][x+j]==TileStatus.AIRCRAFTSCAN || ts[y+i][x+j]==TileStatus.SUBSCANSHIP ||ts[y+i][x+j]==TileStatus.SHIP){
					shipFound = 1;
					if(ts[y+i][x+j]!=TileStatus.AIRCRAFTSCAN && ts[y+i][x+j]!=TileStatus.HIT && ts[y+i][x+j]!=TileStatus.MISS){
						board.updateBoard(x+j, y+i, 0, TileStatus.SUBSCANSHIP);
					}
				}else{
					if(ts[y+i][x+j]!=TileStatus.AIRCRAFTSCAN && ts[y+i][x+j]!=TileStatus.HIT && ts[y+i][x+j]!=TileStatus.MISS){
						board.updateBoard(x+j, y+i, 0, TileStatus.SUBSCANEMPTY);						
					}
				}
			}
		}
		//if no ships found, board sets all spaces scanned this turn to misses
		if(shipFound==0){
			for(int i=-1; i<2; i++){
				for(int j=-1; j<2; j++){
//					if(ts[y+i][x+j]==TileStatus.SUBSCANEMPTY)
					if(ts[y+i][x+j]!=TileStatus.HIT){
						board.updateBoard(x+j, y+i, 0, TileStatus.MISS);
					}
				}
			}
		}
		return shipFound;
	}
}
