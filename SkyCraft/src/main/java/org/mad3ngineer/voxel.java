package org.mad3ngineer;

import org.bukkit.Location;

public class voxel{
	
	int x;
	int y;
	
	public voxel(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public voxel(){
		
	}
	
	public Location toLocation(){
		
		return new Location(SkyCraft.getWorld(), this.x, 0, this.y);
		
	}
	
}