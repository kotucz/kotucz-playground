package tradeworld.graphics;

import tradeworld.RoadNetwork;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.PickRay;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.TrainNetwork;

/**
 *
 * @author Kotuc
 */
public class Land3D extends Model3D {

    private final Heighmap heighmap;
    private final int tilesx, tilesy;
    private final GeometryArray landGeometry;
    private final RoadNetwork roadNetwork;
    private final TrainNetwork trainNetwork;
    private final List<TileLayer> layers = new ArrayList<TileLayer>();
    private final Tile[][] tiles;

    public Heighmap getHeighmap() {
        return heighmap;
    }

    public TrainNetwork getTrainNetwork() {
        return trainNetwork;
    }

    public Land3D(int widthx, int widthy) {
        super();
        this.tilesx = widthx;
        this.tilesy = widthy;

        this.heighmap = new Heighmap(widthx, widthy);

        this.tiles = new Tile[widthx][widthy];

        for (int x = 0; x < widthx; x++) {
            for (int y = 0; y < widthy; y++) {
                tiles[x][y] = new Tile(x, y, offset(x, y));

            }
        }

        this.landGeometry = createLandGeometry();

//        Sphere objFloor = new Sphere();
//        objFloor.setAppearance(new Appearance());
//        objFloor.setAppearance();
        {
            TileLayer grassLayer = new TileLayer(0, Tools.loadTextureUnit("grassT02.jpg", TextureAttributes.DECAL));
            grassLayer.generateRandomTexCoords();
            layers.add(grassLayer);
        }
        {
            TileLayer roadLayer = new TileLayer(1, Tools.loadTextureUnit("road-8-8-v3.png", TextureAttributes.DECAL));
            this.roadNetwork = new RoadNetwork(this, roadLayer);
            layers.add(roadLayer);
        }
        {
            TileLayer trainLayer = new TileLayer(2, Tools.loadTextureUnit("train-8-8-v2.png", TextureAttributes.DECAL));
            this.trainNetwork = new TrainNetwork(this, trainLayer);
            layers.add(trainLayer);
        }

        Appearance appear = createLandAppearance();

        // texture units
        TextureUnitState tus[] = new TextureUnitState[layers.size()];

        // cover the floor with the first texture
//        tus[0] = loadTextureUnit("land1.png", TextureAttributes.DECAL);

        // add second texture (it has transparent parts)
//        tus[1] = Tools.loadTextureUnit("road-8-8.png", TextureAttributes.DECAL);
//        tus[1] = Tools.loadTextureUnit("road-8-8upgrade.png", TextureAttributes.DECAL);

        // modulated light map
//        tus[0] = Tools.loadTextureUnit("mega4.png", TextureAttributes.DECAL);
//        tus[0] = Tools.loadTextureUnit("land1.png", TextureAttributes.DECAL);
//                tus[0] = Tools.loadTextureUnit("grassT02.jpg", TextureAttributes.DECAL);
        // tus[2] = lightMapTUS();

        for (TileLayer tileLayer : layers) {
            tus[tileLayer.layer] = tileLayer.tus;
        }

        appear.setTextureUnitState(tus);


//        objFloor.setCollidable(false);
        Shape3D objFloor = new Shape3D(landGeometry, appear);
//        generateTexCoords();


        objFloor.setName("Land tiles");
        System.out.println("objFloor: " + objFloor);
        addChild(objFloor);
    }

    public Point3d pick(PickRay pickRay) {
        Point3d point = new Point3d();
        Vector3d vec = new Vector3d();
        pickRay.get(point, vec);
        double t = -point.z / vec.z;
        vec.scale(t);
        point.add(vec);
        return point;
    }

    private GeometryArray createLandGeometry() {

        GeometryArray geometry =
                new QuadArray((tilesx) * (tilesy) * 4,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2,
                3, new int[]{0, 1, 2});

        geometry.setCoordinates(0, generateTilesCoords());

        geometry.setColors(0, generateColors());

        { // normals
            GeometryInfo gi = new GeometryInfo(geometry);

            NormalGenerator ng = new NormalGenerator();
            ng.generateNormals(gi);

            geometry = gi.getGeometryArray();
        }

        geometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        geometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        geometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
        geometry.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
// 	     landGeometry.setCapability(GeometryArray.BY_REFERENCE);

        return geometry;
    }

    public Point3d[] generateTilesCoords() {
        Point3d[] coords = new Point3d[(tilesx) * (tilesy) * 4];
        int i = 0;
        for (int y = 0; y < this.tilesy; y++) {
            for (int x = 0; x < this.tilesx; x++) {
                coords[i + 0] = heighmap.get(x, y);
                coords[i + 1] = heighmap.get(x + 1, y);
                coords[i + 2] = heighmap.get(x + 1, y + 1);
                coords[i + 3] = heighmap.get(x, y + 1);
                i += 4;
            }
        }
        return coords;
    }

