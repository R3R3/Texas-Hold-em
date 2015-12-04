package cards.and.stuff;

import static org.junit.Assert.*;

import org.junit.Test;

public class StandardDeckBuilderTest {

	@Test
	public void createDeckTest(){
		StandardDeckBuilder sdb = new StandardDeckBuilder();
		Deck d = sdb.getDeck();
		assertTrue(d.cards.size()==52);
		for(Color color : Color.values()){
			int i=0;
			for(Card card : d.cards){
				if(card.getColor() == color)
					i++;
			}
			assertEquals(i,13);
		}
		for(int j = 0; j<13; j++){
			int i=0;
			for(Card card : d.cards){
				if(card.getFigure() == j){
					i++;
				}
			}
			assertEquals(i,4);
		}
	}
	
	@Test
	public void resetTest(){
		StandardDeckBuilder sdb = new StandardDeckBuilder();
		Deck a = sdb.getDeck();
		sdb.resetDeck();
		Deck b = sdb.getDeck();
		assertNotEquals(a,b);
	}

}
