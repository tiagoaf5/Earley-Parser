package main;

import edu.uci.ics.jung.graph.Graph;

public class MyTree {
	EarleyParser.Node root;
	static private int edgeid = 0;
	Graph<String, Integer> graph;
	public MyTree(EarleyParser.Node n,Graph<String, Integer> g,String rootName) {
		root = n;
		graph = g;
		root.text = rootName;
	}
	public void show() {
		showTree(root,0, null);
	}
	private void showTree(EarleyParser.Node root,int level, String previous) {//modificar para em vez de imprimir adicionar ao grafo
		System.out.print(level);
		for(int i = 0; i <= level; i++)
			System.out.print(" ");
		System.out.println(root.text);
		String cur = root.text;
		while(graph.containsVertex(cur)) {
			cur = cur + " ";
		}

		graph.addVertex(cur);
		if(previous != null) {
			graph.addEdge(edgeid++, previous, cur);
		}
		for(EarleyParser.Node sibling : root.siblings) {
			showTree(sibling, level+1, cur);
		}
	}
}