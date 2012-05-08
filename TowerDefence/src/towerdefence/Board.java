/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package towerdefence;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.vecmath.Point3d;

/**
 *
 * @author Kotuc
 */
public class Board {
    
    private List<Unit> units = new ArrayList<Unit>();
    private List<Unit> unitsToAdd = new ArrayList<Unit>();
    private List<Unit> unitsToRemove = new ArrayList<Unit>();
    
    int width=640, height=480;
    
    BufferedImage heatImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
    public Board() {
        for (int i = 0; i < 10; i++) {
//            Creep creep = new Creep();
//            creep.setPos(new Point3d(Math.random()*width, Math.random()*height, 0));
//            this.put(creep);
////            Tower tower = new Tower();
////            tower.setPos(new Point3d(Math.random()*width, Math.random()*height, 0));
////            this.put(tower);
            CreepsDen den = new CreepsDen();
            den.setPos(new Point3d(50, i*40+20, 0));
            put(den);
        }
        
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                doRound();
            }
        }, 100, 10);
        
    }
    
    public void put(Unit unit) {
        unit.setBoard(this);
        unitsToAdd.add(unit);
    }
    
    public void remove(Unit unit) {
        unitsToRemove.add(unit);
    }
    
    public synchronized void doRound() {
        
        
        
//        while (Math.random()<Math.sqrt((0.99-units.size()/1400.0))) {
////        while (Math.random()<Math.sqrt((0.99-units.size()/1000.0))) {
////        if (Math.random()<(1.0-units.size()/1000.0)) {
////        if (Math.random()<0.54) {
//            Creep creep = new Creep();
//            creep.setPos(new Point3d(-10, Math.random()*height, 0));
//            this.put(creep);
//        }
        
        units.addAll(unitsToAdd);
        unitsToAdd.clear();
        units.removeAll(unitsToRemove);
        unitsToRemove.clear();
        for (Unit unit : units) {
            unit.doRound();
        }
    }

    public List<Unit> getUnits() {
        return units;
    }
        
    public synchronized void paint(Graphics g) {
        
        if (Math.random()<0.01) {
            //recomputeHeat();
        }
        
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, width, height);
//        g.drawImage(heatImg, 0, 0, null);
        g.setColor(Color.BLACK);
        g.drawString("Units: "+units.size(), width-100, 20);
        for (Unit unit : units) {
            unit.paint(g);
        }

    }

    private void recomputeHeat() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                heatImg.setRGB(x, y, getHeat(x, y));
            }
        }
    }
    
    private int getHeat(int x, int y) {
        return x;
    }
    
}
