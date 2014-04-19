package org.mad3ngineer;

public class SCPlayerManager {
	
	public static int getIslandID(String target){
		
		SCPlayer player = SkyCraft.db().getPlayer(target);
		return player.IslandID;
		
	}
	
	public static int getIslandRank(String target){
		
		SCPlayer player = SkyCraft.db().getPlayer(target);
		return player.IslandRank;
		
	}
	
	public static Island getIsland(String target){
		
		return SkyCraft.db().getIsland(SkyCraft.db().getPlayer(target).IslandID);
		
	}
	
	public static void visit(SCPlayer player, String target){
		
		
		
	}

}
