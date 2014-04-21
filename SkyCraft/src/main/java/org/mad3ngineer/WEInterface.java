package org.mad3ngineer;

import java.io.File;
import java.io.IOException;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class WEInterface {

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
	
}
