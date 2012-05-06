package petr;

import soccer.*;

public class UI_Petr extends PascalTeam {

  int returndruhy;
  double returnx, returny;
	
  public int nejblizsi_hrac (Player[] a, Player[] b, Ball mic, double xin, double yin, int druhyint) {
    
/*variables*/

    int i = 0;
    int nej_a = 0;
    int nej_b = 0;
    int tahu = 0;
    double min_a = 0.0;
    double min_b = 0.0;
    double mic_x = 0.0;
    double mic_y = 0.0;
    
    returnx = xin;
    returny = yin;
    returndruhy = druhyint;
    
/*end variables*/

      mic_x = mic.x;
      mic_y = mic.y;
      tahu = 0;
      
      do {
        tahu = tahu+1;
        
        if (/*tahu!=0*/true) 
          {
            mic_x = mic_x+mic.v_x;
            mic_y = mic_y+mic.v_y;
            
            if (((mic_x<3)||(mic_x>637)||(mic_y<3)||(mic_y>477))) 
              {
                
                if (mic_x<3) 
                  mic_x = 3;
                
                if (mic_x>637) 
                  mic_x = 637;
                
                if (mic_y<3) 
                  mic_y = 3;
                
                if (mic_y>477) 
                  mic_y = 477;
                mic.v_x = 0;
                mic.v_y = 0;
              }
          }
        min_a = 10000;
        nej_a = 0;
        
        for (i = 1; i<=MAX_HRACU; i++) 
          {
            
            if (i!=returndruhy) 
              
              if (vzdalenost(a[i].x,a[i].y,mic_x,mic_y)<min_a) 
                {
                  min_a = vzdalenost(a[i].x,a[i].y,mic_x,mic_y);
                  nej_a = i;
                }
          }
        min_b = 10000;
        nej_b = 0;
        
        for (i = 1; i<=MAX_HRACU; i++) 
          {
            
            if (vzdalenost(b[i].x,b[i].y,mic_x,mic_y)<min_b) 
              {
                min_b = vzdalenost(b[i].x,b[i].y,mic_x,mic_y);
                nej_b = i;
              }
          }
        min_b = min_b-1;
        //UNRECOGNIZED TOKEN : BADNAME char/(47)
        // ';' expected 
        //UNRECOGNIZED TOKEN : BADNAME char/(47)
        // ';' expected 
        //UNRECOGNIZED TOKEN : V_HRACE
      } while (!(((min_a<tahu*V_HRACE+10)||(min_b<tahu*V_HRACE+15))));
      returnx = mic_x;
      returny = mic_y;
      
      if (min_a<min_b) 
        {
          
    	  returndruhy = nej_b;
          
          
          return nej_a;
        }
        // ';' expected 
      // ';' expected 
      else
        {
    	  returndruhy = nej_a;
          
          return -nej_b;
        }
    } // end method nejblizsi_hrac
  
  public int doleti (Player[] b, Ball mic, double x, double y) {
    
/*variables*/

    int i = 0;
    int nej_b = 0;
    int tahu = 0;
    double min_b = 0.0;
    
/*end variables*/

      tahu = 0;
      
      do {
        tahu = tahu+1;
        
        if (/*tahu!=0*/true) 
          {
            mic.x = mic.x+mic.v_x;
            mic.y = mic.y+mic.v_y;
            
            if (((mic.x<3)||(mic.x>637)||(mic.y<3)||(mic.y>477))) 
              {
                
                if (mic.x<3) 
                  mic.x = 3;
                
                if (mic.x>637) 
                  mic.x = 637;
                
                if (mic.y<3) 
                  mic.y = 3;
                
                if (mic.y>477) 
                  mic.y = 477;
                mic.v_x = 0;
                mic.v_y = 0;
              }
          }
        min_b = 10000;
        nej_b = 0;
        
        for (i = 1; i<=MAX_HRACU; i++) 
          {
            
            if (vzdalenost(b[i].x,b[i].y,mic.x,mic.y)<min_b) 
              {
                min_b = vzdalenost(b[i].x,b[i].y,mic.x,mic.y);
                nej_b = i;
              }
          }
      } while (!(((vzdalenost(mic.x,mic.y,x,y)<=10)||(min_b<tahu*V_HRACE+10))));
      
      if (vzdalenost(mic.x,mic.y,x,y)<=10) 
        {
          
          return 0;
        }
        // ';' expected 
      // ';' expected 
      else
        {
          
          return nej_b;
        }
    } // end method doleti
  
