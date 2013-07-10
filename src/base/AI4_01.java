package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import ships.Ships;
import ships.Ships.ShipType;
import base.Board.TileStatus;

/**
 * 
 * 7/10/13
 * 
 * @version 4.0.1
 * @author rbauer
 * 
 *         AI 4_01, Classic Probabilities Number of Wins: 549 Average Turns per win: 108 AI 3 Number
 *         of Wins: 451 Average Turns per win: 114
 * 
 *         AI 3 Number of Wins: 482 Average Turns per win: 110 AI 4_01, Classic Probabilities Number
 *         of Wins: 518 Average Turns per win: 107
 */
public class AI4_01 implements AI {
    private Player pOther;
    private Player pThis;
    private String name = "AI 4_01, Classic Probabilities";
    @SuppressWarnings("unused")
    // ?
    private TileStatus[][] board;
    // 0 for untouched, 1 for hit, -1 for miss,
    // -2 for sunken ships, -4 for subscan, -3 for deadspace
    private int[][] hits;
    /*
     * Second to last dimension [][][2][] is starting/static vs dynamic. [0] is starting, [1] is
     * dynamic. Last dimension is as follows: [0] == total probability of any ship being there [1]
     * == probability of carrier being there [2] == probability of battleship being there [3] ==
     * probability of submarine OR destroyer being there (these values are double what they would be
     * for a single ship [4] == probability of patrol boat being there
     */
    private int[][][][] dynamicProb;
    /*
     * 0 is ac, 1 is bs, 2 is des, 3 is sub, 4 is pb
     */
    private boolean[] otherShipsSunk;
    int smallestRemainingShip = 2;
    int originalHitX = 0;
    int originalHitY = 0;
    // boolean debugFirstShot = true;
    int lastHit = -1;
    boolean lastShipSunk = true;
    private SubScanForAI lastSubScan;

    /**
     * Generates this AI's hits matrix. All valid board positions are initiated as 0 (unknown). The
     * 1-14 and A-J are initiated as -1
     * 
     * @param pOther The other Player
     * @param pThis This Player
     * @throws IOException
     */
    public AI4_01(Player pOther, Player pThis) throws IOException {
        this.pOther = pOther;
        this.pThis = pThis;
        otherShipsSunk = new boolean[5];
        for (int i = 0; i < 5; i++) {
            otherShipsSunk[i] = false;
        }
        hits = new int[12][16];

        dynamicProb = new int[12][16][2][5];
        initializeProbabilities();
        for (int i = 0; i < hits.length; i++) {
            for (int j = 0; j < hits[0].length; j++) {
                if (i == 0 || j == 0 || i == 11 || j == 15) {
                    hits[i][j] = -1;
                } else
                    hits[i][j] = 0;
            }
        }
    }

