package table;

import java.util.ArrayList;

import cards.and.stuff.*;

public class Table {

	public TableCards tableCards;
	public Deck deck;
	Player[] players;
	
	private void prepareDeck(DeckBuilder builder){
			deck = builder.getDeck();
	}
	
	private void createPlayers(int amount, int basecash) throws PlayerException{
		for(int i=0; i< amount; i++){
			players[i] = new Player(basecash,i);
		}
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
	
	public static void main(String[] args){
		Table t = new Table();
		t.prepareDeck(new StandardDeckBuilder());
		t.prepareTableCards();
		t.deck.giveCardTo(t.tableCards);
		t.tableCards.giveCardTo(t.tableCards);
	}
	
}
