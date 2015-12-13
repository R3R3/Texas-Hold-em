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
	public int player_with_highest_bet;
	public int highest_bet;
	
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
		for(Player p : players){
			p.notyfyAboutCards();
		}	
	}

	public void notifyAboutTable(TableCardsTurns cards ) {
		// TODO: send strings based on given enum
		switch(cards){
			case FLOP:
				for(Player p : players){
					p.output.println("TABLE 0 " + tableCards.getString(0));
					p.output.println("TABLE 1 " + tableCards.getString(1));
					p.output.println("TABLE 2 " + tableCards.getString(2));
				}
				break;
			case TURN:
				for(Player p : players){
					p.output.println("TABLE 3 " + tableCards.getString(3));
				}
				break;
			case RIVER:
				for(Player p : players){
					p.output.println("TABLE 4 " + tableCards.getString(4));
				}
				break;
		}
		
	}

	public void updatePot(int change) throws NotEnoughCoins {
		for(int i=0;i<num_Players;i++) {
			if(i==change){
				int diff = players[change].tempPot - pot.amount();
				players[change].coins.giveCoinsTo(pot, diff);
				players[change].highestBet = highest_bet;
				updateHighestBet();
			}
			else{
				players[i].tempPot = players[change].tempPot;
			}
			players[i].output.println("POT " + Integer.toString(players[change].tempPot));
		}
		
	}

	public void updateHighestBet() {
		for(Player p : players){
			p.highestBet = highest_bet;
			p.output.println("POT " + Integer.toString(pot.amount()));
		}
		
	}
}
