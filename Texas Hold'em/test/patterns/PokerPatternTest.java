package patterns;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import org.junit.AfterClass;
import org.junit.Test;

import cards.and.stuff.Card;
import cards.and.stuff.CardGen;
import cards.and.stuff.Color;

public class PokerPatternTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void isPokerTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(isPokerList());
		int t = PokerPattern.ifIs(map);
		assertEquals(t,9);
	}
	
	@Test
	public void asPokerTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(asPokerList());
		int t = PokerPattern.ifIs(map);
		assertEquals(t,3);
	}
	@Test
	public void noPokerTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(noPokerList());
		int t = PokerPattern.ifIs(map);
		assertEquals(t,-1);
	}
	@Test
	public void noPokerTest2() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(noPokerList2());
		int t = PokerPattern.ifIs(map);
		assertEquals(t,-1);
	}
	@Test
	public void noPokerTest3() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(noPokerList3());
		int t = PokerPattern.ifIs(map);
		assertEquals(t,-1);
	}
	protected static LinkedList<Card> isPokerList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(5,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(6,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(7,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(8,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(8,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.DIAMOND));
		return list;
	}
	
	protected static LinkedList<Card> noPokerList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(0,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(3,cards.and.stuff.Color.DIAMOND));
		return list;
	}
	
	protected static LinkedList<Card> noPokerList2(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(0,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(3,cards.and.stuff.Color.HEARTS));
		return list;
	}
	
	protected static LinkedList<Card> noPokerList3(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(0,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(3,cards.and.stuff.Color.DIAMOND));
		return list;
	}
	
	protected static LinkedList<Card> asPokerList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(9,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(0,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(3,cards.and.stuff.Color.DIAMOND));
		return list;
	}

}
