package kotuc;

import soccer.*;

public class UI_Kotuc extends PascalTeam {

    public void ui(Player[] a, Player[] b, Ball mic, int strana) {
        
/*variables*/
 int i, nejblizsi;
         double min, mina;
         String ahoj;
        
/*end variables*/

        
// METHOD CALL jdi_na()
            jdi_na(a[1],strana*620+10,mic.y);
            
            /* commentpr0m3n4_dl0uh4_4_5_c15l4m4 */
            
//VARIABLE ahoj:=?;
            ahoj = "ahojtyanecum";
            
//VARIABLE min:=?;
            min = 10000;
            
//VARIABLE nejblizsi:=?;
            nejblizsi = 0;
            
//KEYWORD for
 for (i = 2; i<=MAX_HRACU; i++) 
//KEYWORD begin
{
                
//KEYWORD if
if (vzdalenost(a[i].x,a[i].y,mic.x,mic.y)<min) 
//KEYWORD begin
{
                    
//VARIABLE min:=?;
                    min = vzdalenost(a[i].x,a[i].y,mic.x,mic.y);
                    
//VARIABLE nejblizsi:=?;
                    nejblizsi = i;
                    
//KEYWORD end

//END end
                    }
                
                
                
//UNRECOGNIZED TOKEN : BADNAME char;(59)
                
//KEYWORD end

//END end
                }
            
            
            
//UNRECOGNIZED TOKEN : BADNAME char;(59)
            
//KEYWORD if
if (nejblizsi>1) 
//KEYWORD begin
{
                
// METHOD CALL jdi_na()
                jdi_na(a[nejblizsi],mic.x+random(20)-10,mic.y+random(20)-10);
                
                
//KEYWORD end

//END end
                }
            
            
            
//UNRECOGNIZED TOKEN : BADNAME char;(59)
            
//KEYWORD for
 for (i = 1; i<=MAX_HRACU; i++) 
//KEYWORD begin
{
                
// METHOD CALL kopni_mic()
                kopni_mic(a[i],mic,639-strana*638,random(160)+160,V_MICE);
                
                
//KEYWORD end

//END end
                }
            
            
            
//UNRECOGNIZED TOKEN : BADNAME char;(59)
            
//KEYWORD end

//END end
            
        } // end method kotuc
        
        
    } // class end. 
