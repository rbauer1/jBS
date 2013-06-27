package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class AlgoTest {
	private static int[][] hits;
	private static int[][][][] dynamicProb;
	private static int smallestRemainingShip = 2;

	public static void main(String[] args) throws IOException {
		initializeProbabilities();
		Scanner sc = new Scanner(System.in);
		int x = 0;
		int y = 0;
		int newValue;
		int shipLength = 0;
		printHits();
		while (x != -1) {
			x = sc.nextInt();
			y = sc.nextInt();
			newValue = -sc.nextInt();
			hits[y][x] = newValue;
			dynamicProb[y][x][1][0] = 0;
			dynamicProb[y][x][1][1] = 0;
			dynamicProb[y][x][1][2] = 0;
			dynamicProb[y][x][1][3] = 0;
			dynamicProb[y][x][1][4] = 0;
			for (int i = 5; i > 1; i--) {
				removeDeadSpaces(i);
			}
			updateTotalProbabilities();
			printProbabilities(true, 5);
			printHits();
		}
	}

	public static void printHits() {
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

	private static void initializeProbabilities() throws IOException {
		hits = new int[12][16];

		dynamicProb = new int[12][16][2][5];

		// initialize hits
		for (int i = 0; i < hits.length; i++) {
			for (int j = 0; j < hits[0].length; j++) {
				if (i == 0 || j == 0 || i == 11 || j == 15) {
					hits[i][j] = -1;
				} else
					hits[i][j] = 0;
			}
		}
		BufferedReader br = new BufferedReader(new FileReader(
				"src/base/AI4_Probabilities.txt"));
		String[] tempS = new String[14];
		for (int k = 0; k < 5; k++) {
			for (int i = 0; i < 12; i++) {
				if (i != 0 && i != 11) {
					tempS = br.readLine().split(" ");
				}
				for (int j = 0; j < 16; j++) {
					if (i == 0 || j == 0 || i == 11 || j == 15) {
						dynamicProb[i][j][0][k] = -1;
						dynamicProb[i][j][1][k] = -1;
					} else {
						dynamicProb[i][j][0][k] = Integer
								.parseInt(tempS[j - 1]);
						dynamicProb[i][j][1][k] = dynamicProb[i][j][0][k];
					}
				}
			}
			br.readLine();
		}

		br.close();
	}

	public static void printProbabilities(boolean dynOrStat, int specificShip) {
		int dyOrSt = 0;
		if (dynOrStat) {
			dyOrSt = 1;
		} else {
			dyOrSt = 0;
		}
		if (specificShip == 5) {
			for (int k = 0; k < 5; k++) {
				for (int i = 0; i < 12; i++) {
					for (int j = 0; j < 16; j++) {
						System.out.print(dynamicProb[i][j][dyOrSt][k] + "\t");
					}
					System.out.println();
				}
				System.out.println();
			}
		} else {
			for (int i = 0; i < 12; i++) {
				for (int j = 0; j < 16; j++) {
					System.out.print(dynamicProb[i][j][dyOrSt][specificShip]
							+ "\t");
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	private static void updateTotalProbabilities() {
		int temp = 0;
		for (int i = 1; i < 11; i++) {
			for (int j = 1; j < 15; j++) {
				for (int k = 4; k > 0; k--) {
					temp += dynamicProb[i][j][1][k];
				}
				dynamicProb[i][j][1][0] = temp;
				temp = 0;
			}
		}
	}

	private static void removeDeadSpaces(int lengthOfShipExamined) {
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
					for (int k = 1; k < lengthOfShipExamined && checkD; k++) {
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
					for (int k = 1; k < lengthOfShipExamined && checkU; k++) {
						if (i - k < 0) {
							checkU = false;
						} else {
							if (hits[i - k][j] < 0 && hits[i - k][j] != -4) {
								checkU = false;
							} else
								countU++;
						}
					}

					for (int k = 1; k < lengthOfShipExamined && checkR; k++) {
						if (j + k > 15) {
							checkR = false;
						} else {
							if (hits[i][j + k] < 0 && hits[i][j + k] != -4) {
								checkR = false;
							} else
								countR++;
						}
					}

					for (int k = 1; k < lengthOfShipExamined && checkL; k++) {
						if (j - k < 0) {
							checkL = false;
						} else {
							if (hits[i][j - k] < 0 && hits[i][j - k] != -4) {
								checkL = false;
							} else
								countL++;
						}
					}
					if ((countL + countR + 1 < lengthOfShipExamined)
							&& (countD + countU + 1 < lengthOfShipExamined)) {
						if (lengthOfShipExamined == smallestRemainingShip) {
							hits[i][j] = -3;
						}
						switch (lengthOfShipExamined) {
						case 2:
							dynamicProb[i][j][1][4] = 0;
							dynamicProb[i][j][1][0] = 0;
							break;
						case 3:
							dynamicProb[i][j][1][3] = 0;
							break;
						case 4:
							dynamicProb[i][j][1][2] = 0;
							break;
						case 5:
							dynamicProb[i][j][1][1] = 0;
							break;

						}
					} else if (!checkL || !checkR || !checkU || !checkD) {
						lowerProbs(i, j, lengthOfShipExamined, checkL, checkR,
								checkU, checkD);
					}
				}
			}
		}
	}

	private static void lowerProbs(int i, int j, int lengthOfShipExamined,
			boolean checkL, boolean checkR, boolean checkU, boolean checkD) {
		double adj = 1;
		if (!checkL)
			adj -= 0.25;
		if (!checkR)
			adj -= 0.25;
		if (!checkU)
			adj -= 0.25;
		if (!checkD)
			adj -= 0.25;
		switch (lengthOfShipExamined) {
		case 2:
			dynamicProb[i][j][1][4] = (int) (dynamicProb[i][j][0][4] * adj);
			break;
		case 3:
			dynamicProb[i][j][1][3] = (int) (dynamicProb[i][j][0][3] * adj);
			break;
		case 4:
			dynamicProb[i][j][1][2] = (int) (dynamicProb[i][j][0][2] * adj);
			break;
		case 5:
			dynamicProb[i][j][1][1] = (int) (dynamicProb[i][j][0][1] * adj);
			break;
		}
	}

	public static void initializeShips() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("shipsTest5.txt"));
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
				x = r.nextInt(cols - lengths[currentShip] + 1);
				y = r.nextInt(rows);
				xAdj = 1;
			} else {
				x = r.nextInt(cols);
				y = r.nextInt(rows - lengths[currentShip] + 1);
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
}
