package kotuc;

class WConfig implements Cloneable {
	
	private static final int NUM_PARAMS = W.values().length;
	
	public WConfig() {
		System.out.println("WConfig num params : "+NUM_PARAMS);
		this.cuid = counter1++;
		weights = new double[NUM_PARAMS];
		
		config1();

	}
	
	double[] weights;
	/*
	static String[] names;
	
	static {
	names = new String[NUM_PARAMS];
	
	names[W.toobad.ordinal()] = "badB";
	names[W.toogood.ordinal()] = "gudB";
	names[W.agresivity.ordinal()] = "agrsB";
	names[W.agresivityexp.ordinal()] = "agrseB";
	names[W.careness] = "careB";
	names[W.teamcoop] = "coopB";
	names[W.randomizing] = "ranB";
	
	names[W.toobadloc] = "badL"; 
	names[W.toogoodloc] = "gudL";
	names[W.blockingloc] = "blckL"; 
	names[W.repelency] = "repL"; 
	names[W.repelencyexp] = "repeL"; 
	names[W.locstability] = "locsL";
	names[W.agresivityloc] = "agrL"; 
	names[W.randomizingloc] = "ranL";  
	}
*/
	
	private static int counter1 = 1;
	
	int cuid;

	void config0() {
		set(W.toobad, 3.5); 
		set(W.toogood, 50); 
		set(W.agresivity, 6);
		set(W.agresivityexp, 1.5); 
		set(W.careness, 1);
		set(W.teamcoop, 1);
		set(W.randomizing, 0.1); 
		
		set(W.toobadloc, 3);
		set(W.toogoodloc, 50); 
		set(W.blocking, 2); 
		set(W.repelency, 3); 
		set(W.repelencyexp, 2); 
		set(W.locstability, 1);
		set(W.agresivityloc, 3); 
		set(W.randomizingloc, 0.1);  

	}
	
	void config1() {
		set(W.toobad, 3.5); // X
		set(W.toogood, 50); // X
		set(W.agresivity, 6);
		set(W.agresivityexp, 1.5); // X
		set(W.careness, 1);
		set(W.teamcoop, 1);
		set(W.randomizing, 0.1); // X
		
		set(W.toobadloc, 3); // X
		set(W.toogoodloc, 50); // X
		set(W.blocking, 1); 
		set(W.repelency, 1); // 50 percent
		set(W.repelencyexp, 2); // X
		set(W.locstability, 1);
		set(W.agresivityloc, 3); 
		set(W.randomizingloc, 1); // X  

	}

	void configAp() {
		set(W.toobad, 3.5);
		set(W.toogood, 10);
		set(W.agresivity, 4);
		set(W.agresivityexp, 1.5);
		set(W.careness, 4);
		set(W.teamcoop, 2);
		set(W.randomizing, 0.1); 
		set(W.toobadloc, 3);
		set(W.toogoodloc, 9); 
		set(W.blocking, 3); 
		set(W.repelency, 3); 
		set(W.repelencyexp, 2); 
		set(W.locstability, 1);
		set(W.agresivityloc, 2); 
		set(W.randomizingloc, 0.1);  

	}
	
	
	
	
	

	
	
	
	int homescore = 0;
	int hostscore = 0;
	
	
	public String toString() {
		String cont = "CFG"+cuid+" ";
//		for (int i = 0; i<NUM_PARAMS; i++) {
		for (W w:W.values()) {
			cont += w.name() + ":" + get(w);
//			cont += getName(i) + ":" + get(i);
		}
		return cont;
//		return "CFG"+cuid+" BALL[bad"+toobad+"good"+toogood+", agres"+agresivity+", careness"+careness+", teamcoop"+teamcoop+", randomizing"+randomizing+"]"+
//		"LOC[bad"+toobadloc+"good"+toogoodloc+", agres"+agresivityloc+", blocking"+blockingloc+", repelency"+repelency+"stability"+locstability+"random"+randomizingloc;
	}

//	class WConfigFrame extends Frame {
//		WConfigFrame () {
//			add(new Label(toString()));
//		}
//	}

	double get(W w) {
		return weights[w.ordinal()];
	}
	
	void set(W w, double value) {
		weights[w.ordinal()] = value;
	}
	
/*	String getName(int id) {
		return names[id];
	}
	*/
	double[] changes = new double[NUM_PARAMS];
	
	public void mutate(double random) {
		for (int i = 0; i<weights.length; i++) {
			changes[i] = (0.5-Math.random())*random;
			weights[i] *= 1+changes[i]; 
		}
	}
	
/*	
	public void change(int varid, double delta) {
		switch (varid) {
		case 1: agresivity += delta; break;
		case 2: agresivityexp += delta; break;
		case 3: blockingloc += delta; break;
		case 4: careness += delta; break;
		case 5: locstability += delta; break;
		case 6: randomizing += delta; break;
		case 7: randomizingloc += delta; break;
		case 8: repelency += delta; break;
		case 9: repelencyexp += delta; break;
		case 10: teamcoop += delta; break;
		case 11: toobad += delta; break;
		case 12: toobadloc += delta; break;
		case 13: toogood += delta; break;
		case 14: toogoodloc += delta; break;
		case 15: agresivityloc += delta; break;
		}
		
	}
*/	
	double getFragrate() {
					
		return ((double)(homescore+1)/(double)(hostscore+1));
	
	}
	
	public Object clone() {
		try {
			WConfig wc2 = (WConfig)super.clone();
			wc2.cuid = counter1++;
			wc2.weights = this.weights.clone();
			wc2.changes = this.changes.clone();
			return wc2;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}

/**
*  
* toobad, toobadloc - when quality is less than too bad automaticaly disabled not to take much place
* toogood, toogoodloc - when quality is higher, can not automaticaly disable
* agresivity, agresivityexp , dependence of distance to goal
* careness - awaring of enemy players when kicking ball
* teamcoop - watch team-mates and play to them
* randomizing, randomizingloc
* amount of possibilities replaced every turn 
* 0 .. no 1 .. all
* 
* blockingloc - locations between enemy are prefered to prevent him from getting the ball
* repelency, repelencyexp - weight of concentration repelency forces between players
* locstability - prevents to going to another position across all pitch 
* agresivityloc - prefering position before goal
* 
*/
enum W {
	toobad,
	toogood,
	agresivity,
	agresivityexp,
	careness,
	teamcoop,
	randomizing,
	toobadloc,
	toogoodloc,
	blocking,
	repelency, 
	repelencyexp, 
	locstability,
	agresivityloc,
	randomizingloc;  
	
}


