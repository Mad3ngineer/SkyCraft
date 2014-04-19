package org.mad3ngineer;

public class SCPlayer {

	public static int RANK_OWNER = 1;
	public static int RANK_OFFICER = 2;
	public static int RANK_MEMBER = 3;
	public String name;
	public String invited;
	public int IslandID;
	public int IslandRank;
	
	public Island getIsland(){
		
		if(!this.hasIsland()){
			return null;
		}else{
			return SkyCraft.db().getIsland(this.IslandID);
		}
		
	}
	
	public void updateIsland(Island island){
		
		SkyCraft.db().updateIsland(island);
		
	}
	
	public boolean hasIsland(){
		
		if(this.IslandID>=0){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void createIsland(){
		
		this.IslandID = IslandFactory.createNewIsland(this.name);
		this.IslandRank = SCPlayer.RANK_OWNER;
		
	}
	
}
