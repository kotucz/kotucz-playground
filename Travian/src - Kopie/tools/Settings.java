package tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Settings {
    
    private static String ROOT = System.getProperty("user.home")+"/.tluda/";
    private static File propertiesFile = getFile("tluda.properties");
    private static Properties properties;
    
    private static void init() {
        try {
            if (!new File(ROOT).exists()) {
                firstUseSetup();
            }
            properties = new Properties();
            properties.load(new FileReader(propertiesFile));
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void firstUseSetup() {
        System.out.println("FIRST USE .. creating settings dir");
        try {
            new File(ROOT).createNewFile();
            propertiesFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
              
    public static String getProperty(String key) {
        if (properties==null) {
            init();
        }
        return properties.getProperty(key);
    }
    
    /**
     * @param name relative file name to settings root
     * @return file in settings root
     */
    public static File getFile(String name) {
        return new File(ROOT+name);
    }
    
}
