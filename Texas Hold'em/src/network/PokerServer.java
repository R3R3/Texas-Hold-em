package network;

import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import table.Player;
import table.Table;

public class PokerServer {
	
	static int TIMEOUT = 10;

	public static void main(String[] args) throws Exception 
	{
		
		ServerSocket socket = new ServerSocket(1234);
		System.out.println("Server is running");
		int amount, basecash;

		try{
			if(args.length != 2){System.out.println("args error");return;}
			
			amount = Integer.parseInt(args[0]);
			basecash = Integer.parseInt(args[1]);
			if(amount < 2 || amount > 11 || basecash <= 0){
			System.out.println("incompatible data");
			return;	
			}
			
			Table t = new Table(amount);
			
			socket.setSoTimeout(1000*TIMEOUT);
			for(int i=0;i<amount;i++){
				System.out.println("Waiting for player " + i);
				try {
					t.createPlayers(i, basecash,socket.accept());
				} catch(SocketTimeoutException e) {
					System.out.println("Accept: Timeout !!!");
					//TODO: Here comes bot player replacing timeouted client 
				}
			}
			System.out.println("Setting opponents");
			for(int i=0;i<amount;i++){
				t.players[i].opponents = new Player[amount];
				for(int k=0;k<amount;k++){
					if(i==k){t.players[i].opponents[i] = null; continue;}
					t.players[i].opponents[k] = t.players[k];
				}
			}
			
			
			System.out.println("Running threads");
			for(int i=0;i<amount;i++){
				t.players[i].start();
				
			}
			
			while(true){
				//TODO: Game Logic
			}
			
			
		} 
		catch (NumberFormatException e) {
			System.out.println("data error");
		} finally {
			System.out.println("Closing server socket");
			socket.close();
		}
		
		
	}

}
