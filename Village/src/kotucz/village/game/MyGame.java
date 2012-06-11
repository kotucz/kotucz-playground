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

import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.AbstractBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.texture.Texture;

import java.util.*;

import kotucz.village.tiles.Multitexture;
import kotucz.village.common.MyBox;
import kotucz.village.build.Building;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.transport.BlockingTraffic;
import kotucz.village.transport.PathNetwork;
import kotucz.village.tiles.Pos;
import kotucz.village.tiles.SelectGrid;
import kotucz.village.tiles.TileGrid;
import kotucz.village.transport.Vehicle;

/**
 * @author double1984
 */
public class MyGame extends SimpleApplication {

    public static final Vector3f UP = new Vector3f(0, 0, 1);
    static float bLength = 1f;
    static float bWidth = 1f;
    static float bHeight = 1f;
    public static final String LEFT_CLICK = "LeftClick";
    Material mat;
    Material mat16;
    Material matgrass;
    Material matroad;
    Material matwtr;
    Material matsel;
    Material matveh;
    BasicShadowRenderer bsr;
    private static Sphere bullet;
    private static MyBox box;
    private static Box brick;
    private static SphereCollisionShape bulletCollisionShape;
    private BitmapText actionText;
    private BulletAppState bulletAppState;
    Geometry mark;
    Node selectables;
    final LinearGrid lingrid = new LinearGrid(16, 16);
    SelectGrid selectGrid;
    private PathNetwork pnet;

    public static void main(String args[]) {
        MyGame f = new MyGame();
        f.start();
    }

    private TileGrid selectTileGrid;


    BlockingTraffic traffic;



    @Override
    public void simpleInitApp() {

        assetManager.registerLocator("assets/", FileLocator.class);

        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
//        stateManager.attach(bulletAppState);

        bullet = new Sphere(32, 32, 0.4f, true, false);
        bullet.setTextureMode(TextureMode.Projected);
//        bulletCollisionShape = new SphereCollisionShape(0.4f);


        selectables = new Node("Shootables");
        rootNode.attachChild(selectables);

        Multitexture mtex = new Multitexture(16 * 4, 16 * 4);
//        mtex.createSubtexture(0, 16, 16, 32);

        box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));

        box.setTexture(4, mtex.createSubtexture(0, 16, 16, 32));

        brick = new Box(Vector3f.ZERO, new Vector3f(1, 1, 1));
//        brick = new Box(Vector3f.ZERO, new Vector3f(1, 1, 1));
//        brick.scaleTextureCoordinates(new Vector2f(1f, 1f));



//         {
//              float frustumSize = 1;
//        cam.setParallelProjection(true);
//        float aspect = (float) cam.getWidth() / cam.getHeight();
//        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
//        }


        initMaterial();
//        initWall();
        initBoxes();
//        initFloor();
        initGrid();
        initCrossHairs();

        initKeys();       // load custom key mappings
        initMark();       // a red sphere to mark the hit


        Player player = new Player("Kotuc", null, 10000);



        for (int i = 0; i < 15; i++) {
            Vehicle car = new Vehicle(player, Vehicle.Type.SKODA120, pnet.randomRoadPoint(), matveh, pnet);
            selectables.attachChild(car.getNode());
            traffic.addVehicle(car);
        }



        {
            // setup camera
            getFlyByCamera().setMoveSpeed(50);
            getFlyByCamera().setUpVector(UP);

            this.cam.setLocation(new Vector3f(8, -8, 8f));
            cam.lookAt(new Vector3f(8, 8, 0), UP);
            cam.setFrustumFar(50);
        }

        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "shoot");
        inputManager.addMapping("gc", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addListener(actionListener, "gc");


        rootNode.setShadowMode(ShadowMode.Off);
        bsr = new BasicShadowRenderer(assetManager, 256);
        bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(bsr);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);

        selectGrid.set.clear();

//        actionText.setText("Jelito " + System.currentTimeMillis());

        actionText.setText("Jelito " + System.currentTimeMillis() + " Action: " + currentAction);

