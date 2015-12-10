package network;

import java.util.ArrayList;

import table.Player;
import table.Table;
import table.TableCardsTurns;

public class Game {

	public GameParameters parameters = new GameParameters();
	
	public Table table;
	//End of the game
	public boolean isFinished = false;
	
	public Game(Table table) {
		this.table = table;
	}
	
	
	public void StartGame(){
		
		while(true){
			round();
			int players_with_coins=0;
			for(Player p : table.players){
				if(p.getCoins() > 0){
					players_with_coins++;
				}
			}
			if(players_with_coins==1){break;};
		}
		
		
	}
	
	/*
	 * order:
	 * set dealer (round = 0 -> random , else -> next player)
	 * setting small blind and big blind
	 * give 2 cards
	 * auction #1 (small blind starts all auctions
	 * give "flop"
	 * auction #2
	 * give "turn"
	 * auction #3
	 * give "river"
	 * auction #4
	 * Each player shows his hand
	 * Finding highest set
	 * Winner takes whole pot
	 */
	private void round() {
		parameters.setRound();
		if(parameters.getRound() == 1){
			parameters.setActualDealer(table.getRandomDealer());
		}
		else{
			parameters.setActualDealer(table.setNextDealer());
		}
		
		table.give2CardsToPlayers();
		auction();
		table.giveTableCards(TableCardsTurns.FLOP);
		auction();
		table.giveTableCards(TableCardsTurns.TURN);
		auction();
		table.giveTableCards(TableCardsTurns.RIVER);
		auction();
		ArrayList<Player> winners = new ArrayList<Player>();
		winners = table.findWinner();
		sendMoney(winners);
	}
	
	private void sendMoney(ArrayList<Player> winners) {
		if(winners.size() == 1){
			//z pota do coinsów tego jednego gracza
		}
		else{
			//z pota do coinsów graczy podzielone przez iloœc zwyciêzców
		}
	}


	private void auction(){
		//logika aukcji
		
	}

	//probably unused due to new exiting condition
	private void EndGame() {
		isFinished = true;
	}
	
}
