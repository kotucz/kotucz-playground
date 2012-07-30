import cz.kotuc.chaos.*;

public class KotucCommander extends Commander {
	
	/**
	 * Method KotucSoldier
	 *
	 *
	 */
	public KotucCommander() {
		super();
		setName("Kotuc");
	}	
	
	public void run() {
		go(80);
		while (true) {
			go(1);
			turnRight(1);
			shoot();
			canSeeSoldier();
		}
	}
	
	public String toString() {
		return "Kotuc"+super.toString();
	}
}
