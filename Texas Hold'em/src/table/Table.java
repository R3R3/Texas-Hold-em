package table;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import cards.and.stuff.*;

public class Table {

	public TableCards tableCards;
	public Deck deck;
	public Player[] players;
	private int num_Players;
	
	public Table(int amount){
		num_Players = amount;
		prepareDeck(new StandardDeckBuilder());
		prepareTableCards();
		players = new Player[num_Players];
	}
	
	private void setNextDealer() {
		for(int i=0;i<num_Players;i++){
			if(players[i].isDealer){
				players[i].isDealer = false;
				if(i+1 == num_Players){
					players[0].isDealer = true;
				} else {
					players[i+1].isDealer = true;
				}
				break;
			}
		}
	}
	
	private void getRandomDealer() {
		Random random = new Random();
		players[random.nextInt(num_Players)].isDealer = true;
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
	
	private void give2CardsToPlayers(){
		for(int i = 0; i < 2; i++){
			for(Player p : players){
				deck.giveCardTo(p.getHand());
			}
		}
	}
	
	private void giveTableCards(String set){
		if(set == "flop"){
			deck.giveCardTo(tableCards);
			deck.giveCardTo(tableCards);
			deck.giveCardTo(tableCards);
		}
		else if(set == "turn" || set == "river"){
			deck.giveCardTo(tableCards);
			CheckPatterns.setTableCards(tableCards);
		}
	}
	
	private int[] getResult(Player p) throws TableNotSend{
		return CheckPatterns.getResult(p.getHand());
	}
	
	private ArrayList<Player> findWinner(){
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
	/*
	public static void main(String[] args){
		Table t = new Table();
		t.prepareDeck(new StandardDeckBuilder());
		t.prepareTableCards();
		t.deck.giveCardTo(t.tableCards);
		t.tableCards.giveCardTo(t.tableCards);
	}
	*/
}
