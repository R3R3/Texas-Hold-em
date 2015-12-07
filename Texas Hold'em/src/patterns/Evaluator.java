package patterns;

import java.util.ArrayList;
import java.util.TreeMap;

import cards.and.stuff.Color;

public class Evaluator {
	public static int[] value(TreeMap<Integer,ArrayList<Color>> map){

		int v;
		int [] t;
		//Poker
		v = PokerPattern.ifIs(map);
		if(v>-1)
			return new int [] {8,v};
		//Four of a kind
		t=MultiChecker.isFour(map);
		if(t!=null)
			return t;
		//Full House
		t=MultiChecker.gotThree(map);
		if (t!= null)
			if(t[0] == 6)
				return t;
		//Straight
		v=Straight.ifIs(map);
		if(v!=-1)
			return new int []{5,v};
		//Color
		Color color = ColorPattern.ifSame(map);
		if(color!=null)
			return High.getHigh(map, color);
		//Three of a kind
		if(t!=null)
			return t;
		//Pairs
		t = MultiChecker.gotPair(map);
		if(t!=null)
			return t;
		//HighCard
		return High.getHigh(map);
	}

}
