package cards.and.stuff;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CardTest{
	Random rng;
	
	@Before
	public void generator(){
		rng = new Random();
	}

	@Test
	public void newCardTest(){
		Card card = new Card(7,Color.SPADES);
		assertEquals(card.getFigure(),7);
		assertEquals(card.getColor(),Color.SPADES);
	}
	
	@Test
	public void notNullColorTest(){
		Card card = new Card(4,Color.DIAMOND);
		assertNotNull(card.getColor());
	}

}
