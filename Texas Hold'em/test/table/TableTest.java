package table;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cards.and.stuff.MyDeckBuilder;
import cards.and.stuff.NotEnoughCoins;

public class TableTest {

	Table t;
	ServerSocket serv;
	BufferedReader input;
	
	@Before
	public void createTable() throws Exception{
		
		serv = new ServerSocket(0);
		
		t = new Table(4);
		t.canWinPlayers = new ArrayList<Player> ();
		try {
			for(int i=0;i<4;i++){
				t.createPlayers(i, 100, new Socket("localhost", serv.getLocalPort()));
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
		
		t.players[1].setPlayerState(PlayerState.QUITED);
		t.setNextDealer();
		assertTrue(t.players[2].isDealer);
	}
	
	@Test
	public void nextdealerTest(){
		assertEquals(-1, t.setNextDealer());
		t.players[3].isDealer = true;
		t.players[0].setPlayerState(PlayerState.QUITED);
		t.setNextDealer();
		assertTrue(t.players[1].isDealer);
	}
	
	@Test
	public void randomdealerTest(){
		t.players[0].setPlayerState(PlayerState.QUITED);
		t.players[2].setPlayerState(PlayerState.QUITED);
		t.players[1].setPlayerState(PlayerState.QUITED);
		assertEquals(3,t.getRandomDealer());
	}
	
	@Test
	public void anothernextdealerTest(){
		t.players[2].isDealer = true;
		t.players[3].setPlayerState(PlayerState.QUITED);
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
		for (Player p : t.players){
			t.canWinPlayers.add(p);
		}
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
		for (Player p : t.players){
			t.canWinPlayers.add(p);
		}
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
	public void signalTest() throws IOException{
		
		t.players[1].setPlayerState(PlayerState.QUITED);
		t.sendReset();
		assertNotNull(t.canWinPlayers);
		t.give2CardsToPlayers();
		t.giveTableCards(TableCardsTurns.FLOP);
		t.giveTableCards(TableCardsTurns.TURN);
		t.giveTableCards(TableCardsTurns.RIVER);
		
		t.notifyAboutCards();
		t.notifyAboutTable(TableCardsTurns.FLOP);
		t.notifyAboutTable(TableCardsTurns.RIVER);
		t.notifyAboutTable(TableCardsTurns.TURN);
		t.notifyDealer(3);
		
		t.revealCards(0);
		
		t.players[0].setPlayerState(PlayerState.QUITED);
		t.notifyBet(true);
		t.notifyfixedunlock();
	}
	
	@Test
	public void HaltTest(){
		t.players[1].madeMove = true;
		assertTrue(t.players[1].madeMove);
		t.setAllNotMoved();
		assertFalse(t.players[1].madeMove);
	}
	
	@Test
	public void updatehighestbetTest(){
		t.highest_bet = 100;
		t.updateHighestBet();
		assertEquals(100,t.players[1].highestBet);
	}
	
	@Test
	public void updatepotTest() throws NotEnoughCoins, InterruptedException{
		t.players[0].actualWage = 10;
		t.players[0].highestBet = 10;
		t.players[0].tempPot = 10;
		
		t.updatePot(0);
		assertEquals(10,t.players[3].highestBet);
		assertEquals(10,t.players[2].tempPot);
		
		t.players[1].setPlayerState(PlayerState.FOLDED);
		t.updatePot(1);
		assertFalse(t.canWinPlayers.contains(t.players[1]));
		
		t.players[0].setPlayerState(PlayerState.QUITED);
		t.players[2].actualWage = 20;
		t.players[2].highestBet = 20;
		t.players[2].tempPot = 30;
		t.fixedbounds = 1;
		t.updatePot(2);
		
	}
	
	@After
	public void cleanUp() throws IOException{
		for(Player p : t.players){
			p.socket.close();
		}
		serv.close();
	}
}
