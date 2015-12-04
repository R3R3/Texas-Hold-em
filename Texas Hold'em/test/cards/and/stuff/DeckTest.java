package cards.and.stuff;

import static org.junit.Assert.*;
import org.junit.Test;

public class DeckTest {
	
	@Test
	public void addTest(){
		Deck c = new Deck();
		c.addCard(new Card(7,Color.HEARTS));
		assertTrue(c.cards.getLast().getFigure() == 7 &&
					c.cards.getLast().getColor() ==Color.HEARTS);
	}
	
	
	@Test
	public void shuffleTest(){
		Deck c = new Deck();
		for(int i = 0;i<12;i++){
			c.addCard(new Card(i,Color.HEARTS));
			c.shuffle();
		}
		int i =0;
		int j = 0;
		for(Card card : c.cards){
			//System.out.println(card.getFigure());
			//System.out.println(card.getColor().toString());
			if(card.getFigure()==i)
				j++;
			i++;
		}
		assertTrue(j<4);
	}
	
	@Test
	public void giveCardTest(){
		Deck a = new Deck();
		Deck b = new Deck();
		a.addCard(new Card(11,Color.DIAMOND));
		a.giveCardTo(b);
		assertTrue(a.cards.size()==0);
		assertTrue(b.cards.size()==1);
		assertTrue(b.cards.getLast().getFigure()==11 &&
				b.cards.getLast().getColor()==Color.DIAMOND);
	}
	
	@Test
	public void resetTest(){
		Deck a = new Deck();
		a.addCard(new Card(8,Color.CLUBS));
		a.reset();
		assertTrue(a.cards.size()==0);
	}	
}
