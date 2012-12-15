package cz.kotu.ld48.prime;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame jFrame = new JFrame("Get Away!!!");
        final GamePanel comp = new GamePanel();
        jFrame.add(comp);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);


        new Thread(){
            public void run(){
                comp.loadImages();
                comp.loadSounds();
                comp.game.loop();
            }
        }.start();


    }
}
