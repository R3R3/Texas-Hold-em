package patterns;

import java.util.ArrayList;
import java.util.LinkedList;

import cards.and.stuff.Card;

public class Multi {
	
	private static int[] createTab(int a, int b){
		int [] t = new int [2];
		t[0]=a;
		t[1]=b;
		return t;
	}

	public static ArrayList<int[]> getMulti(LinkedList<Card> list){
		ArrayList<Integer>figure = new ArrayList<Integer>();
		ArrayList<int[]> m = new ArrayList<int[]>();
		for(Card c : list){
			figure.add(c.getFigure());
		}
		figure.sort(null);
		m.add(createTab(0,figure.get(0)));
		int i = 0;
		for(int f : figure){
			if(f == m.get(i)[1]){
				m.get(i)[0]++;
			}
			else
			{
				i++;
				m.add(createTab(1,f));
			}
		}
		return m;
	}
	
}
