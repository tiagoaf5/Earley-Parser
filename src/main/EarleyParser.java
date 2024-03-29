package main;

import java.util.ArrayList;
import java.util.HashMap;

public class EarleyParser {
	
	public static class Node{
		String text;
		ArrayList<Node> siblings = new ArrayList<Node>();
		
		Node(String s)
		{
			text=s;
		}
	}

	class State{
		class Mypair{ //need this to keep the order
			String key;
			ArrayList<State> values;
			Mypair(String key, ArrayList<State> values)
			{
				this.key = key;
				this.values = values;
			}
		}
		
		int i; // position in the sentence
		String left;
		int current; // position in the grammar rule
		ArrayList<String> right;
		ArrayList<Mypair> parents; // each right has parents 

		State(String left, int current, ArrayList<String> right, int i)
		{
			this.i = i;
			this.left = left;
			this.right = right;
			this.current = current;
			parents = new ArrayList<Mypair>();
			for(String r : right)
			{
				parents.add(new Mypair(r,new ArrayList<State>()));
			}
		}

		public void parents(Node node_parent) //visit parents
		{
			for(Mypair pair : parents)
			{
				Node son = new Node(pair.key);
				for(State sparent : pair.values)
				{
					sparent.parents(son);
				}
				node_parent.siblings.add(son);
			}
			
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
	private ArrayList<Node> trees;

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
	
	public ArrayList<Node> getTrees()
	{
		return trees;
	}

	public int run()
	{

		//INICIALIZACAO
		ArrayList<String> right_root = new ArrayList<String>(1);
		right_root.add(start);
		State begin = new State("_ROOT",0,right_root,0);
		addIfNotContains(0,begin);

		for(int i = 0; i < words.getSentence().size()+1; i++)
		{
			System.out.println("\nWord no "+i);
			if(i < words.getSentence().size())
				System.out.println(words.getSentence().get(i));

			if(charts.get(i).isEmpty())
			{
				System.out.println("Nothing to do for this word");
				return i+1;
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

		//TREE
		State last_state = new State("_ROOT",1,right_root,0);
		ArrayList<State> array = charts.get(charts.size()-1);
		trees = new ArrayList<Node>();
		for(State s_root : array) 
		{
			if(s_root.equals(last_state))
			{
				Node root = new Node("_ROOT");
				s_root.parents(root);
				trees.add(root);
			}
		}

		boolean r = charts.get(charts.size()-1).contains(last_state);
		if(r)
			return 0;
		else return -1;
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
		String B = s.right.get(s.current);
		boolean epsilon = B.equals("\"\"");

		if(j > words.getSentence().size())
			return;

		if(j == words.getSentence().size() && !epsilon)//only empty strings can be scanned in last chart
			return;

		if(epsilon)
		{
			System.out.print("Scanner Action epsilon");
			State snew = new State(s.left,s.current+1,s.right,s.i);
			State newAdded = addIfNotContains(j,snew); //adds to current chart
			
			copyParents(s, newAdded);
		} else if(B.equals(words.getSentence().get(j))) 
		{
			System.out.print("Scanner Action");
			State snew = new State(s.left,s.current+1,s.right,s.i);
			State newAdded = addIfNotContains(j+1,snew); //adds to next charts
			
			//copy parents from duplicated state
			copyParents(s, newAdded);
			
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
				System.out.print("Completer Action");
				State newState = new State(currentState.left,currentState.current+1,currentState.right,currentState.i);
				State newAdded = addIfNotContains(k,newState);
				//newAdded.parents.add(s);
				if(newState==newAdded) //only if it's not a new state, it has parents
					newAdded.parents.get(currentState.current).values.add(s);
				copyParents(currentState, newAdded);
			}
		}
	}


	private State addIfNotContains(int num, State s)
	{
		ArrayList<State> list = charts.get(num);
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).equals(s))
			{
				System.out.println("  NOT added " + s + " to chart " + num);
				return list.get(i);
			}
		}
		System.out.println("  Added " + s + " to chart " + num);
		list.add(s);
		return s;
	}
	
	private void copyParents(State s, State newAdded) {
		
		for(int i = 0; i < s.parents.size(); i++) //both states have the same number of right
		{
			for(State value : s.parents.get(i).values)
			{
				if(!newAdded.parents.get(i).values.contains(value))
					newAdded.parents.get(i).values.add(value);
			}
		}
	}

}