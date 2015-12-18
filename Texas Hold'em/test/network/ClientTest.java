package network;

import static org.junit.Assert.*;
import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Test;

public class ClientTest {

	ServerSocket socket;
	
	@Test
	public void clientTest() throws Exception{
		
		socket = new ServerSocket(1234);
		
		String[] args = new String[0];
		String serveraddress = (args.length == 0) ? "localhost" : args[0];
		PokerClient client = new PokerClient(serveraddress);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(400, 400);
		client.frame.setResizable(false);
		client.frame.setVisible(true);
		client.setMainBoard(4);
		assertEquals(4,client.activeResults.length);
		client.setBasecash(100);
		assertEquals(100,Integer.parseInt(client.activeResults[0][2].getText()));
		client.setMyID(0);
		client.activeResults[0][3].setText("20");
		//_ 0 100 20 _ _
		client.mode = GameMode.NOLIMIT;
		client.setActive();
		assertTrue(client.buttons[4].isEnabled());
		
		client.activeResults[0][0].setText("D");
		client.updateDealer(1);
		assertEquals("D",client.activeResults[1][0].getText());
		client.updateDealer(2);
		assertEquals("D",client.activeResults[2][0].getText());
		assertNotEquals("D",client.activeResults[1][0].getText());
		client.updateFold(3);
		assertTrue((client.activeResults[3][1].getBackground() == Color.RED)? true : false);
		client.updateCash(150);
		assertEquals(150,Integer.parseInt(client.activeResults[0][2].getText()));
		client.updateWage(140);
		assertEquals(140,Integer.parseInt(client.activeResults[0][3].getText()));
		client.updateOponentCash(3, 666);
		assertEquals(666,Integer.parseInt(client.activeResults[3][2].getText()));
		client.updateOponentWage(3, 222);
		assertEquals(222,Integer.parseInt(client.activeResults[3][3].getText()));
		assertEquals(222,client.highestWage());
		client.updatePot(213);
		assertEquals("Pot: 213" ,client.Pot.getText());
		
		client.reset();
		assertEquals(" ", client.tableCards[0].getText());
		assertEquals("0", client.activeResults[0][3].getText());
		
		
		assertEquals("ALL-IN ",client.all_inAction());
		assertEquals("CALL ",client.callAction());
		assertEquals("CHECK ",client.checkAction());
		assertEquals("FOLD ",client.foldAction());
		client.betText.setText("10");
		assertEquals("BET 10",client.betAction());
		client.betText.setText("sda");
		assertEquals("BET ",client.betAction());
		client.betText.setText("0");
		assertEquals("BET ",client.betAction());
		client.betText.setText("1000000000");
		assertEquals("BET ",client.betAction());
		
		client.raiseText.setText("10");
		assertEquals("RAISE 10",client.raiseAction());
		client.raiseText.setText("dsad");
		assertEquals("RAISE ",client.raiseAction());
		client.raiseText.setText("0");
		assertEquals("RAISE ",client.raiseAction());
		client.raiseText.setText("10000000");
		assertEquals("RAISE ",client.raiseAction());
		
	
	}
	
	@Test
	public void setActiveTest() throws Exception{
		socket = new ServerSocket(1234);
		
		String[] args = new String[0];
		String serveraddress = (args.length == 0) ? "localhost" : args[0];
		PokerClient client = new PokerClient(serveraddress);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(400, 400);
		client.frame.setResizable(false);
		client.frame.setVisible(true);
		client.setMainBoard(4);
		client.setBasecash(100);
		client.setMyID(0);
		client.mode = GameMode.NOLIMIT;
		client.setInactive();
		client.activeResults[0][3].setText("30");
		client.activeResults[1][3].setText("50");
		client.available_bet = false;
		client.setActive();
		assertTrue(client.buttons[1].isEnabled());
		assertTrue(client.buttons[3].isEnabled());
		assertFalse(client.buttons[0].isEnabled());
		
		client.setInactive();
		client.mode = GameMode.POTLIMIT;
		client.setActive();
		assertTrue(client.buttons[1].isEnabled());
		assertTrue(client.buttons[3].isEnabled());
		assertFalse(client.buttons[0].isEnabled());
		
		client.setInactive();
		client.activeResults[1][3].setText("200");
		client.setActive();
		assertTrue(client.buttons[5].isEnabled());
		assertFalse(client.buttons[3].isEnabled());
	}
	
	@Test
	public void setFixedActiveTest() throws Exception{
		socket = new ServerSocket(1234);
		
		String[] args = new String[0];
		String serveraddress = (args.length == 0) ? "localhost" : args[0];
		PokerClient client = new PokerClient(serveraddress);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(400, 400);
		client.frame.setResizable(false);
		client.frame.setVisible(true);
		client.setMainBoard(4);
		client.setBasecash(100);
		client.setMyID(0);
		client.mode = GameMode.FIXEDLIMIT;
		client.fixedRaise = 20;
		client.available_bet = false;
		client.fixblock = false;
		client.setInactive();
		client.activeResults[0][3].setText("30");
		client.activeResults[1][3].setText("30");
		assertFalse(client.buttons[2].isEnabled());
		client.setActive();
		assertTrue(client.buttons[2].isEnabled());
		assertEquals("20",client.raiseText.getText());
	}
	
	@Test
	public void potraiseTest() throws Exception{
		socket = new ServerSocket(1234);
		
		String[] args = new String[0];
		String serveraddress = (args.length == 0) ? "localhost" : args[0];
		PokerClient client = new PokerClient(serveraddress);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(400, 400);
		client.frame.setResizable(false);
		client.frame.setVisible(true);
		client.setMainBoard(4);
		client.setBasecash(100);
		client.setMyID(0);
		client.mode = GameMode.POTLIMIT;
		client.activeResults[0][3].setText("20");
		client.activeResults[1][3].setText("30");
		client.updatePot(50);
		
		client.raiseText.setText("10");
		assertEquals("RAISE 10",client.raiseAction());
		client.raiseText.setText("0");
		assertEquals("RAISE ",client.raiseAction());
		client.raiseText.setText("60");
		assertEquals("RAISE 60",client.raiseAction());
		client.raiseText.setText("61");
		assertEquals("RAISE ",client.raiseAction());
		
	}
	
	@After
	public void cleanUp() throws IOException{
		socket.close();
	}
}
