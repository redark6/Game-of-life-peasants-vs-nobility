package lifePack;

import java.util.List;

public abstract class Character extends Case{
	
	protected int reproductTime;
	protected boolean isAlive;
	
	public abstract List<Case> choseActionTurn(NeighborsLists neighbor);
	public abstract boolean canReproduce();
	public abstract void move(NeighborsLists neighbor);
	
	public void deadCharacterBecomeClay() {
		current = NeutralField.CreateNeutralField(posX,posY);
	}

}
