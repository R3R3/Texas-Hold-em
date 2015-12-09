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
		int amount, basecash;
		GameMode mode;
		
		try{
			if(args.length < 2 || args.length > 3){System.out.println("args error");return;}
			
			amount = Integer.parseInt(args[0]);
			basecash = Integer.parseInt(args[1]);
			
			if(amount < 2 || amount > 11 || basecash <= 0){
			System.out.println("incompatible data");
			return;	
			}
			
			try {
			mode = GameMode.setMode(args[2]);
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
			
			Game game = new Game(t);
			do {
				game.parameters.setMode(mode);
				game.StartGame();
				
				if (game.isFinished){
					break;
				}
			} while (true);
			
			
		} 
		catch (NumberFormatException e) {
			System.out.println("data error");
		} finally {
			finalize(socket);
		}
	}
	
	private static ServerSocket getSocket(int PORT) throws ServerNotCreated {
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
