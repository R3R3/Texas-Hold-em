package table;

import cards.and.stuff.Card;

public class Player {

	private int coins;
	private MyHand myhand;
	private int ID;
	
	//public boolean isDealer = false;
	
	Player (int coins, int ID) {
		this.coins = coins;
		this.ID = ID;
	}
	
	public int getCoins (){
		return coins;
	}
	
	public void giveCard(Card card){
		myhand.addCard(card);
	}
	
	protected MyHand getHand(){
		return myhand;
	}
}
