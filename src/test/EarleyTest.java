package test;

import static org.junit.Assert.*;
import main.*;

import org.junit.Test;

public class EarleyTest {

	@Test
	public void test1() throws GrammarErrorException {
		Grammar g = new Grammar("./ficheiros_teste/grammar1.txt");
		Lines lines=new Lines("./ficheiros_teste/sentences1.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test
	public void test2() throws GrammarErrorException {
		Grammar g = new Grammar("./ficheiros_teste/grammar1.txt");
		Lines lines=new Lines("./ficheiros_teste/sentences4.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertFalse(result);
	}
	
	@Test
	public void test3() throws GrammarErrorException {
		Grammar g = new Grammar("./ficheiros_teste/grammar5.txt");
		Lines lines=new Lines("./ficheiros_teste/sentences5.txt");
		for(Sentence sentence : lines.getLines())
		{
			EarleyParser ep = new EarleyParser(sentence, g);
			boolean result = ep.run();
			System.out.println(result);
			assertTrue(result);
		}
	}
	
	@Test
	public void test4() throws GrammarErrorException {
		Grammar g = new Grammar("./ficheiros_teste/grammar5.txt");
		Lines lines=new Lines("./ficheiros_teste/sentences5-fail.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertFalse(result);
	}

}
