package cz.kotuc.chaos;

public class OnSoldierDetectedEvent {
	private double angle;
	private double distance;
	
	/**
	 * Method OnSoldierDetectedEvent
	 *
	 *
	 */
	public OnSoldierDetectedEvent(double angle, double distance) {
		this.angle = angle;
		this.distance = distance;
	}	
	
	public double getAngle() {
		return this.angle;
	}
	
	public double getDistance() {
		return this.distance;
	}
}
