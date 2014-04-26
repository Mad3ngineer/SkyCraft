package org.mad3ngineer;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Location;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WEInterface {
	
	public static WorldEditPlugin getWorldEdit(){
		
		return (WorldEditPlugin) SkyCraft.getInstance().getServer().getPluginManager().getPlugin("WorldEdit");
		
	}

	public static void pasteIsland(Vector min, Vector max, Vector vector, String s){
		
		for(int x = min.getBlockX(); x < max.getBlockY(); x++){
			for(int z = min.getBlockZ(); z < max.getBlockZ(); z++){
				SkyCraft.getWorld().getBlockAt(x,0,z).setBiome(Biome.PLAINS);
			}
		}
		
		SchematicFormat schematic = SchematicFormat.getFormat(new File(SkyCraft.getInstance().getDataFolder()+"/schematics", s));
		CuboidClipboard clipboard = null;
		EditSession es = new EditSession(new BukkitWorld(SkyCraft.getWorld()), 1000);
		try {
			clipboard = schematic.load(new File(SkyCraft.getInstance().getDataFolder()+"/schematics", s));
		} catch (Exception e) {
			SkyCraft.getInstance().getLogger().severe("Could not load schematic from "+SkyCraft.getInstance().getDataFolder()+"/schematics"+s);
		}
		
		try{
			clipboard.paste(es, vector, true);
		}catch(MaxChangedBlocksException e){
			
		}
		
	}
	
	public static void clear(Vector min, Vector max){
		
		SkyCraft.getInstance().getLogger().info("Clearing island area");
		Region region = new CuboidRegion(max, min);
		EditSession es = new EditSession(new BukkitWorld(SkyCraft.getWorld()), 100000000);
		try {
			es.setBlocks(region, new BaseBlock(Material.AIR.getId()));
		} catch (MaxChangedBlocksException e) {
			SkyCraft.getInstance().getLogger().severe("Clear failed!");
		}
		
	}
	
	public static void setBiome(Vector min, Vector max, String name, String playername){
		
		switch(name){
		case "DESERT": break;
		case "FOREST": break;
		case "OCEAN": break;
		case "TAIGA": break;
		case "PLAINS": break;
		case "JUNGLE": break;
		case "MUSHROOM_ISLAND": break;
		default: 
			SkyCraft.getInstance().getServer().getPlayer(playername).sendMessage(ChatColor.RED+"Invalid biome! Valid biomes are: "+ChatColor.YELLOW+"DESERT, "+ChatColor.GREEN+"FOREST, "+ChatColor.BLUE+"OCEAN, "+ChatColor.WHITE+"TAIGA, "+ChatColor.GREEN+"PLAINS, "+ChatColor.DARK_GREEN+"JUNGLE, "+ChatColor.GRAY+"MUSHROOM_ISLAND"); 
			return;
		}
		
		SkyCraft.getInstance().getServer().getPlayer(playername).sendMessage(ChatColor.GREEN+"Setting biome for island to "+name);
		
		Biome.valueOf(name);
		for(int x = min.getBlockX(); x < max.getBlockY(); x++){
			for(int z = min.getBlockZ(); z < max.getBlockZ(); z++){
				SkyCraft.getWorld().getBlockAt(x,0,z).setBiome(Biome.valueOf(name));
			}
		}
		
	}
	
}
