/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package kotucz.village.game;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.BasicShadowRenderer;
import kotucz.village.build.Building;
import kotucz.village.character.Avatar;
import kotucz.village.common.*;
import kotucz.village.cubes.Cube;
import kotucz.village.cubes.CubeType;
import kotucz.village.cubes.Pos3D;
import kotucz.village.pipes.Animal;
import kotucz.village.pipes.SimplePipe;
import kotucz.village.tiles.*;
import kotucz.village.transport.*;

import java.util.*;

/**
 * @author kotucz
 */
public class MyGame extends SimpleApplication {

    public static final boolean CONFIG_USE_SHADOWS = true;
    public static final int NUM_CARS = 00;

    public static final String COMMON_MAT_DEFS_MISC_UNSHADED_J3MD = "Common/MatDefs/Misc/Unshaded.j3md";
    public static final String COMMON_MAT_DEFS_LIGHT_LIGHTING_J3MD = "Common/MatDefs/Light/Lighting.j3md";
    private BulletAppState bulletAppState;

    public static final Vector3f UP = new Vector3f(0, 0, 1);
    //    static float bLength = 1f;
//    static float bWidth = 1f;
//    static float bHeight = 1f;
    public static final String LEFT_CLICK = "LeftClick";
    public static final String SHOOT = "shoot";
    public static final String GC = "gc";
    public static final String BUILD_BUILDING = "buildBuilding";
    public static final String BUILD_ROAD = "buildRoad";
    public static final String BUILD_CAR = "buildCAR";
    BasicShadowRenderer bsr;

    private BitmapText textAction;

    final Entities entities = new Entities();

    Geometry mark;
    Node selectables;
    final LinearGrid lingrid = new LinearGrid(16, 16);
//    final LinearGrid lingrid = new LinearGrid(64, 64);
//    final LinearGrid lingrid = new LinearGrid(128, 128);
    // beware larger maps are buggy due to short ints in MeshTileGrid

    final Random random = new Random();

    private TileGrid roadTileGrid;
    private TileGrid arrowsTileGrid;
    private TileGrid selectTileGrid;
    private AbstractSetGrid trafficGrid;


    public GameMap map;
    private BitmapText textSelection;
    private Player player;
    private Material spriteMaterial;
    private RoadBuilder roadBuilder;
    private Dir3D dir3D;

    private Avatar hero;
    final Modeler modeler = new Modeler(this);

    public static void main(String args[]) {
        MyGame f = new MyGame();
        f.start();
    }


