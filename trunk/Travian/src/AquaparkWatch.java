
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Kotuc
 */
public class AquaparkWatch {

    public static void checkImage(String name) {
        try {
            BufferedImage img = getImage(new URL("http://www.aquapark-kravare.cz/kamery/"+name+".jpg"));
            if (true) { // equal images
                ImageIO.write(img, "jpg", new File("C:/Users/Kotuc/Desktop/aquapark/" + name + "_"+System.currentTimeMillis()+".jpg"));    
            }
            System.out.println(name+"\t: OK");
        } catch (Exception ex) {
            System.err.println(name+"\t: Failed"+ex.getLocalizedMessage());
            
        }
    }
    
    public static BufferedImage getImage(URL url) throws IOException {
        return ImageIO.read(url);
    }
    
    public static void main(String[] args) {
        while (true) {
//            for (int i = 0; i < 100; i++) {
//                 checkImage("image"+i);               
//            }
            checkImage("image52");            
            checkImage("image57");  
            checkImage("image58");  
            checkImage("image59");  
            System.out.println("-------------- 10 min ... ");
            try {
                Thread.sleep(10 * 60 * 1000); // 1 min
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
}
