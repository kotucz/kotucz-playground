package rccars;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.m3g.*;

/**
 * 
 * @author Kotuc
 */
public class RCanvas extends GameCanvas implements Runnable {

    Thread myThread = null;
    boolean running = false;
    Graphics3D g3d = null;
    World world = null;				

    Camera cam = null;			
    Group camG = new Group();
    Group camGG = new Group();
    
    RCTrack track;
    
    RCar car1;
    float[] camera = {0, 0, 0, 90, 0};	//0-X,1-Y,2-Z,3-RotZ,4-RotX
    
    
    public RCanvas() {
        super(false);
        setFullScreenMode(true);
        createWorld();
        
        myThread = new Thread(this);
        running = true;
        myThread.start();
    }

    public void run() {
        while (running) {
            
            car1.keys(getKeyStates());
                    
            car1.moveCar();			// funkce na pohybovani s autem
            moveCamera();		// -||- kamerou
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            repaint();				// prekresleni displaye
        }
    }

    /**
     * 
     */
    public void createWorld() {
        
// world        
        world = new World();

// light        
        Light light = new Light();
        light.setColor(0xFFFFFFF);
        light.setIntensity(1f);
        world.addChild(light);

// camera        
        cam = new Camera();
        world.addChild(camGG);
        camGG.addChild(camG);
        camG.addChild(cam); 
        cam.setPerspective(45.0f, (float) getWidth() / (float) getHeight(), 0.1f, 3000);
        world.setActiveCamera(cam);
        
// track        
        track = new RCTrack();
        world.addChild(track.getTrack());
        
// cars       
        car1 = new RCar(track);
        world.addChild(car1.carG);
        

    }



    /**
     * loads the object group with id from file 
     * 
     * @param file
     * @param id
     * @return
     */
    public static Group loadObject(String file, int id) {
        try {
            Object3D[] buffer = Loader.load(file);
            World tempWorld = null;
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] instanceof World) {
                    tempWorld = (World) buffer[i];
                    break;
                }
            }
            Group objG = (Group) tempWorld.find(id);
            tempWorld.removeChild(objG);
            
//            Group helpG = new Group();
//            helpG.addChild(g1);	
//            return helpG;
            
            return objG;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void paint(Graphics g) {	
        g3d = Graphics3D.getInstance();
        try {
            g3d.bindTarget(g, true, 0);
            g3d.render(world);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g3d.releaseTarget();
        }
    }




    private void moveCamera() {				

        // 0-X,1-Y,2-Z,3-RotZ,4-RotX

        if (car1.speed < -3) {	
            camera[3] += (180 - camera[3]) / 5;		
        } else {		
            camera[3] -= camera[3] / 5;	
        }
        
        camera[0] += (car1.x - camera[0]) / 4;	
        camera[1] += (car1.y - camera[1]) / 4;	
        camera[2] += (car1.z - camera[2]) / 4;	
        camera[4] += (car1.rotX - camera[4]) / 4;
        
//        camGG.setTranslation(car1.x, car1.y, car1.z);
        camGG.setTranslation(camera[0], camera[1], camera[2]);
        camGG.setOrientation(car1.rotZ, 0, 0, 1);
        
        camG.setOrientation(70, 1, 0, 0);
        camG.setTranslation(0, -100, 70);

    }



}