    @Override
    public void simpleInitApp() {

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        getPhysicsSpace().setGravity(new Vector3f(0, 0, -9.81f));

        assetManager.registerLocator("assets/", FileLocator.class);


        selectables = new Node("Shootables");
        rootNode.attachChild(selectables);


        map = new GameMap(this, rootNode);

        modeler.initMaterial();
        initGrids();
        initCrossHairs();

        initKeys();       // load custom key mappings
        initMark();       // a red sphere to mark the hit


        roadBuilder = new RoadBuilder(map, map.pnet);

        roadBuilder.findPath(new Pos(2, 2), new Pos(8, 14));

        player = new Player("Kotuc", null, 10000);


        for (int i = 0; i < NUM_CARS; i++) {
            putCar(new Vehicle(player, Vehicle.Type.SKODA120, map.pnet.randomRoadPoint(random), modeler.matVehicles));
        }


        {
            // setup camera
            getFlyByCamera().setMoveSpeed(50);
            getFlyByCamera().setUpVector(UP);

            this.cam.setLocation(new Vector3f(8, -8, 8f));
            cam.lookAt(new Vector3f(8, 8, 0), UP);
            cam.setFrustumFar(500);
        }
        {
            createCubeOnPos3D(new Pos3D(5, 3, 0));
        }
        {

            for (int i = 0; i < 5; i++) {

                putMineral(new Mineral(GoodsType.values()[random.nextInt(5)], new Vector3f(5 + random.nextFloat() * 1, 5 + random.nextFloat(), random.nextFloat() * 5 + 2), Modeler.matResources));

            }


        }

        {
            Conveyor conveyor = new Conveyor(new Vector3f(5.5f, 5.5f, 0.5f), Modeler.matResources);
            conveyor.setDir(new Vector3f(1, 0, 0));
            putConveyor(conveyor);
        }
        {
            Conveyor conveyor = new Conveyor(new Vector3f(6.5f, 5.5f, 0.5f), Modeler.matResources);
            conveyor.setDir(new Vector3f(0, 1, 0));
            putConveyor(conveyor);
        }
        {
            Conveyor conveyor = new Conveyor(new Vector3f(6.5f, 6.5f, 0.5f), Modeler.matResources);
            conveyor.setDir(new Vector3f(-1, 0, 0));
            putConveyor(conveyor);
        }
        {
            Conveyor conveyor = new Conveyor(new Vector3f(5.5f, 6.5f, 0.5f), modeler.matResources);
            conveyor.setDir(new Vector3f(0, -1, 0));
            putConveyor(conveyor);
        }

        {
            hero = new Avatar(modeler.matVehicles, new Vector3f(10, 10, 1));
            putCharacter(hero);
        }

        {
            SimplePipe simplePipe = new SimplePipe(new Vector3f(5, 5, 2f), new Vector3f(6, 6, 2), modeler.matPipes);
            rootNode.attachChild(simplePipe.getSpatial());
            getPhysicsSpace().add(simplePipe.getPhysics());

            SimplePipe simplePipe2 = new SimplePipe(new Vector3f(4, 2, 5f), new Vector3f(6, 4, 2), modeler.matPipes);
            rootNode.attachChild(simplePipe2.getSpatial());

        }

        {
            Animal simplePipe = new Animal(modeler, getPhysicsSpace());
            rootNode.attachChild(simplePipe.getNode());
//            getPhysicsSpace().add(simplePipe.getPhysics());

//            SimplePipe simplePipe2 = new SimplePipe(new Vector3f(4, 2, 5f), new Vector3f(6, 4, 2), modeler.matPipes);
//            rootNode.attachChild(simplePipe2.getSpatial());

        }

        initInputs();

        {
            rootNode.setShadowMode(ShadowMode.Off);
            bsr = new BasicShadowRenderer(assetManager, 1024);
            bsr.setDirection(new Vector3f(-1, 1, -2).normalizeLocal());
            viewPort.addProcessor(bsr);
        }
        {
            AmbientLight ambientLight = new AmbientLight();
            ambientLight.setColor(ColorRGBA.White);
            rootNode.addLight(ambientLight);
        }
        {
            /** Must add a light to make the lit object visible! */
            DirectionalLight sun = new DirectionalLight();
            sun.setDirection(new Vector3f(-1, 1, -2).normalizeLocal());
            sun.setColor(ColorRGBA.White);
            rootNode.addLight(sun);
        }
        {
            // add floor
//        Plane plane = new Plane();
//        plane.setOriginNormal(new Vector3f(0, 0.25f, 0), Vector3f.UNIT_Y);
//        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));
            RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(lingrid.getSizeX() / 2f, lingrid.getSizeY() / 2f, 0.25f)), 0);
            Node node = new Node();
            node.addControl(control);
            control.setPhysicsLocation(new Vector3f(lingrid.getSizeX() / 2f, lingrid.getSizeY() / 2f, -0.25f));


