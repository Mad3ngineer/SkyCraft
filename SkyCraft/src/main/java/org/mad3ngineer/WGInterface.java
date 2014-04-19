package org.mad3ngineer;

import java.util.Iterator;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGInterface {
	
	
	
	public static void protectIsland(Island island){
		
		ProtectedRegion region = null;
        DefaultDomain owners = new DefaultDomain();
		
        
        region = new ProtectedCuboidRegion(regionName(island), new BlockVector(island.getLowCorner().x, 0, island.getLowCorner().y), new BlockVector(island.getHighCorner().x, 0, island.getHighCorner().y));
        
        owners.addPlayer(island.owner);
        region.setOwners(owners);
        try {
			region.setParent(WGBukkit.getRegionManager(SkyCraft.getWorld()).getRegion("__Global__"));
			region.setPriority(100);
			region.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(WGBukkit.getPlugin(), SkyCraft.getInstance().getServer().getPlayer(island.owner) , island.message));
			region.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(WGBukkit.getPlugin(), SkyCraft.getInstance().getServer().getPlayer(island.owner), "allow"));
			region.setFlag(DefaultFlag.CHEST_ACCESS, DefaultFlag.CHEST_ACCESS.parseInput(WGBukkit.getPlugin(), SkyCraft.getInstance().getServer().getPlayer(island.owner), "deny"));
			region.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(WGBukkit.getPlugin(), SkyCraft.getInstance().getServer().getPlayer(island.owner), "deny"));
			ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getApplicableRegions(SkyCraft.db().getPlayer(island.owner).getIsland().getHome());
			 
			if(set.size() > 0){
				
				for(Iterator iterator = set.iterator(); iterator.hasNext();){
                 		
					ProtectedRegion regions = (ProtectedRegion)iterator.next();
                    
					if(!regions.getId().equalsIgnoreCase("__global__")){
						
						WGBukkit.getPlugin().getRegionManager(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("worldname"))).removeRegion(regions.getId());
					}
					
                }

            }
			
			WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).addRegion(region);
            SkyCraft.getInstance().getLogger().info("Created region for "+island.owner+"'s island");
            WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).save();
            
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public static String regionName(Island island){
		
		return "island_"+island.lx+";"+island.ly;
		
	}
	
	public static void removePlayer(Island island, String player){
		
		 DefaultDomain owners = WGBukkit.getPlugin().getRegionManager(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("worldname"))).getRegion(regionName(island)).getOwners();
         owners.removePlayer(player);
         WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).setOwners(owners);
		
	}
	
	public static void addPlayer(Island island, String player){
		
		DefaultDomain owners = WGBukkit.getPlugin().getRegionManager(SkyCraft.getInstance().getServer().getWorld(SkyCraft.getInstance().getConfig().getString("worldname"))).getRegion(regionName(island)).getOwners();
        owners.addPlayer(player);
        WGBukkit.getPlugin().getRegionManager(SkyCraft.getWorld()).getRegion(regionName(island)).setOwners(owners);
		
	}

}