    private void updateTotalProbabilities() {
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

    /**
     * Reduces the matrices for the supplied shipIndex to 0 in both the dynamic <br/>
     * [1] and static [0] probability matrices<br/>
     * 1 == aircraft carrier <br/>
     * 2 == battleship <br/>
     * 3 == submarine AND destroyer. <br/>
     * (this means the first time this method is invoked with shipindex 3, the values are halved,
     * not reduced to 0)<br/>
     * 4 == patrol boat <br/>
     * 
     * @param shipIndex
     */
    private void shipEliminatedUpdateProbabilities(int shipIndex) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 16; j++) {
                if (shipIndex == 3
                        && (!pOther.getShip(3).isThisShipSunk() || !pOther.getShip(2)
                                .isThisShipSunk())) {
                    dynamicProb[i][j][0][shipIndex] = dynamicProb[i][j][0][shipIndex] / 2;
                    dynamicProb[i][j][1][shipIndex] = dynamicProb[i][j][1][shipIndex] / 2;
                } else {
                    dynamicProb[i][j][0][shipIndex] = 0;
                    dynamicProb[i][j][1][shipIndex] = 0;
                }
            }
        }
        updateTotalProbabilities();
    }

    /**
     * Prints a representation of one either the dynamic or static probability matrix or matrices
     * 
     * @param dynOrStat true if dynamic, false if static
     * @param specificShip <br/>
     *        0 == totals<br />
     *        1 == aircraft carrier <br />
     *        2 == battleship <br/>
     *        3 == destroyer AND submarine <br/>
     *        4 == patrol boat <br/>
     *        5 == ALL
     */
    @SuppressWarnings("unused")
    private void printProbabilities(boolean dynOrStat, int specificShip) {
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
                    System.out.print(dynamicProb[i][j][dyOrSt][specificShip] + "\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * called by display for gradient map
     * 
     * @return
     */
    public int findHighestProbabilityPublic() {
        int max = 0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 16; j++) {
                if (dynamicProb[i][j][1][0] > max) {
                    max = dynamicProb[i][j][1][0];
                }
            }
        }
        return max;
    }

    /**
     * 
     * @return pos An int[] with<br/>
     *         pos[0] as the y coordinate <br/>
     *         pos[1] as the x coordinate
     */
    private int[] findHighestProbability() {
        int max = 0;
        int[] pos = new int[2];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 16; j++) {
                if (dynamicProb[i][j][1][0] > max) {
                    max = dynamicProb[i][j][1][0];
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }

    private void initializeProbabilities() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/base/AI4_Probabilities.txt"));
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
                        dynamicProb[i][j][0][k] = Integer.parseInt(tempS[j - 1]);
                        dynamicProb[i][j][1][k] = dynamicProb[i][j][0][k];
                    }
                }
            }
            br.readLine();
        }

        br.close();
    }

    private void updateProbMiss(int x, int y) {
        for (int i = 0; i < 5; i++) {
            dynamicProb[y][x][1][i] = 0;
        }
    }


    /**
     * This method combs through this AI's hits matrix and determines if it knows the position of a
     * ship that has not yet been sunk. This is represented as a positive integer 1-5 (inclusive).
     * If none are found, it will suggest a subscan that represented a possible ship (-4).
     * 
     * @return The next position that should be fired upon as a 3x1 matrix with the first two
     *         entries being the x-y coordinates and the last being either a 1 if the method is
     *         returning the location of a live ship, or 2 if it is returning the possible position
     *         of an unknown ship. Either a live ship, the possibility of a ship (subscan), or null.
     *         If null is returned, attack() will try to use its submarine to scan, if the submarine
     *         is sunk, it will fire randomly.
     */

    public int[] findHits() {
        /*
         * possibleShip refers to scans from submarines (value -4) a hit array has a 1 in position 3
         * a scan array has a 2 in position 3 a -2 is a sunken ship, a -1 is a miss, a 0 is an
         * unknown a -3 is a space that has been determined to be empty (through context)
         */
        boolean random = true;
        Random gen = new Random();
        int[] possibleShip = {-1, -1, 2};
        for (int i = 1; i < hits.length - 1; i++) {
            for (int j = 1; j < hits[0].length - 1; j++) {
                if (hits[i][j] > 0 && hits[i][j] < 6) { // check if is live ship
                    int[] hitPosition = {i, j, 1}; // return a position matrix
                                                   // for that coordinate
                    return hitPosition;
                }
                /*
                 * this conditional checks if there is an inconclusive subscan on the current
                 * coordinate. if there is, and a random boolean is true, then the position matrix
                 * "possibleShip" is updated and if no live ships are found, the last possibleShip
                 * matrix is returned when the end of the hits matrix is reached
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
            possibleShip[2] = -1;
        }
        return possibleShip;
    }

    /**
     * Works to optimize the AI by placing "Empty" tags (in the form of -3s) in the AI's hit matrix.
     * First it locates an unknown space (hits[i][j] == 0). It then uses the int
     * "smallestRemainingShip" which is the length of the the smallest remaining live ship possessed
     * by the opponent to determine if the current space could possibly contain said ship.<br />
     * <br />
     * The checks if (hits[i + k][j] < 0 && hits[i + k][j] != -4) { and the like might be able to be
     * changed to !=0 instead of < 0
     */

    private void removeDeadSpaces(int lengthOfShipExamined) {
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
                        } else {
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
                        }
                    } else if (!checkL || !checkR || !checkU || !checkD) {
                        lowerProbs(i, j, lengthOfShipExamined, countL, countR, countU, countD);
                    }
                }
            }
        }
    }


    /**
     * Called by removeDeadSpaces(int lengthOfShipExamined)<br />
     * the 4 booleans from the above method that get passed tell whether or not a ship starting in
     * this space will fit off in that direction. For each direction x the ship will not fit, the
     * probability is adjusted to 1-x*0.25 the baseline probability
     * 
     * @param i row
     * @param j column
     * @param lengthOfShipExamined
     * @param checkL can fit going left?
     * @param checkR can fit going right?
     * @param checkU can fit going up?
     * @param checkD can fit going down?
     */
    private void lowerProbs(int i, int j, int lengthOfShipExamined, int countL, int countR,
            int countU, int countD) {
        double adj = 1;
        if (countL + 1 < lengthOfShipExamined) adj -= 0.22;
        if (countR + 1 < lengthOfShipExamined) adj -= 0.22;
        if (countU + 1 < lengthOfShipExamined) adj -= 0.22;
        if (countD + 1 < lengthOfShipExamined) adj -= 0.22;
        if (countL + countR + 1 < lengthOfShipExamined) adj -= 0.06;
        if (countU + countD + 1 < lengthOfShipExamined) adj -= 0.06;
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

    private void removeDeadSpaces() {
        // This value determines the length of the smallest remaining ship
        smallestRemainingShip = pOther.getSmallestRemainingShip();
        removeDeadSpaces(smallestRemainingShip);
    }

    /**
     * Backbone of the AI <br />
     * This method initiates all other actions by the AI including:
     * <ul>
     * <li>Updating smallestRemainingShip</li>
     * <li>Getting the latest copy of the opponent's <i>Board</i></li>
     * <li>Removing dead spaces from this AI's hit matrix</li>
     * <li>Deciding whether to scan with sub (implemented) or aircraft (not yet implemented), fire
     * missiles (not yet implemented), move aircraft (not yet implemented), fire at aircraft (not
     * yet implemented), or fire a regular shot (implemented)</li>
     * <li>Determining where to fire the next shot (possibly should be moved to a separate method)</li>
     * <li>Updating the current hit matrix to account for changes made during this turn</li>
     * </ul>
     * 
     */
    public void attack() {
        board = pOther.getPlayerStatusBoard();
        for (int i = 2; i < 6; i++) {
            removeDeadSpaces(i);
        }
        Random gen = new Random();
        // This array either holds the last ship hit
        // pos[2]==1, the location of possible ship from
        // a subscan pos[2]==2, or nothing because there
        // is no useful ship location currently in the
        // hit matrix pos[2]==-1
        int[] pos = findHits();
        if (pos[2] == -1) { // No useful information regarding ships whereabouts
            // if sub dead, fire base on probabilities
            int[] probPos = findHighestProbability();
            int x = probPos[1];
            int y = probPos[0];
            // very small chance it will fire randomly
            if (gen.nextInt(40) < 1) {
                while (hits[y][x] != 0) {
                    x = gen.nextInt(14) + 1;
                    y = gen.nextInt(10) + 1;
                }
            }
            switch (Actions.attack(pOther, x, y)) {
                case 0:
                    hits[y][x] = -1;
                    break;
                case 1:
                    hits[y][x] = determineShip(x, y);
                    break;
            }
            updateProbMiss(x, y);
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
                        switch (Actions.attack(pOther, pos[1] + xMove, pos[0] + yMove)) {
                            case 0:
                                hits[pos[0] + yMove][pos[1] + xMove] = -1;
                                lastHit = -1;
                                break;
                            case 1:
                                hits[pos[0] + yMove][pos[1] + xMove] =
                                        determineShip(pos[1] + xMove, pos[0] + yMove);
                                break;
                        }
                        updateProbMiss(pos[1] + xMove, pos[0] + yMove);
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
                        switch (Actions.attack(pOther, pos[1] + xMove, pos[0] + yMove)) {
                            case 0:
                                hits[pos[0] + yMove][pos[1] + xMove] = -1;
                                if (lastHit == 0 || lastHit == 2) {
                                    lastHit++;
                                } else {
                                    lastHit--;
                                }
                                break;
                            case 1:
                                hits[pos[0] + yMove][pos[1] + xMove] =
                                        determineShip(pos[1] + xMove, pos[0] + yMove);
                                break;
                        }
                        updateProbMiss(pos[1] + xMove, pos[0] + yMove);
                        repeat = false;
                    } else {
                        lastHit = gen.nextInt(4);
                    }

                }
            }
        }
        updateHits();
        updateTotalProbabilities();
        // printProbabilities(true, 5);
    }

    /**
     * Determines if a ship has been sunk, and if so changes hits[][] to account for that (-2).
     * could use some optimization to reduce redundant searches and updates after sunk ships have
     * been accounted for. do that at some point
     */
    private void updateHits() {
        Ships[] s = pOther.getAllShips();
        for (int k = 0; k < 5; k++) {
            if (s[k].isThisShipSunk() && !otherShipsSunk[k]) {
                for (int i = 1; i < hits.length - 1; i++) {
                    for (int j = 1; j < hits[0].length - 1; j++) {
                        if (hits[i][j] == k + 1) hits[i][j] = -2;
                    }
                }
                otherShipsSunk[k] = true;
                /*
                 * this mess is to make the values correctly line up with the method below
                 */
                switch (k) {
                    case 3:
                    case 4:
                        break;
                    case 0:
                    case 1:
                    case 2:
                        k++;
                        break;
                }
                shipEliminatedUpdateProbabilities(k);
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

    public String getName() {
        return name;
    }

    public boolean hasProbabilities() {
        return true;
    }

    public int[][][][] getProbabilities() {
        return dynamicProb;
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
