package patterns;

import java.util.ArrayList;
import java.util.TreeMap;

import cards.and.stuff.Color;

public class MultiChecker {

	public static int [] isFour(TreeMap<Integer,ArrayList<Color>> map){
		int v1=-1;
		int v2=-1;
		for(Integer i : map.keySet())
		{
			if(map.get(i).size()==4){
				v1 = i.intValue();
			}
			else{
				if(i.intValue()>v2)
					v2 =  i.intValue();
			}
		}
		if(v1!=-1)
			return new int[]{7,v1,v2};
		else
			return null;
	}
	
	public static int [] gotThree(TreeMap<Integer,ArrayList<Color>> map){
		int v1=-1;
		int v2=-1;
		int v3=-1;
		int v4=-1;
		int v;
		int s;
		for(Integer i : map.keySet())
		{
			s = map.get(i).size();
			v = i.intValue();
			if(s==3){
				v1 = v;
			}
			else if(s==2){
				v2 = v;
			}
			else{
				if(v>v3)
				{
					v4=v3;
					v3=v;
				}
			}
			
		}
		if(v1 ==-1)
			return null;
		else if(v2 != -1)
			return new int[]{6,v1,v2};
		else
			return new int[]{3,v1,v3,v4};
	}
	
	public static int [] gotPair(TreeMap<Integer,ArrayList<Color>> map){
		int v1=-1;
		int v2=-1;
		int v3=-1;
		int v4=-1;
		int v5=-1;
		int s;
		int v;
		for(Integer i : map.keySet())
		{
			s= map.get(i).size();
			v = i.intValue();
			if(s==2){
				if(v>v1){
					v2=v1;
					v1=v;
				}else if(v>v2){
						v2=v;
					}
			}
			else{
				if(v>v3)
				{
					v5=v4;
					v4=v3;
					v3=v;
				} else if(v>v4){
						v5=v4;
						v4=v;
					}
			}
		}
		if(v1==-1)
			return null;
		else if(v2 != -1)
			return new int []{2,v1,v2,v3};
			else return new int[]{1,v1,v3,v4,v5};
				
	}
	

}
