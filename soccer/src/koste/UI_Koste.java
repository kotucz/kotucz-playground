package koste;

import soccer.*;

public class UI_Koste extends Team {

		
    public void ui(Player[] a, Player[] b, Ball mic, int strana) {
        
		
        double mindist = 300;
        int mini=0;
                
        for (int i = 0; i<a.length; i++) {
            if (a[i].distance(getBall())<mindist) {
                mini = i;
                mindist = a[i].distance(getBall());
            }
        };
        
        a[mini].goTo(getBall().x, getBall().y);       
        
        kickBall((1-getSide())*Pitch.WIDTH/2, Math.random()*Pitch.HEIGHT);
        
//        for (Player p1:a) {
//            if (p1.n==1) {
//                p1.goTo(getBall().x, getBall().y);
//            }
//        }
        } // end method ui
        
        
    } // class end. 
