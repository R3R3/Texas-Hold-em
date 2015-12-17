package network;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import table.PlayerState;
import table.Table;

public class EndOfAuctionTest {

	Table t;
	Game g;

	@Before
	public void setUp() throws Exception {
		t = new Table(6);
		for(int i = 0; i < 6 ; i ++){
			t.createPlayers(i, 0, new Socket());
			t.canWinPlayers.add(t.players[i]);
		}
		
		g =  new Game(t);
	}
	
	@Test
	public void BigBlindCanPlay() {
		setWages(new int []{20,20,20,20,20,0});
		t.players[5].setPlayerState(PlayerState.QUITED);
		g.parameters.setBigBlind(20);
		g.parameters.setActualBB(1);
		assertFalse(g.endAuction(0));
		assertTrue(g.endAuction(1));
	}

	@Test
	public void AllInTest() {
		setWages(new int []{20,50,20,70,20,0});
		t.players[0].setPlayerState(PlayerState.FOLDED);
		t.players[2].setPlayerState(PlayerState.FOLDED);
		t.players[4].setPlayerState(PlayerState.FOLDED);
		t.players[5].setPlayerState(PlayerState.QUITED);
		g.parameters.setBigBlind(20);
		g.parameters.setActualBB(1);
		assertFalse(g.endAuction(4));
		t.players[1].isAll_in = true;
		assertTrue(g.endAuction(1));
	}
	
	@Test
	public void NormalEndingTest() {
		setWages(new int []{20,70,20,70,50,0});
		t.players[0].setPlayerState(PlayerState.FOLDED);
		t.players[2].setPlayerState(PlayerState.FOLDED);
		t.players[4].isAll_in = true;
		t.players[5].setPlayerState(PlayerState.QUITED);
		g.parameters.setBigBlind(20);
		g.parameters.setActualBB(1);
		assertTrue(g.endAuction(2));
	}
	
	private void setWages(int [] tab){
		int max = 0;
		for(int i= 0; i < tab.length; i++){
			t.players[i].actualWage = tab[i];
			if(tab[i]>max)
				max = tab[i];
		}
		t.highest_bet = max;
	}
}
