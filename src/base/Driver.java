package base;

import java.io.IOException;
import java.util.Random;

import ships.Aircraft;
import ships.AircraftCarrier;
import ships.Battleship;
import ships.Destroyer;
import ships.PatrolBoat;
import ships.Ships;
import ships.Ships.ShipType;
import ships.Submarine;

public class Driver {
	static Player p1;
	static Player p2;
	static Actions action;
	static AI ai1;
	static AI ai2;
	static int debugCount = 0;
	static int AI1wins = 0;
	static int AI2wins = 0;
	static int AI1average = 0;
	static int AI2average = 0;
	private static TestObject testObject;
	private static boolean turnReady;
	static Display d = null;

	public static void testRun(int numRuns) throws IOException {
		long timeTotal = System.currentTimeMillis();
		for (int i = 0; i < numRuns; i++) {
			
			if(testObject.isDisplayGUI()){
			    d = new Display(testObject.isPlayerIncluded());
			}
			if(testObject.isPlayerIncluded()){
			    while(!d.arePlayerShipsInitialized()){
			        try {
		                Thread.sleep(50); //might cause lag, may need to lower
		            } catch (InterruptedException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		            }
			    }
			    System.out.println("Holla!");
			    p2 = d.getPlayer();
			}
			initialize();
			if(testObject.isDisplayGUI()){
				d.setPlayersAndAI(p2, p1, ai1);
			}
			while (!gameOver()) {
			    long time=System.currentTimeMillis();
			    while(System.currentTimeMillis()-time<testObject.getTurnDelay());
				debugCount++;
				if (testObject.isPlayerIncluded()) {
					progressTurnOnePlayer();
				} else {
					progressTurnNoPlayers();
				}
				if(testObject.isDisplayGUI()){
				    d.updateBoards();
				}
			}
//			System.out.println("In " + debugCount + " turns!");
			debugCount = 0;
			if (testObject.isDisplayHitBoards()) {
				ai1.printHits();
				System.out.println("-------------------------------");
				if(!testObject.isPlayerIncluded()){
				    ai2.printHits();
				}
			}
			if((i+1)!=numRuns && testObject.isDisplayGUI()){
			    d.dispose();
			}
		}
		System.out.println(ai1.getName());
		System.out.println("Number of Wins: " + AI1wins);
		if (AI1wins > 0) {
			System.out
					.println("Average Turns per win: " + AI1average / AI1wins);
		}
		if(!testObject.isPlayerIncluded()){
		    System.out.println(ai2.getName());
		System.out.println("Number of Wins: " + AI2wins);
		if (AI2wins > 0) {
			System.out
					.println("Average Turns per win: " + AI2average / AI2wins);
		}
		}
		System.out.println("Total run time = " + (((System.currentTimeMillis()-timeTotal)/1000)/60) + " minutes and "  + (((System.currentTimeMillis()-timeTotal)/1000)%60) + " seconds");
	}

	public static void main(String[] args) throws IOException {
		turnReady = false;
		TestPanel tp = new TestPanel();
		testObject = tp.getTestObject();
		tp.dispose();
		System.out.println(testObject.getNumberOfTestRuns());
		testRun(testObject.getNumberOfTestRuns());
		
//		System.exit(0);
		

		// initialize();
		// while (!gameOver()) {
		// debugCount++;
		// TIMER TO SLOW DOWN AND ALLOW ANALYSIS
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// 
		// e.printStackTrace();
		// }
		// progressTurn();
		// }
	}

