package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import main.Grammar;
import main.GrammarErrorException;

import org.junit.Test;

public class GrammarTest {

	@SuppressWarnings("serial")
	@Test
	public void testGrammar1() throws GrammarErrorException {
		
		Grammar a = new Grammar("./ficheiros_teste/grammar1.1.txt");
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

}
