package table;

import cards.and.stuff.Coins;

public class Player {

	
	private Coins coins;
	private MyHand myhand;
	private int ID;
	
	//public boolean isDealer = false;
	
	Player (int coins, int ID) {
		this.coins = new Coins(coins);
		this.ID = ID;
		myhand = new MyHand();
	}
	
	public int getCoins (){
		return coins.amount();
	}
	
	protected MyHand getHand(){
		return myhand;
	}
}
