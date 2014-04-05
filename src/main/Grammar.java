package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Grammar {
	String filePath;
	HashMap<String, ArrayList<String>> tokens = new HashMap<String, ArrayList<String>>();
	ArrayList<String> productions = new ArrayList<String>();


	public Grammar(String path) {
		filePath = path;
	}



	private void read_file() throws FileError {
		
		File f = new File(filePath);
		
		if (!f.exists())
			throw new FileError("File doesn't exist!");
		
		/*while (true) {
			System.out.println("Please enter file path.");
			String line = s.nextLine();
			f = new File(line);
			
			if (f.exists())
				break;
			else
				System.err.println("File does not exist.");
		}*/

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();

			while (line != null) {
				System.out.println("LINE - " + line);
				//System.out.println("! - " + line.matches("[A-Z]+ ::= (.*)"));
				String[] st = line.split(" ");
				for (int i = 0; i < st.length; i++) {
					if(st[i].matches("[A-Z]+") && !productions.contains(st[i])) {
						productions.add(st[i]);
					}
				}

				if (line.matches("[A-Z]+ ::= (.*)")) {
					String s1 = line.substring(0,line.indexOf("::=") - 1);
					String s2 = line.substring(line.indexOf("::=") + 3);
					if (tokens.containsKey(s1)) {
						ArrayList<String> tmp = tokens.get(s1);
						String[] tmp2 = s2.split("\\|");
						for (int i = 0; i < tmp2.length; i++) {
							tmp.add(tmp2[i].trim());
						}
					} else {
						//ArrayList<String> a = new ArrayList<String>(Arrays.asList(s2.split("\\|")));
						//System.out.println("S2 - " + s2);
						//System.out.println("SIZE ARRAYLIST - " + s2.split("\\|").length);
						String[] split = s2.split("\\|");
						for (int i = 0; i < split.length; i++) {
							split[i] = split[i].trim();
						}
						tokens.put(s1, new ArrayList<String>(Arrays.asList(split)));
					}
				} else {
					System.err.println("Invalid grammar");
					System.exit(-1);
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Tokens - " + tokens);
		System.out.println("Non-Terminals - " + productions);
	}

	public static void main(String[] args) {
		Grammar p = new Grammar(args[0]);
		try {
			p.read_file();
		} catch (FileError e) {
			System.err.println(e.getMessage());
		}
	}

}
