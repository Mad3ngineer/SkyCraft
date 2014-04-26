package org.mad3ngineer;

public class ClaimInterface {

	static String type;
	
	public static void init(){
		
		type = SkyCraft.getInstance().getConfig().getString("claimsplugin");
		if(type.equalsIgnoreCase("worldguard")){
			type = "worldguard";
			WGInterface.init();
		}
		if(type.equalsIgnoreCase("griefprevention")){
			type = "griefprevention";
		}else{
			type = "worldguard";
			WGInterface.init();
			SkyCraft.getInstance().getLogger().info("Config setting 'claimsplugin' was an invalid value. Defaulting to worldguard.");
		}
		
	}
	
	public static void protectIsland(Island island){
		
		if(type.equalsIgnoreCase("worldguard")){
			WGInterface.protectIsland(island);
		}else if(type.equalsIgnoreCase("griefprevention")){
			GPInterface.protectIsland(island);
		}
		
	}
	
	public static void removePlayer(Island island, String player){
		
		if(type.equalsIgnoreCase("worldguard")){
			WGInterface.removePlayer(island, player);
		}else if(type.equalsIgnoreCase("griefprevention")){
			GPInterface.addPlayer(island, player);
		}
		
	}
	
	public static void addPlayer(Island island, String player){
		
		if(type.equalsIgnoreCase("worldguard")){
			WGInterface.addPlayer(island, player);
		}else if(type.equalsIgnoreCase("griefprevention")){
			GPInterface.addPlayer(island, player);
		}
		
	}
	
}
