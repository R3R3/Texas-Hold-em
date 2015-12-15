package patterns;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import org.junit.Test;

import cards.and.stuff.Card;
import cards.and.stuff.Color;
import cards.and.stuff.MyDeckBuilder;

public class EvaluatorTest {

	private TreeMap<Integer,ArrayList<Color>> getMap(String [] s){
		LinkedList<Card> list = MyDeckBuilder.genList(s);
		return MapMaker.getMulti(list);
	}
	
	@Test
	public void pokerTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","4S","5S","6S","7S","5H","QS"});
		t = Evaluator.value(map);
		assertTrue(t[0]==8 && t[1] == 5);
	}
	
	@Test
	public void FourTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","JC","JS","6S","JH","5H","JD"});
		t = Evaluator.value(map);
		assertTrue(t[0]==7 && t[1] == 9 && t[2] == 4);
	}
	
	@Test
	public void FullTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","3D","3C","6S","7S","6H","QS"});
		t = Evaluator.value(map);
		assertTrue(t[0]==6 && t[1] == 1 && t[2] == 4);
	}
	
	@Test
	public void ColorTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","4S","KS","6S","7S","5H","QS"});
		t = Evaluator.value(map);
		assertTrue(t[0]==5 && t[1] == 11 && t[2] == 10 && t[3] == 5
				&& t[4] == 4 && t[5] == 2);
	}
	
	@Test
	public void StraightTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","4H","5S","6S","7S","5H","QC"});
		t = Evaluator.value(map);
		assertTrue(t[0]==4 && t[1] == 5);
	}
	
	
	@Test
	public void ThreeTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","4S","5D","5C","7S","5H","QS"});
		t = Evaluator.value(map);
		assertTrue(t[0]==3 && t[1] == 3 && t[2] == 10 && t[3] == 5);
	}
	
	@Test
	public void TwoPairsTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","4S","5D","6S","4C","5H","QS"});
		t = Evaluator.value(map);
		assertTrue(t[0]==2 && t[1] == 3 && t[2] == 2 && t[3] == 10);
	}
	
	@Test
	public void PairTest(){
		TreeMap<Integer,ArrayList<Color>> map;
		int t [];
		map = getMap(new String[]{"3S","4C","9C","6C","5D","5H","QS"});
		t = Evaluator.value(map);
		assertTrue(t[0]==1 && t[1] == 3 && t[2] == 10 && t[3] == 7 && t[4] == 4);
	}

}
