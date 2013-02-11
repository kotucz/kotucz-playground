package rccars;

import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.m3g.Group;
import javax.microedition.m3g.RayIntersection;

/**
 *
 * @author Kotuc
 */
public class RCar {

    final float MAX_SPEED = 20;
    final float MAX_TURN = 15;
    // acceleration
    final float ACC = 2;
    final float width = 14;
    final float lenght = 34;
    final int CAR_M3G_ID = 3; // ID auta
    float speed = 0;
    float turn = 0;
    float x, y, z;
    float vx, vy, vz;
    boolean onground = false;
    float rotZ = 90, rotX = 0;
    Group carG = new Group();
    private RayIntersection ri = new RayIntersection();
    private RCTrack track;

    public RCar(RCTrack track) {
        this.track = track;
        carG.addChild(RCanvas.loadObject("/cars/black_remote.m3g", CAR_M3G_ID));
    }

    /**
     * 
     * @param keys
     */
    public void keys(int keys) {

        boolean kU = (keys & GameCanvas.UP_PRESSED) > 0;
        boolean kD = (keys & GameCanvas.DOWN_PRESSED) > 0;
        boolean kL = (keys & GameCanvas.LEFT_PRESSED) > 0;
        boolean kR = (keys & GameCanvas.RIGHT_PRESSED) > 0;

        if (kL) {
            turn = MAX_TURN;
        } else if (kR) {
            turn = -MAX_TURN;
        } else {
            turn = 0;
        }

        if (kU) {
            speed = Math.min(speed + ACC, MAX_SPEED);
        } else if (kD) {
            speed = -3f;
        } else {
//            speed = 0;
        }

    }

    public void moveCar() {
        float z0 = 0;   // original z of car
        float z1 = 0;	// next z of car

        rotZ += turn*speed/30;
        
//        if (speed >= 0) {   // steering is different when going forward or backward
//            rotZ += turn;
//        } else {
//            rotZ -= turn;
//        }


        if (false) {

            vx = (speed * (float) Math.cos(Math.toRadians(rotZ + 90)));
            vy = (speed * (float) Math.sin(Math.toRadians(rotZ + 90)));

            x += vx;
            y += vy;

        } else {

            vx = (speed * (float) Math.cos(Math.toRadians(rotZ + 90)));
            vy = (speed * (float) Math.sin(Math.toRadians(rotZ + 90)));

            if (track.getTrack().pick(-1, x + vx, y + vy, z + 30, 0, 0, -1, ri)) {	// vysilani paprsku na skupinu s "podlahou" smerem dolu v miste (X a Y) kam se ma car1 posunout
                if (ri.getDistance() < 200) { // 75
                    float[] ray = new float[6]; // field of colliding ray
                    ri.getRay(ray); // ziskani souradnic a smeru kam paprsek letel a kde narazil
//                car1.x = ray[0] + ray[3] * ri.getDistance();   // crash x
//                car1.y = ray[1] + ray[4] * ri.getDistance();   // crash y
                    z1 = ray[2] + ray[5] * ri.getDistance();      // crash z
                    if (speed >= 0) {
//                        car1.rotX = -(float)Math.toDegrees(Mth.atan2((z_po-car1.z), car1.speed));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud jede dopredu)
                    } else {
//                        car1.rotX = -(float)Math.toDegrees(Mth.atan2((car1.z-z_po), Math.abs(car1.speed)));	// vypocet, jak se ma car1 natocit nahoru/dolu podle toho jak prave stoupa/klesa (pokud couva)
                    }
//                    if(car1.rotZ+((car1.z-z_po)*gravitace) < car1.MAX_SPEED && car1.speed+((car1.z-z_po)*gravitace) > -car1.MAX_SPEED) {		// aby auto nezrychlovalo nad maximalni rychlost (popredu i pozadu)
//                        car1.rotZ += (car1.z-z_po)*gravitace;		// zrychlovani/spomalovani auto v zavislosti na kopci z/do ktereho jede
//                    }
                    z0 = z;
                    float inc = (z1 - z0); // stoupani vozidla
                    if (inc > 0) {
                        vz = inc;
                    } else if (inc < 0) {
                        if (inc < -track.GRAV) {
                            vz -= track.GRAV;
                        } else {
                            z += inc;
                            vz = 1;
                        }
                    }
                }

                x += vx;
                y += vy;
                z += vz;
            }

            // applying friction
            if (onground) {
                vx *= 1f - track.FRIC;
                vy *= 1f - track.FRIC;
                speed *= 1f - track.FRIC;
            }

        }   // end non freeride

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

        carG.setTranslation(x, y, z + 3);	// umisteni auta na aktualni pozice
        carG.setOrientation(rotZ, 0, 0, 1);	// nastaveni aktualni rotace auta v ose Z (doleva/doprava)
        carG.postRotate(rotX + 90, 1, 0, 0);		// -||- X (nahoru/dolu)
//        car1.setOrientation(car1.rotX+90, 1, 0, 0);		// -||- X (nahoru/dolu)
    }
    }
