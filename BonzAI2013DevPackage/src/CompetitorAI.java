import java.util.ArrayList;
import java.util.Collection;

import bonzai.api.AI;
import bonzai.api.Duck;
import bonzai.api.Entity;
import bonzai.api.Farmhand;
import bonzai.api.FarmhandAction;
import bonzai.api.GameState;
import bonzai.api.Item;
import bonzai.api.Position;
import bonzai.api.Tile;
import bonzai.api.*;


public class CompetitorAI implements AI {

	@Override
	public Collection<FarmhandAction> turn(GameState state) {
		ArrayList<FarmhandAction> actions = new ArrayList<FarmhandAction>();
		
		for (Farmhand farmhand : state.getMyFarmhands().getNotStumbled()) {
			Entity ent = farmhand.getHeldObject();
			if(farmhand.getHeldObject() instanceof Item){
				Item i = (Item)ent;
				switch(i.getType()){
				case Bucket:
					break;
				case DuckCall:
					break;
				case Egg:
					break;
				case Lantern:
					break;
				case Pitchfork:
					break;
				default:
					try {
						throw new Exception("Unrecognized Item in the switch");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for(Duck d: state.getDucks()){
				}
			}
			actions.add(farmhand.shout("Hello"));
		}
		
		return actions;
	}
	
	
}
