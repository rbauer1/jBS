package base;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ships.Aircraft;
import ships.Ships.ShipType;
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
	private String[] images = { "images/acc1.png", "images/acc2.png",
			"images/bs.png", "images/dest1.png", "images/dest2.png",
			"images/sub1.png", "images/sub2.png", "images/subs.png",
			"images/rec1.png", "images/rec2.png", "images/rec1.png",
			"images/rec2.png", "images/moveR1.png", "images/moveR2.png" };
	private String[] pressedImages = { "images/acc1p.png", "images/acc2p.png",
			"images/bsp.png", "images/dest1p.png", "images/dest2p.png",
			"images/sub1p.png", "images/sub2p.png", "images/subsp.png",
			"images/rec1p.png", "images/rec2p.png", "images/rec1p.png",
			"images/rec2p.png", "images/moveR1p.png", "images/moveR2p.png" };
	private final int FRAME_HEIGHT = 800;
	private final int FRAME_WIDTH = 1000;
	private final int SQ_SIZE = 30;
	private final int BOARD_SEPARATION_VERTICAL = 400;
	private final int BOARD_SEPARATION_HORIZONTAL = 500;

	private int x; // these are used for the mouse listener
	private int y;
	private int lastClickedX = 0;
	private int lastClickedY = 0;

	public Display() {

	}

	public Display(Player p2, Player p1, AI ai) {
		this.ai = ai;
		this.p1 = p1;
		this.p2 = p2;

		frame = new JFrame();
		frame.setBounds(new Rectangle(FRAME_WIDTH, FRAME_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.RED); // for debugging porpoises
		frame.setLayout(null);
		frame.setLocation(100, 50);

		container = frame.getContentPane();
		container.setBackground(Color.BLACK);

		yourBoard = new JPanel[15][11];
		opponentBoard = new JPanel[15][11];
		probabilitiesBoard = new JPanel[15][11];
		;

		coordLabels1 = new JLabel[24];
		coordLabels2 = new JLabel[24];
		coordLabelsProbDisp = new JLabel[24];

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 11; j++) {
				opponentBoard[i][j] = new JPanel();
				container.add(opponentBoard[i][j]);
				opponentBoard[i][j].setLocation(i + i * SQ_SIZE, j + j
						* SQ_SIZE);
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
				if(i>0 &&j>0){
					x=i; y=j;
				opponentBoard[i][j].addMouseListener(new MouseListener() {
					int xx = x;
					int yy = y;
					@Override
					public void mouseClicked(MouseEvent arg0) {
						opponentBoard[xx][yy].setBackground(Color.BLUE);							
						if((lastClickedX !=0 && lastClickedY !=0) ^ (lastClickedX !=xx && lastClickedY != yy)){
							opponentBoard[lastClickedX][lastClickedY].setBackground(updateOpponentBoardHelper(lastClickedX,lastClickedY));
						}
						lastClickedX = xx;
						lastClickedY = yy;
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						if(!opponentBoard[xx][yy].getBackground().equals(Color.BLUE))
						opponentBoard[xx][yy].setBackground(updateOpponentBoardHelper(xx,yy).darker());
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						if(!opponentBoard[xx][yy].getBackground().equals(Color.BLUE))
						opponentBoard[xx][yy].setBackground(updateOpponentBoardHelper(xx,yy));
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

				});
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
				if(i>0 &&j>0){
					x=i; y=j;
				yourBoard[i][j].addMouseListener(new MouseListener() {
					int xx = x;
					int yy = y;
					@Override
					public void mouseClicked(MouseEvent arg0) {
						yourBoard[xx][yy].setBackground(Color.BLUE);
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						yourBoard[xx][yy].setBackground(updateYourBoardHelper(xx,yy).darker());
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						if(!yourBoard[xx][yy].getBackground().equals(Color.BLUE))
						yourBoard[xx][yy].setBackground(updateYourBoardHelper(xx,yy));
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

				});
				}
				yourBoard[i][j].setVisible(true);
				container.add(yourBoard[i][j]);
			}
		}
		yourBoard[0][0].setBackground(Color.BLACK);

		if (ai.hasProbabilities()) {
			aiProbabilities = ai.getProbabilities();
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 11; j++) {
					probabilitiesBoard[i][j] = new JPanel();
					container.add(probabilitiesBoard[i][j]);
					probabilitiesBoard[i][j].setLocation(i + i * SQ_SIZE
							+ BOARD_SEPARATION_HORIZONTAL, j + j * SQ_SIZE
							+ BOARD_SEPARATION_VERTICAL);
					probabilitiesBoard[i][j].setSize(new Dimension(SQ_SIZE,
							SQ_SIZE));
					if ((i == 0) ^ (j == 0)) {
						probabilitiesBoard[i][j].setBackground(Color.GRAY);
						if (i == 0) {
							coordLabelsProbDisp[j - 1] = new JLabel(
									(char) (j + 64) + "");
							probabilitiesBoard[i][j]
									.add(coordLabelsProbDisp[j - 1]);
						}
						if (j == 0) {
							coordLabelsProbDisp[i + 9] = new JLabel(i + "");
							probabilitiesBoard[i][j]
									.add(coordLabelsProbDisp[i + 9]);
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
				opponentBoard[i][j].setLocation(i + i * SQ_SIZE, j + j
						* SQ_SIZE);
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

	public void dispose() {
		frame.dispose();
	}

	public void updateBoards() {
		updateYourBoard();
		updateOpponentBoard();
		if (ai.hasProbabilities()) {
			updateProbabilityBoard();
		}
		container.validate();
	}

	private Color getGradientMapColor(int i, int j) {
		float probability = 0;
		float probMax = ai.findHighestProbabilityPublic();
		probability = aiProbabilities[j][i][1][0] / probMax;
		if (probability == 1) {
			return Color.MAGENTA;
		} else if (probability != 0 && !Float.isNaN(probability)) {
			return new Color(probability, (probability) / (2 - probability),
					(1 - probability) / 2);
		}
		return Color.DARK_GRAY;
	}

	private void updateProbabilityBoard() {
		TileStatus[][] b = p1.getPlayerStatusBoard();
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
	
	private Color updateOpponentBoardHelper(int i, int j){
		TileStatus[][] b = p2.getPlayerStatusBoard();
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
		if (p1.getPlayerShipBoard()[j][i][1] == ShipType.AIRCRAFT1) {
			if (((Aircraft) p1.getAllShips()[5]).launched()
					&& !((Aircraft) p1.getAllShips()[5])
							.isThisShipSunk()) {
				c = new Color(255, 0, 255);
			}
		} else if (p1.getPlayerShipBoard()[j][i][1] == ShipType.AIRCRAFT2) {
			if (((Aircraft) p1.getAllShips()[6]).launched()
					&& !((Aircraft) p1.getAllShips()[6])
							.isThisShipSunk()) {
				c = new Color(100, 0, 255);
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
	
	private Color updateYourBoardHelper(int i, int j){
		if(i==0 && j == 0){
			return Color.BLACK;
		}
		if(i==0 || j == 0){
			return Color.GRAY;
		}
		TileStatus[][] b = p1.getPlayerStatusBoard();
		Color c = new Color(0);
		switch (b[j][i]) {
		case SHIP:
		case SUBSCANSHIP:
		case AIRCRAFTSCAN:
			switch (p1.getPlayerShipBoard()[j][i][0]) {
			case AIRCRAFTCARRIER:
				c = new Color(0, 255, 0);
				break;
			case BATTLESHIP:
				c = new Color(0, 255, 50);
				break;
			case DESTROYER:
				c = new Color(0, 255, 100);
				break;
			case SUBMARINE:
				c = new Color(0, 255, 150);
				break;
			case PATROLBOAT:
				c = new Color(0, 255, 200);
				break;
			default:
				break;
			}
			switch (p1.getPlayerShipBoard()[j][i][1]) {
			case AIRCRAFT1:
				if (!(((Aircraft) p1.getShip(5)).launched())) {
					c = new Color(200, 0, 255);
				}
				break;
			case AIRCRAFT2:
				if (!(((Aircraft) p1.getShip(6)).launched())) {
					c = new Color(100, 0, 255);
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
	}
}
