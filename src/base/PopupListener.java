package base;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import base.TurnObject.Action;
import base.TurnObject.MissileType;
import base.TurnObject.ScanType;

public class PopupListener extends MouseAdapter{
	private final String attack = "ATTACK";
	private final String fire = "Fire at this space";
	private final String fireM = "Fire a missile with this center/start";
	private final String fireAA = "Fire anti-aircraft gun at this space";
	private final String carrierX = "Carrier X";
	private final String carrierC = "Carrier +";
	private final String bship = "Battleship";
	private final String destV = "Destroyer Vertical";
	private final String destH = "Destroyer Horizontal";
	private final String subV = "Sub Vertical";
	private final String subH = "Sub Horizontal";
	private final String scan = "SCAN";
	private final String sub = "Sub scan here";
	private final String acX = "Aircraft scan here x-pattern";
	private final String acC = "Aircraft scan here +-pattern";
	private final String move = "MOVE";
	private final String ac1 = "Move Aircraft 1 here";
	private final String ac2 = "Move Aircraft 2 here";
	JPopupMenu popup;
	JMenuItem menuItem;
	Component invoker;
	private TurnObject turnObject;
	int x;
	int y;

	PopupListener(Component invoker, int x, int y, TurnObject turnObject) {
		this.invoker = invoker;
		this.turnObject = turnObject;
		this.x = x;
		this.y = y;
		popup = new JPopupMenu();
		popup.add(createAttackMenu());
		popup.add(createScanMenu());
		popup.add(createMoveMenu());
		popup.show(invoker, x, y);
	}

	private JMenu createAttackMenu() {
		JMenu attackSubMenu = new JMenu(attack);
		menuItem = new JMenuItem(fire);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_REGULAR);
				Driver.playerTurnReady();
			}
		});
		attackSubMenu.add(menuItem);
		attackSubMenu.add(createMissileMenu());
		menuItem = new JMenuItem(fireAA);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_ANTIAIR);
				Driver.playerTurnReady();
			}
		});
		attackSubMenu.add(menuItem);
		return attackSubMenu;
	}

	private JMenu createMissileMenu() {
		JMenu missileSubMenu = new JMenu(fireM);
		menuItem = new JMenuItem(carrierX);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_MISSILE);
				turnObject.setMissileType(MissileType.CARRIER_X);
				Driver.playerTurnReady();
			}
		});
		missileSubMenu.add(menuItem);
		menuItem = new JMenuItem(carrierC);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_MISSILE);
				turnObject.setMissileType(MissileType.CARRIER_CROSS);
				Driver.playerTurnReady();
			}
		});
		missileSubMenu.add(menuItem);
		menuItem = new JMenuItem(bship);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_MISSILE);
				turnObject.setMissileType(MissileType.BATTLESHIP);
				Driver.playerTurnReady();
			}
		});
		missileSubMenu.add(menuItem);
		menuItem = new JMenuItem(destV);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_MISSILE);
				turnObject.setMissileType(MissileType.DESTROYER_V);
				Driver.playerTurnReady();
			}
		});
		missileSubMenu.add(menuItem);
		menuItem = new JMenuItem(destH);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_MISSILE);
				turnObject.setMissileType(MissileType.DESTROYER_H);
				Driver.playerTurnReady();
			}
		});
		missileSubMenu.add(menuItem);
		menuItem = new JMenuItem(subV);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_MISSILE);
				turnObject.setMissileType(MissileType.SUB_V);
				Driver.playerTurnReady();
			}
		});
		missileSubMenu.add(menuItem);
		menuItem = new JMenuItem(subH);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.FIRE_MISSILE);
				turnObject.setMissileType(MissileType.SUB_H);
				Driver.playerTurnReady();
			}
		});
		missileSubMenu.add(menuItem);
		return missileSubMenu;
	}

	private JMenu createScanMenu() {
		JMenu scanSubMenu = new JMenu(scan);
		menuItem = new JMenuItem(sub);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.SCAN);
				turnObject.setScanType(ScanType.SUB);
				Driver.playerTurnReady();
			}
		});
		scanSubMenu.add(menuItem);
		menuItem = new JMenuItem(acX);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.SCAN);
				turnObject.setScanType(ScanType.AIRCRAFT_X);
				turnObject.setAircraftNumber(-1);
				Driver.playerTurnReady();
			}
		});
		scanSubMenu.add(menuItem);
		menuItem = new JMenuItem(acC);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.SCAN);
				turnObject.setScanType(ScanType.AIRCRAFT_CROSS);
				turnObject.setAircraftNumber(-1);
				Driver.playerTurnReady();
			}
		});
		scanSubMenu.add(menuItem);
		return scanSubMenu;
	}

	private JMenu createMoveMenu() {
		JMenu moveSubMenu = new JMenu(move);
		menuItem = new JMenuItem(ac1);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.MOVE_AIRCRAFT);
				turnObject.setAircraftNumber(1);
				Driver.playerTurnReady();
			}
		});
		moveSubMenu.add(menuItem);
		menuItem = new JMenuItem(ac2);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				turnObject.setAction(Action.MOVE_AIRCRAFT);
				turnObject.setAircraftNumber(2);
				Driver.playerTurnReady();
			}
		});
		moveSubMenu.add(menuItem);
		return moveSubMenu;
	}

	// @Override
	// public void mouseEntered(MouseEvent e){
	// Color c = Color.BLACK;
	// String command = ((JMenuItem)e.getSource()).getText();
	// System.out.println(command);
	// if(command.equals("ATTACK")){
	// c = Color.RED;
	// createAttackMenu();
	// }else if(command.equals("SCAN")){
	// c = Color.BLUE;
	// createScanMenu();
	// }else if(command.equals("MOVE")){
	// c = Color.PINK;
	// createMoveMenu();
	// }
	// invoker.setBackground(c);
	// }

}