  public boolean je_kryty (Player h, Player ja, Player[] b, Ball mic) {
    
/*variables*/

    int i = 0;
    int nej = 0;
    double min = 0.0;
    
/*end variables*/

      min = 10000;
      nej = 0;
      
      for (i = 1; i<=MAX_HRACU; i++) 
        {
          
          if (vzdalenost(h.x,h.y,b[i].cil_x,b[i].cil_y)<min) 
            {
              min = vzdalenost(h.x,h.y,b[i].cil_x,b[i].cil_y);
              nej = i;
            }
        }
      
      if ((min<30)&&((ja.cil_x!=b[nej].cil_x)||(ja.cil_y!=b[nej].cil_y))) 
        {
          
          return true;
        }
        // ';' expected 
      // ';' expected 
      else
        {
          
          return false;
        }
    } // end method je_kryty
  
  public int nejblizsi_nekryty_nepritel (Player h, Player[] a, Player[] b, Ball mic) {
    
/*variables*/

    int i = 0;
    int nej = 0;
    double min = 0.0;
    
/*end variables*/

      min = 10000;
      nej = 0;
      
      for (i = 1; i<=MAX_HRACU; i++) 
        {
          
          if ((vzdalenost(h.x,h.y,b[i].x,b[i].y)<min)&&(je_kryty(b[i],h,a,mic)==false)) 
            {
              min = vzdalenost(h.x,h.y,b[i].x,b[i].y);
              nej = i;
            }
        }
      
      return nej;
    } // end method nejblizsi_nekryty_nepritel
  
  
  
