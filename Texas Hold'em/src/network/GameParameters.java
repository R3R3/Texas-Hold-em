package network;

public class GameParameters {

	private GameMode mode;
	private int PlayerNum;
	private int round=0;
	private int SmallBlind;
	private int BigBlind;
	private int actualDealer;
	private int actualSB;
	private int actualBB;
	private int fixedbounds;
	
	public int getFixedbounds() {
		return fixedbounds;
	}

	public void setFixedbounds(int fixedbounds) {
		this.fixedbounds = fixedbounds;
	}

	public int getPlayerNum() {
		return PlayerNum;
	}

	public void setPlayerNum(int playerNum) {
		PlayerNum = playerNum;
	}

	public int getActualDealer() {
		return actualDealer;
	}

	public void setActualDealer(int actualDealer) {
		this.actualDealer = actualDealer;
	}

	public int getActualSB() {
		return actualSB;
	}

	public void setActualSB(int actualSB) {
		this.actualSB = actualSB;
	}

	public int getActualBB() {
		return actualBB;
	}

	public void setActualBB(int actualBB) {
		this.actualBB = actualBB;
	}

	public int getSmallBlind() {
		return SmallBlind;
	}

	public void setSmallBlind(int smallBlind) {
		SmallBlind = smallBlind;
	}

	public int getBigBlind() {
		return BigBlind;
	}

	public void setBigBlind(int bigBlind) {
		BigBlind = bigBlind;
	}

	public int getRound() {
		return round;
	}

	public void setRound() {
		round ++;
	}

	public GameMode getMode() {
		return mode;
	}

	public void setMode(GameMode mode) {
		this.mode = mode;
	}

	GameParameters(){	
	}
}
