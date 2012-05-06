/*
 * HTTP.java
 *
 * Created on 31. srpen 2007, 14:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Kotuc
 */
public class HTTP {

    private static CookieStore cookiesStore = new MyCookieStore();

    public static String getSource(URL url, String postdata) throws IOException {

        String server = url.getHost();
        System.out.println("server: " + server);

// Construct data
// TODO send cookies
        String cookies = "no cookies";
//            String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
//            data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

        // Send data
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
// sending cookies
        conn.setRequestProperty("Cookie", cookies);

// sending form data POST method
        if ((postdata != null) && (postdata.length() > 0)) {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(postdata);
            wr.flush();
            wr.close();
        }


// Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

// reading cookies
        String inCookies = conn.getRequestProperty("Set-Cookie");
        if (inCookies!=null) {
            for (String cook1: inCookies.split(";")) {
                String cookieName = cook1.split("=")[0];
                String cookieValue = cook1.split("=")[1];
                System.out.println("name:" + cookieName + " value:" + cookieValue);
                cookiesStore.add(URI.create(server), new HttpCookie(cookieName, cookieValue));
            }         
        }

        String line;
        StringBuffer content = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            content.append(line + "\n");
        }

        rd.close();
        return content.toString();
    }

}
