package lifePack;

import java.util.ArrayList;
import java.util.List;

public class NeighborsLists {
	
	public List<Case> wheatFields;
	public List<Case> neutralFields;
	public List<Peasant> peasants;
	public List<Peasant> peasantsAbleToreproduce;
	public List<NobilityMember> nobilityMembers;
	public List<NobilityMember> nobilityMembersAbleToReproduce;
	
	public NeighborsLists(List<Case> neighbors) {
		
		this.wheatFields = new ArrayList<>();
		this.neutralFields = new ArrayList<>();
		this.peasants = new ArrayList<>();
		this.peasantsAbleToreproduce = new ArrayList<>();
		this.nobilityMembers = new ArrayList<>();
		this.nobilityMembersAbleToReproduce = new ArrayList<>();
		neighbors.forEach( (neighbor) ->{
			
			if(neighbor.getClass() == WheatField.class) {
				wheatFields.add((WheatField)neighbor);
			}else if(neighbor.getClass() == NeutralField.class) {
				neutralFields.add((NeutralField)neighbor);
			}else if(neighbor.getClass() == Peasant.class) {
				Peasant peasant = (Peasant)neighbor;
				peasants.add(peasant);
				if(peasant.canReproduce())
					peasantsAbleToreproduce.add(peasant);
			}else if(neighbor.getClass() == NobilityMember.class) {
				NobilityMember nobilityMember = (NobilityMember)neighbor;
				nobilityMembers.add(nobilityMember);
				if(nobilityMember.canReproduce())
					nobilityMembersAbleToReproduce.add(nobilityMember);
			}
			
		});
	}
	
	public List<Case> NeigborListAsList() {
		List<Case> result = new ArrayList<>();
		result.addAll(this.wheatFields);
		result.addAll(this.neutralFields);
		result.addAll(this.peasants);
		result.addAll(this.nobilityMembers);
		return result;
	}

}
