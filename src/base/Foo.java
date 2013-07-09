package base;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Foo {
    private JFrame frame;
    private JPanel[][] opponentBoard;
    private JLabel[] coordLabels;
    private final int FRAME_HEIGHT = 800;
    private final int FRAME_WIDTH = 600;
    private final int SQ_SIZE = 30;
    public Foo() {
        frame = new JFrame();
        frame.setBounds(new Rectangle(FRAME_WIDTH, FRAME_HEIGHT));
        //frame.setVisible(true); Build the UI before making it visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.RED); // for debugging porpoises
        frame.setLayout(null);

        Container container = frame.getContentPane();
        container.setBackground(Color.BLACK);
        container.setVisible(true);

        opponentBoard = new JPanel[15][11];

        coordLabels = new JLabel[24];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                opponentBoard[i][j] = new JPanel();
                container.add(opponentBoard[i][j]);
                opponentBoard[i][j].setLocation(i + i * SQ_SIZE, j + j * SQ_SIZE);
                opponentBoard[i][j].setSize(new Dimension(SQ_SIZE, SQ_SIZE));
                opponentBoard[i][j].setVisible(true);
                if ((i == 0) ^ (j == 0)) {
                    opponentBoard[i][j].setBackground(Color.GRAY);
                    if (i == 0) {
                        coordLabels[j - 1] = new JLabel((char) (j + 64) + "");
                        opponentBoard[i][j].add(coordLabels[j - 1]);
                        System.out.println(j-1);
                    }
                    if (j == 0) {
                        coordLabels[i + 9] = new JLabel(i + "");
                        System.out.println(i + 9 + "    " + i + " " + coordLabels[i + 9].getText());
                        opponentBoard[i][j].add(coordLabels[i + 9]);
                    }
                    //opponentBoard[i][j].repaint();
                } else {
                    opponentBoard[i][j].setBackground(Color.DARK_GRAY);
                }
            }
        }
        opponentBoard[0][0].setBackground(Color.BLACK);

        // If the UI is already visible call validate
        // I've chose to not make the frame visible until all of the children
        // have been added so the call to validate isn't really needed.
        //container.validate();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Foo();
    }
}