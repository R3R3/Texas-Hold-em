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
    //nie wiem czy si� przyda ale p�ki co jest. Player wie o istnieniu innych, a na swoim miejscu znajduje nulla.
    public Player[] opponents;
    private PlayerState state;
    //aktualna warto� zak�adu
    public int actualWage = 0;
    //warto� pot'a z mojej perspektywy
    public int tempPot=0;
    //wysoko� najwy�szego zak�adu
    public int highestBet=0;
    public boolean isDealer = false;
    public boolean isAll_in = false;
    
    
	public synchronized PlayerState getPlayerState() {
		return state;
	}

	public synchronized void setPlayerState(PlayerState state) {
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
	
	public synchronized int getCoins (){
		return coins.amount();
	}
	
	public synchronized int getID(){
		return ID;
	}
	
	protected synchronized MyHand getHand(){
		return myhand;
	}
	
	private synchronized int getPlayerNum() {
		return opponents.length;
	}
	
	public void run() 
	{
		
		try {
			output.println("MESSAGE All players connected");
			System.out.println(ID + " : trying to send amount and setcash");
			output.println("AMOUNT " + Integer.toString(getPlayerNum()));
			output.println("SETCASH " + Integer.toString(getCoins()));
			
			while(true){
					String response = input.readLine();
				
				if (state == PlayerState.ACTIVE) {
					output.println("MESSAGE Your turn !");
					if(response.startsWith("BET")){
						//player pressed bet button
						try {
							int amount = Integer.parseInt(response.substring(4));
							raise(amount);
							state = PlayerState.INACTIVE;
							output.println("INACTIVE");
						} catch (NotEnoughCoins e) {
							e.printStackTrace();
						}
					}
					else if(response.startsWith("RAISE")){
						//pressed raise button
						try {
							int amount = Integer.parseInt(response.substring(6));
							raise(amount);
							state = PlayerState.INACTIVE;
							output.println("INACTIVE");
						} catch (NotEnoughCoins e) {
							e.printStackTrace();
						}
					}
					else if(response.startsWith("CALL")) {
						//pressed call button
						try {
							call();
							state = PlayerState.INACTIVE;
							output.println("INACTIVE");
						} catch (NotEnoughCoins e) {
							e.printStackTrace();
						}
					}
					else if(response.startsWith("FOLD")){
						//client pressed fold button
						//just becomes out of game for a moment, like suspended
						state = PlayerState.FOLDED;
						output.println("INACTIVE");
					}
					else if(response.startsWith("CHECK")){
						//pressed check button
						//none game-changing action
						state = PlayerState.INACTIVE;
						output.println("INACTIVE");
					}
					else if(response.startsWith("ALL-IN")){
						//pressed all-in button and shit
						isAll_in = true;
						try {
							coins.giveCoinsTo(coins, coins.amount());
							state = PlayerState.INACTIVE;
							output.println("INACTIVE");
						} catch (NotEnoughCoins e) {
							e.printStackTrace();
						}
					}
				} 
				
			}
			
		} catch (IOException e) {
			System.out.println("Player " + ID +  " Died: " + e);
		} finally {
			try{socket.close();} catch(IOException e) {}
		}
	}

	private synchronized void raise(int amount) throws NotEnoughCoins {
		call();
		actualWage += amount;
		tempPot += amount;
		highestBet +=amount;
	}

	private synchronized void call() throws NotEnoughCoins {
		int difference = highestBet - actualWage;
		tempPot += difference;
		actualWage = highestBet;
		//output.println("WAGE " + Integer.toString(actualWage));
	}

	public synchronized void notyfyAboutCards() {
		output.println("CARD 0 " + myhand.getString(0));
		output.println("CARD 1 " + myhand.getString(1));
	}
	
}
