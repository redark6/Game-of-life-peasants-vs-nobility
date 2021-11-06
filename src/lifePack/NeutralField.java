package lifePack;

public class NeutralField extends Case{
	private final int minPlowAction = 4;
	private final int maxPlowAction = 20;
	private int plowBeforeBecomingExploitable;
	
	private NeutralField(int x,int y) {
		this.posX = x;
		this.posY = y;
		this.symbol = '-';
		this.plowBeforeBecomingExploitable = initPlowBeforeBecomingExploitable();
		this.current = this;
	}
	
	public static NeutralField CreateNeutralField(int x,int y) {
		return new NeutralField(x,y); 
	}
	
	private int initPlowBeforeBecomingExploitable(){
		return minPlowAction + rand.nextInt(maxPlowAction - minPlowAction);
	}
	
	public void plowField() {
		this.plowBeforeBecomingExploitable--;
		if(plowBeforeBecomingExploitable <= 0) {
			this.current = WheatField.CreateWheatField(posX,posY);
		}
	}

	public int getPlowBeforeBecomingExploitable() {
		return plowBeforeBecomingExploitable;
	}
	
}
