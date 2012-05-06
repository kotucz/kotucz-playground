package soccer;

public class Duck extends Team {
	public void ui() {
	       
        double mindist = 300;
        int mini=0;
        
        
        for (int i = 0; i<players.length; i++) {
            if (players[i].distance(getBall())<mindist) {
                mini = i;
                mindist = players[i].distance(getBall());
            }
        };
        
        players[mini].dx = getBall().x;
        players[mini].dy = getBall().y;       
        
        
        kickBall((1-getSide())*Pitch.WIDTH/2, Math.random()*Pitch.HEIGHT);
        
//        for (Player p1:plays) {
//            if (p1.n==1) {
//                p1.dx = getBall().x;
//                p1.dy = getBall().y;
//            }
//        }
    }
}
