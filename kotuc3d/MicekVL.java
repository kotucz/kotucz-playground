import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

class MicekVL extends Thread {
	public int x = 50;
	public int y = 50;
	public int width = 32;
	public int height = 32;
	
	public void run () {
		this.x+=5;
		this.y+=5;

		try {
			sleep(100);
		}catch (InterruptedException e) {}
	}
}
	