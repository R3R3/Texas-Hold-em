package network;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

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
		//blinds are set before start, by the server	
	}
	
	
	public void StartGame() throws NotEnoughCoins, InterruptedException{
		
		while(true){
			table.canWinPlayers = new ArrayList<Player>();
			for(Player p: table.players)
				table.canWinPlayers.add(p);
			round();
			int players_with_coins=0;
			for(Player p : table.players){
				if(p.getCoins() >= parameters.getBigBlind() && p.getPlayerState() != PlayerState.QUITED){
					players_with_coins++;
					table.canWinPlayers.add(p);
				}
			}
			if(players_with_coins < 2){break;};
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
	private void round() throws NotEnoughCoins, InterruptedException {
		parameters.setRound();
		resetBoard();
		if(parameters.getRound() == 1){
			parameters.setActualDealer(table.getRandomDealer());
		}
		else{
			parameters.setActualDealer(table.setNextDealer());
		}
		table.notifyDealer(parameters.getActualDealer());
		//dispatching blinds. may be impossible to do when sb is too poor
		try{
		setBlinds();
		}
		catch(NotEnoughCoins e) {
			return;
		}
		table.give2CardsToPlayers();
		table.notifyAboutCards();
		table.setAllNotMoved();
		table.firstBet = false;	
		auction(true);
		table.giveTableCards(TableCardsTurns.FLOP);
		table.notifyAboutTable(TableCardsTurns.FLOP); 
		table.setAllNotMoved();
		table.notifyBet(true);
		if(parameters.getMode() == GameMode.FIXEDLIMIT){ table.setFixedbounds(parameters.getFixedbounds()); table.notifyfixedunlock();}
		auction(false);
		table.giveTableCards(TableCardsTurns.TURN);
		table.notifyAboutTable(TableCardsTurns.TURN);
		table.setAllNotMoved();
		table.notifyBet(true);
		if(parameters.getMode() == GameMode.FIXEDLIMIT){ table.setFixedbounds(parameters.getFixedbounds()); table.notifyfixedunlock();}
		auction(false);
		table.giveTableCards(TableCardsTurns.RIVER);
		table.notifyAboutTable(TableCardsTurns.RIVER);
		table.setAllNotMoved();
		table.notifyBet(true);
		if(parameters.getMode() == GameMode.FIXEDLIMIT){ table.setFixedbounds(parameters.getFixedbounds()); table.notifyfixedunlock();}
		auction(false);
		TimeUnit.MILLISECONDS.sleep(500);
		findWinners();
		table.finalizeCash();
		revealCards();
		TimeUnit.SECONDS.sleep(15);
		table.resetPot();
		
	}
	
	protected void revealCards() {
		int interested_players=0;
		for(Player p : table.players){
			if(p.getPlayerState() != PlayerState.FOLDED && p.getPlayerState() != PlayerState.QUITED){
				interested_players++;
			}
		}
		if(interested_players < 2){
			return;
		}
		else {
			for(Player p : table.players){
				if(p.getPlayerState() != PlayerState.FOLDED && p.getPlayerState() != PlayerState.QUITED){
					table.revealCards(p.getID());
				}
			}
		}
		
	}


	protected void resetBoard() throws NotEnoughCoins, InterruptedException {
		table.resetTableCards();
		table.resetDeck();
		table.sendReset();
		
	}


	public void findWinners() throws NotEnoughCoins {
		TreeMap<Integer,ArrayList<Player>> map = makeMap();
		fillMap(map);
		ArrayList<Player> tmp;
		int coins;
		int prevKey = 0;
		int wage = map.firstKey().intValue();
		while(wage != -1){
			coins = 0;
			int dif = wage - prevKey;
			coins = getPotValue(dif);
			table.canWinPlayers = map.get(wage);
			tmp = table.findWinner();
			sendMoney(tmp,coins);
			prevKey = wage;
			wage = map.higherKey(wage)!=null?map.higherKey(wage):-1;
		}
	}
	
	private void sendMoney(ArrayList<Player> winners, int pot) throws NotEnoughCoins{
		int amount = pot;
		amount -= pot% winners.size();
		for(Player p : winners){	
			table.pot.giveCoinsTo(p.coins, amount/winners.size());
			try{
				p.output.println("MESSAGE You Win");
			}
			catch(NullPointerException e){
				System.out.println(p.getID()+ " won "+ amount/winners.size());
			}
		}
	}
	
	private int getPotValue(int dif){
		int coins = 0;
		for(Player p : table.players){
			if(p.actualWage >= dif){
				coins+=dif;
				p.actualWage-=dif;
			}
			else
			{
				coins+=p.actualWage;
				p.actualWage = 0;
			}
		}
		return coins;
	}
	
	private TreeMap<Integer,ArrayList<Player>> makeMap(){
		TreeMap<Integer,ArrayList<Player>> map = new TreeMap<Integer,ArrayList<Player>> ();
		int wage;
		for(Player p : table.players){
			wage = p.actualWage;
			if(p.isAll_in || wage == table.highest_bet)
			{
				boolean isIn = false;
				for(Integer in : map.keySet()){
					if(in.intValue() == wage){
						isIn= true;
					}
				}
				if (!isIn){
					map.put(new Integer(wage), new ArrayList<Player>());
				}
			}
		}
		return map;
	}
	
	private void fillMap(TreeMap<Integer,ArrayList<Player>> map){
		int wage = -1;
		for(Player p : table.players){
			wage = p.actualWage;
			if(p.isAll_in || wage == table.highest_bet)
			{
				for(Integer i: map.keySet()){
					if(i.intValue()<=wage){
						map.get(i).add(p);
					}
				}	
			}
		}
	}

	private void auction(boolean isFirst) throws NotEnoughCoins, InterruptedException{
		//logika aukcji isFirst definiuje pierwsz¹ licytacjê -> inna osoba zaczyna licytacjê
		int i;
		if(isFirst){
			i = parameters.getActualBB() + 1;
		}
		else {
			i = parameters.getActualSB();
		}
		boolean we_play = true;
		while (we_play) {
			if(i == parameters.getPlayerNum()){i=0;}
			if(table.players[i].getPlayerState() == PlayerState.FOLDED 
					|| table.players[i].getPlayerState() == PlayerState.QUITED
					|| table.players[i].isAll_in){i++;continue;}
			table.players[i].setPlayerState(PlayerState.ACTIVE);
			table.players[i].output.println("ACTIVE");
			table.players[i].output.println("MESSAGE Your Turn");
			while (table.players[i].getPlayerState() == PlayerState.ACTIVE){
				TimeUnit.MILLISECONDS.sleep(100);
			}
			table.updatePot(i);
			table.players[i].madeMove = true;
			if(endAuction(i)){
				we_play = false;
			}
			i++;
		}
	}
	
	protected void setBlinds() throws NotEnoughCoins, InterruptedException{
		int i = parameters.getActualDealer();
		
		while(true){
			i++;
			if(i== parameters.getPlayerNum()){i=0;}
			if(table.players[i].getPlayerState() != PlayerState.QUITED){
				
				if(table.players[i].getCoins() < parameters.getSmallBlind()) {
					table.players[i].setPlayerState(PlayerState.QUITED);
					table.canWinPlayers.remove(table.players[i]);
					table.players[i].output.println("MESSAGE You lost: Not enough money to give SmallBlind");
					continue;
				}
			
				parameters.setActualSB(i);
				table.players[i].actualWage = parameters.getSmallBlind();
				table.players[i].tempPot += parameters.getSmallBlind();
				table.players[i].highestBet = parameters.getSmallBlind();
				table.updatePot(i);
				break;
			}
		}
		if(parameters.getMode() == GameMode.FIXEDLIMIT){ table.setFixedbounds(parameters.getFixedbounds()); table.notifyfixedunlock();}
		i = parameters.getActualSB();
		while(true){
			i++;
			if(i== parameters.getPlayerNum()){i=0;}
			if(table.players[i].getPlayerState() != PlayerState.QUITED){
				
				if(table.players[i].getCoins() < parameters.getBigBlind()) {
					table.players[i].setPlayerState(PlayerState.QUITED);
					table.canWinPlayers.remove(table.players[i]);
					table.players[i].output.println("MESSAGE You lost: Not enough money to give BigBlind");
					continue;
				}
				
				parameters.setActualBB(i);
				table.players[i].actualWage = parameters.getBigBlind();
				table.players[i].tempPot += parameters.getBigBlind();
				table.players[i].highestBet = parameters.getBigBlind();
				table.player_with_highest_bet = i;
				table.updatePot(i);
				break;
			}
		}
		
		table.notifyBet(false);
	}

	public boolean endAuction(int actual) {	
		int wage = table.highest_bet;
		for(int i = 0 ; i < table.players.length;i++)
		{
			Player p = table.players[i];
			if(p.getPlayerState()== PlayerState.QUITED ||
					p.getPlayerState()== PlayerState.FOLDED	)
			{
					table.canWinPlayers.remove(p);
					continue;
			}
			else{
				if(p.actualWage != wage && !p.isAll_in)
					return false;
				if(!p.madeMove && (p.getPlayerState()!= PlayerState.QUITED ||
					p.getPlayerState()!= PlayerState.FOLDED	)){
					
					return false;
				}
			}
		}
		
		
		return true;
	}


	public void notifygameMode() {
		table.notifyGameMode(parameters.getMode());
	}


	public void notifyFixedstuff(int fixedraise) {
		table.notifyFixedstuff(fixedraise, parameters.getFixedbounds());
	}
}
