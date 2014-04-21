package org.mad3ngineer;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.sk89q.worldedit.Vector;

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
	
	public void addMember(SCPlayer scp){
		
		SkyCraft.getInstance().getLogger().info("Adding member");
		members.add(scp.name);
		SkyCraft.getInstance().getLogger().info("Member added");
		WGInterface.addPlayer(this, scp.name);
		
		scp.hasIsland = SCPlayer.HAS_ISLAND;
		scp.IX = this.lx;
		scp.IY = this.ly;
		SkyCraft.getInstance().getLogger().info("Player linked to island");
		scp.invited = "";
		scp.IslandRank = SCPlayer.RANK_MEMBER;
		SkyCraft.db().updatePlayer(scp);
		
	}
	
	public void removeMember(SCPlayer scp){
		
		members.remove(scp.name);
		WGInterface.removePlayer(this, scp.name);
		
		scp.hasIsland = SCPlayer.NO_ISLAND;
		
		SkyCraft.db().updatePlayer(scp);
		SkyCraft.db().updateIsland(this);
		
	}
	
	public Location getHome(){
		
		return new Location(SkyCraft.getWorld(), this.x, this.y, this.z);
		
	}
	
	public void delete(){
		
		for(int i = 0; i < members.size(); i++){
			SCPlayer p = SkyCraft.db().getPlayer(members.get(i));
			if((p.IX == this.lx)&&(p.IY == this.ly)){
				p.hasIsland = SCPlayer.NO_ISLAND;
				SkyCraft.db().updatePlayer(p);
			}
		}
		SCPlayer powner = SkyCraft.db().getPlayer(this.owner);
		powner.hasIsland = SCPlayer.NO_ISLAND;
		SkyCraft.db().updatePlayer(powner);
		
		SkyCraft.db().deleteIsland(this);
		SkyCraft.getInstance().getLogger().info("Island deleted from database");
		
		WEInterface.clear(new Vector(getLowCorner().x, 0, getLowCorner().y), new Vector(this.getHighCorner().x, SkyCraft.getWorld().getMaxHeight(), this.getHighCorner().y));
		
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
	
	public Vector getCenter(){
		
		return new Vector(lx * SkyCraft.getInstance().getConfig().getInt("islandsize"), SkyCraft.getInstance().getConfig().getInt("islandheight"), ly * SkyCraft.getInstance().getConfig().getInt("islandsize"));
		
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
	
	private static int abs(int a){
		//absolute value
		if(a>=0){
			return a;
		}else{
			return -a;
		}
		
	}

}