  public void ui (Player[] a, Player[] b, Ball mic, int strana) {
    
/*variables*/

    double cil_x = 0.0;
    double cil_y = 0.0;
    double vcil_x = 0.0;
    double vcil_y = 0.0;
    double vzd = 0.0;
    int i = 0;
    int j = 0;
    int nej = 0;
    Integer druhy = 0;  
    int vnej = 0;
    int vdruhy = 0;
    Ball vmic = new Ball();
    Player vhrac = new Player();
    double prip_x = 0.0;
    double prip_y = 0.0;
    
/*end variables*/

      druhy = 0;
      nej = nejblizsi_hrac(a,b,mic,cil_x,cil_y,druhy);
      cil_x = returnx;
      cil_y = returny;
      druhy = returndruhy;
       
      if (nej>0) 
        {
          /* UTOK */
          
          jdi_na(a[nej],cil_x,cil_y);
          
          for (i = 2; i<=MAX_HRACU; i++) 
            {
              /* nabihani */
              
              if (i!=nej) 
                {
                  /* if((a[i].cil_x==a[i].x)&&(a[i].cil_y==a[i].y))then{a[i].cil_x:==a[i].x+10-20*strana;}; */
                  vhrac.x = cil_x;
                  vhrac.y = cil_y;
                  vmic.x = cil_x;
                  vmic.y = cil_y;
                  
                  kopni_mic(vhrac,vmic,a[i].cil_x,a[i].cil_y,V_MICE);
                  vdruhy = nej;
                  vnej = nejblizsi_hrac(a,b,vmic,vcil_x,vcil_y,vdruhy);
                  vcil_x = returnx;
                  vcil_y = returny;
                  vdruhy = returndruhy;
                  
                  if (vnej<0) 
                    {
                      
                      do {
                        vzd = vzdalenost(a[nej].x,a[nej].y,cil_x,cil_y);
                        
                        do {
                          vcil_x = random(round(vzd))+30;
                          vcil_y = random(round(vzd))+30;
                          
                          if (random(2)==0) 
                            vcil_x = vcil_x*-(0.8+0.4*strana);
                          
                          if (random(2)==0) 
                            vcil_y = vcil_y*-1;
                          prip_x = a[i].x+vcil_x;
                          prip_y = a[i].y+vcil_y;
                        } while (!((vzdalenost(prip_x,prip_y,a[nej].x,a[nej].y)>40)||(random(10)==0)));
                        vhrac.x = cil_x;
                        vhrac.y = cil_y;
                        vmic.x = cil_x;
                        vmic.y = cil_y;
                        
                        kopni_mic(vhrac,vmic,prip_x,prip_y,V_MICE);
                        vdruhy = 0;
                        vnej = nejblizsi_hrac(a,b,vmic,vcil_x,vcil_y,vdruhy);
                        vcil_x = returnx;
                        vcil_y = returny;
                        vdruhy = returndruhy;
                      } while (!(((random(200)==0)||(vnej==i))));
                      
                      if (vnej==i) 
                        {
                          
                          jdi_na(a[i],prip_x,prip_y);
                          /* //showmessage('+inttostr(i)); */
                        }
                        // ';' expected 
                      // ';' expected 
                      else
                        {
                          
                          if (vzdalenost(a[i].x,a[i].y,a[i].cil_x,a[i].cil_y)<15) 
                            {
                              j = random(3);
                              
                              switch (j) {
                                case 0:
                                  prip_x = 640/3;
                                break;
                                
                                case 1:
                                  prip_x = 640-640/3;
                                break;
                                
                                case 2:
                                  prip_x = 640/2;
                                break;
                                
                              } // end switch 
                              j = random(3);
                              
                              switch (j) {
                                case 0:
                                  prip_y = 480/3;
                                break;
                                
                                case 1:
                                  prip_y = 480-480/3;
                                break;
                                
                                case 2:
                                  prip_y = 480/2;
                                break;
                                
                              } // end switch 
                              
                              jdi_na(a[i],prip_x,prip_y);
                            }
                        }
                    }
                }
            }
          /* smicem */
          
          if (vzdalenost(a[nej].x,a[nej].y,mic.x,mic.y)<=10) 
            {
              j = 0;
              
              for (i = 2; i<=MAX_HRACU; i++) 
                {
                  
                  if (i!=nej) 
                    {
                      vmic = mic;
                      
                      kopni_mic(a[nej],vmic,a[i].cil_x,a[i].cil_y,V_MICE);
                      vdruhy = nej;
                      vnej = nejblizsi_hrac(a,b,vmic,vcil_x,vcil_y,vdruhy);
                      vcil_x = returnx;
                      vcil_y = returny;
                      vdruhy = returndruhy;
                      
                      if (vnej>0) 
                        {
                          
                          if (j==0) 
                            {
                              j = i;
                            }
                            // ';' expected 
                          // ';' expected 
                          else
                            {
                              
                              if ((vzdalenost(a[i].x,a[i].y,639-strana*638,240)<vzdalenost(a[j].x,a[j].y,639-strana*638,240))&&(vzdalenost(a[i].x,a[i].y,b[druhy].x,b[druhy].y)>15)) 
                                {
                                  j = i;
                                }
                            }
                        }
                    }
                }
              
              if ((j>0)&&(random(5)!=0)) 
                {
                  
                  if (vzdalenost(a[nej].x,a[nej].y,b[druhy].x,b[druhy].y)<40) 
                    {
                      
                      kopni_mic(a[nej],mic,a[j].cil_x,a[j].cil_y,V_MICE);
                    }
                    // ';' expected 
                  // ';' expected 
                  else
                    {
                      
                      if (vzdalenost(a[nej].x,a[nej].y,639-strana*638,160)<vzdalenost(a[nej].x,a[nej].y,639-strana*638,320)) 
                        {
                          
                          jdi_na(a[nej],639-strana*638,160);
                        }
                        // ';' expected 
                      // ';' expected 
                      else
                        {
                          
                          jdi_na(a[nej],639-strana*638,320);
                        }
                      
                      kopni_mic(a[nej],mic,a[nej].cil_x,a[nej].cil_y,5);
                    }
                }
                // ';' expected 
              // ';' expected 
              else
                {
                  /* jdenabranu */
                  
                  if (vzdalenost(a[nej].x,a[nej].y,639-strana*638,160)<vzdalenost(a[nej].x,a[nej].y,639-strana*638,320)) 
                    {
                      
                      jdi_na(a[nej],639-strana*638,160);
                    }
                    // ';' expected 
                  // ';' expected 
                  else
                    {
                      
                      jdi_na(a[nej],639-strana*638,320);
                    }
                  
                  kopni_mic(a[nej],mic,a[nej].cil_x,a[nej].cil_y,5);
                }
              /* brankarsmicemvohrozeni */
              
              if (nej==1) 
                {
                  
                  if (vzdalenost(a[nej].x,a[nej].y,b[druhy].x,b[druhy].y)<60) 
                    {
                      /* pokudmuze,nahraje */
                      
                      if (j>0) 
                        {
                          
                          kopni_mic(a[nej],mic,a[j].cil_x,a[j].cil_y,V_MICE);
                        }
                        // ';' expected 
                      // ';' expected 
                      else
                        {
                          
                          if ((vzdalenost(b[druhy].x,b[druhy].y,strana*620+10,5)-vzdalenost(a[nej].x,a[nej].y,strana*620+10,5)>vzdalenost(a[nej].x,a[nej].y,strana*620+10,475)-vzdalenost(a[nej].x,a[nej].y,strana*620+10,5))) 
                            {
                              
                              kopni_mic(a[nej],mic,strana*620+10,5,V_MICE);
                            }
                            // ';' expected 
                          // ';' expected 
                          else
                            {
                              
                              kopni_mic(a[nej],mic,strana*620+10,475,V_MICE);
                            }
                          /* kdybytonepritelvzal,takradsikopnijinam */
                          vdruhy = 0;
                          vnej = nejblizsi_hrac(a,b,mic,vcil_x,vcil_y,vdruhy);
                          vcil_x = returnx;
                          vcil_y = returny;
                          vdruhy = returndruhy;
                          
                          if (vnej<0) 
                            {
                              
                              if (a[1].y<240) 
                                {
                                  
                                  kopni_mic(a[nej],mic,635-strana*630,5,V_MICE);
                                }
                                // ';' expected 
                              // ';' expected 
                              else
                                {
                                  
                                  kopni_mic(a[nej],mic,635-strana*630,475,V_MICE);
                                }
                              //UNRECOGNIZED TOKEN : BADNAME char/(47)
                              // ';' expected 
                              //UNRECOGNIZED TOKEN : BADNAME char/(47)
                              // ';' expected 
                              //UNRECOGNIZED TOKEN : showmessage
                              // ';' expected 
                              //UNRECOGNIZED TOKEN : BADNAME char((40)
                              // ';' expected 
                              //UNRECOGNIZED TOKEN : BADNAME char'(39)
                              // ';' expected 
                              //UNRECOGNIZED TOKEN : BADNAME char)(41)
                            }
                        }
                    }
                }
            }
          /* strili,pokuddagol */
          vmic = (Ball)mic.clone();
          vcil_y = random(159)+161;
          
          kopni_mic(a[nej],vmic,639-strana*638,vcil_y,V_MICE);
          
          if (doleti(b,vmic,639-strana*638,vcil_y)==0) 
            {
              
              kopni_mic(a[nej],mic,639-strana*638,vcil_y,V_MICE);
            }
//          vmic = mic;
          vmic = (Ball)mic.clone();
          
          kopni_mic(a[nej],vmic,639-strana*638,165,V_MICE);
          
          if (doleti(b,vmic,639-strana*638,165)==0) 
            {
              
              kopni_mic(a[nej],mic,639-strana*638,165,V_MICE);
            }
//          vmic = mic;
          vmic = (Ball)mic.clone();
          
          kopni_mic(a[nej],vmic,639-strana*638,315,V_MICE);
          
          if (doleti(b,vmic,639-strana*638,315)==0) 
            {
              
              kopni_mic(a[nej],mic,639-strana*638,315,V_MICE);
            }
        }
        // ';' expected 
      // ';' expected 
      else
        {
          /* OBRANA */
          
          for (i = 2; i<=MAX_HRACU; i++) 
            {
              
              if (i!=druhy) 
                {
                  j = nejblizsi_nekryty_nepritel(a[i],a,b,mic);
                  
                  if (j!=0) 
                    {
                      vmic.x = b[j].x;
                      vmic.y = b[j].y;
                      vhrac.x = b[j].x;
                      vhrac.y = b[j].y;
                      
                      kopni_mic(vhrac,vmic,cil_x,cil_y,V_MICE);
                      vmic.x = vmic.x+vmic.v_x*2;
                      vmic.y = vmic.y+vmic.v_y*2;
                      
                      jdi_na(a[i],vmic.x,vmic.y);
                    }
                }
            }
          
          jdi_na(a[druhy],cil_x,cil_y);
          
          kopni_mic(a[druhy],mic,639-strana*638,random(160)+160,V_MICE);
        }
      /* brankar */
      
      if ((1!=nej)&&(druhy!=1)) 
        {
          /* ifnej>0thennej:==-druhy;vmic.x:==strana*630+5;vmic.y:==240;ifmic.y<220thenvmic.y:==200;ifmic.y>260thenvmic.y:==280;vhrac.x:==vmic.x;vhrac.y:==vmic.y;kopni_mic(vhrac,vmic,b[-nej].x,b[-nej].y,V_MICE);jdi_na(a[1],vmic.x+vmic.v_x*5,vmic.y+vmic.v_y*5); */
          
          if (nej>0) 
            nej = -druhy;
          vmic.x = strana*630+5;
          vmic.y = 240;
          
          if (mic.y<220) 
            vmic.y = 200;
          
          if (mic.y>260) 
            vmic.y = 280;
          vhrac.x = vmic.x;
          vhrac.y = vmic.y;
          
          kopni_mic(vhrac,vmic,b[-nej].x,b[-nej].y,V_MICE);
          vcil_y = vmic.y+vmic.v_y*5;
          vcil_x = vmic.x+vmic.v_x*5;
          prip_y = a[1].y;
          vmic = (Ball)mic.clone();
//          vmic = mic;
          
          kopni_mic(b[-nej],vmic,638*strana+1,161,V_MICE);
          
          if (doleti(a,vmic,638*strana+1,161)==0) 
            {
              prip_y = prip_y-V_HRACE/2;
              /* //showmessage('); */
            }
          vmic = (Ball)mic.clone();
//          vmic = mic;
          
          kopni_mic(b[-nej],vmic,638*strana+1,319,V_MICE);
          
          if (doleti(a,vmic,638*strana+1,319)==0) 
            {
              prip_y = prip_y+V_HRACE/2;
              /* //showmessage('); */
            }
          
          if (prip_y!=a[1].y) 
            vcil_y = prip_y;
          
          jdi_na(a[1],vcil_x,vcil_y);
        }
    } // end method petr
} // class end. 

