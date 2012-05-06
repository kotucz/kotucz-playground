/*
 * Pitch.java
 *
 * Created on 1. bøezen 2007, 21:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package soccer;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.image.*;
import javax.swing.*;



/**
 *
 * @author PC
 */
public class Pitch extends Applet 
implements KeyListener, MouseListener, Runnable {

	/** Creates a new instance of Pitch */
	public Pitch() {
	}

	
	public static final int LEFTX = 3;
	public static final int TOPY = 3;
	public static final int CENTX = 320;
	public static final int CENTY = 240;
	public static final int RIGHTX = 637;
	public static final int BOTTOMY = 477;
		
	public static final int WIDTH = 640;//getWidth();
	public static final int HEIGHT = 480;//getHeight();
	
	static int numPlayers = 5;
	static boolean ballType = true;
//	static int playerVers = 0;
	static int delay = 100;
	static int maxTick = 4000;	
	
	String[] teamNames = loadTeamNames(); 
		
		
	static String[] loadTeamNames() {
		return new String[] {
				"soccer.Duck",
				"kotuc.UI_Kotuc",
				"kotuc.GTeam",
				"kotuc.EvolTeam2",
				"kotuc.AtomTeam",
				"petr.UI_Petr",
				"koste.UI_Koste",
				"kotuc.FinalTeam",
				"kotuc.VizTeam"
		};
	}
	
	int team1index=5, team2index=7;
	
	/** Initialization method that will be called after the applet is loaded
	 *  into the browser.
	 */
	public void init() {
		// TODO start asynchronous download of heavy resources

		this.addMouseListener(this);
		this.addKeyListener(this);
		setSize(WIDTH, HEIGHT);
		
		try {
			kickClip = getAudioClip(getClass().getResource("/soccer/data/punch.wav"));
			goalClip = getAudioClip(getClass().getResource("/soccer/data/ding.wav"));
//			melodyClip = getAudioClip(getClass().getResource("/soccer/data/melody.wav"));
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		newDuel(team1index, team2index);

	}

	static AudioClip kickClip, melodyClip, goalClip;
	
	public void newDuel(int id1, int id2) {
		team1index = id1;
		team2index = id2;
		newDuel(teamNames[team1index], teamNames[team2index]);
	}
	
	public void newDuel(String team1classname, String team2classname) {
		try {
			team1 = (Team)Class.forName(team1classname).newInstance();
			team2 = (Team)Class.forName(team2classname).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		team1 = new Team();        
//		team2 = new Team();       

		team1.setSide(-1);
		team2.setSide(1);
		team1.setTeamColor(Color.RED);
//		team1.name = "RED Team";
//		team2.name = "GREEN Team";
		team2.setTeamColor(Color.GREEN);
		team1.opponents = team2.players;
		team2.opponents = team1.players;
		Team.pitch = this;

		ball = new Ball();
		ball.original = ball;
		
		if ("kotuc.AtomTeam".equals(team2.getClass().getName())) addMouseListener((MouseListener)team2);
		
		startTime = System.currentTimeMillis();

		tick = 0;
		
//		new Timer().schedule(new RTTask(), 100);
//new Timer().schedule(new RTTask(), 100, 50);


//		bgClip.loop();

		
//		new Thread(team1).start();
//		new Thread(team2).start();
		System.out.println("started");
	}
	
	// TODO overwrite start(), stop() and destroy() methods

	Thread thread;

	public void start() {
		if (thread==null) {
			thread=new Thread(this);
			thread.start();			
		}

	} 

//	java.util.List<Player> players = new LinkedList(); 

	static Team team1, team2;   

	public static int getScore(int side) {
		if (side==-1) return team1.score;
		if (side==1) return team2.score;
		return -1; 
	}

	Ball getBall() {
		return ball;
	}

	protected static Ball ball;

	protected void doTeamMoves() {

		team1.ui0();
		team1.doMoves();

		ball.doHalfMove();

//		tick++;

		team2.ui0();
		team2.doMoves();

		ball.doHalfMove();

		tick++;

//		for (Player p1:players) {


//	for (Vert v2:verts) {
//	if (v1!=v2) v1.apply(v2);
//}
//}
	}

	protected void doTeamMovesWithRandom() {

		boolean swap = Math.random()<0.5;
		
		if (swap) team2.ui0(); else team1.ui0();	
//		tick++;
		if (swap) team1.ui0(); else team2.ui0();
				
		team1.doMoves();
		ball.doHalfMove();
		team2.doMoves();
		ball.doHalfMove();

		tick++;

	}
	
	private Font font1 = new Font("Arial", Font.BOLD, 14);

	protected long startTime;

	protected long tick;

	void paintField(Graphics g) {
//		System.out.println("paintfield");        


		g.drawLine(320, 0, 320, 480);
		g.drawRect(1, 1, WIDTH-2, HEIGHT-2);


		g.setColor(Color.ORANGE);        
//		g.fillRect(1, 1, width-2, height-2);
		g.drawRect(580, 160, 60, 160);
		g.drawRect(0, 160, 60, 160);




		team1.paint(g);
		team2.paint(g);
		ball.paint(g);

		g.setFont(font1);

		g.setColor(team1.getTeamColor());
		g.drawString(""+team1.score, 320+team1.side*100, 40);
		g.drawString(""+team1.name, 240+team1.side*200, 40);

		g.setColor(team2.getTeamColor());
		g.drawString(""+team2.score, 320+team2.side*100, 40);
		g.drawString(""+team2.name, 240+team2.side*200, 40);

		g.setColor(Color.CYAN);

		g.drawString("time:  "+(System.currentTimeMillis()-startTime)/60000+":"+((System.currentTimeMillis()-startTime)%60000)/100, 50, 60);
		g.drawString("tick:  "+tick, 50, 80);
		g.drawString("delay: "+delay, 50, 100);

       
	}

	private BufferedImage imageBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	public void paint(Graphics g) {

		g.drawImage(imageBuffer, 0, 0, null);

	}

	
	
	public void flushGraphics() {
		
		BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		paintField(bi.getGraphics());
		imageBuffer = bi;
		repaint();

	}

	public void run() {

		long lastflush=0;
		
		int j=0;
		while (true) {

			if (thread!=Thread.currentThread()) break;
			
			if (tick==maxTick) break;
			
			try {

				long t = System.currentTimeMillis();
				
				doTeamMovesWithRandom();

//				System.out.println("benchmoves: "+bench());
				
//				if ((j%1)==0) {
					flushGraphics();
//				}

/*				if ((System.currentTimeMillis()-lastflush)>100)	{
					flushGraphics();
					lastflush = System.currentTimeMillis();
				}
*/				
				
				t = delay - (System.currentTimeMillis() - t);
				
				if (t>0)
					try {
						Thread.sleep(t);
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
//				if ((j%10)==0) System.out.println("bench10paint: "+bench());

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			j++;
		}
	}
//	}


	private long benchstart;
	public long bench() {
		long b = benchstart;
		benchstart = System.currentTimeMillis();
		return benchstart-b; 
	}
		
	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {
//		System.out.println(""+e);
		if ('-'==e.getKeyChar()) if (delay>1) delay/=2;
		if ('+'==e.getKeyChar()) delay*=2;
	}


	public void keyTyped(KeyEvent e) {
		
	}

	public void update(Graphics g) {
		paint(g);
	}


//	enum Action {
//	NEW_VERT, 
//SELECT_VERT,
//NO
//}

//	public Action action = Action.NEW_VERT;

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}


	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
		//   	pascal.TransForm.main(null);
		if (e.getButton()==e.BUTTON3) showUpSets();  
	}
	
	SettingsFrame setsFrame;
	
	public void showUpSets() {
		if (setsFrame!=null) setsFrame.setVisible(false);
		setsFrame = new SettingsFrame();
		setsFrame.setVisible(true);
	}
	
	class SettingsFrame extends JFrame {
		
		SettingsFrame() {
			reset();
		}
		
		Choice team1chooser;
		Choice team2chooser;
		Checkbox ballcheckbox;
		TextField numplayerstextfield;
		
		public void reset() {
			setLayout(new FlowLayout());
			
			team1chooser = new Choice();
			team2chooser = new Choice();
			
			for (String tn:teamNames) {
				team1chooser.add(tn);
				team2chooser.add(tn);
			}
			
			team1chooser.select(team1index);
			team2chooser.select(team2index);
			
			Button startButton = new Button("Start");
			
			startButton.addActionListener(new ActionListener() {

				/* (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent arg0) {
					ballType = ballcheckbox.getState();
					numPlayers = Integer.valueOf(numplayerstextfield.getText());
					newDuel(team1chooser.getSelectedIndex(), team2chooser.getSelectedIndex());					
				}
				
			});
			
			ballcheckbox = new Checkbox("pascal ball", ballType);
			numplayerstextfield = new TextField(""+numPlayers, 2);
			
			add(team1chooser);
			add(team2chooser);
						
			add(startButton);
			
			add(numplayerstextfield);
			
			add(ballcheckbox);
			
			setSize(200, 150);
		}
		

		
	}

	
}


