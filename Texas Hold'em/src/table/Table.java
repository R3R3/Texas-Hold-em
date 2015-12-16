package table;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import cards.and.stuff.*;

public class Table {

	public TableCards tableCards;
	public Deck deck;
	public Player[] players;
	public ArrayList<Player> canWinPlayers;
	public int num_Players;
	public Coins pot;
	public int player_with_highest_bet;
	public int highest_bet;
	
	public Table(int amount){
		num_Players = amount;
		prepareDeck(new StandardDeckBuilder());
		prepareTableCards();
		players = new Player[num_Players];
		pot = new Coins();
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
		int i;
		while (true){
			i = random.nextInt(num_Players);
			if(players[i].getPlayerState() != PlayerState.QUITED) {
				players[i].isDealer = true;
				break;
			}
		}
		return i;
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
		int [][] result = new int [canWinPlayers.size()][];
		ArrayList<Player> winners = new ArrayList<Player>();
		CheckPatterns.setTableCards(tableCards);
		int i = 0;
		int j = 0;
		for (Player p : canWinPlayers){
			try {
				result[j] = getResult(p);
				i = CheckPatterns.check(result[j], t);
			} catch (TableNotSend e) {}
			if(i==1){
				winners=new ArrayList<Player>();
				t = result[j];
				winners.add(p);
			}else if(i==0){
				winners.add(p);
			}
			j++;
		}
		return winners;
	}

	public void notifyAboutCards() {
		for(Player p : players){
			p.notyfyAboutCards();
		}	
	}

	public void notifyAboutTable(TableCardsTurns cards ) {
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
		
		//CHYBA DZIA£A
		
		if(players[change].getPlayerState() == PlayerState.FOLDED){
			updateFolded(change);
		}
		int diff = players[change].tempPot - pot.amount();
		players[change].coins.giveCoinsTo(pot, diff);
		if(players[change].highestBet > highest_bet){
			highest_bet = players[change].highestBet; 
			player_with_highest_bet = change;
		}
		updateHighestBet();
		refreshPlayers();
		
	}

	private void refreshPlayers() {
		
		for(Player p: players){
			for(int i=0;i<num_Players;i++){
				if (p.getID() == i){
					p.output.println("CASH " + Integer.toString(p.coins.amount()));
					p.output.println("WAGE " + Integer.toString(p.actualWage));
					p.output.println("POT " + Integer.toString(pot.amount()));
				} else {
					p.output.println("OP_CASH " + Integer.toString(i) + " " + Integer.toString(players[i].coins.amount()));
					p.output.println("OP_WAGE " + Integer.toString(i) + " " + Integer.toString(players[i].actualWage));
					p.output.println("POT " + Integer.toString(pot.amount()));
				}
			}
		}
		
	}

	private void updateFolded(int change) {
		Player f = null;
		for(Player p : players){
			p.output.println("FOLD " + Integer.toString(change));
			if(p.getID()==change){
				f = p;
			}
		}
		canWinPlayers.remove(f);	
	}

	public void updateHighestBet() {
		for(Player p : players){
			p.highestBet = highest_bet;
			p.tempPot = pot.amount();
		}
		
	}

	public void notifyDealer(int actualDealer) {
		for(Player p : players) {
			p.output.println("DEALER " + Integer.toString(actualDealer));
		}
		
	}

	public void resetDeck() {
		deck.reset();
		prepareDeck(new StandardDeckBuilder());
	}

	public void resetTableCards() {
		tableCards.reset();
		prepareTableCards();
	}

	public void sendReset() {
		canWinPlayers = new ArrayList<Player> ();
		for(Player p:players){
			p.output.println("RESET");
			if(p.getPlayerState() != PlayerState.QUITED){
				p.setPlayerState(PlayerState.INACTIVE);
				p.output.println("INACTIVE");
				p.isAll_in = false;
				canWinPlayers.add(p);				
			}
		}
	}

	public void setAllNotMoved() {
		for(Player p : players){
			p.madeMove = false;
		}
		
	}
}
