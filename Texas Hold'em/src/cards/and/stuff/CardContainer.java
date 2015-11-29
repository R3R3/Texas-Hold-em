package cards.and.stuff;

import java.util.LinkedList;
import java.util.List;

public abstract class CardContainer {
	protected LinkedList <Card> cards;
	
	public CardContainer (){
		this.cards = new LinkedList<Card>();
	}
	
	public CardContainer (List<Card> cards){
		this.cards = (LinkedList<Card>) cards;
	}
	
	public void giveCardTo(CardContainer con){
		Card card = cards.removeFirst();
		con.addCard(card);
	}
	
	private void addCard(Card card){
		cards.add(card);
	}
	
	public void reset(){
		cards = new LinkedList<Card>();
	}
}
