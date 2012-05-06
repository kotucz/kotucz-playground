unit UI_Petr;

interface
uses Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls, Unit_podpora;


procedure petr(var a : Ttym; b : Ttym; var mic : Tmic; strana : byte);


implementation


function nejblizsi_hrac(a, b : Ttym; mic : Tmic; var x, y : real; var druhy : integer) : integer;
var i, nej_a, nej_b, tahu : integer;
    min_a, min_b, mic_x, mic_y  : real;
begin

  mic_x := mic.x;
  mic_y := mic.y;

  tahu := 0;
  repeat
    tahu := tahu + 1;


    if {tahu <> 0} true then begin
      mic_x := mic_x + mic.v_x;
      mic_y := mic_y + mic.v_y;

      if ((mic_x < 3) or (mic_x > 637) or (mic_y < 3) or (mic_y > 477)) then begin

        if mic_x < 3 then mic_x := 3;
        if mic_x > 637 then mic_x := 637;
        if mic_y < 3 then mic_y := 3;
        if mic_y > 477 then mic_y := 477;

        mic.v_x := 0;
        mic.v_y := 0;
      end;

    end;

    min_a := 10000;
    nej_a := 0;

    for i := 1 to MAX_HRACU do begin
      if i <> druhy then
      if vzdalenost(a[i].x, a[i].y, mic_x, mic_y) < min_a then begin
        min_a := vzdalenost(a[i].x, a[i].y, mic_x, mic_y);
        nej_a := i;

      end;
    end;

    min_b := 10000;
    nej_b := 0;

    for i := 1 to MAX_HRACU do begin
      if vzdalenost(b[i].x, b[i].y, mic_x, mic_y) < min_b then begin
        min_b := vzdalenost(b[i].x, b[i].y, mic_x, mic_y);
        nej_b := i;

      end;
    end;

    min_b := min_b - 1;//V_HRACE;



  until ((min_a < tahu * V_HRACE + 10) or (min_b < tahu * V_HRACE + 15));

  x := mic_x;
  y := mic_y;


  if min_a < min_b then begin
    druhy := nej_b;
    nejblizsi_hrac := nej_a;
   end
  else begin
    druhy := nej_a;
    nejblizsi_hrac := -nej_b;
  end;



end;


function doleti(b : Ttym; mic : Tmic; x, y : real) : integer;
var i, nej_b, tahu : integer;
    min_b : real;
begin


  tahu := 0;
  repeat
    tahu := tahu + 1;


    if {tahu <> 0}true then begin
      mic.x := mic.x + mic.v_x;
      mic.y := mic.y + mic.v_y;

      if ((mic.x < 3) or (mic.x > 637) or (mic.y < 3) or (mic.y > 477)) then begin

        if mic.x < 3 then mic.x := 3;
        if mic.x > 637 then mic.x := 637;
        if mic.y < 3 then mic.y := 3;
        if mic.y > 477 then mic.y := 477;

        mic.v_x := 0;
        mic.v_y := 0;
      end;

    end;

    min_b := 10000;
    nej_b := 0;

    for i := 1 to MAX_HRACU do begin
      if vzdalenost(b[i].x, b[i].y, mic.x, mic.y) < min_b then begin
        min_b := vzdalenost(b[i].x, b[i].y, mic.x, mic.y);
        nej_b := i;

      end;
    end;


  until ((vzdalenost(mic.x, mic.y, x, y) <= 10) or (min_b < tahu * V_HRACE + 10));


  if vzdalenost(mic.x, mic.y, x, y) <= 10 then begin
    doleti := 0;

  end
  else begin
    doleti := nej_b;

  end;

end;


function je_kryty(h : Thrac; ja : Thrac; b : Ttym; mic : Tmic) : boolean;
var i, nej : integer;
    min : real;
