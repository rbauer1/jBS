package base;

public class SubScanForAI {
	private int[][] scanArray = new int[3][3];
	private int centerX;
	private int centerY;
	private boolean shipFound = false;
	// as long as this scan is relevant, it is true
	private boolean relevant = true;

	public SubScanForAI(int x, int y, int[][] hitMap) {
		centerX = x;
		centerY = y;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				scanArray[i + 1][j + 1] = hitMap[y + i][x + j];
			}
		}
	}

	/**
	 * this updates this subscan's array and determines if it is still relevant
	 * 
	 * @param hitUpdate
	 *            (hit board)
	 * @return true if need to rescan area, false else
	 */
	public boolean update(int[][] hitUpdate) {
		boolean rescan = false;
		int temp = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				temp = hitUpdate[centerY + i][centerX + j];
				scanArray[i + 1][j + 1] = temp;
				if (temp > 0 || temp == -4) {
					shipFound = true;
				}
			}
		}
		if (shipFound) {
			rescan = findShipExhausted();
		}
		return rescan;
	}

	/**
	 * This method determines if a ship in or passing through the subscan has
	 * been fully destroyed (within the subscan), if it has, it tells the AI to
	 * rescan at the center of the original scan in order to decide if the rest
	 * of the scan is worth shooting at
	 * 
	 * @return true if needs to be rescanned false if not
	 **/
	public boolean findShipExhausted() {
		boolean rescan = false;
		outer: for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (scanArray[i + 1][j + 1] == -2) { // no sunkenships in this
													 // area
					rescan = true;
					break outer;
				}
			}
		}
		if(rescan){
			setRelevance(false);
		}
		return rescan;
	}

	public void setRelevance(boolean r) {
		relevant = r;
	}

	public boolean getRelevance() {
		return relevant;
	}

	/**
	 * 
	 * @return An int array with first position as the center x coordinate of
	 *         the 3x3 and the second position as the center y coordinate
	 */
	public int[] getCenterCoords() {
		int[] coords = { centerX, centerY };
		return coords;
	}

}
