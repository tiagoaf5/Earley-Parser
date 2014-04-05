package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {

	final String RE_SPLIT_SPACES = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
	final String RE_SPLIT_PIPES = "\\|(?![^\"]*\"(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

	String filePath;

	HashMap<String, ArrayList<ArrayList<String>>> grammar = new HashMap<String, ArrayList<ArrayList<String>>>();
	ArrayList<String> productions = new ArrayList<String>();

	public static void main(String[] args) {
		Grammar p = new Grammar(args[0]);
		try {
			p.read_file();
		} catch (GrammarError e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}


	public Grammar(String path) {
		filePath = path;
	}





	private void read_file() throws GrammarError {

		File f = new File(filePath);

		if (!f.exists())
			throw new GrammarError("File doesn't exist!");

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();

			while (line != null) {
				System.out.println("LINE - " + line);
				//System.out.println("! - " + line.matches("[A-Z]+ ::= (.*)"));
				//String[] st = line.split(" ");

				//Getting productions
				ArrayList<String> st =  splitBySpace(line, false);

				for(String i : st)
					if(i.matches("[A-Z]+") && !productions.contains(i)) {
						grammar.put(i, new ArrayList<ArrayList<String>>());
						productions.add(i);
					}

				
				if (line.matches("[A-Z]+ ::= (.*)")) {
					
					String head = line.substring(0,line.indexOf("::=") - 1);
					String body = line.substring(line.indexOf("::=") + 3);
					
					if (grammar.containsKey(head)) {
						ArrayList<ArrayList<String>> bodies = grammar.get(head);
						
						
						String[] tmp2 = body.split(RE_SPLIT_PIPES);
						
						for (String i : tmp2) {
							ArrayList<String> tmp = splitBySpace(i, true);
							
							bodies.add(tmp);	
						}
						
					} else {
						/*
						String[] split = body.split("\\|");
						for (int i = 0; i < split.length; i++) {
							split[i] = split[i].trim();
						}
						tokens.put(head, new ArrayList<String>(Arrays.asList(split)));*/
						throw new GrammarError("Error line:\n" + line);
					}
				} 
				else 
					throw new GrammarError("Invalid grammar! Doesn't follow:\n Non-Terminal ::= body");

				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Tokens - " + grammar);
		System.out.println("Non-Terminals - " + productions);
	}

	ArrayList<String> splitBySpace(String subjectString, boolean textInsideInverted) {

		ArrayList<String> matchList = new ArrayList<String>();
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
		} 
		return matchList;
	}



}
