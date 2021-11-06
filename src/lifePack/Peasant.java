package lifePack;

import java.util.List;
import java.util.Random;

public class Peasant extends Character{
	
	private int MAXWHEATCANPOSSES = 10;
	private int MINWHEATCANCOLLECT = 1;
	private int MAXWHEATCANCOLLECT = 4;
	private int AMOUNTOFWHEATFORCHILD = 3;
	private int NUMBERTURNTOALLOWREPRODUCTION = 3;
	private int INITBASEWHEAT = 6;
	
	private int wheat;
	private int explotationCoefficient;
	
	public static Peasant createPeasant(int x,int y) {
		return new Peasant(x,y);
	}

	private Peasant(int x,int y) {
		this.posX = x;
		this.posY = y;
		this.symbol = 'P';
		this.wheat = INITBASEWHEAT;
		this.reproductTime = NUMBERTURNTOALLOWREPRODUCTION;
		this.isAlive = true;
		this.current = this;
		this.explotationCoefficient = MINWHEATCANCOLLECT + rand.nextInt(MAXWHEATCANCOLLECT - MINWHEATCANCOLLECT);
	}
	
	private void bornPeasant(int baseWheatAmount,Case location,int x,int y) {
		location = new Peasant(baseWheatAmount,x,y);
	}
	
	private Peasant(int baseWheatAmount,int x,int y) {
		this.posX = x;
		this.posY = y;
		this.symbol = 'P';
		this.wheat = baseWheatAmount;
		this.reproductTime = NUMBERTURNTOALLOWREPRODUCTION;
		this.isAlive = true;
		this.current = this;
		this.explotationCoefficient = MINWHEATCANCOLLECT + rand.nextInt(MAXWHEATCANCOLLECT - MINWHEATCANCOLLECT);
	}
	

	public List<Case> choseActionTurn(NeighborsLists neighbor) {
		
		if(neighbor.peasants.size() >= 4 && isAlive) {
			peasantFamine(neighbor.peasants);
		}
		
		if(this.isAlive) {
			
			if(neighbor.peasantsAbleToreproduce.size() > 0 && this.canReproduce() && ( neighbor.neutralFields.size() > 0 || neighbor.wheatFields.size() > 0)) {
				reproduce(neighbor);
				System.out.println("babyPeasant");
			}else if(neighbor.wheatFields.size() > 0) {
				if(wheat <8) {
					collectWheat(neighbor.wheatFields);
					if(wheat == 10) {
						neighbor.nobilityMembers.add(NobilityMember.createNobilityMember(this.posX, this.posY));
						System.out.println("becomeNoble");
					}
				}else {
					move(neighbor);
				}

				//System.out.println("collectWheat");
			}else if(neighbor.neutralFields.size() > 0) {
				NeutralField fieldChosen= findFieldThatCanBePlowBeforeHungerDeath(neighbor.neutralFields);
				if(fieldChosen != null) {
					fieldChosen.plowField();
					//System.out.println("work");
				}else {
					move(neighbor);
					System.out.println("movePeasant");
				}
				
			}else {
				move(neighbor);
				System.out.println("movePeasant");
			}
		}
		
		this.wheat--;
		
		if(this.reproductTime > 0)
			this.reproductTime--;
		
		if(this.wheat <= 0) {
			System.out.println("diePeasant");
			this.kill();
			deadCharacterBecomeClay();
			neighbor.neutralFields.add(current);
		}else {
			neighbor.peasants.add((Peasant)current);
		}
		
		return neighbor.NeigborListAsList();
	}

	private void peasantFamine(List<Peasant> peasants) {
		System.out.println("famine");	
		findWeakestPeasantAndKillHim(peasants);
		allPeasantLosePartOfWealth(peasants);
	}
	
	private void allPeasantLosePartOfWealth(List<Peasant> peasants) {
		peasants.forEach(member-> {
			if(member.isAlive)
				member.loseWheat(member.wheat/2);
		});
		if(isAlive)
			loseWheat(wheat/2);
	}


	private void reproduce(NeighborsLists neighbor) {
		Peasant partner = neighbor.peasantsAbleToreproduce.get(0);
		this.loseWheat(AMOUNTOFWHEATFORCHILD);
		partner.loseWheat(AMOUNTOFWHEATFORCHILD);
		if(neighbor.neutralFields.size() > 0) {
			int chosen = rand.nextInt(neighbor.neutralFields.size());
			this.bornPeasant(AMOUNTOFWHEATFORCHILD * 2, neighbor.neutralFields.get(chosen),neighbor.neutralFields.get(chosen).posX,neighbor.neutralFields.get(chosen).posY);
		}else {
			int chosen = rand.nextInt(neighbor.wheatFields.size());
			this.bornPeasant(AMOUNTOFWHEATFORCHILD * 2, neighbor.wheatFields.get(chosen),neighbor.wheatFields.get(chosen).posX,neighbor.wheatFields.get(chosen).posY);
		}
	}

	private void collectWheat(List<Case> wheatFields) {
		int incrementLimit = explotationCoefficient < wheatFields.size() ? explotationCoefficient : wheatFields.size();
		int weathShift = Math.abs(MAXWHEATCANPOSSES - wheat);
		if(incrementLimit > weathShift)
			incrementLimit = weathShift;
		for (int i = 0; i < incrementLimit; i++) {
			WheatField field = (WheatField)wheatFields.get(i);
			field.exploitField();
			wheatFields.set(i, field);
			this.wheat++;
		}
	}

	private NeutralField findFieldThatCanBePlowBeforeHungerDeath(List<Case> neutralFields) {
		for (int i = 0; i < neutralFields.size(); i++) {
			NeutralField field = (NeutralField)neutralFields.get(i);
			if(field.getPlowBeforeBecomingExploitable() < wheat)
				return field;
		}
		return null;
	}

	private void findWeakestPeasantAndKillHim(List<Peasant> peasantsNeighbor) {
		int weakestPeasantIndex = 0;
		int weakestPeasantWheatAmount = MAXWHEATCANPOSSES;
		for (int i = 0; i < peasantsNeighbor.size(); i++) {
			if(peasantsNeighbor.get(i).getWheat() < weakestPeasantWheatAmount) {
				weakestPeasantIndex = i;
				weakestPeasantWheatAmount = peasantsNeighbor.get(i).getWheat();
			}
		}
		
		if(this.getWheat() < weakestPeasantWheatAmount) {
			this.kill();
		}else {
			peasantsNeighbor.get(weakestPeasantIndex).kill();
		}
	}
	
	public void kill() {
		this.isAlive = false;
	}

	public int getWheat() {
		return wheat;
	}
	
	public boolean canReproduce() {
		return (this.reproductTime <= 0 && this.wheat >= 6 );
	}
	
	public void loseWheat(int amount) {
		this.wheat-= amount;
	}
	
	public void move(NeighborsLists neighbor) {
		int tempX = this.posX,tempY = this.posY;
		Case chosen = null;
		if(neighbor.neutralFields.size() > 0) {
			chosen = neighbor.neutralFields.remove(rand.nextInt(neighbor.neutralFields.size()));
		}else if(neighbor.wheatFields.size() > 0) {
			chosen = neighbor.wheatFields.remove(rand.nextInt(neighbor.wheatFields.size()));
		}
		
		if(chosen != null) {
			this.posX = chosen.posX;
			this.posY = chosen.posY;
			chosen = NeutralField.CreateNeutralField(tempX,tempY);
			neighbor.neutralFields.add(chosen);
		}
	}
}
