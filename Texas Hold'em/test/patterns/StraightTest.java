package patterns;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import org.junit.Test;

import cards.and.stuff.Card;
import cards.and.stuff.CardGen;
import cards.and.stuff.Color;

public class StraightTest {


	@Test
	public void isStraightTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(isStrList());
		int t = Straight.ifIs(map);
		assertEquals(8,t);
	}	
	
	@Test
	public void noStraightTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(noStrList());
		int t = Straight.ifIs(map);
		assertEquals(-1,t);
	}	
	
	@Test
	public void asStraightTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(asStrList());
		int t = Straight.ifIs(map);
		assertEquals(3,t);
	}
	
	protected static LinkedList<Card> isStrList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(5,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(6,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(7,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(8,cards.and.stuff.Color.CLUBS));
		return list;
	}
	
	protected static LinkedList<Card> noStrList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(3,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(5,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(6,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(7,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(8,cards.and.stuff.Color.CLUBS));
		return list;
	}
	
	protected static LinkedList<Card> asStrList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(3,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(0,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(7,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(8,cards.and.stuff.Color.CLUBS));
		return list;
	}

}
