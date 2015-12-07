package table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import cards.and.stuff.Coins;

public class Player extends Thread{

	private Socket socket;
	private Coins coins;
	private MyHand myhand;
	private int ID;
	private BufferedReader input;
    private PrintWriter output;
    //nie wiem czy siê przyda ale póki co jest. Player wie o istnieniu innych, a na swoim miejscu znajduje nulla.
    public Player[] opponents;

    
	public boolean isDealer = false;
	
	Player (int coins, int ID, Socket socket) 
	{
		this.coins = new Coins(coins);
		this.ID = ID;
		myhand = new MyHand();
		this.socket = socket;
		try{
		input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println("WELCOME PLAYER " + ID);
		output.println("MESSAGE Waiting for other players to connect..");
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
				if (isDealer) {
					output.println("MESSAGE Your turn !");
				}
			}
			
		}
		/*
		catch (IOException e) {
			System.out.println("Dead Player :(" + e);
		}
		*/
		finally {
			try{socket.close();} catch(IOException e) {}
		}
	}
}
