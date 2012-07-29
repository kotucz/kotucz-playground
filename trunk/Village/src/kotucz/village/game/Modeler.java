package kotucz.village.game;

import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

public class Modeler {
    private final MyGame myGame;
    public Material mat;
    public Material mat16;
    public Material matgrass;
    public Material matroad;
    public Material matroadarrows;
    public Material matwtr;
    public Material matsel;
    public Material matVehicles;
    public Material matBuildings;
    public Material matPipes;
    public static Material matResources;

    public Modeler(MyGame myGame) {
        this.myGame = myGame;
    }

    public void initMaterial() {
        {
            mat = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);
            TextureKey key = new TextureKey("Textures/tex1.png");
//        key.setTextureTypeHint(Texture.Type.TwoDimensionalArray);       
            key.setGenerateMips(true);
//        key.setAnisotropy();
            Texture tex = myGame.getAssetManager().loadTexture(key);
            tex.setMagFilter(Texture.MagFilter.Nearest);
//        tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
            mat.setTexture("ColorMap", tex);
        }
//        mat2 = mat;
//        mat3 = mat;
        {
            mat16 = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);
            TextureKey key2 = new TextureKey("Textures/tex16.png");
//            key2.setGenerateMips(true);

            Texture tex2 = myGame.getAssetManager().loadTexture(key2);
            tex2.setMagFilter(Texture.MagFilter.Nearest);
            mat16.setTexture("ColorMap", tex2);
        }

        {  // unshaded
            Material matgrass = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);

            TextureKey key3 = new TextureKey("Textures/gras16.png");
//            key3.setGenerateMips(true);

            Texture tex3 = myGame.getAssetManager().loadTexture(key3);
            tex3.setMagFilter(Texture.MagFilter.Nearest);

            matgrass.setTexture("ColorMap", tex3);

//            this.matgrass = matgrass;

        }
        {

            Material matgrass = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_LIGHT_LIGHTING_J3MD);

            TextureKey key3 = new TextureKey("Textures/gras16.png");
//            key3.setGenerateMips(true);

            Texture tex3 = myGame.getAssetManager().loadTexture(key3);
            tex3.setMagFilter(Texture.MagFilter.Nearest);

            matgrass.setTexture("m_DiffuseMap", tex3); // light

            this.matgrass = matgrass;

        }
        {
            matwtr = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);
//            TextureKey key4 = new TextureKey("Textures/road16.png");
            TextureKey key4 = new TextureKey("Textures/watrnodes16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = myGame.getAssetManager().loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matwtr.setTexture("ColorMap", tex4);
            matwtr.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        {
            matroad = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);
            TextureKey key4 = new TextureKey("Textures/road16.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = myGame.getAssetManager().loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matroad.setTexture("ColorMap", tex4);
            matroad.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        {
            Material material = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);

            TextureKey key4 = new TextureKey("Textures/roadarrows16.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = myGame.getAssetManager().loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            material.setTexture("ColorMap", tex4);
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            matroadarrows = material;
        }
        {
            Material material = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);

            TextureKey textureKey = new TextureKey("Textures/buildings.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture texture = myGame.getAssetManager().loadTexture(textureKey);
            texture.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            material.setTexture("ColorMap", texture);
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

            matBuildings = material;

        }

        {   // resources unshaded
            Material material = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);

            TextureKey textureKey = new TextureKey("Textures/tiles.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture texture = myGame.getAssetManager().loadTexture(textureKey);
            texture.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            material.setTexture("ColorMap", texture);
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

//            matResources = material;

        }
        {   // resources shaded
            Material material = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_LIGHT_LIGHTING_J3MD);

            TextureKey textureKey = new TextureKey("Textures/tiles.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture texture = myGame.getAssetManager().loadTexture(textureKey);
            texture.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            material.setTexture("m_DiffuseMap", texture);
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

            matResources = material;

        }

        {
            matsel = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);
            TextureKey key4 = new TextureKey("Textures/select16.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = myGame.getAssetManager().loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matsel.setTexture("ColorMap", tex4);
            matsel.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        {
            Material matveh = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_MISC_UNSHADED_J3MD);
            TextureKey key4 = new TextureKey("Textures/veh256.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
            Texture tex4 = myGame.getAssetManager().loadTexture(key4);
            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matveh.setColor("Color", new ColorRGBA(1f, 0f, 1f, 1f));
            matveh.setTexture("ColorMap", tex4);
            matveh.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            this.matVehicles = matveh;
        }

        {
            Material matveh = new Material(myGame.getAssetManager(), MyGame.COMMON_MAT_DEFS_LIGHT_LIGHTING_J3MD);
//            TextureKey key4 = new TextureKey("Textures/veh256.png");
//            TextureKey key4 = new TextureKey("Textures/watr16.png");
//            TextureKey key3 = new TextureKey("Textures/tex16.png");
//            key3.setGenerateMips(true);`
//            Texture tex4 = myGame.getAssetManager().loadTexture(key4);
//            tex4.setMagFilter(Texture.MagFilter.Nearest);
//        tex3.setWrap(WrapMode.Repeat);
            matveh.setColor("Diffuse", new ColorRGBA(1f, 0f, 1f, 1f));
//            matveh.setTexture("ColorMap", tex4);
  //          matveh.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            this.matPipes = matveh;
        }

        {
//            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
//            mat.setBoolean("PointSprite", true);
//            mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
//            myGame.setSpriteMaterial(mat);
        }
    }
}