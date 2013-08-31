package AGCS_run;

import java.lang.*;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.UIManager.*; 		// void main NimbusLookAndFeel
import javax.swing.SwingUtilities.*;  // void main NimbusLookAndFeel
import javax.swing.text.BadLocationException;

import java.awt.Color;  			// display of control area
import java.util.ArrayList; 		// display of control area

import java.io.File;

public class Main_AGCS extends JFrame  implements ComponentListener, MouseListener { // MouseMotionListener
int framewidth=700;         
int frameheight=750;  
int frameoldwidth=700;
int frameoldheight=600;
ScheduledExecutorService executorService;
JList langabbrev;
DefaultListModel listModel;

JScrollPane langpane;
JScrollPane codepane;
JPanel jp_mainmenu;
JPanel jp_filemenu;
JPanel jp_input;
JPanel jp_menus;
JPanel content;
int mousex, mousey;
StringBuilder code = new StringBuilder("");
JTextArea htmlTextArea = new JTextArea(); 
//JTextPane htmlTextArea = new JTextPane(); //Pane();	
//public static Color red = new Color(128, 0, 0);
Font asciifnt;

private CardLayout cardLayout = new CardLayout();
JList listcode;

String menushowing="mainmenu"; 
String menutoshow="filemenu";

		String[] lang = {
			// S = spr name
			// M = milliseconds
		 	// T = free text
		 	// L = list
		 	// % = [  ]
		 	// ^ = A (% without [ ])
		 	// # = 00 000 0000 etc
		 	// #%
			"add #% to score#%}9}1",
			"clear scene#%}1",
			"clear score#%}1",
			"clear spr[S]}1",
			"copy scene#% to scene#%}1",
			"// comment T}",
			"data set# at line #%}3",
			"data set# in file [L]}3",			 // new
			"data values # # # # #}9}9}9}9}9",
			"debug L}on/off",
			"display posit at x=#% y=#%}4}4",
			"display scene#%}1",
			"display spr#% frame#%}2}2",
			"draw (T)",
			"end if",
			"if ^ =#% then}9}9",
			"if ^ >#% then}9}9",
			"if ^ <#% then}9}9",
			"if button# is L  then}1}on/off",
     		"if mouse button is L then}down/up/pushed/let go",
     		"if mouse ptr on spr[L] then",
     		"if mouse ptr moves L then}right/left/up/down",    			
			"if pressed key [T] then",
			"if score#%=#% then}9}9",
			"if score#># then}9}9",
			"if score#<# then}9}9",
			"if spr[S] hit color # then}6",
			"if spr[S] hit spr[S] then",
     		"if spr[S] curr frame=#%}2}2",
			"if any sprite hit spr[S] then",
			"if any sprite hit color # then}6",
     		"if any sprite curr frame=#%}2",
     		"if timer#%=#% then",
     		"input [%]",
			"jump to label #%}9",
			"jump to subroutine at #%}9",
			"jump out",
			"key input %",			
			"move cols #% to #%, rows #% to #%}2}2}2}2",
			"move pixels xy (#%,#%)-(#%,#%)}3}3}3}3",
			"move screen [L] by #% columns}up/down/right/left}2",
			"move screen [L] by #% pixels}up/down/right/left}3",
			"otherwise",
			"pause for M ms", // 00.0
			"play wave file (T)",
			"plot a dot at x=#% y=#%}3}3",
			"print 'T'",
			"print %",
			"print at row #% column #%}3}3",
			"print at x=#%, y=#%}3}3",
			"print color=L}white/red/blue/green/yellow/orange/brown/cyan/black",
			"print color=%}",
			"print value of %",
			"reset timer",
			"return to state[%]",
			"save program state[%]",
			"screen# background=L}1}black/white/blue",
			"screen# background=%}1",
			"screen# foreground=L}1}black/white/blue",
			"screen# foreground=%}1",
			"screen# is [T]}1",
			"scene# = [T]}1",	
			"scene# scrolls by #% pixels}1}2",
			"scene# view xy (#,#)-(#,#)}1}3}3}3}3",
			"score# at row #% column #%}1}2}2",
			"score# at x=#% y=#%}1}3}3",			
			"score# color L}1}white/red/blue/green/yellow/orange/brown/cyan/black",
			"set ^ =#%}9",
			"set ^ =% +#%}9",
			"set ^ =% -#%}9",
			"set ^ =% *#%}9",
			"set ^ =% /#%}9",
     		"set ^ =mouse curr x position",
     		"set ^ =mouse curr y position",
     		"set ^ =mouse button status",		
			"set ^ =rnd number # to #}9}9",
     		"set ^ =spr[S] animation speed}2",			
			"set ^ =spr[S] current frame}2",
     		"set ^ =spr[S] dist from spr#%}2",						
     		"set ^ =spr[S] movement speed}2",
     		"set ^ =spr[S] rotation angle}2",
     		"set ^ =spr[S] x position}2",
     		"set ^ =spr[S] y position}2",
			"set ^ =value at data+[%]",
			"skip next #% lines if ^ =#}2}9",
			"skip next #% lines if ^ >#}9",
			"skip next #% lines if ^ <#}9",
			"sprite loading is [S]}2",
			"spr[S] animation speed=#%}2}2",
			"spr[S] direction = xL#%}2}+/-}4",
			"spr[S] direction = yL#%}2}+/-}4",
			"spr[S] is L}2}seen/hidden",
			"spr[S] movement speed=#%}2}3",  // spr under/over sp1 to sp1
			"spr[S] screen wrap L}2}on/off",
			"spr[S] uses frames #% to #%}2}2}2",
			"spr[S] x position=#%}2}4",
			"spr[S] y position=#%}2}4",		
			"sprites all delay by M ms",
			"start timer#%",
			"stop timer#%",
			"stop program", // trace window on
			"switch to program[T] L data}reset/retain"
		};

