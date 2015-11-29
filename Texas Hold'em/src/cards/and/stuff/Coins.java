package cards.and.stuff;

public class Coins {
	int coins;
	public Coins(){
		coins=0;
	}
	
	public Coins(int c){
		coins=c;
	}
	
	public void giveCoinsTo(Coins con, int i) throws NotEnoughCoins{
		if(coins<i)
			throw new NotEnoughCoins();
		coins-=i;
		con.addCoins(i);
	}
	
	private void addCoins(int i){
		coins+=i;
	}
	
	public int amount(){
		return coins;
	}
}
