package network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
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
SETCASH
ACTIVE
INACTIVE
POT
CASH
WAGE
CARD
OP_WAGE
OP_CASH
OP_CARD
TABLE
RESET
DEALER
FOLD
BET
MODE
FIXLOCK
FIXUNLOCK
FIXRAI
*/

public class PokerClient {
	
	private static int PORT = 1234;
	private Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    private int MyID;
	public int pot;
    public boolean available_bet = true;
    protected GameMode mode;
    protected int fixedRaise;
    protected boolean fixblock = false;
    
    protected JFrame frame = new JFrame("Texas Hold'em !");
    protected JLabel messageLabel = new JLabel("");
    protected JLabel Pot = new JLabel("Pot: ");
    //0-Bet, 1-Raise, 2-Check, 3-Call, 4-Fold, 5-All-in
    protected JButton[] buttons = new JButton[6];
    protected JLabel[] players;
    protected JTextField betText = new JTextField(5);
    protected JTextField raiseText = new JTextField(5);
    protected JPanel board = new JPanel(new BorderLayout( 20, 20));
    protected JLabel[] tableCards = new JLabel[5];
    protected JLabel[][] activeResults;
    
    public void setMyID(int myID) {
		MyID = myID;
	}
	
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
				betAction();
			}
			}
		);
		buttons[2] = new JButton("Check");
		buttons[2].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				checkAction();
			}
			}
		);
		buttons[1] = new JButton("Raise");
		buttons[1].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				raiseAction();
			}
			}
		);
		buttons[3] = new JButton("Call");
		buttons[3].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				callAction();
			}
			}
		);
		buttons[4] = new JButton("Fold");
		buttons[4].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				foldAction();
			}
			}
		);
		buttons[5] = new JButton("All-in");
		buttons[5].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//when pressed
				all_inAction();
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
	
	protected String all_inAction() {
		String message = "ALL-IN ";
		out.println(message);
		System.out.println(message);
		return message;
	}

	protected String foldAction() {
		String message = "FOLD ";
		out.println(message);
		System.out.println(message);
		return message;
	}

	protected String callAction() {
		String message = "CALL ";
		out.println(message);
		System.out.println(message);
		return message;
	}

	protected String raiseAction() {
		String message = "RAISE ";
		int raise =0;
		try {
			raise = Integer.parseInt(raiseText.getText());
			if(mode == GameMode.NOLIMIT){
				if(raise <= 0 || (raise + highestWage() - Integer.parseInt(activeResults[MyID][3].getText())) > Integer.parseInt(activeResults[MyID][2].getText())){
					throw new NumberFormatException();
				}
			}
			else if(mode == GameMode.POTLIMIT){
				String pot = Pot.getText().substring(5);
				if(raise <= 0 || (raise + highestWage() - Integer.parseInt(activeResults[MyID][3].getText())) > Integer.parseInt(activeResults[MyID][2].getText()) 
						|| Integer.parseInt(pot) + (highestWage() - Integer.parseInt(activeResults[MyID][3].getText())) < raise){
					throw new NumberFormatException();
				}
			} else {}
			/* since we respect all in as a move only with no enough coins, there's no way to make all in through raise
			 * if(raise == Integer.parseInt(activeResults[MyID][2].getText())){
				//raise with all money == all-in
				out.println("ALL-IN " );
				System.out.println("ALL-IN (from raise)" );
				raiseText.setText("");
				return;
			}*/
			message += Integer.toString(raise);
			out.println(message);
			System.out.println(message);

		} catch (NumberFormatException x) {
			System.out.println("raiseText data error");
		}
		
		raiseText.setText("");
		return message;
	}

	protected String checkAction() {
		String message = "CHECK ";
		out.println(message);
		System.out.println(message);
		return message;
	}

	protected String betAction() {
		String message = "BET ";
		int bet =0;
		try {
			bet = Integer.parseInt(betText.getText());
			if(bet <= 0 || bet > Integer.parseInt(activeResults[MyID][2].getText())){
				throw new NumberFormatException();
			}
			/* since we respect all in as a move only with no enough coins, there's no way to make all in through bet
			 * if(bet == Integer.parseInt(activeResults[MyID][2].getText())){
				//bet with all money == all-in
				out.println("ALL-IN " );
				System.out.println("ALL-IN (from bet)" );
				betText.setText("");
				return;
			}*/
			message += Integer.toString(bet);
			out.println(message);
			System.out.println(message);

		} catch (NumberFormatException x) {
			System.out.println("betText data error");
		}
		
		betText.setText("");
		return message;
	}

	public void play() throws Exception{
		String response;
		try{
			response = in.readLine();
			if (response.startsWith("WELCOME")) {
				String id = response.substring(15);
				setMyID(Integer.parseInt(id));
				frame.setTitle("Texas Hold'em - Player " + id);
			}
			while (true){
				response = in.readLine();
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
				else if (response.startsWith("CASH")){
					int cash = Integer.parseInt(response.substring(5));
					updateCash(cash);
				}
				else if (response.startsWith("WAGE")){
					int wage = Integer.parseInt(response.substring(5));
					updateWage(wage);
				}
				else if (response.startsWith("OP_CASH")){
					int i = Integer.parseInt(response.substring(8, 9));
					int cash = Integer.parseInt(response.substring(10));
					updateOponentCash(i, cash);
				}
				else if (response.startsWith("OP_WAGE")){
					int i = Integer.parseInt(response.substring(8, 9));
					int cash = Integer.parseInt(response.substring(10));
					updateOponentWage(i, cash);
				} 
				else if (response.startsWith("FOLD")){
					int i = Integer.parseInt(response.substring(5));
					updateFold(i);
				}
				else if (response.startsWith("DEALER")){
					int i = Integer.parseInt(response.substring(7));
					updateDealer(i);
				}
				else if (response.startsWith("RESET")){
					reset();
				}
				else if (response.startsWith("TABLE")){
					tableCards[Integer.parseInt(response.substring(6,7))].setText(response.substring(8));
					frame.pack();
				}
				else if (response.startsWith("CARD")){
					activeResults[MyID][4+Integer.parseInt(response.substring(5,6))].setText(response.substring(7));
					frame.pack();
				}
				else if (response.startsWith("OP_CARD")){
					int id = Integer.parseInt(response.substring(10, 11));
					int i = Integer.parseInt(response.substring(8, 9));
					activeResults[id][4 + i].setText(response.substring(12));
					frame.pack();
				}
				else if (response.startsWith("BET")){
					if(response.charAt(4) == 'T'){
						available_bet = true;
					}
					else if (response.charAt(4) == 'F'){
						available_bet = false;
					}
				}
				else if (response.startsWith("MODE")){
					if(response.charAt(5) == 'N'){
						mode = GameMode.NOLIMIT;
					}
					else if(response.charAt(5) == 'P'){
						mode = GameMode.POTLIMIT;
					}
					else if(response.charAt(5) == 'F'){
						mode = GameMode.FIXEDLIMIT;
					}
				}
				else if (response.startsWith("FIXLOCK")){
					fixblock = true;
				}
				else if (response.startsWith("FIXUNLOCK")){
					fixblock = false;
				}
				else if (response.startsWith("FIXRAI")){
					fixedRaise = Integer.parseInt(response.substring(7));
				}
			}
		}
		finally {
			System.out.println("Closing client socket");
			socket.close();
		}
		
	}
	
	protected void reset() {
		for(int i = 0; i< 5; i++ ){
			tableCards[i].setText(" ");
		}
		
		for(int i = 0;i<activeResults.length;i++){
			activeResults[i][1].setBackground(Color.GREEN);
			activeResults[i][3].setText("0");
			activeResults[i][4].setText(" ");
			activeResults[i][5].setText(" ");
		}
		
		updatePot(0);
	}

	protected void updateDealer(int i) {
		for(int k=0;k<activeResults.length;k++){
			if(activeResults[k][0].getText() == "D"){
				activeResults[k][0].setText(" ");
			}
		}
		activeResults[i][0].setText("D");
		frame.pack();
	}

	protected void updateFold(int i) {
		activeResults[i][1].setBackground(Color.RED);
		
	}

	protected void updateOponentWage(int i, int cash) {
		activeResults[i][3].setText(Integer.toString(cash));
		
	}

	protected void updateOponentCash(int i, int cash) {
		activeResults[i][2].setText(Integer.toString(cash));
		
	}

	protected void updateWage(int wage) {
		activeResults[MyID][3].setText(Integer.toString(wage));
		
	}

	protected void updateCash(int cash) {
		activeResults[MyID][2].setText(Integer.toString(cash));
		
	}

	protected void setBasecash(int basecash) {
		
		for(int i=0;i<activeResults.length;i++){
			activeResults[i][2].setText(Integer.toString(basecash));
		}
	}
	
	protected int highestWage() {
		int wage = Integer.parseInt(activeResults[0][3].getText());
		for(int i= 1; i< activeResults.length;i++){
			if(Integer.parseInt(activeResults[i][3].getText()) > wage){
				wage = Integer.parseInt(activeResults[i][3].getText());
			}
		}
		return wage;
	}

	protected void setMainBoard(int PN) 
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
					activeResults[i][j].setBackground(Color.GREEN);
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
		
		//bet
		if(available_bet && Integer.parseInt(activeResults[MyID][2].getText()) > 0){
			betText.setEnabled(true);
			buttons[0].setEnabled(true);
		}
		//raise
		switch(mode){
			case NOLIMIT :
				if(!available_bet && Integer.parseInt(activeResults[MyID][3].getText()) <= highestWage() &&
					highestWage() - Integer.parseInt(activeResults[MyID][3].getText()) < Integer.parseInt(activeResults[MyID][2].getText())){
					raiseText.setEnabled(true);
					buttons[1].setEnabled(true);
				}
				break;
			case POTLIMIT :
				if(!available_bet && Integer.parseInt(activeResults[MyID][3].getText()) <= highestWage() &&
					highestWage() - Integer.parseInt(activeResults[MyID][3].getText()) < Integer.parseInt(activeResults[MyID][2].getText())){
					raiseText.setEnabled(true);
					buttons[1].setEnabled(true);
				}
				break;
			case FIXEDLIMIT :
				if(!available_bet && !fixblock && Integer.parseInt(activeResults[MyID][3].getText()) <= highestWage() &&
					highestWage() - Integer.parseInt(activeResults[MyID][3].getText()) < Integer.parseInt(activeResults[MyID][2].getText())){
					raiseText.setEnabled(true);
					raiseText.setText(Integer.toString(fixedRaise));
					raiseText.setEditable(false);
					buttons[1].setEnabled(true);
				}
				break;
		}
		
		//check
		if(Integer.parseInt(activeResults[MyID][3].getText()) == highestWage()){
			buttons[2].setEnabled(true);
		}
		//call
		if(!available_bet && Integer.parseInt(activeResults[MyID][3].getText()) < highestWage() 
				&& highestWage() - Integer.parseInt(activeResults[MyID][3].getText()) <= Integer.parseInt(activeResults[MyID][2].getText()) ){
			buttons[3].setEnabled(true);
		}
		//fold
		//no if clause - fold is always open
			buttons[4].setEnabled(true);
		//all-in
		if(!available_bet && highestWage() - Integer.parseInt(activeResults[MyID][3].getText()) > Integer.parseInt(activeResults[MyID][2].getText())){
			buttons[5].setEnabled(true);
		}
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
