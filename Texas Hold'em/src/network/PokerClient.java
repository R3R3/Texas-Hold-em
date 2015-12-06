package network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PokerClient {
	
	private static int PORT = 1234;
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int MyID;
    
    private JFrame frame = new JFrame("Texas Hold'em !");
    private JLabel messageLabel = new JLabel("");
    private JLabel Pot = new JLabel("Pot: ");
    //0-Bet, 1-Check, 2-Raise, 3-Call, 4-Fold, 5-All-in
    private JButton[] buttons = new JButton[6];
    private JLabel[] players;
	
	public PokerClient(String ServerAddress) throws Exception { 
		socket = new Socket(ServerAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		//TODO: GUI
		messageLabel.setBackground(Color.LIGHT_GRAY);
		messageLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,15));
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		frame.getContentPane().add(messageLabel,"North");
		JPanel board = new JPanel();
		JPanel boardPanel = new JPanel();
	    JPanel buttonsPanel = new JPanel();
	    
		boardPanel.setLayout(new BorderLayout());
		
		buttons[0] = new JButton("Bet");
		buttons[0].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				//TODO: adjustable value for bet
				out.println("BET " );
			}
			}
		);
		buttons[1] = new JButton("Check");
		buttons[1].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				out.println("CHECK " );
			}
			}
		);
		buttons[2] = new JButton("Raise");
		buttons[2].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				//TODO: adjustable value for raise
				out.println("RAISE " );
			}
			}
		);
		buttons[3] = new JButton("Call");
		buttons[3].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				out.println("CALL " );
			}
			}
		);
		buttons[4] = new JButton("Fold");
		buttons[4].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				out.println("FOLD " );
			}
			}
		);
		buttons[5] = new JButton("All-in");
		buttons[5].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				out.println("ALL-IN " );
			}
			}
		);
		buttonsPanel.setLayout(new GridLayout(1,6,20,0));
		for(int i=0;i<6;i++){
			buttonsPanel.add(buttons[i]);
		}
		
		
		//TODO: JPanel board
		
		
		Pot.setFont(new Font(Font.DIALOG,Font.PLAIN,40));
		Pot.setHorizontalAlignment(JLabel.CENTER);
		Pot.setVerticalAlignment(JLabel.CENTER);
		
		boardPanel.add(Pot,BorderLayout.NORTH);
		boardPanel.add(board, BorderLayout.CENTER);
		boardPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		boardPanel.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(boardPanel, "Center");
		frame.pack();
	}

	public void play() throws Exception{
		String response;
		try{
			response = in.readLine();
			if (response.startsWith("WELCOME")) {
				String id = response.substring(15);
				MyID = Integer.parseInt(id);
				frame.setTitle("Texas Hold'em - Player " + id);
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
