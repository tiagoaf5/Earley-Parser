package main;

import java.util.ArrayList;
import java.util.HashMap;

public class EarleyParser {

	class State{
		int i; // pos na frase
		String left;
		int current; // pos na regra da gramatica
		ArrayList<String> right;
		//State parent = null;
		
		State(String left, int current, ArrayList<String> right, int i)
		{
			this.i = i;
			this.left = left;
			this.right = right;
			this.current = current;
		}
		
		public String toString()
		{
			String out = left + "->";
			for(int k = 0; k < right.size(); k++)
			{
				if(k==current)
					out += "@";
				out += right.get(k);	
			}
			if(right.size()==current)
				out += "@";
			
			return "("+out+","+i+")";
		}
		
		public boolean equals(Object obj) {
	        if(obj instanceof State)
	        {
	        	State s2 = (State)obj;
	        	if(i != s2.i)
	        		return false;
	        	if(current != s2.current)
	        		return false;
	        	if(!left.equals(s2.left))
	        		return false;
	        	if(right.size()!=s2.right.size())
	        		return false;
	        	for(int k = 0; k < right.size(); k++)
	        		if(!right.get(k).equals(s2.right.get(k)))
	        			return false;
	        	return true;
	        }
	        return false;
	    }

	}
	
	private Sentence words;
	private HashMap<String,ArrayList<ArrayList<String>>> grammar;
	private String start;
	private ArrayList<ArrayList<State>> charts;
	
	public EarleyParser(Sentence words, Grammar grammar) {
		this.words = words;
		this.grammar = grammar.getGrammar();
		this.start = grammar.getStartProduction();
		this.charts = new ArrayList<ArrayList<State>>(words.getSentence().size()+1);
		for(int i = 0; i < words.getSentence().size()+1; i++)
		{
			this.charts.add(new ArrayList<State>());
		}
	}

	public boolean run()
	{
		
		//INICIALIZACAO
		ArrayList<String> right_root = new ArrayList<String>(1);
		right_root.add(start);
		addIfNotContains(0,new State("_ROOT",0,right_root,0));
		
		for(int i = 0; i < words.getSentence().size()+1; i++)
		{
			System.out.println("\nWord no "+i);
			if(i < words.getSentence().size())
				System.out.println(words.getSentence().get(i));
			
			if(charts.get(i).isEmpty())
			{
				System.out.println("Nothing to do for this word");
				return false;
			}
			
			for(int snum = 0; snum < charts.get(i).size();snum++)
			{
				State s = charts.get(i).get(snum);
				System.out.println("state to process " + s);
				if(s.current==s.right.size()) // end of rule
				{
					System.out.println("Completer");
					completer(s,i);
				} else
				{
					if(s.right.get(s.current).startsWith("\""))
					{
						System.out.println("Scanner");
						scanner(s,i);
					}
					else
					{
						System.out.println("Predictor");
						predictor(s,i);
					}
				}
			}
		}
		
		State last_state = new State("_ROOT",1,right_root,0);
		ArrayList<State> lastchart = charts.get(charts.size()-1);
		return lastchart.contains(last_state);
		
	}

	private void predictor(State s, int j) {
		String B = s.right.get(s.current);
		ArrayList<ArrayList<String>> rules = grammar.get(B);
		for(ArrayList<String> rule : rules)
		{
			System.out.print("Predictor Action");
			State snew = new State(B,0,rule,j);
			addIfNotContains(j,snew);
		}
	}

	private void scanner(State s, int j) {
		if(j+1 >= charts.size())
			return;
		String B = s.right.get(s.current);
		if(B.startsWith("\""))
			B = B.substring(1, B.length()-1); //TODO change when all tokens have ""
		if(B.equals(words.getSentence().get(j)))
		{
			System.out.print("Scanner Action");
			State snew = new State(s.left,s.current+1,s.right,s.i);
			addIfNotContains(j+1,snew);
		}
	}

	private void completer(State s, int k) {
		for(int snum = 0; snum < charts.get(s.i).size(); snum++)
		{
			State currentState = charts.get(s.i).get(snum);
			if(currentState.current >= currentState.right.size())
				continue;
			if(s.left.equals(currentState.right.get(currentState.current)))
			{
			//	System.out.println(currentState);
				System.out.print("Completer Action");
				State newState = new State(currentState.left,currentState.current+1,currentState.right,currentState.i);
				addIfNotContains(k,newState);
			}
		}
	}
	
	private void addIfNotContains(int num, State s)
	{
		ArrayList<State> list = charts.get(num);
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).equals(s))
			{
				System.out.println("  NOT added " + s + " to chart " + num);
				return;
			}
		}
		System.out.println("  Added " + s + " to chart " + num);
		list.add(s);
	}

}