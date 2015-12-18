package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import table.PlayerException;
import table.Table;

public class PokerServer {
	
	int amount, basecash, smallblind, bigblind, fixedraise,fixedbounds;
	GameMode mode;
	int TIMEOUT = 10;
	ServerSocket socket = null;
	
	PokerServer(String[] args){
		
		try{
			if(args.length < 4 || args.length > 7){System.out.println("args error");throw new NumberFormatException("args error");}
			
			amount = Integer.parseInt(args[0]);
			basecash = Integer.parseInt(args[1]);
			smallblind = Integer.parseInt(args[2]);
			bigblind = Integer.parseInt(args[3]);
			
			if(amount < 2 || amount > 11 || smallblind <= 0 || bigblind < smallblind || basecash <= 0 + bigblind){
			System.out.println("incompatible data");
			throw new NumberFormatException("incompatible data");
			}
			
			try {
			mode = GameMode.setMode(args[4]);
			}
			catch(ArrayIndexOutOfBoundsException e){
				// not given -> setting default
				System.out.println("Setting default mode");
				mode = GameMode.setDefault();
			}
			
			if(mode == GameMode.FIXEDLIMIT){
				fixedraise = Integer.parseInt(args[5]);
				fixedbounds = Integer.parseInt(args[6]);
				
				if(fixedraise <= 0 || fixedraise >= basecash - bigblind || fixedbounds <= 0){
					System.out.println("incompatible data");
					throw new NumberFormatException("incompatible data");
				}
			}
			
		} catch (NumberFormatException e) {
			System.out.println("data error");
			throw new NumberFormatException(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception
	{		
		PokerServer server = null;
		try {
			server = new PokerServer(args);
			server.getSocket(1234);
			Table t = server.getTable();
			server.connectPlayers(server.getSocket(1234), t.num_Players, t);
			Game g = server.getGame(t);
			
			g.StartGame();
		} finally {
			server.finalize(server.getSocket(1234));
		}
	}

	protected void connectPlayers(ServerSocket socket, int amount, Table t) throws SocketException{
		socket.setSoTimeout(1000*TIMEOUT);
		for(int i=0;i<amount;i++){
			System.out.println("Waiting for player " + i);
			try {
				t.createPlayers(i, basecash,socket.accept());
			} catch(PlayerException | IOException e) {
				System.out.println("Accept: Timeout !!!");
				//TODO: Here comes bot player replacing timeouted client 
			} 
		}
		
		System.out.println("Running threads");
		for(int i=0;i<amount;i++){
			t.players[i].start();	
		}
	}
	
	protected Table getTable(){
		return new Table(amount);
	}
	
	protected Game getGame(Table t){
		Game game = new Game(t);
		game.parameters.setMode(mode);
		game.parameters.setSmallBlind(smallblind);
		game.parameters.setBigBlind(bigblind);
		game.notifygameMode();
		if(mode == GameMode.FIXEDLIMIT){
			game.parameters.setFixedbounds(fixedbounds);
			game.notifyFixedstuff(fixedraise);
		}
		return game;
	}
	
	protected synchronized ServerSocket getSocket(int PORT) throws ServerNotCreated {
		try {
			if(socket == null){
				ServerSocket serv = new ServerSocket(PORT);
				System.out.println("Server is running");
				setSocket(serv);
			}
			return socket;
		} catch (IOException e) {
			throw new ServerNotCreated(e.getMessage());
		}
	}

	private void setSocket(ServerSocket serv) {
		this.socket = serv;
		
	}

	protected void finalize(ServerSocket socket){
		System.out.print("Closing server socket...");
		try {
			socket.close();
			System.out.print("closed\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
