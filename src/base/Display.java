package base;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ships.Aircraft;
import ships.AircraftCarrier;
import ships.Battleship;
import ships.Destroyer;
import ships.PatrolBoat;
import ships.Ships;
import ships.Ships.ShipType;
import ships.Submarine;
import base.Board.TileStatus;

public class Display {
    private Player p1;
    private Player p2;
    private AI ai = null;
    private JFrame frame;
    private Container container;
    private JPanel[][] yourBoard;
    private JPanel[][] opponentBoard;
    private JPanel[][] probabilitiesBoard;
    private JLabel[] coordLabels1;
    private JLabel[] coordLabels2;
    private JLabel[] coordLabelsProbDisp;
    private int[][][][] aiProbabilities;
    private String[] images = {"images/acc1.png", "images/acc2.png", "images/bs.png",
            "images/dest1.png", "images/dest2.png", "images/sub1.png", "images/sub2.png",
            "images/subs.png", "images/rec1.png", "images/rec2.png", "images/rec1.png",
            "images/rec2.png", "images/moveR1.png", "images/moveR2.png"};
    private String[] pressedImages = {"images/acc1p.png", "images/acc2p.png", "images/bsp.png",
            "images/dest1p.png", "images/dest2p.png", "images/sub1p.png", "images/sub2p.png",
            "images/subsp.png", "images/rec1p.png", "images/rec2p.png", "images/rec1p.png",
            "images/rec2p.png", "images/moveR1p.png", "images/moveR2p.png"};
    private final int FRAME_HEIGHT = 800;
    private final int FRAME_WIDTH = 1000;
    private final int SQ_SIZE = 30;
    private final int BOARD_SEPARATION_VERTICAL = 400;
    private final int BOARD_SEPARATION_HORIZONTAL = 500;
    private final Color CARRIER_COLOR = new Color(0, 255, 0);
    private final Color AIRCRAFT1_COLOR = new Color(200, 0, 255);
    private final Color AIRCRAFT2_COLOR = new Color(100, 0, 255);
    private final Color BATTLESHIP_COLOR = new Color(0, 255, 50);
    private final Color DESTROYER_COLOR = new Color(0, 255, 100);
    private final Color SUBMARINE_COLOR = new Color(0, 255, 150);
    private final Color PATROLBOAT_COLOR = new Color(0, 255, 200);

    private int opX; // these are used for the mouse listener
    private int opY;
    private int yoX;
    private int yoY;
    private int lastClickedOpponentX = 0;
    private int lastClickedOpponentY = 0;
    private int lastClickedYourX = 0;
    private int lastClickedYourY = 0;
    private int[][] playerShips = null;
    private boolean shipsNotPlaced = true;
    private boolean horizontalOrientation = true;
    private boolean validShipLocation = false;
    private CurrentShip currentShip = CurrentShip.CARRIER;
    public Player p = null;

    public void setPlayersAndAI(Player p2, Player p1, AI ai) {
        this.ai = ai;
        this.p1 = p1;
        this.p2 = p2;
        initializeProbabilitiesBoard();
    }