//        currentAction = new SelectAction();


        currentAction.updateGui();


        traffic.update(tpf);

        selectGrid.updateGrid();

    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

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
            if (name.equals("gc") && !keyPressed) {
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

    Pos pick() {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        selectables.collideWith(ray, results);
        // 4. Print the results
        System.out.println("----- Collisions? " + results.size() + "-----");
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint();
            String hit = results.getCollision(i).getGeometry().getName();
            System.out.println("* Collision #" + i);
            System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");


        }


        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
            // The closest collision point is what was truly hit:
            CollisionResult closest = results.getClosestCollision();
            // Let's interact - we mark the hit with a red dot.
            mark.setLocalTranslation(closest.getContactPoint());
            rootNode.attachChild(mark);

            System.out.println("Test user value: " + closest.getGeometry().getUserData("test"));

            if ("selgrid".equals(closest.getGeometry().getName())) {
                Vector3f contactPoint = closest.getContactPoint();
                int x = (int) Math.floor(contactPoint.x);
                int y = (int) Math.floor(contactPoint.y);
//                selectTileGrid.setTexture(x, y, 1);
//                selectGrid.add(x, y);
//                selectGrid.updateGrid();
                return new Pos(x, y);
            }

        } else {
            // No hits? Then remove the red mark.
            rootNode.detachChild(mark);
        }
        return null;
    }

    public void initBoxes() {

        {
            Geometry reBoxg = new Geometry("brick", box);
            reBoxg.setMaterial(mat16);
            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));

            this.rootNode.attachChild(reBoxg);
        }


        {
            Geometry reBoxg = new Geometry("brick3", box);
            reBoxg.setMaterial(mat16);
            reBoxg.setLocalTranslation(new Vector3f(0, 3, 0));

            this.rootNode.attachChild(reBoxg);
        }

        {
            Geometry reBoxg = new Geometry("brick2", brick);
            reBoxg.setMaterial(mat);
            reBoxg.setLocalTranslation(new Vector3f(2, 0, 0));

            this.rootNode.attachChild(reBoxg);
        }

//        addBox(new Vector3f(0, 0, 0), box);

