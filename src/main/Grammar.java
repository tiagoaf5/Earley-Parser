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

	public static void main(String[] args) {
		Grammar p = new Grammar(args[0]);
		try {
			p.read_file();
			p.semanticAnalysis();
		} catch (GrammarErrorException e) {
			System.err.println(e.getMessage());
		}
	}


	public Grammar(String path) {
		filePath = path;
	}





	private void read_file() throws GrammarErrorException {

		File f = new File(filePath);

		if (!f.exists())
			throw new GrammarErrorException("File doesn't exist!");

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();

			while (line != null) {
				System.out.println("LINE - " + line);

				if (line.matches("[A-Za-z0-9]+ ::= (.*)")) {

					String head = line.substring(0,line.indexOf("::=") - 1);
					String body = line.substring(line.indexOf("::=") + 3);
					
					productions.add(head); //add head to productions list

					if (grammar.containsKey(head)) {
						ArrayList<ArrayList<String>> bodies = grammar.get(head);

						String[] tmp2 = body.split(RE_SPLIT_PIPES);

						for (String i : tmp2) {
							ArrayList<String> tmp = splitBySpace(i, true);
							
							//add non-terminals to productions list
							for(String j: tmp){
								if(j.charAt(0) != '\"')
									productions.add(j);
							}
							
							bodies.add(tmp);	
						}

					} else {
						ArrayList<ArrayList<String>> bodies = new ArrayList<ArrayList<String>>();
						grammar.put(head, bodies);

						String[] tmp2 = body.split(RE_SPLIT_PIPES);

						for (String i : tmp2) {
							ArrayList<String> tmp = splitBySpace(i, true);

							//add non-terminals to productions list
							for(String j: tmp){
								if(j.charAt(0) != '\"')
									productions.add(j);
							}
							
							bodies.add(tmp);	
						}
					}
				} 
				else {
					String abc ="Invalid grammar! Doesn't follow:\n Non-Terminal ::= body";
					throw new GrammarErrorException(abc);
				}
					

				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Tokens - " + grammar);
		System.out.println("Non-Terminals - " + productions);
	}

	void semanticAnalysis() throws GrammarErrorException {
		for(String x : productions)
			if(!grammar.containsKey(x))
				throw new GrammarErrorException("Production \"" + x + "\" doesn't have a body");
	}
	
	ArrayList<String> splitBySpace(String subjectString, boolean textInsideInverted) {

		/*ArrayList<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile(RE_SPLIT_SPACES);
		Matcher regexMatcher = regex.matcher(subjectString);

		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				// Add double-quoted string without the quotes 
				if(textInsideInverted)
					matchList.add(regexMatcher.group(1));
			} else if (regexMatcher.group(2) != null ) {
				// Add single-quoted string without the quotes
				if(textInsideInverted)
					matchList.add(regexMatcher.group(2));
			} else {
				// Add unquoted word
				matchList.add(regexMatcher.group());
			}

		} */
		//http://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double
		
		ArrayList<String> matchList = new ArrayList<String>();
		
		Pattern regex = Pattern.compile(RE_SPLIT_SPACES);
		Matcher regexMatcher = regex.matcher(subjectString);
		
		while (regexMatcher.find()) {
			matchList.add(regexMatcher.group().trim());
		} 

		return matchList;
	}



}
