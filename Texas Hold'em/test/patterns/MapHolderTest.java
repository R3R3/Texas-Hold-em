package patterns;

import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.Test;

import cards.and.stuff.Color;

public class MapHolderTest {

	@Test
	public void mapTest(){
		MapHolder holder = new MapHolder(MapMakerTest.genList());
		TreeMap<Integer, ArrayList<Color>> map = holder.getMap();
		MapMakerTest.tryMapTest(map);
	}

}
