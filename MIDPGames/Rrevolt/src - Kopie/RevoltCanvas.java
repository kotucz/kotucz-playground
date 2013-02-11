import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import java.util.Random;
import javax.microedition.m3g.*;

public class RevoltCanvas extends Canvas implements Runnable {
    Thread myThread = null;
    boolean running = false;
    
    Graphics3D g3d = null;
    World world = null;				// "hlavni" svet
    
    Camera cam = null;				// kamera snimajici car1 z pohledu 3. osoby
    Group camG = new Group();		// pomocna skupina pro kameru (-> snadnejsi manipulace)
    Group camGG = new Group();		// dalsi pomocna skupina pro kameru
    Group car1 = null;				// nase auto
    Group car1G = new Group();		// pomocna skupina s nasim autem
    Group zemG = new Group();		// skupina, kde se nachazi "podlaha" (pro kontrolu kolizi)
    Group okrajeG = new Group();	// skupina, kde se nachazi "okraje" (pro kontrolu kolizi)
    
    float sirkaAuta = 14;
    float delkaAuta = 34;
    
    float[] auto1 = {0, 0, 0, 90, 0, 0, 20, 8, 3};	//0-X,1-Y,2-Z,3-RotZ,4-RotX,5-Speed,6-MaxSpeed,7-ZatocValue,8-Acc
    float[] camera = {0, 0, 0, 90, 0};	//0-X,1-Y,2-Z,3-RotZ,4-RotX
    
    float gravitace = 1;			// velikost gravitace
    
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
    
    public RevoltCanvas() {
        setFullScreenMode(true);	// nastaveni na fullscreen
        loadWorld();				// funkce na nacteni vsech potrebnych m3g souboru do 1 sveta
        loadObjects();				// funkce na nacteni atp. jednotlivych objektu
        
        myThread = new Thread(this);
        running = true;
        myThread.start();
    }
    
    public void run() {
        while(running) {
            hejbejAutem();			// funkce na pohybovani s autem
            hejbejKamerou();		// -||- kamerou
            try {
                Thread.sleep(100);
            } catch(Exception e) {
                System.out.println(e);
            }
            repaint();				// prekresleni displaye
        }
    }
    
