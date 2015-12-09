package network;

import table.Table;

public class Game {

	public GameParameters parameters = new GameParameters();
	
	public Table table;
	//End of the game
	public boolean isFinished = false;
	
	public Game(Table table) {
		this.table = table;
	}
	
	public void StartGame(){
		
	}
	
	private void EndGame() {
		isFinished = true;
	}
	
}
