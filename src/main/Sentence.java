package main;
import java.util.ArrayList;

public class Sentence {

	private ArrayList<String> tokens=new ArrayList<String>();
	
	public Sentence(String[] splits) {
		
		for(int i=0; i<splits.length; i++) {
			tokens.add(splits[i]);
		}
	}
	
	public ArrayList<String> getSentence() {
		return tokens;
	}

}
