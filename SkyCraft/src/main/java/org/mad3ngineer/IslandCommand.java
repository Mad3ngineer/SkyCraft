package org.mad3ngineer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class IslandCommand implements CommandExecutor{
	
	String userperm = "skycraft.user.";
	String devperm = "skycraft.dev.";
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1){
			
			return false;
			
		}

		if (sender instanceof Player) {
			
	        Player player = (Player) sender;
	        
	        if(cmd.getName().equalsIgnoreCase("island")){
	        	
	        	if(args[0].equalsIgnoreCase("sethome")){
	        		if(player.hasPermission(userperm+"sethome")){
	        			SCOperations.setHome(player);
	        			return true;
	        		}else{
	        			player.sendMessage(ChatColor.RED+"You do not have permission!");
	        			return true;
	        		}
	        	}
	        	
	        	else if(args[0].equalsIgnoreCase("visit")){
	        		if(player.hasPermission(userperm+"visit")){
	        			SCOperations.visit(player, args[0]);
	        			return true;
	        		}else{
	        			player.sendMessage(ChatColor.RED+"You do not have permission!");
	        			return true;
	        		}
	        	}
	        	
	        	else if(args[0].equalsIgnoreCase("home")){
	        		if(player.hasPermission(userperm+"home")){
	        		    SCOperations.home(player);
	        		    return true;
	        		}else{
	        			player.sendMessage(ChatColor.RED+"You do not have permission!");
	        			return true;
	        		}
	        	}
	        	
	        	else if(args[0].equalsIgnoreCase("create")){
	        		if(player.hasPermission(userperm+"create")){
	        			SCOperations.createIsland(player);
	        			return true;
	        		}else{
	        			player.sendMessage(ChatColor.RED+"You do not have Permission!");
	        			return true;
	        		}
	        	}
	        	
	        }else if(cmd.getName().equalsIgnoreCase("islandev")){
	        	
	        	if(args[0].equalsIgnoreCase("tp")){
	        		if(player.hasPermission(devperm+"tp")){
	        			SCOperations.tp(player, args[0]);
	        			return true;
	        		}else{
	        			player.sendMessage(ChatColor.RED+"You do not have permission!");
	        			return true;
	        		}
	        	}else if(args[0].equalsIgnoreCase("transfer")){
	        		//Transfer an island from player 1 to player 2
	        		if(3<=args.length){
	        			String player1 = args[1];
	        			String player2 = args[2];
	        			if(player.hasPermission(devperm+"transfer")){
	        				SCOperations.transferIsland(player, player1, player2);
	        				return true;
	        			}else{
	        				player.sendMessage(ChatColor.RED+"You do not have permission!");
	        				return true;
	        			}
	        		}else{//Not enough arguments
	        			return false;
	        		}
	        	}else if(args[0].equalsIgnoreCase("delete")){
	        		//Delete "player"'s island. Usage: /islandev delete <player>
	        		if(2<=args.length){ //Check if there are enough arguments
	        			if(player.hasPermission(devperm+"delete")){
	        				SCOperations.deleteIsland(player, args[1]);
	        				return true;
	        			}else{
	        				player.sendMessage(ChatColor.RED+"You do not have permission!");
	        				return true;
	        			}
	        		}else{//if loop failed, there werent enough arguments
	        			return false;
	        		}
	        	}
	        	
	        }
	        
	    } else {
	    	
	        sender.sendMessage("Cannot run this command from console");
	        return false;
	        
	    }
		
	    return false;
		
	}

}
