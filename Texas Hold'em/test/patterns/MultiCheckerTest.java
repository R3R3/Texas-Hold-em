package patterns;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import org.junit.Test;

import cards.and.stuff.Card;
import cards.and.stuff.CardGen;
import cards.and.stuff.Color;

public class MultiCheckerTest {


	@Test
	public void FourOfAKindTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genFour());
		int [] t = MultiChecker.isFour(map);
		assertEquals(t[0],7);
		assertEquals(t[1],4);
		assertEquals(t[2],11);
	}
	
	@Test
	public void noFourOfAKindTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genFull());
		int [] t = MultiChecker.isFour(map);
		assertEquals(t,null);
	}
	
	@Test
	public void FullTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genFull());
		int [] t = MultiChecker.gotThree(map);
		assertEquals(t[0],6);
		assertEquals(t[1],4);
		assertEquals(t[2],11);
	}
	
	@Test
	public void noFullTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genThree());
		int [] t = MultiChecker.gotThree(map);
		assertNotEquals(t[0],6);
	}
	
	@Test
	public void ThreeTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genThree());
		int [] t = MultiChecker.gotThree(map);
		assertEquals(t[0],3);
		assertEquals(t[1],4);
		assertEquals(t[2],12);
		assertEquals(t[3],11);
	}
	
	@Test
	public void noThreeTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(threePairs());
		int [] t = MultiChecker.gotThree(map);
		assertEquals(t,null);
	}
	@Test
	public void TwoPairsTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(threePairs());
		int [] t = MultiChecker.gotPair(map);
		assertEquals(t[0],2);
		assertEquals(t[1],11);
		assertEquals(t[2],9);
		assertEquals(t[3],12);
	}
	@Test
	public void noTwoPairsTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(onePair());
		int [] t = MultiChecker.gotPair(map);
		assertNotEquals(t[0],2);
	}
	
	@Test
	public void PairTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(onePair());
		int [] t = MultiChecker.gotPair(map);
		assertEquals(t[0],1);
		assertEquals(t[1],10);
		assertEquals(t[2],12);
		assertEquals(t[3],9);
		assertEquals(t[4],7);
	}
	
	@Test
	public void noMultiTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(noMulti());
		int [] t = MultiChecker.gotPair(map);
		assertEquals(t,null);
	}

	protected static LinkedList<Card> genFour(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(11,cards.and.stuff.Color.CLUBS));
		return list;
	}
	
	protected static LinkedList<Card> genFull(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(11,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(11,cards.and.stuff.Color.SPADES));
		return list;
	}
	
	protected static LinkedList<Card> genThree(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(11,cards.and.stuff.Color.SPADES));
		return list;
	}
	
	protected static LinkedList<Card> threePairs(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(11,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(11,cards.and.stuff.Color.SPADES));
		return list;
	}

	protected static LinkedList<Card> onePair(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(7,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.SPADES));
		return list;
	}
	
	protected static LinkedList<Card> noMulti(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(4,cards.and.stuff.Color.SPADES));
		list.add(CardGen.genCard(7,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.CLUBS));
		list.add(CardGen.genCard(5,cards.and.stuff.Color.SPADES));
		return list;
	}
	
}
