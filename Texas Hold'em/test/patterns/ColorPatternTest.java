package patterns;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import org.junit.Test;

import cards.and.stuff.Card;
import cards.and.stuff.CardGen;
import cards.and.stuff.Color;

public class ColorPatternTest {

	@Test
	public void noColorTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(noColorList());
		Color color = ColorPattern.ifSame(map);
		assertNull(color);
	}
	
	@Test
	public void isColorTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		map = MapMaker.getMulti(isColorList());
		Color color = ColorPattern.ifSame(map);
		assertEquals(color,Color.DIAMOND);
	}
	
	
	protected static LinkedList<Card> isColorList(){
		LinkedList<Card> list = new LinkedList<Card>();
		list.add(CardGen.genCard(7,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.HEARTS));
		list.add(CardGen.genCard(1,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(2,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(12,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.DIAMOND));
		list.add(CardGen.genCard(9,cards.and.stuff.Color.CLUBS));
		return list;
	}
	protected static LinkedList<Card> noColorList(){
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
