package base;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ColorCycler {
    private static JFrame frame;
    private static JPanel panel;

    public ColorCycler() {
        frame = new JFrame();
        frame.setBounds(100, 100, 200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = frame.getContentPane();
        panel = new JPanel();
        panel.setBounds(0, 0, 200, 200);

        c.add(panel);

        frame.setVisible(true);
        c.setVisible(true);
        panel.setVisible(true);
    }

    public static void main(String[] args) {
        new ColorCycler();
        for (;;) {
            try {
                Thread.sleep(9);//important
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            panel.setBackground(getColorTime());
            // frame.invalidate();
        }
    }

    private static Color getColorTime() {
        System.out.println();
        System.out.println("Current time" + System.currentTimeMillis());
        double time = ((double) (System.currentTimeMillis() % 10000)) / 10000;
        System.out.println("Time " + time);
        int switchTime = (int) (time*1000);
        System.out.println("SwitchTime " + switchTime);
        if (switchTime < 167) {
            time /=167;
            time *=1000;
            System.out.println("Time adj " + time);
            return new Color(255, 0, (int) (255 - 255 * time));
        }
        if (switchTime < 333) {
            time -=0.167;
            time /=167;
            time *=1000;
            System.out.println("Time adj " + time);
            return new Color(255, (int) (255 * time), 0);
        }
        if(switchTime <500){
            time -=0.333;
            time /=167;
            time *=1000;
            System.out.println("Time adj " + time);
            return new Color((int) (255 - 255 * time), 255, 0);
        }
        if (switchTime < 667) {
            time -=0.500;
            time /=167;
            time *=1000;
            System.out.println("Time adj " + time);
            return new Color(0, 255, (int) (255 * time));
        }
        if (switchTime < 833) {
            time -=0.667;
            time /=167;
            time *=1000;
            System.out.println("Time adj " + time);
            return new Color(0, (int) (255 - 255 * time), 255);
        }
        if(switchTime <1001){
            if(time==1)
                time-=0.01;
            time -=0.833;
            time /=167;
            time *=1000;
            System.out.println("Time adj " + time);
            return new Color((int) (255 * time), 0, 255);
        }
        System.err.println("Should Not Reach This");
        return Color.BLACK;
        
    }

}
