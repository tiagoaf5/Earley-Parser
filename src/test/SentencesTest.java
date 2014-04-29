package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import main.Lines;
import org.junit.Test;

public class SentencesTest {

	@SuppressWarnings("serial")
	@Test
	public void testSentences1() {
		Lines lines=new Lines("ficheiros_teste/sentences1.txt");
		ArrayList<String> expected = new ArrayList<String> () {{
			add("\"2\"");
			add("\"+\"");
			add("\"3\"");
			add("\"*\"");
			add("\"4\"");
		}};
		ArrayList<String> actual =new ArrayList<String> ();
		actual=lines.getNextLine();
		assertEquals(expected, actual);
		assertEquals(null, lines.getNextLine());
	}

	@SuppressWarnings("serial")
	@Test
	public void testSentences2() {
		Lines lines=new Lines("ficheiros_teste/sentences2.txt");
		ArrayList<String> expected = new ArrayList<String> () {{
			add("\"id\"");
			add("\"+\"");
			add("\"id\"");
			add("\"+\"");
			add("\"id\"");
		}};
		ArrayList<String> actual=lines.getNextLine();
		assertEquals(expected, actual);
		expected = new ArrayList<String> () {{
			add("\"id\"");
		}};
		actual=lines.getNextLine();
		assertEquals(expected, actual);
		assertEquals(null, lines.getNextLine());
	}

	@SuppressWarnings("serial")
	@Test
	public void testSentences3() {
		Lines lines=new Lines("ficheiros_teste/sentences3.txt");
		ArrayList<String> expected = new ArrayList<String> () {{
			add("\"bc\"");
		}};
		ArrayList<String> actual =new ArrayList<String> ();
		actual=lines.getNextLine();
		assertEquals(expected, actual);
		assertEquals(null, lines.getNextLine());
	}
	
	/*private void compareArrayString(ArrayList<String> expected, ArrayList<String> actual) {
		
		for(int i=0; i<expected.size(); i++) {
			if(i<actual.size()) {
				assertEquals(expected.get(i), actual.get(i));
			} else {
				fail();
			}
		}
	}*/
}