begin
  min := 10000;
  nej := 0;

  for i := 1 to MAX_HRACU do begin
    if vzdalenost(h.x, h.y, b[i].cil_x, b[i].cil_y) < min then begin
      min := vzdalenost(h.x, h.y, b[i].cil_x, b[i].cil_y);
      nej := i;

    end;
  end;

  if (min < 30) and ((ja.cil_x <> b[nej].cil_x) or (ja.cil_y <> b[nej].cil_y)) then begin
    je_kryty := true;
  end
  else begin
    je_kryty := false;
  end;



end;

function nejblizsi_nekryty_nepritel(h : Thrac; a, b : Ttym; mic : Tmic) : byte;
var i, nej : integer;
    min : real;
begin

  min := 10000;
  nej := 0;

  for i := 1 to MAX_HRACU do begin
    if (vzdalenost(h.x, h.y, b[i].x, b[i].y) < min) and (je_kryty(b[i], h, a, mic) = false) then begin
      min := vzdalenost(h.x, h.y, b[i].x, b[i].y);
      nej := i;

    end;
  end;


  nejblizsi_nekryty_nepritel := nej;




end;

procedure petr(var a : Ttym; b : Ttym; var mic : Tmic; strana : byte);
var cil_x, cil_y, vcil_x, vcil_y, vzd : real;
    i, j, nej, druhy, vnej, vdruhy : integer;
    vmic : Tmic;
    vhrac : Thrac;
    prip_x, prip_y : real;
