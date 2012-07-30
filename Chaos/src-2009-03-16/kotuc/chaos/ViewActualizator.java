package kotuc.chaos;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.picking.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.image.*;


/**
 *	@deprecated
 */
public class ViewActualizator extends Behavior {
	WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);
	
	private Player player;
	
	public ViewActualizator (Player player) {
		this.player = player;
		player.addBehavior(this);
	}
	
	public void initialize () {
		wakeupOn(w);			
	}

	public void processStimulus(java.util.Enumeration en) {
					
//	actualization of background
					
//	setViewBehindPlayer(su); 
//	setViewPlayersEyes(su);
	
		wakeupOn(w);					
	}
}
