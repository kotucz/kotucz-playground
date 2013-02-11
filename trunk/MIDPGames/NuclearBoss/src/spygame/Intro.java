package spygame;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

import javax.microedition.media.*;
import javax.microedition.media.control.ToneControl;
import spygame.*;

public class Intro extends Canvas {
	
	Image image;
	/**
	 * Method Intro
	 *
	 *
	 */
    public Intro() {
        setFullScreenMode(true);
      
        image = Game.createImage("tegies");
   }
    
    public void paint(Graphics g) {
    	g.setColor(0xFFFF0000);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, getWidth()/2, getHeight()/2, g.HCENTER|g.VCENTER);

    }	
}
