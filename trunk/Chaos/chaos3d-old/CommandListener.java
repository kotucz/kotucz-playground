package cz.kotuc.chaos;

public interface CommandListener {
	
	public void goForward();
	public void goBack();
	public void goLeft();
	public void goRight();
	
	public void turnRight();
	public void turnLeft(); 
	
	public void shoot();
	
	public boolean aimed();
}
