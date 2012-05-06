/*
 * MyCookiesStore.java
 *
 * Created on 15. duben 2007, 19:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package travian;

import java.net.*;
import java.util.*;

class MyCookieStore 
   implements CookieStore, Runnable
{
    static CookieStore store = new CookieManager().getCookieStore();

    public MyCookieStore() {
        // get the default in memory cookie store
        
         
        
        // todo: read in cookies from persistant storage
        // and add them store
        loadCooks();

        // add a shutdown hook to write out the in memory cookies
        Runtime.getRuntime().addShutdownHook(new Thread(this)); 
    }

    private void loadCooks() {
///                                                                             2EzN5AzN1cTMxojN2ATM4UjN3ETM6cjOzYWO3EWMyIGM2ozY1R3brpzM2QTO6MjN0kzIzYDN5MyM2QTO
///                                                                                     %3D%3DQN3AjM2ojNwkTM4UjN3ETM6cjOzYWO3EWMyIGM2ozY1R3brpzM2QTO6AzIwMyM2QTO
//        add(URI.create("http://s2.travian.cz/dorf1.php"), new HttpCookie("T3E", "3cDN5oDNzEzM4UjN3ETM6cjOzYWO3EWMyIGM2ozY1R3brpzM2QTO6AzIwMyM2QTO"));
///        store.add(URI.create("http://s2.travian.cz/"), new HttpCookie("T3E", "2EzN5AzN1cTMxoDOzEjM4UjN3ETM6cjOzYWO3EWMyIGM2ozY1R3brpzM2QTO6MjN0kzIzYDN5MyM2QTO"));
///                store.add(URI.create("http://s2.travian.cz/"), new HttpCookie("T3E", "%3D%3DQN3ATNxozNyMTM4UjN3ETM6cjOzYWO3EWMyIGM2ozY1R3brpzM2QTO6AzIwMyM2QTO"));
//        add(URI.create("http://kotuc.chytrak.cz"), new HttpCookie("nick", "jenišek"));
    }
    
    private void saveCooks() {
        System.out.println("closing Cookies:");        
        System.out.println(list);        
        System.out.println(uris);
    }
    
    public void run() {
        // todo: write cookies in store to persistent storage
        saveCooks();
    }

    
    public void	add(URI uri, HttpCookie cookie) {
        MainFrame.cookieField.setText(cookie.getValue());
        store.add(uri, cookie);
        list.clear(); // i want only the specivic T3E cookie. no duplicity
        list.add(cookie);
        uris.add(uri);
        System.out.println("new cookie: uri: "+uri+"; hc: "+cookie+";");    
    }

    List<HttpCookie> list = new LinkedList<HttpCookie>();
    
    List<URI> uris = new LinkedList<URI>();
    
    public List<HttpCookie> get(URI uri) {
        System.out.println("getting cookies for uri:"+uri+" = "+list);
//        return store.get(uri);
        return list;
    }

    public List<HttpCookie> getCookies() {
        System.out.println("get cookies");
//        return store.getCookies();
        return list;
    }
    
    public List<URI> getURIs() {
        System.out.println("get uris");
        return uris;
//        return store.getURIs();
    }

    public boolean remove(URI uri, HttpCookie cookie) {
        System.out.println("removing cookie uri:"+uri+" hc:"+cookie);
        return store.remove(uri, cookie);
    }

    public boolean removeAll()  {
        System.out.println("cookies remove all");
        return store.removeAll();
    }
}