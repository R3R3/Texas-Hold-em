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
			
			
			if(c != 5)
			{
				boolean [] p = {false,false,false,false,false};
				boolean str = true;
				for(Integer in : map.keySet())
				{
					switch(in.intValue()){
						case 12:{						
							if(map.get(12).contains(color))
								p[0]= true;
							break;
						}
						case 0:{						
							if(map.get(0).contains(color))
								p[1]= true;
							break;
						}
						case 1:{						
							if(map.get(1).contains(color))
								p[2]= true;
							break;
						}
						case 2:{						
							if(map.get(2).contains(color))
								p[3]= true;
							break;
						}
						case 3: {						
							if(map.get(3).contains(color))
								p[4]= true;
							break;
						}
						default:
							break;
					}
						
				}
				for(boolean b : p){
					if(!b)
						str = false;
				}
				
				if(str){
					c=5;
					i=3;	
				}
			}
			
			return (c==5)?i:-1;
		}
	}
	
}