	public static void initialize() throws IOException {
		int shipInfo[][] = initializeShips();
		AircraftCarrier ac = new AircraftCarrier(shipInfo[0][0],
				shipInfo[0][1], shipInfo[0][2]);
		Battleship bs = new Battleship(shipInfo[1][0], shipInfo[1][1],
				shipInfo[1][2]);
		Destroyer de = new Destroyer(shipInfo[2][0], shipInfo[2][1],
				shipInfo[2][2]);
		Submarine sub = new Submarine(shipInfo[3][0], shipInfo[3][1],
				shipInfo[3][2]);
		PatrolBoat pb = new PatrolBoat(shipInfo[4][0], shipInfo[4][1],
				shipInfo[4][2]);
		Aircraft a1 = new Aircraft(shipInfo[5][0], shipInfo[5][1],
				shipInfo[5][2], 1);
		Aircraft a2 = new Aircraft(shipInfo[6][0], shipInfo[6][1],
				shipInfo[6][2], 2);
		Ships[] ships = { ac, bs, de, sub, pb, a1, a2 };
		Board b1 = new Board(ships);
		// System.out.println();
		p1 = new Player(b1, true, ships);
		if(!testObject.isPlayerIncluded()){
			AircraftCarrier ac2 = new AircraftCarrier(shipInfo[0][0],
					shipInfo[0][1], shipInfo[0][2]);
			Battleship bs2 = new Battleship(shipInfo[1][0], shipInfo[1][1],
					shipInfo[1][2]);
			Destroyer de2 = new Destroyer(shipInfo[2][0], shipInfo[2][1],
					shipInfo[2][2]);
			Submarine sub2 = new Submarine(shipInfo[3][0], shipInfo[3][1],
					shipInfo[3][2]);
			PatrolBoat pb2 = new PatrolBoat(shipInfo[4][0], shipInfo[4][1],
					shipInfo[4][2]);
			Aircraft a12 = new Aircraft(shipInfo[5][0], shipInfo[5][1],
					shipInfo[5][2], 1);
			Aircraft a22 = new Aircraft(shipInfo[6][0], shipInfo[6][1],
					shipInfo[6][2], 2);
	        Ships[] ships2 = { ac2, bs2, de2, sub2, pb2, a12, a22};
	        Board b2 = new Board(ships2);
	        p2 = new Player(b2, true, ships2);
		}
		/*
		 * I know there must be a better way to do this. figure it out.
		 */
		switch (testObject.getFirstAIChosen()) {
		case NONE:
			System.out
					.println("If only using 1 AI, it needs to be from the first drop down menu");
			System.exit(1);
		case AI:
			ai1 = new AI1(p2);
			break;
		case AI2:
			ai1 = new AI2(p2, p1);
			break;
		case AI3:
			ai1 = new AI3(p2, p1);
			break;
		case AI3_1:
			ai1 = new AI3_1(p2, p1);
			break;
		case AI4_0:
            ai1 = new AI4_0(p2, p1);
            break;
		case AI4_01:
            ai1 = new AI4_01(p2, p1);
            break;
		case AI4_02:
            ai1 = new AI4_02(p2, p1);
            break;
		case AI4:
			ai1 = new AI4(p2, p1);
			break;
		case AI4_1:
			ai1 = new AI4_1(p2, p1);
			break;
		case AI4_11:
            ai1 = new AI4_11(p2, p1);
            break;
		case AI4_12:
            ai1 = new AI4_12(p2, p1);
            break;
		case AI4_13:
            ai1 = new AI4_13(p2, p1);
            break;
		}
		if (!testObject.isPlayerIncluded()) {
			switch (testObject.getSecondAIChosen()) {
			case NONE:
				// Maybe should just eliminate this?
				break;
			case AI:
				ai2 = new AI1(p1);
				break;
			case AI2:
				ai2 = new AI2(p1, p2);
				break;
			case AI3:
				ai2 = new AI3(p1, p2);
				break;
			case AI3_1:
				ai2 = new AI3_1(p1, p2);
				break;
			case AI4_0:
			    ai2 = new AI4_0(p1, p2);
			    break;
			case AI4_01:
                ai2 = new AI4_01(p1, p2);
                break;
			case AI4_02:
                ai2 = new AI4_02(p1, p2);
                break;
			case AI4:
				ai2 = new AI4(p1, p2);
				break;
			case AI4_1:
				ai2 = new AI4_1(p1, p2);
				break;
			case AI4_11:
                ai2 = new AI4_11(p1, p2);
                break;
			case AI4_12:
                ai2 = new AI4_12(p1, p2);
                break;
			case AI4_13:
                ai2 = new AI4_13(p1, p2);
                break;
			}
		}

	}

	public enum AIName {
		NONE, AI, AI2, AI3, AI3_1, AI4_0, AI4_01, AI4_02, AI4, AI4_1, AI4_11, AI4_12, AI4_13
	}

	/**
	 * This is a temporary method that only exists to print when ships are
	 * destroyed to the terminal, it should be removed once an actual GUI is
	 * built for the game
	 */
	public static void printShipDestroyed(ShipType shipType) {
//		if(testObject.isDisplayShipsSunk()){
//			System.out.println("Enemy " + shipType + " has been sunk!");
//		}
		if(p1.myTurn()){
			d.setMessage("Player2's " + shipType + " has been sunk!");
		}else{
			d.setMessage("Player1's " + shipType + " has been sunk!");
		}
	}

	public static boolean gameOver() {
		if (!p1.isAlive()) {
//			System.out.println("PLAYER 2 WINS!!!");
			AI2wins++;
			AI2average += debugCount;
			d.setMessage("Player2 Wins!");
			return true;
		}
		if (!p2.isAlive()) {
//			System.out.println("PLAYER 1 WINS!!!");
			AI1wins++;
			AI1average += debugCount;
			d.setMessage("Player1 Wins!");
			return true;
		}
		return false;
	}

	public static void progressTurnNoPlayers() {
		Player current;
		Player other;
		if (p1.myTurn()) {
			current = p1;
			other = p2;
		} else {
			current = p2;
			other = p1;
		}
		if (p2.myTurn()) {
			ai2.attack();
		} else {
			ai1.attack();
		}
		if (testObject.isDisplayBoardsTurn()) {
			current.playerPrintBoard(other);
			System.out.println("-------------------------------------------");
		}
		p1.changeTurn();
		p2.changeTurn();

	}

