//package petr;
//
//import soccer.*;
//
//public class UI_Petr extends PascalTeam {
//
//	public int nejblizsi_hrac (Player[] a, Player[] b, Ball mic, Double x, Double y, Integer druhy) {
//
//		/*variables*/
//		int i = 0;
//		int nej_a = 0;
//		int nej_b = 0;
//		int tahu = 0;
//		double min_a = 0.0;
//		double min_b = 0.0;
//		double mic_x = 0.0;
//		double mic_y = 0.0;
//
//		mic = (Ball)mic.clone();
//		
//		/*end variables*/
//
//
////		VARIABLE mic_x:=?;
//		mic_x = mic.x;
//
////		VARIABLE mic_y:=?;
//		mic_y = mic.y;
//
////		VARIABLE tahu:=?;
//		tahu = 0;
//
////		KEYWORD repeat
//		do {
//
////			VARIABLE tahu:=?;
//			tahu = tahu+1;
//
////			KEYWORD if
//
//			if (/*tahu!=0*/true) 
////				KEYWORD begin
//			{
//
////				VARIABLE mic_x:=?;
//				mic_x = mic_x+mic.v_x;
//
////				VARIABLE mic_y:=?;
//				mic_y = mic_y+mic.v_y;
//
////				KEYWORD if
//
//				if (((mic_x<3)||(mic_x>637)||(mic_y<3)||(mic_y>477))) 
////					KEYWORD begin
//				{
//
////					KEYWORD if
//
//					if (mic_x<3) 
////						VARIABLE mic_x:=?;
//						mic_x = 3;
//
//
////					KEYWORD if
//
//					if (mic_x>637) 
////						VARIABLE mic_x:=?;
//						mic_x = 637;
//
//
////					KEYWORD if
//
//					if (mic_y<3) 
////						VARIABLE mic_y:=?;
//						mic_y = 3;
//
//
////					KEYWORD if
//
//					if (mic_y>477) 
////						VARIABLE mic_y:=?;
//						mic_y = 477;
//
//
////					VARIABLE mic.v_x:=?;
//					mic.v_x = 0;
//
////					VARIABLE mic.v_y:=?;
//					mic.v_y = 0;
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			VARIABLE min_a:=?;
//			min_a = 10000;
//
////			VARIABLE nej_a:=?;
//			nej_a = 0;
//
////			KEYWORD for
//			for (i = 1; i<=MAX_HRACU; i++) 
////				KEYWORD begin
//			{
//
////				KEYWORD if
//
//				if (i!=druhy) 
////					KEYWORD if
//
//					if (vzdalenost(a[i].x,a[i].y,mic_x,mic_y)<min_a) 
////						KEYWORD begin
//					{
//
////						VARIABLE min_a:=?;
//						min_a = vzdalenost(a[i].x,a[i].y,mic_x,mic_y);
//
////						VARIABLE nej_a:=?;
//						nej_a = i;
//
////						KEYWORD end
//
////						END end
//					}
//
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			VARIABLE min_b:=?;
//			min_b = 10000;
//
////			VARIABLE nej_b:=?;
//			nej_b = 0;
//
////			KEYWORD for
//			for (i = 1; i<=MAX_HRACU; i++) 
////				KEYWORD begin
//			{
//
////				KEYWORD if
//
//				if (vzdalenost(b[i].x,b[i].y,mic_x,mic_y)<min_b) 
////					KEYWORD begin
//				{
//
////					VARIABLE min_b:=?;
//					min_b = vzdalenost(b[i].x,b[i].y,mic_x,mic_y);
//
////					VARIABLE nej_b:=?;
//					nej_b = i;
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			VARIABLE min_b:=?;
//			min_b = min_b-1;
//
////			UNRECOGNIZED TOKEN : BADNAME char/(47)
//			// ';' expected 
////			UNRECOGNIZED TOKEN : BADNAME char/(47)
//			// ';' expected 
////			UNRECOGNIZED TOKEN : V_HRACE
//
////			KEYWORD until
//		} while (!(((min_a<tahu*V_HRACE+10)||(min_b<tahu*V_HRACE+15))));
//
//
////		VARIABLE x:=?;
//		x = mic_x;
//
////		VARIABLE y:=?;
//		y = mic_y;
//
////		KEYWORD if
//
//		if (min_a<min_b) 
////			KEYWORD begin
//		{
//
////			VARIABLE druhy:=?;
//			druhy = nej_b;
//
//
////			RETURNING RESULT of nejblizsi_hrac
//			return nej_a;
//
//
////			KEYWORD end
//
////			END end
//		}
//
//		// ';' expected 
//		// ';' expected 
////		KEYWORD else
//		else 
////			KEYWORD begin
//		{
//
////			VARIABLE druhy:=?;
//			druhy = nej_a;
//
//
////			RETURNING RESULT of nejblizsi_hrac
//			return -nej_b;
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
//
////		KEYWORD end
//
////		END end
//
//	} // end method nejblizsi_hrac
//	public int doleti (Player[] b, Ball mic, double x, double y) {
//
//		/*variables*/
//		int i = 0;
//		int nej_b = 0;
//		int tahu = 0;
//		double min_b = 0.0;
//		mic = (Ball)mic.clone();
//		
//		/*end variables*/
//
//
////		VARIABLE tahu:=?;
//		tahu = 0;
//
////		KEYWORD repeat
//		do {
//
////			VARIABLE tahu:=?;
//			tahu = tahu+1;
//
////			KEYWORD if
//
//			if (/*tahu!=0*/true) 
////				KEYWORD begin
//			{
//
////				VARIABLE mic.x:=?;
//				mic.x = mic.x+mic.v_x;
//
////				VARIABLE mic.y:=?;
//				mic.y = mic.y+mic.v_y;
//
////				KEYWORD if
//
//				if (((mic.x<3)||(mic.x>637)||(mic.y<3)||(mic.y>477))) 
////					KEYWORD begin
//				{
//
////					KEYWORD if
//
//					if (mic.x<3) 
////						VARIABLE mic.x:=?;
//						mic.x = 3;
//
//
////					KEYWORD if
//
//					if (mic.x>637) 
////						VARIABLE mic.x:=?;
//						mic.x = 637;
//
//
////					KEYWORD if
//
//					if (mic.y<3) 
////						VARIABLE mic.y:=?;
//						mic.y = 3;
//
//
////					KEYWORD if
//
//					if (mic.y>477) 
////						VARIABLE mic.y:=?;
//						mic.y = 477;
//
//
////					VARIABLE mic.v_x:=?;
//					mic.v_x = 0;
//
////					VARIABLE mic.v_y:=?;
//					mic.v_y = 0;
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			VARIABLE min_b:=?;
//			min_b = 10000;
//
////			VARIABLE nej_b:=?;
//			nej_b = 0;
//
////			KEYWORD for
//			for (i = 1; i<=MAX_HRACU; i++) 
////				KEYWORD begin
//			{
//
////				KEYWORD if
//
//				if (vzdalenost(b[i].x,b[i].y,mic.x,mic.y)<min_b) 
////					KEYWORD begin
//				{
//
////					VARIABLE min_b:=?;
//					min_b = vzdalenost(b[i].x,b[i].y,mic.x,mic.y);
//
////					VARIABLE nej_b:=?;
//					nej_b = i;
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			KEYWORD until
//		} while (!(((vzdalenost(mic.x,mic.y,x,y)<=10)||(min_b<tahu*V_HRACE+10))));
//
//
////		KEYWORD if
//
//		if (vzdalenost(mic.x,mic.y,x,y)<=10) 
////			KEYWORD begin
//		{
//
//
////			RETURNING RESULT of doleti
//			return 0;
//
//
////			KEYWORD end
//
////			END end
//		}
//
//		// ';' expected 
//		// ';' expected 
////		KEYWORD else
//		else 
////			KEYWORD begin
//		{
//
//
////			RETURNING RESULT of doleti
//			return nej_b;
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
//
////		KEYWORD end
//
////		END end
//
//	} // end method doleti
//	public boolean je_kryty (Player h, Player ja, Player[] b, Ball mic) {
//
//		/*variables*/
//		int i = 0;
//		int nej = 0;
//		double min = 0.0;
//		mic = (Ball)mic.clone();
//
//		/*end variables*/
//
//
////		VARIABLE min:=?;
//		min = 10000;
//
////		VARIABLE nej:=?;
//		nej = 0;
//
////		KEYWORD for
//		for (i = 1; i<=MAX_HRACU; i++) 
////			KEYWORD begin
//		{
//
////			KEYWORD if
//
//			if (vzdalenost(h.x,h.y,b[i].cil_x,b[i].cil_y)<min) 
////				KEYWORD begin
//			{
//
////				VARIABLE min:=?;
//				min = vzdalenost(h.x,h.y,b[i].cil_x,b[i].cil_y);
//
////				VARIABLE nej:=?;
//				nej = i;
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
////		KEYWORD if
//
//		if ((min<30)&&((ja.cil_x!=b[nej].cil_x)||(ja.cil_y!=b[nej].cil_y))) 
////			KEYWORD begin
//		{
//
//
////			RETURNING RESULT of je_kryty
//			return true;
//
//
////			KEYWORD end
//
////			END end
//		}
//
//		// ';' expected 
//		// ';' expected 
////		KEYWORD else
//		else 
////			KEYWORD begin
//		{
//
//
////			RETURNING RESULT of je_kryty
//			return false;
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
//
////		KEYWORD end
//
////		END end
//
//	} // end method je_kryty
//	public int nejblizsi_nekryty_nepritel (Player h, Player[] a, Player[] b, Ball mic) {
//
//		/*variables*/
//		int i = 0;
//		int nej = 0;
//		double min = 0.0;
//
//		/*end variables*/
//
//
////		VARIABLE min:=?;
//		min = 10000;
//
////		VARIABLE nej:=?;
//		nej = 0;
//
////		KEYWORD for
//		for (i = 1; i<=MAX_HRACU; i++) 
////			KEYWORD begin
//		{
//
////			KEYWORD if
//
//			if ((vzdalenost(h.x,h.y,b[i].x,b[i].y)<min)&&(je_kryty(b[i],h,a,mic)==false)) 
////				KEYWORD begin
//			{
//
////				VARIABLE min:=?;
//				min = vzdalenost(h.x,h.y,b[i].x,b[i].y);
//
////				VARIABLE nej:=?;
//				nej = i;
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
//
////		RETURNING RESULT of nejblizsi_nekryty_nepritel
//		return nej;
//
//
////		KEYWORD end
//
////		END end
//
//	} // end method nejblizsi_nekryty_nepritel
//
//	public void ui (Player[] a, Player[] b, Ball mic, int strana) {
//
//		/*variables*/
//		double cil_x = 0.0;
//		double cil_y = 0.0;
//		double vcil_x = 0.0;
//		double vcil_y = 0.0;
//		double vzd = 0.0;
//		int i = 0;
//		int j = 0;
//		int nej = 0;
//		int druhy = 0;
//		int vnej = 0;
//		int vdruhy = 0;
//		Ball vmic = new Ball();
//		Player vhrac = new Player();
//		double prip_x = 0.0;
//		double prip_y = 0.0;
//
//		/*end variables*/
//
//
////		VARIABLE druhy:=?;
//		druhy = 0;
//
////		VARIABLE nej:=?;
//		nej = nejblizsi_hrac(a,b,(Ball)mic.clone(),cil_x,cil_y,druhy);
//
////		KEYWORD if
//
//if (nej>0) 
////	KEYWORD begin
//{
//	/* UTOK */
//
//
////	CALLING METHOD jdi_na WITH PARAMS 
//jdi_na(a[nej],cil_x,cil_y);
//
//
////	KEYWORD for
//for (i = 2; i<=MAX_HRACU; i++) 
////	KEYWORD begin
//{
//	/* nabihani */
//
////	KEYWORD if
//
//	if (i!=nej) 
////		KEYWORD begin
//	{
//		/* if((a[i].cil_x==a[i].x)&&(a[i].cil_y==a[i].y))then{a[i].cil_x:==a[i].x+10-20*strana;}; */
//
////		VARIABLE vhrac.x:=?;
//		vhrac.x = cil_x;
//
////		VARIABLE vhrac.y:=?;
//		vhrac.y = cil_y;
//
////		VARIABLE vmic.x:=?;
//		vmic.x = cil_x;
//
////		VARIABLE vmic.y:=?;
//		vmic.y = cil_y;
//
//
////		CALLING METHOD kopni_mic WITH PARAMS 
//		kopni_mic(vhrac,vmic,a[i].cil_x,a[i].cil_y,V_MICE);
//
//
////		VARIABLE vdruhy:=?;
//		vdruhy = nej;
//
////		VARIABLE vnej:=?;
//		vnej = nejblizsi_hrac(a,b,vmic,vcil_x,vcil_y,vdruhy);
//
////		KEYWORD if
//
//		if (vnej<0) 
////			KEYWORD begin
//		{
//
////			KEYWORD repeat
//			do {
//
////				VARIABLE vzd:=?;
//				vzd = vzdalenost(a[nej].x,a[nej].y,cil_x,cil_y);
//
////				KEYWORD repeat
//				do {
//
////					VARIABLE vcil_x:=?;
//					vcil_x = random(round(vzd))+30;
//
////					VARIABLE vcil_y:=?;
//					vcil_y = random(round(vzd))+30;
//
////					KEYWORD if
//
//					if (random(2)==0) 
////						VARIABLE vcil_x:=?;
//						vcil_x = vcil_x*-(0.8+0.4*strana);
//
//
////					KEYWORD if
//
//					if (random(2)==0) 
////						VARIABLE vcil_y:=?;
//						vcil_y = vcil_y*-1;
//
//
////					VARIABLE prip_x:=?;
//					prip_x = a[i].x+vcil_x;
//
////					VARIABLE prip_y:=?;
//					prip_y = a[i].y+vcil_y;
//
////					KEYWORD until
//				} while (!((vzdalenost(prip_x,prip_y,a[nej].x,a[nej].y)>40)||(random(10)==0)));
//
//
////				VARIABLE vhrac.x:=?;
//				vhrac.x = cil_x;
//
////				VARIABLE vhrac.y:=?;
//				vhrac.y = cil_y;
//
////				VARIABLE vmic.x:=?;
//				vmic.x = cil_x;
//
////				VARIABLE vmic.y:=?;
//				vmic.y = cil_y;
//
//
////				CALLING METHOD kopni_mic WITH PARAMS 
//				kopni_mic(vhrac,vmic,prip_x,prip_y,V_MICE);
//
//
////				VARIABLE vdruhy:=?;
//				vdruhy = 0;
//
////				VARIABLE vnej:=?;
//				vnej = nejblizsi_hrac(a,b,vmic,vcil_x,vcil_y,vdruhy);
//
////				KEYWORD until
//			} while (!(((random(200)==0)||(vnej==i))));
//
//
////			KEYWORD if
//
//			if (vnej==i) 
////				KEYWORD begin
//			{
//
//
////				CALLING METHOD jdi_na WITH PARAMS 
//				jdi_na(a[i],prip_x,prip_y);
//
//				/* //showmessage('+inttostr(i)); */
//
////				KEYWORD end
//
////				END end
//			}
//
//			// ';' expected 
//			// ';' expected 
////			KEYWORD else
//			else 
////				KEYWORD begin
//			{
//
////				KEYWORD if
//
//				if (vzdalenost(a[i].x,a[i].y,a[i].cil_x,a[i].cil_y)<15) 
////					KEYWORD begin
//				{
//
////					VARIABLE j:=?;
//					j = random(3);
//
////					KEYWORD case
//					switch (j) {
//					case 0:
//
////						VARIABLE prip_x:=?;
//					prip_x = 640/3;
//
//					break;
//					case 1:
//
////						VARIABLE prip_x:=?;
//						prip_x = 640-640/3;
//
//						break;
//					case 2:
//
////						VARIABLE prip_x:=?;
//						prip_x = 640/2;
//
//						break;
//
//					} // end switch 
//
////					VARIABLE j:=?;
//					j = random(3);
//
////					KEYWORD case
//					switch (j) {
//					case 0:
//
////						VARIABLE prip_y:=?;
//					prip_y = 480/3;
//
//					break;
//					case 1:
//
////						VARIABLE prip_y:=?;
//						prip_y = 480-480/3;
//
//						break;
//					case 2:
//
////						VARIABLE prip_y:=?;
//						prip_y = 480/2;
//
//						break;
//
//					} // end switch 
//
//
////					CALLING METHOD jdi_na WITH PARAMS 
//					jdi_na(a[i],prip_x,prip_y);
//
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
////		KEYWORD end
//
////		END end
//	}
//
//
//
////	KEYWORD end
//
////	END end
//}
//
//
///* smicem */
//
////KEYWORD if
//
//if (vzdalenost(a[nej].x,a[nej].y,mic.x,mic.y)<=10) 
////	KEYWORD begin
//{
//
////	VARIABLE j:=?;
//	j = 0;
//
////	KEYWORD for
//	for (i = 2; i<=MAX_HRACU; i++) 
////		KEYWORD begin
//	{
//
////		KEYWORD if
//
//		if (i!=nej) 
////			KEYWORD begin
//		{
//
////			VARIABLE vmic:=?;
//			vmic = mic;
//
//
////			CALLING METHOD kopni_mic WITH PARAMS 
//			kopni_mic(a[nej],vmic,a[i].cil_x,a[i].cil_y,V_MICE);
//
//
////			VARIABLE vdruhy:=?;
//			vdruhy = nej;
//
////			VARIABLE vnej:=?;
//			vnej = nejblizsi_hrac(a,b,vmic,vcil_x,vcil_y,vdruhy);
//
////			KEYWORD if
//
//			if (vnej>0) 
////				KEYWORD begin
//			{
//
////				KEYWORD if
//
//				if (j==0) 
////					KEYWORD begin
//				{
//
////					VARIABLE j:=?;
//					j = i;
//
////					KEYWORD end
//
////					END end
//				}
//
//				// ';' expected 
//				// ';' expected 
////				KEYWORD else
//				else 
////					KEYWORD begin
//				{
//
////					KEYWORD if
//
//					if ((vzdalenost(a[i].x,a[i].y,639-strana*638,240)<vzdalenost(a[j].x,a[j].y,639-strana*638,240))&&(vzdalenost(a[i].x,a[i].y,b[druhy].x,b[druhy].y)>15)) 
////						KEYWORD begin
//					{
//
////						VARIABLE j:=?;
//						j = i;
//
////						KEYWORD end
//
////						END end
//					}
//
//
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
////		KEYWORD end
//
////		END end
//	}
//
//
//
////	KEYWORD if
//
//	if ((j>0)&&(random(5)!=0)) 
////		KEYWORD begin
//	{
//
////		KEYWORD if
//
//		if (vzdalenost(a[nej].x,a[nej].y,b[druhy].x,b[druhy].y)<40) 
////			KEYWORD begin
//		{
//
//
////			CALLING METHOD kopni_mic WITH PARAMS 
//			kopni_mic(a[nej],mic,a[j].cil_x,a[j].cil_y,V_MICE);
//
//
////			KEYWORD end
//
////			END end
//		}
//
//		// ';' expected 
//		// ';' expected 
////		KEYWORD else
//		else 
////			KEYWORD begin
//		{
//
////			KEYWORD if
//
//			if (vzdalenost(a[nej].x,a[nej].y,639-strana*638,160)<vzdalenost(a[nej].x,a[nej].y,639-strana*638,320)) 
////				KEYWORD begin
//			{
//
//
////				CALLING METHOD jdi_na WITH PARAMS 
//				jdi_na(a[nej],639-strana*638,160);
//
//
////				KEYWORD end
//
////				END end
//			}
//
//			// ';' expected 
//			// ';' expected 
////			KEYWORD else
//			else 
////				KEYWORD begin
//			{
//
//
////				CALLING METHOD jdi_na WITH PARAMS 
//				jdi_na(a[nej],639-strana*638,320);
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
//
//
////			CALLING METHOD kopni_mic WITH PARAMS 
//			kopni_mic(a[nej],mic,a[nej].cil_x,a[nej].cil_y,5);
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
//
////		KEYWORD end
//
////		END end
//	}
//
//	// ';' expected 
//	// ';' expected 
////	KEYWORD else
//	else 
////		KEYWORD begin
//	{
//		/* jdenabranu */
//
////		KEYWORD if
//
//		if (vzdalenost(a[nej].x,a[nej].y,639-strana*638,160)<vzdalenost(a[nej].x,a[nej].y,639-strana*638,320)) 
////			KEYWORD begin
//		{
//
//
////			CALLING METHOD jdi_na WITH PARAMS 
//			jdi_na(a[nej],639-strana*638,160);
//
//
////			KEYWORD end
//
////			END end
//		}
//
//		// ';' expected 
//		// ';' expected 
////		KEYWORD else
//		else 
////			KEYWORD begin
//		{
//
//
////			CALLING METHOD jdi_na WITH PARAMS 
//			jdi_na(a[nej],639-strana*638,320);
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
//
//
////		CALLING METHOD kopni_mic WITH PARAMS 
//		kopni_mic(a[nej],mic,a[nej].cil_x,a[nej].cil_y,5);
//
//
////		KEYWORD end
//
////		END end
//	}
//
//
//
//	/* brankarsmicemvohrozeni */
//
////	KEYWORD if
//
//	if (nej==1) 
////		KEYWORD begin
//	{
//
////		KEYWORD if
//
//		if (vzdalenost(a[nej].x,a[nej].y,b[druhy].x,b[druhy].y)<60) 
////			KEYWORD begin
//		{
//			/* pokudmuze,nahraje */
//
////			KEYWORD if
//
//			if (j>0) 
////				KEYWORD begin
//			{
//
//
////				CALLING METHOD kopni_mic WITH PARAMS 
//				kopni_mic(a[nej],mic,a[j].cil_x,a[j].cil_y,V_MICE);
//
//
////				KEYWORD end
//
////				END end
//			}
//
//			// ';' expected 
//			// ';' expected 
////			KEYWORD else
//			else 
////				KEYWORD begin
//			{
//
////				KEYWORD if
//
//				if ((vzdalenost(b[druhy].x,b[druhy].y,strana*620+10,5)-vzdalenost(a[nej].x,a[nej].y,strana*620+10,5)>vzdalenost(a[nej].x,a[nej].y,strana*620+10,475)-vzdalenost(a[nej].x,a[nej].y,strana*620+10,5))) 
////					KEYWORD begin
//				{
//
//
////					CALLING METHOD kopni_mic WITH PARAMS 
//					kopni_mic(a[nej],mic,strana*620+10,5,V_MICE);
//
//
////					KEYWORD end
//
////					END end
//				}
//
//				// ';' expected 
//				// ';' expected 
////				KEYWORD else
//				else 
////					KEYWORD begin
//				{
//
//
////					CALLING METHOD kopni_mic WITH PARAMS 
//					kopni_mic(a[nej],mic,strana*620+10,475,V_MICE);
//
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
//				/* kdybytonepritelvzal,takradsikopnijinam */
//
////				VARIABLE vdruhy:=?;
//				vdruhy = 0;
//
////				VARIABLE vnej:=?;
//				vnej = nejblizsi_hrac(a,b,mic,vcil_x,vcil_y,vdruhy);
//
////				KEYWORD if
//
//				if (vnej<0) 
////					KEYWORD begin
//				{
//
////					KEYWORD if
//
//					if (a[1].y<240) 
////						KEYWORD begin
//					{
//
//
////						CALLING METHOD kopni_mic WITH PARAMS 
//						kopni_mic(a[nej],mic,635-strana*630,5,V_MICE);
//
//
////						KEYWORD end
//
////						END end
//					}
//
//					// ';' expected 
//					// ';' expected 
////					KEYWORD else
//					else 
////						KEYWORD begin
//					{
//
//
////						CALLING METHOD kopni_mic WITH PARAMS 
//						kopni_mic(a[nej],mic,635-strana*630,475,V_MICE);
//
//
////						KEYWORD end
//
////						END end
//					}
//
//
//
//
////					UNRECOGNIZED TOKEN : BADNAME char/(47)
//					// ';' expected 
////					UNRECOGNIZED TOKEN : BADNAME char/(47)
//					// ';' expected 
////					UNRECOGNIZED TOKEN : showmessage
//					// ';' expected 
////					UNRECOGNIZED TOKEN : BADNAME char((40)
//					// ';' expected 
////					UNRECOGNIZED TOKEN : BADNAME char'(39)
//					// ';' expected 
////					UNRECOGNIZED TOKEN : BADNAME char)(41)
//
////					KEYWORD end
//
////					END end
//				}
//
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
////		KEYWORD end
//
////		END end
//	}
//
//
//
////	KEYWORD end
//
////	END end
//}
//
//
///* strili,pokuddagol */
//
////VARIABLE vmic:=?;
//vmic = mic;
//
////VARIABLE vcil_y:=?;
//vcil_y = random(159)+161;
//
//
////CALLING METHOD kopni_mic WITH PARAMS 
//kopni_mic(a[nej],vmic,639-strana*638,vcil_y,V_MICE);
//
//
////KEYWORD if
//
//if (doleti(b,vmic,639-strana*638,vcil_y)==0) 
////	KEYWORD begin
//{
//
//
////	CALLING METHOD kopni_mic WITH PARAMS 
//	kopni_mic(a[nej],mic,639-strana*638,vcil_y,V_MICE);
//
//
////	KEYWORD end
//
////	END end
//}
//
//
//
////VARIABLE vmic:=?;
//vmic = mic;
//
//
////CALLING METHOD kopni_mic WITH PARAMS 
//kopni_mic(a[nej],vmic,639-strana*638,165,V_MICE);
//
//
////KEYWORD if
//
//if (doleti(b,vmic,639-strana*638,165)==0) 
////	KEYWORD begin
//{
//
//
////	CALLING METHOD kopni_mic WITH PARAMS 
//	kopni_mic(a[nej],mic,639-strana*638,165,V_MICE);
//
//
////	KEYWORD end
//
////	END end
//}
//
//
//
////VARIABLE vmic:=?;
//vmic = mic;
//
//
////CALLING METHOD kopni_mic WITH PARAMS 
//kopni_mic(a[nej],vmic,639-strana*638,315,V_MICE);
//
//
////KEYWORD if
//
//if (doleti(b,vmic,639-strana*638,315)==0) 
////	KEYWORD begin
//{
//
//
////	CALLING METHOD kopni_mic WITH PARAMS 
//	kopni_mic(a[nej],mic,639-strana*638,315,V_MICE);
//
//
////	KEYWORD end
//
////	END end
//}
//
//
//
////KEYWORD end
//
////END end
//}
//
//// ';' expected 
//// ';' expected 
////KEYWORD else
//else 
////	KEYWORD begin
//{
//	/* OBRANA */
//
////	KEYWORD for
//	for (i = 2; i<=MAX_HRACU; i++) 
////		KEYWORD begin
//	{
//
////		KEYWORD if
//
//		if (i!=druhy) 
////			KEYWORD begin
//		{
//
////			VARIABLE j:=?;
//			j = nejblizsi_nekryty_nepritel(a[i],a,b,mic);
//
////			KEYWORD if
//
//			if (j!=0) 
////				KEYWORD begin
//			{
//
////				VARIABLE vmic.x:=?;
//				vmic.x = b[j].x;
//
////				VARIABLE vmic.y:=?;
//				vmic.y = b[j].y;
//
////				VARIABLE vhrac.x:=?;
//				vhrac.x = b[j].x;
//
////				VARIABLE vhrac.y:=?;
//				vhrac.y = b[j].y;
//
//
////				CALLING METHOD kopni_mic WITH PARAMS 
//				kopni_mic(vhrac,vmic,cil_x,cil_y,V_MICE);
//
//
////				VARIABLE vmic.x:=?;
//				vmic.x = vmic.x+vmic.v_x*2;
//
////				VARIABLE vmic.y:=?;
//				vmic.y = vmic.y+vmic.v_y*2;
//
//
////				CALLING METHOD jdi_na WITH PARAMS 
//				jdi_na(a[i],vmic.x,vmic.y);
//
//
////				KEYWORD end
//
////				END end
//			}
//
//
//
////			KEYWORD end
//
////			END end
//		}
//
//
//
////		KEYWORD end
//
////		END end
//	}
//
//
//
//
////	CALLING METHOD jdi_na WITH PARAMS 
//	jdi_na(a[druhy],cil_x,cil_y);
//
//
//
////	CALLING METHOD kopni_mic WITH PARAMS 
//	kopni_mic(a[druhy],mic,639-strana*638,random(160)+160,V_MICE);
//
//
////	KEYWORD end
//
////	END end
//}
//
//
//
///* brankar */
//
////KEYWORD if
//
//if ((1!=nej)&&(druhy!=1)) 
////	KEYWORD begin
//{
//	/* ifnej>0thennej:==-druhy;vmic.x:==strana*630+5;vmic.y:==240;ifmic.y<220thenvmic.y:==200;ifmic.y>260thenvmic.y:==280;vhrac.x:==vmic.x;vhrac.y:==vmic.y;kopni_mic(vhrac,vmic,b[-nej].x,b[-nej].y,V_MICE);jdi_na(a[1],vmic.x+vmic.v_x*5,vmic.y+vmic.v_y*5); */
//
////	KEYWORD if
//
//	if (nej>0) 
////		VARIABLE nej:=?;
//		nej = -druhy;
//
//
////	VARIABLE vmic.x:=?;
//	vmic.x = strana*630+5;
//
////	VARIABLE vmic.y:=?;
//	vmic.y = 240;
//
////	KEYWORD if
//
//	if (mic.y<220) 
////		VARIABLE vmic.y:=?;
//		vmic.y = 200;
//
//
////	KEYWORD if
//
//	if (mic.y>260) 
////		VARIABLE vmic.y:=?;
//		vmic.y = 280;
//
//
////	VARIABLE vhrac.x:=?;
//	vhrac.x = vmic.x;
//
////	VARIABLE vhrac.y:=?;
//	vhrac.y = vmic.y;
//
//
////	CALLING METHOD kopni_mic WITH PARAMS 
//	kopni_mic(vhrac,vmic,b[-nej].x,b[-nej].y,V_MICE);
//
//
////	VARIABLE vcil_y:=?;
//	vcil_y = vmic.y+vmic.v_y*5;
//
////	VARIABLE vcil_x:=?;
//	vcil_x = vmic.x+vmic.v_x*5;
//
////	VARIABLE prip_y:=?;
//	prip_y = a[1].y;
//
////	VARIABLE vmic:=?;
//	vmic = mic;
//
//
////	CALLING METHOD kopni_mic WITH PARAMS 
//	kopni_mic(b[-nej],vmic,638*strana+1,161,V_MICE);
//
//
////	KEYWORD if
//
//	if (doleti(a,vmic,638*strana+1,161)==0) 
////		KEYWORD begin
//	{
//
////		VARIABLE prip_y:=?;
//		prip_y = prip_y-V_HRACE/2;
//		/* //showmessage('); */
//
////		KEYWORD end
//
////		END end
//	}
//
//
//
////	VARIABLE vmic:=?;
//	vmic = mic;
//
//
////	CALLING METHOD kopni_mic WITH PARAMS 
//	kopni_mic(b[-nej],vmic,638*strana+1,319,V_MICE);
//
//
////	KEYWORD if
//
//	if (doleti(a,vmic,638*strana+1,319)==0) 
////		KEYWORD begin
//	{
//
////		VARIABLE prip_y:=?;
//		prip_y = prip_y+V_HRACE/2;
//		/* //showmessage('); */
//
////		KEYWORD end
//
////		END end
//	}
//
//
//
////	KEYWORD if
//
//	if (prip_y!=a[1].y) 
////		VARIABLE vcil_y:=?;
//		vcil_y = prip_y;
//
//
//
////	CALLING METHOD jdi_na WITH PARAMS 
//	jdi_na(a[1],vcil_x,vcil_y);
//
//
////	KEYWORD end
//
////	END end
//}
//
//
//
////KEYWORD end
//
////END end
//
//	} // end method petr
//
//
//
//} // class end. 
