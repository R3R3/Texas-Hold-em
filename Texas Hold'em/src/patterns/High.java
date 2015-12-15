package patterns;

import java.util.ArrayList;
import java.util.TreeMap;

import cards.and.stuff.Color;

public class High {
	
	public static int[] getHigh(TreeMap<Integer,ArrayList<Color>> map){
		int []t = new int [map.keySet().size()+1];
		t[0]=0;
		int i = 20;
		i = (map.lowerKey(i)!=null)?map.lowerKey(i).intValue():-1;
		int j= 1;
		while(i!=-1 && j<6){
			t[j]=i;
			j++;
			i = (map.lowerKey(i)!=null)?map.lowerKey(i).intValue():-1;
		}
		return t;
	}
	
	public static int[] getHigh(TreeMap<Integer,ArrayList<Color>> map,Color color){
		int []t = new int [map.keySet().size()+1];
		t[0]=5;
		int i = 20;
		i = (map.lowerKey(i)!=null)?map.lowerKey(i).intValue():-1;
		int j= 1;
		while(i!=-1 && j<6){
			if(map.get(new Integer(i)).contains(color))
			{
				t[j]=i;
				j++;
			}
			i = (map.lowerKey(i)!=null)?map.lowerKey(i).intValue():-1;
		}
		return t;
	}
	
}