		String[] displang = {
			"add 0000 to score1",
			"clear scene1",
			"clear score1",
			"clear spr[     ]",
			"copy scene1 to scene3",
			"// comment",
			"data set1 at line 100",
			"data set1 in file [     ]",			 // new
			"data values 000 000 000 000 000",
			"debug on",
			"display posit at x=000 y=000",
			"display scene1",
			"display spr[     ] frame1",
			"end if",
			"if a =000 then",
			"if a >000 then",
			"if a <000 then",
			"if button1 is on then",
     		"if mouse button1 is down then",
     		"if mouse ptr on spr[     ] then",
     		"if mouse ptr moves left then",    			
			"if pressed key [ ] then",
			"if score1=0000 then",
			"if score1>0000 then",
			"if score1<0000 then",
			"if spr[     ] hit color1 then",
			"if spr[     ] hit sprite1 then",
     		"if spr[     ] curr frame=00",
			"if any sprite hit sprite[     ] then",
			"if any sprite hit color[  ] then",
     		"if any sprite curr frame=1",
     		"if timer1=000 then",
			"jump to label 100",
			"jump to subroutine at 100",
			"jump back",
			"key input [  ]",			
     		"line input [  ]",	
			"move cols 00 to 00, rows 00 to 00",
			"move pixels xy (000,000)-(000,000)",
			"move pixels up by 00 columns",
			"move pixels up by 00 pixels",
			"otherwise",
			"pause for 0.00 ms", // 00.0
			"play wave file [       ]",
			"plot a dot at x=000 y=000",
			"print _______",
			"print [  ]",
			"print at row 00 column 00",
			"print at x=000, y=000",
			"print color=red",
			"print color=[  ]",
			"print value of [  ]",
			"reset timer1",
			"return to state[  ]",
			"save program state[  ]",
			"screen1 background=blue",
			"screen1 background=00",
			"screen1 foreground=blue",
			"screen1 foreground=00",
			"screen1 is [       ]",
			"scene1 = [       ]",	
			"scene1 scrolls by 000 pixels",
			"scene1 view xy (000,000)-(000,000)",
			"score1 at row 00 column 00",
			"score1 at x=000 y=000",			
			"score1 color 00",
			"set a =000",
			"set a =a + 000",
			"set a =a - 000",
			"set a =a * 000",
			"set a =a / 000",
     		"set a =mouse curr x position",
     		"set a =mouse curr y position",
     		"set a =mouse button status",		
			"set a =rnd number 000 to 000",
     		"set a =spr[     ] animation speed",			
			"set a =spr[     ] current frame",
     		"set a =spr[     ] dist from sprite2",						
     		"set a =spr[     ] movement speed",
     		"set a =spr[     ] rotation angle",
     		"set a =spr[     ] x position",
     		"set a =spr[     ] y position",
			"set a =value at data+[  ]",
			"skip next 00 lines if a =000",
			"skip next 00 lines if a >000",
			"skip next 00 lines if a <000",
			"sprite loading is [    ]",
			"spr[     ] animation speed=000",
			"spr[     ] direction = x+00 y+00",
			"spr[     ] is seen",
			"spr[     ] movement speed=000",  // sprite under/over sp1 to sp1
			"spr[     ] screen wrap on",
			"spr[     ] uses frames 00 to 00",
			"spr[     ] x position=000",
			"spr[     ] y position=000",		
			"sprites all delay by 0.00 ms",
			"start timer1",
			"stop timer1",
			"stop program", // trace window on
			"switch to program[       ] retain data"
		};

