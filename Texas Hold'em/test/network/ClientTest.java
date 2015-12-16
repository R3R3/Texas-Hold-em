package network;

import static org.junit.Assert.*;
import java.awt.Color;
import java.net.ServerSocket;

import javax.swing.JFrame;

import org.junit.Test;

public class ClientTest {

	@Test
	public void clientTest() throws Exception{
		
		@SuppressWarnings({ "unused", "resource" })
		ServerSocket socket = new ServerSocket(1234);
		
		String[] args = new String[0];
		String serveraddress = (args.length == 0) ? "localhost" : args[0];
		PokerClient client = new PokerClient(serveraddress);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(400, 400);
		client.frame.setResizable(false);
		client.frame.setVisible(true);
		client.setInactive();
		assertFalse(client.buttons[1].isEnabled());
		client.setActive();
		assertTrue(client.buttons[1].isEnabled());
		client.setMainBoard(4);
		assertEquals(4,client.activeResults.length);
		client.setBasecash(100);
		assertEquals(100,Integer.parseInt(client.activeResults[0][2].getText()));
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
	
	}
}
