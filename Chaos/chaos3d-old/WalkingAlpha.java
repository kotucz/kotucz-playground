package cz.kotuc.chaos;

public class WalkingAlpha extends javax.media.j3d.Alpha {
	
	/**
	 * Method WalkingAlpha
	 *
	 *
	 */
	Humanoid entity;
	 
	public WalkingAlpha(Humanoid entity) {
		this.entity = entity;
	}
	
	public float value () {
		entity.walking+=entity.getVel().length()/60.0;
		return entity.getWalking();
	}
	
	public float value (long l) {
		return value();
	}	
}
