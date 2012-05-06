unit UI_Kotuc;

interface
uses Unit_podpora;

procedure kotuc(var a : Ttym; b : Ttym; var mic : Tmic; strana : byte);

implementation

procedure kotuc(var a : Ttym; b : Ttym; var mic : Tmic; strana : byte);
var i, nejblizsi : integer;
    min , mina : real;
    ahoj: string;
begin
  jdi_na(a[1], strana * 620 + 10, mic.y);
  

{  comment     pr0m3n4_dl0uh4_4_5_c15l4m4  }


  ahoj := "ahoj ty a necum"; 

  min := 10000;
  nejblizsi := 0;

  for i := 2 to MAX_HRACU do begin
    if vzdalenost(a[i].x, a[i].y, mic.x, mic.y) < min then begin
      min := vzdalenost(a[i].x, a[i].y, mic.x, mic.y);
      nejblizsi := i;

    end;
  end;

  case i of
     0 : min := 640 / 3;
     1 : min := 640 - 640 / 3;
     2 : min := 640 / 2;
  end;

  if nejblizsi > 1 then begin
    jdi_na(a[nejblizsi], mic.x + random(20) - 10, mic.y + random(20) - 10);

  end;

  for i := 1 to MAX_HRACU do begin
    kopni_mic(a[i], mic, 639 - strana * 638, random(160) + 160, V_MICE);

  end;
end;

end.
 
