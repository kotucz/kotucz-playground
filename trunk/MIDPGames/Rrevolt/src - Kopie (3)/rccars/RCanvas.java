package rccars;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.*;

/**
 * 
 * @author Kotuc
 */
public class RCanvas extends Canvas implements Runnable {

    boolean carview = true;
    // gravitation
    float grav = 1.8f;
    // friction
    float fric = 0.22f; // 0.3f
    // car does not test floor
    boolean freeride = false;
    Thread myThread = null;
    boolean running = false;
    Graphics3D g3d = null;
    World world = null;				// "hlavni" svet
    Camera cam = null;				// kamera snimajici car1 z pohledu 3. osoby
    Group camG = new Group();		// pomocna skupina pro kameru (-> snadnejsi manipulace)
    Group camGG = new Group();		// dalsi pomocna skupina pro kameru
//    Group car1 = null;				// nase auto
    Group car1G = new Group();		// pomocna skupina s nasim autem
    Group groundG = new Group();		// skupina, kde se nachazi "podlaha" (pro kontrolu kolizi)
    Group track = new Group();
    Group roadG = null;
    
    RCar car1 = new RCar(); //0-X,1-Y,2-Z,3-RotZ,4-RotX,5-Speed,6-MaxSpeed,7-ZatocValue,8-Acc
//    float[] car1 = {0, 0, 0, 90, 0, 0, 20, 8, 3};	//0-X,1-Y,2-Z,3-RotZ,4-RotX,5-Speed,6-MaxSpeed,7-ZatocValue,8-Acc
    float[] camera = {0, 0, 0, 90, 0};	//0-X,1-Y,2-Z,3-RotZ,4-RotX
//  
    // ID
    int car1ID = 3; // ID auta
    int groundID = 11; // ID "podlahy"
    // KEYS
    boolean kU = false;	// if key up is pressed
    boolean kD = false;	// if key down is pressed
    boolean kL = false;	// if key left is pressed
    boolean kR = false;	// if key right is pressed
    RayIntersection ri = new RayIntersection();		// paprsek pouzivany na kolize

    public RCanvas() {
        setFullScreenMode(true);	// nastaveni na fullscreen
        loadWorld();				// funkce na nacteni vsech potrebnych m3g souboru do 1 sveta
        loadObjects();				// funkce na nacteni atp. jednotlivych objektu

        myThread = new Thread(this);
        running = true;
        myThread.start();
    }

    public void run() {
        while (running) {
            moveCar();			// funkce na pohybovani s autem
            moveCamera();		// -||- kamerou
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            repaint();				// prekresleni displaye
        }
    }

    public void loadWorld() {			// funkce na nacteni vsech potrebnych m3g souboru do 1 sveta
//        try {
//            Object3D[] buffer = Loader.load("/mapa_01.m3g");
//            for (int i = 0; i < buffer.length; i++) {
//                if (buffer[i] instanceof World) {
//                    world = (World) buffer[i];
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        world = new World();
        
        
        
        Light light = new Light();
        light.setColor(0xFFFFFFF);
        light.setIntensity(1f);
        world.addChild(light);

//
//        try {
//            Object3D[] buffer = Loader.load("/black_remote.m3g");
//            World autoWorld = null;						// docasny svet s modelem auta
//            for(int i = 0; i < buffer.length; i++) {
//                if(buffer[i] instanceof World) {
//                    autoWorld = (World)buffer[i];
//                    break;
//                }
//            }
//            car1 = (Group)autoWorld.find(car1ID);		// ziskani naseho auta
//            autoWorld.removeChild(car1);				// odstraneni auta z "jeho sveta"
//            car1G.addChild(car1);						// pridani modelu auta do pomocne skupiny auta
////            car1.preRotate(90, 0, 0, 0);
//            world.addChild(car1G);						// vlozeni skupiny s autem do hlavniho sveta
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
        car1G = loadObject("/cars/black_remote.m3g", car1ID);
        world.addChild(car1G);

        trackAdd("/maps/lvl_celek.m3g", 0, 0, 0);

//        trackAdd("/rovinka.m3g", -110, 0, 0);
//        trackAdd("/rovinka.m3g", 0, 0, 0);
//        trackAdd("/most.m3g", 110, 0, 0);
//        trackAdd("/zatacka.m3g", 110, 0, 0);
//        trackAdd("/rovinka.m3g", 220, -150, -90);
//        trackAdd("/zatacka.m3g", 220, -160, -90);
//        trackAdd("/kopecek.m3g", 110, -270, 0);

        world.addChild(track);
    }

