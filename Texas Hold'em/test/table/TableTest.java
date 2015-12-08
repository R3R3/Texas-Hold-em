package table;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TableTest {

	Table t;
	
	@Before
	public void createTable(){
		t = new Table(4);
	}
	
	@Test
	public void createTableTest(){
		assertNotNull(t);
	}
	
	@Test
	public void createPlayer(){
		try {
			t.createPlayers(0, 100, new Socket());
		} catch (PlayerException e) {}
		assertNotNull(t.players[0]);
		
		assertEquals(100, t.players[0].getCoins());
		
	}
	
	@Ignore
	@Test
	public void dealerTest(){
		t.getRandomDealer();
		boolean testdealer = false;
		int i;
		for(i = 0; i< t.players.length;i++){
			if(t.players[i].isDealer){ testdealer = true; break;}
		}
		
		assertTrue(testdealer);
		
		t.setNextDealer();
		assertTrue(t.players[(i+1 == t.players.length) ? 0 : i+1].isDealer);
	}
}