    public void loadWorld() {			// funkce na nacteni vsech potrebnych m3g souboru do 1 sveta
        try {
            Object3D[] buffer = Loader.load("/mapa_01.m3g");
            for(int i = 0; i < buffer.length; i++) {
                if(buffer[i] instanceof World) {
                    world = (World)buffer[i];
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        try {
            Object3D[] buffer = Loader.load("/black_remote.m3g");
            World autoWorld = null;						// docasny svet s modelem auta
            for(int i = 0; i < buffer.length; i++) {
                if(buffer[i] instanceof World) {
                    autoWorld = (World)buffer[i];
                    break;
                }
            }
            car1 = (Group)autoWorld.find(autoID);		// ziskani naseho auta
            autoWorld.removeChild(car1);				// odstraneni auta z "jeho sveta"
            car1G.addChild(car1);						// pridani modelu auta do pomocne skupiny auta
//            car1.preRotate(90, 0, 0, 0);
            world.addChild(car1G);						// vlozeni skupiny s autem do hlavniho sveta
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void loadObjects() {
        cam = world.getActiveCamera();		// ziskani aktivni makery
        world.removeChild(cam);				// odstraneni kamery z hl. sveta
        world.addChild(camGG);				// vlozeni 2. pomocne skupiny s kamerou do sveta
        camGG.addChild(camG);				// vlozeni pomocne skupiny s kamerou do 2. pomocne skupiny s kamerou
        camG.addChild(cam);					// vlozeni kamery do pomocne skupiny s kamerou
        Mesh zem = (Mesh)world.find(zemID);	// ziskani "podlahy" ze sveta
        world.removeChild(zem);				// odstraneni "podlahy" ze sveta
        zemG.addChild(zem);					// vlozeni "podlahy" do pomocne skupiny (pro kontrolu kolizi)
        world.addChild(zemG);				// vlozeni pomocne skupiny s "podlahou" do sveta
        Mesh okraje = (Mesh)world.find(okrajeID);	// ziskani "okraju" ze sveta
        world.removeChild(okraje);					// odstraneni "okraju" ze sveta
        okrajeG.addChild(okraje);					// vlozeni "okraju" do pomocne skupiny (pro kontrolu kolizi)
        world.addChild(okrajeG);					// vlozeni pomocne skupiny s "okraji" do sveta
        cam.setPerspective(45.0f, (float)getWidth()/(float)getHeight(), 0.1f, 3000);
    }
    
    public void paint(Graphics g) {			// vykreslovani cele sceny
        g3d = Graphics3D.getInstance();
        try {
            g3d.bindTarget(g, true, 0);
            g3d.render(world);
        } catch(Exception e) {
            System.out.println(e);
        } finally {
            g3d.releaseTarget();
        }
    }
    
    private void hejbejAutem() {			// funkce na pohyb s autem
        float z_pred = 0;			// puvodni pozice auta
        float z_po = 0;				// nasledujici pozice auta
        if(auto1[5] >= 0) {				// podminka starajici se o to, aby car1 spravne atacelo kdyz jede dopredu nebo dozadu
            if(kL == true ) {				// zataceni doleva
                auto1[3] += auto1[7];
                camera[3] -= auto1[7];
            } else if(kR == true) {			// zataceni doprava
                auto1[3] -= auto1[7];
                camera[3] += auto1[7];
            }
        } else {							// kdyz jede car1 pospatku
            if(kL == true ) {				// zataceni doleva
                auto1[3] -= auto1[7];
                camera[3] += auto1[7];
            } else if(kR == true) {			// zataceni doprava
                auto1[3] += auto1[7];
                camera[3] -= auto1[7];
            }
        }
        if(kU == true) {					// zrychlovani auta (kdyz je aktualni rychlost mensi nez maximalni rychlost)
            auto1[5] = auto1[5]<auto1[6]?auto1[5]+auto1[8]:auto1[6];
        }
        if(kD == true) {					// zpomalovani a couvani auta (couva pomalejs nez kdyz jede dopredu)
            auto1[5] = auto1[5]>0?auto1[5]-auto1[8]*2:auto1[5]-auto1[8]*0.5f;
        }
        
        if(zemG.pick(-1, auto1[0]-(auto1[5] * (float)Math.cos(Math.toRadians(auto1[3]+90))), auto1[1]-(auto1[5] * (float)Math.sin(Math.toRadians(auto1[3]+90))), auto1[2]+10, 0, 0, -1, ri)) {	// vysilani paprsku na skupinu s "podlahou" smerem dolu v miste (X a Y) kam se ma car1 posunout
            if(ri.getDistance() <= 75) {
                float[] ray = new float[6];						// pole potrebne k zjisteni kam "narazil" kolizni paprsek
                ri.getRay(ray);									// ziskani souradnic a smeru kam paprsek letel a kde narazil
                auto1[0] = ray[0] + ray[3] * ri.getDistance();		// vypocet souradnice X kam presne narazil paprsek
                auto1[1] = ray[1] + ray[4] * ri.getDistance();		// -||-Y -||-
                z_po = ray[2] + ray[5] * ri.getDistance();	// -||- Z -||-
                if(auto1[5] >= 0) {
                    auto1[4] = -(float)Math.toDegrees(Mth.atan2((z_po-auto1[2]), auto1[5]));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud jede dopredu)
                } else {
                    auto1[4] = -(float)Math.toDegrees(Mth.atan2((auto1[2]-z_po), Math.abs(auto1[5])));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud couva)
                }
                if(auto1[5]+((auto1[2]-z_po)*gravitace) < auto1[6] && auto1[5]+((auto1[2]-z_po)*gravitace) > -auto1[6]) {		// aby auto nezrychlovalo nad maximalni rychlost (popredu i pozadu)
                    auto1[5] += (auto1[2]-z_po)*gravitace;		// zrychlovani/spomalovani auto v zavislosti na kopci z/do ktereho jede
                }
                z_pred = auto1[2];
                auto1[2] = z_po;			// nastaveni pozice auta v ose Z na misto kde narazil paprsek
            }
        }
        //////////////////////////
        float dirZ = z_pred-z_po;
        float[] riDistance = {auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8], auto1[6]+auto1[8]};	//	+50°, +30°, +10°, -10°, -30°, -50°	(zleva doprava)
        int smallestRI = 0;
        if(auto1[5] >= 0) {
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+140)), -(float)Math.sin(Math.toRadians(auto1[3]+140)), dirZ, ri)) {
                riDistance[0] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+120)), -(float)Math.sin(Math.toRadians(auto1[3]+120)), dirZ, ri)) {
                riDistance[1] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+100)), -(float)Math.sin(Math.toRadians(auto1[3]+100)), dirZ, ri)) {
                riDistance[2] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+80)), -(float)Math.sin(Math.toRadians(auto1[3]+80)), dirZ, ri)) {
                riDistance[3] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+60)), -(float)Math.sin(Math.toRadians(auto1[3]+60)), dirZ, ri)) {
                riDistance[4] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, -(float)Math.cos(Math.toRadians(auto1[3]+40)), -(float)Math.sin(Math.toRadians(auto1[3]+40)), dirZ, ri)) {
                riDistance[5] = ri.getDistance();
            }
        } else {
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+140)), (float)Math.sin(Math.toRadians(auto1[3]+140)), dirZ, ri)) {
                riDistance[0] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+120)), (float)Math.sin(Math.toRadians(auto1[3]+120)), dirZ, ri)) {
                riDistance[1] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+100)), (float)Math.sin(Math.toRadians(auto1[3]+100)), dirZ, ri)) {
                riDistance[2] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+80)), (float)Math.sin(Math.toRadians(auto1[3]+80)), dirZ, ri)) {
                riDistance[3] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+60)), (float)Math.sin(Math.toRadians(auto1[3]+60)), dirZ, ri)) {
                riDistance[4] = ri.getDistance();
            }
            if(okrajeG.pick(-1, auto1[0], auto1[1], auto1[2]+2, (float)Math.cos(Math.toRadians(auto1[3]+40)), (float)Math.sin(Math.toRadians(auto1[3]+40)), dirZ, ri)) {
                riDistance[5] = ri.getDistance();
            }
        }
        for(int i = 0; i < riDistance.length; i++) {
            if(riDistance[i] < riDistance[smallestRI]) {
                smallestRI = i;
            }
        }
        if(riDistance[smallestRI] < Math.abs(auto1[5])) {
            switch(smallestRI) {
                case 0:
                    auto1[3] -= 50;
                    camera[3] += 50;
                    break;
                case 1:
                    auto1[3] -= 30;
                    camera[3] += 30;
                    break;
                case 2:
                    auto1[3] -= 10;
                    camera[3] += 10;
                    break;
                case 3:
                    auto1[3] += 10;
                    camera[3] -= 10;
                    break;
                case 4:
                    auto1[3] += 30;
                    camera[3] -= 30;
                    break;
                case 5:
                    auto1[3] += 50;
                    camera[3] -= 50;
                    break;
            }
            auto1[5] /= 3;
                        /*vygenerujCastice(auto1[0], auto1[1], auto1[2], 20, 0, 0, 5, 0, 0, -0.5f, 1, 1, 1, 50, 20, 0, 255, 0);
                        pohybujCasticema = true;*/
        }
        
        
        car1G.setTranslation(auto1[0], auto1[1], auto1[2]+3);	// umisteni auta na aktualni pozice
        car1G.setOrientation(auto1[3]+180, 0, 0, 1);	// nastaveni aktualni rotace auta v ose Z (doleva/doprava)
        car1.setOrientation(auto1[4]+90, 1, 0, 0);		// -||- X (nahoru/dolu)
    }
    
    private void hejbejKamerou() {					// funkce na pohyb s kamerou
        
        //0-X,1-Y,2-Z,3-RotZ,4-RotX
        
        if(auto1[5] < -3) {		// kdyz auto jede pozadu
            camera[3] += (180-camera[3])/5;				// plynule vyrovnani rotace kamery podle osy Z (doleva/doprava) pred auto1
        } else {				// popredu nebo "malo" pozadu (rychlost je vice jak -3)
            camera[3] -= camera[3]/5;				// plynule vyrovnani rotace kamery podle osy Z (doleva/doprava) za auto1
        }
        camera[0] += (auto1[0]-camera[0])/4;	// plynuly pohyb kamery za autem (v ose X)
        camera[1] += (auto1[1]-camera[1])/4;	// plynuly pohyb kamery za autem (v ose Y)
        camera[2] += (auto1[2]-camera[2])/4;	// plynuly pohyb kamery za autem (v ose Z)
        camera[4] += (auto1[4]-camera[4])/4;	// plynule vyrovnani rotace kamery podle osy X (nahoru/dolu)
        
        camGG.setTranslation(camera[0], camera[1], camera[2]);	// umisteni 2. pomocne skupiny s kamerou na souradnice auta
        camGG.setOrientation(auto1[3], 0, 0, 1);	// natoceni 2. pomocne skupiny s kamerou podle osy Z (doleva/doprava) jako car1
        camG.setOrientation(camera[3], 0, 0, 1);	// natoceni pomocne kamery s kamerou v ose Z
        camG.preRotate(camera[4], 1, 0, 0);			// natoceni pomocne kamery s kamerou v ose X
    }
    
    protected void keyPressed(int keyCode) {		// zpracovani stisku klaves
        if(keyCode == KEY_NUM2 || keyCode == getKeyCode(Canvas.UP)) {
            kU = true;	// bylo stisknuto tlacitko "UP"
        } else if(keyCode == KEY_NUM8 || keyCode == getKeyCode(Canvas.DOWN)) {
            kD = true;	// -||- "DOWN"
        } else if(keyCode == KEY_NUM4 || keyCode == getKeyCode(Canvas.LEFT)) {
            kL = true;	// -||- "LEFT"
        } else if(keyCode == KEY_NUM6 || keyCode == getKeyCode(Canvas.RIGHT)) {
            kR = true;	// -||- "RIGHT"
        }
    }
    
    protected void keyReleased(int keyCode) {		// zpracovani uvolneni klaves
        if(keyCode == KEY_NUM2 || keyCode == getKeyCode(Canvas.UP)) {
            kU = false;	// bylo uvolneno tlacitko "UP"
        } else if(keyCode == KEY_NUM8 || keyCode == getKeyCode(Canvas.DOWN)) {
            kD = false;	// -||- "DOWN"
        } else if(keyCode == KEY_NUM4 || keyCode == getKeyCode(Canvas.LEFT)) {
            kL = false;	// -||- "LEFT"
        } else if(keyCode == KEY_NUM6 || keyCode == getKeyCode(Canvas.RIGHT)) {
            kR = false;	// -||- "RIGHT"
        }
    }
}