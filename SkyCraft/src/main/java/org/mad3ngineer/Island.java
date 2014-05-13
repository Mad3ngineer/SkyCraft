package org.mad3ngineer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
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
	
	public void setOwner(String owner){
		
		SkyCraft.getInstance().getServer().getPluginManager().callEvent(new IslandEvent(owner, this, "owner"));
		
		SCPlayer oldo = SkyCraft.db().getPlayer(this.owner);
		SCPlayer newo = SkyCraft.db().getPlayer(owner);
		
		oldo.IslandRank = SCPlayer.RANK_MEMBER;
		newo.IslandRank = SCPlayer.RANK_OWNER;
		this.owner = owner;
		
		oldo.save();
		newo.save();
		this.save();
		
	}
	
	public void setHome(Location loc){
		
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		save();
		
	}
	
	public void save(){
		
		SkyCraft.db().updateIsland(this);
		
	}
	
	public void addMember(SCPlayer scp){
		
		SkyCraft.getInstance().getServer().getPluginManager().callEvent(new IslandEvent(scp.name, this, "join"));
		
		members.add(scp.name);
		ClaimInterface.addPlayer(this, scp.name);
		
		scp.hasIsland = SCPlayer.HAS_ISLAND;
		scp.IX = this.lx;
		scp.IY = this.ly;
		scp.invited = "";
		scp.IslandRank = SCPlayer.RANK_MEMBER;
		scp.save();
		save();
		
	}
	
	public void removeMember(SCPlayer scp){
		
		SkyCraft.getInstance().getServer().getPluginManager().callEvent(new IslandEvent(scp.name, this, "kick"));
		
		scp.sendMessage(ChatColor.DARK_RED+"You have been kicked from your island!");
		
		members.remove(scp.name);
			
		ClaimInterface.removePlayer(this, scp.name);
		
		scp.hasIsland = SCPlayer.NO_ISLAND;
		
		scp.save();
		save();
		
	}
	
	public Location getHome(){
		
		return new Location(SkyCraft.getWorld(), this.x, this.y, this.z);
		
	}
	
	public void setBiome(String name, String playername){
		
		WEInterface.setBiome(new Vector(this.getLowCorner().x, 0, this.getLowCorner().y), new Vector(this.getHighCorner().x, 0, this.getHighCorner().y), name, playername);
		
	}
	
	public void delete(){
		
		for(int i = 0; i < members.size(); i++){
			SCPlayer p = SkyCraft.db().getPlayer(members.get(i));
			if((p.IX == this.lx)&&(p.IY == this.ly)){
				p.leaveIsland();
				SkyCraft.getInstance().getServer().getPlayer(p.name).teleport(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("spawnworld")).getSpawnLocation());
			}
		}
		SkyCraft.db().getPlayer(this.owner).leaveIsland();
		SkyCraft.getInstance().getServer().getPlayer(this.owner).teleport(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("spawnworld")).getSpawnLocation());
		
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
		
		ClaimInterface.protectIsland(this);
		
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
