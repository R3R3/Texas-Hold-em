package network;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Ignore;
import org.junit.Test;

public class ServerTest {
	
	@Test
	public void createandshutTest(){
		ServerSocket test = null;
		try {
			test = PokerServer.getSocket(666);
		} catch (ServerNotCreated e) {fail("Exception occured");}
		assertNotNull(test);
		assertEquals(666,test.getLocalPort());
		
		PokerServer.finalize(test);
		assertTrue(test.isClosed());
	}
	
	@Ignore
	@Test (expected = ServerNotCreated.class)
	public void failserverTest() throws ServerNotCreated{
		
		PokerServer.getSocket(777);
		
		ServerSocket test2 = PokerServer.getSocket(777);
		
		PokerServer.finalize(test2);
	}
	
	@Test
	public void servTest(){
		
		String[] args = new String[0];
		try {
			PokerServer.main(args);
		} catch (Exception e) {fail("No exceptions should be thrown at this point");}
		
		args = new String[6];
		
		try {
			PokerServer.main(args);
		} catch (Exception e) {fail("No exceptions should be thrown at this point");}
	}
	
	@Test (expected = NumberFormatException.class)
	public void incorrectPlayerFormatTest() throws Exception{
		String[] args = new String[4];
		args[0] = "abc";
		args[1] = Integer.toString(100);
		args[2] = Integer.toString(10);
		args[3] = Integer.toString(20);
		
		try {
			PokerServer.main(args);
		} catch (NumberFormatException e) {throw new NumberFormatException();}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void incorrectCashFormatTest() throws Exception{
		String[] args = new String[4];
		args[0] = Integer.toString(4);
		args[1] = "100b";
		args[2] = Integer.toString(10);
		args[3] = Integer.toString(20);
		
		try {
			PokerServer.main(args);
		} catch (NumberFormatException e) {throw new NumberFormatException();}
	}
	
	@Test
	public void incompatibleDataTest() throws Exception{
		String[] args = new String[4];
		args[0] = Integer.toString(1);
		args[1] = Integer.toString(100);
		args[2] = Integer.toString(10);
		args[3] = Integer.toString(20);
		
		PokerServer.main(args);
		
		args[0] = Integer.toString(3);
		args[1] = Integer.toString(0);
		args[2] = Integer.toString(10);
		args[3] = Integer.toString(20);
		
		PokerServer.main(args);
		
		args[0] = Integer.toString(3);
		args[1] = Integer.toString(100);
		args[2] = Integer.toString(0);
		args[3] = Integer.toString(20);
		
		PokerServer.main(args);
		
		args[0] = Integer.toString(3);
		args[1] = Integer.toString(100);
		args[2] = Integer.toString(10);
		args[3] = Integer.toString(0);
		
		PokerServer.main(args);
	}
}
