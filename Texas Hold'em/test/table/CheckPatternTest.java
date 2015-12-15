package table;

import static org.junit.Assert.*;
import org.junit.Test;
import cards.and.stuff.CardContainer;
import cards.and.stuff.Deck;
import cards.and.stuff.MyDeckBuilder;

public class CheckPatternTest extends CardContainer{
	
	@Test
	public void CheckPatternsSimpleTest() throws TableNotSend{
		int [] t;
		MyHand hand = new MyHand();
		TableCards tc = new TableCards();
		Deck deck = new MyDeckBuilder().getDeck(new String[]{"3S","6D","9H","AC","KH","5H","QS"});
		for(int i = 0 ; i < 5; i++){
			deck.giveCardTo(tc);
		}
		while(deck.giveCardTo(hand));
		CheckPatterns.setTableCards(tc);
		t = CheckPatterns.getResult(hand);
		assertTrue(t[0]==0);		
	}
	
	@Test
	public void CheckPatternsOtherTest() throws TableNotSend{
		int [] t;
		MyHand hand = new MyHand();
		TableCards tc = new TableCards();
		Deck deck = new MyDeckBuilder().getDeck(new String[]{"3S","2D","3H","AC","3H","2H","QS"});
		for(int i = 0 ; i < 5; i++){
			deck.giveCardTo(tc);
		}
		while(deck.giveCardTo(hand));
		CheckPatterns.setTableCards(tc);
		t = CheckPatterns.getResult(hand);
		assertTrue(t[0]==6);
		assertTrue(t[1]==1);
		assertTrue(t[2]==0);
	}
	
	

}
