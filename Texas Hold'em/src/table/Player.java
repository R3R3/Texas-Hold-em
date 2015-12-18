package table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import cards.and.stuff.Coins;
import cards.and.stuff.NotEnoughCoins;

public class Player extends Thread{

	public Socket socket;
	public Coins coins;
	private MyHand myhand;
	private int ID;
	private int playerNum;
	protected BufferedReader input;
    public PrintWriter output;
    private PlayerState state;
    //aktualna wartoœ zak³adu
    public int actualWage = 0;
    //wartoœ pot'a z mojej perspektywy
    public int tempPot=0;
    //wysokoœ najwy¿szego zak³adu
    public int highestBet=0;
    public boolean isDealer = false;
    public boolean isAll_in = false;
    public boolean madeMove = false;
    
    
	public PlayerState getPlayerState() {
		return state;
	}

	public void setPlayerState(PlayerState state) {
		this.state = state;
	}
	
	Player (int coins, int ID, Socket socket, int playerNum) 
	{
		this.coins = new Coins(coins);
		this.ID = ID;
		this.playerNum = playerNum;
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
	
	public void run() 
	{
		
		try {
			output.println("MESSAGE All players connected");
			System.out.println(ID + " : trying to send amount and setcash");
			output.println("AMOUNT " + playerNum);
			output.println("SETCASH " + Integer.toString(getCoins()));
			
			while(true){
					String response = input.readLine();
				
				/*if (state == PlayerState.ACTIVE) {
					output.println("MESSAGE Your turn !");*/
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
				//} 
				
			}
			
		} catch (IOException e) {
			System.out.println("Player " + ID +  " Died: " + e);
		} catch (NullPointerException e) {
			//player disappeared
			state = PlayerState.QUITED;
		} finally {
			try{socket.close();} catch(IOException e) {}
		}
	}

	protected synchronized void raise(int amount) throws NotEnoughCoins {
		call();
		actualWage += amount;
		tempPot += amount;
		highestBet +=amount;
	}

	protected synchronized void call() throws NotEnoughCoins {
		int difference = highestBet - actualWage;
		tempPot += difference;
		actualWage = highestBet;
	}

	protected void notyfyAboutCards() {
		output.println("CARD 0 " + myhand.getString(0));
		output.println("CARD 1 " + myhand.getString(1));
	}
	
}
