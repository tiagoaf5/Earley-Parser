package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import main.Grammar;
import main.GrammarErrorException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GrammarTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();


	@SuppressWarnings("serial")
	@Test
	public void testGrammar1() throws GrammarErrorException {

		Grammar a = new Grammar("./ficheiros_teste/grammar1.txt");
		LinkedHashSet<String> productions = a.getProductions();
		HashMap<String, ArrayList<ArrayList<String>>> grammar = a.getGrammar();

		//test start production
		assertEquals(a.getStartProduction(), "P");


		//test Productions
		ArrayList<String> p = new ArrayList<String>() {{
			add("P");
			add("S");
			add("M");
			add("T");
		}};

		for(String i: p)
			assertTrue(productions.contains(i));

		//test grammar
		HashMap<String, ArrayList<ArrayList<String>>> g = new HashMap<String, ArrayList<ArrayList<String>>>() {{
			put("P", new ArrayList<ArrayList<String>>() {{ //P ::= S
				add(new ArrayList<String>() {{ //S 
					add("S");
				}});
			}});

			put("S", new ArrayList<ArrayList<String>>() {{ //S ::= S "+" M | M
				add(new ArrayList<String>() {{ //S "+" M
					add("S");
					add("\"+\"");
					add("M");
				}});
				add(new ArrayList<String>() {{ //M
					add("M");
				}});
			}});

			put("M", new ArrayList<ArrayList<String>>() {{ //M ::= M "*" T | T
				add(new ArrayList<String>() {{ //M "*" T
					add("M");
					add("\"*\"");
					add("T");
				}});
				add(new ArrayList<String>() {{ //T
					add("T");
				}});
			}}); 

			put("T", new ArrayList<ArrayList<String>>() {{ //T ::= "1" | "2" | "3" | "4"
				add(new ArrayList<String>() {{ //1
					add("\"1\"");
				}});
				add(new ArrayList<String>() {{ //2 
					add("\"2\"");
				}});
				add(new ArrayList<String>() {{ //3
					add("\"3\"");
				}});
				add(new ArrayList<String>() {{ //4 
					add("\"4\"");
				}});
			}});
		}};

		assertTrue(grammar.equals(g));
	}

	@SuppressWarnings("serial")
	@Test
	public void testGrammar2() throws GrammarErrorException {

		Grammar a = new Grammar("./ficheiros_teste/grammar2.txt");
		LinkedHashSet<String> productions = a.getProductions();
		HashMap<String, ArrayList<ArrayList<String>>> grammar = a.getGrammar();

		//test start production
		assertEquals(a.getStartProduction(), "P");


		//test Productions
		ArrayList<String> p = new ArrayList<String>() {{
			add("P");
			add("S");
			add("M");
			add("T");
		}};

		assertTrue(p.size() == productions.size());

		for(String i: productions)
			assertTrue(p.contains(i));


		//test grammar
		HashMap<String, ArrayList<ArrayList<String>>> g = new HashMap<String, ArrayList<ArrayList<String>>>() {{
			put("P", new ArrayList<ArrayList<String>>() {{ //P ::= S
				add(new ArrayList<String>() {{ //S 
					add("S");
				}});
			}});

			put("S", new ArrayList<ArrayList<String>>() {{ //S ::= S "+" M | M
				add(new ArrayList<String>() {{ //S "+" M
					add("S");
					add("\"+\"");
					add("M");
				}});
				add(new ArrayList<String>() {{ //M
					add("M");
				}});
			}});

			put("M", new ArrayList<ArrayList<String>>() {{ //M ::= M "*" T | T
				add(new ArrayList<String>() {{ //M "*" T
					add("M");
					add("\"*\"");
					add("T");
				}});
				add(new ArrayList<String>() {{ //T
					add("T");
				}});
			}}); 

			put("T", new ArrayList<ArrayList<String>>() {{ //T ::= "1" | "2" | "3" | "4"
				add(new ArrayList<String>() {{ //1
					add("\"1\"");
				}});
				add(new ArrayList<String>() {{ //2 
					add("\"2\"");
				}});
				add(new ArrayList<String>() {{ //3
					add("\"3\"");
				}});
				add(new ArrayList<String>() {{ //4 
					add("\"4\"");
				}});
			}});
		}};

		assertTrue(grammar.equals(g));
	}

	@SuppressWarnings("serial")
	@Test
	public void testGrammar3() throws GrammarErrorException {
		Grammar a = new Grammar();
		a.setFilePath("./ficheiros_teste/grammar3.txt");

		a.readFile();
		LinkedHashSet<String> productions = a.getProductions();

		//test start production
		assertEquals(a.getStartProduction(), "E");
		HashMap<String, ArrayList<ArrayList<String>>> grammar = a.getGrammar();


		//test Productions
		ArrayList<String> p = new ArrayList<String>() {{
			add("E");
			add("T");
			add("id");
		}};

		System.out.println(p);
		assertTrue(p.size() == productions.size());

		for(String i: productions)
			assertTrue(p.contains(i));



		//test grammar
		HashMap<String, ArrayList<ArrayList<String>>> g = new HashMap<String, ArrayList<ArrayList<String>>>() {{
			put("E", new ArrayList<ArrayList<String>>() {{ //E ::= T "+" id | id
				add(new ArrayList<String>() {{ //T "+" id 
					add("T");
					add("\"+\"");
					add("id");
				}});
				add(new ArrayList<String>() {{ //id 
					add("id");
				}});
			}});

			put("T", new ArrayList<ArrayList<String>>() {{ //T ::= E
				add(new ArrayList<String>() {{ //T "+" id 
					add("E");
				}});
			}});
		}};

		assertEquals(g, grammar);

		exception.expect(GrammarErrorException.class);
		a.semanticAnalysis();
		
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testGrammar4() throws GrammarErrorException {
		Grammar a = new Grammar();
		a.setFilePath("./ficheiros_teste/grammar4.txt");

		a.readFile();
		LinkedHashSet<String> productions = a.getProductions();

		//test start production
		assertEquals(a.getStartProduction(), "A");
		HashMap<String, ArrayList<ArrayList<String>>> grammar = a.getGrammar();


		//test Productions
		ArrayList<String> p = new ArrayList<String>() {{
			add("A");
			add("B");
			add("C");//TODO:
			add("b");
			//add("c");
		}};

		System.out.println(p);
		assertTrue(p.size() == productions.size());

		for(String i: productions)
			assertTrue(p.contains(i));



		//test grammar
		HashMap<String, ArrayList<ArrayList<String>>> g = new HashMap<String, ArrayList<ArrayList<String>>>() {{
			put("A", new ArrayList<ArrayList<String>>() {{ //A ::= BC
				add(new ArrayList<String>() {{ //BC
					add("B");
					add("C");
				}});
			}});

			put("B", new ArrayList<ArrayList<String>>() {{ //B ::= b
				add(new ArrayList<String>() {{ //b
					add("b");
				}});
			}});
			
			put("C", new ArrayList<ArrayList<String>>() {{ //C ::= c
				add(new ArrayList<String>() {{ //c 
					add("c");
				}});
			}});
		}};

		assertEquals(g, grammar);

		exception.expect(GrammarErrorException.class);
		a.semanticAnalysis();
		
	}


}