    public Display(boolean playerIncluded) {
        frame = new JFrame();
        frame.setBounds(new Rectangle(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.RED); // for debugging porpoises
        frame.setLayout(null);
        frame.setLocation(100, 50);

        container = frame.getContentPane();
        container.setBackground(Color.BLACK);
        container.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    horizontalOrientation = !horizontalOrientation;
                }
            }
        });


        yourBoard = new JPanel[15][11];
        opponentBoard = new JPanel[15][11];
        probabilitiesBoard = new JPanel[15][11];

        coordLabels1 = new JLabel[24];
        coordLabels2 = new JLabel[24];
        coordLabelsProbDisp = new JLabel[24];

        if (playerIncluded) {
            playerShips = new int[7][3];
        }else{
            shipsNotPlaced = false;
        }

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                opponentBoard[i][j] = new JPanel();
                container.add(opponentBoard[i][j]);
                opponentBoard[i][j].setLocation(i + i * SQ_SIZE, j + j * SQ_SIZE);
                opponentBoard[i][j].setSize(new Dimension(SQ_SIZE, SQ_SIZE));
                if ((i == 0) ^ (j == 0)) {
                    opponentBoard[i][j].setBackground(Color.GRAY);
                    if (i == 0) {
                        coordLabels1[j - 1] = new JLabel((char) (j + 64) + "");
                        opponentBoard[i][j].add(coordLabels1[j - 1]);
                    }
                    if (j == 0) {
                        coordLabels1[i + 9] = new JLabel(i + "");
                        opponentBoard[i][j].add(coordLabels1[i + 9]);
                    }
                } else {
                    opponentBoard[i][j].setBackground(Color.DARK_GRAY);
                }
                if (i > 0 && j > 0) {
                    opX = i;
                    opY = j;
                    opponentBoard[i][j].addMouseListener(new MouseListener() {
                        int xx = opX;
                        int yy = opY;

                        @Override
                        public void mouseClicked(MouseEvent arg0) {
                            opponentBoard[xx][yy].setBackground(Color.YELLOW);
                            if ((lastClickedOpponentX != 0 && lastClickedOpponentY != 0)
                                    ^ (lastClickedOpponentX == xx && lastClickedOpponentY == yy)) {
                                opponentBoard[lastClickedOpponentX][lastClickedOpponentY]
                                        .setBackground(updateOpponentBoardHelper(
                                                lastClickedOpponentX, lastClickedOpponentY));
                            }
                            lastClickedOpponentX = xx;
                            lastClickedOpponentY = yy;
                        }

                        @Override
                        public void mouseEntered(MouseEvent arg0) {
                            if (!opponentBoard[xx][yy].getBackground().equals(Color.YELLOW))
                                opponentBoard[xx][yy].setBackground(updateOpponentBoardHelper(xx,
                                        yy).darker());
                        }

                        @Override
                        public void mouseExited(MouseEvent arg0) {
                            if (!opponentBoard[xx][yy].getBackground().equals(Color.YELLOW))
                                opponentBoard[xx][yy].setBackground(updateOpponentBoardHelper(xx,
                                        yy));
                        }

                        @Override
                        public void mousePressed(MouseEvent arg0) {
                            opponentBoard[xx][yy].setBackground(new Color(175, 175, 0));
                        }

                        @Override
                        public void mouseReleased(MouseEvent arg0) {
                            opponentBoard[xx][yy].setBackground(updateOpponentBoardHelper(xx, yy));
                        }

                    });
                }
                opponentBoard[i][j].setVisible(true);
            }
        }
        opponentBoard[0][0].setBackground(Color.BLACK);


        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                yourBoard[i][j] = new JPanel();
                yourBoard[i][j].setLocation(i + i * SQ_SIZE, j + j * SQ_SIZE
                        + BOARD_SEPARATION_VERTICAL);
                yourBoard[i][j].setSize(new Dimension(SQ_SIZE, SQ_SIZE));
                if ((i == 0) ^ (j == 0)) {
                    yourBoard[i][j].setBackground(Color.GRAY);
                    if (i == 0) {
                        coordLabels2[j - 1] = new JLabel((char) (j + 64) + "");
                        yourBoard[i][j].add(coordLabels2[j - 1]);
                    }
                    if (j == 0) {
                        coordLabels2[i + 9] = new JLabel(i + "");
                        yourBoard[i][j].add(coordLabels2[i + 9]);
                    }
                } else {
                    yourBoard[i][j].setBackground(Color.DARK_GRAY);
                }
                if (i > 0 && j > 0) {
                    yoX = i;
                    yoY = j;
                    yourBoard[i][j].addMouseListener(new MouseListener() {
                        int xx = yoX;
                        int yy = yoY;

                        @Override
                        public void mouseClicked(MouseEvent arg0) {
                            if (shipsNotPlaced) {
                                int length = 0;
                                Color color = Color.BLACK;
                                switch (currentShip) {
                                    case CARRIER:
                                        length = 5;
                                        color = CARRIER_COLOR;
                                        break;
                                    case AIRCRAFT1:
                                        length = 1;
                                        color = AIRCRAFT1_COLOR;
                                        break;
                                    case AIRCRAFT2:
                                        length = 1;
                                        color = AIRCRAFT2_COLOR;
                                        break;
                                    case BATTLESHIP:
                                        length = 4;
                                        color = BATTLESHIP_COLOR;
                                        break;
                                    case DESTROYER:
                                        length = 3;
                                        color = DESTROYER_COLOR;
                                        break;
                                    case SUBMARINE:
                                        length = 3;
                                        color = SUBMARINE_COLOR;
                                        break;
                                    case PATROLBOAT:
                                        length = 2;
                                        color = PATROLBOAT_COLOR;
                                        break;
                                }
                                int j = 0;
                                int k = 0;
                                if (horizontalOrientation) {
                                    j++;
                                } else {
                                    k++;
                                }
                                if (length == 1) {
                                    yourBoard[xx][yy].setBackground(color);
                                    playerShips[thisShip(currentShip)][0] = yy;
                                    playerShips[thisShip(currentShip)][1] = xx;
                                    playerShips[thisShip(currentShip)][2] = 0;
                                } else {
                                    for (int i = -length / 2; i < length - (length / 2); i++) {
                                        if (xx + (i * j) < 15 && xx + (i * j) > 0
                                                && yy + (i * k) < 11 && yy + (i * k) > 0)
                                            yourBoard[xx + (i * j)][yy + (i * k)]
                                                    .setBackground(color);
                                    }
                                    playerShips[thisShip(currentShip)][0] =
                                            yy + ((-length / 2) * k);
                                    playerShips[thisShip(currentShip)][1] =
                                            xx + ((-length / 2) * j);
                                    playerShips[thisShip(currentShip)][2] = k; // 0 if horizontal, 1
                                                                               // if vertical
                                }
                                nextShip(currentShip);
                            } else {
                                yourBoard[xx][yy].setBackground(Color.YELLOW);
                                if ((lastClickedYourX != 0 && lastClickedYourY != 0)
                                        ^ (lastClickedYourX == xx && lastClickedYourY == yy)) {
                                    yourBoard[lastClickedYourX][lastClickedYourY]
                                            .setBackground(updateYourBoardHelper(lastClickedYourX,
                                                    lastClickedYourY));
                                }
                                lastClickedYourX = xx;
                                lastClickedYourY = yy;
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent arg0) {
                            if (shipsNotPlaced) {
                                int length = 0;
                                Color color = Color.BLACK;
                                switch (currentShip) {
                                    case CARRIER:
                                        length = 5;
                                        color = CARRIER_COLOR.darker();
                                        break;
                                    case AIRCRAFT1:
                                        length = 1;
                                        color = AIRCRAFT1_COLOR.darker();
                                        break;
                                    case AIRCRAFT2:
                                        length = 1;
                                        color = AIRCRAFT2_COLOR.darker();
                                        break;
                                    case BATTLESHIP:
                                        length = 4;
                                        color = BATTLESHIP_COLOR.darker();
                                        break;
                                    case DESTROYER:
                                        length = 3;
                                        color = DESTROYER_COLOR.darker();
                                        break;
                                    case SUBMARINE:
                                        length = 3;
                                        color = SUBMARINE_COLOR.darker();
                                        break;
                                    case PATROLBOAT:
                                        length = 2;
                                        color = PATROLBOAT_COLOR.darker();
                                        break;
                                }
                                if (length == 1 &&
                                        yourBoard[xx][yy].getBackground()!=CARRIER_COLOR &&
                                        yourBoard[xx][yy].getBackground()!=AIRCRAFT1_COLOR &&
                                        yourBoard[xx][yy].getBackground()!=AIRCRAFT2_COLOR &&
                                        yourBoard[xx][yy].getBackground()!=BATTLESHIP_COLOR &&
                                        yourBoard[xx][yy].getBackground()!=DESTROYER_COLOR &&
                                        yourBoard[xx][yy].getBackground()!=SUBMARINE_COLOR &&
                                        yourBoard[xx][yy].getBackground()!=PATROLBOAT_COLOR) {
                                    yourBoard[xx][yy].setBackground(color);
                                } else {
                                    int j = 0;
                                    int k = 0;
                                    if (horizontalOrientation) {
                                        j++;
                                    } else {
                                        k++;
                                    }
                                    for (int i = -length / 2; i < length - (length / 2); i++) {
                                        if (xx + (i * j) < 15 && xx + (i * j) > 0
                                                && yy + (i * k) < 11 && yy + (i * k) > 0){
                                            if(yourBoard[xx + (i * j)][yy + (i * k)].getBackground()!=CARRIER_COLOR &&
                                                    yourBoard[xx + (i * j)][yy + (i * k)].getBackground()!=AIRCRAFT1_COLOR &&
                                                    yourBoard[xx + (i * j)][yy + (i * k)].getBackground()!=AIRCRAFT2_COLOR &&
                                                    yourBoard[xx + (i * j)][yy + (i * k)].getBackground()!=BATTLESHIP_COLOR &&
                                                    yourBoard[xx + (i * j)][yy + (i * k)].getBackground()!=DESTROYER_COLOR &&
                                                    yourBoard[xx + (i * j)][yy + (i * k)].getBackground()!=SUBMARINE_COLOR &&
                                                    yourBoard[xx + (i * j)][yy + (i * k)].getBackground()!=PATROLBOAT_COLOR)
                                                yourBoard[xx + (i * j)][yy + (i * k)]
                                                        .setBackground(color);
                                        }
                                    }
                                }
                            } else if (!yourBoard[xx][yy].getBackground().equals(Color.YELLOW)) {
                                yourBoard[xx][yy].setBackground(updateYourBoardHelper(xx, yy)
                                        .darker());
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent arg0) {
                            if (shipsNotPlaced) {
                                for (int i = -2; i < 3; i++) {
                                    for (int j = -2; j < 3; j++) {
                                        if (xx + i < 15 && xx + i > 0 && yy + j < 11 && yy + j > 0) {
                                            if(yourBoard[xx + i][yy + j].getBackground()!=CARRIER_COLOR &&
                                               yourBoard[xx + i][yy + j].getBackground()!=AIRCRAFT1_COLOR &&
                                               yourBoard[xx + i][yy + j].getBackground()!=AIRCRAFT2_COLOR &&
                                               yourBoard[xx + i][yy + j].getBackground()!=BATTLESHIP_COLOR &&
                                               yourBoard[xx + i][yy + j].getBackground()!=DESTROYER_COLOR &&
                                               yourBoard[xx + i][yy + j].getBackground()!=SUBMARINE_COLOR &&
                                               yourBoard[xx + i][yy + j].getBackground()!=PATROLBOAT_COLOR){
//                                            System.out.println((xx + i) + " " + (yy + j));
                                            yourBoard[xx + i][yy + j]
                                                    .setBackground(updateYourBoardHelper(xx + i, yy
                                                            + j));
                                            }
                                        }
                                    }
                                }
                            }else if (!yourBoard[xx][yy].getBackground().equals(Color.YELLOW)) {
                                yourBoard[xx][yy].setBackground(updateYourBoardHelper(xx, yy));
                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent arg0) {
//                          if(yourBoard[xx][yy].getBackground()!=CARRIER_COLOR &&
//                          yourBoard[xx][yy].getBackground()!=AIRCRAFT1_COLOR &&
//                          yourBoard[xx][yy].getBackground()!=AIRCRAFT2_COLOR &&
//                          yourBoard[xx][yy].getBackground()!=BATTLESHIP_COLOR &&
//                          yourBoard[xx][yy].getBackground()!=DESTROYER_COLOR &&
//                          yourBoard[xx][yy].getBackground()!=SUBMARINE_COLOR &&
//                          yourBoard[xx][yy].getBackground()!=PATROLBOAT_COLOR)
                            yourBoard[xx][yy].setBackground(new Color(175, 175, 0));
                        }

                        @Override
                        public void mouseReleased(MouseEvent arg0) {
//                            if(yourBoard[xx][yy].getBackground()!=CARRIER_COLOR &&
//                                        yourBoard[xx][yy].getBackground()!=AIRCRAFT1_COLOR &&
//                                        yourBoard[xx][yy].getBackground()!=AIRCRAFT2_COLOR &&
//                                        yourBoard[xx][yy].getBackground()!=BATTLESHIP_COLOR &&
//                                        yourBoard[xx][yy].getBackground()!=DESTROYER_COLOR &&
//                                        yourBoard[xx][yy].getBackground()!=SUBMARINE_COLOR &&
//                                        yourBoard[xx][yy].getBackground()!=PATROLBOAT_COLOR)
                            yourBoard[xx][yy].setBackground(updateYourBoardHelper(xx, yy));
                        }

                    });
                }
                yourBoard[i][j].setVisible(true);
                container.add(yourBoard[i][j]);
            }
        }
        yourBoard[0][0].setBackground(Color.BLACK);

        frame.setVisible(true);
        container.setVisible(true);
    }

    private void initializeProbabilitiesBoard() {
        if (ai.hasProbabilities()) {
            aiProbabilities = ai.getProbabilities();
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 11; j++) {
                    probabilitiesBoard[i][j] = new JPanel();
                    container.add(probabilitiesBoard[i][j]);
                    probabilitiesBoard[i][j].setLocation(i + i * SQ_SIZE
                            + BOARD_SEPARATION_HORIZONTAL, j + j * SQ_SIZE
                            + BOARD_SEPARATION_VERTICAL);
                    probabilitiesBoard[i][j].setSize(new Dimension(SQ_SIZE, SQ_SIZE));
                    if ((i == 0) ^ (j == 0)) {
                        probabilitiesBoard[i][j].setBackground(Color.GRAY);
                        if (i == 0) {
                            coordLabelsProbDisp[j - 1] = new JLabel((char) (j + 64) + "");
                            probabilitiesBoard[i][j].add(coordLabelsProbDisp[j - 1]);
                        }
                        if (j == 0) {
                            coordLabelsProbDisp[i + 9] = new JLabel(i + "");
                            probabilitiesBoard[i][j].add(coordLabelsProbDisp[i + 9]);
                        }
                    } else {
                        probabilitiesBoard[i][j].setBackground(Color.DARK_GRAY);
                    }
                    probabilitiesBoard[i][j].setVisible(true);
                }
            }
            probabilitiesBoard[0][0].setBackground(Color.BLACK);
        }
    }

    /**
     * 
     * @return true if yes
     */
    public boolean arePlayerShipsInitialized() {
        return !shipsNotPlaced;
    }

    public Player getPlayer() {
        return p;
    }

    public Display(Player p2, Player p1) {
        this.p1 = p1;
        this.p2 = p2;

        frame = new JFrame();
        frame.setBounds(new Rectangle(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.RED); // for debugging porpoises
        frame.setLayout(null);

        container = frame.getContentPane();
        container.setBackground(Color.BLACK);

        yourBoard = new JPanel[15][11];
        opponentBoard = new JPanel[15][11];

        coordLabels1 = new JLabel[24];
        coordLabels2 = new JLabel[24];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                opponentBoard[i][j] = new JPanel();
                container.add(opponentBoard[i][j]);
                opponentBoard[i][j].setLocation(i + i * SQ_SIZE, j + j * SQ_SIZE);
                opponentBoard[i][j].setSize(new Dimension(SQ_SIZE, SQ_SIZE));
                if ((i == 0) ^ (j == 0)) {
                    opponentBoard[i][j].setBackground(Color.GRAY);
                    if (i == 0) {
                        coordLabels1[j - 1] = new JLabel((char) (j + 64) + "");
                        opponentBoard[i][j].add(coordLabels1[j - 1]);
                    }
                    if (j == 0) {
                        coordLabels1[i + 9] = new JLabel(i + "");
                        opponentBoard[i][j].add(coordLabels1[i + 9]);
                    }
                } else {
                    opponentBoard[i][j].setBackground(Color.DARK_GRAY);
                }
                opponentBoard[i][j].setVisible(true);
            }
        }
        opponentBoard[0][0].setBackground(Color.BLACK);

        frame.setVisible(true);
        container.setVisible(true);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                yourBoard[i][j] = new JPanel();
                container.add(yourBoard[i][j]);
                yourBoard[i][j].setLocation(i + i * SQ_SIZE, j + j * SQ_SIZE
                        + BOARD_SEPARATION_VERTICAL);
                yourBoard[i][j].setSize(new Dimension(SQ_SIZE, SQ_SIZE));
                if ((i == 0) ^ (j == 0)) {
                    yourBoard[i][j].setBackground(Color.GRAY);
                    if (i == 0) {
                        coordLabels2[j - 1] = new JLabel((char) (j + 64) + "");
                        yourBoard[i][j].add(coordLabels2[j - 1]);
                    }
                    if (j == 0) {
                        coordLabels2[i + 9] = new JLabel(i + "");
                        yourBoard[i][j].add(coordLabels2[i + 9]);
                    }
                } else {
                    yourBoard[i][j].setBackground(Color.DARK_GRAY);
                }
                yourBoard[i][j].setVisible(true);
            }
        }
        yourBoard[0][0].setBackground(Color.BLACK);

    }

    private void initializePlayerShips(int[][] ships) {
        for(int i=0; i<7; i++){
            for(int j=0; j<3; j++){
                System.out.print(ships[i][j]+ " ");
            }
            System.out.println();
        }
        AircraftCarrier ac = new AircraftCarrier(ships[0][0], ships[0][1], ships[0][2]);
        Battleship bs = new Battleship(ships[3][0], ships[3][1], ships[3][2]);
        Destroyer de = new Destroyer(ships[4][0], ships[4][1], ships[4][2]);
        Submarine sub = new Submarine(ships[5][0], ships[5][1], ships[5][2]);
        PatrolBoat pb = new PatrolBoat(ships[6][0], ships[6][1], ships[6][2]);
        Aircraft a1 = new Aircraft(ships[1][0], ships[1][1], ships[1][2], 1);
        Aircraft a2 = new Aircraft(ships[2][0], ships[2][1], ships[2][2], 2);
        Ships[] s = {ac, bs, de, sub, pb, a1, a2};
        Board b = new Board(s);
        p = new Player(b, false, s);
    }

    public void dispose() {
        frame.dispose();
    }

    public void updateBoards() {
        if(!shipsNotPlaced){
        updateYourBoard();
        updateOpponentBoard();
        if (ai.hasProbabilities()) {
            updateProbabilityBoard();
        }
        container.validate();
        }
    }

    private Color getGradientMapColor(int i, int j) {
        float probability = 0;
        float probMax = ai.findHighestProbabilityPublic();
        probability = aiProbabilities[j][i][1][0] / probMax;
        if (probability == 1) {
            return Color.MAGENTA;
        } else if (probability != 0 && !Float.isNaN(probability)) {
            return new Color(probability, (probability) / (2 - probability), (1 - probability) / 2);
        }
        return Color.DARK_GRAY;
    }

    private void updateProbabilityBoard() {
        TileStatus[][] b = p2.getPlayerStatusBoard();
        Color c = new Color(0);
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 11; j++) {
                switch (b[j][i]) {
                    case HIT:
                        c = Color.RED;
                        break;
                    case MISS:
                        c = Color.WHITE;
                        break;
                    case SUBSCANEMPTY:
                        c = Color.CYAN;
                        break;
                    default:
                        c = getGradientMapColor(i, j);
                        break;
                }
                probabilitiesBoard[i][j].setBackground(c);
            }
        }
    }

    // private void updateProbabilityBoard() {
    // TileStatus[][] b = p2.getPlayerStatusBoard();
    // Color c = new Color(0);
    // for (int i = 1; i < 15; i++) {
    // for (int j = 1; j < 11; j++) {
    // switch (b[j][i]) {
    // case HIT:
    // c = Color.RED;
    // break;
    // case AIRCRAFTSCANEMPTY:
    // case MISS:
    // c = Color.WHITE;
    // break;
    // case SUBSCANEMPTY:
    // c = Color.CYAN;
    // break;
    // case SUBSCANSHIP:
    // c = Color.CYAN;
    // break;
    // case AIRCRAFTSCAN:
    // c = Color.BLUE;
    // break;
    // default:
    // c = getGradientMapColor(i, j);
    // break;
    // }
    // if (p1.getPlayerShipBoard()[j][i][1] == ShipType.AIRCRAFT1) {
    // if (((Aircraft) p1.getAllShips()[5]).launched()
    // && !((Aircraft) p1.getAllShips()[5]).isThisShipSunk()) {
    // c = new Color(255, 0, 255);
    // }
    // } else if (p1.getPlayerShipBoard()[j][i][1] == ShipType.AIRCRAFT2) {
    // if (((Aircraft) p1.getAllShips()[6]).launched()
    // && !((Aircraft) p1.getAllShips()[6]).isThisShipSunk()) {
    // c = new Color(100, 0, 255);
    // }
    // }
    // probabilitiesBoard[i][j].setBackground(c);
    // }
    // }
    // }

    private void updateOpponentBoard() {

        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 11; j++) {
                opponentBoard[i][j].setBackground(updateOpponentBoardHelper(i, j));
            }
        }
    }

    private Color updateOpponentBoardHelper(int i, int j) {
        TileStatus[][] b = p1.getPlayerStatusBoard();
        Color c = new Color(0);
        switch (b[j][i]) {
            case HIT:
                c = Color.RED;
                break;
            case AIRCRAFTSCANEMPTY:
            case MISS:
                c = Color.WHITE;
                break;
            case SUBSCANEMPTY:
                c = Color.CYAN;
                break;
            case SUBSCANSHIP:
                c = Color.CYAN;
                break;
            case AIRCRAFTSCAN:
                c = Color.BLUE;
                break;
            default:
                c = Color.DARK_GRAY;
                break;
        }
        if (p2.getPlayerShipBoard()[j][i][1] == ShipType.AIRCRAFT1) {
            if (((Aircraft) p2.getAllShips()[5]).launched()
                    && !((Aircraft) p2.getAllShips()[5]).isThisShipSunk()) {
                c = AIRCRAFT1_COLOR;
            }
        } else if (p2.getPlayerShipBoard()[j][i][1] == ShipType.AIRCRAFT2) {
            if (((Aircraft) p2.getAllShips()[6]).launched()
                    && !((Aircraft) p2.getAllShips()[6]).isThisShipSunk()) {
                c = AIRCRAFT2_COLOR;
            }
        }
        return c;
    }

    private void updateYourBoard() {
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 11; j++) {
                yourBoard[i][j].setBackground(updateYourBoardHelper(i, j));
            }
        }
        frame.repaint();
    }

    private Color updateYourBoardHelper(int i, int j) {
        if (i == 0 && j == 0) {
            return Color.BLACK;
        }
        if (i == 0 || j == 0) {
            return Color.GRAY;
        }
        if (arePlayerShipsInitialized()) {
            try{
            TileStatus[][] b = p2.getPlayerStatusBoard();
            Color c = new Color(0);
            switch (b[j][i]) {
                case SHIP:
                case SUBSCANSHIP:
                case AIRCRAFTSCAN:
                    switch (p2.getPlayerShipBoard()[j][i][0]) {
                        case AIRCRAFTCARRIER:
                            c = CARRIER_COLOR;
                            break;
                        case BATTLESHIP:
                            c = BATTLESHIP_COLOR;
                            break;
                        case DESTROYER:
                            c = DESTROYER_COLOR;
                            break;
                        case SUBMARINE:
                            c = SUBMARINE_COLOR;
                            break;
                        case PATROLBOAT:
                            c = PATROLBOAT_COLOR;
                            break;
                        default:
                            break;
                    }
                    switch (p2.getPlayerShipBoard()[j][i][1]) {
                        case AIRCRAFT1:
                            if (!(((Aircraft) p2.getShip(5)).launched())) {
                                c = AIRCRAFT1_COLOR;
                            }
                            break;
                        case AIRCRAFT2:
                            if (!(((Aircraft) p2.getShip(6)).launched())) {
                                c = AIRCRAFT2_COLOR;
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case HIT:
                    c = Color.RED;
                    break;
                case MISS:
                    c = Color.WHITE;
                    break;
                case SUBSCANEMPTY:
                    c = Color.CYAN;
                    break;
                default:
                    c = Color.DARK_GRAY;
                    break;
            }
            return c;
            }catch(java.lang.NullPointerException n){
                System.err.println("Null pointer where p2 should be");
                for(int ii=0; ii<7; ii++){
                    for(int jj=0; jj<3; jj++){
                        System.out.print(playerShips[ii][jj]+ " ");
                    }
                    System.out.println();
                }
                System.exit(1);
                return Color.DARK_GRAY;
            }
        } else
            return Color.DARK_GRAY;
    }

    /**
     * 
     * @param c CurrentShip
     * @return int representation of the ship (0-6) (carrier is 0 aircraft are 1 and 2 etc)
     */
    private int thisShip(CurrentShip c) {
        int num = 0;
        switch (c) {
            case CARRIER:
                num = 0;
                break;
            case AIRCRAFT1:
                num = 1;
                break;
            case AIRCRAFT2:
                num = 2;
                break;
            case BATTLESHIP:
                num = 3;
                break;
            case DESTROYER:
                num = 4;
                break;
            case SUBMARINE:
                num = 5;
                break;
            case PATROLBOAT:
                num = 6;
                break;
        }
        return num;
    }

    /**
     * moves currentShip to the next one in the enum
     */
    private void nextShip(CurrentShip c) {
        switch (c) {
            case CARRIER:
                currentShip = CurrentShip.AIRCRAFT1;
                break;
            case AIRCRAFT1:
                currentShip = CurrentShip.AIRCRAFT2;
                break;
            case AIRCRAFT2:
                currentShip = CurrentShip.BATTLESHIP;
                break;
            case BATTLESHIP:
                currentShip = CurrentShip.DESTROYER;
                break;
            case DESTROYER:
                currentShip = CurrentShip.SUBMARINE;
                break;
            case SUBMARINE:
                currentShip = CurrentShip.PATROLBOAT;
                break;
            case PATROLBOAT:
                shipsNotPlaced = false;
                initializePlayerShips(playerShips);
                break;
        }
    }

    private enum CurrentShip {
        CARRIER, AIRCRAFT1, AIRCRAFT2, BATTLESHIP, DESTROYER, SUBMARINE, PATROLBOAT
    }
}
