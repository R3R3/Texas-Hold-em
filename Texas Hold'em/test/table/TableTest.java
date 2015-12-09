package table;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cards.and.stuff.CardContainer;

public class TableTest {

	Table t;
	
	@Before
	public void createTable(){
		t = new Table(4);
		try {
			for(int i=0;i<4;i++){
				t.createPlayers(i, 100, new Socket());
			}
		} catch (PlayerException e) {}
		
		
	}
	
	@Test
	public void createTableTest(){
		assertNotNull(t);
	}
	
	@Test
	public void createPlayer(){
		
		assertNotNull(t.players[0]);
		
		assertEquals(100, t.players[0].getCoins());
		assertEquals(0, t.players[0].getID());
		
	}
	
	@Test
	public void dealerTest(){
		t.getRandomDealer();
		boolean testdealer = false;
		int i;
		for(i = 0; i< t.players.length;i++){
			if(t.players[i].isDealer){ testdealer = true; break;}
		}
		
		assertTrue(testdealer);
		
		for(i=0;i<t.players.length;i++){
			t.players[i].isDealer = false;
		}
		
		t.players[1].isDealer = true;
		t.setNextDealer();
		
		assertTrue(t.players[2].isDealer);
		t.players[2].isDealer = false;
		
		t.players[3].isDealer = true;
		t.setNextDealer();
		
		assertTrue(t.players[0].isDealer);
	}
	
	@Test
	public void give2CardsTest(){
		
		t.give2CardsToPlayers();
		for(int i=0;i<t.players.length;i++){
			assertNotNull(t.players[i].getHand());
			
			//TODO: iloœc kart?
		}
		
	}
	
	@Ignore
	@Test
	public void setTableCardsTest() {
		
		//TODO: patrzenie na karty/iloœc?
		t.giveTableCards(TableCardsTurns.FLOP);
		
		t.giveTableCards(TableCardsTurns.TURN);
		
		t.giveTableCards(TableCardsTurns.RIVER);
	}
	
	@Ignore
	@Test
	public void resultTest(){
		
		int[] testResult = null;
		
		t.give2CardsToPlayers();
		
		t.giveTableCards(TableCardsTurns.FLOP);
		t.giveTableCards(TableCardsTurns.TURN);
		t.giveTableCards(TableCardsTurns.RIVER);
		
		try {
			testResult = t.getResult(t.players[0]);
		} catch (TableNotSend e) {}
		
		assertNotNull(testResult);
		assertTrue((testResult.length > 0) ? true : false);
		
		ArrayList<Player> winners = new ArrayList<Player>();
		winners = t.findWinner();
		assertFalse(winners.isEmpty());
		
	}
}
