package base;

import base.Driver.AIName;

public class TestObject {
	private int numberOfTestRuns;
	private int turnDelay;
	private AIName firstAIChosen;
	private AIName secondAIChosen;
	private boolean playerIncluded; // true if two AIs battling
	private boolean displayBoardsTurn; // show boards after each turn
	private boolean displayHitBoards; // show hit matrices from AIs after game
	private boolean displayGUI;
	/*
	 * this one might require some code changes, currently the code that
	 * controls this is in the Ships class
	 */
	private boolean displayShipsSunk;
	public TestObject(int numberOfTestRuns, int turnDelay, AIName firstAIChosen,
			AIName secondAIChosen, boolean playerIncluded, boolean displayBoardsTurn,
			boolean displayHitBoards, boolean displayShipsSunk, boolean displayGUI) {
		super();
		this.numberOfTestRuns = numberOfTestRuns;
		this.turnDelay = turnDelay;
		this.firstAIChosen = firstAIChosen;
		this.secondAIChosen = secondAIChosen;
		this.playerIncluded = playerIncluded;
		this.displayBoardsTurn = displayBoardsTurn;
		this.displayHitBoards = displayHitBoards;
		this.displayShipsSunk = displayShipsSunk;
		this.displayGUI = displayGUI;
	}
	
	/**
     * @return the displayGUI
     */
    public boolean isDisplayGUI() {
        return displayGUI;
    }
    /**
     * @param numberOfTestRuns the numberOfTestRuns to set
     */
    public void setDisplayGUI(boolean displayGUI) {
        this.displayGUI = displayGUI;
    }
    /**
     * @return the turnDelay
     */
    public int getTurnDelay() {
        return turnDelay;
    }
    /**
     * @param numberOfTestRuns the numberOfTestRuns to set
     */
    public void setTurnDelay(int turnDelay) {
        this.turnDelay = turnDelay;
    }
	/**
	 * @return the numberOfTestRuns
	 */
	public int getNumberOfTestRuns() {
		return numberOfTestRuns;
	}
	/**
	 * @param numberOfTestRuns the numberOfTestRuns to set
	 */
	public void setNumberOfTestRuns(int numberOfTestRuns) {
		this.numberOfTestRuns = numberOfTestRuns;
	}
	/**
	 * @return the firstAIChosen
	 */
	public AIName getFirstAIChosen() {
		return firstAIChosen;
	}
	/**
	 * @param firstAIChosen the firstAIChosen to set
	 */
	public void setFirstAIChosen(AIName firstAIChosen) {
		this.firstAIChosen = firstAIChosen;
	}
	/**
	 * @return the secondAIChosen
	 */
	public AIName getSecondAIChosen() {
		return secondAIChosen;
	}
	/**
	 * @param secondAIChosen the secondAIChosen to set
	 */
	public void setSecondAIChosen(AIName secondAIChosen) {
		this.secondAIChosen = secondAIChosen;
	}
	/**
	 * @return the playerIncluded
	 */
	public boolean isPlayerIncluded() {
		return playerIncluded;
	}
	/**
	 * @param playerIncluded the playerIncluded to set
	 */
	public void setPlayerIncluded(boolean playerIncluded) {
		this.playerIncluded = playerIncluded;
	}
	/**
	 * @return the displayBoardsTurn
	 */
	public boolean isDisplayBoardsTurn() {
		return displayBoardsTurn;
	}
	/**
	 * @param displayBoardsTurn the displayBoardsTurn to set
	 */
	public void setDisplayBoardsTurn(boolean displayBoardsTurn) {
		this.displayBoardsTurn = displayBoardsTurn;
	}
	/**
	 * @return the displayHitBoards
	 */
	public boolean isDisplayHitBoards() {
		return displayHitBoards;
	}
	/**
	 * @param displayHitBoards the displayHitBoards to set
	 */
	public void setDisplayHitBoards(boolean displayHitBoards) {
		this.displayHitBoards = displayHitBoards;
	}
	/**
	 * @return the displayShipsSunk
	 */
	public boolean isDisplayShipsSunk() {
		return displayShipsSunk;
	}
	/**
	 * @param displayShipsSunk the displayShipsSunk to set
	 */
	public void setDisplayShipsSunk(boolean displayShipsSunk) {
		this.displayShipsSunk = displayShipsSunk;
	}

	
}
