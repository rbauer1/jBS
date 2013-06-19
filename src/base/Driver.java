package base;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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

	public static void testRun(int numRuns) throws IOException {
		for (int i = 0; i < numRuns; i++) {
			initialize();
			while (!gameOver()) {
				debugCount++;
				if (testObject.isPlayerIncluded()) {
					progressTurnOnePlayer();
				} else {
					progressTurnNoPlayers();
				}
			}
			System.out.println("In " + debugCount + " turns!");
			debugCount = 0;
			if (testObject.isDisplayHitBoards()) {
				ai1.printHits();
				System.out.println("-------------------------------");
				ai2.printHits();
			}
		}
		System.out.println(ai1.getName());
		System.out.println("Number of Wins: " + AI1wins);
		if (AI1wins > 0) {
			System.out
					.println("Average Turns per win: " + AI1average / AI1wins);
		}
		System.out.println(ai2.getName());
		System.out.println("Number of Wins: " + AI2wins);
		if (AI2wins > 0) {
			System.out
					.println("Average Turns per win: " + AI2average / AI2wins);
		}
	}

	public static void main(String[] args) throws IOException {
		// Display d = new Display();
		// action = new Actions();
		TestPanel tp = new TestPanel();
		testObject = tp.getTestObject();
		System.out.println(testObject.getNumberOfTestRuns());
		testRun(testObject.getNumberOfTestRuns());
		// initialize();
		// while (!gameOver()) {
		// debugCount++;
		// TIMER TO SLOW DOWN AND ALLOW ANALYSIS
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// progressTurn();
		// }
	}

	public static void initialize() throws IOException {
		Scanner sc = new Scanner(new File("shipsTest3.txt"));
		String[] input = sc.nextLine().split(" ");
		AircraftCarrier ac = new AircraftCarrier(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Battleship bs = new Battleship(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Destroyer de = new Destroyer(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Submarine sub = new Submarine(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		PatrolBoat pb = new PatrolBoat(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Aircraft a1 = new Aircraft(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]), 1);
		input = sc.nextLine().split(" ");
		Aircraft a2 = new Aircraft(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]), 2);
		Ships[] ships = { ac, bs, de, sub, pb, a1, a2 };
		Board b1 = new Board(ships);
		// System.out.println();
		sc = new Scanner(new File("shipsTest4.txt"));
		input = sc.nextLine().split(" ");
		AircraftCarrier ac2 = new AircraftCarrier(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Battleship bs2 = new Battleship(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Destroyer de2 = new Destroyer(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Submarine sub2 = new Submarine(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		PatrolBoat pb2 = new PatrolBoat(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]));
		input = sc.nextLine().split(" ");
		Aircraft a21 = new Aircraft(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]), 1);
		input = sc.nextLine().split(" ");
		Aircraft a22 = new Aircraft(Integer.parseInt(input[0]),
				Integer.parseInt(input[1]), Integer.parseInt(input[2]), 2);
		Ships[] ships2 = { ac2, bs2, de2, sub2, pb2, a21, a22 };
		Board b2 = new Board(ships2);
		p1 = new Player(b1, true, ships);
		p2 = new Player(b2, false, ships2);
		/*
		 * I know there must be a better way to do this. figure it out.
		 */
		/*
		 * ALSO currently the AIs only generate a new board for their NEXT game
		 * because of the silly way that things are set up. they each have an
		 * identical method called initializeShips() that generates a shipTest
		 * file for them that is read by the initializer. AI1 uses shipTest3.txt
		 * and AI2 uses shipTest4.txt I'm not sure how important it actually is,
		 * but it probably should be changed so that they generate a new board
		 * for their current game, instead of their next one.
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
		}
		ai1.initializeShips();
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
			}
			ai2.initializeShips();
		}

	}

	public enum AIName {
		NONE, AI, AI2, AI3, AI3_1
	}

	/**
	 * This is a temporary method that only exists to print when ships are
	 * destroyed to the terminal, it should be removed once an actual GUI is
	 * built for the game
	 */
	public static void printShipDestroyed(ShipType shipType) {
		if(testObject.isDisplayShipsSunk()){
			System.out.println("Enemy " + shipType + " has been sunk!");
		}
	}

	public static boolean gameOver() {
		if (!p1.isAlive()) {
			System.out.println("PLAYER 2 WINS!!!");
			AI2wins++;
			AI2average += debugCount;
			return true;
		}
		if (!p2.isAlive()) {
			System.out.println("PLAYER 1 WINS!!!");
			AI1wins++;
			AI1average += debugCount;
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
		int x;
		int y;
		int config = 1;
		int shipIndex;
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
		} else {
			current.playerPrintBoard(other);
			System.out.println("Player " + (current.getID() ? 1 : 2));
			Scanner sc = new Scanner(System.in);
			while (!turnFinished) {
				System.out.print("Select Option: ");
				int choice = sc.nextInt();
				switch (choice) {
				case 1:
					System.out.print("x coord: ");
					x = sc.nextInt();
					System.out.print("y coord: ");
					y = sc.nextInt();
					if (Actions.attack(other, x, y) != -1) {
						turnFinished = true;
					}
					break;

				case 2:
					System.out.println("AircraftCarrier = 0, Battleship = 1,"
							+ '\n' + "Destroyer = 2, Submarine = 3");
					System.out.print("Choose Ship: ");
					shipIndex = sc.nextInt();
					while (shipIndex < 0 || shipIndex > 3) {
						System.out.print("Choose Ship: ");
						shipIndex = sc.nextInt();
					}
					if (!current.getShip(shipIndex).canFireMissile()) {
						break;
					}
					if (shipIndex != 1) {
						System.out.print("Missile Type: ");
						config = sc.nextInt();
						while (config < 1 || config > 2) {
							System.out.print("Missile Type: ");
							config = sc.nextInt();
						}
					}
					System.out.print("x coord: ");
					x = sc.nextInt();
					System.out.print("y coord: ");
					y = sc.nextInt();
					if (current.getShip(shipIndex).fireMissile(other, x, y,
							config)) {
						turnFinished = true;
					}
					break;

				case 3:
					System.out
							.println("Sub scan = 3, Aircraft1 scan = 5, Aircraft2 scan = 6");
					// Insert actual choose ship after aircraft implemented
					shipIndex = sc.nextInt();
					if (shipIndex != 3 && shipIndex != 5 && shipIndex != 6) {
						break;
					}
					if (current.getShip(shipIndex).isThisShipSunk()) {
						break;
					}
					if (shipIndex == 5) {
						System.out.print("Scan configuration: ");
						config = sc.nextInt();
						if (current.getAir(1).scan(other, config) != -1) {
							turnFinished = true;
						}
					} else if (shipIndex == 6) {
						System.out.print("Scan configuration: ");
						config = sc.nextInt();
						if (current.getAir(2).scan(other, config) != -1) {
							turnFinished = true;
						}
					} else {
						System.out.print("x coord: ");
						x = sc.nextInt();
						System.out.print("y coord: ");
						y = sc.nextInt();
						if (current.getSub().scan(other, x, y) != -1) {
							turnFinished = true;
						}
					}
					break;
				case 4:
					System.out.println("Choose Aircraft to Move (1 or 2)");
					shipIndex = sc.nextInt();
					System.out.print("x coord: ");
					x = sc.nextInt();
					System.out.print("y coord: ");
					y = sc.nextInt();
					if (Actions.moveAir(current, shipIndex, x, y)) {
						turnFinished = true;
					}
					break;
				case 5:
					System.out
							.println("Choose Anti-Aircraft Firing Coordinates");
					System.out.print("x coord: ");
					x = sc.nextInt();
					System.out.print("y coord: ");
					y = sc.nextInt();
					if (Actions.antiAircraft(other, x, y)) {
						turnFinished = true;
					}
					break;
				default:
					System.out.println("No quitting!");
					turnFinished = false;
					break;
				}
			}
		}
		// current.playerPrintBoard(other);
		p1.changeTurn();
		p2.changeTurn();
		System.out.println("-------------------------------------------");
	}

}
