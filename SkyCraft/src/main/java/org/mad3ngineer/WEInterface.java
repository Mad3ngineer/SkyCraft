package org.mad3ngineer;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
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

	public static void pasteIsland(Vector vector, String s){
		
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
	
}
