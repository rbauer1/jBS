package base;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import base.Driver.AIName;

public class TestPanel extends JFrame {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TestObject testObject;
	private JButton runButton;
	private JComboBox firstAIChoice;
	private JComboBox secondAIChoice;
	private JPanel navigationPanel;
	private JLabel numRunsLabel;
	private JTextField numRunsField;
	private JLabel delayLabel;
	private JTextField delayField;
	private JToggleButton includePlayerToggle;
	private boolean includePlayer;
	private JToggleButton displayBoardsToggle;
	private boolean displayBoards;
	private JToggleButton displayHitBoardsToggle;
	private boolean displayHitBoards;
	private JToggleButton displayShipsSunkToggle;
	private boolean displayShipsSunk;
	private JToggleButton displayGUIToggle;
    private boolean displayGUI;
	private int xDim = 750;
	private int yDim = 400;
	private boolean readyFlag;

	public TestPanel() {
		readyFlag = false;
		includePlayer = false;
		displayBoards = false;
		displayHitBoards = false;
		displayShipsSunk = false;
		displayGUI = true;
		setBounds(50, 50, xDim, yDim);
		Container c = this.getContentPane();
		numRunsLabel = new JLabel("Enter the number of test games");
		numRunsField = new JTextField(10);
		delayLabel = new JLabel("Enter the wait-time between turns (ms)");
		delayField = new JTextField(10);
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
		displayGUIToggle = new JToggleButton();
        displayGUIToggle.setText("Do NOT Display Game GUI (DEFAULT IS SHOW GUI)");
		navigationPanel = new JPanel();
		navigationPanel.setBounds(0, 0, xDim, yDim);
		navigationPanel.setLayout(new GridLayout(6, 2, 10, 10));
		/*
		 * this next thing should be modified at somepoint so it doesn't need to
		 * be hardcoded. This should probably be done through some UI that
		 * allows custom AIs to be added to the list in some manner that doesn't
		 * involve changing the actual code of the program
		 */
		firstAIChoice = new JComboBox(AIName.values());
		firstAIChoice.setSelectedIndex(AIName.values().length-1);
		secondAIChoice = new JComboBox(AIName.values());
		secondAIChoice.setSelectedIndex(AIName.values().length-1);
		// AIChoices.addActionListener(new ActionListener(){
		// public void
		// });
		navigationPanel.add(numRunsLabel);
		navigationPanel.add(numRunsField);
		navigationPanel.add(delayLabel);
		navigationPanel.add(delayField);
		navigationPanel.add(includePlayerToggle);
		navigationPanel.add(displayBoardsToggle);
		navigationPanel.add(displayHitBoardsToggle);
		navigationPanel.add(displayShipsSunkToggle);
		navigationPanel.add(firstAIChoice);
		navigationPanel.add(secondAIChoice);
		navigationPanel.add(displayGUIToggle);
		navigationPanel.add(runButton);
		c.add(navigationPanel);
		setListeners();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		c.setVisible(true);
		navigationPanel.setVisible(true);
	}

	private void setListeners() {
		displayShipsSunkToggle.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
				      if(ev.getStateChange()==ItemEvent.SELECTED){
				    	  displayShipsSunk = true;
				      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
				    	  displayShipsSunk = false;
				      }
				   }
				});
		displayHitBoardsToggle.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
				      if(ev.getStateChange()==ItemEvent.SELECTED){
				    	  displayHitBoards = true;
				      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
				    	  displayHitBoards = false;
				      }
				   }
				});
		displayBoardsToggle.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
				      if(ev.getStateChange()==ItemEvent.SELECTED){
				    	  displayBoards = true;
				      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
				    	  displayBoards = false;
				      }
				   }
				});
		displayGUIToggle.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                   if(ev.getStateChange()==ItemEvent.SELECTED){
                       displayGUI = false;
                   } else if(ev.getStateChange()==ItemEvent.DESELECTED){
                       displayGUI = true;
                   }
                }
             });
		includePlayerToggle.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
				      if(ev.getStateChange()==ItemEvent.SELECTED){
				    	  includePlayer = true;
				    	  displayGUI = true;
				    	  displayGUIToggle.setEnabled(false);
				      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
				    	  includePlayer = false;
				    	  displayGUIToggle.setEnabled(true);
				      }
				   }
				});
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int runs;
				int delay;
				try {
					runs = Integer.parseInt(numRunsField.getText());
				} catch (java.lang.NumberFormatException ex) {
					runs = 1;
					System.out
							.println("Invalid or no input for number of runs, now running 1 time");
				}
				try {
                    delay = Integer.parseInt(delayField.getText());
                } catch (java.lang.NumberFormatException ex) {
                    delay = 0;
                    System.out
                            .println("Invalid or no input for delay time, using \"0ms\"");
                }
				AIName ai1 = (AIName) firstAIChoice.getSelectedItem();
				AIName ai2 = (AIName) secondAIChoice.getSelectedItem();
				testObject = new TestObject(runs, delay, ai1, ai2, includePlayer,
						displayBoards, displayHitBoards, displayShipsSunk, displayGUI);
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
		if(!readyFlag) System.exit(0); //probably should change this
		return testObject;
	}
}
