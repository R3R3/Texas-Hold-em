package cards.and.stuff;

public interface DeckBuilder {
	
	// void createDeck() throws CardException;
	Deck getDeck() throws CardException;
	void resetDeck() throws CardException; 
}
