package org.mad3ngineer;

import org.bukkit.Location;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.CreateClaimResult;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GPInterface {

	public static void protectIsland(Island island){
		
		String[] trusted = {island.owner};
		String[] sk = {"skycraft"};
		GriefPrevention.instance.dataStore.addClaim(new Claim(new Location(SkyCraft.getWorld(), island.getLowCorner().x, 0, island.getLowCorner().y), new Location(SkyCraft.getWorld(), island.getHighCorner().x, SkyCraft.getWorld().getMaxHeight(), island.getHighCorner().y), "skycraft", trusted, trusted, trusted, sk , (long) ((((int) island.x) << 8) + island.y), false));
		
	}
	
	public static void addPlayer(Island island, String player){
		
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(new Location(SkyCraft.getWorld(), island.getCenter().getX(), island.getCenter().getY(), island.getCenter().getZ()), false);
		claim.allowEdit(SkyCraft.getInstance().getServer().getPlayer(player));
		claim.allowBuild(SkyCraft.getInstance().getServer().getPlayer(player));
		claim.allowAccess(SkyCraft.getInstance().getServer().getPlayer(player));
		claim.allowContainers(SkyCraft.getInstance().getServer().getPlayer(player));
		GriefPrevention.instance.dataStore.saveClaim(claim);
		
	}
	
	public static void removePlayer(Island island, String player){
		
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(new Location(SkyCraft.getWorld(), island.getCenter().getX(), island.getCenter().getY(), island.getCenter().getZ()), false);
		claim.dropPermission(player);
		GriefPrevention.instance.dataStore.saveClaim(claim);
		
	}
	
}
