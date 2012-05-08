package tradeworld.graphics;

import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.Link;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;

/**
 *
 * @author Kotuc
 */
public enum Models {

    INSTANCE;

    static Models getInstance() {
        return INSTANCE;
    }
    private Map<String, SharedGroup> cache = new HashMap<String, SharedGroup>();

    void store(String name, Node node) {
        final SharedGroup sharedGroup = new SharedGroup();
        sharedGroup.addChild(node);
        cache.put(name, sharedGroup);
    }

    Node createLink(String name) {
        Link link = new Link(cache.get(name));
        return link;
    }
}
