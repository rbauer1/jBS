package base;

import java.util.Random;

import ships.Ships;
import ships.Ships.ShipType;
import base.Board.TileStatus;

public class AI {
	private Player p;
	@SuppressWarnings("unused") //?
	private TileStatus[][] board;
	private int[][] hits;
	int smallestRemainingShip = 2;
	int originalHitX = 0;
	int originalHitY = 0;
	// int lastHitX = 0;
	// int lastHitY = 0;
	// boolean debugFirstShot = true;
	int lastHit = -1;
	boolean lastShipSunk = true;

	// int leftRight = 0;
	// int upDown = 0;

	public AI(Player p) {
		this.p = p;
		hits = new int[12][16];
		for (int i = 0; i < hits.length; i++) {
			for (int j = 0; j < hits[0].length; j++) {
				if (i == 0 || j == 0 || i == 11 || j == 15) {
					hits[i][j] = -1;
				} else
					hits[i][j] = 0;
			}
		}
	}

	public int[] findHits() {
		for (int i = 1; i < hits.length - 1; i++) {
			for (int j = 1; j < hits[0].length - 1; j++) {
				if (hits[i][j] > 0 && hits[i][j] < 6) {
					int[] hitPosition = { i, j };
					return hitPosition;
				}
			}
		}
		return null;
	}

	private void removeDeadSpaces() {
		for (int i = 1; i < hits.length - 1; i++) {
			for (int j = 1; j < hits[0].length - 1; j++) {
				if (hits[i][j] == 0) {
					boolean checkL = true;
					boolean checkR = true;
					boolean checkU = true;
					boolean checkD = true;
					int countL = 0;
					int countR = 0;
					int countU = 0;
					int countD = 0;

					for (int k = 1; k < smallestRemainingShip && checkD; k++) {
						if (i + k > 11) {
							checkD = false;
						} else {
							if (hits[i + k][j] < 0) {
								checkD = false;
							} else
								countD++;
						}
					}

					for (int k = 1; k < smallestRemainingShip && checkU; k++) {
						if (i - k < 0) {
							checkU = false;
						} else {
							if (hits[i - k][j] < 0) {
								checkU = false;
							} else
								countU++;
						}
					}

					for (int k = 1; k < smallestRemainingShip && checkR; k++) {
						if (j + k > 15) {
							checkR = false;
						} else {
							if (hits[i][j + k] < 0) {
								checkR = false;
							} else
								countR++;
						}
					}

					for (int k = 1; k < smallestRemainingShip && checkL; k++) {
						if (j - k < 0) {
							checkL = false;
						} else {
							if (hits[i][j - k] < 0) {
								checkL = false;
							} else
								countL++;
						}
					}
					if ((countL + countR + 1 < smallestRemainingShip)
							&& (countD + countU + 1 < smallestRemainingShip)) {
//						System.out.println("PROBABLY WRONG");
						hits[i][j] = -3;
					}
				}
			}
		}
	}

