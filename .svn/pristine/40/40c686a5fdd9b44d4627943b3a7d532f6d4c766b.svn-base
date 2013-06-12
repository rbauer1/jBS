package base;

public class SubScanForAI {
	int[][] scanArray = new int[3][3];
	int centerX;
	int centerY;
	boolean shipFound = false;
	//as long as this scan is relevant, it is true
	boolean relevant = true;
	public SubScanForAI(int x, int y, int[][] hitMap){
		centerX = x;
		centerY = y;
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				scanArray[i+1][j+1] = hitMap[y+i][x+j];
			}
		}
	}
	/**
	 * this updates this subscan's array and determines if it is still relevant
	 * @param hitUpdate (hit board)
	 * @return true if need to rescan area, false else
	 */
	public boolean update(int [][] hitUpdate){
		boolean rescan = false;
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				scanArray[i+1][j+1] = hitUpdate[centerY+i][centerX+j];
			}
		}
		if(shipFound){
			rescan = findShipExhausted();
		}
		return rescan;
	}
	/**
	 *This method determines if a ship in or passing through the subscan has been fully destroyed (within the subscan),
	 *if it has, it tells the AI to rescan at the center of the original scan in order to decide if the rest of the scan is worth shooting at
	 *@return true if needs to be rescanned false if not
	 **/
	public boolean findShipExhausted(){
		boolean rescan = true;
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(scanArray[i+1][j+1] < 0){
					rescan = false;
				}
			}
		}
		return rescan;
	}
	public void setRelevance(boolean r){
		relevant = r;
	}
	public boolean getRelevance(){
		return relevant;
	}
	
}
