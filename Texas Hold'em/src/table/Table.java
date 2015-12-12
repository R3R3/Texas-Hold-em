package table;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import cards.and.stuff.*;

public class Table {

	public TableCards tableCards;
	public Deck deck;
	public Player[] players;
	public int num_Players;
	public Coins pot;
	
	public Table(int amount){
		num_Players = amount;
		prepareDeck(new StandardDeckBuilder());
		prepareTableCards();
		players = new Player[num_Players];
	}
	
	public int setNextDealer() {
		
		for(int i=0;i<num_Players;i++){
			if(players[i].isDealer){
				players[i].isDealer = false;
				if(i+1 == num_Players){
					int j;
					for (j = 0; j<num_Players;j++){
						if(players[j].getPlayerState() != PlayerState.QUITED){
							players[j].isDealer = true;
							return j;
						}
					}
				} else {
					int j;
					for(j = i;j<num_Players;j++){
						if(players[j+1].getPlayerState() != PlayerState.QUITED){
							players[j+1].isDealer = true;
							return j+1;
						}
						if(j+1 == num_Players){j=-2;}
					}
				}
			}
		}
		return -1;
	}
	
	public int getRandomDealer() {
		Random random = new Random();
		int i = random.nextInt(num_Players);
		if(players[i].getPlayerState() != PlayerState.QUITED) {
			return i;
		}
		else {
			getRandomDealer();
		}
		return -1;
	}
	
	private void prepareDeck(DeckBuilder builder){
			deck = builder.getDeck();
	}
	
	public void createPlayers(int ID, int basecash, Socket socket) throws PlayerException {
			players[ID] = new Player(basecash,ID, socket);
	}
	
	private void prepareTableCards(){
		tableCards = new TableCards();
		CheckPatterns.setTableCards(tableCards);
		
	}
	
	public void give2CardsToPlayers(){
		for(int i = 0; i < 2; i++){
			for(Player p : players){
				deck.giveCardTo(p.getHand());
			}
		}
	}
	
	public void giveTableCards(TableCardsTurns turn){
		switch(turn){
			case FLOP:
				deck.giveCardTo(tableCards);
				deck.giveCardTo(tableCards);
				deck.giveCardTo(tableCards);
				break;
			case TURN:
				deck.giveCardTo(tableCards);
				break;
			case RIVER:
				deck.giveCardTo(tableCards);
				CheckPatterns.setTableCards(tableCards);
				break;
		}
	}
	
	protected int[] getResult(Player p) throws TableNotSend{
		return CheckPatterns.getResult(p.getHand());
	}
	
	public ArrayList<Player> findWinner(){
		int[]t = new int []{-1};
		ArrayList<Player> winners = new ArrayList<Player>();
		CheckPatterns.setTableCards(tableCards);
		int i = 0;
		for (Player p : players){
			try {
				i = CheckPatterns.check(getResult(p), t);
			} catch (TableNotSend e) {}
			if(i==1){
				winners=new ArrayList<Player>();
				winners.add(p);
			}else if(i==0){
				winners.add(p);
			}		
		}
		return winners;
	}

	public void notifyAboutCards() {
		//TODO: send strings about cards to each player
		int i=0;
		while (i<2){
			for(Player p : players){
				p.output.println("HAND");
			}
		}
		
	}

	public void notifyAboutTable(TableCardsTurns cards ) {
		// TODO: send strings based on given enum
		switch(cards){
			case FLOP:
				break;
			case TURN:
				break;
			case RIVER:
				break;
		}
		
	}

	public void updatePot() {
		for(Player p : players){
			p.pot = pot.amount();
			p.output.println("POT " + Integer.toString(pot.amount()) );
		}
		
	}
}
