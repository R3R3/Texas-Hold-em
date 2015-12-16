package table;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import cards.and.stuff.MyDeckBuilder;

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
	
	@Test
	public void oneWinnerTest(){
		t.deck = new MyDeckBuilder().getDeck(new String[]{
						"6H",
						"0C","9C","JC","5D","6S",
						"6C","QC","2C","QH",
						"5H","KC","3C","KH"
						});
		t.give2CardsToPlayers();
		
		t.giveTableCards(TableCardsTurns.FLOP);
		t.giveTableCards(TableCardsTurns.TURN);
		t.giveTableCards(TableCardsTurns.RIVER);
		
		ArrayList<Player> winners = new ArrayList<Player>();
		winners = t.findWinner();
		assertEquals(winners.get(0),t.players[2]);
		
	}
	
	@Test
	public void moreWinnersTest(){
		t.deck = new MyDeckBuilder().getDeck(new String[]{
						"6H",
						"0C","9C","JC","5D","6S",
						"QS","QD","2D","QH",
						"KS","KD","3H","KH"
						});
		t.give2CardsToPlayers();
		
		t.giveTableCards(TableCardsTurns.FLOP);
		t.giveTableCards(TableCardsTurns.TURN);
		t.giveTableCards(TableCardsTurns.RIVER);
		
		ArrayList<Player> winners = new ArrayList<Player>();
		winners = t.findWinner();
		assertTrue(winners.size()==3);
		
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
