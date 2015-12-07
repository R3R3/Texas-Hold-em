package cards.and.stuff;

import cards.and.stuff.Card;
import cards.and.stuff.Color;

public class CardGen {

	public static Card genCard(int i,Color c){
		return new Card(i,c);
	}
}

