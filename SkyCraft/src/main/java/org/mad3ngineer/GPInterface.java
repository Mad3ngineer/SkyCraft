package org.mad3ngineer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.CreateClaimResult;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GPInterface {

	public static void protectIsland(Island island){
		
		String[] sk = {"skycraft"};
		String[] blank = {""};
		
		try{
		GriefPrevention.instance.dataStore.deleteClaim(GriefPrevention.instance.dataStore.getClaimAt(new Location(SkyCraft.getWorld(), island.getCenter().getX(), island.getCenter().getY(), island.getCenter().getZ()), false));
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().info("No claim existed");
		}
		
		Claim claim = new Claim(new Location(SkyCraft.getWorld(), island.getLowCorner().x, 0, island.getLowCorner().y), new Location(SkyCraft.getWorld(), island.getHighCorner().x, SkyCraft.getWorld().getMaxHeight(), island.getHighCorner().y), "skycraft", blank, blank, blank, sk, (long) ((((int) island.x) << 8) + island.y), true);
		GriefPrevention.instance.dataStore.addClaim(claim);
		GriefPrevention.instance.dataStore.saveClaim(claim);
		
		addPlayer(island, island.owner);
		
		for(String player : island.members){
			addPlayer(island, player);
		}
		
	}
	
	public static void addPlayer(Island island, String player){
		
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(new Location(SkyCraft.getWorld(), island.getCenter().getX(), island.getCenter().getY(), island.getCenter().getZ()), true);

		if(claim==null){
			SkyCraft.getInstance().getLogger().info("claim was null");
			return;
		}
		
		Player p = SkyCraft.getInstance().getServer().getPlayer(player);
		
		SkyCraft.getInstance().getLogger().info(p.getName());
		SkyCraft.getInstance().getLogger().info(p.toString());
		SkyCraft.getInstance().getLogger().info(String.valueOf(p.hasPermission("test")));
		
		claim.allowBuild(p);
		
		GriefPrevention.instance.dataStore.saveClaim(claim);
		
	}
	
	public static void removePlayer(Island island, String player){
		
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(new Location(SkyCraft.getWorld(), island.getCenter().getX(), island.getCenter().getY(), island.getCenter().getZ()), false);
		claim.dropPermission(player);
		GriefPrevention.instance.dataStore.saveClaim(claim);
		
	}
	
}
