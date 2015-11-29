package cards.and.stuff;

import java.util.LinkedList;
import java.util.Random;

public class Deck extends CardContainer{
	
	protected Deck(){
	}
	
	public boolean addCard(Card card){
		cards.add(card);
		return true;
	}
	
	protected synchronized void shuffle(){
		Card[] tmp = new Card[cards.size()];
		cards.toArray(tmp);
		cards = new LinkedList<Card>();
		final Random gen = new Random();
		int l;
		for(int k = tmp.length ; k>1; k--){
			l = gen.nextInt(k);
			cards.add(tmp[l]);
			tmp[l]=tmp[k-1];
		}
		cards.add(tmp[0]);
	}
}
