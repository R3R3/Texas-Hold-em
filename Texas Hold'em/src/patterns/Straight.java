package patterns;

import java.util.ArrayList;
import java.util.TreeMap;

import cards.and.stuff.Color;

public class Straight {
	
	public static int ifIs(MapHolder holder){
		TreeMap<Integer,ArrayList<Color>> map = holder.getMap();
		int size = map.keySet().size();
		int max=-1;
		if(size <5)
			return -1;
		Integer[] tab = new Integer[size];
		
		int key = -1;
		for(int j=0; j< size; j++)
		{
			tab[j]=map.higherKey(key);
			key=tab[j];
		}

		
		if(tab[size-1].intValue() == 12 &&
		tab[0].intValue() == 0  &&
		tab[1].intValue() == 1 &&
		tab[2].intValue() == 2 &&
		tab[3].intValue() == 3
		)
		max=3;

		int c = 0;
		int i = size-1;
		for(int j = size-1; j>1 && c!= 4;j--){
			if(tab[j].intValue()-1 != tab[j-1].intValue()){
				i = j-1;
				c=0;
			}
			else{
				c++;
			}
		}
		
		if(c==4)
			max = tab[i].intValue();
		return max;
	}

}