		String[] clicklang = { // ONLY where mouseclick registers for each value! VERY important 
			"add #### to ######",
			"clear ######",
			"clear ######",
			"clear spr[#####]",
			"copy ###### to ######",
			"// #######",
			"data #### at line ###",
			"data #### in file #######",			 // new
			"data values ### ### ### ### ###",
			"########",
			"display posit at x=### y=###",
			"display ######",
			"display sprite####### ######",
			"end if",
			"if ##=### then",
			"if ##>### then",
			"if ##<### then",
			"if ####### is ## then",
     		"if mouse ####### is #### then",
     		"if mouse ptr on spr####### then",
     		"if mouse ptr moves #### then",    			
			"if pressed key ### then",
			"if ######=#### then",
			"if ######>#### then",
			"if ######<#### then",
			"if spr####### hit ###### then",
			"if spr####### hit ####### then",
     		"if spr####### curr frame###",
			"if any sprite hit sprite####### then",
			"if any sprite hit ###### then",
     		"if any sprite curr #######",
     		"if ######=### then",
			"jump to label ###",
			"jump to subroutine at ###",
			"jump back",
			"key input ####",			
     		"line input ####",	
			"move cols ## to ##, rows ## to ##",
			"move pixels xy (###,###)-(###,###)",
			"move pixels up by ## columns",
			"move pixels up by ## pixels",
			"otherwise",
			"pause for #### ms", // 00.0
			"play wave file [#######]",
			"plot a dot at x=### y=###",
			"print #######",
			"print ####",
			"print at ###### #########",
			"print at #####, #####",
			"print color####",
			"print color#####",
			"print value of ####",
			"reset ######",
			"return to state####",
			"save program state####",
			"####### background#####",
			"####### background###",
			"####### foreground#####",
			"####### foreground###",
			"####### is [#######]",
			"###### = [#######]",	
			"###### scrolls by ### pixels",
			"###### view xy (###,###)-(###,###)",
			"###### at ###### colu#####",
			"###### at x=### y=###",			
			"###### color###",
			"set###=000",
			"set###=##+ ###",
			"set###=##- ###",
			"set###=##* ###",
			"set###=##/ ###",
     		"set###=mouse curr x position",
     		"set###=mouse curr y position",
     		"set###=mouse button status",		
			"set###=rnd number ### to ###",
     		"set###=spr####### animation speed",			
			"set###=spr####### current frame",
     		"set###=spr####### dist from sprite2",						
     		"set###=spr####### movement speed",
     		"set###=spr####### rotation angle",
     		"set###=spr####### x position",
     		"set###=spr####### y position",
			"set###=value at data+####",
			"skip next####lines if###=###",
			"skip next####lines if###>###",
			"skip next####lines if###<###",
			"sprite loading is #######",
			"spr####### animation speed=###",
			"spr####### direction = x### y###",
			"spr####### is ####",
			"spr####### movement speed=###",  // sprite under/over sp1 to sp1
			"spr####### screen wrap###",
			"spr####### uses frames####to###",
			"spr####### x position=###",
			"spr####### y position=###",		
			"sprites all delay by #### ms",
			"start ######",
			"stop ######",
			"stop program", // trace window on
			"switch to program######### retain data"
		};

static class StringWrapper {
       String whichmenu;
//       Container scr;
       
       StringWrapper(String whichmenu) {
           this.whichmenu = whichmenu;
//           this.scr = scr;          
       } 
}
final StringWrapper savedcontent = new StringWrapper("mainmenu");

/*
public static String getwindowheight() {
	return h;
}
public static String setwindowheight(s) {
	h=s;
}
*/

