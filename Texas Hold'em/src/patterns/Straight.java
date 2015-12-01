package patterns;
import java.util.ArrayList;
import java.util.LinkedList;

import cards.and.stuff.*;

public class Straight {
	
	public static boolean ifIs(LinkedList<Card> list){
		ArrayList<Integer>figure = new ArrayList<Integer>();
		for(Card c : list){
			figure.add(c.getFigure());
		}
		figure.sort(null);
		boolean t = true;
		for(int i = 1;i<5;i++){
			if(figure.get(i) > figure.get(i-1))
				t=false;
		}
		figure = new ArrayList<Integer>();
		for(Card c : list){
			if(c.getFigure()==12)
				figure.add(-1);
			else
				figure.add(c.getFigure());
		}
		figure.sort(null);
		boolean f = true;
		for(int i = 1;i<5;i++){
			if(figure.get(i) > figure.get(i-1))
				f=false;
		}
		return t||f;
	}

}
