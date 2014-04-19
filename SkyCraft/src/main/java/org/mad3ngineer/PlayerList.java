package org.mad3ngineer;

import java.util.ArrayList;
import java.util.HashMap;

import com.sk89q.worldguard.bukkit.WGBukkit;

public class PlayerList {
	
	//Stores a list of the players online and the assets required to run skyblock for them
	
	static HashMap<String,SCPlayer> players = new HashMap();
	
	public static void addPlayer(SCPlayer player){
		
		//Load player
		players.put(player.name, player);
		
	}
	
	public static SCPlayer getPlayer(String target){
		
		return players.get(target);
		
	}
	
	public static void removePlayer(SCPlayer player){
		
		players.remove(player.name);
		
		
	}
	

}
