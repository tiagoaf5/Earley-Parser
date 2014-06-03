package main;

import gui.Window;

public class MyTree {
	EarleyParser.Node root;
	private int edgeid;
	//adicionar aqui objectos do grafo
	public MyTree(EarleyParser.Node n) {
		root = n;
	}
	public void show() {
		showTree(root,0, null);
	}
	private void showTree(EarleyParser.Node root,int level, String previous) {//modificar para em vez de imprimir adicionar ao grafo
		/*System.out.print(level);
		for(int i = 0; i <= level; i++)
			System.out.print(" ");
		System.out.println(root.text);*/
		String cur = root.text;
		while(Window.g1.containsVertex(cur)) {
			cur = cur + " ";
		}

		Window.g1.addVertex(cur);
		if(previous != null) {
			Window.g1.addEdge(edgeid++, previous, cur);
		}
		for(EarleyParser.Node sibling : root.siblings) {
			showTree(sibling, level+1, cur);
		}
	}
}