	public Main_AGCS() { // Constructor
		super("Adventure Game Construction Set");
		// =================== General setup of display =====================  	
		content = new JPanel();
		content.setLayout(new GridBagLayout());
	    content.setBackground(Color.white);
		content.addComponentListener(this);							
//		content.addMouseMotionListener(this);
		jp_mainmenu = new JPanel();
		jp_filemenu = new JPanel();
		jp_input = new JPanel();	
//        jp_input.setLayout(cardLayout);
        jp_menus = new JPanel();        
        jp_menus.setLayout(cardLayout);

		builddisplay_input();		
		builddisplay_codearea();
		builddisplay_langarea();								
		builddisplay_mainmenubuttons();										
		builddisplay_filemenubuttons();												
        jp_menus.add(jp_mainmenu, "MainMenu");
        jp_menus.add(jp_filemenu, "FileMenu");
        //blah
   		cardLayout.show(jp_menus, "MainMenu");
		content.add(jp_menus);
   		add(content);
		setMinimumSize(new Dimension(600, 500));   		
	} 
/*
		// =================== setup scheduler =====================        
		executorService = Executors.newSingleThreadScheduledExecutor();
	    executorService.scheduleWithFixedDelay(new Runnable() {
	        @Override
        	public void run() {
        		frameresize();        	
//	            System.out.println("beep ");
        	}
	    }, 1, 100, TimeUnit.MILLISECONDS);
	}
*/	
	// =================== setup display of input area =====================
	public void builddisplay_input() {
//		codepane = new JScrollPane(htmlTextArea);		

//		try {Font font = new Font("vt323", Font.PLAIN,22); htmlTextArea.setFont(font);}
//		catch(Exception e) {System.out.println("Could not set font.");}
		GridBagConstraints c = new GridBagConstraints();    


//		c.gridheight = 1;        
		c.insets = new Insets(6,6,6,6);       // top left bottom right  
//    	label1.setFont (new Font ("vt323", Font.PLAIN, 12));
//	   	label1.setText("<html><br><br>[  ] Insert<br>Ctrl-X Delete<br>Ctrl-U Undo<br>Ctrl-L Clear<br>Ctrl-R Run<br>Ctrl-E Menu<br>Ctrl-H Help</html>");


		Container editpane = new Container(); // editpane is needed to get all components in horizontal format
		editpane.setLayout(new BoxLayout(editpane, BoxLayout.Y_AXIS));

		String bfont="vt323";	

		// add edit box
		JTextField textinput = new JTextField();
//		JTextArea textinput = new JTextArea(1,10);		
		textinput.setBounds(1,1,20,1);
		textinput.setColumns(10);
		textinput.setBorder(BorderFactory.createLoweredBevelBorder());
		c.weighty=0;	
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.VERTICAL;		
		editpane.add(textinput, c);
		textinput.setMaximumSize(new Dimension(200,20));

		// add var label
	    JLabel label1 = new JLabel ("Vars");
		label1.setFont (new Font (bfont, Font.PLAIN, 18));	    
	    label1.setBackground(Color.WHITE);
		editpane.add(label1,c);		
		
		// add var list	
		String[] temp = {"12123123r1","r2","r3","r4","r5","r6"};
		JList recentvarlist = new JList(temp);
		recentvarlist.setFont (new Font (bfont, Font.PLAIN, 18));	    
		JScrollPane temp2 = new JScrollPane(recentvarlist);
		temp2.setMaximumSize(new Dimension(200,160));
		c.weighty=0;
		editpane.add(temp2, c);
             
    	final ButtonGroup bgrp1 = new ButtonGroup();
	    JRadioButton radioButton1;
    	editpane.add(radioButton1 = new JRadioButton("by recent"));
	    radioButton1.setActionCommand("recent");
	    bgrp1.add(radioButton1);

	    editpane.add(radioButton1 = new JRadioButton("by name"));
	    radioButton1.setActionCommand("name");
    	bgrp1.add(radioButton1);

    	final ButtonGroup bgrp2 = new ButtonGroup();
	    JRadioButton radioButton2;
	    editpane.add(radioButton2 = new JRadioButton("all vars"));
	    radioButton2.setActionCommand("all vars");
    	bgrp2.add(radioButton2);
        
    	editpane.add(radioButton2 = new JRadioButton("just local"));
	    radioButton2.setActionCommand("just local");
	    bgrp2.add(radioButton2);

		// add subrout label
	    JLabel label4 = new JLabel ("Subroutines");
		label4.setFont (new Font (bfont, Font.PLAIN, 18));	    
	    label4.setBackground(Color.WHITE);
		editpane.add(label4,c);		
        
		// subrout list
		String[] temp6 = {"hello1","hello2","hello3","hello4","hello5","hello6","hello7","hello8"};
		JList allsublist = new JList(temp6);
		allsublist.setFont (new Font (bfont, Font.PLAIN, 18));	    		
		JScrollPane temp7 = new JScrollPane(allsublist);
		temp7.setMaximumSize(new Dimension(200,100));
		c.weighty=0;
		editpane.add(temp7, c);

		// add sprites label
	    JLabel label3 = new JLabel ("Sprites");
		label3.setFont (new Font (bfont, Font.PLAIN, 18));	    
	    label3.setBackground(Color.WHITE);
		editpane.add(label3,c);		
					
		// sprites list
		String[] temp4 = {"sprite1","sprite2","sprite3","sprite4","sprite5","sprite6","sprite7","sprite8"};
		JList allsprlist = new JList(temp4);
		allsprlist.setFont (new Font (bfont, Font.PLAIN, 18));	    		
		JScrollPane temp5 = new JScrollPane(allsprlist);
		temp5.setMaximumSize(new Dimension(200,100));
		c.weighty=0;
		editpane.add(temp5, c);

		c.gridheight = GridBagConstraints.REMAINDER;
		// add all var label
	    JLabel label5 = new JLabel ("");
		label5.setFont (new Font (bfont, Font.PLAIN, 18));	    
	    label5.setBackground(Color.WHITE);
		editpane.add(label5,c);			
		
		jp_input.add(editpane);
		jp_input.setBackground(Color.WHITE);
		content.add(jp_input);		
	}
        
	        
	// =================== setup display of code area =====================
	public void builddisplay_codearea() {
		try {
			//Returned font is of pt size 1
			asciifnt = Font.createFont(Font.TRUETYPE_FONT, new File("ASCII.TTF"));
		    //Derive and return a 12 pt version. Need to use float otherwise it would be interpreted as style
		   	 asciifnt = asciifnt.deriveFont(16F);
		} catch (IOException|FontFormatException e1) {
			System.out.println("HAH1");
		}		                
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ASCII.TTF")));
		} catch (IOException|FontFormatException e2) {
		System.out.println("HAH2");
		}

//		htmlTextArea.setContentType("text/html");
		htmlTextArea.setFont(asciifnt);
		htmlTextArea.setEditable(true);		
//		Font vt323 = new Font("vt323", Font.BOLD, 12);
//		htmlTextArea.setFont(vt323);
//		htmlTextArea.setForeground(Color.BLUE);		
//        htmlTextArea.setText(code.toString());
//		Event e; int x; int y;
//		htmlTextArea.mouseDown() {
		
//		}
		
//		try {Font font = new Font("vt323", Font.PLAIN,22); htmlTextArea.setFont(font);}
//		catch(Exception e) {System.out.println("Could not set font.");}

