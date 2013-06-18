package base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import ships.Ships;
import ships.Ships.ShipType;
import base.Board.TileStatus;

public class AI2 implements AI{
	private Player pOther;
	private Player pThis;
	private String name = "AI 2";
	@SuppressWarnings("unused")
	// ?
	private TileStatus[][] board;
	private int[][] hits;
	// 0 for untouched, 1 for hit, -1 for miss,
	// -2 for sunken ships, -4 for subscan, -3 for deadspace
	int smallestRemainingShip = 2;
	int originalHitX = 0;
	int originalHitY = 0;
	// boolean debugFirstShot = true;
	int lastHit = -1;
	boolean lastShipSunk = true;
	private SubScanForAI lastSubScan;

	/**
	 * Generates this AI's hits matrix. All valid board positions are initiated
	 * as 0 (unknown). The 1-14 and A-J are initiated as -1
	 * 
	 * @param pOther
	 *            The other Player
	 * @param pThis
	 *            This Player
	 */
	public AI2(Player pOther, Player pThis) {
		this.pOther = pOther;
		this.pThis = pThis;
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

	public void initializeShips() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("shipsTest4.txt"));
		int cols = 14;
		int rows = 10;
		int[][] airLocationForWrite = new int[2][2]; // aircraft 1 is index
														// [0][*]
		// the second dimension holds first the y coordinate, then the x
		// coordinate of the aircraft
		int[][] tempBoard = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tempBoard[i][j] = 0;
			}
		}
		Random r = new Random();
		int air1 = r.nextInt(5);
		int air2 = r.nextInt(5);
		while (air2 == air1) {
			air2 = r.nextInt(5);
		}
		int[] lengths = { 5, 4, 3, 3, 2 };
		int currentShip = 0;
		boolean flag = false;
		while (currentShip < lengths.length && !flag) {
			int orientation = r.nextInt(2);
			int x = 0;
			int y = 0;
			int xAdj = 0;
			int yAdj = 0;
			if (orientation == 0) {
				x = r.nextInt(cols - lengths[currentShip]+1);
				y = r.nextInt(rows);
				xAdj = 1;
			} else {
				x = r.nextInt(cols);
				y = r.nextInt(rows - lengths[currentShip]+1);
				yAdj = 1;
			}
			for (int i = 0; i < lengths[currentShip] && !flag; i++) {
				if (tempBoard[y + yAdj * i][x + xAdj * i] != 0) {
					flag = true;
				}
			}
			if (!flag) {

				for (int i = 0; i < lengths[currentShip]; i++) {
					tempBoard[y + yAdj * i][x + xAdj * i] = lengths[currentShip];
					if (currentShip == 0 && i == air1) {
						tempBoard[y + yAdj * i][x + xAdj * i] = 8;
						airLocationForWrite[0][0] = y + yAdj * i + 1;
						airLocationForWrite[0][1] = x + xAdj * i + 1;
					}
					if (currentShip == 0 && i == air2) {
						tempBoard[y + yAdj * i][x + xAdj * i] = 9;
						airLocationForWrite[1][0] = y + yAdj * i + 1;
						airLocationForWrite[1][1] = x + xAdj * i + 1;
					}

				}
				bw.write((y + 1) + " " + (x + 1) + " " + orientation + "\n");
				currentShip++;
			} else {
				flag = false;
			}
		}
		bw.write(airLocationForWrite[0][0] + " " + airLocationForWrite[0][1]
				+ " " + 0 + "\n");
		bw.write(airLocationForWrite[1][0] + " " + airLocationForWrite[1][1]
				+ " " + 0);
		bw.close();
		// below is code for printing the board
		// for (int i = 0; i < rows; i++) {
		// for (int j = 0; j < cols; j++) {
		// System.out.print(tempBoard[i][j]+ " ");
		// }
		// System.out.println();
		// }
	}

	/**
	 * This method combs through this AI's hits matrix and determines if it
	 * knows the position of a ship that has not yet been sunk. This is
	 * represented as a positive integer 1-5 (inclusive). If none are found, it
	 * will suggest a subscan that represented a possible ship (-4).
	 * 
	 * @return The next position that should be fired upon as a 3x1 matrix with
	 *         the first two entries being the x-y coordinates and the last
	 *         being either a 1 if the method is returning the location of a
	 *         live ship, or 2 if it is returning the possible position of an
	 *         unknown ship. Either a live ship, the possibility of a ship
	 *         (subscan), or null. If null is returned, attack() will try to use
	 *         its submarine to scan, if the submarine is sunk, it will fire
	 *         randomly.
	 */

	public int[] findHits() {
		/*
		 * possibleShip refers to scans from submarines (value -4) a hit array
		 * has a 1 in position 3 a scan array has a 2 in position 3 a -2 is a
		 * sunken ship, a -1 is a miss, a 0 is an unknown a -3 is a space that
		 * has been determined to be empty (through context)
		 */
		boolean random = true;
		Random gen = new Random();
		int[] possibleShip = { -1, -1, 2 };
		for (int i = 1; i < hits.length - 1; i++) {
			for (int j = 1; j < hits[0].length - 1; j++) {
				if (hits[i][j] > 0 && hits[i][j] < 6) { // check if is live ship
					int[] hitPosition = { i, j, 1 }; // return a position matrix
														// for that coordinate
					return hitPosition;
				}
				/*
				 * this conditional checks if there is an inconclusive subscan
				 * on the current coordinate. if there is, and a random boolean
				 * is true, then the position matrix "possibleShip" is updated
				 * and if no live ships are found, the last possibleShip matrix
				 * is returned when the end of the hits matrix is reached
				 */
				if (hits[i][j] == -4 && random) { // random to avoid always
													// firing at top left corner
													// of subscans
					possibleShip[0] = i;
					possibleShip[1] = j;
					random = gen.nextBoolean();
				}
			}
		}
		// conditional check for existent possibleShip.
		if (possibleShip[0] == -1) {
			return null;
		} else
			return possibleShip;
	}

	/**
	 * Works to optimize the AI by placing "Empty" tags (in the form of -3s) in
	 * the AI's hit matrix. First it locates an unknown space (hits[i][j] == 0).
	 * It then uses the int "smallestRemainingShip" which is the length of the
	 * the smallest remaining live ship possessed by the opponent to determine
	 * if the current space could possibly contain said ship.<br />
	 * <br />
	 * The checks if (hits[i + k][j] < 0 && hits[i + k][j] != -4) { and the like
	 * might be able to be changed to !=0 instead of < 0
	 */
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
					// Check down starting the space below the initial unknown
					// coordinate
					for (int k = 1; k < smallestRemainingShip && checkD; k++) {
						// checks if passes bottom of board
						if (i + k > 11) {
							checkD = false;
						} else {
							// makes sure there are no misses/dead ships in the
							// path but does acknowledge that subscans are
							// inconclusive (-4)
							if (hits[i + k][j] < 0 && hits[i + k][j] != -4) {
								checkD = false;
							} else
								countD++;
						}
					}
					// same, except check up (above)
					for (int k = 1; k < smallestRemainingShip && checkU; k++) {
						if (i - k < 0) {
							checkU = false;
						} else {
							if (hits[i - k][j] < 0 && hits[i - k][j] != -4) {
								checkU = false;
							} else
								countU++;
						}
					}

					for (int k = 1; k < smallestRemainingShip && checkR; k++) {
						if (j + k > 15) {
							checkR = false;
						} else {
							if (hits[i][j + k] < 0 && hits[i][j + k] != -4) {
								checkR = false;
							} else
								countR++;
						}
					}

					for (int k = 1; k < smallestRemainingShip && checkL; k++) {
						if (j - k < 0) {
							checkL = false;
						} else {
							if (hits[i][j - k] < 0 && hits[i][j - k] != -4) {
								checkL = false;
							} else
								countL++;
						}
					}
					if ((countL + countR + 1 < smallestRemainingShip)
							&& (countD + countU + 1 < smallestRemainingShip)) {
						// System.out.println("PROBABLY WRONG");
						hits[i][j] = -3;
					}
				}
			}
		}
	}

	/**
	 * Called by attack() <br />
	 * Generates a random valid x-y coordinate and uses a subscan on that
	 * location. Includes checks for relevance. Does not necessarily avoid large
	 * overlaps. Possibly a good place to work on optimizing.<br/>
	 * Also updates lastSubScan which is a SubScanForAI object that is used by
	 * the attack() method
	 */
	private void subScan() {
		Random gen = new Random();
		int x;
		int y;
		int check = -1;
		int newValue = 0;
		while (check == -1) {
			x = gen.nextInt(12) + 2;
			y = gen.nextInt(8) + 2;
			check = pThis.getSub().scan(pOther, x, y);
			if (check == 1) { // ship found
				newValue = -4; // possibly a ship in this space
			} else {
				newValue = -1; // no ships (miss)
			}
			// update hit matrix with either -1s (misses)
			// or with -4s (possibly ship coordinate)
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (hits[y + i][x + j] == 0) {
						hits[y + i][x + j] = newValue;
					}
				}
			}
			// this SubScanForAI object is used in the attack method
			lastSubScan = new SubScanForAI(x, y, hits);
		}
	}

	private void subScan(int x, int y) {
		int check = pThis.getSub().scan(pOther, x, y);
		int newValue = 0;
		if (check == 1) { // ship found
			newValue = -4; // possibly a ship in this space
		} else {
			newValue = -1; // no ships (miss)
		}
		// update hit matrix with either -1s (misses)
		// or with -4s (possibly ship coordinate)
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (hits[y + i][x + j] == 0 || hits[y + i][x + j] == -4) {
					hits[y + i][x + j] = newValue;
				}
			}
		}
	}

	public void attack() {
		smallestRemainingShip = pOther.getSmallestRemainingShip();
		board = pOther.getPlayerStatusBoard();
		removeDeadSpaces();
		Random gen = new Random();
		if (findHits() == null) {
			if (!pThis.getSub().isThisShipSunk()) {
				subScan();
			} else {
				int x = gen.nextInt(14) + 1;
				int y = gen.nextInt(10) + 1;
				while (hits[y][x] != 0) {
					x = gen.nextInt(14) + 1;
					y = gen.nextInt(10) + 1;
				}

				switch (Actions.attack(pOther, x, y)) {
				case 0:
					hits[y][x] = -1;
					break;
				case 1:
					hits[y][x] = determineShip(x, y);
					break;
				}
			}
		} else {
			int[] pos = findHits();
			if (pos[2] == 2) {
//				if (!pThis.getSub().isThisShipSunk() && lastSubScan.getRelevance()
//						&& lastSubScan.update(hits)) {
//					subScan(lastSubScan.getCenterCoords()[0],
//							lastSubScan.getCenterCoords()[1]);
//
//				} else {
					switch (Actions.attack(pOther, pos[1], pos[0])) {
					case 0:
						hits[pos[0]][pos[1]] = -1;
						break;
					case 1:
						hits[pos[0]][pos[1]] = determineShip(pos[1], pos[0]);
						break;
					}
//				}
			} else {
				if (lastHit == -1) {
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
						if (hits[pos[0] + yMove][pos[1] + xMove] == 0
								|| hits[pos[0] + yMove][pos[1] + xMove] == -4) {
							switch (Actions.attack(pOther, pos[1] + xMove,
									pos[0] + yMove)) {
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
						if (hits[pos[0] + yMove][pos[1] + xMove] == 0
								|| hits[pos[0] + yMove][pos[1] + xMove] == -4) {
							switch (Actions.attack(pOther, pos[1] + xMove,
									pos[0] + yMove)) {
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
			}
		}
		updateHits();
	}

	/**
	 * Determines if a ship has been sunk, and if so changes hits[][] to account
	 * for that (-2). could use some optimization to reduce redundant searches
	 * and updates after sunk ships have been accounted for. do that at some
	 * point
	 */
	private void updateHits() {
		Ships[] s = pOther.getAllShips();
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
		ShipType st = pOther.getPlayerShipBoard()[y][x][0];
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
	
	public String getName(){
		return name;
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
