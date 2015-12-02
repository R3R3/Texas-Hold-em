package patterns;

import java.util.ArrayList;
import java.util.TreeMap;

import cards.and.stuff.Color;

public class Evaluator {
	public static int[] value(MapHolder holder){
		TreeMap<Integer,ArrayList<Color>> map = holder.getMap();
		int v;
		int [] t;
		//Poker
		v = PokerPattern.ifIs(holder);
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
		v=Straight.ifIs(holder);
		if(v!=-1)
			return new int []{5,v};
		//Color
		Color color = ColorPattern.ifSame(holder);
		if(color!=null)
			return High.getHigh(holder, color);
		//Three of a kind
		if(t!=null)
			return t;
		//Pairs
		t = MultiChecker.gotPair(map);
		if(t!=null)
			return t;
		//HighCard
		return High.getHigh(holder);
	}

}