	public static void progressTurnOnePlayer() {
		Player current;
		Player other;
		boolean turnFinished = false;
		if (p2.myTurn()) {
			current = p2;
			other = p1;
		} else {
			current = p1;
			other = p2;
		}

		if (p1.myTurn()) {
			ai1.attack();
//		
		}else{
			while(!turnReady){
		        try {
	                Thread.sleep(50); //might cause lag, may need to lower, or change altogether
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
		    }
			TurnObject newTurn = d.getTurnObject();
			current.playerPrintBoard(other);
				switch (newTurn.getAction()) {
				case FIRE_REGULAR:
					if (Actions.attack(other, newTurn.getxTile(), newTurn.getyTile()) != -1) {
						turnFinished = true;
					}
					break;

				case FIRE_MISSILE:
//					if (!current.getShip(shipIndex).canFireMissile()) {
//						break;
//					}
					switch(newTurn.getMissileType()){
					case CARRIER_X:
						if (current.getShip(0).fireMissile(other, newTurn.getxTile(), newTurn.getyTile(),
								1)[0][0]!=-1) {
							turnFinished = true;
						}
						break;
					case CARRIER_CROSS:
						if (current.getShip(0).fireMissile(other, newTurn.getxTile(), newTurn.getyTile(),
								2)[0][0]!=-1) {
							turnFinished = true;
						}
						break;
					case BATTLESHIP:
						if (current.getShip(1).fireMissile(other, newTurn.getxTile(), newTurn.getyTile(),
								1)[0][0]!=-1) {
							turnFinished = true;
						}
						break;
					case DESTROYER_V:
						if (current.getShip(2).fireMissile(other, newTurn.getxTile(), newTurn.getyTile(),
								1)[0][0]!=-1) {
							turnFinished = true;
						}
						break;
					case DESTROYER_H:
						if (current.getShip(2).fireMissile(other, newTurn.getxTile(), newTurn.getyTile(),
								2)[0][0]!=-1) {
							turnFinished = true;
						}
						break;
					case SUB_V:
						if (current.getShip(3).fireMissile(other, newTurn.getxTile(), newTurn.getyTile(),
								2)[0][0]!=-1) {
							turnFinished = true;
						}
						break;
					case SUB_H:
						if (current.getShip(3).fireMissile(other, newTurn.getxTile(), newTurn.getyTile(),
								1)[0][0]!=-1) {
							turnFinished = true;
						}
						break;
					}
					break;

				case SCAN:
					switch(newTurn.getScanType()){
					case SUB:
						if (current.getSub().scan(other, newTurn.getxTile(), newTurn.getyTile()) != -1) {
							turnFinished = true;
						}
						break;
					case AIRCRAFT_X:
						if (current.getAir(newTurn.getAircraftNumber()).scan(other, 1) != -1) {
							turnFinished = true;
						}
						break;
					case AIRCRAFT_CROSS:
						if (current.getAir(newTurn.getAircraftNumber()).scan(other, 2) != -1) {
							turnFinished = true;
						}
						break;
					}
					break;
				case MOVE_AIRCRAFT:
					if (Actions.moveAir(current, 4+newTurn.getAircraftNumber(), newTurn.getxTile(), newTurn.getyTile())) {
						turnFinished = true;
					}
					break;
				case FIRE_ANTIAIR:
					if (Actions.antiAircraft(other, newTurn.getxTile(), newTurn.getyTile())) {
						turnFinished = true;
					}
					break;
				default:
					System.out.println("No quitting!");
					turnFinished = false;
					break;
				}
			turnReady = false;
		}
		// current.playerPrintBoard(other);
		p1.changeTurn();
		p2.changeTurn();
		System.out.println("-------------------------------------------");
	}
	
	protected static void playerTurnReady(){
		turnReady = true;
	}
	
	/**
	 * This method should probably move to a different class
	 * @throws IOException
	 */
	public static int[][] initializeShips() throws IOException {
        int cols = 14;
        int rows = 10;
        int[][] airLocationForWrite = new int[2][2]; // aircraft 1 is index
        // [0][*]
        // the second dimension holds first the y coordinate, then the x
        // coordinate of the aircraft
        int[][] tempBoard = new int[rows][cols];
        int[][] shipPositionMatrix = new int[7][3];
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
        int[] lengths = {5, 4, 3, 3, 2};
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
                shipPositionMatrix[currentShip][0] = y + 1;
                shipPositionMatrix[currentShip][1] = x + 1;
                shipPositionMatrix[currentShip][2] = orientation;
                currentShip++;
            } else {
                flag = false;
            }
        }
        shipPositionMatrix[currentShip][0] = airLocationForWrite[0][0];
        shipPositionMatrix[currentShip][1] = airLocationForWrite[0][1];
        shipPositionMatrix[currentShip][2] = 0;
        currentShip++;
        shipPositionMatrix[currentShip][0] = airLocationForWrite[1][0];
        shipPositionMatrix[currentShip][1] = airLocationForWrite[1][1];
        shipPositionMatrix[currentShip][2] = 0;
        return shipPositionMatrix;
        // below is code for printing the board
        // for (int i = 0; i < rows; i++) {
        // for (int j = 0; j < cols; j++) {
        // System.out.print(tempBoard[i][j]+ " ");
        // }
        // System.out.println();
        // }
    }

}
