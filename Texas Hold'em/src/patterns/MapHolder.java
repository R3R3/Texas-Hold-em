package patterns;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import cards.and.stuff.Card;
import cards.and.stuff.Color;

public class MapHolder {

	TreeMap<Integer,ArrayList<Color>> map;
	
	public MapHolder(LinkedList<Card> list)
	{
		map = MapMaker.getMulti(list);
	}

	public TreeMap<Integer, ArrayList<Color>> getMap(){
		return map;
	}
	
}
