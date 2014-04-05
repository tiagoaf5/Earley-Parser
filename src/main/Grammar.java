package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {

	final String RE_SPLIT_SPACES = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
	final String RE_SPLIT_PIPES = "\\|(?![^\"]*\"(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

	String filePath;

	HashMap<String, ArrayList<ArrayList<String>>> grammar = new HashMap<String, ArrayList<ArrayList<String>>>();
	LinkedHashSet<String> productions = new LinkedHashSet<String>();

	String startProduction;

	public static void main(String[] args) {
		try {
			new Grammar(args[0]);
		} catch (GrammarErrorException e) {
			System.err.println(e.getMessage());
		}
	}


	public Grammar(String path) throws GrammarErrorException {
		filePath = path;
		readFile();
		semanticAnalysis();
	}
	
	public Grammar() {

	}

	public void readFile() throws GrammarErrorException {

		File f = new File(filePath);

		if (!f.exists())
			throw new GrammarErrorException("File doesn't exist!");

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
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
					String abc ="Invalid grammar! Doesn't follow:\n Non-Terminal ::= body";
					throw new GrammarErrorException(abc);
				}


				line = br.readLine();
				cont++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\nGrammar - " + grammar);
		System.out.println("Non-Terminals - " + productions);
		System.out.println("StartProduction - " + startProduction);
	}


	private void parseBody(String body, ArrayList<ArrayList<String>> bodies) throws GrammarErrorException {
		String[] tmp2 = body.split(RE_SPLIT_PIPES);

		for (String i : tmp2) {
			ArrayList<String> tmp = splitBySpace(i, true);

			//add non-terminals to productions list
			for(String j: tmp){
				if(j.charAt(0) != '\"') {
					
					if(!j.matches("[A-Za-z][A-Za-z0-9]*"))
						throw new GrammarErrorException("Invalid production name: \"" + j + "\"");
					productions.add(j);
				}
			}

			bodies.add(tmp);	
		}
	}

	public void semanticAnalysis() throws GrammarErrorException {
		for(String x : productions)
			if(!grammar.containsKey(x))
				throw new GrammarErrorException("Production \"" + x + "\" doesn't have a body");
	}

	ArrayList<String> splitBySpace(String subjectString, boolean textInsideInverted) {
		//http://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double

		ArrayList<String> matchList = new ArrayList<String>();

		Pattern regex = Pattern.compile(RE_SPLIT_SPACES);
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