    /**
     * adds the part of level loaded from file
     * to position specified by x, y, rotZ
     * 
     * @param file
     * @param x
     * @param y
     * @param rotZ
     */
    public void trackAdd(String file, float x, float y, float rotZ) {

        Group roadGr = loadObject(file, 3);
        roadGr.setScale(10, 10, 10);
        roadGr.preRotate(90, 1, 0, 0);

        Group roadMG = new Group();
        roadMG.setTranslation(x, y, 0);
        roadMG.preRotate(rotZ, 0, 0, 1);

        roadMG.addChild(roadGr);
        track.addChild(roadMG);
    }

    /**
     * loads the object group with id from file 
     * 
     * @param file
     * @param id
     * @return
     */
    public Group loadObject(String file, int id) {
        try {
            Object3D[] buffer = Loader.load(file);
            World tempWorld = null;						// docasny svet s modelem auta
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] instanceof World) {
                    tempWorld = (World) buffer[i];
                    break;
                }
            }
            Group g1 = (Group) tempWorld.find(id);		// ziskani naseho objektu
            tempWorld.removeChild(g1);				// odstraneni auta z "jeho sveta"
            Group helpG = new Group();
            helpG.addChild(g1);						// pridani modelu do pomocne skupiny
//            car1.preRotate(90, 0, 0, 0);
//            world.addChild(helpG);          // vlozeni skupiny s modelem do hlavniho sveta
            return helpG;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadObjects() {
        cam = new Camera();
        world.addChild(camGG);
        camGG.addChild(camG);
        camG.addChild(cam); 
        world.setActiveCamera(cam);
        
//        Mesh ground = (Mesh) world.find(groundID); 
//        world.removeChild(ground);	
//        groundG.addChild(ground); 
        cam.setPerspective(45.0f, (float) getWidth() / (float) getHeight(), 0.1f, 3000);
    }

    public void paint(Graphics g) {			// vykreslovani cele sceny
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

    private void moveCar() {			// funkce na pohyb s autem
        float z0 = 0;			// puvodni pozice auta
        float z1 = 0;				// nasledujici pozice auta
        if (car1.speed >= 0) {				// podminka starajici se o to, aby car1 spravne atacelo kdyz jede dopredu nebo dozadu
            if (kL == true) {				// zataceni doleva
                car1.rotZ += car1.turn;
                camera[3] -= car1.turn;
            } else if (kR == true) {			// zataceni doprava
                car1.rotZ -= car1.turn;
                camera[3] += car1.turn;
            }
        } else {							// kdyz jede car1 pospatku
            if (kL == true) {				// zataceni doleva
                car1.rotZ -= car1.turn;
                camera[3] += car1.turn;
            } else if (kR == true) {			// zataceni doprava
                car1.rotZ += car1.turn;
                camera[3] -= car1.turn;
            }
        }
        if (kU == true) {                                // zrychlovani auta (kdyz je aktualni rychlost mensi nez maximalni rychlost)
            car1.speed = 5f; //= car1.speed<car1.maxSpeed?car1.speed+car1.acc:car1.maxSpeed;
        } else if (kD == true) {					// zpomalovani a couvani auta (couva pomalejs nez kdyz jede dopredu)
            car1.speed = -3f; // car1.speed>0?car1.speed-car1.acc*2:car1.speed-car1.acc*0.5f;
        } else {
            car1.speed = 0;
        }

//        if (freeride) {
//            car1.vx += -(car1.speed * (float)Math.cos(Math.toRadians(car1.rotZ+90)));
//            car1.vy += -(car1.speed * (float)Math.sin(Math.toRadians(car1.rotZ+90)));
//            car1.x += car1.vx;
//            car1.y += car1.vy;
//        } else {
//
        car1.vx += -(car1.speed * (float) Math.cos(Math.toRadians(car1.rotZ + 90)));
        car1.vy += -(car1.speed * (float) Math.sin(Math.toRadians(car1.rotZ + 90)));

        if (track.pick(-1, car1.x + car1.vx, car1.y + car1.vy, car1.z + 30, 0, 0, -1, ri)) {	// vysilani paprsku na skupinu s "podlahou" smerem dolu v miste (X a Y) kam se ma car1 posunout
            if (ri.getDistance() < 200) { // 75
                float[] ray = new float[6]; // field of colliding ray
                ri.getRay(ray); // ziskani souradnic a smeru kam paprsek letel a kde narazil
//                car1.x = ray[0] + ray[3] * ri.getDistance();   // crash x
//                car1.y = ray[1] + ray[4] * ri.getDistance();   // crash y
                z1 = ray[2] + ray[5] * ri.getDistance();      // crash z
                if (car1.speed >= 0) {
//                        car1.rotX = -(float)Math.toDegrees(Mth.atan2((z_po-car1.z), car1.speed));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud jede dopredu)
                } else {
//                        car1.rotX = -(float)Math.toDegrees(Mth.atan2((car1.z-z_po), Math.abs(car1.speed)));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud couva)
                }
                //             if(car1.rotZ+((car1.z-z_po)*gravitace) < car1.maxSpeed && car1.speed+((car1.z-z_po)*gravitace) > -car1.maxSpeed) {		// aby auto nezrychlovalo nad maximalni rychlost (popredu i pozadu)
                //                 car1.rotZ += (car1.z-z_po)*gravitace;		// zrychlovani/spomalovani auto v zavislosti na kopci z/do ktereho jede
                //             }
                z0 = car1.z;
                float inc = (z1 - z0); // stoupani vozidla
                if (inc > 0) {
                    car1.vz = inc;
                } else if (inc < 0) {
                    if (inc < -grav) {
                        car1.vz -= grav;
                    } else {
                        car1.z += inc;
                        car1.vz = 1;
                    }
                }
            }

            car1.x += car1.vx;
            car1.y += car1.vy;

            car1.z += car1.vz;



        }

        car1.vx *= 1f - fric;
        car1.vy *= 1f - fric;

//        }

//        //////////////////////////
//        float dirZ = z0-z_po;
//        float[] riDistance = {car1[6]+car1[8], car1[6]+car1[8], car1[6]+car1[8], car1[6]+car1[8], car1[6]+car1[8], car1[6]+car1[8]};	//	+50°, +30°, +10°, -10°, -30°, -50°	(zleva doprava)
//        int smallestRI = 0;
//        if(car1[5] >= 0) {
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, -(float)Math.cos(Math.toRadians(car1[3]+140)), -(float)Math.sin(Math.toRadians(car1[3]+140)), dirZ, ri)) {
//                riDistance[0] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, -(float)Math.cos(Math.toRadians(car1[3]+120)), -(float)Math.sin(Math.toRadians(car1[3]+120)), dirZ, ri)) {
//                riDistance[1] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, -(float)Math.cos(Math.toRadians(car1[3]+100)), -(float)Math.sin(Math.toRadians(car1[3]+100)), dirZ, ri)) {
//                riDistance[2] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, -(float)Math.cos(Math.toRadians(car1[3]+80)), -(float)Math.sin(Math.toRadians(car1[3]+80)), dirZ, ri)) {
//                riDistance[3] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, -(float)Math.cos(Math.toRadians(car1[3]+60)), -(float)Math.sin(Math.toRadians(car1[3]+60)), dirZ, ri)) {
//                riDistance[4] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, -(float)Math.cos(Math.toRadians(car1[3]+40)), -(float)Math.sin(Math.toRadians(car1[3]+40)), dirZ, ri)) {
//                riDistance[5] = ri.getDistance();
//            }
//        } else {
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, (float)Math.cos(Math.toRadians(car1[3]+140)), (float)Math.sin(Math.toRadians(car1[3]+140)), dirZ, ri)) {
//                riDistance[0] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, (float)Math.cos(Math.toRadians(car1[3]+120)), (float)Math.sin(Math.toRadians(car1[3]+120)), dirZ, ri)) {
//                riDistance[1] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, (float)Math.cos(Math.toRadians(car1[3]+100)), (float)Math.sin(Math.toRadians(car1[3]+100)), dirZ, ri)) {
//                riDistance[2] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, (float)Math.cos(Math.toRadians(car1[3]+80)), (float)Math.sin(Math.toRadians(car1[3]+80)), dirZ, ri)) {
//                riDistance[3] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, (float)Math.cos(Math.toRadians(car1[3]+60)), (float)Math.sin(Math.toRadians(car1[3]+60)), dirZ, ri)) {
//                riDistance[4] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, car1[0], car1[1], car1[2]+2, (float)Math.cos(Math.toRadians(car1[3]+40)), (float)Math.sin(Math.toRadians(car1[3]+40)), dirZ, ri)) {
//                riDistance[5] = ri.getDistance();
//            }
//        }
//        for(int i = 0; i < riDistance.length; i++) {
//            if(riDistance[i] < riDistance[smallestRI]) {
//                smallestRI = i;
//            }
//        }
//        if(riDistance[smallestRI] < Math.abs(car1[5])) {
//            switch(smallestRI) {
//                case 0:
//                    car1[3] -= 50;
//                    camera[3] += 50;
//                    break;
//                case 1:
//                    car1[3] -= 30;
//                    camera[3] += 30;
//                    break;
//                case 2:
//                    car1[3] -= 10;
//                    camera[3] += 10;
//                    break;
//                case 3:
//                    car1[3] += 10;
//                    camera[3] -= 10;
//                    break;
//                case 4:
//                    car1[3] += 30;
//                    camera[3] -= 30;
//                    break;
//                case 5:
//                    car1[3] += 50;
//                    camera[3] -= 50;
//                    break;
//            }
//            car1[5] /= 3;
//                        /*vygenerujCastice(car1[0], car1[1], car1[2], 20, 0, 0, 5, 0, 0, -0.5f, 1, 1, 1, 50, 20, 0, 255, 0);
//                        pohybujCasticema = true;*/
//        }
//

        car1G.setTranslation(car1.x, car1.y, car1.z + 3);	// umisteni auta na aktualni pozice
        car1G.setOrientation(car1.rotZ + 180, 0, 0, 1);	// nastaveni aktualni rotace auta v ose Z (doleva/doprava)
        car1G.postRotate(car1.rotX + 90, 1, 0, 0);		// -||- X (nahoru/dolu)
//        car1.setOrientation(car1.rotX+90, 1, 0, 0);		// -||- X (nahoru/dolu)
    }

    private void moveCamera() {					// funkce na pohyb s kamerou

        //0-X,1-Y,2-Z,3-RotZ,4-RotX

        if (car1.speed < -3) {	
            camera[3] += (180 - camera[3]) / 5;				// plynule vyrovnani rotace kamery podle osy Z (doleva/doprava) pred car1
        } else {		
            camera[3] -= camera[3] / 5;	
        }
        
        camera[0] += (car1.x - camera[0]) / 4;	
        camera[1] += (car1.y - camera[1]) / 4;	
        camera[2] += (car1.z - camera[2]) / 4;	
        camera[4] += (car1.rotX - camera[4]) / 4;
        
        camGG.setTranslation(car1.x, car1.y, car1.z);
//        camGG.setTranslation(camera[0], camera[1], camera[2]+200);
        camGG.setOrientation(car1.rotZ+180, 0, 0, 1);
        
        camGG.postRotate(camera[4]+60, 1, 0, 0);
        
        camG.setOrientation(camera[3], 0, 0, 1);
        camG.setTranslation(0, 0, 200);
    }

    protected void keyPressed(int keyCode) {		// zpracovani stisku klaves
        if (keyCode == KEY_NUM2 || keyCode == getKeyCode(Canvas.UP)) {
            kU = true;	// bylo stisknuto tlacitko "UP"
        } else if (keyCode == KEY_NUM8 || keyCode == getKeyCode(Canvas.DOWN)) {
            kD = true;	// -||- "DOWN"
        } else if (keyCode == KEY_NUM4 || keyCode == getKeyCode(Canvas.LEFT)) {
            kL = true;	// -||- "LEFT"
        } else if (keyCode == KEY_NUM6 || keyCode == getKeyCode(Canvas.RIGHT)) {
            kR = true;	// -||- "RIGHT"
        }
    }

    protected void keyReleased(int keyCode) {		// zpracovani uvolneni klaves
        if (keyCode == KEY_NUM2 || keyCode == getKeyCode(Canvas.UP)) {
            kU = false;	// bylo uvolneno tlacitko "UP"
        } else if (keyCode == KEY_NUM8 || keyCode == getKeyCode(Canvas.DOWN)) {
            kD = false;	// -||- "DOWN"
        } else if (keyCode == KEY_NUM4 || keyCode == getKeyCode(Canvas.LEFT)) {
            kL = false;	// -||- "LEFT"
        } else if (keyCode == KEY_NUM6 || keyCode == getKeyCode(Canvas.RIGHT)) {
            kR = false;	// -||- "RIGHT"
        }
    }
}
