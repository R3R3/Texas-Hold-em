package patterns;
import java.util.LinkedList;

import cards.and.stuff.*;

public class High {
	
	public int getHigh(LinkedList<Card> list){
		int max = list.getFirst().getFigure();
		for(Card c : list)
		{
			if(c.getFigure()>max)
				max = c.getFigure();
		}		
		return max;	
	}
	
}
