package table;

import java.util.LinkedList;

import cards.and.stuff.Card;
import cards.and.stuff.CardContainer;

public class TableCards extends CardContainer {

	private LinkedList <Card> cards;
	
	TableCards(){
		super();
	}
	
	
	/*
	 * Ze sto³u kart nie zabieramy pojedynczo?
	 */
	@Override
	public Card getCard(){
		return null;
	}
	
}
