package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/*class MyTree {
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

		String cur = root.text;
		while(Main.g1.containsVertex(cur)) {
			cur = cur + " ";
		}

		Main.g1.addVertex(cur);
		if(previous != null) {
			Main.g1.addEdge(edgeid++, previous, cur);
		}
		for(EarleyParser.Node sibling : root.siblings) {
			showTree(sibling, level+1, cur);
		}
	}
}
*/
public class Main {
	public static Graph<String, Integer> g1;
	public static int i = 0;
	
	public static void main(String[] args) {
		Grammar g;
		g1 = new DelegateForest<String, Integer>(new DirectedOrderedSparseMultigraph());

		
		ArrayList<MyTree> trees = new ArrayList<MyTree>();
		try {
			g = new Grammar("./ficheiros_teste/grammar1.txt");
			Lines lines=new Lines("./ficheiros_teste/sentences1.txt");
			EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
			boolean result = ep.run();
			System.out.println("Result: " + result);
			for(EarleyParser.Node node : ep.getTrees())	{
				trees.add(new MyTree(node));
			};
					
			for(MyTree tree : trees) {
				tree.show();
			}
		} catch (Exception e) {
			System.out.println("ERROR");
			System.out.println(e.getMessage());
		}
		
		Layout<String, String> layout = new TreeLayout((Forest) g1);
        //layout.setSize(new Dimension(300,300)); // sets the initial size of the layout space
        // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
        
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
		    public Paint transform(String s) {
		    	if(s.startsWith("\""))
		    		return Color.CYAN;
		    	else
		    		return Color.BLUE;
		    }
		};
		
        BasicVisualizationServer<String,String> vv = new BasicVisualizationServer<String,String>(layout);
        vv.setGraphLayout(layout);
        vv.setPreferredSize(new Dimension(800,600)); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		
		JFrame frame = new JFrame("Tree Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);  
	}
}
