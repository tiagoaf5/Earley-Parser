package main;

import java.util.ArrayList;


public class Main {

	public static void main(String[] args)
	{
		Grammar g;
		try {
			g = new Grammar("./ficheiros_teste/grammar1.txt");
			Lines lines=new Lines("./ficheiros_teste/sentences1.txt");
			EarleyParser ep = new EarleyParser(lines.getLines().get(0), g);
			boolean result = ep.run();
			System.out.println("Result: " + result);
			ArrayList<EarleyParser.Node> roots =  ep.getTrees();
			for(EarleyParser.Node root : roots)
			{
				showTree(root,0); //o level e so para os espacos, nao e necessario
			}
		} catch (Exception e) {
			System.out.println("ERROR");
		}
	}
	
	private static void showTree(EarleyParser.Node root,int level) //modificar para em vez de imprimir adicionar ao grafo
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
