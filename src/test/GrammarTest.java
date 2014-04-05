package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import main.Grammar;
import main.GrammarErrorException;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void testGrammar1() throws GrammarErrorException {
		
		Grammar a = new Grammar("./ficheiros_teste/grammar1.1.txt");
		LinkedHashSet<String> productions = a.getProductions();
		HashMap<String, ArrayList<ArrayList<String>>> grammar = a.getGrammar();
		
		//start production
		assertEquals(a.getStartProduction(), "P");
		
		
		//Productions
		ArrayList<String> p = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{
			add("P");
			add("S");
			add("M");
			add("T");
		}};
		
		for(String i: p)
			assertTrue(productions.contains(i));
		
		/*HashMap<String, String> test = new HashMap<String, String>(){{
			    	 put("a","b");
			    }
		};*/
	}

}
