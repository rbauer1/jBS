package base;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import base.Driver.AIName;

public class TestPanel extends JFrame {
	private TestObject testObject;
	private JButton runButton;
	private JComboBox firstAIChoice;
	private JComboBox secondAIChoice;
	private JPanel navigationPanel;
	private JTextField numRunsField;
	private JLabel numRunsLabel;
	private JToggleButton includePlayerToggle;
	private boolean includePlayer;
	private JToggleButton displayBoardsToggle;
	private boolean displayBoards;
	private JToggleButton displayHitBoardsToggle;
	private boolean displayHitBoards;
	private JToggleButton displayShipsSunkToggle;
	private boolean displayShipsSunk;
	private int xDim = 750;
	private int yDim = 400;
	private boolean readyFlag;

	public TestPanel() {
		readyFlag = false;
		includePlayer = false;
		displayBoards = false;
		displayHitBoards = false;
		displayShipsSunk = false;
		setBounds(50, 50, xDim, yDim);
		setVisible(true);
		Container c = this.getContentPane();
		c.setVisible(true);
		numRunsLabel = new JLabel("Enter the number of test games");
		numRunsField = new JTextField(10);
		runButton = new JButton("Run");
		includePlayerToggle = new JToggleButton();
		includePlayerToggle.setText("Include Player");
		displayBoardsToggle = new JToggleButton();
		displayBoardsToggle.setText("Display Each Turn's Board");
		displayHitBoardsToggle = new JToggleButton();
		displayHitBoardsToggle
				.setText("Display each AI's Hit Board at the end of each game");
		displayShipsSunkToggle = new JToggleButton();
		displayShipsSunkToggle.setText("Display when ships are sunk");
		navigationPanel = new JPanel();
		navigationPanel.setBounds(0, 0, xDim, yDim);
		navigationPanel.setVisible(true);
		navigationPanel.setLayout(new GridLayout(5, 2, 10, 10));
		/*
		 * this next thing should be modified at somepoint so it doesn't need to
		 * be hardcoded. This should probably be done through some UI that
		 * allows custom AIs to be added to the list in some manner that doesn't
		 * involve changing the actual code of the program
		 */
		firstAIChoice = new JComboBox(AIName.values());
		firstAIChoice.setSelectedIndex(0);
		secondAIChoice = new JComboBox(AIName.values());
		secondAIChoice.setSelectedIndex(0);
		// AIChoices.addActionListener(new ActionListener(){
		// public void
		// });
		c.add(navigationPanel);
		navigationPanel.add(numRunsLabel);
		navigationPanel.add(numRunsField);
		navigationPanel.add(includePlayerToggle);
		navigationPanel.add(displayBoardsToggle);
		navigationPanel.add(displayHitBoardsToggle);
		navigationPanel.add(displayShipsSunkToggle);
		navigationPanel.add(firstAIChoice);
		navigationPanel.add(secondAIChoice);
		navigationPanel.add(runButton);
		setListeners();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setListeners() {
		displayShipsSunkToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayShipsSunk = true;
			}
		});
		displayHitBoardsToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayHitBoards = true;
			}
		});
		displayBoardsToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayBoards = true;
			}
		});
		includePlayerToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				includePlayer = true;
			}
		});
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int runs;
				try {
					runs = Integer.parseInt(numRunsField.getText());
				} catch (java.lang.NumberFormatException ex) {
					runs = 1;
					System.out
							.println("Invalid or no input for number of runs, now running 1 time");
				}
				AIName ai1 = (AIName) firstAIChoice.getSelectedItem();
				AIName ai2 = (AIName) secondAIChoice.getSelectedItem();
				testObject = new TestObject(runs, ai1, ai2, includePlayer,
						displayBoards, displayHitBoards, displayShipsSunk);
				readyFlag = true;
			}
		});
	}

	public TestObject getTestObject() {
		int count = 0;
		while (count < 5000 && !readyFlag) {
			count++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return testObject;
	}
}
