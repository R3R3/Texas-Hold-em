package network;

public class GameParameters {

	private GameMode mode;
	private int PlayerNum;
	private int round=0;
	private int SmallBlind;
	private int BigBlind;
	
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
