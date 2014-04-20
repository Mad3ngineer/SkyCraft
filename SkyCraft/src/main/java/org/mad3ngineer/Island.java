package org.mad3ngineer;

import java.util.ArrayList;

import org.bukkit.Location;

public class Island {
	
	String owner;
	String message;
	double x;
	double y;
	double z;
	int lx;
	int ly;
	boolean visitable;
	ArrayList<String> members;
	
	public Island(){
		
		owner = "";
		members = new ArrayList<String>();
		
	}
	
	public void addMember(String name){
		
		members.add(name);
		WGInterface.addPlayer(this, name);
		SkyCraft.db().updateIsland(this);
		
	}
	
	public void removeMember(String name){
		
		members.remove(name);
		WGInterface.removePlayer(this, name);
		SkyCraft.db().updateIsland(this);
		
	}
	
	public Location getHome(){
		
		
		return new Location(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("worldname")), this.x, this.y, this.z);
		
	}
	
	public void delete(){
		
		for(int i = 0; i < members.size(); i++){
			SCPlayer p = SkyCraft.db().getPlayer(members.get(i));
			if((p.IX == this.lx)&&(p.IY == this.ly)){
				p.IX = -1;
				p.IY = -1;
				SkyCraft.db().updatePlayer(p);
			}
		}
		SkyCraft.db().deleteIsland(this);
		IslandFactory.deleteIsland(this);
		
	}
	
	public voxel getLowCorner(){
		
		voxel corner = new voxel();
		corner.x = (lx * SkyCraft.getInstance().getConfig().getInt("islandsize"))-(SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		corner.y = (ly * SkyCraft.getInstance().getConfig().getInt("islandsize"))-(SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		
		return corner;
		
	}
	
	public voxel getHighCorner(){
		
		voxel corner = new voxel();
		corner.x = (lx * SkyCraft.getInstance().getConfig().getInt("islandsize"))+(SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		corner.y = (ly * SkyCraft.getInstance().getConfig().getInt("islandsize"))+(SkyCraft.getInstance().getConfig().getInt("islandsize")/2);
		
		return corner;
		
	}
	
	public boolean insideIsland(voxel worldcoord){
		
		voxel min = getLowCorner();
		voxel max = getHighCorner();

		if((min.x < worldcoord.x && worldcoord.x < max.x)||(min.x > worldcoord.x && worldcoord.x > max.x)){
			
			if((min.y < worldcoord.y && worldcoord.y < max.y)||(min.y > worldcoord.y && worldcoord.y > max.y)){
				
				return true;
				
			}
			
		}
		return false;
		
		
	}
	
	public void protect(){
		
		WGInterface.protectIsland(this);
		
	}

}
