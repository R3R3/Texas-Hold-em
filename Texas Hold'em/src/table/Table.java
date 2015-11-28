package table;

import cards.and.stuff.*;

public class Table {

	public TableCards tableCards;
	public Deck deck;
	Player[] players;
	private Pattern pattern;
	
	private void prepareDeck(DeckBuilder builder){
		try {
			deck = builder.getDeck();
		} catch (CardException e) {
			e.getMessage();
		}
	}
	
	private void createPlayers(int amount, int basecash) throws PlayerException{
		for(int i=0; i< amount; i++){
			players[i] = new Player(basecash,i);
		}
	}
	
	private void prepareTableCards(){
		tableCards = new TableCards();
		
	}
	
	private void give2CardsToPlayers(){
		for(int i = 0; i < 2; i++){
			for(Player p : players){
				p.giveCard(deck.getCard());
			}
		}
	}
	
	private void giveTableCards(String set){
		if(set == "flop"){
			tableCards.addCard(deck.getCard());
			tableCards.addCard(deck.getCard());
			tableCards.addCard(deck.getCard());
		}
		else if(set == "turn" || set == "river"){
			tableCards.addCard(deck.getCard());
		}
	}
	
	private void getResult(Player p){
		
		int[] result;
		result = pattern.getResult(p.getHand());
	}
	
	private void findWinner(){
		pattern = new Pattern(tableCards);
		for (Player p : players){
			getResult(p);
		}
	}
	
}
