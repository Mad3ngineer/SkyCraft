package org.mad3ngineer;

//This class will convert between island coordinates and world coordinates. 

public class IslandWorld {
	
	public static voxel getIslandCoords(voxel worldcoord){
		
		voxel islandcoords = new voxel();
		islandcoords.x = worldcoord.x / SkyCraft.getInstance().getConfig().getInt("islandsize");
		islandcoords.y = worldcoord.y / SkyCraft.getInstance().getConfig().getInt("islandsize");
		
		return islandcoords;
		
	}
	
	public static boolean insideIsland(voxel worldcoord, voxel islandcoord){
		
		islandcoord.x = islandcoord.x * SkyCraft.getInstance().getConfig().getInt("islandsize");
		islandcoord.y = islandcoord.y * SkyCraft.getInstance().getConfig().getInt("islandsize");
		
		voxel islandmin = new voxel();
		voxel islandmax = new voxel();
		
		islandmin.x = islandcoord.x - (SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		islandmin.y = islandcoord.y - (SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		islandmax.x = islandcoord.x + (SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		islandmax.y = islandcoord.y + (SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		
		if((islandmin.x < worldcoord.x && worldcoord.x < islandmax.x)||(islandmin.x > worldcoord.x && worldcoord.x > islandmax.x)){
			
			if((islandmin.y < worldcoord.y && worldcoord.y < islandmax.y)||(islandmin.y > worldcoord.y && worldcoord.y > islandmax.y)){
				
				return true;
				
			}
			
		}
		return false;
		
		
	}

}
