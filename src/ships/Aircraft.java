package ships;

import base.Board;
import base.Player;
import base.Board.TileStatus;

public class Aircraft extends Ships {
	private boolean launched;

	public Aircraft(int x, int y, int orientation, int num) {
		launched = false;
		hp = 1;
		position[0] = x;
		position[1] = y;
		position[2] = orientation;
		position[3] = 1;
		numMissiles = 0;
		if (num == 1) {
			type = Ships.ShipType.AIRCRAFT1;
		} else {
			type = Ships.ShipType.AIRCRAFT2;
		}
	}

	public int[][] fireMissile(Player other, int x, int y, int config) {
		System.out.println("Aircraft have no missiles");
		int[][] returnArr= {{-1}};
		return returnArr;
	}

	/**
	 * moves the selected aircraft to the selected space
	 * 
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		position[0] = y;
		position[1] = x;
		if(!launched){
			setLaunched();
		}
	}
	
	private void setLaunched(){
		launched = true;
	}
	
	/**
	 * returns true if Aircraft has launched
	 * returns false if aircraft is still on aircraft carrier
	 * @return
	 */
	public boolean launched() {
		return launched;
	}

	public int scan(Player other, int config) {
		int y = position[0];
		int x = position[1];
		if (config != 1 && config != 2 || !launched) {
			return -1;
		}
		int shipFound = 0;
		Board board = other.getPlayerBoard();
		TileStatus[][] ts = board.returnBoard();
		if (config == 1) {
			for (int i = -1; i < 2; i = i + 2) {
				for (int j = -1; j < 2; j = j + 2) {
					if (ts[y + i][x + j] == TileStatus.SUBSCANSHIP
							|| ts[y + i][x + j] == TileStatus.AIRCRAFTSCAN
							|| ts[y + i][x + j] == TileStatus.SHIP) {
						shipFound = 1;
						board.updateBoard(x + j, y + i, 0,
								TileStatus.AIRCRAFTSCAN);
					} else if (!(ts[y + i][x + j]==TileStatus.HIT)) {
						board.updateBoard(x + j, y + i, 0, TileStatus.AIRCRAFTSCANEMPTY);
					}
				}
			}
		} else {
			for (int i = -1; i < 2; i = i + 2) {
				if (ts[y + i][x] == TileStatus.SUBSCANSHIP
						|| ts[y + i][x] == TileStatus.AIRCRAFTSCAN
						|| ts[y + i][x] == TileStatus.SHIP) {
					shipFound = 1;
					board.updateBoard(x, y + i, 0, TileStatus.AIRCRAFTSCAN);
				} else if (ts[y + i][x] != TileStatus.HIT) {
					board.updateBoard(x, y + i, 0, TileStatus.AIRCRAFTSCANEMPTY);
				}
			}
			for (int j = -1; j < 2; j = j + 2) {
				if (ts[y][x + j] == TileStatus.SUBSCANSHIP
						|| ts[y][x + j] == TileStatus.AIRCRAFTSCAN
						|| ts[y][x + j] == TileStatus.SHIP) {
					shipFound = 1;
					board.updateBoard(x + j, y, 0, TileStatus.AIRCRAFTSCAN);
				} else if (ts[y][x + j] != TileStatus.HIT) {
					board.updateBoard(x + j, y, 0, TileStatus.AIRCRAFTSCANEMPTY);
				}
			}
		}
		return shipFound;
	}
}
