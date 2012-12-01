/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interlos2012;

import javax.imageio.ImageIO;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Transforming image swaping pixel positions
 *
 * @author tkotula
 */
public class P7 {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        String PATH = "C:\\Users\\tkotula\\Desktop\\Interlos 2012/";

        BufferedImage cil = ImageIO.read(new File(PATH+"cil.bmp"));

        System.out.println("w "+ cil.getWidth());
        
        
        BufferedImage start = ImageIO.read(new File(PATH+"cil.bmp"));

        for (int i = 0; i < 160*160; i++) {

              int j = 160*160-i-1;

              start.setRGB(i%160, i/160, cil.getRGB(j%160, j/160));

        }

        ImageIO.write(start, "bmp", new File(PATH+"start.bmp"));


    }


}