            rootNode.attachChild(node);
            getPhysicsSpace().add(node);
        }
    }

    private void putConveyor(Conveyor conveyor) {
        rootNode.attachChild(conveyor.getSpatial());
        getPhysicsSpace().add(conveyor.getSpatial());
    }


    private void putMineral(Mineral mineral1) {
        Mineral mineral = mineral1;
        rootNode.attachChild(mineral.getSpatial());
        getPhysicsSpace().add(mineral.getSpatial());
    }

    private void putCube(Cube cube) {
        selectables.attachChild(cube.getSpatial());
        getPhysicsSpace().add(cube.getSpatial());
        entities.put(cube.getId(), cube);
    }


    private void putCharacter(Avatar c) {
        selectables.attachChild(c.getSpatial());
        getPhysicsSpace().add(c.getPhysics());
        entities.put(c.getId(), c);
    }

    private void putBuilding(Building building) {
        selectables.attachChild(building.getNode());
        entities.put(building.getId(), building);
        map.buildings.putBuilding(building);
    }


    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    private void initInputs() {
        inputManager.addMapping(SHOOT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, SHOOT);

        inputManager.addMapping(GC, new KeyTrigger(KeyInput.KEY_X));
        inputManager.addListener(actionListener, GC);

        inputManager.addMapping(BUILD_BUILDING, new KeyTrigger(KeyInput.KEY_B));
        inputManager.addListener(actionListener, BUILD_BUILDING);

        inputManager.addMapping(BUILD_ROAD, new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(actionListener, BUILD_ROAD);

        inputManager.addMapping(BUILD_CAR, new KeyTrigger(KeyInput.KEY_V));
        inputManager.addListener(actionListener, BUILD_CAR);

        inputManager.addMapping("CharUp", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("CharDown", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_L));

        inputManager.addListener(analogListener, "CharLeft");
        inputManager.addListener(analogListener, "CharRight");
        inputManager.addListener(analogListener, "CharUp");
        inputManager.addListener(analogListener, "CharDown");



    }

    private void putCar(Vehicle vehicle) {
        Vehicle car = vehicle;
        car.setBehavior(new VehicleBehavior(car, map.pnet));
        selectables.attachChild(car.getNode());
        map.traffic.putVehicle(car);
        getPhysicsSpace().add(car.getNode().getChild(0));
    }

    public void createCubeOnPos3D(Pos3D pos) {
        Vector3f vector3f = new Vector3f(pos.x + 0.5f, pos.y + 0.5f, pos.z + 0.5f);
        putCube(new Cube(CubeType.values()[random.nextInt(2)], pos, vector3f, Modeler.matResources));

    }


    float dropper;
    float nextDrop;

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);



        textSelection.setText("No selection");

        dropper += tpf;
        if (dropper > nextDrop) {
            putMineral(new Mineral(GoodsType.values()[random.nextInt(5)], new Vector3f(5 + random.nextFloat() * 1, 5 + random.nextFloat(), random.nextFloat() * 5 + 2), Modeler.matResources));
            nextDrop += 3; // drop interval
        }

        selectTileGrid.setAllTo(TexturesSelect.VOID);

//        textAction.setText("Jelito " + System.currentTimeMillis());

        textAction.setText("Jelito " + System.currentTimeMillis() + " Action: " + currentAction);


//        textSelection.setText("Jelito " + System.currentTimeMillis() + " Action: " + currentAction);

//        currentAction = new SelectAction();


        map.traffic.update(tpf);

        currentAction.updateGui();
        trafficGrid.updateGrid();
        selectTileGrid.updateTexture();


        hero.setDir(new Vector3f(1f*avatarwalkx, 1f*avatarwalky, 0));

        avatarwalkx = 0;
        avatarwalky = 0;

    }

    transient float avatarwalkx = 0;
    transient float avatarwalky = 0;


    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
//            if (name.equals("shoot") && !keyPressed) {
//                Geometry bulletg = new Geometry("bullet", bullet);
//                bulletg.setMaterial(mat16);
//                bulletg.setShadowMode(ShadowMode.CastAndReceive);
//                bulletg.setLocalTranslation(cam.getLocation());
//
////                SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(0.4f);
////                RigidBodyControl bulletNode = new BombControl(assetManager, bulletCollisionShape, 1);
////                RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, 1);
////                bulletNode.setLinearVelocity(cam.getDirection().mult(25));
////                bulletg.addControl(bulletNode);
////                rootNode.attachChild(bulletg);
////                getPhysicsSpace().add(bulletNode);
//            }
            if (name.equals(BUILD_BUILDING) && !keyPressed) {
                currentAction = new BuildAction();
            }
            if (name.equals(BUILD_CAR) && !keyPressed) {
                currentAction = new DropCarAction();
            }
            if (name.equals(BUILD_ROAD) && !keyPressed) {
                currentAction = new BuildRoadAction();
            }
            if (name.equals(GC) && !keyPressed) {
                System.gc();
            }
            if (name.equals(LEFT_CLICK)) {
                if (!keyPressed) {
                    currentAction.onMouseUp();
                } else {
                    currentAction.onMouseDown();

                }
            }




        }
    };

    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            System.out.println(" "+name +" = "+value);
            if (name.equals("CharLeft")) {
                avatarwalkx -= Math.signum(value);
            } else if (name.equals("CharRight")) {
                avatarwalkx += Math.signum(value);
            } else if (name.equals("CharUp")) {
                avatarwalky += Math.signum(value);
            } else if (name.equals("CharDown")) {
                avatarwalky -= Math.signum(value);
            }

        }
    };


    Object pick() {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        selectables.collideWith(ray, results);
        // 4. Print the results
//        System.out.println("----- Collisions? " + results.size() + "-----");
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint();
            String hit = results.getCollision(i).getGeometry().getName();

//            results.getCollision(i).getGeometry().getMaterial().setColor("Color", ColorRGBA.randomColor());


//            System.out.println("* Collision #" + i);
//            System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");


        }


        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
            // The closest collision point is what was truly hit:
            CollisionResult closest = results.getClosestCollision();
            // Let's interact - we mark the hit with a red dot.
            mark.setLocalTranslation(closest.getContactPoint());
            rootNode.attachChild(mark);

            String kode = closest.getGeometry().getUserData(Entities.ID_KEY);
