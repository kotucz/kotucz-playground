package cz.kotu.ld48.prime;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame jFrame = new JFrame("Ride away!");
        final GamePanel comp = new GamePanel(jFrame);
        jFrame.add(comp);
        jFrame.setResizable(Game.DEBUG);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);




        new Thread(){
            public void run(){
//                R.id.loadImages();
//                R.id.loadSounds();
                comp.game.loop();
            }
        }.start();


    }
}