    public Color3f[] generateColors() { // colors
        Color3f[] colors = new Color3f[getNumTiles()];
        for (int i = 0; i <
                colors.length; i++) {
            colors[i] = colors[i] = new Color3f((float) Math.random(), (float) Math.random(), (float) Math.random());
        }

        return colors;
//        landGeometry.setColorRefFloat(new float[N * 3]);

    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    public class Tile {

        final int x, y;
        final int index;

        public Tile(int x, int y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

        public Point3d[] generate4TileCoords() {
            Point3d[] coords = new Point3d[4];
            coords[0] = heighmap.get(x, y);
            coords[1] = heighmap.get(x + 1, y);
            coords[2] = heighmap.get(x + 1, y + 1);
            coords[3] = heighmap.get(x, y + 1);
            return coords;
        }
    }

    boolean checkTileBounds(int tilex, int tiley) {
        return !(tilex < 0 || tilesx <= tilex || tiley < 0 || tilesy <= tiley);
    }

    /**
     * Returns first index of tile.
     * @param tilex
     * @param tiley
     * @return
     */
    private int offset(int tilex, int tiley) {
        if (!checkTileBounds(tilex, tiley)) {
            throw new ArrayIndexOutOfBoundsException("tile out of bounds " + tilex + ", " + tiley);
        }
        return 4 * (tilex + tilesx * tiley);
    }

    public int getTilesx() {
        return tilesx;
    }

    public int getTilesy() {
        return tilesy;
    }
//        public TextureSelect(int texx, int texy, int texw, int texh, int rot, int mirr) {
//            texs[0] = new TexCoord2f((float) (texx) / (texw), (float) (texy) / (texh));
//            texs[1] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy) / (texh));
//            texs[2] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy + 1) / (texh));
//            texs[3] = new TexCoord2f((float) (texx) / (texw), (float) (texy + 1) / (texh));
//        }

    public int getNumTiles() {
        return tilesx * tilesy;
    }

    public class TileLayer {

        final int layer;
        final TextureUnitState tus;

        public TileLayer(int layer, TextureUnitState tus) {
            this.layer = layer;
            this.tus = tus;
        }

        public void selectTexture(int tilex, int tiley, TextureSelect select) {
            try {
                int offset = offset(tilex, tiley);
                landGeometry.setTextureCoordinates(layer, offset, select.getTexs());
            } catch (Exception e) {
//                System.err.println("EX " + e.getMessage());
            }
        }

        public void generateRandomTexCoords() { // texture1

            for (int x = 0; x < tilesx; x++) {
                for (int y = 0; y < tilesy; y++) {
                    TextureSelect textureSelect = new TextureSelect(
                            (int) Math.round(Math.random()), (int) Math.round(Math.random()), 2, 2,
                            1, 0);
                    selectTexture(x, y, textureSelect);
                }
            }

//        for (int x = 0; x < widthx; x++) {
//            for (int y = 0; y < widthy; y++) {
//                TextureSelect textureSelect = new TextureSelect(
//                        new Random().nextInt(8), (int) Math.round(Math.random()), 8, 1,
//                        0, 0);
//                selectTexture(1, x, y, textureSelect);
//            }
//        }

//        for (int i = 0; i < coords.length; i += 4) {
//            TexCoord2f[] texs2 = helpTile((int) Math.round(Math.random()), (int) Math.round(Math.random()), 2, 2);
//            for (int j = 0; j < 4; j++) {
//                texs[i + j] = texs2[j];
//            }
//        }

//        return texs;

        }
    }

    private Appearance createLandAppearance() {

        Appearance appear = new Appearance();

//        landAppearance.setColoringAttributes (new ColoringAttributes (new Color3f (0.0f, 0.0f, 1.0f),1));
//        landAppearance.setPolygonAttributes(new PolygonAttributes);

        Material ma = new Material();

        ma.setAmbientColor(new Color3f(0.32f, 0.32f, 0.32f));
        ma.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
        ma.setEmissiveColor(new Color3f(0.41f, 0.41f, 0.41f));
        ma.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f));
        appear.setMaterial(ma);
//        ma.setShininess(1.0f);


//        {
////  landTexturing
//            try {
////            BufferedImage img = ImageIO.read(getClass().getResource("./res/images/land1.png"));
//                BufferedImage img1 = ImageIO.read(new File("./res/images/land1.png"));
//                BufferedImage img2 = ImageIO.read(new File("./res/images/land2.png"));
////            System.out.println("Image " + img + " " + img.getWidth() + "x" + img.getHeight());
//
////        TextureLoader loader = new TextureLoader("grass.jpg");
//                ImageComponent2D image1 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, img1);//loader.getImage();
//                ImageComponent2D image2 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, img2);//loader.getImage();
//
////            System.out.println("comp " + image.getWidth() + " x " + image.getHeight());
//
////            image.set(img);
////        if (image == null) {
////            System.out.println("no image");
////        }
//
////            Texture2D texture = new Texture2D();
////            texture.setImage(0, image);
//
////            Texture3D texture = new Texture3D();
//                Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
//                        image1.getWidth(), image1.getHeight());
////            texture.setImage(0, image);
////            texture.setImages(new ImageComponent[]{image1, image2});
//                texture.setImages(new ImageComponent[]{image1});
//
//                appear.setTexture(texture);
//            } catch (IOException ex) {
//                Logger.getLogger(Land.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }



//        TextureLoader loader = new TextureLoader("./images/grass.jpg", null);
//        ImageComponent2D image = loader.getImage();
//
//        if (image == null) {
//            System.out.println("load failed for texture grass.jpg");
//        }
//
//        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
//                image.getWidth(), image.getHeight());
//        texture.setImage(0, image);
//        appear.setTexture(texture);
//
//        TextureAttributes ta = new TextureAttributes();
//        ta.setTextureMode(TextureAttributes.MODULATE);
//        appear.setTextureAttributes(ta);
//        appear.setTexCoordGeneration(new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR, TexCoordGeneration.TEXTURE_COORDINATE_2));


//		return new Appearance();

        return appear;
    }
//    private TexCoord2f[] generateTexCoordsEasy() { // texture1
//        TexCoord2f[] texs = new TexCoord2f[getNumTiles()]; // strange
////        for (int i = 0; i < texs.length; i++) {
////            texs[i] = new TexCoord2f((float) Math.random(), (float) Math.random());
////        }
//        for (int i = 0; i < texs.length; i += 4) {
//            texs[i + 0] = new TexCoord2f(0, 0);
//            texs[i + 1] = new TexCoord2f(1, 0);
//            texs[i + 2] = new TexCoord2f(1, 1);
//            texs[i + 3] = new TexCoord2f(0, 1);
//        }
////        landGeometry.setColorRefFloat(new float[N * 3]);
////        landGeometry.setTexCoordRefFloat(0, texs);//ColorRefFloat(tex);
//
//        return texs;
//    }
//
//    private TexCoord2f[] generateTexCoords() { // texture1
//        TexCoord2f[] texs = new TexCoord2f[getNumTiles()];
//
//        for (int i = 0; i < texs.length; i++) {
//            texs[i] = new TexCoord2f();
////            TexCoord2f[] texs2 = helpTile((int) Math.round(Math.random()), (int) Math.round(Math.random()), 2, 2);
////            for (int j = 0; j < 4; j++) {
////                texs[i + j] = texs2[j];
////            }
//        }
//
//        return texs;
//
//    }
//    private GeometryUpdater waterUpdater = new WaterUpdater();
    /*
    void initPhaseMap() {
    phaseMap=new int[widthx][widthy][];
    for (int x=0; x<widthx; x++) {
    for (int y=0; y<widthy; y++) {
    if (true) {
    phaseMap[x][y]=new int[1];
    phaseMap[x][y][0]=SOLID;
    } else {
    phaseMap[x][y]=new int[5];
    phaseMap[x][y][0]=SOLID;
    }
    }
    }

    createPhaseMap();

    }
     */
    /*
    BranchGroup objPhaseMap;

    public BranchGroup createPhaseMap () {

    objPhaseMap = new BranchGroup();

    for (int x=0; x<phaseMap.length; x++) {
    for (int y=0; y<phaseMap[x].length; y++) {
    for (int z=0; z<phaseMap[x][y].length; z++) {

    Appearance phaseAppear = new Appearance();
    phaseAppear.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0.0f));

    switch (phaseMap[x][y][z]) {
    case SOLID :
    phaseAppear.setColoringAttributes(new ColoringAttributes(0.0f, 1.0f, 0.0f, ColoringAttributes.FASTEST));
    break;
    case WATER :
    phaseAppear.setColoringAttributes(new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.FASTEST));
    break;
    case AIR :
    phaseAppear.setColoringAttributes(new ColoringAttributes(1.0f, 1.0f, 1.0f, ColoringAttributes.FASTEST));
    break;
    default :
    break;
    }

    Transform3D t3dSet = new Transform3D();
    t3dSet.set(new Vector3f((float)x+0.5f, (float)y+0.5f, (float)z-0.5f));
    TransformGroup objTrans = new TransformGroup(t3dSet);

    objTrans.addChild(new Box(0.49f, 0.49f, 0.49f, phaseAppear));
    objPhaseMap.addChild(objTrans);
    }
    }
    }

    objLand.addChild(objPhaseMap);

    return objPhaseMap;
    }

    int getPhase (Point3d point) {
    int x= (int)Math.floor(point.x);
    int y= (int)Math.floor(point.y);
    int z= (int)Math.floor(point.z)-1;

    if ((x<phaseMap.length)&&x>=0&&(y<phaseMap[x].length)&&y>=0&&(z<phaseMap[x][y].length)&&z>=0)
    return phaseMap[x][y][z];
    return AIR;
    }
     */
}
