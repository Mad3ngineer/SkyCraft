package org.mad3ngineer;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyCraft extends JavaPlugin {
	
	public static Plugin instance;
	public static DBInterface db;

	
	@Override
	public void onEnable() {
	    
		instance = this;
		db = new DBInterface();
		db.init(this);
        getCommand("island").setExecutor(new IslandCommand());
		
	}
	
	public static DBInterface db(){
		
		return db;
		
	}
	
	@Override
	public void onDisable() {
		
	    getLogger().info("SkyCraft has been disabled!");
		
	}
	
	public void SetEnabled(boolean e){
		
		this.setEnabled(e);
		
	}
	
	public static Plugin getInstance(){
		
		return instance;
		
	}
	
	public static World getWorld(){
		
		return getInstance().getServer().getWorld(getInstance().getConfig().getString("worldname"));
		
	}
	
	public static void exit(){
		//If the plugin fails, call this function to close it.
		SkyCraft.getInstance().onDisable();
		SkyCraft.getInstance().getServer().getPluginManager().disablePlugin(getInstance());
		
	}
	
}
