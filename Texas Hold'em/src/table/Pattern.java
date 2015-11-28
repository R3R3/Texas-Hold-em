package table;

import java.util.LinkedList;

import cards.and.stuff.Card;
import cards.and.stuff.CardContainer;

public class Pattern {

	private int [] result;
	private static CardContainer tablecards;
	
	public Pattern(TableCards tableCards){
		Pattern.tablecards = tableCards;
	}
	
	public int[] getResult(MyHand hand){
		/*
		LinkedList <Card> cards;		
		cards.addAll(hand.getAllCards());
		*/
		return result;
	}
}
