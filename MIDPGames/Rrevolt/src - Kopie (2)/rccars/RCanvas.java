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
    Group zemG = new Group();		// skupina, kde se nachazi "podlaha" (pro kontrolu kolizi)
    Group okrajeG = new Group();	// skupina, kde se nachazi "okraje" (pro kontrolu kolizi)
    Group track = new Group();
    Group roadG = null;
    
    RCar auto1 = new RCar(); //0-X,1-Y,2-Z,3-RotZ,4-RotX,5-Speed,6-MaxSpeed,7-ZatocValue,8-Acc
//    float[] auto1 = {0, 0, 0, 90, 0, 0, 20, 8, 3};	//0-X,1-Y,2-Z,3-RotZ,4-RotX,5-Speed,6-MaxSpeed,7-ZatocValue,8-Acc
    float[] camera = {0, 0, 0, 90, 0};	//0-X,1-Y,2-Z,3-RotZ,4-RotX
//    float gravitace = 1;			// velikost gravitace
    // ID
    int autoID = 3;				// ID auta
    int zemID = 11;					// ID "podlahy"
    int okrajeID = 19;				// ID "vertikalnich casti mapy"
    // KEYS
    boolean kU = false;				// kontrola jestli je zmacknuta klavesa "UP"
    boolean kD = false;				// -||- "DOWN"
    boolean kL = false;				// -||- "LEFT"
    boolean kR = false;				// -||- "RIGHT"
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
            hejbejAutem();			// funkce na pohybovani s autem
            hejbejKamerou();		// -||- kamerou
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
            repaint();				// prekresleni displaye
        }
    }

    public void loadWorld() {			// funkce na nacteni vsech potrebnych m3g souboru do 1 sveta
        try {
            Object3D[] buffer = Loader.load("/mapa_01.m3g");
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] instanceof World) {
                    world = (World) buffer[i];
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
//            car1 = (Group)autoWorld.find(autoID);		// ziskani naseho auta
//            autoWorld.removeChild(car1);				// odstraneni auta z "jeho sveta"
//            car1G.addChild(car1);						// pridani modelu auta do pomocne skupiny auta
////            car1.preRotate(90, 0, 0, 0);
//            world.addChild(car1G);						// vlozeni skupiny s autem do hlavniho sveta
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
        car1G = loadObject("/black_remote.m3g", autoID);
        world.addChild(car1G);

        trackAdd("/lvl_celek.m3g", 0, 0, 0);

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
//            System.exit(-1);
            return null;
        }
    }

    public void loadObjects() {
        cam = world.getActiveCamera(); // ziskani aktivni makery
        world.removeChild(cam); // odstraneni kamery z hl. sveta
        world.addChild(camGG); // vlozeni 2. pomocne skupiny s kamerou do sveta
        camGG.addChild(camG); // vlozeni pomocne skupiny s kamerou do 2. pomocne skupiny s kamerou
        camG.addChild(cam); // vlozeni kamery do pomocne skupiny s kamerou
        Mesh zem = (Mesh) world.find(zemID); // ziskani "podlahy" ze sveta
        world.removeChild(zem);	// odstraneni "podlahy" ze sveta
        zemG.addChild(zem); // vlozeni "podlahy" do pomocne skupiny (pro kontrolu kolizi)
//        world.addChild(zemG);	// vlozeni pomocne skupiny s "podlahou" do sveta
        Mesh okraje = (Mesh) world.find(okrajeID); // ziskani "okraju" ze sveta
        world.removeChild(okraje); // odstraneni "okraju" ze sveta
        okrajeG.addChild(okraje); // vlozeni "okraju" do pomocne skupiny (pro kontrolu kolizi)
//        world.addChild(okrajeG); // vlozeni pomocne skupiny s "okraji" do sveta
        cam.setPerspective(45.0f, (float) getWidth() / (float) getHeight(), 0.1f, 3000);
    }

    public void paint(Graphics g) {			// vykreslovani cele sceny
        g3d = Graphics3D.getInstance();
        try {
            g3d.bindTarget(g, true, 0);
            g3d.render(world);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            g3d.releaseTarget();
        }
    }

    private void hejbejAutem() {			// funkce na pohyb s autem
        float z0 = 0;			// puvodni pozice auta
        float z1 = 0;				// nasledujici pozice auta
        if (auto1.speed >= 0) {				// podminka starajici se o to, aby car1 spravne atacelo kdyz jede dopredu nebo dozadu
            if (kL == true) {				// zataceni doleva
                auto1.rotZ += auto1.turn;
                camera[3] -= auto1.turn;
            } else if (kR == true) {			// zataceni doprava
                auto1.rotZ -= auto1.turn;
                camera[3] += auto1.turn;
            }
        } else {							// kdyz jede car1 pospatku
            if (kL == true) {				// zataceni doleva
                auto1.rotZ -= auto1.turn;
                camera[3] += auto1.turn;
            } else if (kR == true) {			// zataceni doprava
                auto1.rotZ += auto1.turn;
                camera[3] -= auto1.turn;
            }
        }
        if (kU == true) {                                // zrychlovani auta (kdyz je aktualni rychlost mensi nez maximalni rychlost)
            auto1.speed = 5f; //= auto1.speed<auto1.maxSpeed?auto1.speed+auto1.acc:auto1.maxSpeed;
        } else if (kD == true) {					// zpomalovani a couvani auta (couva pomalejs nez kdyz jede dopredu)
            auto1.speed = -3f; // auto1.speed>0?auto1.speed-auto1.acc*2:auto1.speed-auto1.acc*0.5f;
        } else {
            auto1.speed = 0;
        }

//        if (freeride) {
//            auto1.vx += -(auto1.speed * (float)Math.cos(Math.toRadians(auto1.rotZ+90)));
//            auto1.vy += -(auto1.speed * (float)Math.sin(Math.toRadians(auto1.rotZ+90)));
//            auto1.x += auto1.vx;
//            auto1.y += auto1.vy;
//        } else {
//
        auto1.vx += -(auto1.speed * (float) Math.cos(Math.toRadians(auto1.rotZ + 90)));
        auto1.vy += -(auto1.speed * (float) Math.sin(Math.toRadians(auto1.rotZ + 90)));

        if (track.pick(-1, auto1.x + auto1.vx, auto1.y + auto1.vy, auto1.z + 30, 0, 0, -1, ri)) {	// vysilani paprsku na skupinu s "podlahou" smerem dolu v miste (X a Y) kam se ma car1 posunout
            if (ri.getDistance() < 200) { // 75
                float[] ray = new float[6]; // field of colliding ray
                ri.getRay(ray); // ziskani souradnic a smeru kam paprsek letel a kde narazil
//                auto1.x = ray[0] + ray[3] * ri.getDistance();   // crash x
//                auto1.y = ray[1] + ray[4] * ri.getDistance();   // crash y
                z1 = ray[2] + ray[5] * ri.getDistance();      // crash z
                if (auto1.speed >= 0) {
//                        auto1.rotX = -(float)Math.toDegrees(Mth.atan2((z_po-auto1.z), auto1.speed));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud jede dopredu)
                } else {
//                        auto1.rotX = -(float)Math.toDegrees(Mth.atan2((auto1.z-z_po), Math.abs(auto1.speed)));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud couva)
                }
                //             if(auto1.rotZ+((auto1.z-z_po)*gravitace) < auto1.maxSpeed && auto1.speed+((auto1.z-z_po)*gravitace) > -auto1.maxSpeed) {		// aby auto nezrychlovalo nad maximalni rychlost (popredu i pozadu)
                //                 auto1.rotZ += (auto1.z-z_po)*gravitace;		// zrychlovani/spomalovani auto v zavislosti na kopci z/do ktereho jede
                //             }
                z0 = auto1.z;
                float inc = (z1 - z0); // stoupani vozidla
                if (inc > 0) {
                    auto1.vz = inc;
                } else if (inc < 0) {
                    if (inc < -grav) {
                        auto1.vz -= grav;
                    } else {
                        auto1.z += inc;
                        auto1.vz = 1;
                    }
                }
            }

            auto1.x += auto1.vx;
            auto1.y += auto1.vy;

            auto1.z += auto1.vz;



        }

        auto1.vx *= 1f - fric;
        auto1.vy *= 1f - fric;

//        }

//        //////////////////////////
//        float dirZ = z0-z_po;
//        float[] riDistance = {auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8]};	//	+50°, +30°, +10°, -10°, -30°, -50°	(zleva doprava)
//        int smallestRI = 0;
//        if(auto1[5] >= 0) {
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+140)), -(float)Math.sin(Math.toRadians(auto1[3]+140)), dirZ, ri)) {
//                riDistance[0] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+120)), -(float)Math.sin(Math.toRadians(auto1[3]+120)), dirZ, ri)) {
//                riDistance[1] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+100)), -(float)Math.sin(Math.toRadians(auto1[3]+100)), dirZ, ri)) {
//                riDistance[2] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+80)), -(float)Math.sin(Math.toRadians(auto1[3]+80)), dirZ, ri)) {
//                riDistance[3] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+60)), -(float)Math.sin(Math.toRadians(auto1[3]+60)), dirZ, ri)) {
//                riDistance[4] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+40)), -(float)Math.sin(Math.toRadians(auto1[3]+40)), dirZ, ri)) {
//                riDistance[5] = ri.getDistance();
//            }
//        } else {
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+140)), (float)Math.sin(Math.toRadians(auto1[3]+140)), dirZ, ri)) {
//                riDistance[0] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+120)), (float)Math.sin(Math.toRadians(auto1[3]+120)), dirZ, ri)) {
//                riDistance[1] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+100)), (float)Math.sin(Math.toRadians(auto1[3]+100)), dirZ, ri)) {
//                riDistance[2] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+80)), (float)Math.sin(Math.toRadians(auto1[3]+80)), dirZ, ri)) {
//                riDistance[3] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+60)), (float)Math.sin(Math.toRadians(auto1[3]+60)), dirZ, ri)) {
//                riDistance[4] = ri.getDistance();
//            }
//            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+40)), (float)Math.sin(Math.toRadians(auto1[3]+40)), dirZ, ri)) {
//                riDistance[5] = ri.getDistance();
//            }
//        }
//        for(int i = 0; i < riDistance.length; i++) {
//            if(riDistance[i] < riDistance[smallestRI]) {
//                smallestRI = i;
//            }
//        }
//        if(riDistance[smallestRI] < Math.abs(auto1[5])) {
//            switch(smallestRI) {
//                case 0:
//                    auto1[3] -= 50;
//                    camera[3] += 50;
//                    break;
//                case 1:
//                    auto1[3] -= 30;
//                    camera[3] += 30;
//                    break;
//                case 2:
//                    auto1[3] -= 10;
//                    camera[3] += 10;
//                    break;
//                case 3:
//                    auto1[3] += 10;
//                    camera[3] -= 10;
//                    break;
//                case 4:
//                    auto1[3] += 30;
//                    camera[3] -= 30;
//                    break;
//                case 5:
//                    auto1[3] += 50;
//                    camera[3] -= 50;
//                    break;
//            }
//            auto1[5] /= 3;
//                        /*vygenerujCastice(auto1[0], auto1[1], auto1[2], 20, 0, 0, 5, 0, 0, -0.5f, 1, 1, 1, 50, 20, 0, 255, 0);
//                        pohybujCasticema = true;*/
//        }
//

        car1G.setTranslation(auto1.x, auto1.y, auto1.z + 3);	// umisteni auta na aktualni pozice
        car1G.setOrientation(auto1.rotZ + 180, 0, 0, 1);	// nastaveni aktualni rotace auta v ose Z (doleva/doprava)
        car1G.postRotate(auto1.rotX + 90, 1, 0, 0);		// -||- X (nahoru/dolu)
//        car1.setOrientation(auto1.rotX+90, 1, 0, 0);		// -||- X (nahoru/dolu)
    }

    private void hejbejKamerou() {					// funkce na pohyb s kamerou

        //0-X,1-Y,2-Z,3-RotZ,4-RotX

        if (auto1.speed < -3) {		// kdyz auto jede pozadu
            camera[3] += (180 - camera[3]) / 5;				// plynule vyrovnani rotace kamery podle osy Z (doleva/doprava) pred auto1
        } else {				// popredu nebo "malo" pozadu (rychlost je vice jak -3)
            camera[3] -= camera[3] / 5;				// plynule vyrovnani rotace kamery podle osy Z (doleva/doprava) za auto1
        }
        camera[0] += (auto1.x - camera[0]) / 4;	// plynuly pohyb kamery za autem (v ose X)
        camera[1] += (auto1.y - camera[1]) / 4;	// plynuly pohyb kamery za autem (v ose Y)
        camera[2] += (auto1.z - camera[2]) / 4;	// plynuly pohyb kamery za autem (v ose Z)
        camera[4] += (auto1.rotX - camera[4]) / 4;	// plynule vyrovnani rotace kamery podle osy X (nahoru/dolu)

        camGG.setTranslation(camera[0], camera[1], camera[2]);	// umisteni 2. pomocne skupiny s kamerou na souradnice auta
        camGG.setOrientation(auto1.rotZ, 0, 0, 1);	// natoceni 2. pomocne skupiny s kamerou podle osy Z (doleva/doprava) jako car1
        camG.setOrientation(camera[3], 0, 0, 1);	// natoceni pomocne kamery s kamerou v ose Z
        camG.preRotate(camera[4], 1, 0, 0);			// natoceni pomocne kamery s kamerou v ose X
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
