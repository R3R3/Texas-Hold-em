package patterns;

import java.util.ArrayList;
import java.util.TreeMap;

import cards.and.stuff.Color;

public class PokerPattern {
	
	public static int ifIs(TreeMap<Integer,ArrayList<Color>> map){
		Color color = ColorPattern.ifSame(map);
		if(color == null)
			return -1;
		else{
			int i = map.firstKey().intValue();
			int j = i-1;
			int c = 0;
			while(c!=5 && i != -5){
				if(map.get(i).contains(color))
				{
					if(i-j==1)
						c++;
					else
						c=1;
					
					j=i;
					i = (map.higherKey(i)==null)?-5:map.higherKey(i).intValue();
				}
				else
				{
					i=(map.higherKey(i)==null)?-5:map.higherKey(i).intValue();
					c=0;
					j=i-1;
				}
			}
			i = (i!=-5)?map.lowerKey(i):map.lastKey();
			
			if(c != 5 &&
				map.get(12).contains(color) &&
				map.get(0).contains(color) &&
				map.get(1).contains(color) &&
				map.get(2).contains(color) &&
				map.get(3).contains(color) )
			{
				c=5;
				i=3;
			}
			
			return (c==5)?i:-1;
		}
	}
	
}
