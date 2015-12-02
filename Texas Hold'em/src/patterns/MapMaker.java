package patterns;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import cards.and.stuff.Card;
import cards.and.stuff.Color;

public class MapMaker {
	
	private static ArrayList<Color> createArray(Color c){
		ArrayList<Color> list = new ArrayList<Color>();
		list.add(c);
		return list;
	}

	public static TreeMap<Integer,ArrayList<Color>> getMulti(LinkedList<Card> list){
		ArrayList<Color> l;
		TreeMap<Integer,ArrayList<Color>> map = new TreeMap<Integer,ArrayList<Color>>();
		for(Card c : list){
			int f = c.getFigure();
			Color color = c.getColor();
			if(map.get(f)==null)
			{
				map.put( new Integer(f) , createArray(color));
			}
			else
			{
				l = map.get(f);
				l.add(color);
				map.remove(f);
				map.put(f, l);
			}
			
		}
		return map;
	}
	
}
