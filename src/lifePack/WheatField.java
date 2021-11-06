package lifePack;

public class WheatField extends Case{
	private final int minWheatAmountForField = 2;
	private final int maxWheatAmountForField = 14;
	private int amountOfWheatCanBeExploited;
	
	private WheatField(int x,int y){
		this.posX = x;
		this.posY = y;
		this.symbol = '#';
		this.amountOfWheatCanBeExploited = initAmountOfWheatCanBeExploited();
		this.current = this;
	}
	
	public static WheatField CreateWheatField(int x,int y) {
		return new WheatField(x,y); 
	}
	
	private int initAmountOfWheatCanBeExploited(){
		return minWheatAmountForField + rand.nextInt(maxWheatAmountForField - minWheatAmountForField);
	}
	
	public void exploitField() {
		this.amountOfWheatCanBeExploited--;
		if(amountOfWheatCanBeExploited <= 0) {
			this.current = NeutralField.CreateNeutralField(posX,posY);
		}
	}

	public int getAmountOfWheatCanBeExploited() {
		return amountOfWheatCanBeExploited;
	}

}
