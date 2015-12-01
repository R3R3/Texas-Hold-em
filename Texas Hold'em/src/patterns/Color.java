package patterns;
import java.util.LinkedList;

import cards.and.stuff.*;

public class Color {

	public static boolean ifSame(LinkedList<Card> list){
		cards.and.stuff.Color color = list.getFirst().getColor();
		boolean r = true;
		for(Card c : list)
		{
			if(c.getColor() != color)
				r = false;
		}
		return r;
	}
	
}
