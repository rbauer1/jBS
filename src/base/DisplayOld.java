package base;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class DisplayOld {
//	private int buttonPlaceV = 80;
//	private int buttonPlaceH = 10;
//	private int hChange = 59;
//	private int action = 0;
	private JButton[] buttonArray = new JButton[14];
//
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

	public DisplayOld() {
//		final Board board = new Board();
//		final int[] buttonkey={0,0,1,2,2,3};
//		JFrame frame = new JFrame();
//		frame.setLayout(null);
//		frame.setBackground(Color.DARK_GRAY);
//		frame.setBounds(20, 20, 1052, 660);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//
//		JPanel boardPanel = new JPanel();
//		boardPanel.setBounds(30, 30, 420, 330);
//		boardPanel.setLayout(null);
//		boardPanel.setVisible(true);
//
//		JPanel actionPanel = new JPanel();
//		actionPanel.setBounds(30, 380, 800, 240);
//		actionPanel.setBackground(Color.GRAY);
//		actionPanel.setVisible(true);
//		actionPanel.setLayout(null);
//		for (int z = 0; z < 14; z++) {
//			final int x = z;
//			buttonArray[z] = new JButton(new ImageIcon(images[z]));
//			buttonArray[z].setBounds(buttonPlaceH, buttonPlaceV, 39, 39);
//			buttonArray[z].setFocusable(false);
//			buttonArray[z].addActionListener(new ActionListener() {				
//						
//						public void actionPerformed(ActionEvent e) {
//							if(x>5||board.checkFireAble(buttonkey[x])){
//						resetButtons();
//						buttonArray[x].setIcon(new ImageIcon(pressedImages[x]));
//						action = x;
//					}
//				}
//			});
//			buttonPlaceH += hChange;
//			if (z == 6) {
//				buttonPlaceV += 80;
//				buttonPlaceH = 10;
//			}
//			actionPanel.add(buttonArray[z]);
//		}
//
//		Container contentPane = frame.getContentPane();
//		contentPane.setLayout(null);
//		contentPane.setBackground(Color.DARK_GRAY);
//		contentPane.add(boardPanel);
//		contentPane.add(actionPanel);
	}

	public void resetButtons() {
		for (int i = 0; i < buttonArray.length; i++) {
			buttonArray[i].setIcon(new ImageIcon(images[i]));
		}
	}

	public void turnOffButton(JButton jb) {
		jb.setIcon(new ImageIcon("images/cannotUse.png"));
		jb.removeActionListener(null);
	}

}
