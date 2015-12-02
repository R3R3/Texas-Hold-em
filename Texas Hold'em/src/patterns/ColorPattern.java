package patterns;
import java.util.ArrayList;
import java.util.TreeMap;

import cards.and.stuff.*;

public class ColorPattern {

	public static Color ifSame(MapHolder holder){
		Color res = null;
		int i =0;
		TreeMap<Integer,ArrayList<Color>> map = holder.getMap();
		int size = map.keySet().size();
		
		if(size < 5)
			return null;
		
		int [] tab = {0,0,0,0};
		for(Color c : Color.values())
		{
			for(Integer v : map.keySet()){
				if(map.get(v).contains(c))
				{
					tab[i]++;
					if(tab[i]==5)
						res = c;
				}
			}
			i++;
		}
		return res;
	}
}
