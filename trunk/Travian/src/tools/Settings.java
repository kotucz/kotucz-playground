package tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
        throw new IllegalStateException("");
//        try {
//            if (!new File(ROOT).exists()) {
//                firstUseSetup();
//            }
//            System.out.println("Root directory: "+ROOT);
//            properties = new Properties();
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        store();
//                    } catch (IOException ex) {
//                        Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            });
//            properties.load(new FileReader(propertiesFile));
//        } catch (IOException ex) {
//            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static void store() throws IOException {
        properties.store(new FileWriter(propertiesFile), "Tluda config file");
    }
    
    private static void firstUseSetup() {
        System.out.println("FIRST USE .. creating settings dir");
        try {
            new File(ROOT).mkdir();
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
    
    public static String getProperty(String key, String def) {
        String val = getProperty(key);
        if (val==null) {
            val = def;
            setProperty(key, val);
        }
        return val;
    }
    
    public static void setProperty(String key, String value) {
        if (properties==null) {
            init();
        }
        properties.setProperty(key, value);
    }
    
    
    /**
     * @param name relative file name to settings root
     * @return file in settings root
     */
    public static File getFile(String name) {
        return new File(ROOT+name);
    }
    
}