		// --- code pane ---	
		String bfont="vt323";
		JButton btn1c = new JButton("<html><center>Insert</center></html>");			
		btn1c.setFont (new Font (bfont, Font.PLAIN, 18));
		btn1c.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Insert!!!");
	        }
    	});
		JButton btn2c = new JButton("<html><center>Delete</center></html>");			
		btn2c.setFont (new Font (bfont, Font.PLAIN, 18));
		btn2c.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Delete!!!");
	        }
    	});    	
		JButton btn3c = new JButton("<html><center>Copy</center></html>");			
		btn3c.setFont (new Font (bfont, Font.PLAIN, 18));
		btn3c.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Copy!!!");
	        }
    	});   
		JButton btn4c = new JButton("<html><center>Paste</center></html>");			
		btn4c.setFont (new Font (bfont, Font.PLAIN, 18));
//		btn4c.setMinimumSize(new Dimension(120,30));
//		btn4c.setPreferredSize(new Dimension(120,30));		
//		btn4c.setMaximumSize(new Dimension(120,30));
		btn4c.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Paste!!!");
	        }
    	});       	
    	
//		Container editbuttonpane = new Container(); // editpane is needed to get all components in horizontal format
//		editbuttonpane.setLayout(new BoxLayout(editbuttonpane, BoxLayout.X_AXIS));

		//	----------------  create code area ----------------
		// step 1 - add buttons
		JPanel editbuttonpane = new JPanel();
		editbuttonpane.setLayout(new BoxLayout(editbuttonpane, BoxLayout.LINE_AXIS));
		editbuttonpane.add(btn1c);
		editbuttonpane.add(btn2c);
		editbuttonpane.add(btn3c);
		editbuttonpane.add(btn4c);		
    	editbuttonpane.setBackground(Color.white);
//    	editbuttonpane.setMinimumSize(new Dimension(300,30));
//    	editbuttonpane.setMaximumSize(new Dimension(300,30));    	
//    	editbuttonpane.setPreferredSize(new Dimension(300,30));
    	editbuttonpane.setBorder(BorderFactory.createLineBorder(Color.black));
		// step 2 - add code area 
		codepane = new JScrollPane(htmlTextArea);
	    codepane.setPreferredSize(new Dimension(framewidth/2-100, frameheight-160));
//	    codepane.setMinimumSize(new Dimension(200, frameheight-160));
	    
//    	codepane.setBorder(BorderFactory.createLineBorder (Color.black, 2));
    	codepane.setBackground(Color.white);

		// step 3 - put buttons and code together in vertical column
		GridBagConstraints c = new GridBagConstraints();    
