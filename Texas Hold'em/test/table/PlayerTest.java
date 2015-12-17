package table;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import cards.and.stuff.NotEnoughCoins;

public class PlayerTest {

	Player player;
	
	@Before
	public void createPlayer() throws Exception{
		
		player = new Player(100,0,new Socket(),4);
		player.actualWage = 20;
		player.highestBet = 40;
		player.tempPot = 60;
	}
	
	@Test
	public void callTest() throws NotEnoughCoins {
		player.call();
		assertEquals(40,player.highestBet);
		assertEquals(40,player.actualWage);
		assertEquals(80,player.tempPot);
	}
	
	@Test
	public void raiseTest() throws NotEnoughCoins{
		player.raise(10);
		assertEquals(50,player.highestBet);
		assertEquals(50,player.actualWage);
		assertEquals(90,player.tempPot);
	}
}
