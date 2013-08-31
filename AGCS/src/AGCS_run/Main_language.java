package AGCS_run;

public class Main_language {
	String[] lang = {
		// S = sprite name
	 	// T = free text
	 	// L = list
	 	// % = [  ]
	 	// ^ = A (% without [ ])
	 	// # = 00 000 0000 etc
		"add #% to score#%}9}1",
		"clear scene#%}1",
		"clear score#%}1",
		"clear sprite#%}1",
		"copy scene#% to scene#%}1",
		"/ T}",
		"data table at #%}3",
		"data values # # # # #}9}9}9}9}9",
		"debug L}on/off",		
		"display posit at x=#% y=#%}4}4",
		"display scene#%}1",
		"display sprite#% frame#%}2}2",
		"draw (T)",
		"end if",
		"if ^ =#% then}9}9",
		"if ^ >#% then}9}9",
		"if ^ <#% then}9}9",
		"if button# is L  then}1}on/off",
		"if joystick# is L then}1}right/left/up/down",
		"if pressed key ' ' then",
		"if score#%=#% then}9}9",
		"if score#># then}9}9",
		"if score#<# then}9}9",
		"if sprite# hit sprite# then}2}2",
		"if sprite# hit color# then}2}2",
		"jump to label #%}9",
		"jump to subroutine at #%}9",
		"jump back",
		"key input %",			
		"move columns #% to #%}2}2",
		"move rows #% to #%}2}2",
		"move pixels y=#% to y=#%}4}4",
		"move screen [L] by #% columns}up/down/right/left}2",
		"move screen [L] by #% pixels}up/down/right/left}3",
		"otherwise",
		"pause for 00.00 seconds", // 00.0
		"play mpeg (T)",
		"plot a dot at x=#% y=#%}3}3",
		"print 'T'",
		"print %",
		"print at row #% column #%}3}3",			
		"print color=L}white/red/blue/green/yellow/orange/brown/cyan/black",
		"print color=%}",
		"print value of %",
		"scene# background=L}1}black/white/blue",
		"scene# background=%}1",
		"scene# foreground=L}1}black/white/blue",
		"scene# foreground=%}1",
		"scene# is [T]}1",
		"score# at row #% column #%}1}2}2",
		"score# color L}1}white/red/blue/green/yellow/orange/brown/cyan/black",
		"set ^ =#%}9",
		"set ^ =% +#%}9",
		"set ^ =% -#%}9",
		"set ^ =% *#%}9",
		"set ^ =% /#%}9",									
		"set ^ =rnd number # to #}9}9",
		"set ^ =[S] x position",
		"set ^ =[S] y position",
		"set ^ =value at data+[%]",
		"skip next if ^ =#}9",
		"skip next if ^ >#}9",
		"skip next if ^ <#}9",
		"sprite1 is [S]",
		"sprite[S] animation speed=#%",
		"sprite[S] direction = xL#%}+/-}4",
		"sprite[S] direction = yL#%}+/-}4",
		"sprite[S] is L}seen/hidden",
		"sprite[S] movement speed=#%}3",
		"sprite[S] uses frames # to #}2}2",
		"sprite[S] x position=#%}4",
		"sprite[S] x position=#%}4",		
		"stop program",
		"switch to program[T] L data}clear/keep"
	};

	
	// idx = location in lang[] to start showing language, numrows = how many to show
	// add static if method does something that doesn't depend on the individual characteristics of its class
	public String showlang(int idx, int numrows) {
		int pos1, pos2, pos3, pos4, pos5, pos6, pos7;
		int numchars=1; // how big i.e. 2=00 3=000
		String htmloutput="";
		String t1;
		String temp;
		for (int i=idx; i<idx+numrows; i++) {
			temp = lang[i];
			// parse line
			for (int i1=1; i1<temp.length(); i1++) {
				pos1=temp.indexOf("T",i1); // T = free text
				pos2=temp.indexOf("L",i1); // L = list
				pos3=temp.indexOf("%",i1); // % = [  ]
				pos4=temp.indexOf("^",i1); // ^ = A (% without [ ])
				pos5=temp.indexOf("#",i1); // # = 00 000 0000 etc
//				pos6=temp.indexOf("S",i1);

				// add 0000 and [  ] where needed
				if (pos5==-1 && pos3>-1) {temp.replace("%", "[  ]");}
				temp.replace("#","000");
				pos7=temp.indexOf("}",i1);
				if (pos7>-1) {numchars=Integer.parseInt(temp.substring(pos7+1,1));}
				if (numchars==1) {t1="1";}
				else if (numchars==2) {t1="00";}
				else if (numchars==3) {t1="000";}
				else if (numchars==4) {t1="0000";}
				else if (numchars==5) {t1="00000";}
				else if (numchars==6) {t1="000000";}
				else if (numchars==7) {t1="0000000";}
				else if (numchars==8) {t1="00000000";}
				else t1="000000000";
				temp.replace("S","  ");	
				temp.replace("^","a");						
				htmloutput+=temp;										
			}
		
		}
	return htmloutput;
	}
	
}