//		c.insets = new Insets(0,0,0,0);       // top left bottom right  		
//        c.ipadx = 0;
//        c.ipady = 0;	
		c.gridy = 0;
		c.gridx = 1;
		c.weighty=0;
        c.anchor = GridBagConstraints.NORTHWEST;		
		content.add(editbuttonpane,c);				

		ScrollPaneLayout sl = new ScrollPaneLayout();		
		c.insets = new Insets(35,0,0,0);       // top left bottom right  
		c.gridy = 0;
		c.gridx = 1;
		c.weighty = 0;
        c.anchor = GridBagConstraints.SOUTH;		
		content.add(codepane, c);        		

		 MouseListener mouseListener = new MouseAdapter() {
		     public void mouseClicked(MouseEvent e) {
		         if (e.getClickCount() == 1) {
					int caretpos=htmlTextArea.getCaretPosition();
					int linenum=0;
					try {
						linenum = htmlTextArea.getLineOfOffset(caretpos);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					int i=0;
					try {
						i = caretpos - htmlTextArea.getLineStartOffset(linenum);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // columnnum
//scooby

					
					int npos=0; // position on \n hard return
					int pos1 = htmlTextArea.getLineStartOffset(i);
					int pos2 = htmlTextArea.getLineEndOffset(i);					
					String s = htmlTextArea.getText(pos1,pos2-pos1); // param 1 = caretpos-i
					System.out.println("["+i+"]["+s+"]");								    
					//clicklang
					     
		          }
		     }
		 };
		htmlTextArea.addMouseListener(mouseListener);		 
	}

	// =================== setup display of lang area =====================  	
	public void builddisplay_langarea() {

		langabbrev = new JList(displang);

		 MouseListener mouseListener = new MouseAdapter() {
		     public void mouseClicked(MouseEvent e) {
		         if (e.getClickCount() == 1) {
		                int index = langabbrev.locationToIndex(e.getPoint());	
						int len=code.length();	
						char test=0;
						if (len>0) {test=code.charAt(len-1);}
						int i=(int) test;
						if (i==219) { // cursor was end of of program code. It would not be at the end if someone inserted lines somewhere in the middle of code thus changing the cursor position
							if (len>5) {code.replace(len-1,len,"");} 	// remove (char)219> at the end
						}
//						code.replace(0, len, "<");
						code.append(displang[index]); // "<font color='red'>100</font><br>");
						//scooby
				    	code.append("\n"+(char)219);
						htmlTextArea.setText(code.toString());					    				    	
		          }
		     }
		 };
		langabbrev.addMouseListener(mouseListener);
		
		try {Font font = new Font("vt323", Font.PLAIN,22); langabbrev.setFont(font);}
		catch(Exception e) {System.out.println("Could not set font.");}		
				
		langpane = new JScrollPane(langabbrev);		
		langpane.setAlignmentX(langabbrev.LEFT_ALIGNMENT);
	    langpane.setPreferredSize(new Dimension(framewidth/2+100, frameheight-120));
	    langpane.setMinimumSize(new Dimension(300, frameheight-120));
	    
    	langpane.setBorder(BorderFactory.createLineBorder (Color.black, 2));
    	langpane.setBackground(Color.white);
  	
		GridBagConstraints c = new GridBagConstraints();    

	// --- language pane ---
		c.insets = new Insets(0,2,0,2);       // top left bottom right  	
        c.fill = GridBagConstraints.VERTICAL;
		c.gridheight = GridBagConstraints.REMAINDER;	        
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
//		c.gridheight = 10;		
		content.add(langpane, c);         	            	
					
	}

   	
	// =================== setup display of mainmenu button area =====================
	public void builddisplay_mainmenubuttons() {
  		GridBagConstraints c = new GridBagConstraints();    
		String s;
        c.gridx = 3;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;        
        c.anchor = GridBagConstraints.NORTH;
        c.ipadx = 5;
        c.ipady = 2;
//		c.gridheight = 10;        
		c.insets = new Insets(2,15,2,15);       // top left bottom right  
		String bfont="vt323";	
		
		JButton btn1m = new JButton("<html><center>Menu</center></html>");			
		btn1m.setFont (new Font (bfont, Font.PLAIN, 20));
		btn1m.setMaximumSize(new Dimension(60,30));
		btn1m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Menu!!!");
	        }
    	});
        c.gridy = 1;			
		JButton btn2m = new JButton("<html><center>File</center></html>");	
		btn2m.setFont (new Font (bfont, Font.PLAIN, 20));
		btn2m.setMaximumSize(new Dimension(60,30));
		btn2m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		cardLayout.show(jp_menus, "FileMenu");
   	        	System.out.println("Hello File!!!");
	        }
    	});
		//savedcontent.btn2=btn2;
        c.gridy = 2;
		JButton btn3m = new JButton("<html><center>Clr</center></html>");	
		btn3m.setFont (new Font (bfont, Font.PLAIN, 20));
		btn3m.setMaximumSize(new Dimension(60,30));		 	
		btn3m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Clr!!!");
	        }
    	});
        c.gridy = 3;			    		
		JButton btn4m = new JButton("<html><center>Run</center></html>");	
		btn4m.setFont (new Font (bfont, Font.PLAIN, 20));
		btn4m.setMaximumSize(new Dimension(60,30));			 	
		btn4m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Run!!!");
	        }
    	});
        c.gridy = 4;								
		JButton btn5m = new JButton("<html><center>View</center></html>");	
		btn5m.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn5m.setMaximumSize(new Dimension(60,30));		
		btn5m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello View!!!");
	        }
    	});
        c.gridy = 5;				
		JButton btn6m = new JButton("<html><center>Help</center></html>");	
		btn6m.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn6m.setMaximumSize(new Dimension(60,30));
		btn6m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Help!!!");
	        }
    	});
        c.gridy = 6; 
		JButton btn7m = new JButton("<html><center>Find</center></html>");					
		btn7m.setFont (new Font (bfont, Font.PLAIN, 20));
		btn7m.setMaximumSize(new Dimension(60,30));
		btn7m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
    	        System.out.println("Hello Find!!!");
	        }
	    });			 		
        c.gridy = 7;
		JButton btn8m = new JButton("<html><center>Copy</center></html>");														    
		btn8m.setFont (new Font (bfont, Font.PLAIN, 20));
		btn8m.setMaximumSize(new Dimension(60,30));
		btn8m.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Copy!!!");
	        }
    	});
        c.gridy = 8;    	
	    JLabel label1 = new JLabel ("");
    	label1.setFont (new Font ("vt323", Font.PLAIN, 12));
	   	label1.setText("<html><br><br>[  ] Insert<br>Ctrl-X Delete<br>Ctrl-U Undo<br>Ctrl-L Clear<br>Ctrl-R Run<br>Ctrl-E Menu<br>Ctrl-H Help</html>");
   		c.insets = new Insets(1,1,1,1); // top left bottom right 

		Container menupane = new Container();
		menupane.setLayout(new BoxLayout(menupane, BoxLayout.Y_AXIS));
		btn1m.setAlignmentX(Component.LEFT_ALIGNMENT);        
		btn2m.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn3m.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn4m.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn5m.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn6m.setAlignmentX(Component.LEFT_ALIGNMENT);		
		btn7m.setAlignmentX(Component.LEFT_ALIGNMENT);		
		btn8m.setAlignmentX(Component.LEFT_ALIGNMENT);		
		// add everything    	
		menupane.add(btn1m,c);	
		menupane.add(btn2m,c);	
		menupane.add(btn3m,c);	
		menupane.add(btn4m,c);	
		menupane.add(btn5m,c);	
		menupane.add(btn6m,c);	
		menupane.add(btn7m,c);	
		menupane.add(btn8m,c);	
		c.weighty=1;
		c.gridheight = GridBagConstraints.REMAINDER;			
		menupane.add(label1,c);	
		jp_mainmenu.add(menupane);	
	}


	// =================== setup display of mainmenu button area =====================
	public void builddisplay_filemenubuttons() {
//	    content = getContentPane();
//	    content.setBackground(Color.white);
//		content.setLayout(new GridBagLayout());														
//		menutoshow = savedcontent.whichmenu;

  		GridBagConstraints c = new GridBagConstraints();    
		String s;
        c.gridx = 3;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 5;
        c.ipady = 2;
		c.weighty=0;        
		c.insets = new Insets(2,15,2,15);       // top left bottom right  
		String bfont="vt323";	

		JButton btn1f = new JButton("<html><center>Quit</center></html>");			
		btn1f.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn1f.setMaximumSize(new Dimension(60,30));
		btn1f.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				cardLayout.show(jp_menus, "MainMenu");        	
   	        	System.out.println("Hello Quit!!!");
	        }
    	});
		JButton btn2f = new JButton("<html><center>Load</center></html>");	
		btn2f.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn2f.setMaximumSize(new Dimension(60,30));
		btn2f.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Load!!!");
	        }
    	});
		JButton btn3f = new JButton("<html><center>Save</center></html>");	
		btn3f.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn3f.setMaximumSize(new Dimension(60,30));
		btn3f.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
	   	        	System.out.println("Hello Save!!!");
	        }
    	});
		JButton btn4f = new JButton("<html><center>Rena</center></html>");	
		btn4f.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn4f.setMaximumSize(new Dimension(60,30));
		btn4f.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Rename!!!");
	        }
    	});
		JButton btn5f = new JButton("<html><center>Del</center></html>");	
		btn5f.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn5f.setMaximumSize(new Dimension(60,30));
		btn5f.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Delete!!!");
	        }
    	});
		JButton btn6f = new JButton("<html><center>Print</center></html>");	
		btn6f.setFont (new Font (bfont, Font.PLAIN, 20)); 	
		btn6f.setMaximumSize(new Dimension(60,30));
		btn6f.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
   	        	System.out.println("Hello Print!!!");
	        }
    	});
	    JLabel label1 = new JLabel ("");
    	label1.setFont (new Font ("vt323", Font.PLAIN, 12));
	   	label1.setText("<html><br><br>[  ] Insert<br>Ctrl-X Delete<br>Ctrl-U Undo<br>Ctrl-L Clear<br>Ctrl-R Run<br>Ctrl-E Menu<br>Ctrl-H Help</html>");
   		c.insets = new Insets(1,1,1,1); // top left bottom right 

		Container filepane = new Container();
		filepane.setLayout(new BoxLayout(filepane, BoxLayout.Y_AXIS));
		btn1f.setAlignmentX(Component.LEFT_ALIGNMENT);        
		btn2f.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn3f.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn4f.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn5f.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn6f.setAlignmentX(Component.LEFT_ALIGNMENT);		
		// add everything    	
		filepane.add(btn1f,c);	
		filepane.add(btn2f,c);
		filepane.add(btn3f,c);
		filepane.add(btn4f,c);
		filepane.add(btn5f,c);
		filepane.add(btn6f,c);										
		c.weighty=1;        		
		filepane.add(label1,c);	
	    filepane.setPreferredSize(new Dimension(100, frameheight));
	    filepane.setMinimumSize(new Dimension(100, frameheight));
