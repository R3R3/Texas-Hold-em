package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import table.Player;
import table.Table;

public class PokerServer {
	
	static int TIMEOUT = 10;

	public static void main(String[] args) throws Exception 
	{
		
		ServerSocket socket;
		try {
			socket = getSocket(1234);
		} catch (ServerNotCreated e) {
			return;
		}
		System.out.println("Server is running");
		int amount, basecash, smallblind, bigblind;
		GameMode mode;
		
		try{
			if(args.length < 4 || args.length > 5){System.out.println("args error");return;}
			
			amount = Integer.parseInt(args[0]);
			basecash = Integer.parseInt(args[1]);
			smallblind = Integer.parseInt(args[2]);
			bigblind = Integer.parseInt(args[3]);
			
			if(amount < 2 || amount > 11 || smallblind <= 0 || bigblind < smallblind || basecash <= 0 + bigblind){
			System.out.println("incompatible data");
			return;	
			}
			
			try {
			mode = GameMode.setMode(args[4]);
			}
			catch(ArrayIndexOutOfBoundsException e){
				// not given -> setting default
				mode = GameMode.setDefault();
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
			
			//game needs only our table and chosen mode plus wages
			Game game = new Game(t);
			game.parameters.setMode(mode);
			game.parameters.setSmallBlind(smallblind);
			game.parameters.setBigBlind(bigblind);
			game.StartGame();
			//after that we will reach finally block
			
		} 
		catch (NumberFormatException e) {
			System.out.println("data error");
			throw new NumberFormatException("data error");
		} finally {
			finalize(socket);
		}
	}
	
	protected static ServerSocket getSocket(int PORT) throws ServerNotCreated {
		try {
			return new ServerSocket(PORT);
		} catch (IOException e) {
			throw new ServerNotCreated(e.getMessage());
		}
	}

	protected static void finalize(ServerSocket socket){
		System.out.println("Closing server socket");
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
