package network;

import java.io.IOException;
import java.net.ServerSocket;

import cards.and.stuff.StandardDeckBuilder;
import table.Table;

public class PokerServer {

	public static void main(String[] args) throws Exception {
		
		ServerSocket socket = new ServerSocket(1234);
		System.out.println("Server is running");
		int amount, basecash;

		try{
			if(args.length != 2){System.out.println("args error");}
			
			amount = Integer.parseInt(args[0]);
			basecash = Integer.parseInt(args[1]);
			if(amount < 2 || amount > 11 || basecash <= 0){
			return;	
			}
			
			Table t = new Table(amount);
			
			for(int i=0;i<amount;i++){
				System.out.println("Waiting for player " + i);
				t.createPlayers(i, basecash,socket.accept());
			}
			System.out.println("Running threads");
			for(int i=0;i<amount;i++){
				t.players[i].start();
				
			}
			
			while(true){
				
			}
			
			
		} 
		catch (NumberFormatException e) {
			e.getMessage();
		} finally {
			System.out.println("Closing server socket");
			socket.close();
		}
		
		
	}

	private static void substring(int i) {
		// TODO Auto-generated method stub
		
	}

}
