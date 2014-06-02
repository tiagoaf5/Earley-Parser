package main;

import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import java.awt.Dimension;

import javax.swing.JFrame;

class MyTree {
	EarleyParser.Node root;
	//adicionar aqui objectos do grafo
	public MyTree(EarleyParser.Node n) {
		root = n;
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
		while(Main.g1.containsVertex(cur)) {
			cur = cur + " ";
		}
		//System.out.println(Main.i);
		//Main.i++;
		Main.g1.addVertex(cur);
		if(previous != null) {
			//System.out.println("RANDOM - " + (int) (Math.random()*100000000) + ", cur = " + cur + ", previous - " + previous);
			Main.g1.addEdge((int) (Math.random()*100000000), previous, cur);
		}
		for(EarleyParser.Node sibling : root.siblings) {
			showTree(sibling,level+1, cur);
		}
	}
}

public class Main {
	public static Graph<String, Integer> g1;
	public static int i = 0;
	
	public static void main(String[] args)
	{
		Grammar g;
		g1 = new DelegateForest<String, Integer>();

		
		ArrayList<MyTree> trees = new ArrayList<MyTree>();
		try {
			g = new Grammar("./ficheiros_teste/grammar1.txt");
			Lines lines=new Lines("./ficheiros_teste/sentences1.txt");
			EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
			boolean result = ep.run();
			System.out.println("Result: " + result);
			for(EarleyParser.Node node : ep.getTrees())
					{
						trees.add(new MyTree(node));
					};
					
			for(MyTree tree : trees)
			{
				tree.show();
			}
		} catch (Exception e) {
			System.out.println("ERROR");
		}
		
		Layout<Integer, String> layout = new TreeLayout((Forest) g1);
        //layout.setSize(new Dimension(300,300)); // sets the initial size of the layout space
        // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
        
        BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        vv.setGraphLayout(layout);
        vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		
		JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);  
	}
}
