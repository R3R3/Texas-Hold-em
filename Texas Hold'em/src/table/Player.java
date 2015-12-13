package table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import cards.and.stuff.Coins;
import cards.and.stuff.NotEnoughCoins;

public class Player extends Thread{

	private Socket socket;
	public Coins coins;
	private MyHand myhand;
	private int ID;
	public BufferedReader input;
    public PrintWriter output;
    //nie wiem czy siê przyda ale póki co jest. Player wie o istnieniu innych, a na swoim miejscu znajduje nulla.
    public Player[] opponents;
    private PlayerState state;
    //aktualna wartoœ zak³adu
    public int actualWage = 0;
    //wartoœ pot'a z mojej perspektywy
    public int tempPot=0;
    //wysokoœ najwy¿szego zak³adu
    public int highestBet=0;
    public boolean isDealer = false;
    public boolean isAll_in = false;
    
    
	public PlayerState getPlayerState() {
		return state;
	}

	public void setPlayerState(PlayerState state) {
		this.state = state;
	}
	
	Player (int coins, int ID, Socket socket) 
	{
		this.coins = new Coins(coins);
		this.ID = ID;
		setPlayerState(PlayerState.INACTIVE);
		myhand = new MyHand();
		this.socket = socket;
		try{
		input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println("WELCOME PLAYER " + ID);
		output.println("MESSAGE Waiting for other players to connect..");
		output.println("INACTIVE");
		}
		catch (IOException e) {
			System.out.println("Dead Player :(" + e);
		}
	}
	
	public int getCoins (){
		return coins.amount();
	}
	
	public int getID(){
		return ID;
	}
	
	protected MyHand getHand(){
		return myhand;
	}
	
	private int getPlayerNum() {
		return opponents.length;
	}
	
	public void run() 
	{
		
		try {
			output.println("MESSAGE All players connected");
			String response = "";
			try {
				sleep(500);
			} catch (InterruptedException e) {}
			System.out.println("trying to send amount and setcash");
			output.println("AMOUNT " + Integer.toString(getPlayerNum()));
			try {
				sleep(500);
			} catch (InterruptedException e) {}
			output.println("SETCASH " + Integer.toString(getCoins()));
			
			while(true){
				try {
					response = input.readLine();
				} catch (IOException e) {
					System.out.println("Error while reading input from client");
				}
				if (state == PlayerState.ACTIVE) {
					output.println("MESSAGE Your turn !");
					if(response.startsWith("BET")){
						//player pressed bet button
						try {
							int amount = Integer.parseInt(response.substring(4));
							raise(amount);
							state = PlayerState.INACTIVE;
						} catch (NotEnoughCoins e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(response.startsWith("RAISE")){
						//pressed raise button
						try {
							int amount = Integer.parseInt(response.substring(6));
							raise(amount);
							state = PlayerState.INACTIVE;
						} catch (NotEnoughCoins e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(response.startsWith("CALL")) {
						//pressed call button
						try {
							call();
							state = PlayerState.INACTIVE;
						} catch (NotEnoughCoins e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(response.startsWith("FOLD")){
						//pressed fold button
						//just becomes out of game for a moment, like suspended
						state = PlayerState.FOLDED;
					}
					else if(response.startsWith("CHECK")){
						//pressed check button
						//none game-changing action
						state = PlayerState.INACTIVE;
					}
					else if(response.startsWith("ALL-IN")){
						//pressed all-in button and shit
						isAll_in = true;
						try {
							coins.giveCoinsTo(coins, coins.amount());
							state = PlayerState.INACTIVE;
						} catch (NotEnoughCoins e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} 
				
			}
			
		} finally {
			try{socket.close();} catch(IOException e) {}
		}
	}

	private void raise(int amount) throws NotEnoughCoins {
		// TODO: call to even the highest bet and then raise for amount
		//wyrównuje najpierw jeœli potrzeba
		call();
		//zwiêkszam najwiêkszy zak³ad i mój pot o kwotê podbicia
		highestBet = actualWage + amount;
		actualWage = highestBet;
		tempPot += amount;
		
	}

	private void call() throws NotEnoughCoins {
		// TODO: even up the highest bet
		// ró¿nica miêdzy najwy¿szym zak³adem a tym co ja da³em
		int difference = highestBet - actualWage;
		actualWage = highestBet;
		//mój pot zwiêkszam o róznicê, stó³ dokona przelewu
		tempPot += difference;
	}

	public void notyfyAboutCards() {
		// TODO: inform about cards
		output.println("CARD 0 " + myhand.getString(0));
		output.println("CARD 1 " + myhand.getString(1));
	}
	
}
