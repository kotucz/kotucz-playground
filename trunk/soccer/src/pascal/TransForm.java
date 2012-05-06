package pascal;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TransForm extends JFrame {

	TextArea pasArea;
	TextArea javaArea;
	TextArea outArea;

	JEditorPane htmlArea;

	TransForm() {
		setLayout(new BorderLayout());
		pasArea = new TextArea("click here to load *.pas file..", 20, 60);

		pasArea.addMouseListener(new MouseAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent arg0) {

				doAction();

				super.mouseClicked(arg0);
			}

		});


		javaArea = new TextArea("/*precompiled java source*/", 20, 60);


		outArea = new TextArea("load pas..", 5, 60);




		htmlArea = new JEditorPane();
//		htmlArea.setContentType("text/html");
		htmlArea.setContentType("text/plain");
//		htmlArea.setAutoscrolls(true);
		htmlArea.setPreferredSize(new Dimension(800, 5000));
		JScrollPane jsp1 = new JScrollPane(htmlArea);
		jsp1.setPreferredSize(new Dimension(1000, 1000));
//		pasArea.

//		htmlArea.setContentType("text/plain");

//		javaArea.setText(htmlArea.getText());





//		add(pasArea, BorderLayout.WEST);
//		add(javaArea, BorderLayout.CENTER);
		add(outArea, BorderLayout.SOUTH);
//		add(jsp1, BorderLayout.WEST);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp1, javaArea);
		splitPane.setDividerLocation(400);
//		splitPane.setContinuousLayout(true);
//		splitPane.setOneTouchExpandable(true);
		add(splitPane, BorderLayout.CENTER);

//		addWindowListener(new WindowAdapter() {

//		/* (non-Javadoc)
//		* @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
//		*/
//		@Override
//		public void windowClosing(WindowEvent arg0) {
//		// TODO Auto-generated method stub
//		arg0.getWindow().setVisible(false);
//		super.windowClosing(arg0);
//		System.exit(0);
//		}

//		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(800, 600);

