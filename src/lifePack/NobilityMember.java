package lifePack;

import java.util.List;

public class NobilityMember extends Character {
	
	
	private int MAXMONEYCANPOSSES = 25;
	private int AMOUNTOFMONEYFORCHILD = 7;
	private int NUMBERTURNTOALLOWREPRODUCTION = 5;
	private int INITBASEMONEY = 8;
	
	private int money;

	public static NobilityMember createNobilityMember(int x,int y) {
		return new NobilityMember(x,y);
	}

	private NobilityMember(int x,int y) {
		this.posX = x;
		this.posY = y;
		this.symbol = 'R';
		this.money = INITBASEMONEY;
		this.reproductTime = NUMBERTURNTOALLOWREPRODUCTION;
		this.isAlive = true;
		this.current = this;
	}
	
	private void bornNobilityMember(int baseWheatAmount,Case location,int x,int y) {
		location = new NobilityMember(baseWheatAmount,x,y);
	}
	
	private NobilityMember(int baseMoneyAmount,int x,int y) {
		this.posX = x;
		this.posY = y;
		this.symbol = 'R';
		this.money = baseMoneyAmount;
		this.reproductTime = NUMBERTURNTOALLOWREPRODUCTION;
		this.isAlive = true;
		this.current = this;
	}
	
	public List<Case> choseActionTurn(NeighborsLists neighbor) {
		
		if(neighbor.nobilityMembers.size() >= 4 && isAlive) {
			nobleConspiracy(neighbor.nobilityMembers);
		}
		
		if(neighbor.peasants.size() >= 10 && isAlive) {
			peasantRevoltion();
			System.out.println("revolution");

		}
		
		if(this.isAlive) {
			
			if(neighbor.nobilityMembersAbleToReproduce.size() > 0 && this.canReproduce() && ( neighbor.neutralFields.size() > 0 || neighbor.wheatFields.size() > 0)) {
				reproduce(neighbor);
				System.out.println("babyNobility");
			}else if(neighbor.peasants.size() > 1 && money < 8) {
				collectMoney(neighbor.peasants);
			}else {
				move(neighbor);
			}
		}
		
		if(neighbor.peasants.size() > 0) {
			loseMoney(2);
		}else {
			loseMoney(1);
		}

		if(this.reproductTime > 0)
			this.reproductTime--;
		
		if(this.money <= 0) {
			this.kill();
			deadCharacterBecomeClay();
			neighbor.neutralFields.add(current);
		}else {
			neighbor.nobilityMembers.add((NobilityMember) current);
		}
		
		return neighbor.NeigborListAsList();
	}
	
	
	private void nobleConspiracy(List<NobilityMember> nobilityMembers) {
		findWeakestNobleAndKillHim(nobilityMembers);
		allNoblesLosePartOfWealth(nobilityMembers);
		System.out.println("noblesConspiracy");
	}

	private void allNoblesLosePartOfWealth(List<NobilityMember> nobilityMembers) {
		nobilityMembers.forEach(member-> {
			if(member.isAlive)
				member.loseMoney(member.money/2);
		});
		if(isAlive)
			loseMoney(money/2);
	}

	private void peasantRevoltion() {
		this.kill();
	}

	public boolean canReproduce() {
		return (this.reproductTime <= 0 && this.money >= 8);
	}

	private void reproduce(NeighborsLists neighbor) {
		NobilityMember partner = neighbor.nobilityMembersAbleToReproduce.get(0);
		this.loseMoney(AMOUNTOFMONEYFORCHILD);
		partner.loseMoney(AMOUNTOFMONEYFORCHILD);
		if(neighbor.neutralFields.size() > 0) {
			int chosen = rand.nextInt(neighbor.neutralFields.size());
			this.bornNobilityMember(AMOUNTOFMONEYFORCHILD * 2, neighbor.neutralFields.get(chosen),neighbor.neutralFields.get(chosen).posX,neighbor.neutralFields.get(chosen).posY);
		}else {
			int chosen = rand.nextInt(neighbor.wheatFields.size());
			this.bornNobilityMember(AMOUNTOFMONEYFORCHILD * 2, neighbor.wheatFields.get(chosen),neighbor.wheatFields.get(chosen).posX,neighbor.wheatFields.get(chosen).posY);
		}
	}

	private void loseMoney(int amount) {
		this.money -= amount;
	}

	private void collectMoney(List<Peasant> peasants) {
		int incrementLimit = peasants.size() < Math.abs( MAXMONEYCANPOSSES - money) ? peasants.size() : Math.abs( MAXMONEYCANPOSSES - money);
		for (int i = 0; i < incrementLimit; i++) {
			peasants.get(i).loseWheat(1);
			this.money++;
		}
	}

	private void findWeakestNobleAndKillHim(List<NobilityMember> nobilityMemberNeighbor) {
		int weakestNobilityMemberIndex = 0;
		int weakestNobilityMemberMoneyAmount = MAXMONEYCANPOSSES;
		for (int i = 0; i < nobilityMemberNeighbor.size(); i++) {
			if(nobilityMemberNeighbor.get(i).getMoney() < weakestNobilityMemberMoneyAmount) {
				weakestNobilityMemberIndex = i;
				weakestNobilityMemberMoneyAmount = nobilityMemberNeighbor.get(i).getMoney();
			}
		}
		
		if(this.getMoney() < weakestNobilityMemberMoneyAmount) {
			this.kill();
		}else {
			nobilityMemberNeighbor.get(weakestNobilityMemberIndex).kill();
		}
	}

	public int getMoney() {
		return money;
	}

	public void kill() {
		this.isAlive = false;
	}
	
	public void move(NeighborsLists neighbor) {
		int tempX = this.posX,tempY = this.posY;
		Case chosen = null;

		if(neighbor.wheatFields.size() > 0) {
			chosen = neighbor.wheatFields.remove(rand.nextInt(neighbor.wheatFields.size()));
		}else if(neighbor.neutralFields.size() > 0) {
			chosen = neighbor.neutralFields.remove(rand.nextInt(neighbor.neutralFields.size()));
		}
		
		if(chosen != null) {
			this.posX = chosen.posX;
			this.posY = chosen.posY;
			chosen = NeutralField.CreateNeutralField(tempX,tempY);
			neighbor.neutralFields.add(chosen);
		}
	}
}
