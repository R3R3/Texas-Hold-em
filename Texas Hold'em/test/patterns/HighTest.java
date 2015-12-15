package patterns;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import org.junit.Test;

import cards.and.stuff.Card;
import cards.and.stuff.CardGen;
import cards.and.stuff.Color;

public class HighTest {


	@Test
	public void ResultTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genList());
		int [] t = High.getHigh(map);
		assertEquals(t[0],0);	
		assertEquals(t[1],12);
		assertEquals(t[2],11);
		assertEquals(t[3],10);
		assertEquals(t[4],7);
		assertEquals(t[5],4);
	}
	
	@Test
	public void ResultColorTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genList());
		int [] t = High.getHigh(map,Color.DIAMOND);
		assertEquals(t[0],5);	
		assertEquals(t[1],12);
		assertEquals(t[2],10);
		assertEquals(t[3],7);
		assertEquals(t[4],4);
		assertEquals(t[5],2);
	}
	
	@Test
	public void ResultNoColorTest() {
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(genList());
		int [] t = High.getHigh(map,Color.DIAMOND);
		assertEquals(t[0],5);	
		assertEquals(t[1],12);
		assertEquals(t[2],10);
		assertEquals(t[3],7);
		assertEquals(t[4],4);
		assertEquals(t[5],2);
	}
	
	
	
	protected static LinkedList<Card> genList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(7,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(4,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(10,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(11,cards.and.stuff.Color.CLUBS));
		return list;
	}

}