//                System.out.println("Kode: " + kode);



            final Object pick = entities.find(kode);

            if (kode != null) {
                System.out.println("P "+pick);
            }


            if (pick != null) {
                System.out.println("Pick "+pick);
                if (pick instanceof Cube) {
                    Cube cube = (Cube) pick;
                    Vector3f contactNormal = closest.getContactNormal();
                    dir3D = Dir3D6.valueOfVector(contactNormal);
                    textSelection.setText("" + pick + " " + contactNormal + " " + dir3D);


//                    vehicle.mark(ColorRGBA.randomColor());
//                    selectTileGrid.setTexture(vehicle.requestPos, TexturesSelect.SELECTED);
                    return pick;
                }

                if (pick instanceof Building) {
                    Building building = (Building) pick;
                    textSelection.setText("" + building);
                    building.mark(ColorRGBA.randomColor());
                    selectTileGrid.setTexture(building.getEntrancePos(), TexturesSelect.SELECTED);
                }

                if (pick instanceof Vehicle) {
                    Vehicle vehicle = (Vehicle) pick;

                    textSelection.setText("" + vehicle);
//                    vehicle.mark(ColorRGBA.randomColor());
//                    selectTileGrid.setTexture(vehicle.requestPos, TexturesSelect.SELECTED);
                }

            } else  if ("selgrid".equals(closest.getGeometry().getName())) {
                Vector3f contactPoint = closest.getContactPoint();
                int x = (int) Math.floor(contactPoint.x);
                int y = (int) Math.floor(contactPoint.y);
//                selectTileGrid.setTexture(x, y, 1);
//                selectGrid.add(x, y);
//                selectGrid.updateGrid();
                Pos pos = new Pos(x, y);
//                textSelection.setText("pozice " + pos);
                return pos;
            }

        } else {
            // No hits? Then remove the red mark.
            rootNode.detachChild(mark);
        }
        return null;
    }

    Pos pickPos() {
        Object pick = pick();
        if (pick instanceof Pos) {
            return (Pos) pick;
        } else {
            return null;
        }

    }


    public void initGrids() {

        //final float epsz = 1 / 16f;
        final float epsz = 1 / 1024f;

        float layerZ = 0;

        final Node grids = new Node("floorGrids");
//        grids.setQueueBucket(RenderQueue.Bucket.Sky);
//        grids.setQueueBucket(RenderQueue.Bucket.Transparent);

        {
//            final Geometry geometry = new Geometry("grid", new MeshTileGrid());
//            geometry.setMaterial(matgrass);
//            geometry.setShadowMode(ShadowMode.Receive);
//            geometry.setLocalTranslation(new Vector3f(0, 0, 0.f));
//            this.rootNode.attachChild(geometry);

            Multitexture1 mtex = new Multitexture1(new LinearGrid(4, 4));
            TileGrid tileGrid = new TileGrid(lingrid, modeler.matgrass, this);
            final Geometry geometry = tileGrid.getGeometry();


//            geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
//            geometry.setMaterial(matwtr);
//            geometry.setShadowMode(ShadowMode.Receive);
            for (Tile tile : lingrid) {
                tileGrid.setTexture(tile.pos, mtex.getTex(random.nextInt(16)));
            }
            tileGrid.updateTexture();

//            geometry.setLocalTranslation(new Vector3f(0, 0, layerZ += epsz));
            geometry.setLocalTranslation(new Vector3f(0, 0, 0.1f));
            geometry.setShadowMode(ShadowMode.Receive);
            geometry.setQueueBucket(RenderQueue.Bucket.Sky);

            grids.attachChild(geometry);


        }


        {

//            PathNetwork pnet = new PathNetwork(16, 16);
//            pnet.randomlySelect(40);


//            final Geometry geometry = new Geometry("grid16", new NodedefTileGrid(pnet));
            TileGrid tileGrid = new TileGrid(lingrid, modeler.matwtr, this);
            final Geometry geometry = tileGrid.getGeometry();
            NodeSetGrid watter = new NodeSetGrid(tileGrid);
            watter.set.clear();


            // TODO refactor
            int i1 = random.nextInt(lingrid.getTotalNum() / 4);
            for (int i = 0; i < i1; i++) {
                watter.set.add(lingrid.randomPos(random));
            }

            watter.updateGrid();

            map.water = watter;

//            geometry.setMaterial(matwtr);
//            geometry.setShadowMode(ShadowMode.Receive);
            geometry.setLocalTranslation(new Vector3f(0, 0, layerZ += epsz));
            grids.attachChild(geometry);
        }
        {


            roadTileGrid = new RoadTextureTileGrid(map.pnet, modeler.matroad, this);
            roadTileGrid.updateTexture();
            arrowsTileGrid = new UnidirectionalRoadTileGrid(map.pnet, modeler.matroadarrows, this);
            arrowsTileGrid.updateTexture();
            {
                final Geometry geometry = roadTileGrid.getGeometry();
//            geometry.setMaterial(matroadarrows);
//            geometry.setShadowMode(ShadowMode.Receive);
                geometry.setLocalTranslation(new Vector3f(0, 0, layerZ += epsz));
//            geometry.setQueueBucket(Bucket.Transparent);
                grids.attachChild(geometry);
            }
            {
                final Geometry geometry = arrowsTileGrid.getGeometry();
//            geometry.setMaterial(matroadarrows);
//            geometry.setShadowMode(ShadowMode.Receive);
                geometry.setLocalTranslation(new Vector3f(0, 0, layerZ += epsz));
//            geometry.setQueueBucket(Bucket.Transparent);
                grids.attachChild(geometry);
            }

        }


        {


//            PathNetwork pnet = new PathNetwork(16, 16);
//            pnet.randomlySelect(20);

            Material matsel1 = modeler.matsel.clone();
            matsel1.setColor("Color", ColorRGBA.Orange);


            TileGrid selectTileGrid = new TileGrid(lingrid, matsel1, this);
            trafficGrid = new AbstractSetGrid(selectTileGrid, 15) {
                @Override
                public boolean contains(Pos pos) {
                    return map.traffic.getOccupier(pos) != null;
//                    return super.contains(x, y);
                }
            };

            trafficGrid.updateGrid();

            Geometry selgeom = selectTileGrid.getGeometry();
//            selgeom.setName("selgrid");

//            selgeom.setMaterial(matsel);
//            selgeom.setShadowMode(ShadowMode.Receive);
            selgeom.setLocalTranslation(new Vector3f(0, 0, layerZ += epsz));
//            selgeom.setQueueBucket(Bucket.Transparent);
            grids.attachChild(selgeom);
        }

        {


//            PathNetwork pnet = new PathNetwork(16, 16);
//            pnet.randomlySelect(20);

            Material matsel1 = modeler.matsel.clone();
            matsel1.setColor("Color", ColorRGBA.Green);

            selectTileGrid = new TileGrid(lingrid, matsel1, this);
//            selectGrid = new SetGrid(selectTileGrid, 15);
//            {
//                @Override
//                public boolean contains(Pos pos) {
//                    return traffic.getOccupier(pos) != null;
////                    return super.contains(x, y);
//                }
//            };
//            selectGrid.updateGrid();

            selgeom = selectTileGrid.getGeometry();
            selgeom.setName("selgrid");

//            selgeom.setMaterial(matsel);
//            selgeom.setShadowMode(ShadowMode.Receive);
            selgeom.setLocalTranslation(new Vector3f(0, 0, layerZ += epsz));
//            selgeom.setQueueBucket(Bucket.Transparent);
            this.selectables.attachChild(selgeom);
        }

        this.rootNode.attachChild(grids);

    }

    Geometry selgeom;

    public void initFloor() {
        Box floorBox = new Box(Vector3f.ZERO, 10f, 0.1f, 5f);
        floorBox.scaleTextureCoordinates(new Vector2f(3, 6));

        Geometry floor = new Geometry("floor", floorBox);
        floor.setMaterial(modeler.matgrass);
        floor.setShadowMode(ShadowMode.Receive);
        floor.setLocalTranslation(0, -0.1f, 0);
        floor.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(10f, 0.1f, 5f)), 0));
        this.rootNode.attachChild(floor);

    }

    public void initMaterial() {
        //        mat2 = mat;
//        mat3 = mat;

        modeler.initMaterial();
    }

    protected void initCrossHairs() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        {
            BitmapText ch = new BitmapText(guiFont, false);
            ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
            ch.setText("+"); // crosshairs
            ch.setLocalTranslation( // center
                    settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                    settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
            guiNode.attachChild(ch);
        }
        {
            textAction = new BitmapText(guiFont, false);
            BitmapText score = textAction;
            score.setSize(guiFont.getCharSet().getRenderedSize() * 2);
            score.setText("Bingo"); // crosshairs
            score.setLocalTranslation( // center
                    10,
                    settings.getHeight()
                            - 10, 0);
            guiNode.attachChild(score);
        }

        {
            textSelection = new BitmapText(guiFont, false);
            BitmapText score = textSelection;
            score.setSize(guiFont.getCharSet().getRenderedSize() * 2);
            score.setText("Bingo"); // crosshairs
            score.setLocalTranslation( // center
                    10,
                    settings.getHeight()
                            - 50, 0);
            guiNode.attachChild(score);
        }

    }

    /**
     * Declaring the "Shoot" action and mapping to its triggers.
     */
    private void initKeys() {
        inputManager.addMapping(LEFT_CLICK,
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(actionListener, LEFT_CLICK);
    }

    /**
     * A red ball that marks the last spot that was "hit" by the "shot".
     */
    protected void initMark() {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    //    MyAction currentAction = new SelectAction();
//    MyAction currentAction = new BuildRoadAction();
    MyAction currentAction = new CubeAction();


    class SelectAction extends MyAction {

        Pos start;
        Pos current;

        @Override
        void updateGui() {
            current = pickPos();
//            selectGrid.set.clear();
            if (start != null) {
                selectTileGrid.setTexture(start, TexturesSelect.SELECTED);
            }
            if (current != null) {
                selectTileGrid.setTexture(current, TexturesSelect.SELECTED);
//                selectTileGrid.add(current.x, current.y + 1);
//                selectTileGridd.add(current.x + 1, current.y + 1);
            }
//            selectGrid.updateGrid();
        }

        @Override
        void onMouseDown() {
            this.start = pickPos();
        }

        @Override
        void onMouseUp() {
            if (this.current != null) {
            }
            this.start = null;
        }

//        void cancel() {
//            selectGrid.set.clear();
//            selectGrid.updateGrid();
//        }
    }

    class BuildRoadAction extends MyAction {

        Pos start;
        Pos current;
        private List<Pos> c;

        @Override
        void updateGui() {
            current = pickPos();
//            selectGrid.set.clear();
            if (start != null) {
                selectTileGrid.setTexture(start, TexturesSelect.SELECTED);
            }
            if (current != null) {
                selectTileGrid.setTexture(current, TexturesSelect.SELECTED);
            }
            if (start != null && current != null) {
//            c = simplepath4(start, current);
                c = roadBuilder.findPath(start, current);
                selectTileGrid.setAllTo(c, TexturesSelect.SELECTED);
            }


//            selectGrid.updateGrid();
        }

        List<Pos> simplepath4(Pos start, Pos end) {
            List<Pos> poses = new ArrayList<Pos>();
            if (start != null && end != null) {
                int len = -1;
                Dir dir = null;
                if (start.x == end.x) {
//                    for (int i = Math.min(start.y, end.y); i <= Math.max(start.y, end.y); i++) {


                    dir = ((start.y < end.y) ? Dir4.N : Dir4.S);
                    len = Math.abs(end.y - start.y);


                }
                if (start.y == end.y) {
//                    for (int i = Math.min(start.x, end.x); i <= Math.max(start.x, end.x); i++) {
//                    for (int x = start.x; x != end.x; x+= (start.x<end.x)?1:-1 ) {
//                        poses.add(new Pos(x, start.y));
//                    }

                    dir = ((start.x < end.x) ? Dir4.E : Dir4.W);
                    len = Math.abs(end.x - start.x);
                }
                Pos pos = start;
                for (int i = 0; i <= len; i++) {
                    poses.add(pos);
                    pos = pos.inDir(dir);
                }

            }
            return poses;
        }

        @Override
        void onMouseDown() {
            this.start = pickPos();
        }

        @Override
        void onMouseUp() {
            if (this.current != null) {
//                for (Pos pos : c) {
//                    pnet.addPoint(pos);
//                }
                map.pnet.build(c); // build oriented path

                map.pnet.addPoint(this.current);
                roadTileGrid.updateTexture();
                arrowsTileGrid.updateTexture();
//                pnet.updateTextures();
            }
            this.start = null;
        }

//        void cancel() {
//            selectGrid.set.clear();
//            selectGrid.updateGrid();
//        }
    }

    class BuildAction extends MyAction {

        Pos start;
        Pos current;
        //        Building.Type buildingType = Building.Type.FACTORY;
//        Building.Type buildingType = Building.Type.HOUSE;
        Building.Type buildingType = Building.Type.MINE;

        @Override
        void updateGui() {
            current = pickPos();
//            selectGrid.set.clear();
            if (start != null) {
                selectTileGrid.setTexture(start, TexturesSelect.SELECTED);
            }
            if (current != null) {
                Set<Pos> occupyPosses = Building.getOccupyPosses(current, buildingType);


//                selectGrid.set.addAll(occupyPosses);

                for (Pos occupyPoss : occupyPosses) {
                    if (map.isBuildable(occupyPoss)) {
                        selectTileGrid.setTexture(occupyPoss, TexturesSelect.SELECTED);
                    }
                }


//
// selectGrid.add(current.x, current.y);
//                selectGrid.add(current.x, current.y + 1);
//                selectGrid.add(current.x + 1, current.y + 1);
            }
//            selectGrid.updateGrid();
        }

        @Override
        void onMouseDown() {
            this.start = pickPos();
        }

        @Override
        void onMouseUp() {
            if (this.current != null) {
//                Building building = new Building(this.current, matBuildings);
                putBuilding(new Building(this.current, modeler.matBuildings, buildingType, player));
            }
            this.start = null;
        }



//        void cancel() {
//            selectGrid.set.clear();
//            selectGrid.updateGrid();
//        }
    }

    class DropCarAction extends MyAction {

        Pos start;
        Pos current;
        private RoadPoint roadPoint;

        @Override
        void updateGui() {
            current = pickPos();
//            selectGrid.set.clear();
            if (start != null) {
                selectTileGrid.setTexture(start, TexturesSelect.SELECTED);
            }
            if (current != null) {
                roadPoint = map.pnet.getRoadPoint(current);

                if (roadPoint != null) {
                    selectTileGrid.setTexture(current, TexturesSelect.SELECTED);
                }

//
// selectGrid.add(current.x, current.y);
//                selectGrid.add(current.x, current.y + 1);
//                selectGrid.add(current.x + 1, current.y + 1);
            }
//            selectGrid.updateGrid();
        }

        @Override
        void onMouseDown() {
            this.start = pickPos();
        }

        @Override
        void onMouseUp() {
            if (this.current != null) {

                if (roadPoint != null) {
                    putCar(new Vehicle(player, Vehicle.Type.SKODA120, roadPoint, modeler.matVehicles));
                }
            }
            this.start = null;
        }

//        void cancel() {
//            selectGrid.set.clear();
//            selectGrid.updateGrid();
//        }
    }

    class CubeAction extends MyAction {


        Cube current;


        @Override
        void updateGui() {
            Object pick = pick();
            if (pick instanceof Cube) {
                current = (Cube) pick;
            } else {
                current = null;
            }
//            selectGrid.set.clear();
            if (current != null) {
//                selectTileGrid.setTexture(current, TexturesSelect.SELECTED);
//                selectTileGrid.add(current.x, current.y + 1);
//                selectTileGridd.add(current.x + 1, current.y + 1);
            }
//            selectGrid.updateGrid();
        }

        @Override
        void onMouseDown() {
            //          this.start = pickPos();
        }

        @Override
        void onMouseUp() {
            if (this.current != null) {
                // put new cube
                Pos3D pos3D = current.getPos().inDir(dir3D);
                createCubeOnPos3D(pos3D);
            }
        }

//        void cancel() {
//            selectGrid.set.clear();
//            selectGrid.updateGrid();
//        }
    }


}
