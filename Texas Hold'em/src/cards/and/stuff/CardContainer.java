package cards.and.stuff;

import java.util.LinkedList;
import java.util.List;

public class CardContainer {
	private LinkedList <Card> cards;
	
	public CardContainer (){
		cards = new LinkedList<Card>();
	}
	
	public CardContainer (List<Card> cards){
		this.cards = (LinkedList<Card>) cards;
	}
		
	public boolean addCard(Card card){
		if(cards.contains(card)){
			return false;
		}
		else{
			cards.add(card);
			return true;
		}
	}
	
	public CardContainer getAllCards(){
		return this;
	}
	
	public Card getCard(){
		return cards.removeFirst();
	}
}
