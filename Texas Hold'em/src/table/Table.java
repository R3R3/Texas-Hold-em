package table;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import cards.and.stuff.*;
import network.GameMode;

public class Table {

	public TableCards tableCards;
	public boolean firstBet = false;
	public Deck deck;
	public Player[] players;
	public ArrayList<Player> canWinPlayers;
	public int num_Players;
	public Coins pot;
	public int player_with_highest_bet;
	public int highest_bet;
	protected int fixedbounds = -1;
	
	public void setFixedbounds(int fixedbounds) {
		this.fixedbounds = fixedbounds;
		this.fixedbounds++;
	}
	
	public int getFixedbounds(){
		return fixedbounds;
	}

	public Table(int amount){
		num_Players = amount;
		prepareDeck(new StandardDeckBuilder());
		prepareTableCards();
		players = new Player[num_Players];
		pot = new Coins();
		canWinPlayers = new ArrayList<Player>();
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
						if(j+1 == num_Players){j=-1;}
						if(players[j+1].getPlayerState() != PlayerState.QUITED){
							players[j+1].isDealer = true;
							return j+1;
						}
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
			players[ID] = new Player(basecash,ID, socket, num_Players);
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
		int [][] result = new int [players.length][];
		ArrayList<Player> winners = new ArrayList<Player>();
		CheckPatterns.setTableCards(tableCards);
		int i = 0;
		for (Player p : canWinPlayers){
			try {
				result[p.getID()] = getResult(p);
				i = CheckPatterns.check(result[p.getID()], t);
			} catch (TableNotSend e) {}
			if(i==1){
				winners=new ArrayList<Player>();
				t = result[p.getID()];
				winners.add(p);
			}else if(i==0){
				winners.add(p);
			}
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

	public void updatePot(int change) throws NotEnoughCoins, InterruptedException {
		
		//CHYBA DZIA£A
		if(players[change].getPlayerState() == PlayerState.FOLDED){
			updateFolded(change);
			this.canWinPlayers.remove(players[change]);
		}
		int diff = players[change].tempPot - pot.amount();
		players[change].coins.giveCoinsTo(pot, diff);
		if(players[change].highestBet > highest_bet){
			highest_bet = players[change].highestBet; 
			player_with_highest_bet = change;
			notifyBet(false);
			if(fixedbounds != -1){
				fixedbounds--;
				if(fixedbounds == 0){
					blockRaise();
				}
			}
		}
		updateHighestBet();
		refreshPlayers();
		
	}

	protected void blockRaise() {
		for(Player p : players){
			if(p.getPlayerState() != PlayerState.QUITED){
				p.output.println("FIXLOCK");
			}
		}
	}

	public void refreshPlayers() throws InterruptedException {
		
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

	protected void updateFolded(int change) {
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

	public void sendReset() throws NotEnoughCoins, InterruptedException {
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

	public void revealCards(int player) {
		
		for(Player p: players){
			if(p.getPlayerState() != PlayerState.QUITED){
				p.output.println("OP_CARD 0 " + Integer.toString(player) + " " + p.getHand().getString(0));
				p.output.println("OP_CARD 1 " + Integer.toString(player) + " " + p.getHand().getString(1));
			}
		}
	}

	public void notifyBet(boolean availableBet) {
		firstBet = !availableBet;
		for(Player p: players){
			if(p.getPlayerState() != PlayerState.QUITED){
				if(availableBet){
					p.output.println("BET T");
				}
				else {
					p.output.println("BET F");
				}
			}
		}
		
	}

	public void notifyGameMode(GameMode mode) {
		for(Player p : players){
			switch(mode){
				case POTLIMIT :
					p.output.println("MODE P");
					break;
				case NOLIMIT :
					p.output.println("MODE N");
					break;
				case FIXEDLIMIT :
					p.output.println("MODE F");
					break;
			}
		}
		
	}

	public void notifyFixedstuff(int fixedraise, int fixedbounds) {
		this.fixedbounds = fixedbounds +1;
		for(Player p : players){
			p.output.println("FIXRAI " + Integer.toString(fixedraise));
		}
	}

	public void notifyfixedunlock() {
		for(Player p: players){
			if(p.getPlayerState() != PlayerState.QUITED){
				p.output.println("FIXUNLOCK");
			}
		}
	}

	public void finalizeCash() {
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

	public void resetPot() {
		for(Player p: players){
			p.actualWage = 0;
			p.tempPot = 0;
			p.highestBet = 0;
		}
		pot.reset();
	}
}
