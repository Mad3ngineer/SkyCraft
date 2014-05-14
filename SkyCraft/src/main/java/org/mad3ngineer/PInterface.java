package org.mad3ngineer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PInterface implements Listener{

	static PInterface instance;
	static ArrayList<SCPlayer> players;
	
	public static void init() {
		
		players = new ArrayList<SCPlayer>();
		for(Player p : SkyCraft.getInstance().getServer().getOnlinePlayers()){
			players.add(SkyCraft.db().getPlayer(p.getName()));
		}
		
		instance = new PInterface();
		SkyCraft.getInstance().getServer().getPluginManager().registerEvents(instance, SkyCraft.getInstance());
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		
		for(SCPlayer p:players){
			if(p.name.equals(event.getPlayer().getName())){
				players.remove(p);
			}
		}
		
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		
		for(SCPlayer p:players){
			if(p.name.equals(event.getPlayer().getName())){
				players.remove(p);
			}
		}
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		
		players.add(SkyCraft.db().getPlayer(event.getPlayer().getName()));
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		
		if(checkWorld(event.getBlock().getLocation().getWorld().getName())){
			
			Player player = event.getPlayer();
			Block block = event.getBlock();
			
			for(SCPlayer p : players){
				
				if(p.name.equals(player.getName())){
					if(!inside(block, p)){
						if(!player.hasPermission("skycraft.islandev.edit")){
							event.setCancelled(true);
							player.sendMessage(protect());
						}
					}
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onBlockExplode(EntityExplodeEvent event){
		
		if(checkWorld(event.getLocation().getWorld().getName())){
			
			event.blockList().clear();
			
		}
		
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		
		if(checkWorld(event.getClickedBlock().getLocation().getWorld().getName())){
			
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			
			for(SCPlayer p : players){
				
				if(p.name.equals(player.getName())){
					if(!inside(block, p)){
						if(!player.hasPermission("skycraft.islandev.edit")){
							event.setCancelled(true);
							player.sendMessage(protect());
						}
					}
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		
		if(checkWorld(event.getRightClicked().getWorld().getName())){
			
			Player player = event.getPlayer();
			Block block = event.getRightClicked().getLocation().getBlock();
			
			for(SCPlayer p : players){
				
				if(p.name.equals(player.getName())){
					if(!inside(block, p)){
						if(!player.hasPermission("skycraft.islandev.edit")){
							event.setCancelled(true);
							player.sendMessage(protect());
						}
					}
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event){
		
		if(checkWorld(event.getBlockClicked().getWorld().getName())){
			
			Player player = event.getPlayer();
			Block block = event.getBlockClicked();
			
			for(SCPlayer p : players){
				
				if(p.name.equals(player.getName())){
					if(!inside(block, p)){
						if(!player.hasPermission("skycraft.islandev.edit")){
							event.setCancelled(true);
							player.sendMessage(protect());
						}
					}
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
		
		if(checkWorld(event.getBlockClicked().getWorld().getName())){
			
			Player player = event.getPlayer();
			Block block = event.getBlockClicked();
			
			for(SCPlayer p : players){
				
				if(p.name.equals(player.getName())){
					if(!inside(block, p)){
						if(!player.hasPermission("skycraft.islandev.edit")){
							event.setCancelled(true);
							player.sendMessage(protect());
						}
					}
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onIslandEvent(IslandEvent event){
		
		for(SCPlayer p:players){
			if(p.name.equals(event.getPlayerName())){
				players.remove(p);
			}
		}
		
		players.add(SkyCraft.db().getPlayer(event.getPlayerName()));
		
	}
	
	public static String protect(){
		
		return SkyCraft.getInstance().getConfig().getString("protected");
		
	}
	
	public static boolean checkWorld(String world){
		
		if(world.equals(SkyCraft.getInstance().getConfig().getString("worldname"))){
			return true;
		}
		return false;
		
	}
	
	public static boolean inside(Block block, SCPlayer p){
		
		Location min = p.getLowCorner().toLocation();
		Location max = p.getHighCorner().toLocation();
		
		if(p.hasIsland()){
			if(min.getBlockX()<block.getX()&&block.getX()<max.getBlockX()){
				if(min.getBlockZ()<block.getZ()&&block.getZ()<max.getBlockZ()){
					return true;
				}
			}
		}
		return false;
		
	}

}
