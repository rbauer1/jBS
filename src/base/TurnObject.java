package base;

/**
 * Stores all data relevant to a player's turn. Primarily used in the Display class, it should also be used by the AIs 
 * @author Riley
 *
 */
public class TurnObject {
	private int xTile;
	private int yTile;
	private int aircraftNumber;
	private MissileType missileType;
	private ScanType scanType;
	private Action action;
	private Player playerSendingMove;
	
	public TurnObject(Player p){
		xTile = -1;
		yTile = -1;
		aircraftNumber = -1;
		missileType = null;
		scanType = null;
		action = null;
		playerSendingMove = p;
	}
		
	@Override
	public String toString() {
		return "TurnObject [xTile=" + xTile + ", yTile=" + yTile
				+ ", aircraftNumber=" + aircraftNumber + ", missileType="
				+ missileType + ", scanType=" + scanType + ", action=" + action
				+ ", playerSendingMove=" + playerSendingMove + "]";
	}

	/**
	 * @return the playerSendingMove
	 */
	public Player getPlayerSendingMove() {
		return playerSendingMove;
	}

	/**
	 * @param playerSendingMove the playerSendingMove to set
	 */
	public void setPlayerSendingMove(Player playerSendingMove) {
		this.playerSendingMove = playerSendingMove;
	}

	/**
	 * @return the xTile
	 */
	public int getxTile() {
		return xTile;
	}

	/**
	 * @param xTile the xTile to set
	 */
	public void setxTile(int xTile) {
		this.xTile = xTile;
	}

	/**
	 * @return the yTile
	 */
	public int getyTile() {
		return yTile;
	}

	/**
	 * @param yTile the yTile to set
	 */
	public void setyTile(int yTile) {
		this.yTile = yTile;
	}

	/**
	 * @return the aircraftNumber
	 */
	public int getAircraftNumber() {
		return aircraftNumber;
	}

	/**
	 * @param aircraftNumber the aircraftNumber to set
	 */
	public void setAircraftNumber(int aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}

	/**
	 * @return the missileType
	 */
	public MissileType getMissileType() {
		return missileType;
	}

	/**
	 * @param missileType the missileType to set
	 */
	public void setMissileType(MissileType missileType) {
		this.missileType = missileType;
	}

	/**
	 * @return the scanType
	 */
	public ScanType getScanType() {
		return scanType;
	}

	/**
	 * @param scanType the scanType to set
	 */
	public void setScanType(ScanType scanType) {
		this.scanType = scanType;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}



	protected enum MissileType{
		CARRIER_X, CARRIER_CROSS, BATTLESHIP, DESTROYER_V, DESTROYER_H, SUB_V, SUB_H;
	}
	
	protected enum ScanType{
		AIRCRAFT_X, AIRCRAFT_CROSS, SUB
	}

	protected enum Action{
		FIRE_REGULAR, FIRE_MISSILE, SCAN, MOVE_AIRCRAFT, FIRE_ANTIAIR
	}
}