begin

  druhy := 0;
  nej := nejblizsi_hrac(a, b, mic, cil_x, cil_y, druhy);


  if nej > 0 then begin                    {UTOK}
    jdi_na(a[nej], cil_x, cil_y);

    for i := 2 to MAX_HRACU do begin                {nabihani}
      if i <> nej then begin

      {  if ((a[i].cil_x = a[i].x) and (a[i].cil_y = a[i].y)) then begin
          a[i].cil_x := a[i].x + 10 - 20 * strana ;
        end;        }

        vhrac.x := cil_x;
        vhrac.y := cil_y;
        vmic.x := cil_x;
        vmic.y := cil_y;
        kopni_mic(vhrac, vmic, a[i].cil_x, a[i].cil_y, V_MICE);

        vdruhy := nej;
        vnej := nejblizsi_hrac(a, b, vmic, vcil_x, vcil_y, vdruhy);


        if vnej < 0 then begin

          repeat

            vzd := vzdalenost(a[nej].x, a[nej].y, cil_x, cil_y);


            repeat
              vcil_x := random(round(vzd)) + 30;
              vcil_y := random(round(vzd)) + 30;

              if random(2) = 0 then vcil_x := vcil_x * -(0.8 + 0.4 * strana);
              if random(2) = 0 then vcil_y := vcil_y * -1;

              prip_x := a[i].x + vcil_x;
              prip_y := a[i].y + vcil_y;
            until (vzdalenost(prip_x, prip_y, a[nej].x, a[nej].y) > 40) or (random(10) = 0);




            vhrac.x := cil_x;
            vhrac.y := cil_y;
            vmic.x := cil_x;
            vmic.y := cil_y;
            kopni_mic(vhrac, vmic, prip_x, prip_y, V_MICE);

            vdruhy := 0;
            vnej := nejblizsi_hrac(a, b, vmic, vcil_x, vcil_y, vdruhy);

          until ((random(200) = 0) or (vnej = i));

          if vnej = i then begin

            jdi_na(a[i], prip_x, prip_y);
 {           //showmessage('nabiham ' + inttostr(i)); }

          end
          else begin
            if vzdalenost(a[i].x, a[i].y, a[i].cil_x, a[i].cil_y) < 15 then begin
              j := random(3);
              case j of
                0 : prip_x := 640 / 3;
                1 : prip_x := 640 - 640 / 3;
                2 : prip_x := 640 / 2;
              end;
              j := random(3);
              case j of
                0 : prip_y := 480 / 3;
                1 : prip_y := 480 - 480 / 3;
                2 : prip_y := 480 / 2;
              end;
              jdi_na(a[i], prip_x, prip_y);
            end;
          end;


        end;
      end;
    end;


                                                               {s micem}
    if vzdalenost(a[nej].x, a[nej].y, mic.x, mic.y) <= 10 then begin
      j := 0;
      for i := 2 to MAX_HRACU do begin
        if i <> nej then begin
          vmic := mic;

          kopni_mic(a[nej], vmic, a[i].cil_x, a[i].cil_y, V_MICE);

          vdruhy := nej;
          vnej := nejblizsi_hrac(a, b, vmic, vcil_x, vcil_y, vdruhy);
          if vnej > 0 then begin
            if j = 0 then begin
              j := i;
            end
            else begin
              if (vzdalenost(a[i].x, a[i].y, 639 - strana * 638, 240) < vzdalenost(a[j].x, a[j].y, 639 - strana * 638, 240)) and (vzdalenost(a[i].x, a[i].y, b[druhy].x, b[druhy].y) > 15) then begin
                j := i;

              end;

            end;
          end;
        end;


      end;

      if (j > 0) and (random(5) <> 0) then begin
        if vzdalenost(a[nej].x, a[nej].y, b[druhy].x, b[druhy].y) < 40 then begin
          kopni_mic(a[nej], mic, a[j].cil_x, a[j].cil_y, V_MICE);
        end
        else begin
          if vzdalenost(a[nej].x, a[nej].y, 639 - strana * 638, 160) < vzdalenost(a[nej].x, a[nej].y, 639 - strana * 638, 320) then begin
            jdi_na(a[nej], 639 - strana * 638, 160);
          end
          else begin
            jdi_na(a[nej], 639 - strana * 638, 320);
          end;
          kopni_mic(a[nej], mic, a[nej].cil_x, a[nej].cil_y, 5);
        end;

      end
      else begin

         {jde na branu}
        if vzdalenost(a[nej].x, a[nej].y, 639 -  strana * 638, 160) < vzdalenost(a[nej].x, a[nej].y, 639 - strana * 638, 320) then begin
          jdi_na(a[nej], 639 - strana * 638, 160);
        end
        else begin
          jdi_na(a[nej], 639 - strana * 638, 320);
        end;
        kopni_mic(a[nej], mic, a[nej].cil_x, a[nej].cil_y, 5);
      end;

      {brankar s micem v ohrozeni}
      if nej = 1 then begin
        if vzdalenost(a[nej].x, a[nej].y, b[druhy].x, b[druhy].y) < 60 then begin
                                 {pokud muze, nahraje}
          if j > 0  then begin
            kopni_mic(a[nej], mic, a[j].cil_x, a[j].cil_y, V_MICE);
          end
          else begin

            if (vzdalenost(b[druhy].x, b[druhy].y, strana * 620 + 10, 5) - vzdalenost(a[nej].x, a[nej].y, strana * 620 + 10, 5) > vzdalenost(a[nej].x, a[nej].y, strana * 620 + 10, 475) - vzdalenost(a[nej].x, a[nej].y, strana * 620 + 10, 5)) then begin
              kopni_mic(a[nej], mic, strana * 620 + 10, 5, V_MICE);
            end
            else begin
              kopni_mic(a[nej], mic, strana * 620 + 10, 475, V_MICE);
            end;

              {kdyby to nepritel vzal, tak radsi kopni jinam}
            vdruhy := 0;
            vnej := nejblizsi_hrac(a, b, mic, vcil_x, vcil_y, vdruhy);
            if vnej < 0 then begin
              if a[1].y < 240 then begin
                kopni_mic(a[nej], mic, 635 - strana * 630, 5, V_MICE);
              end
              else begin
                kopni_mic(a[nej], mic, 635 - strana * 630, 475, V_MICE);
              end;
             // showmessage('kopu do rohu');
            end;

          end;
        end;
      end;
    end;

    {strili, pokud da gol}
    vmic := mic;
    vcil_y := random(159) + 161;
    kopni_mic(a[nej], vmic, 639 - strana * 638, vcil_y, V_MICE);
    if doleti(b, vmic, 639 - strana * 638, vcil_y) = 0 then begin
      kopni_mic(a[nej], mic, 639 - strana * 638, vcil_y, V_MICE);
    end;
    vmic := mic;
    kopni_mic(a[nej], vmic, 639 - strana * 638, 165, V_MICE);
    if doleti(b, vmic, 639 - strana * 638, 165) = 0 then begin
      kopni_mic(a[nej], mic, 639 - strana * 638, 165, V_MICE);
    end;
    vmic := mic;
    kopni_mic(a[nej], vmic, 639 - strana * 638, 315, V_MICE);
    if doleti(b, vmic, 639 - strana * 638, 315) = 0 then begin
      kopni_mic(a[nej], mic, 639 - strana * 638, 315, V_MICE);
    end;



  end
  else begin                     {OBRANA}


    for i := 2 to MAX_HRACU do begin
      if i <> druhy then begin
        j := nejblizsi_nekryty_nepritel(a[i], a, b, mic);
        if j <> 0 then begin
          vmic.x := b[j].x;
          vmic.y := b[j].y;
          vhrac.x := b[j].x;
          vhrac.y := b[j].y;
          kopni_mic(vhrac, vmic, cil_x, cil_y, V_MICE);
          vmic.x := vmic.x + vmic.v_x * 2;
          vmic.y := vmic.y + vmic.v_y * 2;
          jdi_na(a[i], vmic.x, vmic.y);


        end;
      end;
    end;

    jdi_na(a[druhy], cil_x, cil_y);
    kopni_mic(a[druhy], mic, 639 - strana * 638, random(160) + 160, V_MICE);


  end;



                            {brankar}
  if (1 <> nej) and (druhy <> 1) then begin
    {

    if nej > 0 then nej := -druhy;


    vmic.x := strana * 630 + 5;
    vmic.y := 240;
    if mic.y < 220 then vmic.y := 200;
    if mic.y > 260 then vmic.y := 280;
    vhrac.x := vmic.x;
    vhrac.y := vmic.y;
    kopni_mic(vhrac, vmic, b[-nej].x, b[-nej].y, V_MICE);



    jdi_na(a[1], vmic.x + vmic.v_x * 5, vmic.y + vmic.v_y * 5);   }

   
    if nej > 0 then nej := -druhy;


    vmic.x := strana * 630 + 5;
    vmic.y := 240;
    if mic.y < 220 then vmic.y := 200;
    if mic.y > 260 then vmic.y := 280;
    vhrac.x := vmic.x;
    vhrac.y := vmic.y;
    kopni_mic(vhrac, vmic, b[-nej].x, b[-nej].y, V_MICE);


    vcil_y := vmic.y + vmic.v_y * 5;
    vcil_x := vmic.x + vmic.v_x * 5;


    prip_y := a[1].y;
    
    vmic := mic;
    kopni_mic(b[-nej], vmic, 638 * strana + 1, 161, V_MICE);
    if doleti(a, vmic, 638 * strana + 1, 161) = 0 then begin
      prip_y := prip_y - V_HRACE / 2;
{     // showmessage('-10'); }

    end;
    vmic := mic;
    kopni_mic(b[-nej], vmic, 638 * strana + 1, 319, V_MICE);
    if doleti(a, vmic, 638 * strana + 1, 319) = 0 then begin
      prip_y := prip_y + V_HRACE / 2;
{    //  showmessage('+10'); }

    end;
    if prip_y <> a[1].y then vcil_y := prip_y;



    jdi_na(a[1], vcil_x, vcil_y);

  end;



end;



















end.
