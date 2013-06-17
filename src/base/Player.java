package base;

import ships.Aircraft;
import ships.Ships;
import ships.Ships.ShipType;
import ships.Submarine;
import base.Board.TileStatus;

/*
 I think  the player class should instantiate its own ships
 	ships should have access to the board
	ships should contain their missile functions, not actions. 
	actions probably should just be a few methods in board.
	
 */

public class Player {
	private Ships[] ships;
	private Board board;
	private boolean myTurn;
	private boolean id; // if player ID is true, they are player 1, else, they
						// are player 2

	public Player(Board b, boolean player1, Ships[] s) {
		board = b;
		id = player1;
		myTurn = player1;
		ships=s;
	}
	
	public boolean isAlive(){
		for(int i=0; i<ships.length; i++){
			if(!ships[i].isThisShipSunk()){
				return true;
			}
		}
		return false;
	}

	public void changeTurn() {
		myTurn = !myTurn;
	}

	public boolean getID() {
		return id;
	}
	
	public Submarine getSub(){
		return (Submarine) ships[3];
	}
	
	/** 
	 * @param i
	 * @return <b>Aircraft</b> either 1 or 2 based on <b>i</b> 
	 */
	public Aircraft getAir(int i){
		if(i <=1) i=1;
		if(i >1) i=2;
		return (Aircraft)ships[4+i];
	}
	
	public Ships[] getAllShips(){
		return ships;
	}
	
	public Ships getShip(int i){
		return ships[i];
	}

	public boolean myTurn() {
		return myTurn;
	}

	public TileStatus[][] getPlayerStatusBoard() {
		return board.returnBoard();
	}
	
	public int getSmallestRemainingShip(){
		if(!ships[4].isThisShipSunk()){
			return 2;
		}else
			if(!ships[3].isThisShipSunk() || !ships[2].isThisShipSunk()){
				return 3;
			}else
				if(!ships[1].isThisShipSunk()){
					return 4;
				}
		return 5;
		
	}
	
	public ShipType[][][] getPlayerShipBoard(){
		return board.returnShipBoard();
	}

	public Board getPlayerBoard() {
		return board;
	}

	public void playerPrintBoard(Player other) {
		Board opponentBoard = other.getPlayerBoard();
		System.out.println("Player " + (id ? 1 : 2));
		board.printBoard(board.generateMyUIBoard(board.returnBoard()));
		System.out.println("Player " + (id ? 2 : 1));
		opponentBoard.printBoard(opponentBoard
				.generateTheirUIBoard(opponentBoard.returnBoard(), getPlayerShipBoard(), getAllShips()));
	}

	public void updateBoard(int x, int y, TileStatus ts) {
		board.updateBoard(x, y, 0, ts);
	}
}