//        addBox(new Vector3f(2, 0, 0), brick);

    }

    public void initWall() {
        float startpt = bLength / 4;
        float height = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 4; i++) {
                Vector3f vt = new Vector3f(i * bLength * 2 + startpt, bHeight + height, 0);
                addBrick(vt);
            }
            startpt = -startpt;
            height += 2 * bHeight;
        }
    }

    public void initGrid() {
        {
//            final Geometry geometry = new Geometry("grid", new MeshTileGrid());
//            geometry.setMaterial(matgrass);
//            geometry.setShadowMode(ShadowMode.Receive);
//            geometry.setLocalTranslation(new Vector3f(0, 0, 0.f));
//            this.rootNode.attachChild(geometry);

            final Geometry geometry = new TileGrid(lingrid, matgrass, this).getGeometry();
//            geometry.setMaterial(matwtr);
//            geometry.setShadowMode(ShadowMode.Receive);
            geometry.setLocalTranslation(new Vector3f(0, 0, 0.001f));
            this.rootNode.attachChild(geometry);

        }


        {

//            PathNetwork pnet = new PathNetwork(16, 16);
//            pnet.randomlySelect(40);


//            final Geometry geometry = new Geometry("grid16", new NodedefTileGrid(pnet));
            TileGrid tileGrid = new TileGrid(lingrid, matwtr, this);
            final Geometry geometry = tileGrid.getGeometry();
            SelectGrid se = new SelectGrid(tileGrid, 0);
            se.set.clear();

            Random r = new Random();

            for (int i = 0; i < 100; i++) {
                se.add(r.nextInt(16), r.nextInt(16));
            }

            se.updateGrid();

//            geometry.setMaterial(matwtr);
//            geometry.setShadowMode(ShadowMode.Receive);
            geometry.setLocalTranslation(new Vector3f(0, 0, 0.002f));
            this.rootNode.attachChild(geometry);
        }
        {


            TileGrid tileGrid = new TileGrid(lingrid, matroad, this);
            pnet = new PathNetwork(tileGrid);
//            pnet.randomlySelect(80); 
            pnet.generateRandomWalk(new Random());
            pnet.updateTextures();

            final Geometry geometry = tileGrid.getGeometry();
            geometry.setMaterial(matroad);
//            geometry.setShadowMode(ShadowMode.Receive);
            geometry.setLocalTranslation(new Vector3f(0, 0, 0.003f));
//            geometry.setQueueBucket(Bucket.Transparent);
            this.rootNode.attachChild(geometry);
        }

        traffic = new BlockingTraffic(pnet);


        {


//            PathNetwork pnet = new PathNetwork(16, 16);
//            pnet.randomlySelect(20);

            selectTileGrid = new TileGrid(lingrid, matsel, this);
            selectGrid = new SelectGrid(selectTileGrid, 15) {
                @Override
                public boolean contains(int x, int y) {
                    return traffic.getOccupier(new Pos(x, y))!=null;
//                    return super.contains(x, y);    //To change body of overridden methods use File | Settings | File Templates.
                }
            };
            selectGrid.updateGrid();

            selgeom = selectTileGrid.getGeometry();
            selgeom.setName("selgrid");

//            selgeom.setMaterial(matsel);
//            selgeom.setShadowMode(ShadowMode.Receive);
            selgeom.setLocalTranslation(new Vector3f(0, 0, 0.004f));
//            selgeom.setQueueBucket(Bucket.Transparent);
            this.selectables.attachChild(selgeom);
        }
    }

    Geometry selgeom;

    public void initFloor() {
        Box floorBox = new Box(Vector3f.ZERO, 10f, 0.1f, 5f);
        floorBox.scaleTextureCoordinates(new Vector2f(3, 6));

        Geometry floor = new Geometry("floor", floorBox);
        floor.setMaterial(matgrass);
        floor.setShadowMode(ShadowMode.Receive);
        floor.setLocalTranslation(0, -0.1f, 0);
        floor.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(10f, 0.1f, 5f)), 0));
        this.rootNode.attachChild(floor);
        this.getPhysicsSpace().add(floor);
    }

    public void initMaterial() {
        {
            mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            TextureKey key = new TextureKey("Textures/tex1.png");
//        key.setTextureTypeHint(Texture.Type.TwoDimensionalArray);       
            key.setGenerateMips(true);
//        key.setAnisotropy();
            Texture tex = assetManager.loadTexture(key);
            tex.setMagFilter(Texture.MagFilter.Nearest);
//        tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
            mat.setTexture("ColorMap", tex);
        }
//        mat2 = mat;
//        mat3 = mat;
        {
            mat16 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            TextureKey key2 = new TextureKey("Textures/tex16.png");
//            key2.setGenerateMips(true);

            Texture tex2 = assetManager.loadTexture(key2);
            tex2.setMagFilter(Texture.MagFilter.Nearest);
            mat16.setTexture("ColorMap", tex2);
        }
        {
            matgrass = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            TextureKey key3 = new TextureKey("Textures/gras16.png");
//            key3.setGenerateMips(true);

            Texture tex3 = assetManager.loadTexture(key3);
            tex3.setMagFilter(Texture.MagFilter.Nearest);

            matgrass.setTexture("ColorMap", tex3);
        }
        {
            matwtr = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//            TextureKey key4 = new TextureKey("Textures/road16.png");
            TextureKey key4 = new TextureKey("Textures/watrnodes16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = assetManager.loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matwtr.setTexture("ColorMap", tex4);
            matwtr.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        }
        {
            matroad = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            TextureKey key4 = new TextureKey("Textures/road16.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = assetManager.loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matroad.setTexture("ColorMap", tex4);
            matroad.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        }
        {
            matsel = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            TextureKey key4 = new TextureKey("Textures/select16.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = assetManager.loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matsel.setTexture("ColorMap", tex4);
            matsel.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        }
        {
            matveh = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            TextureKey key4 = new TextureKey("Textures/veh256.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = assetManager.loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matveh.setTexture("ColorMap", tex4);
            matveh.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        }

    }

    public void addBox(Vector3f ori, AbstractBox box) {

        Geometry reBoxg = new Geometry("brick", box);
        reBoxg.setMaterial(mat);
        reBoxg.setLocalTranslation(ori);

        //for geometry with sphere mesh the physics system automatically uses a sphere collision shape
//        reBoxg.addControl(new RigidBodyControl(1.5f));
//        reBoxg.setShadowMode(ShadowMode.CastAndReceive);
//        reBoxg.getControl(RigidBodyControl.class).setFriction(0.6f);
        this.rootNode.attachChild(reBoxg);
//        this.getPhysicsSpace().add(reBoxg);

    }

    public void addBrick(Vector3f ori) {

        Geometry reBoxg = new Geometry("brick", brick);
        reBoxg.setMaterial(mat);
        reBoxg.setLocalTranslation(ori);
        //for geometry with sphere mesh the physics system automatically uses a sphere collision shape
        reBoxg.addControl(new RigidBodyControl(1.5f));
        reBoxg.setShadowMode(ShadowMode.CastAndReceive);
        reBoxg.getControl(RigidBodyControl.class).setFriction(0.6f);
        this.rootNode.attachChild(reBoxg);
        this.getPhysicsSpace().add(reBoxg);
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
            actionText = new BitmapText(guiFont, false);
            BitmapText score = actionText;
            score.setSize(guiFont.getCharSet().getRenderedSize() * 2);
            score.setText("Bingo"); // crosshairs
            score.setLocalTranslation( // center
                    10,
                    settings.getHeight()
                            - 10, 0);
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
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

//    MyAction currentAction = new SelectAction();
    MyAction currentAction = new BuildRoadAction();


    class SelectAction extends MyAction {

        Pos start;
        Pos current;

        @Override
        void updateGui() {
            current = pick();
            selectGrid.set.clear();
            if (start != null) {
                selectGrid.add(start.x, start.y);
            }
            if (current != null) {
                selectGrid.add(current.x, current.y);
                selectGrid.add(current.x, current.y + 1);
                selectGrid.add(current.x + 1, current.y + 1);
            }
            selectGrid.updateGrid();
        }

        @Override
        void onMouseDown() {
            this.start = pick();
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
        private Set<Pos> c;

        @Override
        void updateGui() {
            current = pick();
            selectGrid.set.clear();
            if (start != null) {
                selectGrid.add(start);
            }
            if (current != null) {
                selectGrid.add(current);
            }
            c = simplepath4(start, current);
            selectGrid.set.addAll(c);
            selectGrid.updateGrid();
        }

        Set<Pos> simplepath4(Pos start, Pos end) {
            Set<Pos> poses = new HashSet<Pos>();
            if (start != null && end != null) {
                if (start.x == end.x) {
                    for (int i = Math.min(start.y, end.y); i <= Math.max(start.y, end.y); i++) {
                        poses.add(new Pos(start.x, i));
                    }
                }
                if (start.y == end.y) {
                    for (int i = Math.min(start.x, end.x); i <= Math.max(start.x, end.x); i++) {
                        poses.add(new Pos(i, start.y));
                    }
                }
            }
            return poses;
        }

        @Override
        void onMouseDown() {
            this.start = pick();
        }

        @Override
        void onMouseUp() {
            if (this.current != null) {
                for (Pos pos : c) {
                    pnet.addPoint(pos.x, pos.y);
                }

                pnet.addPoint(this.current.x, this.current.y);
                pnet.updateTextures();
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

        @Override
        void updateGui() {
            current = pick();
            selectGrid.set.clear();
            if (start != null) {
                selectGrid.add(start.x, start.y);
            }
            if (current != null) {
                selectGrid.add(current.x, current.y);
                selectGrid.add(current.x, current.y + 1);
                selectGrid.add(current.x + 1, current.y + 1);
            }
            selectGrid.updateGrid();
        }

        @Override
        void onMouseDown() {
            this.start = pick();
        }

        @Override
        void onMouseUp() {
            if (this.current != null) {
                Building building = new Building(this.current, mat16);
                selectables.attachChild(building.getNode());
            }
            this.start = null;
        }

//        void cancel() {
//            selectGrid.set.clear();
//            selectGrid.updateGrid();
//        }
    }


}
