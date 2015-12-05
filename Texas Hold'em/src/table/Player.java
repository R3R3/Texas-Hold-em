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

	
	public boolean isDealer = false;
	
	Player (int coins, int ID, Socket socket) {
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
	
	public void run() {
		
		try {
			output.println("MESSAGE All players connected");
			
			while(true){
				if (isDealer) {
					output.println("MESSAGE Your move");
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
