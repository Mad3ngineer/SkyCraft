package org.mad3ngineer;

import java.util.Iterator;

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
		
		ProtectedRegion global = WGBukkit.getRegionManager(SkyCraft.getWorld()).getRegion("__global__");
		
		global.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
		global.setFlag(DefaultFlag.BUILD, State.DENY);
		global.setFlag(DefaultFlag.USE, State.DENY);
		
	}
	
	public static void protectIsland(Island island){
		
		ProtectedRegion region = null;
        DefaultDomain owners = new DefaultDomain();
        
        try {
			WGBukkit.getRegionManager(SkyCraft.getWorld()).save();
		} catch (ProtectionDatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        region = new ProtectedCuboidRegion(regionName(island), new BlockVector(island.getLowCorner().x, 0, island.getLowCorner().y), new BlockVector(island.getHighCorner().x, 0, island.getHighCorner().y));
        
        owners.addPlayer(island.owner);
        region.setOwners(owners);
        try {
			region.setParent(WGBukkit.getRegionManager(SkyCraft.getWorld()).getRegion("__Global__"));
			region.setPriority(100);
			
			region.setFlag(DefaultFlag.GREET_MESSAGE, island.message);
			region.setFlag(DefaultFlag.PVP, State.DENY);
			region.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
			region.setFlag(DefaultFlag.USE, State.DENY);
			region.setFlag(DefaultFlag.BUILD, State.DENY);
			ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getApplicableRegions(SkyCraft.db().getPlayer(island.owner).getIsland().getHome());
			
			if(WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).hasRegion(regionName(island))){
				WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).removeRegion(regionName(island));
			}
			
			WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).addRegion(region);
            SkyCraft.getInstance().getLogger().info("Created region for "+island.owner+"'s island");
            WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).save();
            WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).load();
            
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public static String regionName(Island island){
		
		return "island_"+island.lx+"_"+island.ly;
		
	}
	
	public static void removePlayer(Island island, String player){
		
		 DefaultDomain owners = WGBukkit.getPlugin().getRegionManager(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("worldname"))).getRegion(regionName(island)).getOwners();
         owners.removePlayer(player);
         WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).setOwners(owners);
		
	}
	
	public static void addPlayer(Island island, String player){
		
		SkyCraft.getInstance().getLogger().info("Adding "+player+" to region for "+regionName(island));
		protectIsland(island);
		DefaultDomain owners = WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).getOwners();
        owners.addPlayer(player);
        WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).setOwners(owners);
        SkyCraft.getInstance().getLogger().info("Player added to region!");
		
	}

}
