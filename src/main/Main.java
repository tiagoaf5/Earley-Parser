package main;

import java.util.ArrayList;

class MyTree
{
	EarleyParser.Node root;
	//adicionar aqui objectos do grafo
	public MyTree(EarleyParser.Node n) {
		root = n;
	}
	public void show()
	{
		showTree(root,0);
	}
	private void showTree(EarleyParser.Node root,int level) //modificar para em vez de imprimir adicionar ao grafo
	{
		System.out.print(level);
		for(int i = 0; i <= level; i++)
			System.out.print(" ");
		System.out.println(root.text);
		for(EarleyParser.Node sibling : root.siblings)
		{
			showTree(sibling,level+1);
		}
	}
}

public class Main {
	
	public static void main(String[] args)
	{
		Grammar g;
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
	}
	
	
	
}
