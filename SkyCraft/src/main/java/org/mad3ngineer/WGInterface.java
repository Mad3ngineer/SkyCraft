package org.mad3ngineer;

import java.util.Iterator;

import org.bukkit.ChatColor;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGInterface {
	
	
	public static void init(){
		
		WGBukkit.getPlugin().onEnable();
		
		SkyCraft.getInstance().getServer().getPluginManager().enablePlugin(WGBukkit.getPlugin());
		
		try{
			ProtectedRegion global = WGBukkit.getRegionManager(SkyCraft.getWorld()).getRegion("__global__");
		
			global.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
			global.setFlag(DefaultFlag.BUILD, State.DENY);
			global.setFlag(DefaultFlag.USE, State.DENY);
			WGBukkit.getRegionManager(SkyCraft.getWorld()).save();
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe(ChatColor.RED+"Could not load region '__global__'");
		}
		
	}
	
	public static void protectIsland(Island island){
		
		ProtectedRegion region = null;
        DefaultDomain members = new DefaultDomain();
        
        try {
			WGBukkit.getRegionManager(SkyCraft.getWorld()).save();
		} catch (ProtectionDatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        region = new ProtectedCuboidRegion(regionName(island), new BlockVector(island.getLowCorner().x, 0, island.getLowCorner().y), new BlockVector(island.getHighCorner().x, SkyCraft.getWorld().getMaxHeight(), island.getHighCorner().y));
        
        //region.setFlag(DefaultFlag.BUILD, State.DENY);
        
        members.addPlayer(island.owner);
        
        for(String player : island.members){
			members.addPlayer(player);
		}
        
        region.setMembers(members);
        
        try {
			region.setParent(WGBukkit.getRegionManager(SkyCraft.getWorld()).getRegion("__Global__"));
			region.setPriority(100);
				
			if(WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).hasRegion(regionName(island))){
				WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).removeRegion(regionName(island));
			}
			
			WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).addRegion(region);
            
            WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).save();
            
            SkyCraft.getInstance().getLogger().info("Created region for "+island.owner+"'s island");
            
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public static String regionName(Island island){
		
		return "island_"+island.lx+"_"+island.ly;
		
	}
	
	public static void removePlayer(Island island, String player){
		
		 DefaultDomain members = WGBukkit.getPlugin().getRegionManager(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("worldname"))).getRegion(regionName(island)).getMembers();
         members.removePlayer(player);
         WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).setOwners(members);
		
	}
	
	public static void addPlayer(Island island, String player){
		
		SkyCraft.getInstance().getLogger().info("Adding "+player+" to region for "+regionName(island));
		protectIsland(island);
		DefaultDomain members = WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).getMembers();
        members.addPlayer(player);
        WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).setOwners(members);
        SkyCraft.getInstance().getLogger().info("Player added to region!");
		
	}

}
