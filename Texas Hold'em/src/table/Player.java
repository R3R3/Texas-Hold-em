package table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import cards.and.stuff.Coins;

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
    public Coins actualWage;
    public boolean isDealer = false;

    
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
		actualWage = new Coins();
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
					}
					else if(response.startsWith("RAISE")){
						//pressed raise button
					}
					else if(response.startsWith("CALL")) {
						//pressed call button
					}
					else if(response.startsWith("FOLD")){
						//pressed fold button
					}
					else if(response.startsWith("CHECK")){
						//pressed check button
					}
					else if(response.startsWith("ALL-IN")){
						//pressed all-in button
					}
				} 
				
			}
			
		} finally {
			try{socket.close();} catch(IOException e) {}
		}
	}
}
