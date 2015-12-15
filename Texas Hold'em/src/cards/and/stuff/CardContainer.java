package cards.and.stuff;

import java.util.LinkedList;

public abstract class CardContainer {
	protected LinkedList <Card> cards;
	
	public CardContainer (){
		this.cards = new LinkedList<Card>();
	}
	
	/*public CardContainer (List<Card> cards){
		this.cards = (LinkedList<Card>) cards;
	}*/
	
	public boolean giveCardTo(CardContainer con){
		if(cards.isEmpty())
			return false;
		Card card = cards.removeFirst();
		con.addCard(card);
		return true;
	}
	
	private void addCard(Card card){
		cards.add(card);
	}
	
	public void reset(){
		cards = new LinkedList<Card>();
	}
}