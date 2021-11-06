package lifePack;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
	
	int sizeX;
	int sizeY;
	int numberTurn;
	
	Case terrain[][];
	
	public GameBoard(int sizeX, int sizeY,int numberTurn) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.numberTurn = numberTurn;
		
		this.terrain = new Case[sizeX][sizeY];
		initFiled();
	}
	
	public void play() {
		showGameBoard();
		
		for (int i = 0; i < numberTurn; i++) {
			playTurn();
			showGameBoard();
		}
		
	}

	private void playTurn() {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				if(terrain[x][y].getClass().getSuperclass().equals(Character.class)) {
					if(terrain[x][y] instanceof Character) {
						Character character = (Character) terrain[x][y];
						List<Case> result;
						if(character instanceof NobilityMember) {
							result = character.choseActionTurn(this.findNeigbor5X5(x,y));
						}else {
							result = character.choseActionTurn(this.findNeigbor3X3(x,y));
						}
						result.forEach(element ->{
							terrain[element.posX][element.posY] = element;
						});
					}
				}
			}
		}
	}

	private void showGameBoard() {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				terrain[x][y].display();
			}
			System.out.println();
		}
		System.out.println('\n');
	}
	
	private NeighborsLists findNeigbor3X3(int x, int y) {
		List<Case> neighbors = new ArrayList<>();
		for (int yPos = -1; yPos < 2; yPos++) {
			for (int xPos = -1; xPos < 2; xPos++) {
				if( ( (x+xPos) != x || (y+yPos) != y ) && ((x+xPos) >= 0 && (x+xPos) < sizeX ) && ((y+yPos) >= 0 && (y+yPos) < sizeY ) ) {
					neighbors.add(terrain[x+xPos][y+yPos]);
				}
			}
		}
		return new NeighborsLists(neighbors);
	}

	private NeighborsLists findNeigbor5X5(int x, int y) {
		List<Case> neighbors = new ArrayList<>();
		for (int yPos = -2; yPos < 3; yPos++) {
			for (int xPos = -2; xPos < 3; xPos++) {
				if( ( (x+xPos) != x || (y+yPos) != y ) && ((x+xPos) >= 0 && (x+xPos) < sizeX ) && ((y+yPos) >= 0 && (y+yPos) < sizeY ) ) {
					neighbors.add(terrain[x+xPos][y+yPos]);
				}
			}
		}
		return new NeighborsLists(neighbors);
	}

	private void initFiled() {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				terrain[x][y] = InitCaseOfField(x,y);
			}
		}
	}
	
	private Case InitCaseOfField(int x,int y) {
			double state = Math.random();
			
			if(state>=0 && state <0.45) {
				return NeutralField.CreateNeutralField(x,y);
			}else if(state>=0.45 && state <0.75) {
				return WheatField.CreateWheatField(x,y);
			}else if(state>=0.75 && state <0.9) {
				return Peasant.createPeasant(x,y);
			}else {
				return NobilityMember.createNobilityMember(x,y);
			}
	}
	
	

}
