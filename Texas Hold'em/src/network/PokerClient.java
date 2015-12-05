package network;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class PokerClient {
	
	private static int PORT = 1234;
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    private JFrame frame = new JFrame("Texas Hold'em !");
    private JLabel messageLabel = new JLabel("");

	
	public PokerClient(String ServerAddress) throws Exception { 
		socket = new Socket(ServerAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		//TODO: GUI
		messageLabel.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(messageLabel,"South");
		
	}

	public void play() throws Exception{
		String response;
		try{
			response = in.readLine();
			if (response.startsWith("WELCOME")) {
				//TODO: GUI actions
				String id = response.substring(8);
				frame.setTitle("Texas Hold'em - " + id);
			}
			while (true){
				response = in.readLine();
				//TODO: responses and actions based on messages
				if(response.startsWith("MESSAGE")) {
					messageLabel.setText(response.substring(8));
				}
			}
		}
		finally {
			System.out.println("Closing client socket");
			socket.close();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		String serveraddress = (args.length == 0) ? "localhost" : args[0];
		PokerClient client = new PokerClient(serveraddress);
		
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(600, 800);
		client.frame.setResizable(false);
		client.frame.setVisible(true);
		
		client.play();
	}
}
