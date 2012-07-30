
public class KotucSoldier extends Soldier3D {
	
	/**
	 * Method KotucSoldier
	 *
	 *
	 */
	public KotucSoldier() {
		super();
		// TODO: Add your code here
	}	
	
	public void run() {
//		while (true) {
//			go(1);
//			turnRight(1);
//			shoot();
//		}
	}
	
	public void doEveryFrame() {
//		go(1);
		turnRight(1);
//		shoot();
	}
	
	public String toString() {
		return "Kotuc"+super.toString();
	}
}
