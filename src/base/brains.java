package base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class brains {
	static long[][][][] total = new long[10][14][5][1];

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 14; j++) {
				for (int k = 0; k < 5; k++) {
					for (int l = 0; l < 1; l++) {
						total[i][j][k][l] = 0;
					}
				}
			}
		}
		for (long n = 0; true; n++) {
			int[][] temp = initializeShips();
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 7; j++) {
					switch (temp[i][j]) {
					case 0:
						break;
					case 2:
						// patrol is 3
						total[i][j][3][0]++;
						total[i][j][4][0]++;
						break;
					case 3:
						// sub and destroyer are 2
						total[i][j][2][0]++;
						total[i][j][4][0]++;
						break;
					case 4:
						// battleship is 1
						total[i][j][1][0]++;
						total[i][j][4][0]++;
						break;
					case 5:
						// carrier is 0
						total[i][j][0][0]++;
						total[i][j][4][0]++;
						break;
					}
				}
			}
			if (n % 50000000 == 0) {
				System.out.println(n + "-----------------------------");
				for (int k = 0; k < 5; k++) {
					switch (k) {
					case 0:
						System.out.println("Carrier");
						break;
					case 1:
						System.out.println("Battleship");
						break;
					case 2:
						System.out.println("Sub/Destroyer");
						break;
					case 3:
						System.out.println("Patrol Boat");
						break;
					case 4:
						System.out.println("TOTAL");
						break;
					}
					for (int i = 0; i < 10; i++) {
						for (int j = 0; j < 14; j++) {
							System.out.print(total[i][j][k][0] + "\t");
						}
						System.out.println();
					}

					System.out.println();
				}
			}
		}

	}

	public static int[][] initializeShips() throws IOException {
		// BufferedWriter bw = new BufferedWriter(new
		// FileWriter("shipsTest4.txt"));
		int cols = 14;
		int rows = 10;
		int[][] tempBoard = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tempBoard[i][j] = 0;
			}
		}
		Random r = new Random();
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
				}
				// bw.write((y + 1) + " " + (x + 1) + " " + orientation + "\n");
				currentShip++;
			} else {
				flag = false;
			}
		}
		// bw.close();
		// below is code for printing the board
		// for (int i = 0; i < rows; i++) {
		// for (int j = 0; j < cols; j++) {
		// System.out.print(tempBoard[i][j] + " ");
		// }
		// System.out.println();
		// }
		// System.out.println();
		return tempBoard;
	}
}
