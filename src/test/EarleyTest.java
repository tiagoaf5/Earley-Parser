package test;

import static org.junit.Assert.*;
import main.*;

import org.junit.Test;

public class EarleyTest {

	@Test
	public void test1() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar1.txt");
		Lines lines=new Lines("./testsuite/sentences1.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test
	public void test2() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar1.txt");
		Lines lines=new Lines("./testsuite/sentences4.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertFalse(result);
	}
	
	@Test
	public void test3() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar5.txt");
		Lines lines=new Lines("./testsuite/sentences5.txt");
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
		Grammar g = new Grammar("./testsuite/grammar5.txt");
		Lines lines=new Lines("./testsuite/sentences5-fail.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertFalse(result);
	}
	
	@Test
	public void test_epsa() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar_epsa.txt");
		Lines lines=new Lines("./testsuite/sentences_epsa.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
		EarleyParser ep2 = new EarleyParser(lines.getLines().get(1), g);
		result = ep2.run();
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test
	public void test5() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar6.txt");
		Lines lines=new Lines("./testsuite/sentences6.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test
	public void test5_1() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar6-1.txt");
		Lines lines=new Lines("./testsuite/sentences6.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
		EarleyParser ep2 = new EarleyParser(lines.getLines().get(1), g);
		boolean result2 = ep2.run();
		System.out.println(result2);
		assertTrue(result2);
	}
	
	@Test
	public void test_star() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar_star.txt");
		Lines lines=new Lines("./testsuite/sentences_star.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test
	public void test_star2() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar_star2.txt");
		Lines lines=new Lines("./testsuite/sentences_star2.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test
	public void test_plus() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar_plus.txt");
		Lines lines=new Lines("./testsuite/sentences_plus.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
		
		ep = new EarleyParser(lines.getLines().get(1), g);
		result = ep.run();
		System.out.println(result);
		assertFalse(result);
	}
	
	@Test
	public void test_question() throws GrammarErrorException {
		Grammar g = new Grammar("./testsuite/grammar_question.txt");
		Lines lines=new Lines("./testsuite/sentences_question.txt");
		EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
		boolean result = ep.run();
		System.out.println(result);
		assertTrue(result);
		
		ep = new EarleyParser(lines.getLines().get(1), g);
		result = ep.run();
		System.out.println(result);
		assertTrue(result);
		
		ep = new EarleyParser(lines.getLines().get(2), g);
		result = ep.run();
		System.out.println(result);
		assertTrue(result);
		
		ep = new EarleyParser(lines.getLines().get(3), g);
		result = ep.run();
		System.out.println(result);
		assertFalse(result);
		
		
	}

}
