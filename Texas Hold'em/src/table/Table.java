package table;

import cards.and.stuff.*;

public class Table {

	public CardContainer tableCards;
	public Deck deck;
	Player[] players;
	
	private void prepareDeck(DeckBuilder builder){
		try {
			deck = builder.getDeck();
		} catch (CardException e) {
			e.getMessage();
		}
	}
	
	private void createPlayers(int amount, int basecash) throws PlayerException{
		for(int i=0; i< amount; i++){
			players[i] = new Player(basecash);
		}
	}
	
	private void prepareTableCards(){
		tableCards = new TableCards();
		
	}
	
	
	
}
