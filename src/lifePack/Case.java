package lifePack;

import java.util.Random;

public abstract class Case {
	public static final Random rand = new Random();
	protected char symbol;
	protected Case current;
	protected int posX;
	protected int posY;
	
	public void display() {
		System.out.print(this.symbol);
	}
}