//		pack();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TransForm frame = new TransForm();
		frame.setVisible(true);
		frame.doAction();
	}

	void doAction() {
		// TODO Auto-generated method stub

		if (openPas()) {

			try {
				htmlArea.setPage(pasFile.toURL());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			println("translating:");

			translate();

			println("compiling:");

			compileJava();

			println("COMPLETED");

		}
	}

	void setTokenizer(StreamTokenizer st) {
//		st.resetSyntax();

//		st.quoteChar('"'); 

//		st.ordinaryChar('_');
		st.ordinaryChar(':');
		st.ordinaryChar('/');
		st.ordinaryChar(';');

//		st.quoteChar(''); 
//		st.quoteChar('_');
		st.wordChars('a', 'z');
		st.wordChars('A', 'Z');
		st.wordChars('_', '_');
		st.wordChars('.', '.');
		st.wordChars('[', ']');
		st.wordChars('"', '"');
//		st.wordChars('1', '9');
//		st.parseNumbers();

//		st.eolIsSignificant(true); 

	}	

	File pasFile;
	String pasContent;
	File javaFile;

//	boolean started=true;
//	boolean skipbegin=false;
	String packageName = "player";
	String className="NewClass";
	String directory="/";
//	indention
	int ind;

	boolean openPas() {
		println("opening *.pas: ");
		FileDialog fdial = new FileDialog(this, "Open *.PAS file:", FileDialog.LOAD);			
//		fdial.set
		fdial.setVisible(true);
		pasFile = new File((directory = fdial.getDirectory())+fdial.getFile());
//		System.out.println("file"+pasFile);
		println("file: "+pasFile);
		if (pasFile!=null) return true; else return false;
	}

	void translate() {

		addSupertypeMethods();
		setTypesConversions();
		setWordConversions();

		FileReader fr = null;
		try {

			fr = new FileReader(pasFile);

			StreamTokenizer st = new StreamTokenizer(fr); 

			setTokenizer(st);

			pasArea.setText("");
			javaArea.setText("");


			ind = 0;

			/*while () {
				st.nextToken();
				pasArea.append(st.sval);
			}*/

			st.nextToken();
			if ("unit".equals(st.sval.toLowerCase())) {
				className = readName(st);

				printJava("package "+packageName+";\n");

				newLine();
				printJava("import soccer.*;\n");

				newLine();
				printJava("public class "+className+" extends PascalTeam {\n");
				pasArea.append("unit "+className+"\n");

//				skip to implementation
				while (!"implementation".equals(readName(st).toLowerCase()));
				pasArea.append("implementation\n");
				
				ind++;
				readBody(st);
				ind--;

				printJava("} // class end. \n");
			} else if ("program".equals(st.sval.toLowerCase())) {
				// TODO program ..
				println("not ready for PROGRAM transcription!! ");
				printJava("UNSUPPORTED FILE - pas program");
			} else println("line "+st.lineno()+": program or unit expected");



			javaArea.append(javaBuffer.toString());






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


	}


	String template = "template"; 

	java.util.List<String> methodNames = new ArrayList<String>();

	void addMethod(String name) {
		println("new method: "+name+"()");
		methodNames.add(name);
	}

	java.util.List<String> varNames = new ArrayList<String>();

	void addVar(String name, String type) {
//		printJava("var: "+type+" "+name+";\n");
//		pasArea.append("var: "+name+":"+type+";\n");
		println("variable: "+name+"("+type+")");
		varNames.add(name);
		if ("Ball".equals(type)) {
			varNames.add(name+".x");
			varNames.add(name+".y");
			varNames.add(name+".v_x");
			varNames.add(name+".v_y");
		}
		if ("Player".equals(type)) {
			varNames.add(name+".x");
			varNames.add(name+".y");
			varNames.add(name+".cil_x");
			varNames.add(name+".cil_y");
		}
	}


	String readName(StreamTokenizer st) throws IOException {
		if (st.nextToken()==StreamTokenizer.TT_WORD) return st.sval;
		println("line "+st.lineno()+": name or keyword expected "+(char)st.ttype+"("+st.ttype+") found");
		if (st.lineno()==47) new Exception().printStackTrace();
//		try {
//		Thread.sleep(50000);
//		} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}
		return "BADNAME char"+(char)st.ttype+"("+st.ttype+")";
	}


	void readBody(StreamTokenizer st) throws IOException {




//		String word = readName(st).toLowerCase();
////		while (!"end.".equals(word)) {
////		while (!"begin".equals(word)) {			
//		if ("var".equals(word)) {
//		variablesDefinition(st);
//		} else
//		if ("procedure".equals(word)||"function".equals(word)) 
//		defineMethod(st);
//		else;
//		word = readName(st).toLowerCase();
//		}

		String word = readName(st).toLowerCase();
		if ("var".equals(word)) {
			variablesDefinition(st);
			word = readName(st).toLowerCase();
		} 
		while
			("procedure".equals(word)||"function".equals(word)) { 
			defineMethod(st);
			word = readName(st).toLowerCase();
			println("SECRET WORD = "+word);
		} 


//		st.pushBack();
//		while (!"end".equals(word)) {

//		begin token		

		newLine();
		if ("begin".equals(word)) {
			ind++;
			while (readCommand(st));
			ind--;
		}
//		end token

		newLine();

//		word = readName(st).toLowerCase();
//		}
	}

	void compileJava() {
		File dir = new File(directory+packageName+"/");
		if (!dir.exists()) dir.mkdirs();

		File javaFile = new File(directory+packageName+"/"+className+".java");
		println("java file: "+javaFile);
		try {
			PrintWriter pw = new PrintWriter(javaFile);
			pw.print(javaArea.getText());
			pw.close();
			println("java saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		println("compiler: "+System.getProperty("java.compiler"));
		Compiler.enable();
		if (Compiler.compileClasses(className))
			println(className+" compiled");
		else  println(className+" compile FAILED ");

	}

	/**
	 * 	starts after <b>var</b> and ends before definition of <b>begin</b>, <b>function</b> or <b>procedure</b>
	 * @throws IOException 
	 */
	void variablesDefinition(StreamTokenizer st) throws IOException {

		printJava("\n/*variables*/\n");


		/*		
		while (true) {

				String varn = null;
				String vart = "not!";

				LinkedList<String> varns = new LinkedList<String>();

				varn = readName(st);

				if 	("begin".equals(varn.toLowerCase())
					||"procedure".equals(varn.toLowerCase())
					||"function".equals(varn.toLowerCase())
				) break;

				varns.add(varn);

				while (','==st.nextToken()) {
					varn = readName(st);
//					if ("var".equals(varn)) varn = readName(st);//TODO var - link/value
//					printJava(varn+",");
					varns.add(varn);
				} 


				if (':'==st.ttype) vart = readType(st);
				else printJava(" ':' expected "+(char)st.ttype+"("+st.ttype+") found");

				for (String vn:varns) {
					addVar(vn, vart);
//					if (!"".equals(params)) params+=", " ;
					printJava(vart+" "+vn+";");
					newLine();


				}
				varns = new LinkedList<String>();				




			}
		 */		

		String varName  = readName(st);

//		int j=1;
		
		while (true) {

			LinkedList<String> varns = new LinkedList<String>();
			
			String varType = "no_type";

//			addVar(varName, varType);
			varns.add(varName);
			
			while (','==st.nextToken()) {
				varName = readName(st);
//				addVar(v2, varType);
//				varName += ", "+v2;
				varns.add(varName);
			}
			println("token:"+st.ttype + (char)st.ttype);

			if (':'==st.ttype) {
				varType = readType(st);
			}
			 
			for (String n1 : varns) {
				String init;
				if ("int".equals(varType)) init = "0";
				else if ("double".equals(varType)) init = "0.0";
				else if ("String".equals(varType)) init = "\"\"";
				else if ("Player".equals(varType)) init = "new Player()";
				else if ("Ball".equals(varType)) init = "new Ball()";
				else init = "null";
				
				printJava(varType+" "+n1+" = "+init+";");
				addVar(n1, varType);
				pasArea.append("{var} "+varName+":"+varType+";");
				newLine();
			}
			
			if (';'==st.nextToken()) ; else printJava(" ';' expected");

			varName = readName(st);

			if 	("begin".equals(varName.toLowerCase())
					||"procedure".equals(varName.toLowerCase())
					||"function".equals(varName.toLowerCase())
			) break;

			varns = new LinkedList<String>();
			
		}		

		st.pushBack();
		printJava("\n/*end variables*/\n");





	}


	/**
	 * after procedure
	 */
	void defineMethod(StreamTokenizer st) {

		try {

			String methodName = readName(st);

			String returnType = "void";

			String params = "";

			if ('('==st.nextToken()) {

				while (true) {
//					while (')'!=st.nextToken()) {
//					if (st.ttype==StreamTokenizer.TT_WORD) headline+=st.sval;
//					else headline+=""+(char)st.ttype;

					String varn = null;
					String vart = "not!";

					LinkedList<String> varns = new LinkedList<String>();

					do {
						varn = readName(st);
						if ("var".equals(varn)) varn = readName(st);//TODO var - link/value
//						printJava(varn+",");
						varns.add(varn);
					} while (','==st.nextToken());


					if (':'==st.ttype) vart = readType(st);
					else printJava(" ':' expected "+(char)st.ttype+"("+st.ttype+") found");

					for (String vn:varns) {
						addVar(vn, vart);
						if (!"".equals(params)) params+=", " ;
						params+=vart+" "+vn;


					}
					varns = new LinkedList<String>();				


					if (';'!=st.nextToken()) 
						if (')'==st.ttype) break; 
						else printJava(" ')' or ';' expected ");
//					params+=", ";
				}

				if (':'==st.nextToken()) {
					returnType = readType(st);
//					printJava(" type "+returnType);
					if (';'!=st.nextToken()) printJava("';' expected "+(char)st.ttype+"("+st.ttype+") found");
				} else if (';'!=st.ttype) printJava("';' expected "+(char)st.ttype+"("+st.ttype+") found");

			} else printJava("'(' expected");
//			if (")".equals(""+(char)st.nextToken())) printJava(") {");


			freeLine();
			
			printJava("public "+returnType+" "+methodName+" ("+params+") {");
			pasArea.append("procedure "+methodName+"("+params+"):"+returnType+";");

			addMethod(methodName);

			ind++;

//			newLine();

			readBody(st);

			printJava("} // end method "+methodName);
			pasArea.append("end; {"+methodName+"}");

//			newLine();
			
			ind--;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * after procedure
	 * @throws IOException 
	 */
	void methodCall(StreamTokenizer st) throws IOException {


		String methodName = st.sval;

		String headline = "";

		int nestedBracket = 0;

		if (':'==st.nextToken()) {
			if ('='==st.nextToken()) {
//				returning result
				println("// RETURNING RESULT of "+methodName+"");
//				newLine();
				printJava("return "+readUntil(st, ';')+";");
			} else {
				println("ERROR line"+st.lineno()+": '=' expected");
//				st.pushBack();
//				st.pushBack();
			}
		} else 			
			if ('('==st.ttype) {
				println("// CALLING METHOD "+methodName+" WITH PARAMS ");

				while ((')'!=st.nextToken())||(nestedBracket>0)) {
					if (st.ttype==StreamTokenizer.TT_WORD) headline+=st.sval;
					else 
						if (st.ttype==StreamTokenizer.TT_NUMBER) headline+=number(st.nval);
						else {
							if (')'==st.ttype) nestedBracket--;
							if ('('==st.ttype) nestedBracket++;
							headline+=""+(char)st.ttype;
						}
				}

				if (';'!=st.nextToken()) printJava("';' expected "+(char)st.ttype+" found");

//				newLine();

				printJava(methodName+"("+headline+");");
				pasArea.append(methodName+"("+headline+");");

			} else if (';'==st.ttype){
				println("// CALLING METHOD "+methodName+" WITHOUT PARAMS ");
//				newLine();
				printJava(methodName+"();");
				pasArea.append(methodName+";");
//				newLine();
			} else 
				printJava("'(' expected");


		newLine();

	}


	boolean isKeyWord(String word) {
		int pos = " begin end end. case var if else then unit program uses implementation interface procedure function to do whilw repeat until for div mod nil of ".indexOf(" "+word+" "); 
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

	void newLine() {
//		printJava("\n");
		pasArea.append("\n");
		for (int i = 0; i<ind; i++) {
//			printJava("    ");
			pasArea.append("  ");
		}
	}

	String nl() {
		String nl = "\n";
		for (int i = 0; i<ind; i++) nl+="  ";
		return nl;
	}

	void addSupertypeMethods() {
		addMethod("jdi_na");
		addMethod("kopni_mic");
		addMethod("vzdalenost");
		addMethod("random");
	}

	/**
	 * called after if
	 * @param st
	 */
	void ifClause(StreamTokenizer st) throws IOException {

		String condition = "";

		condition = readUntil(st, "then");

		/*
		st.nextToken();
		while ((st.ttype!=StreamTokenizer.TT_WORD)||(!"then".equals(st.sval.toLowerCase()))) {

			if (st.ttype==StreamTokenizer.TT_WORD)
				condition+=st.sval;
			else
			if (st.ttype==StreamTokenizer.TT_NUMBER)
				condition+=st.nval;
			else
				condition+=""+(char)st.ttype;
			st.nextToken();

		}*/

		printJava("if ("+condition+") ");
		ind++;
		readCommand(st);
		ind--;
	}

	boolean readCommand(StreamTokenizer st) throws IOException {
		String word = readName(st);
//		println("read command");
		if ('{'==st.ttype) {
//			pascal comment {} -> /* */
			printJava("/* "+readUntil(st, '}')+" */");
//			newLine();
			return true;
		} else
			if (StreamTokenizer.TT_EOF==st.ttype) {
				println("UNEXPECTED EOF!!!!!!!!!!");
				return false;			
			} else
				if (StreamTokenizer.TT_EOL==st.ttype) {
//					printJava("\n"+st.lineno()+":");
					return true;			
				} else
					if (isMethod(word)) {
//						newLine();
						freeLine();
						methodCall(st);
//						newLine();
					} else 
						if (isVariable(word)) {
//							printJava("//VARIABLE "+word+":=?;");
//							newLine();
//							freeLine();
							if (':'==st.nextToken()&&'='==st.nextToken()) {
//								printJava(word+":=;");
								printJava(word+" = "+readUntil(st, ';')+";");
							} else printJava(" ':=' expected "+(char)st.ttype+" found");			
//							newLine();
						} else
							if (isKeyWord(word)) {
//								printJava("\n//KEYWORD "+word+"\n");
								if ("if".equals(word)) {
//									newLine();
									freeLine();
									ifClause(st);
									
								} else 
									if ("else".equals(word)) {
										printJava("else");
										ind++;
										readCommand(st);
										ind--;
//										newLine();
									} else 
										if ("for".equals(word)) {
											freeLine();
											forClause(st);
										} else 
											if ("begin".equals(word)) {
												printJava("{");
												ind++;
//												newLine();
												while (readCommand(st));
//												end token
												ind--;
												printJava("}");
//												newLine();
											} else 
												if ("repeat".equals(word)) {
													freeLine();
													printJava("do {");
													ind++;
//													newLine();
													while (readCommand(st));
//													until token
													ind--;
													String cause = readUntil(st, ';');
													printJava("} while (!("+cause+"));");
//													newLine();
												} else
												if ("until".equals(word)) {
													return false;
												} else 
												if ("case".equals(word)) {
													freeLine();
													caseClause(st);
												} else 
												if ("end".equals(word)||"end.".equals(word)) {
//													printJava("\n//END "+word);
//													newLine();
													if (';'!=st.nextToken()) st.pushBack();
													return false;
												} else printJava("// TODO "+word);


//								newLine();
							} else {
								printJava("//UNRECOGNIZED TOKEN : "+word);
								if (StreamTokenizer.TT_EOF==st.ttype) return false;
//								newLine();
							}

		if (';'!=st.ttype)
			if (';'!=st.nextToken()) {
				st.pushBack();
				printJava("// ';' expected ");
			}
		return true;
	}

	Map<String, String> typeMap = new HashMap<String, String>();

	String readType(StreamTokenizer st) throws IOException {
		String type = readName(st);
		String ntype = typeMap.get(type);
		if (ntype!=null) return ntype;
		return "unknown("+type+")";
	}

	void setTypesConversions() {
		typeMap.put("integer", "int");
		typeMap.put("Ttym", "Player[]");
		typeMap.put("Thrac", "Player");
		typeMap.put("Tmic", "Ball");
		typeMap.put("real", "double");
		typeMap.put("string", "String");
		typeMap.put("byte", "int");
		typeMap.put("boolean", "boolean");
	}

	/**
	 * if ("for".equals(st.sval)) {
	 * @param st
	 * @throws IOException 
	 */
	void forClause(StreamTokenizer st) throws IOException {
		String varName = readName(st);
		if (':'!=st.nextToken()||'='!=st.nextToken()) printJava("':=' expected "+(char)st.ttype+" found");
		String from = readUntil(st, "to");
		String to = readUntil(st, "do");
		printJava("for ("+varName+" = "+from+"; "+varName+"<="+to+"; "+varName+"++) ");
		pasArea.append("for "+varName+" := "+from+" to "+to+" do ");
		ind++;
		readCommand(st);
		ind--;
	}

	String readUntil(StreamTokenizer st, String terminator) throws IOException {
		String expression = "";

		while ((StreamTokenizer.TT_WORD!=st.nextToken())||(!terminator.equals(st.sval.toLowerCase()))) {

			if (st.ttype==StreamTokenizer.TT_WORD)
				expression+=word(st.sval);
			else
				if (st.ttype==StreamTokenizer.TT_NUMBER)
					expression+=number(st.nval);
				else 
					if (st.ttype>0) {
						expression+=chars(st);
//						expression+=""+(char)st.ttype;
					}



		}	
		return expression;
	}

	String readUntil(StreamTokenizer st, char terminator) throws IOException {
		String expression = "";
		while (terminator!=st.nextToken()) {

			if (st.ttype>0) {
				expression+=chars(st);
			} else			
				if (st.ttype==StreamTokenizer.TT_WORD) {
					expression+=word(st.sval);
				} else
					if (st.ttype==StreamTokenizer.TT_NUMBER) {
						expression+=number(st.nval);
					}

		}	
		return expression;
	}

	String number(double n) {
		if ((int)n==n) return ""+(int)n;
		else return ""+n;
	}

	Map<String, String> wordMap = new HashMap<String, String>();

	String word(String w1) {
		String word = wordMap.get(w1);
		if (word!=null) return word;
		return w1;
	}

	void setWordConversions() {
		wordMap.put("or", "||");
		wordMap.put("and", "&&");
		wordMap.put("not", "!");
		wordMap.put("begin", "{");
		wordMap.put("end", "}");

		wordMap.put("<>", "!=");
		wordMap.put(":=", "=");
		wordMap.put("=", "==");
		wordMap.put("{", "/*");
		wordMap.put("}", "*/");

	}

	/**
	 * call to conwert when nextToken returns > 0
	 * @param st 
	 * @return
	 * @throws IOException 
	 */
	String chars(StreamTokenizer st) throws IOException {
		switch (st.ttype) {
		case '{': return "/*";
		case '}': return "*/";
		case '=': return "==";
		case '<': 
			if ('>'==st.nextToken()) return "!="; else 
			if ('='==st.ttype) return "<="; else {
				st.pushBack();
			return "<";}
		case '>': 
			if ('='==st.nextToken()) return ">="; else {st.pushBack(); return ">";}
		default : return ""+(char)st.ttype+"";
		}

	}


	/**
	 * call after case kw
	 * @throws IOException 
	 *
	 */
	void caseClause(StreamTokenizer st) throws IOException {
		String caseExpression = readUntil(st, "of");
		printJava("switch ("+caseExpression+") {");
		ind++;
//		newLine();
		while (st.TT_NUMBER==st.nextToken()) {
			printJava("case "+number(st.nval)+":");
//			if (':'!=st.nextToken()) printJava("':' expected ");
			check(st, ':');
//			newLine();
//			printJava("comm");
			ind++;
			readCommand(st);
//			printJava("and");
//			newLine();
			ind--;
			printJava("break;");
			freeLine();
//			newLine();
		}
//		printJava("/*"+readUntil(st, "end")+"*/");
		ind--;
//		newLine();
		printJava("} // end switch ");
	}

	StringBuffer javaBuffer =  new StringBuffer();

	void printJava(String text) {
		javaBuffer.append(indents[ind]+text+"\n");
	}
		
	static String[] indents;
	
	static {
		indents = new String[50];
		for (int i = 0; i<indents.length; i++) {
			indents[i] = "";
			for (int j = 0; j<i; j++) {
				indents[i]+="  ";
			}
		}
	}
	
	public void freeLine() {
		printJava("");
	}
	
	boolean sameLine = false;
	
	boolean check(StreamTokenizer st, char next) throws IOException {
		if (next != st.nextToken()) {
			st.pushBack();
			println("ERROR: line "+st.lineno()+": '"+next+"' expected '"+(char)st.ttype+"'("+st.ttype+") found");
			return false;
		} else return true;
	}

//	void printlnJava(String text) {
//	printJava(text);
//	newLine();
//	}
	
//	void error(String text) {
//		st.lineno();
//	}
	

}
