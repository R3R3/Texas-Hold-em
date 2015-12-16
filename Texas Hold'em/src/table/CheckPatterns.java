package table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import cards.and.stuff.Card;
import cards.and.stuff.Color;
import patterns.Evaluator;
import patterns.MapMaker;

public class CheckPatterns {

	private static TableCards tablecards;
	private LinkedList<Card> hand;
	private LinkedList<Card> table;
	private LinkedList<Card> all;
	
	private CheckPatterns(MyHand hand) throws TableNotSend{
		if(tablecards==null)
			throw new TableNotSend();
		hand.sendToCheck(this);
		tablecards.sendToCheck(this);
	}
	
	public static void setTableCards(TableCards tableCards){
		CheckPatterns.tablecards = tableCards;
	}
	
	protected void getHandCards(LinkedList<Card> hand){
		this.hand = hand;
	}
	protected void getTableCards(LinkedList<Card> table){
		this.table = table;
	}
	
	private void merge(){
		all = new LinkedList<Card>();
		for(Card c : table){
			all.add(c);
		}
		for(Card c : hand){
			all.add(c);
		}
	}
	
	public static int[] getResult(MyHand hand) throws TableNotSend{
		CheckPatterns p = new CheckPatterns(hand);
		p.merge();
		TreeMap<Integer,ArrayList<Color>> map = MapMaker.getMulti(p.all);
		return Evaluator.value(map);
	}
	
	public static int check(int [] a, int []b){
		int s1 = a.length;
		int s2 = b.length;
		int max = s1>s2?s1:s2;
		for(int i = 0; i < max; i++)
		{
			if(a[i]>b[i]){
				return 1;
			} else if (b[i]>a[i]){
					return 2;
				}
		}
		return 0;
	}
	
	
}
