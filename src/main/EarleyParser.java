package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class EarleyParser {

	class State{
		int i;
		int left;
		int current;
		ArrayList<Integer> right;

		State(int left, int current, ArrayList<Integer> right, int i)
		{

		}

	}

	private ArrayList<String> words;
	private HashMap<Integer,ArrayList<ArrayList<Integer>>> grammar;
	private ConcurrentLinkedQueue<State> queue;

	public EarleyParser(ArrayList<Integer> words, HashMap<Integer,ArrayList<ArrayList<Integer>>> grammar) {
		this.words = words;
		this.grammar = grammar;
	}

	public boolean run()
	{
		ArrayList<Integer> first = new ArrayList<Integer>(1);
		queue.add(new State(0,0,first,0)); // ???

		for(int i = 0; i < words.size(); i++)
		{
			//for(in)
		}


		return false;
	}

}
/*
	function EARLEY-PARSE(words, grammar)
    ENQUEUE((Î³ â†’ â€¢S, 0), chart[0])
    for i â†� from 0 to LENGTH(words) do
        for each state in chart[i] do
            if INCOMPLETE?(state) then
                if NEXT-CAT(state) is a nonterminal then
                    PREDICTOR(state, i, grammar)         // non-terminal
                else do
                    SCANNER(state, i)                    // terminal
            else do
                COMPLETER(state, i)
        end
    end
    return chart

procedure PREDICTOR((A â†’ Î±â€¢B, i), j, grammar)
    for each (B â†’ Î³) in GRAMMAR-RULES-FOR(B, grammar) do
        ADD-TO-SET((B â†’ â€¢Î³, j), chart[ j])
    end

procedure SCANNER((A â†’ Î±â€¢B, i), j)
    if B âŠ‚ PARTS-OF-SPEECH(word[j]) then
        ADD-TO-SET((B â†’ word[j], i), chart[j + 1])
    end

procedure COMPLETER((B â†’ Î³â€¢, j), k)
    for each (A â†’ Î±â€¢BÎ², i) in chart[j] do
        ADD-TO-SET((A â†’ Î±Bâ€¢Î², i), chart[k])
    end*/


