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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*

Client-Server relation is based on sending and receiving specified commands. 
Commands are represented as string. First part is the type of an action, second - optional - represents argument.
Not every command needs an argument
Possible commands that can be sent:

Client ---> Server
BET <n> (unsigned int)
RAISE <n> (unsigned int)
CHECK
CALL
FOLD
ALL-IN

Server ---> Client
MESSAGE <text> (String)
WELCOME <n> (int)
AMOUNT <n> (int)
to be continued...	
*/

public class PokerClient {
	
	private static int PORT = 1234;
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int MyID;
    public int pot;
    
    private JFrame frame = new JFrame("Texas Hold'em !");
    private JLabel messageLabel = new JLabel("");
    private JLabel Pot = new JLabel("Pot: ");
    //0-Bet, 1-Raise, 2-Check, 3-Call, 4-Fold, 5-All-in
    private JButton[] buttons = new JButton[6];
    private JLabel[] players;
    private JTextField betText = new JTextField(5);
    private JTextField raiseText = new JTextField(5);
    private JPanel board = new JPanel(new BorderLayout( 20, 20));
    private JLabel[] tableCards = new JLabel[5];
    private JLabel[][] activeResults;
	
	public PokerClient(String ServerAddress) throws Exception { 
		socket = new Socket(ServerAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		
		messageLabel.setBackground(Color.LIGHT_GRAY);
		messageLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,15));
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		frame.getContentPane().add(messageLabel,"North");
		
		JPanel boardPanel = new JPanel();
		
	    JPanel buttonsPanel = new JPanel();
	    
		boardPanel.setLayout(new BorderLayout(20,20));
		
		
		buttons[0] = new JButton("Bet");
		buttons[0].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				int bet =0;
				try {
					bet = Integer.parseInt(betText.getText());
					if(bet <= 0){
						throw new NumberFormatException();
					}
					out.println("BET " + Integer.toString(bet));
					System.out.println("BET " + Integer.toString(bet));

				} catch (NumberFormatException x) {
					System.out.println("betText data error");
				}
				
				betText.setText("");
			}
			}
		);
		buttons[2] = new JButton("Check");
		buttons[2].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				out.println("CHECK " );
				System.out.println("CHECK " );
			}
			}
		);
		buttons[1] = new JButton("Raise");
		buttons[1].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				int raise =0;
				try {
					raise = Integer.parseInt(raiseText.getText());
					if(raise <= 0 || raise > Integer.parseInt(activeResults[MyID][3].getText())){
						throw new NumberFormatException();
					}
					if(raise == Integer.parseInt(activeResults[MyID][3].getText())){
						//raise with all money == all-in
						out.println("ALL-IN " );
						System.out.println("ALL-IN (from raise)" );
						raiseText.setText("");
						return;
					}
					out.println("RAISE " + Integer.toString(raise));
					System.out.println("RAISE " + Integer.toString(raise));

				} catch (NumberFormatException x) {
					System.out.println("raiseText data error");
				}
				
				raiseText.setText("");
			}
			}
		);
		buttons[3] = new JButton("Call");
		buttons[3].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				out.println("CALL " );
				System.out.println("CALL " );
			}
			}
		);
		buttons[4] = new JButton("Fold");
		buttons[4].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				setInactive();
				out.println("FOLD " );
				System.out.println("FOLD " );
			}
			}
		);
		buttons[5] = new JButton("All-in");
		buttons[5].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				out.println("ALL-IN " );
				System.out.println("ALL-IN " );
			}
			}
		);
		buttonsPanel.setLayout(new GridLayout(2,4,20,20));
		for(int i=0;i<6;i++){
			if(i==0){
				buttonsPanel.add(betText);
			} else if (i==1) {
				buttonsPanel.add(raiseText);
			}
			buttonsPanel.add(buttons[i]);
		}		
		
		
		Pot.setFont(new Font(Font.DIALOG,Font.PLAIN,40));
		Pot.setOpaque(true);
		Pot.setBackground(Color.LIGHT_GRAY);
		Pot.setHorizontalAlignment(JLabel.CENTER);
		Pot.setVerticalAlignment(JLabel.CENTER);
		
		boardPanel.add(Pot,BorderLayout.NORTH);
		boardPanel.add(board, BorderLayout.CENTER);
		JLabel label = new JLabel(" ");
		label.setOpaque(true);
		label.setBackground(Color.LIGHT_GRAY);
		boardPanel.add(label ,BorderLayout.SOUTH);
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
				else if (response.startsWith("AMOUNT")) {
					int playerNum = Integer.parseInt(response.substring(7));
					setMainBoard(playerNum);
					frame.pack();
				}
				else if (response.startsWith("SETCASH")){
					int basecash = Integer.parseInt(response.substring(8));
					setBasecash(basecash);
					frame.pack();
				}
				else if (response.startsWith("ACTIVE")) {
					setActive();
					
				}
				else if (response.startsWith("INACTIVE")) {
					setInactive();
				}
				else if (response.startsWith("POT")) {
					int actual = Integer.parseInt(response.substring(4));
					updatePot(actual);
				}
			}
		}
		finally {
			System.out.println("Closing client socket");
			socket.close();
		}
		
	}
	
	private void setBasecash(int basecash) {
		
		for(int i=0;i<activeResults.length;i++){
			activeResults[i][2].setText(Integer.toString(basecash));
		}
	}

	private void setMainBoard(int PN) 
	{
		
		JPanel cards = new JPanel();
		cards.setLayout(new GridLayout(1,5,20,20));
		for(int i=0;i<5;i++){
			tableCards[i] = new JLabel(" ");
			tableCards[i].setOpaque(true);
	//		tableCards[i].setSize(20, 40);
			tableCards[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			tableCards[i].setBackground(Color.WHITE);
			cards.add(tableCards[i]);
		}
		
		board.add(cards, BorderLayout.NORTH);
		
		JPanel view = new JPanel();
		view.setLayout(new GridLayout(PN,6,10,20));
		activeResults = new JLabel[PN][6];
		
		for(int i=0;i<PN;i++){
			for(int j=0;j<6;j++){
				activeResults[i][j] = new JLabel("");
				activeResults[i][j].setOpaque(true);
				if(i == MyID){
					activeResults[i][j].setBorder(BorderFactory.createLineBorder(Color.RED));
				}
				else {
					activeResults[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
				if(j==1){
					activeResults[i][j].setText(Integer.toString(i));
				}
				else if( j==3){
					activeResults[i][j].setText("0");
				}
				else if(j==4 || j==5){
					activeResults[i][j].setBackground(Color.WHITE);
				}
				view.add(activeResults[i][j]);
			}
		}
		
		board.add(view, BorderLayout.CENTER);
	}
	
	public void setActive(){
		for(JButton b : buttons){
			b.setEnabled(true);
		}
		raiseText.setEnabled(true);
		betText.setEnabled(true);
	}
	
	public void updatePot(int pot){
		String base = "Pot: ";
		Pot.setText(base + Integer.toString(pot));
	}
	
	public void setInactive(){
		for(JButton b : buttons){
			b.setEnabled(false);
		}
		raiseText.setEnabled(false);
		betText.setEnabled(false);
	}
	
	public static void main(String[] args) throws Exception {
		
		String serveraddress = (args.length == 0) ? "localhost" : args[0];
		PokerClient client = new PokerClient(serveraddress);
		
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(400, 400);
		client.frame.setResizable(false);
		client.frame.setVisible(true);
		
		client.play();
	}
}
