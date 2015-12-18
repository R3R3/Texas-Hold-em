package cards.and.stuff;

import java.util.LinkedList;

public class MyDeckBuilder implements DeckBuilder{
	
	private Deck deck;
	
	private void createDeck(LinkedList<Card> list){
		deck = new Deck();
		while(!list.isEmpty()){
			deck.addCard(list.removeLast());
		}
	}
	
	@Override
	public Deck getDeck(){
		return null;
	}
	
	public Deck getDeck(String[] s){
		LinkedList<Card> list = genList(s);
		createDeck(list);
		return deck;
	}
	
	@Override
	public void resetDeck(){
	}
	private static final String f = "234567890JQKA";
	
	private static Color getColor(String s){
		switch(s){
		case "S":
			return Color.SPADES;
		case "H":
			return Color.HEARTS;
		case "C":
			return Color.CLUBS;
		case "D":
			return Color.DIAMOND;
		default:
			return null;
		}
	}

	public static LinkedList<Card> genList(String[] s){
		LinkedList<Card> list = new LinkedList<Card>();
		for(int i =0; i < s.length;i++){
			int fig;
			Color col;
			if(s[i].length()!=2)
				continue;
			else{
				fig = f.indexOf(s[i].substring(0, 1));
				col = getColor(s[i].substring(1, 2));
				if(fig!=-1 && col != null){
					Card card = CardGen.genCard(fig, col);
					list.add(card);
				}
			}
		}
		return list;	
	}

}
