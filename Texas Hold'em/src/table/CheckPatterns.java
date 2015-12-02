package table;

import java.util.LinkedList;

import cards.and.stuff.Card;
import cards.and.stuff.CardContainer;
import patterns.Evaluator;
import patterns.MapHolder;

public class CheckPatterns {

	private static CardContainer tablecards;
	private LinkedList<Card> hand;
	private LinkedList<Card> table;
	private LinkedList<Card> all;
	
	private CheckPatterns(MyHand hand) throws TableNotSend{
		if(tablecards==null)
			throw new TableNotSend();
		hand.sendToCheck(this);
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
		all.addAll(table);
		all.addAll(hand);
	}
	
	public static int[] getResult(MyHand hand) throws TableNotSend{
		CheckPatterns p = new CheckPatterns(hand);
		p.merge();
		MapHolder holder = new MapHolder(p.all);
		return Evaluator.value(holder);
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
