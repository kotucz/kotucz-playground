/*
 * HTTP.java
 *
 * Created on 31. srpen 2007, 14:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tluda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import travian.MyCookieStore;

/**
 *
 * @author PC
 */
public class HTTP {
    
    /** Creates a new instance of HTTP */
    public HTTP() {
        
    }
    
    protected static CookieStore cookiesStore = new MyCookieStore();
    
    public static String getSource(URL url) {
        return null;
    }
    
    public static Page openPage(URL url, String postdata/*, String cookie*/) {
        
        String cookies = "";
        for (HttpCookie hc:cookiesStore.getCookies()) {
            cookies += hc.toString()+"; ";
        }
        
        String serverurl = url.getHost();
        println("server: "+serverurl);
        
        Page page = new Page();
        
        try {
            // Construct data
//            String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
//            data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");
            
            // Send data
            println("openPageCookies:"+url+" pripojovani.. ");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
// sending cookies
            conn.setRequestProperty("Cookie", cookies);
            
// sending form data POST method
            if ((postdata!=null)&&(postdata.length()>0)) {
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(postdata);
                wr.flush();
                wr.close();
            }
            
            
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
// reading headers
//            println("headers:");
            String headerName=null;
            for (int i=1; (headerName = conn.getHeaderFieldKey(i))!=null; i++) {
//                println("header: "+headerName+" "+conn.getHeaderField(i));
                if (headerName.equals("Set-Cookie")) {
                    String cookie1 = conn.getHeaderField(i);
                    println("cookie1:");
                    cookie1 = cookie1.substring(0, cookie1.indexOf(";"));
                    String cookieName = cookie1.substring(0, cookie1.indexOf("="));
                    String cookieValue = cookie1.substring(cookie1.indexOf("=") + 1, cookie1.length());
                    println("name:"+cookieName+" value:"+cookieValue);
                    cookiesStore.add(URI.create(serverurl), new HttpCookie(cookieName, cookieValue));
                }
            }
            
            // clean reading page
            String line;
//            plainPane.setText("");
            StringBuffer content = new StringBuffer();
//            plainPane.setText(null);
//            htmlPane.setText(null);
//            htmlPane.getEditorKit().createDefaultDocument();
//            clearHtmlPane();
//            println("plainpane:"+plainPane.getText());
//            println("htmlpane:"+htmlPane.getText());
            while ((line = rd.readLine()) != null) {
                content.append(line+"\n");
            }
            
//          plainPane.setText("url:"+url+"\npostdata:"+datapost+"\ncookies:"+cookies+"\n      ------------        \n"+content.toString());
            
            
            rd.close();
            println("hotovo");
            
            page.url = url;
            page.content = content.toString();
            page.parseHTML();
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static void println(String text) {
        System.out.println(text);
    }
    
}
