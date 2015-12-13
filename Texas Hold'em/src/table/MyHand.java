package table;

import cards.and.stuff.CardContainer;

public class MyHand extends CardContainer {
	
	MyHand(){
	}
	
	public void sendToCheck(CheckPatterns p){
		p.getHandCards(cards);
	}
	
	public String getString(int i){
		return cards.get(i).toString();
	}
	
}