	public void attack() {
		smallestRemainingShip = p.getSmallestRemainingShip();
		board = p.getPlayerStatusBoard();
		removeDeadSpaces();
		Random gen = new Random();
		if (findHits() == null) {
			int x = gen.nextInt(14) + 1;
			int y = gen.nextInt(10) + 1;
			while (hits[y][x] != 0) {
				x = gen.nextInt(14) + 1;
				y = gen.nextInt(10) + 1;
			}
			// if(debugFirstShot == true){
			// x = 8;
			// y = 7;
			// debugFirstShot = false;
			// }
			switch (Actions.attack(p, x, y)) {
			case 0:
				hits[y][x] = -1;
				break;
			case 1:
				hits[y][x] = determineShip(x, y);
				break;
			}
		} else if (lastHit == -1) {
			int[] pos = findHits();
			boolean repeat = true;
			while (repeat) {
				int chooseDirection = gen.nextInt(4);
				int xMove = 0;
				int yMove = 0;
				switch (chooseDirection) {
				case 0:
					xMove = -1;
					lastHit = 0;
					break;
				case 1:
					xMove = 1;
					lastHit = 1;
					break;
				case 2:
					yMove = -1;
					lastHit = 2;
					break;
				case 3:
					yMove = 1;
					lastHit = 3;
					break;
				}
				if (hits[pos[0] + yMove][pos[1] + xMove] == 0) {
					switch (Actions.attack(p, pos[1] + xMove, pos[0] + yMove)) {
					case 0:
						hits[pos[0] + yMove][pos[1] + xMove] = -1;
						lastHit = -1;
						break;
					case 1:
						hits[pos[0] + yMove][pos[1] + xMove] = determineShip(
								pos[1] + xMove, pos[0] + yMove);
						break;
					}
					repeat = false;
				}
			}
		} else {

			int[] pos = findHits();
			boolean repeat = true;
			while (repeat) {
				int xMove = 0;
				int yMove = 0;
				switch (lastHit) {
				case 0:
					xMove = -1;
					while (hits[pos[0] + yMove][pos[1] + xMove] > 0) {
						xMove--;
					}
					break;
				case 1:
					xMove = 1;
					while (hits[pos[0] + yMove][pos[1] + xMove] > 0) {
						xMove++;
					}
					break;
				case 2:
					yMove = -1;
					while (hits[pos[0] + yMove][pos[1] + xMove] > 0) {
						yMove--;
					}
					break;
				case 3:
					yMove = 1;
					while (hits[pos[0] + yMove][pos[1] + xMove] > 0) {
						yMove++;
					}
					break;
				}
				if (hits[pos[0] + yMove][pos[1] + xMove] == 0) {
					switch (Actions.attack(p, pos[1] + xMove, pos[0] + yMove)) {
					case 0:
						hits[pos[0] + yMove][pos[1] + xMove] = -1;
						if (lastHit == 0 || lastHit == 2) {
							lastHit++;
						} else {
							lastHit--;
						}
						break;
					case 1:
						hits[pos[0] + yMove][pos[1] + xMove] = determineShip(
								pos[1] + xMove, pos[0] + yMove);
						break;
					}
					repeat = false;
				} else {
					lastHit = gen.nextInt(4);
				}

			}
		}
		updateHits();
	}

	/**
	 * Determines if a ship has been sunk, and if so changes hits[][] to account
	 * for that. could use some optimization to reduce redundant searches and
	 * updates after sunk ships have been accounted for. do that at some point
	 */
	private void updateHits() {
		Ships[] s = p.getAllShips();
		for (int k = 0; k < 5; k++) {
			if (s[k].isThisShipSunk()) {
				for (int i = 1; i < hits.length - 1; i++) {
					for (int j = 1; j < hits[0].length - 1; j++) {
						if (hits[i][j] == k + 1)
							hits[i][j] = -2;
					}
				}
			}
		}
	}

	private int determineShip(int x, int y) {
		ShipType st = p.getPlayerShipBoard()[y][x][0];
		switch (st) {
		case AIRCRAFTCARRIER:
			return 1;
		case BATTLESHIP:
			return 2;
		case DESTROYER:
			return 3;
		case SUBMARINE:
			return 4;
		case PATROLBOAT:
			return 5;
		default:
			return -1;
		}
	}

	// private int[] determineNextShot() {
	// Random gen = new Random();
	// if (lastHitX == 0 && lastHitY == 0) {
	// int lr = gen.nextInt(3) - 1;
	// int ud = gen.nextInt(3) - 1;
	// while (lr == 0 && ud == 0) {
	// lr = gen.nextInt(3) - 1;
	// ud = gen.nextInt(3) - 1;
	// }
	// if (gen.nextBoolean()) {
	// if (lr != 0 && ud != 0) {
	// ud = 0;
	// }
	// } else {
	// if (lr != 0 && ud != 0) {
	// lr = 0;
	// }
	// }
	// }
	// }
	//
	// public void resetWeights() {
	// lastShipSunk = true;
	// // leftRight = 0;
	// // upDown = 0;
	// lastHitX = 0;
	// lastHitY = 0;
	// originalHitX = 0;
	// originalHitY = 0;
	// }
	//
	// public void setLastHit(int x, int y) {
	// lastHitX = x;
	// lastHitY = y;
	// }

	public int[][] getAllHits() {
		return hits;
	}

	public void printHits() {
		System.out.println("Doesn't include negative signs");
		for (int i = 0; i < hits.length - 1; i++) {
			for (int j = 0; j < hits[0].length - 1; j++) {
				if (i == 0 && j == 0) {
					System.out.print("  ");
				} else {
					if (i == 0) {
						System.out.print((char) (48 + j) + " ");
					} else {
						if (j == 0) {
							System.out.print((char) (64 + i) + " ");
						} else {
							System.out.print(hits[i][j] * -1 + " ");
						}
					}
				}
			}
			System.out.println();
		}
	}
}
