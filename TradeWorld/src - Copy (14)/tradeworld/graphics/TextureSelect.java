package tradeworld.graphics;

import javax.vecmath.TexCoord2f;

public class TextureSelect {

    public static final int FLIP_NO = 0;
    public static final int FLIP_RL = 1;
    public static final int FLIP_TB = 3;
    public static final int ROT_0 = 0;
    public static final int ROT_90 = 1;
    public static final int ROT_180 = 2;
    public static final int ROT_270 = 3;
    private final TexCoord2f[] texs = new TexCoord2f[4];

    public TexCoord2f[] getTexs() {
        return texs;
    }

    public TextureSelect(int texx, int texy, int texw, int texh) {
        this(texx, texy, texw, texh, ROT_0, FLIP_NO);
    }

    public TextureSelect(int texx, int texy, int texw, int texh, int rot, int mirr) {
        super();
        final float one = 1f;
        texs[((0 + rot) & 3) ^ mirr] = new TexCoord2f((float) texx / texw, (float) texy / texh);
        texs[((1 + rot) & 3) ^ mirr] = new TexCoord2f((texx + one) / texw, (float) texy / texh);
        texs[((2 + rot) & 3) ^ mirr] = new TexCoord2f((texx + one) / texw, (texy + one) / (texh));
        texs[((3 + rot) & 3) ^ mirr] = new TexCoord2f((float) texx / texw, (texy + one) / (texh));
    }
}