//		blah
		jp_filemenu.add(filepane);	    	
	}

    	
    public void componentResized(ComponentEvent arg0) {frameresize(); }    
    public void mouseMoved(MouseEvent e) {savemousepos(e);}   
//    public void paint(Graphics g) {frameresize("paint"); }

    public void savemousepos(MouseEvent e) {
    	mousex=e.getX();
    	mousey=e.getY();   
//   	 	System.out.println(mousey);	
    }
    
    public void frameresize() {
    	frameheight=content.getHeight()-40;

	    jp_input.setPreferredSize(new Dimension(140, frameheight));
	    jp_input.setMinimumSize(new Dimension(140, frameheight));

//	    codepane.setPreferredSize(new Dimension(framewidth/2+100, frameheight));
	    codepane.setMinimumSize(new Dimension(380, frameheight));

//	    langpane.setPreferredSize(new Dimension(framewidth/2+200, frameheight));
	    langpane.setMinimumSize(new Dimension(260, frameheight));

	    jp_filemenu.setPreferredSize(new Dimension(100, frameheight));
	    jp_filemenu.setMinimumSize(new Dimension(100, frameheight));
	   	frameheight+=20;
    }

    public void componentHidden(ComponentEvent e) {
    	executorService.shutdown();
         System.out.println("ComponentListener method called: componentHidden.");    
    }

    public void componentMoved(ComponentEvent e) {
         System.out.println("ComponentListener method called: componentMoved.");    
    }

