package tools;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class Link {

    Page page;
    final URL url;
    String desc;
    List<URL> thumbs = new ArrayList<URL>();

    public Link(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public String getTitle() {
        return desc;
    }

    public List<URL> getThumbs() {
        return thumbs;
    }

    @Override
    public String toString() {
        return url.toString() + ":" + desc;
    }

    void addThumb(URL url) {
        thumbs.add(url);
    }
}
