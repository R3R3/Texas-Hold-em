package network;

import java.util.ArrayList;

import javax.sql.rowset.CachedRowSet;

import cards.and.stuff.NotEnoughCoins;
import table.Player;
import table.PlayerState;
import table.Table;
import table.TableCardsTurns;

public class Game {

	public GameParameters parameters = new GameParameters();
	
	public Table table;
	//End of the game
	public boolean isFinished = false;
	
	public Game(Table table) {
		this.table = table;
		parameters.setPlayerNum(table.num_Players);
	}
	
	
	public void StartGame() throws NotEnoughCoins{
		
		while(true){
			round();
			int players_with_coins=0;
			for(Player p : table.players){
				if(p.getCoins() > 0 && p.getPlayerState() != PlayerState.QUITED){
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
	private void round() throws NotEnoughCoins {
		parameters.setRound();
		if(parameters.getRound() == 1){
			parameters.setActualDealer(table.getRandomDealer());
		}
		else{
			parameters.setActualDealer(table.setNextDealer());
		}
		setBlinds();
		table.give2CardsToPlayers();
		table.notifyAboutCards(); //to implement
		auction();
		table.giveTableCards(TableCardsTurns.FLOP);
		table.notifyAboutTable(TableCardsTurns.FLOP); //to implement
		auction();
		table.giveTableCards(TableCardsTurns.TURN);
		table.notifyAboutTable(TableCardsTurns.TURN);
		auction();
		table.giveTableCards(TableCardsTurns.RIVER);
		table.notifyAboutTable(TableCardsTurns.RIVER);
		auction();
		ArrayList<Player> winners = new ArrayList<Player>();
		winners = table.findWinner();
		sendMoney(winners);
		//reveal cards if at least 2 players are still in the game (not folded) 
	}
	
	private void sendMoney(ArrayList<Player> winners) throws NotEnoughCoins {
		if(winners.size() == 1){
			table.pot.giveCoinsTo(winners.get(0).coins, table.pot.amount());
		}
		else{
			int amount = table.pot.amount();
			for(Player p : winners){
				if(table.pot.amount() % winners.size() == 0){
					table.pot.giveCoinsTo(p.coins, amount/winners.size());
				}
				else {
					int reszta = table.pot.amount() % winners.size();
					amount -= reszta;
					table.pot.giveCoinsTo(p.coins, amount/winners.size());
					table.pot.reset();
				}
			}
		}
	}

	private void auction(){
		//logika aukcji
		int i = parameters.getActualSB() + 1;
		while (true) {
			if(i == parameters.getPlayerNum()){i=0;}
			table.players[i].setPlayerState(PlayerState.ACTIVE);
			table.players[i].output.println("ACTIVE");
			while (table.players[i].getPlayerState() == PlayerState.ACTIVE){
			//waiting for and action which will change the state of a player	
			}
			
			
		}
	}
	
	private void setBlinds(){
		int i;
		for(i=parameters.getActualDealer();true;++i){
			if(i== parameters.getPlayerNum()){i=0;}
			if(table.players[i].getPlayerState() != PlayerState.QUITED){
				parameters.setActualSB(i);
				break;
			}
		}
		
		for(i=parameters.getActualSB();true;++i){
			if(i==parameters.getPlayerNum()){i=0;}
			if(table.players[i].getPlayerState() != PlayerState.QUITED){
				parameters.setActualBB(i);
				break;
			}
		}
	}

	//probably unused due to new exiting condition
	private void EndGame() {
		isFinished = true;
	}
	
}
