<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
<head>
<title>Travian cz2</title>
<link rel=stylesheet type="text/css" href="un3.css">
<script src="un3.js" type=text/javascript></script>

<meta http-equiv="imagetoolbar" content="no">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<body onload="start()"><div class="div1"></div><map name="nb"><area shape=rect coords="0,0,35,100" href="berichte.php" title="Hlášení"><area shape=rect coords="35,0,70,100" href="nachrichten.php" title="Zprávy"></map><div class="div2">
<a href="dorf1.php" id="n1"><img class="fl2" src="img/un/l/d1.gif" width="70" height="100" border="0" onmouseover="this.className='fl1'" onmouseout="this.className='fl2'" title="Přehled vesnice"></a><a href="dorf2.php" id="n2"><img class="fl2" src="img/un/l/d2.gif" width="70" height="100" border="0" onmouseover="this.className='fl1'" onmouseout="this.className='fl2'" title="Centrum vesnice"></a><a href="karte.php" id="n3"><img class="fl2" src="img/un/l/ka.gif" width="70" height="100" border="0" onmouseover="this.className='fl1'" onmouseout="this.className='fl2'" title="Mapa"></a><a href="statistiken.php" id="n4"><img class="fl2" src="img/un/l/st.gif" width="70" height="100" border="0" onmouseover="this.className='fl1'" onmouseout="this.className='fl2'" title="Statistika"></a><img src="img/un/l/m4.gif" width="70" height="100" border="0" usemap="#nb"></div><div class="plus"><a href="plus.php"><img src="img/cz/a/plus.gif" width="80" height="100" border="0" title="Plus menu"></a></div><img src="img/un/a/x.gif" width="1" height="90" border="0">

<table cellspacing="0" cellpadding="0"><tr valign="top">
<td class="s1">
<a href="http://www.travian.cz/"><img src="img/cz/a/travian0.gif" width="116" height="60" border="0"></a>

<table style="height:auto" width="116" cellspacing="0" cellpadding="0">
<tr>
<td class="menu">
<a href="http://www.travian.cz/">Úvod</a>
<a href="#" onClick="Popup(0,0); return false;">Návod</a>
<a href="spieler.php?uid=9463">Profil</a><a href="logout.php">Logout</a><br><br>
<a href="http://forum.travian.cz/" target="_blank">Fórum</a><a href="http://www.travian.cz/chat/" target="_blank">Chat</a><br><br><a href="plus.php?id=3">Travian <b><font color="#71D000">P</font><font color="#FF6F0F">l</font><font color="#71D000">u</font><font color="#FF6F0F">s</font></b></a>
<!--<a href="mobile.php">Travian <i><font color="#71D000"><b>Mobile</b></font></i></a>-->
</td>
</tr>
</table>

<td class="s2"><img src="img/un/a/w.gif" width="1" height="10" border="0"></td>
<td class="s3"><h1>Poslat jednotky</h1>

<p><table class="p1" style="width:100%"cellspacing="1" cellpadding="0"><tr><td>
<table width="100%" class="f10">
<form method="POST" name="snd" action="a2b.php">
<input type="hidden" name="b" value="1">
<tr>

<td width="20"><img src="img/un/u/1.gif" width="16" height="16" title="Legionář"></td><td width="35"><input class="fm" type="Text" name="t1" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td><td width="20"><img src="img/un/u/4.gif" width="16" height="16" title="Equites Legáti"></td><td width="35"><input class="fm" type="Text" name="t4" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td><td width="20"><img src="img/un/u/7.gif" width="16" height="16" title="Římanské beranidlo"></td><td width="35"><input class="fm" type="Text" name="t7" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td><td width="20"><img src="img/un/u/9.gif" width="16" height="16" title="Senátor"></td><td width="35"><input class="fm" type="Text" name="t9" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td></tr>


<tr>
<td width="20"><img src="img/un/u/2.gif" width="16" height="16" title="Pretorián"></td><td width="35"><input class="fm" type="Text" name="t2" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td><td width="20"><img src="img/un/u/5.gif" width="16" height="16" title="Equites Imperatoris"></td><td width="35"><input class="fm" type="Text" name="t5" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td><td width="20"><img src="img/un/u/8.gif" width="16" height="16" title="Ohnivý katapult"></td><td width="35"><input class="fm" type="Text" name="t8" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td><td width="20"><img src="img/un/u/10.gif" width="16" height="16" title="Osadník"></td><td width="35"><input class="fm" type="Text" name="t10" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td></tr>


<tr>
<td width="20"><img src="img/un/u/3.gif" width="16" height="16" title="Imperián" border="0" onClick="document.snd.t3.value=''; return false;"></td><td width="35"><input class="fm" type="Text" name="t3" value="" size="2" maxlength="6"></td><td class="f8"><a href="#" onClick="document.snd.t3.value=5; return false;">(5)</a></td><td width="20"><img src="img/un/u/6.gif" width="16" height="16" title="Equites Caesaris"></td><td width="35"><input class="fm" type="Text" name="t6" value="" size="2" maxlength="6"></td><td class="f8 c b"><b>(0)</b></td><td colspan="6"></td></tr></table></td></tr></table></p>

<p><table width="100%" class="f10">
<tr><td valign="top" width="33%">
<div class="f10"><input type="Radio" name="c" value="2" checked>Podpora</div>
<div class="f10"><input type="Radio" name="c" value="3" >Útok: normální</div>
<div class="f10"><input type="Radio" name="c" value="4" >Útok: loupež</div>
</td>

<td valign="top">
<div><font size="+1"><b>Vesnice:</b></font>

<input class="fm" type="Text" name="dname" value="" size="10" maxlength="20"></div><div><i>nebo</i></div>

<div><font size="+1">
<b>X:</b>
<input class="fm" type="Text" name="x" value="54" size="2" maxlength="4">
<b>Y:</b>
<input class="fm" type="Text" name="y" value="79" size="2" maxlength="4">
</font></div>

</tr>
</table>

<p><input type="image" value="ok" border="0" name="s1" src="img/cz/b/ok1.gif" width="50" height="20" onMousedown="btm1('s1','','img/cz/b/ok2.gif',1)" onMouseOver="btm1('s1','','img/cz/b/ok3.gif',1)" onMouseUp="btm0()" onMouseOut="btm0()"></input></p>
</form></p></td>
<td class="s2"><img src="img/un/a/w.gif" width="1" height="10" border="0"></td>
</tr>
</table><div class="div4">
<table align="center" cellspacing="0" cellpadding="0"><tr class="f10" valign="top">
<td><img src="img/un/r/1.gif" width="18" height="12" border="0" title="Dřevo"></td>
<td id=l1 title=240>557/4000</td>
<td class="s7"> <img src="img/un/r/2.gif" width="18" height="12" title="Hlína"></td>
<td id=l2 title=260>1001/4000</td>
<td class="s7"> <img src="img/un/r/3.gif" width="18" height="12" title="Železo"></td>
<td id=l3 title=260>1188/4000</td><td class="s7"> <img src="img/un/r/4.gif" width="18" height="12" title="Obilí"></td>
<td id=l4 title=155>1397/3100</td>
<td class="s7"> &nbsp;<img src="img/un/r/5.gif" width="18" height="12" alt="Spotřeba obilí">&nbsp;247/402</td></tr></table>
</div><div class="div3">Vygenerováno za <b>9</b> ms<br>Čas serveru: <b><span id=tp1>22:09:01</span></b> </div>
<div id="ce"></div></body>
</html>