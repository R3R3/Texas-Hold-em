package network;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;

import org.junit.Test;

import table.Player;
import table.PlayerException;
import table.Table;

public class ServerTest {
	
	@Test
	public void basicServTest() throws Exception{
		PokerServer server = null;
		String[] args = new String[4];
		args[0] = "3";
		args[1] = "100";
		args[2] = "10";
		args[3] = "20";
		//args[4] = "";
		
		server = new PokerServer(args);
		assertNotNull(server);
		server.getSocket(666);
		assertNotNull(server.getSocket(666));
		assertFalse(server.getSocket(666).isClosed());
		Table t = server.getTable();
		t.canWinPlayers = new ArrayList<Player> ();
		try {
			for(int i=0;i<3;i++){
				t.createPlayers(i, 100, new Socket("localhost", 666));
			}
		} catch (PlayerException e) {}	
		assertNotNull(t);
		assertEquals(3,t.num_Players);
		//server.connectPlayers(server.getSocket(666), t.num_Players, t);
		
		Game g = server.getGame(t);
		assertNotNull(g);
		assertEquals(10,g.parameters.getSmallBlind());
		assertEquals(20,g.parameters.getBigBlind());
		assertEquals(GameMode.NOLIMIT,g.parameters.getMode());
		
		server.finalize(server.getSocket(666));
		assertTrue(server.getSocket(666).isClosed());
	}
	
	@Test
	public void dummyTest() throws Exception{
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "POTLIMIT";
		
		server = new PokerServer(args);
		assertNotNull(server);
		server.getSocket(669);
		assertNotNull(server.getSocket(669));
		assertFalse(server.getSocket(669).isClosed());
		Table t = server.getTable();
		t.canWinPlayers = new ArrayList<Player> ();
		try {
			for(int i=0;i<4;i++){
				t.createPlayers(i, 100, new Socket("localhost", 669));
			}
		} catch (PlayerException e) {}	
		assertNotNull(t);
		assertEquals(4,t.num_Players);
		//server.connectPlayers(server.getSocket(669), t.num_Players, t);
		
		Game g = server.getGame(t);
		assertNotNull(g);
		assertEquals(11,g.parameters.getSmallBlind());
		assertEquals(22,g.parameters.getBigBlind());
		assertEquals(GameMode.POTLIMIT,g.parameters.getMode());
		
		server.finalize(server.getSocket(669));
		assertTrue(server.getSocket(669).isClosed());
	}
	
	@Test
	public void fixedTest() throws Exception{
		PokerServer server = null;
		String[] args = new String[7];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "FIXEDLIMIT";
		args[5] = "20";
		args[6] = "5";
		
		server = new PokerServer(args);
		assertNotNull(server);
		server.getSocket(669);
		assertNotNull(server.getSocket(669));
		assertFalse(server.getSocket(669).isClosed());
		Table t = server.getTable();
		t.canWinPlayers = new ArrayList<Player> ();
		try {
			for(int i=0;i<4;i++){
				t.createPlayers(i, 100, new Socket("localhost", 669));
			}
		} catch (PlayerException e) {}	
		assertNotNull(t);
		assertEquals(4,t.num_Players);
		//server.connectPlayers(server.getSocket(669), t.num_Players, t);
		
		Game g = server.getGame(t);
		assertNotNull(g);
		assertEquals(11,g.parameters.getSmallBlind());
		assertEquals(22,g.parameters.getBigBlind());
		assertEquals(5,g.parameters.getFixedbounds());
		assertEquals(20, server.fixedraise);
		assertEquals(GameMode.FIXEDLIMIT,g.parameters.getMode());
		
		server.finalize(server.getSocket(669));
		assertTrue(server.getSocket(669).isClosed());
	}
	
	@Test (expected = NumberFormatException.class)
	public void anotherfixedTest() throws Exception{
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[7];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "FIXEDLIMIT";
		args[5] = "da";
		args[6] = "5";
		try{
			server = new PokerServer(args);
		} catch(NumberFormatException e){
			throw new NumberFormatException (e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void anotherfixedTest2() throws Exception{
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[7];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "FIXEDLIMIT";
		args[5] = "0";
		args[6] = "5";
		try{
			server = new PokerServer(args);
		} catch(NumberFormatException e){
			throw new NumberFormatException (e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void anotherfixedTest3() throws Exception{
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[7];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "FIXEDLIMIT";
		args[5] = "120";
		args[6] = "5";
		try{
			server = new PokerServer(args);
		} catch(NumberFormatException e){
			throw new NumberFormatException (e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void anotherfixedTest4() throws Exception{
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[7];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "FIXEDLIMIT";
		args[5] = "20";
		args[6] = "0";
		try{
			server = new PokerServer(args);
		} catch(NumberFormatException e){
			throw new NumberFormatException (e.getMessage());
		}
	}
	
	@Test
	public void trollimitTest() throws Exception{
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "TROLLIMIT";
		
		server = new PokerServer(args);
		server.getSocket(669);
		Table t = server.getTable();
		t.canWinPlayers = new ArrayList<Player> ();
		try {
			for(int i=0;i<4;i++){
				t.createPlayers(i, 100, new Socket("localhost", 669));
			}
		} catch (PlayerException e) {}	
		//server.connectPlayers(server.getSocket(669), t.num_Players, t);
		
		Game g = server.getGame(t);
		assertEquals(GameMode.NOLIMIT,g.parameters.getMode());
		
		server.finalize(server.getSocket(669));
	}
	
	@Test (expected = NumberFormatException.class)
	public void toofewargumentsTest() throws NumberFormatException{
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[3];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		//args[3] = "22";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void toomuchargumentsTest() throws NumberFormatException{
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[8];
		args[0] = "4";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		args[4] = "POTLIMIT";
		args[5] = "trol";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void nonIntDataTest() throws NumberFormatException{
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "ass";
		args[1] = "120";
		args[2] = "11";
		args[3] = "22";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void minusBaseCashTest(){
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "4";
		args[1] = "-4";
		args[2] = "11";
		args[3] = "22";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void toolessPlayersTest(){
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "1";
		args[1] = "100";
		args[2] = "11";
		args[3] = "22";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
		
	}
	
	@Test (expected = NumberFormatException.class)
	public void toomuchPlayersTest(){
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "12";
		args[1] = "100";
		args[2] = "11";
		args[3] = "22";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
		
	}
	
	@Test (expected = NumberFormatException.class)
	public void toollittleSBTest(){
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "4";
		args[1] = "100";
		args[2] = "0";
		args[3] = "22";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void toolittleBBTest(){
		@SuppressWarnings("unused")
		PokerServer server = null;
		String[] args = new String[5];
		args[0] = "4";
		args[1] = "100";
		args[2] = "20";
		args[3] = "19";
		try{
			server = new PokerServer(args);
		} catch (NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		}
	}
}
