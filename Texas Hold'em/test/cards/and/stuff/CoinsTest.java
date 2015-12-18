package cards.and.stuff;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoinsTest {

	@Test
	public void coinsConstructorTest(){
		Coins b = new Coins();
		Coins c = new Coins(8);
		assertEquals(c.amount(),8);
		assertEquals(b.amount(),0);
	}
	
	@Test
	public void giveCoinsTest() throws NotEnoughCoins{
		Coins a = new Coins(5);
		Coins b = new Coins();
		a.giveCoinsTo(b, 3);
		assertTrue(a.amount()==2 &&
				b.amount()==3);
	}
	
	@Test(expected = NotEnoughCoins.class)
	public void coinExceptionTest() throws NotEnoughCoins{
		Coins a = new Coins(2);
		Coins b = new Coins(2);
		try {
			a.giveCoinsTo(b, 5);
		} catch (NotEnoughCoins e) {
			throw e;
		}
	}

}
