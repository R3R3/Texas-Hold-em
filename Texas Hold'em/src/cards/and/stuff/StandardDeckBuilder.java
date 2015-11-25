package cards.and.stuff;

public class StandardDeckBuilder implements DeckBuilder{
	private Deck deck;
	
	@Override
	public void createDeck() throws CardException {
		deck = new Deck();
		for(int i = 0; i< 13 ; i++)
		{
			deck.addCard(new Card(i,Color.CLUBS));				
			deck.addCard(new Card(i,Color.DIAMOND));
			deck.addCard(new Card(i,Color.HEARTS));
			deck.addCard(new Card(i,Color.SPADES));
		}
		deck.shuffle();		
	}
	
	@Override
	public Deck getDeck() throws CardException {
		if(deck == null)
			createDeck();
		return deck;
	}
	
	@Override
	public void resetDeck() throws CardException {
		deck = null;
	}

}
