package table;

import java.util.LinkedList;

import cards.and.stuff.Card;
import cards.and.stuff.CardContainer;

public class CheckPatterns {

	private int [] result;
	private static CardContainer tablecards;
	private LinkedList<Card> hand;
	
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
	
	public static int[] getResult(MyHand hand) throws TableNotSend{
		CheckPatterns p = new CheckPatterns(hand);
		
		return null;
	}
	
	
}
