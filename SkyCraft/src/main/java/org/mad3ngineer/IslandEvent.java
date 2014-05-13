package org.mad3ngineer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IslandEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    private String player;
    private Island island;
    
    //kick, join, owner,
    private String type;
 
    public IslandEvent(String example, Island island, String type){
        player = example;
        island = island;
        this.type = type;
    }
 
    public String getPlayerName(){
        return player;
    }
 
    public Island getIsland(){
    	return island;
    }
    public String getEventType(){
    	return type;
    }
    
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
