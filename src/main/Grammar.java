package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {

	/*
	 * Sites expressoes regulares
	 * 
	 * http://www.regexr.com/
	 * http://www.regexplanet.com/advanced/java/index.html
	 * 
	 */

	final String GR_SEPARATOR = "::=";

	final String RE_SPLIT_SPACES = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
	final String RE_SPLIT_SPACES2 =  	"[^\\s\\\"'()]+|\\\"([^\\\"]*)\\\"|'([^']*)'|\\(([^\\)]*)\\)*\\*"; //nova com parentesis [^\s\"'()]+|\"([^\"]*)\"|'([^']*)'|\(([^\)]*)\)*\*
	final String RE_SPLIT_PIPES = "\\|(?![^\"]*\"(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
	final String RE_SPLIT_PARENTHESES = "\\(([^\\)]*)\\)*(\\*|\\+|\\?)"; // \(([^\)]*)\)*\*

	String filePath;

	HashMap<String, ArrayList<ArrayList<String>>> grammar = new HashMap<String, ArrayList<ArrayList<String>>>();
	LinkedHashSet<String> productions = new LinkedHashSet<String>();

	String startProduction;

	private int production_index = 1;

	public Grammar(String path) throws GrammarErrorException {
		filePath = path;
		readFile();
		semanticAnalysis();
	}

	public Grammar(String text, boolean test) throws GrammarErrorException {
		readString(text);
		semanticAnalysis();
	}
	
	public Grammar() {
	}

	public void readFile() throws GrammarErrorException {

		File f = new File(filePath);

		if (!f.exists())
			throw new GrammarErrorException("File doesn't exist!");

		try {
			reader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new GrammarErrorException("File doesn't exist!");
		}
	}
	
	public void readString(String x) throws GrammarErrorException {
		reader(new StringReader(x));
	}


	private void reader(Reader in) throws GrammarErrorException {
		try (@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(in)) {
			String line = br.readLine();

			int cont = 0;

			while (line != null) {

				System.out.println("LINE - " + line);

				if (line.matches("[A-Za-z][A-Za-z0-9]* ::= (.*)")) { //match Rule: production ::= body

					String head = line.substring(0,line.indexOf("::=") - 1);
					String body = line.substring(line.indexOf("::=") + 3);

					if(cont == 0)
						startProduction = head;

					productions.add(head); //add head to productions list

					if (grammar.containsKey(head)) {
						ArrayList<ArrayList<String>> bodies = grammar.get(head);
						parseBody(body, bodies);

					} else {
						ArrayList<ArrayList<String>> bodies = new ArrayList<ArrayList<String>>();
						grammar.put(head, bodies);
						parseBody(body, bodies);
					}
				} 
				else {
					String abc ="Invalid grammar! Line: \""+ line + "\" doesn't follow:\n Non-Terminal ::= body";
					throw new GrammarErrorException(abc);
				}


				line = br.readLine();
				cont++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		
		System.out.println("\nGrammar - " + grammar);
		System.out.println("Non-Terminals - " + productions);
		System.out.println("StartProduction - " + startProduction);
	}


	private void parseBody(String body, ArrayList<ArrayList<String>> bodies) throws GrammarErrorException {
		String[] tmp2 = body.split(RE_SPLIT_PIPES);


		for (String i : tmp2) {
			System.out.println("-> " + i);


			/*ArrayList<String> parentheses = splitSpecial(i, RE_SPLIT_PARENTHESES);
			System.out.println("---> " + parentheses);
			 */

			/*   ----------------------------------------------------     */

			ArrayList<String> parentheses = new ArrayList<String>(); //TODO: delete this

			Pattern regex = Pattern.compile(RE_SPLIT_PARENTHESES);
			Matcher regexMatcher = regex.matcher(i);

			StringBuffer sb = new StringBuffer();
			
			while (regexMatcher.find()) 
			{
				
				String matched = regexMatcher.group().trim();
				String production = "#" + production_index;
				String rule_body = null;

				if(matched.charAt(matched.length() - 1) == '*') {
					rule_body = matched.substring(1, matched.length() - 2) + " " + production 
							+/* " | " +  matched.substring(1, matched.length() - 2) + */" | \"\"";
				}
				else if(matched.charAt(matched.length() - 1) == '+') {
					rule_body = matched.substring(1, matched.length() - 2) + " " + production 
							+ " | " +  matched.substring(1, matched.length() - 2);
				}
				else if(matched.charAt(matched.length() - 1) == '?') {
					rule_body = matched.substring(1, matched.length() - 2) + " | \"\"";
				}
				
				//parse this new rule
				ArrayList<ArrayList<String>> b = new ArrayList<ArrayList<String>>();
				grammar.put(production, b);
				parseBody(rule_body, b);

				parentheses.add(matched); 
				String replacement = production;
				regexMatcher.appendReplacement(sb, replacement);
				production_index++;
			} 

			regexMatcher.appendTail(sb);

			System.out.println(sb.toString());

			/*   ----------------------------------------------------     */

			ArrayList<String> tmp = splitSpecial(sb.toString(), RE_SPLIT_SPACES);
			System.out.println(tmp);

			//add non-terminals to productions list
			for(String j: tmp) {
				if(j.charAt(0) != '\"') {

					if(!j.matches("[A-Za-z][A-Za-z0-9]*|#[0-9]*"))
						throw new GrammarErrorException("Invalid production name: \'" + j + "\' in body: \'" + body + "\'");
					productions.add(j);
				} /*else if (j.charAt(0) == '(' && j.charAt(j.length()-1) == '*') {
					//Caso de ser ("ab" C)* 

					//New production body
					String inside = j.substring(1, j.length() - 3) + " " + "#" + production_index;



					//create entry on grammar
					grammar.put("#" + production_index, new ArrayList<ArrayList<String>>());
					grammar.get("#" + production_index).add(new ArrayList<String>() {{add("");}});

					//add to the productions list (avoid semantic errors)
					productions.add("#" + production_index);

					j = "#" + production_index;

					production_index++;



					//TODO: keep going you're doing Ok
				}*/
			}

			bodies.add(tmp);	
		}
	}

	public void semanticAnalysis() throws GrammarErrorException {
		for(String x : productions)
			if(!grammar.containsKey(x))
				throw new GrammarErrorException("Production \'" + x + "\' doesn't have a body");
	}

	ArrayList<String> splitSpecial(String subjectString, String re) {
		//http://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double

		ArrayList<String> matchList = new ArrayList<String>();

		Pattern regex = Pattern.compile(re); //RE_SPLIT_SPACES
		Matcher regexMatcher = regex.matcher(subjectString);

		while (regexMatcher.find()) {
			matchList.add(regexMatcher.group().trim());
		} 

		return matchList;
	}




	/**
	 * @return the grammar
	 */
	public HashMap<String, ArrayList<ArrayList<String>>> getGrammar() {
		return grammar;
	}


	/**
	 * @return the productions
	 */
	public LinkedHashSet<String> getProductions() {
		return productions;
	}


	/**
	 * @return the startProduction
	 */
	public String getStartProduction() {
		return startProduction;
	}


	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}


	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}




}
