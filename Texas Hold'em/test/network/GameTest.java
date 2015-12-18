package network;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cards.and.stuff.NotEnoughCoins;
import table.Player;
import table.PlayerException;
import table.PlayerState;
import table.Table;

public class GameTest {

	Game game;
	Table t;
	ServerSocket serv;
	
	@Before
	public void SetUp() throws IOException{
		t = new Table(5);
		serv = new ServerSocket(0);
		t.canWinPlayers = new ArrayList<Player> ();
		try {
			for(int i=0;i<5;i++){
				t.createPlayers(i, 100, new Socket("localhost", serv.getLocalPort()));
			}
		} catch (PlayerException e) {}	
		
		game = new Game(t);
	}
	
	@Test
	public void resetTest(){
		game.resetBoard();
		
	}
	
	@Test
	public void revealTest(){
		t.give2CardsToPlayers();
		game.revealCards();
		t.players[1].setPlayerState(PlayerState.FOLDED);
		t.players[2].setPlayerState(PlayerState.QUITED);
		game.revealCards();
		
	}
	
	@Test
	public void norevealingTest(){
		t.give2CardsToPlayers();
		t.players[1].setPlayerState(PlayerState.FOLDED);
		t.players[2].setPlayerState(PlayerState.QUITED);
		t.players[3].setPlayerState(PlayerState.FOLDED);
		t.players[4].setPlayerState(PlayerState.QUITED);
		
		game.revealCards();
	}
	
	@Test
	public void setBlindsTest() throws NotEnoughCoins, InterruptedException{
		game.parameters.setActualDealer(0);
		game.parameters.setSmallBlind(10);
		game.parameters.setBigBlind(20);
		game.parameters.setMode(GameMode.FIXEDLIMIT);
		game.parameters.setFixedbounds(5);
		game.setBlinds();		
		
		assertEquals(1,game.parameters.getActualSB());
		assertEquals(2,game.parameters.getActualBB());
		assertEquals(10,t.players[1].actualWage);
		assertEquals(20,t.players[2].actualWage);
		assertEquals(5,t.getFixedbounds());
	}
	
	@Test
	public void allbranchesforsmallblindTest() throws NotEnoughCoins, InterruptedException{
		game.parameters.setActualDealer(3);
		game.parameters.setSmallBlind(10);
		game.parameters.setBigBlind(20);
		t.players[4].setPlayerState(PlayerState.QUITED);
		t.players[0].coins.giveCoinsTo(t.pot, 95);
		
		game.setBlinds();
		
		assertEquals(1,game.parameters.getActualSB());
		assertEquals(2,game.parameters.getActualBB());
		assertEquals(PlayerState.QUITED,t.players[0].getPlayerState());
		assertEquals(10,t.players[1].actualWage);
		assertEquals(20,t.players[2].actualWage);
	}
	
	@Test
	public void allbranchesforbigblindTest() throws NotEnoughCoins, InterruptedException{
		game.parameters.setActualDealer(2);
		game.parameters.setSmallBlind(10);
		game.parameters.setBigBlind(20);
		t.players[4].setPlayerState(PlayerState.QUITED);
		t.players[0].coins.giveCoinsTo(t.pot, 85);
		
		game.setBlinds();
		
		assertEquals(3,game.parameters.getActualSB());
		assertEquals(1,game.parameters.getActualBB());
		assertEquals(PlayerState.QUITED,t.players[0].getPlayerState());
		assertEquals(10,t.players[3].actualWage);
		assertEquals(20,t.players[1].actualWage);
	}
	
	
	
	@After
	public void cleanUp() throws IOException{
		for(Player p : t.players){
			p.socket.close();
		}
		serv.close();
	}
}
