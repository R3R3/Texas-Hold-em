package table;

import cards.and.stuff.CardContainer;

public class TableCards extends CardContainer {
	
	TableCards(){
	}
	
	public void sendToCheck(CheckPatterns p){
		p.getTableCards(cards);
	}

	
}
