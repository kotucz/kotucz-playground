package kotucz.village.common;

import kotucz.village.build.Building;
import kotucz.village.tiles.GenericGrid;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Pos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kotuc
 */
public class Entities<T> {


    public static final String ID_KEY = "MyUserDataIdKey";

    //    private Vector3f destShort = new Vector3f();

    private static final AtomicInteger idGen = new AtomicInteger();

    synchronized public static String acquireUniqueKey(Object target) {
        return target.getClass().getSimpleName()+idGen;
    }

    public final Map<String, T> map = new HashMap<String, T>();


    public void put(String key, T obj) {
         map.put(key, obj);
    }

    public T find(String key) {
        return map.get(key);
    }

}
