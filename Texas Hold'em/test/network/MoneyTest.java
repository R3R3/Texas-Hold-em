package network;

import static org.junit.Assert.*;

import java.net.Socket;
import org.junit.Before;
import org.junit.Test;

import cards.and.stuff.Coins;
import cards.and.stuff.MyDeckBuilder;
import cards.and.stuff.NotEnoughCoins;
import table.Table;
import table.TableCardsTurns;

public class MoneyTest {
	private Table t;
	private Game g;
	@Before
	public void setUp() throws Exception {
		t = new Table(6);
		for(int i = 0; i < 6 ; i ++){
			t.createPlayers(i, 0, new Socket());
		}
		g =  new Game(t);
		t.deck = new MyDeckBuilder().getDeck(new String[]{
			"7C","5S","9H","AC","KH","5H","QS",
			"JD","5C","JC","5S","4H","AS",
			"0D","AS","0C","AH","0H","3H"
		});
		t.give2CardsToPlayers();
		t.giveTableCards(TableCardsTurns.FLOP);
		t.giveTableCards(TableCardsTurns.RIVER);
		t.giveTableCards(TableCardsTurns.TURN);
	}
	
	
	@Test
	public void oneWinnerTest() throws NotEnoughCoins{
		t.pot = new Coins(100);
		wages(new int[] {40,10,10,10,10,20});
		g.findWinners();
		assertTrue(t.players[0].coins.amount()==100);
	}
	
	@Test
	public void moreWinnersTest() throws NotEnoughCoins{
		t.pot = new Coins(100);
		wages(new int[] {10,10,30,10,30,10});
		g.findWinners();
		assertTrue(t.players[2].coins.amount()==50 &&
					t.players[4].coins.amount()==50 );
	}
	
	@Test
	public void allInCase1Test() throws NotEnoughCoins{
		t.pot = new Coins(100);
		wages(new int[] {10,10,25,10,35,10});
		t.players[2].isAll_in = true;
		g.findWinners();
		assertEquals(t.players[2].coins.amount(),45);
		assertEquals(t.players[4].coins.amount(),55);
	}

	private void wages(int tab []){
		int max = 0;
		for(int i = 0 ; i < 6 ;i ++)
		{
			t.players[i].actualWage = tab[i];
			if ( tab[i] > max)
				max = tab[i];
		}
		t.highest_bet =  max;
	}
}
