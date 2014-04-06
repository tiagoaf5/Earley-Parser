package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EarleyParser {

	class State{
		int i; // pos na frase
		String left;
		int current; // pos na regra da gramatica
		ArrayList<String> right;
		
		State(String left, int current, ArrayList<String> right, int i)
		{
			this.i = i;
			this.left = left;
			this.right = right;
			this.current = current;
		}

	}
	
	private Sentence words;
	private HashMap<String,ArrayList<ArrayList<String>>> grammar;
	private ArrayList<ConcurrentLinkedQueue<State>> charts;
	
	public EarleyParser(Sentence words, HashMap<String,ArrayList<ArrayList<String>>> grammar) {
		this.words = words;
		this.grammar = grammar;
		this.charts = new ArrayList<ConcurrentLinkedQueue<State>>(words.getSentence().size());
	}

	public boolean run()
	{
		//INICIALIZACAO
		ArrayList<String> right_root = new ArrayList<String>(1);
		right_root.add("S");
		charts.get(0).add(new State("_ROOT",0,right_root,0));
		
		for(int i = 0; i < words.getSentence().size(); i++)
		{
			Iterator<State> it = charts.get(i).iterator();
			while(it.hasNext())
			{
				State s = it.next();
				//if(words.getSentence().get(i).startsWith("\"") || (s.current==s.right.size() && !words.getSentence().get(i).startsWith("\"")))
				if(s.current==s.right.size()) // end of rule
				{
					//complete
					completer(s,i);
				} else
				{
					if(s.right.get(s.current).startsWith("\""))
					{
						//scanner
						scanner(s,i);
					}
					else
					{
						//predictor
						predictor(s,i);
					}
				}
			}
		}
		return false;
	}

	private void predictor(State s, int j) {
		String B = s.right.get(s.current);
		ArrayList<ArrayList<String>> rules = grammar.get(B);
		for(ArrayList<String> rule : rules)
		{
			charts.get(j).add(new State(B,0,rule,j));
		}
	}

	private void scanner(State s, int j) { // ?????
		String B = s.right.get(s.current);
		if(B.equals(words.getSentence().get(j)))
		{
			charts.get(j+1).add(new State(s.left,s.current+1,s.right,s.i));
		}
	}

	private void completer(State s, int k) {
		Iterator<State> it = charts.get(s.i).iterator();
		while(it.hasNext())
		{
			State currentState = it.next();
			if(s.left.equals(currentState.right.get(currentState.current)))
			{
				State newState = new State(currentState.left,currentState.current+1,currentState.right,currentState.i);
				charts.get(k).add(newState);
			}
		}
	}

}