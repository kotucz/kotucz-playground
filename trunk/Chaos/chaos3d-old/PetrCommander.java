import cz.kotuc.chaos.*;

public class PetrCommander extends Commander {
	
	/**
	 * Method main
	 *
	 *
	 * @param args
	 *
	 */
	public static void main(String[] args) {
	}

	/**
	 * Method PetrSoldier
	 *
	 *
	 */
	public PetrCommander() {
		super();
		setName("Petr");	
	}	
	
	public void run() {
		go(100);
		while (true) {
			//go(1);
			//turnLeft(1);
			//go(50);
			turnRight(1);
			//shoot();
			canSeeSoldier();
			
		}
	}
	
	
	
	public String toString() {
		return "Petr"+super.toString();
	}
}
