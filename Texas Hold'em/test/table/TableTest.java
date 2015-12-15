package table;

import static org.junit.Assert.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TableTest {

	Table t;
	
	@Before
	public void createTable() throws Exception{
		
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
		
		int i = t.getRandomDealer();
		
		assertTrue(t.players[i].isDealer);
		
		for(i=0;i<t.players.length;i++){
			t.players[i].isDealer = false;
			if(i!=0){t.players[i].setPlayerState(PlayerState.QUITED);}
		}
		
		i = t.getRandomDealer();
		
		assertTrue(t.players[i].isDealer);
		
		for(i=0;i<t.players.length;i++){
			t.players[i].isDealer = false;
			t.players[i].setPlayerState(PlayerState.INACTIVE);
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
			
			assertNotEquals(" ",t.players[i].getHand().getString(0));
			assertNotEquals(" ",t.players[i].getHand().getString(1));
		}
		
	}
	
	@Test
	public void setTableCardsTest() {
		
		t.giveTableCards(TableCardsTurns.FLOP);
		assertNotEquals(" ",t.tableCards.getString(0));
		assertNotEquals(" ",t.tableCards.getString(1));
		assertNotEquals(" ",t.tableCards.getString(2));
		t.giveTableCards(TableCardsTurns.TURN);
		assertNotEquals(" ",t.tableCards.getString(3));
		t.giveTableCards(TableCardsTurns.RIVER);
		assertNotEquals(" ",t.tableCards.getString(4));
	}
	
	@Ignore
	@Test
	public void resultTest(){
		
		t.give2CardsToPlayers();
		
		t.giveTableCards(TableCardsTurns.FLOP);
		t.giveTableCards(TableCardsTurns.TURN);
		t.giveTableCards(TableCardsTurns.RIVER);
		
		ArrayList<Player> winners = new ArrayList<Player>();
		winners = t.findWinner();
		assertFalse(winners.isEmpty());
		
	}
	
	@Test
	public void resetTest(){
		
		t.resetDeck();
		
		t.giveTableCards(TableCardsTurns.FLOP);
		
		String test = t.tableCards.getString(0);
		t.resetTableCards();
		t.deck.giveCardTo(t.tableCards);
		String test2;
		try{
		test2 = t.tableCards.getString(0);
		} catch (IndexOutOfBoundsException e){test2 = " ";}
		assertNotEquals(test2,test);
	
	}
	
	@Ignore
	@Test
	public void signalTest(){
		t.give2CardsToPlayers();
		t.giveTableCards(TableCardsTurns.FLOP);
		t.giveTableCards(TableCardsTurns.TURN);
		t.giveTableCards(TableCardsTurns.RIVER);
		
		t.notifyAboutCards();
		t.notifyAboutTable(TableCardsTurns.FLOP);
		t.notifyAboutTable(TableCardsTurns.RIVER);
		t.notifyAboutTable(TableCardsTurns.TURN);
		t.notifyDealer(3);
	}
}
