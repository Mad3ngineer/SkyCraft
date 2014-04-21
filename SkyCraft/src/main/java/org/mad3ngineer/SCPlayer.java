package org.mad3ngineer;

public class SCPlayer {

	public static int RANK_OWNER = 1;
	public static int RANK_OFFICER = 2;
	public static int RANK_MEMBER = 3;
	public static int HAS_ISLAND = 1;
	public static int NO_ISLAND = 0;
	public String name;
	public String invited;
	public int hasIsland;
	public int IX;
	public int IY;
	public int IslandRank;
	
	public Island getIsland(){
		
		if(!this.hasIsland()){
			return null;
		}else{
			return SkyCraft.db().getIsland(this.IX+";"+this.IY);
		}
		
	}
	
	public void updateIsland(Island island){
		
		SkyCraft.db().updateIsland(island);
		
	}
	
	public boolean hasIsland(){
		
		if(this.hasIsland==SCPlayer.HAS_ISLAND){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void createIsland(){
		
		voxel v = new voxel();
		IslandFactory.createNewIsland(this.name);
		this.IslandRank = SCPlayer.RANK_OWNER;
		
	}
	
}
