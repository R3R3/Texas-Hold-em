package cards.and.stuff;

public class Card {
	
	private int figure;
	private Color color;
	protected Card(int figure, Color color){
		this.figure = figure;
		this.color = color;
	}
	
	public int getFigure(){
		return figure;
	}
	
	public Color getColor(){
		return color;
	}	
}
