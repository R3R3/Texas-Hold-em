package patterns;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import org.junit.Test;

import cards.and.stuff.Card;
import cards.and.stuff.CardGen;
import cards.and.stuff.Color;

public class MapMakerTest extends CardGen{

	@Test
	public void getMultiTest(){
		LinkedList<Card> list = genList();
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(list);
		assertNotNull(map);
	}
	
	public static void tryMapTest(TreeMap<Integer,ArrayList<Color>> map){
		int i = map.keySet().size();
		assertEquals(i,4);
		i = map.get(new Integer(9)).size();
		assertEquals(i,3);
		Color c = map.get(new Integer(7)).get(0);
		assertEquals(c,Color.SPADES);
		/*
		 for(Integer intg : map.keySet()){
			for(Color color : map.get(intg)){
				System.out.println(intg+" "+color);
			}
		}
		*/
	}
	
	@Test
	public void prepMapTest(){
		LinkedList<Card> list = genList();
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(list);
		tryMapTest(map);
	}
	
	
	protected static LinkedList<Card> genList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(7,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.CLUBS));
		return list;
	}

}
