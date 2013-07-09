package base;

import ships.*;
import ships.Ships.ShipType;

public class Board {
	private Ships[] ships;// order is: ac, bs, dest, sub, pb, a1, a2
	private TileStatus[][] board = new TileStatus[11][15];
	private ShipType[][][] shipBoard = new ShipType[11][15][2];

	public Board(Ships[] ship) {
		ships=ship;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (i == 0 || j == 0) {
					board[i][j] = TileStatus.INVALID;
				} else {
					board[i][j] = TileStatus.EMPTY;
				}
				shipBoard[i][j][0] = ShipType.NULL;
				shipBoard[i][j][1] = ShipType.NULL;
			}
		}
		for (int i = 0; i < ships.length; i++) {
			int[] position = ships[i].getPosition();
			int x = position[0];
			int y = position[1];
			int xPlus = 0;
			int yPlus = 1;
			if (position[2] == 1) {
				xPlus = 1;
				yPlus = 0;
			}
			for (int j = 0; j < position[3]; j++) {
				if (ships[i].getType() != ShipType.AIRCRAFT1 && ships[i].getType() != ShipType.AIRCRAFT2) {
					board[x + xPlus * j][y + yPlus * j] = TileStatus.SHIP;
					shipBoard[x + xPlus * j][y + yPlus * j][0] = ships[i]
							.getType();
				}else{
					board[x][y] = TileStatus.SHIP;
					shipBoard[x][y][1] = ships[i].getType();
				}
			}
		}
	}

	public void updateBoard(int x, int y, int z, TileStatus newStatus) {
		board[y][x] = newStatus;
//		generateMyUIBoard(board);
		if (newStatus == TileStatus.HIT) {
			updateShipStatus(x, y, z);
		}
	}

	public void updateShipStatus(int x, int y, int z) {
		int shipHit = -1;
		switch (shipBoard[y][x][z]) {
		case AIRCRAFTCARRIER:
			shipHit = 0;
			break;
		case BATTLESHIP:
			shipHit = 1;
			break;
		case DESTROYER:
			shipHit = 2;
			break;
		case SUBMARINE:
			shipHit = 3;
			break;
		case PATROLBOAT:
			shipHit = 4;
			break;
		case AIRCRAFT1:
			shipHit = 5;
			break;
		case AIRCRAFT2:
			shipHit = 6;
			break;
		default:
			shipHit = -1;
			break;
		}
		if (shipHit != -1) {
			ShipType hitShip = ships[shipHit].hitUpdate();
			/*The method in Driver that is called here should be removed once a real GUI is created
			* I think
			*/
			if(!hitShip.equals(ShipType.NULL)){
				Driver.printShipDestroyed(hitShip);
			}
		}
	}
	
	public void updtateAirPositions(int oldX, int oldY){
		shipBoard[oldY][oldX][1] = ShipType.NULL;
		shipBoard[ships[5].getPosition()[0]][ships[5].getPosition()[1]][1] = ShipType.AIRCRAFT1;
		shipBoard[ships[6].getPosition()[0]][ships[6].getPosition()[1]][1] = ShipType.AIRCRAFT2;
	}
	
	public ShipType[][][] returnShipBoard(){
		return shipBoard;
	}

	public TileStatus[][] returnBoard() {
		return board;
	}

	public void printBoard(char[][] b) {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				System.out.print(b[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public char[][] generateMyUIBoard(TileStatus[][] t) {
		char thisTile = ' ';
		char[][] UIBoard = new char[11][15];
		for (int i = 0; i < UIBoard.length; i++) {
			for (int j = 0; j < UIBoard[0].length; j++) {
				if (i == 0 && j == 0) {
					UIBoard[i][j] = ' ';
				} else {
					if (i == 0) {
						UIBoard[i][j] = (char) (48 + j);
					} else {
						if (j == 0) {
							UIBoard[i][j] = (char) (64 + i);
						} else {
							TileStatus b = t[i][j];
							switch (b) {
							case SHIP:
							case SUBSCANSHIP:
							case AIRCRAFTSCAN:
								switch (shipBoard[i][j][0]) {
								case AIRCRAFTCARRIER:
									thisTile = 'a';
									break;
								case BATTLESHIP:
									thisTile = 'b';
									break;
								case DESTROYER:
									thisTile = 'd';
									break;
								case SUBMARINE:
									thisTile = 's';
									break;
								case PATROLBOAT:
									thisTile = 'p';
									break;
								default:
									break;
								}
								switch(shipBoard[i][j][1]){
								case AIRCRAFT1:
									if(!(((Aircraft) ships[5]).launched())){
										thisTile = '#';						
									}
									break;
								case AIRCRAFT2:
									if(!(((Aircraft) ships[6]).launched())){
										thisTile = '$';
									}
									break;
								default:
									break;
								}
								break;
							case HIT:
								thisTile = 'X';
								break;
							case MISS:
								thisTile = 'M';
								break;
							case SUBSCANEMPTY:
								thisTile = '/';
								break;
							default:
								thisTile = '-';
								break;
							}
							UIBoard[i][j] = thisTile;
						}
					}
				}
			}
		}
		return UIBoard;
	}

	public char[][] generateTheirUIBoard(TileStatus[][] t, ShipType[][][] s, Ships[] opShips) {
		char thisTile = ' ';
		char[][] UIBoard = new char[11][15];
		for (int i = 0; i < UIBoard.length; i++) {
			for (int j = 0; j < UIBoard[0].length; j++) {
				if (i == 0 && j == 0) {
					UIBoard[i][j] = ' ';
				} else {
					if (i == 0) {
						UIBoard[i][j] = (char) (48 + j);
					} else {
						if (j == 0) {
							UIBoard[i][j] = (char) (64 + i);
						} else {
							TileStatus b = t[i][j];
							switch (b) {
							case HIT:
								thisTile = 'H';
								break;
							case AIRCRAFTSCANEMPTY:
							case MISS:
								thisTile = 'M';
								break;
							case SUBSCANEMPTY:
								thisTile = '/';
								break;
							case SUBSCANSHIP:
								thisTile = '/';
								break;
							case AIRCRAFTSCAN:
								thisTile = '@';
								break;
							default:
								thisTile = '?';
								break;
							}
							if(s[i][j][1] == ShipType.AIRCRAFT1){
								if(((Aircraft) opShips[5]).launched() && !((Aircraft) opShips[5]).isThisShipSunk()){
									thisTile = '#';										
								}
							}else if(s[i][j][1] == ShipType.AIRCRAFT2){
								if(((Aircraft) opShips[6]).launched() && !((Aircraft) opShips[6]).isThisShipSunk()){
									thisTile = '$';										
								}
							}
							UIBoard[i][j] = thisTile;
						}
					}
				}
			}
		}
		
		return UIBoard;
	}
	
	

	public boolean statusUnknown(TileStatus ts) {
		return ts == TileStatus.EMPTY || ts == TileStatus.SHIP
				|| ts == TileStatus.SUBSCANSHIP
				|| ts == TileStatus.SUBSCANEMPTY
				|| ts == TileStatus.AIRCRAFTSCAN;
	}

	public enum TileStatus {
		UNKNOWN, EMPTY, MISS, HIT, SUBSCANSHIP, SUBSCANEMPTY, AIRCRAFTSCAN, AIRCRAFTSCANEMPTY, INVALID, SHIP
	}
}
