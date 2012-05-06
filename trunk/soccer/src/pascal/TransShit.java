package pascal;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class TransShit extends Frame {

	TextArea pasArea;
	TextArea javaArea;
	TextArea outArea;
	
	TransShit() {
		setLayout(new BorderLayout());
		pasArea = new TextArea("click here to load *.pas file..", 20, 60);
		pasArea.addMouseListener(new MouseAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
				openPas();
				super.mouseClicked(arg0);
			}
			
		});
		add(pasArea, BorderLayout.WEST);
		
		javaArea = new TextArea("/*precompiled java source*/", 20, 60);
		add(javaArea, BorderLayout.CENTER);
		
		outArea = new TextArea("load pas..", 5, 60);
		add(outArea, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				arg0.getWindow().setVisible(false);
				super.windowClosing(arg0);
			}
			
		});
		setSize(640, 480);
//		pack();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TransShit().setVisible(true);
	}
	
	File pasFile;
	String pasContent;
	File javaFile;
	
	boolean started=true;
	boolean skipbegin=false;
	String className="NewClass";
	int ind;
	
	void openPas() {
		println("opening *.pas:");
		FileDialog fdial = new FileDialog(this, "Open *.PAS file:", FileDialog.LOAD);			
//		fdial.set
		fdial.setVisible(true);
		pasFile = new File(fdial.getDirectory()+fdial.getFile());
//		System.out.println("file"+pasFile);
		println("file"+pasFile);
			
		FileReader fr = null;
		try {
			
			fr = new FileReader(pasFile);
			
			StreamTokenizer st = new StreamTokenizer(fr); 
			
//			st.resetSyntax();
			
//			st.ordinaryChar('_');
			st.ordinaryChar(':');
//			st.ordinaryChar('.');
			st.ordinaryChar(';');
			
//			st.quoteChar(''); 
//			st.quoteChar('_');
			st.wordChars('a', 'Z');
			st.wordChars('_', '_');
			st.wordChars('.', '.');
			st.wordChars('[', ']');
//			st.wordChars('1', '9');
//			st.parseNumbers();
			st.eolIsSignificant(true); 

			pasArea.setText("");
			javaArea.setText("");
	
			
			ind = 1;
			
			/*while () {
				st.nextToken();
				pasArea.append(st.sval);
			}*/
			
			while (st.nextToken()!=StreamTokenizer.TT_EOF) {
			switch (st.ttype) {
			case StreamTokenizer.TT_EOL:
				pasArea.append("\n");
				if (started) {
					javaArea.append("\n");
					javaArea.append(""+st.lineno()+": ");
				}
				for (int i = 0; i<ind; i++) {
					pasArea.append("  ");
					javaArea.append("  ");
				}
				break;
			case StreamTokenizer.TT_WORD:	
				
				
			if ("interface".equals(st.sval)) {
				started = false;
				pasArea.append("interface");
				break;
			} else 
			if ("implementation".equals(st.sval)) {
				started = true;
				pasArea.append("implementation");
				break;
			} else if (("unit".equals(st.sval.toLowerCase()))
					||("program".equals(st.sval.toLowerCase()))) {
				className = readName(st);
//				String className = "CLAAS";
				
				
				javaArea.append("public class ["+className+"] {");
				pasArea.append(st.sval+" "+className+";");
				ind++;
			} else				
		    if (started) {
				if ("var".equals(st.sval)) {
					defineVars(st);
				} else if ("procedure".equals(st.sval)) {
					defineMethod(st);
					
					
				} else if ("if".equals(st.sval)) {
					javaArea.append("if (");
					pasArea.append("if "+st.sval);
				} else if ("begin".equals(st.sval)) {
					if (!skipbegin) {
							ind++;
							javaArea.append(" { ");
					}
					pasArea.append(" begin ");
				} else if ("for".equals(st.sval)) {
					String varName = readName(st);
					String from = ""+0;
					String to = ""+5;
					javaArea.append(" for (["+varName+"] = "+from+"; "+varName+"<="+to+"; "+varName+"++) ");
					pasArea.append(" for "+varName+" := "+from+" to "+to+" do ");
				} else if ("end".equals(st.sval)) {
					ind--;
					javaArea.append(" } ");
					pasArea.append(" end ");
				} else if (isVariable(st.sval)) {
					javaArea.append("V>"+st.sval+"<");
				} else {
					javaArea.append("?"+st.sval+"? ");
					pasArea.append("?"+st.sval+"? ");
				}
			
			}
				
				
			
				
				
				
						
			break;
			case StreamTokenizer.TT_NUMBER:	
				javaArea.append("(int)"+(int)st.nval+"");
				pasArea.append("NUMBER>"+st.nval+"<");
			break;
			default:
				pasArea.append(""+(char)st.ttype);
				if (started) javaArea.append(""+(char)st.ttype);
			break;
			}
			
			}
												
			pasArea.append("DONE");
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		compileJava();
	}
	
	String template = "template"; 
	
	java.util.List<String> methodNames = new ArrayList<String>();
	
	void addMethod(String name) {
		println("new method: "+name+"()");
		methodNames.add(name);
	}
	
	java.util.List<String> varNames = new ArrayList<String>();
	
	void addVar(String name, String type) {
		javaArea.append("var: "+type+" "+name+";\n");
		pasArea.append("var: "+name+":"+type+";\n");
		println("new variable: "+name+"("+type+")");
		varNames.add(name);
	}
	
	String readName(StreamTokenizer st) {
		String name = "";
		try {
			while (st.nextToken()!=StreamTokenizer.TT_EOF) {
//			println(""+st.ttype);
			switch (st.ttype) {
			case StreamTokenizer.TT_EOL:
				st.pushBack();
				return name;
			case StreamTokenizer.TT_WORD:
				if (isKeyWord(name)) ; else 
				name+="+"+st.sval;
			break;
			case StreamTokenizer.TT_NUMBER:
				name+="+"+(int)st.nval;
			break;
			default:
//				if ("_".indexOf((char)st.ttype, 0)>-1) name+="+"+(char)st.ttype;
//				else {
					st.pushBack();
					return name; 
//				}
//			break;
			}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
		
	}

	String readExpresion(StreamTokenizer st) {
		String name = " ";
		try {
			while (st.nextToken()!=StreamTokenizer.TT_EOF) {
			println(""+st.ttype);
			switch (st.ttype) {
			case StreamTokenizer.TT_EOL:
				st.pushBack();
				return name;
			case StreamTokenizer.TT_WORD:
				name+=st.sval;
			break;
			case StreamTokenizer.TT_NUMBER:
				name+=""+st.nval;
			break;
			default:
//				if ("_".indexOf((char)st.ttype, 0)>-1) name+=(char)st.ttype;
//				else {
				// not necesary since set word chars (_)
					st.pushBack();
					return name; 
//				}
//			break;
			}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
		
	}
	
	
	void compileJava() {
		File javaFile = new File("/"+className+".java");
		println("java file: "+javaFile);
		try {
			PrintWriter pw = new PrintWriter(javaFile);
			pw.print(javaArea.getText());
			pw.close();
			println("java saved ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 	start with var and ends with definition of begin function or procedure
	 */
	void defineVars(StreamTokenizer st) {
		
	  javaArea.append("\n/*variables*/\n");
	  try {
	  end26: while (
			  !(("begin".equals(st.sval))
				||("procedure".equals(st.sval))
				||("function".equals(st.sval)))
			  &&(!(")".equals(""+(char)st.ttype)))	
		  ) {

		    
		  
			String varName  = readName(st);
				
			while (",".equals(""+(char)st.nextToken())) {
				varName += ", "+readName(st);
			}
			
			String varType = "!NO_TYPE!";
			println("token:"+st.ttype + (char)st.ttype);
			if (":".equals(""+(char)st.ttype)) {
				varType = readName(st);
			}
//			if (";".equals(""+(char)st.nextToken())) {			
//				addVar(varName, varType);
//			} else javaArea.append(" <;> expected!!! found:"+st.ttype + (char)st.ttype);
			addVar(varName, varType);
			javaArea.append(" ("+varType+")-["+varName+"] ");
			pasArea.append("var "+varName+":"+varType);
						
/*			if (st.nextToken()==StreamTokenizer.TT_WORD) {
				if (("begin".equals(st.sval))
					||("procedure".equals(st.sval))
					||("function".equals(st.sval)))
				{	
					st.pushBack();
					break;
				}
			}*/
			
/*			if (st.ttype>=0) {
				if (";".equals(""+st.ttype)) continue;
				st.pushBack();
				break;
			}*/
			
			
		    while (st.nextToken()>0) {
		    	if (")".equals(""+(char)st.ttype)) {
		    		break end26;
		    	}
		    	javaArea.append("["+(char)st.ttype+st.sval+"]");
		    }
//			st.nextToken();
		}
		st.pushBack();
		javaArea.append("\n/*end variables*/\n");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
							
		
	}

	/*
	 * after procedure
	 */
	void defineMethod(StreamTokenizer st) {
		
		try {
			
			String methodName = readName(st);
			addMethod(methodName);
			javaArea.append("public void "+methodName+"");
			pasArea.append("procedure "+methodName+";");
			if ("(".equals(""+(char)st.nextToken())) {
				
				String headline = "";
				while (!(")".equals(""+(char)st.nextToken()))) {
					if (st.ttype==StreamTokenizer.TT_WORD) headline+=st.sval;
					else headline+=""+(char)st.ttype;
				}
				javaArea.append("( HEADLINE>"+headline+" ) { ");
				
				
//				defineVars(st);
				
			}
//			if (")".equals(""+(char)st.nextToken())) javaArea.append(") {");
			skipbegin = true;
			ind++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		st.nextToken();
//		if ("var".equals(st.sval)) defineVars(st);
		
//		  javaArea.append("\n/*variables*/\n");
//		  try {
//		  end26: while (
//				  !(("begin".equals(st.sval))
//					||("procedure".equals(st.sval))
//					||("function".equals(st.sval)))
//				  &&(!(")".equals(""+(char)st.ttype)))	
//			  ) {
//
//			    
//			  
//				String varName  = readName(st);
//					
//				while (",".equals(""+(char)st.nextToken())) {
//					varName += ", "+readName(st);
//				}
//				
//				String varType = "!NO_TYPE!";
//				println("token:"+st.ttype + (char)st.ttype);
//				if (":".equals(""+(char)st.ttype)) {
//					varType = readName(st);
//				}
////				if (";".equals(""+(char)st.nextToken())) {			
////					addVar(varName, varType);
////				} else javaArea.append(" <;> expected!!! found:"+st.ttype + (char)st.ttype);
//				addVar(varName, varType);
//				javaArea.append(" ("+varType+")-["+varName+"] ");
//				pasArea.append("var "+varName+":"+varType);
//							
//	/*			if (st.nextToken()==StreamTokenizer.TT_WORD) {
//					if (("begin".equals(st.sval))
//						||("procedure".equals(st.sval))
//						||("function".equals(st.sval)))
//					{	
//						st.pushBack();
//						break;
//					}
//				}*/
//				
//	/*			if (st.ttype>=0) {
//					if (";".equals(""+st.ttype)) continue;
//					st.pushBack();
//					break;
//				}*/
//				
//				
//			    while (st.nextToken()>0) {
//			    	if (")".equals(""+(char)st.ttype)) {
//			    		break end26;
//			    	}
//			    	javaArea.append("["+(char)st.ttype+st.sval+"]");
//			    }
////				st.nextToken();
//			}
//			st.pushBack();
//			javaArea.append("\n/*end variables*/\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			
								
			
	}
	
	boolean isKeyWord(String word) {
		int pos = " begin end var if then unit program uses implementation interface procedure function  ".indexOf("  "+word+" "); 
		return (pos>-1)?true:false;
	}
	
	boolean isVariable(String var) {
		return varNames.contains(var);
	}
	
	boolean isMethod(String var) {
		return methodNames.contains(var);
	}
	
	void println(String s) {
		outArea.append(s+"\n");
	}
	
}
 