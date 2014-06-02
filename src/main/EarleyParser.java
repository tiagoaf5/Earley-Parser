package main;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import java.awt.geom.*;
import main.*;


import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;
import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;
import org.jgrapht.graph.DefaultEdge;

class State{
	int i; // pos na frase
	String left;
	int current; // pos na regra da gramatica
	ArrayList<String> right;
	ArrayList<State> parents; 
	static ListenableGraph<String, DefaultEdge> g;
	static JGraphModelAdapter<String, DefaultEdge> jgAdapter;

	State(String left, int current, ArrayList<String> right, int i)	{
		this.i = i;
		this.left = left;
		this.right = right;
		this.current = current;
		parents = new ArrayList<State>();
	}

	public void parents(int level, String previous) {//Print and visit parents
		StringBuilder ss = new StringBuilder();
		for(String word : right)
			ss.append(word);
		System.out.println(ss.toString());
		boolean bela = false;
		if(!g.addVertex(ss.toString())) {
			bela = true;
			g.addVertex(ss.toString()+" ");
		}
		positionVertexAt(ss.toString(), (int) Math.floor(Math.random()%600), 75 * level);
		if(previous != null){
			if(bela)
				g.addEdge(previous, ss.toString()+" ");
			else g.addEdge(previous, ss.toString());}
		for(State sparent : parents) {
			for(int i = 0; i <= level; i++)
				System.out.print("   ");
			if(bela)
				sparent.parents(++level, ss.toString() + " ");
			else
				sparent.parents(++level, ss.toString());
		}
	}

	@SuppressWarnings("unchecked") // FIXME hb 28-nov-05: See FIXME below
	private void positionVertexAt(Object vertex, int x, int y) {
		DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
		AttributeMap attr = cell.getAttributes();
		Rectangle2D bounds = GraphConstants.getBounds(attr);
		Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(), bounds.getHeight());
		GraphConstants.setBounds(attr, newBounds);
		AttributeMap cellAttr = new AttributeMap();
		cellAttr.put(cell, attr);
		jgAdapter.edit(cellAttr, null, null, null);
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

public class EarleyParser extends JApplet {
	private Sentence words;
	private HashMap<String,ArrayList<ArrayList<String>>> grammar;
	private String start;
	private ArrayList<ArrayList<State>> charts;

	private static final long serialVersionUID = 3256444702936019250L;
	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
	private static final Dimension DEFAULT_SIZE = new Dimension(800, 600);
	private JGraphModelAdapter<String, DefaultEdge> jgAdapter;
	private ListenableGraph<String, DefaultEdge> g;

	public EarleyParser(Sentence words, Grammar grammar) {
		this.words = words;
		this.grammar = grammar.getGrammar();
		this.start = grammar.getStartProduction();
		this.charts = new ArrayList<ArrayList<State>>(words.getSentence().size()+1);
		for(int i = 0; i < words.getSentence().size()+1; i++) {
			this.charts.add(new ArrayList<State>());
		}
	}

	public boolean run() {
		//INICIALIZACAO
		ArrayList<String> right_root = new ArrayList<String>(1);
		right_root.add(start);
		State begin = new State("_ROOT",0,right_root,0);
		addIfNotContains(0,begin);

		g = new ListenableDirectedMultigraph<String, DefaultEdge>(DefaultEdge.class);
		State.g=g;
		jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);
		State.jgAdapter = jgAdapter;

		JGraph jgraph = new JGraph(jgAdapter);

		adjustDisplaySettings(jgraph);
		getContentPane().add(jgraph);
		resize(DEFAULT_SIZE);

		for(int i = 0; i < words.getSentence().size()+1; i++) {
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

		//TREE
		State last_state = new State("_ROOT",1,right_root,0);
		ArrayList<State> array = charts.get(charts.size()-1);
		for(State s_root : array) 
		{
			if(s_root.equals(last_state))
				s_root.parents(0, null);
		}

		boolean r = charts.get(charts.size()-1).contains(last_state);
		return r;
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

	public static void main(String [] args) {
		//JGraphAdapterDemo applet = new JGraphAdapterDemo();
		//applet.init();

		Grammar g2;
		try {
			g2 = new Grammar("./ficheiros_teste/grammar1.txt");
			Lines lines=new Lines("./ficheiros_teste/sentences1.txt");
			EarleyParser ep = new EarleyParser(lines.getLines().get(0), g2);
			boolean result = ep.run();

			JFrame frame = new JFrame();
			frame.getContentPane().add(ep);
			frame.setTitle("JGraphT Adapter to JGraph Demo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		} catch (GrammarErrorException e) {
			e.printStackTrace();
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
			for(State parent : s.parents){//copy parents from duplicated state
				if(!newAdded.parents.contains(parent))
					newAdded.parents.add(parent);
			}
		} else if(B.equals(words.getSentence().get(j))) 
		{
			System.out.print("Scanner Action");
			State snew = new State(s.left,s.current+1,s.right,s.i);
			State newAdded = addIfNotContains(j+1,snew); //adds to next charts
			for(State parent : s.parents){//copy parents from duplicated state
				if(!newAdded.parents.contains(parent))
					newAdded.parents.add(parent);
			}
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
				newAdded.parents.add(s);
				for(State parent : currentState.parents){ //copy parents from duplicated state
					if(!newAdded.parents.contains(parent))
						newAdded.parents.add(parent);
				}
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

	private void adjustDisplaySettings(JGraph jg) {
		jg.setPreferredSize(DEFAULT_SIZE);
		Color c = DEFAULT_BG_COLOR;
		String colorStr = null;
		try {
			colorStr = getParameter("bgcolor");
		} catch (Exception e) {
		}
		if (colorStr != null) {
			c = Color.decode(colorStr);
		}
		jg.setBackground(c);
	}


	/**
	 * a listenable directed multigraph that allows loops and parallel edges.
	 */
	private static class ListenableDirectedMultigraph<V, E> extends DefaultListenableGraph<V, E> implements DirectedGraph<V, E> {
		private static final long serialVersionUID = 1L;
		ListenableDirectedMultigraph(Class<E> edgeClass) {
			super(new DirectedMultigraph<V, E>(edgeClass));
		}
	}
}