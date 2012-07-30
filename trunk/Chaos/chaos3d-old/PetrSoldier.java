
public class PetrSoldier extends Soldier3D {
	
	/**
	 * Method main
	 *
	 *
	 * @param args
	 *
	 */
	public static void main(String[] args) {
		// TODO: Add your code here
	}

	/**
	 * Method PetrSoldier
	 *
	 *
	 */
	public PetrSoldier() {
		super();
		// TODO: Add your code here
	}	
	
	public void run() {
		go(10);
		while (true) {
			/*go(1);
			turnLeft(1);
			go(10);
			turnRight(10);*/
			shoot();
			
		}
	}
	
	public void doEveryFrame() {
		go(1);
//		turnLeft(1);
//		go(10);
//		turnRight(10);
//		shoot();
	}
	
	public String toString() {
		return "Petr"+super.toString();
	}
}
