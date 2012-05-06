package soccer;

public abstract class PascalTeam extends Team {
	
	public static final int MAX_HRACU = Pitch.numPlayers;
	
	public static final int V_MICE = 10;
	public static final int V_HRACE = 5;
	
//	public void ui () {
//		ui(getPlayers(), getOpponents(), getBall(), (getSide()>0)?1:0);
////		does nothing, since pascal is using	public abstract void ui(Player[] plays, Player[] opps, Ball ball, int strana) ;
////		calls pascal ui
//	}
	
	public abstract void ui(Player[] plays, Player[] opps, Ball ball, int strana) ;
	
	public double vzdalenost(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
	}
	
	public int random(int i) {
		return (int)Math.floor(Math.random()*i);
	}
	
	public int round(double i) {
		return (int)Math.round(i);
	}
	
	public void jdi_na(Player p, double x, double y) {
		if (p!=null) p.goTo(x, y);
	}
	
	public void kopni_mic(Player p, Ball b, double x, double y, double v) {
//		kickBall(x, y);
        if (p.distance(b)<Player.RANGE) b.kick(x-b.x, y-b.y, v);
	}
	
	/**
	 * returns players field converted to pascal array system [1..MAX_HRACU]
	 * the [0] field is null
	 * 
	 * !not null since null pointer exception is thrown
	 * 
	 */
	public Player[] getPlayers() {
		Player[] pls = super.getPlayers();
		Player[] pps = new Player[pls.length+1];
		pps[0] = new Player();
		for (int i = 0; i<pls.length; i++) {
			pps[i+1] = pls[i];
		}
		return pps;
	}

	/**
	 * returns players field converted to pascal array system [1..MAX_HRACU]
	 * the [0] field is null
	 * 
	 * !not null since null pointer exception is thrown
	 */
	public Player[] getOpponents() {
		Player[] ops = super.getOpponents();
		Player[] pops = new Player[ops.length+1];
		pops[0] = new Player();
		for (int i = 0; i<ops.length; i++) {
			pops[i+1] = ops[i];
		}
//		System.out.println("opps:"+pops);
		return pops;
	}
	
}