//    public void componentResized(ComponentEvent arg0) {
//    	frameresize();         
//    	System.out.println("ComponentListener method called: componentResized."); 
//    } 

    public void componentShown(ComponentEvent e) {
         System.out.println("ComponentListener method called: componentShown.");    
    }  
      
    public void mouseDragged(MouseEvent me)  
     { 
         System.out.println("MouseMotionListener method called: mouseDragged.");         
     } 
//    public void mouseMoved(MouseEvent me)  
//     { 
//         System.out.println("MouseMotionListener method called: mouseMoved.");     
//     } 


    // inner class to handle mouse events
   public void mouseClicked(MouseEvent e)
   {
         System.out.println("MouseListener method called: MouseClicked.");   
      // handle mouse click event and determine which button was pressed
      int xPos = e.getX(); // get x position of mouse
      int yPos = e.getY(); // get y position of mouse
		
/*		
      details = String.format( "Clicked %d time(s)", 
        e.getClickCount() );
      
      if ( e.isMetaDown() ) // right mouse button   
        details += " with right mouse button";
      else if ( event.isAltDown() ) // middle mouse button
          details += " with center mouse button";
      else // left mouse button                       
        details += " with left mouse button";

       statusBar.setText( details ); // display message in statusBar
*/
   } 
	   
 	public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

	public static void main (String[] args) {		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
				    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch(Exception e){}
//scoobs
int r=128;
int g=64;
int b=64;
//				UIManager.put("nimbusBase", new Color(r,g,b));
//				UIManager.put("nimbusBlueGrey", new Color(r,g,b));
//				UIManager.put("control", new Color(r,g,b));

                Main_AGCS main = new Main_AGCS();
                main.setVisible(true);	    	                  
    			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
            }        
        });
	}

}

//		String[] lang_abbrev = new String[103];
//		int i1;
//		String ss;
//		JList listlang = new JList(lang);
/*		
  		for (int i=0; i<103; i++) { 
			i1 = lang[i].length();		
			if (i1>=25) {ss=lang[i].substring(0,25);} else {ss=lang[i];}
			lang_abbrev[i]=ss;
		}	
*/


//		addMouseMotionListener(this);	
/*
        ArrayList<Object> gradients = new ArrayList();
        gradients.add(0.3);
        gradients.add(0.0);
        gradients.add(new Color(221, 232, 243));
        gradients.add(new Color(255, 255, 255));
        gradients.add(new Color(184, 207, 229));
        javax.swing.UIManager.put("Button.background", Color.PINK);
        javax.swing.UIManager.put("Button.gradient", gradients);
        javax.swing.UIManager.put("Button.foreground", Color.PINK);
        SwingUtilities.updateComponentTreeUI(this);    
*/
/* 
// save to an image 
public static BufferedImage charToImage(char c, int width, int height)
{
    BufferedImage off_Image = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);

    Graphics2D g2 = off_Image.createGraphics();
    g2.setColor(Color.black);
    g2.setBackground(Color.WHITE);  
    g2.clearRect(0, 0, width, height);  
    g2.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
    g2.drawString(Character.toString(c), 0, height);
    saveImage(off_Image, "code" + (int)c);
    return off_Image;
}
 */

/*
// read from file
fr = new FileReader(ficheroEntrada);
BufferedReader rEntrada = new BufferedReader(fr, "UTF-8"));

String linea = rEntrada.readLine();
if (linea == null) {
logger.error("ERROR: Empty file.");
return null;
} 
String delimitador = "[;]";
String[] tokens = null;

List<String> token = new ArrayList<String>();
while ((linea = rEntrada.readLine()) != null) {
    // Some parsing specific to my file. 
    tokens = linea.split(delimitador);
    token.add(tokens[0]);
    token.add(tokens[1]);
}
logger.info("List of tokens: " + token);
return token;
 */ 
