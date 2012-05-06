package player;

import soccer.*;

public class UI_Kotuc extends PascalTeam {

  
  public void ui(Player[] a, Player[] b, Ball mic, int strana) {
    
/*variables*/

    int i = 0;
    int nejblizsi = 0;
    double min = 0.0;
    double mina = 0.0;
    String ahoj = "";
    
/*end variables*/

      
      jdi_na(a[1],strana*620+10,mic.y);
      /* commentpr0m3n4_dl0uh4_4_5_c15l4m4 */
      ahoj = "ahojtyanecum";
      min = 10000;
      nejblizsi = 0;
      
      for (i = 2; i<=MAX_HRACU; i++) 
        {
          
          if (vzdalenost(a[i].x,a[i].y,mic.x,mic.y)<min) 
            {
              min = vzdalenost(a[i].x,a[i].y,mic.x,mic.y);
              nejblizsi = i;
            }
        }
      
      switch (i) {
        case 0:
          min = 640/3;
        break;
        
        case 1:
          min = 640-640/3;
        break;
        
        case 2:
          min = 640/2;
        break;
        
      } // end switch 
      
      if (nejblizsi>1) 
        {
          
          jdi_na(a[nejblizsi],mic.x+random(20)-10,mic.y+random(20)-10);
        }
      
      for (i = 1; i<=MAX_HRACU; i++) 
        {
          
          kopni_mic(a[i],mic,639-strana*638,random(160)+160,V_MICE);
        }
    } // end method kotuc
} // class end. 

