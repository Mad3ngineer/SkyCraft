package org.mad3ngineer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SCOperations {
	
	//User Operations
	
	public static void home(Player player){
		
		SCPlayer scplayer = SkyCraft.db().getPlayer(player.getName());
		
		if(scplayer.hasIsland()){
			Island island = scplayer.getIsland();
			Location home = island.getHome();
			player.sendMessage(ChatColor.GREEN+"Teleporting to your island");
			player.teleport(home);
		}else{
			player.sendMessage(ChatColor.RED+"You do not have an island! Create one with "+ChatColor.GOLD+"/island create");
		}
		
	}
	
	public static void setHome(Player player){
		
		SCPlayer scplayer = SkyCraft.db().getPlayer(player.getName());
		if(scplayer.hasIsland()){
			if(scplayer.IslandRank>=SCPlayer.RANK_OFFICER){
			
				Island island = scplayer.getIsland();
				voxel ixy = new voxel();
				ixy.x = (int) island.x;
				ixy.y = (int) island.y;
			
				Location loc = player.getLocation();
				voxel xy = new voxel();
				xy.x = (int) loc.getX();
				xy.y = (int) loc.getZ();
			
				if(IslandWorld.insideIsland(xy, ixy)){
					
					island.x = loc.getX();
					island.y = loc.getY();
					island.z = loc.getY();
			    	scplayer.updateIsland(island);
			    	player.sendMessage(ChatColor.GREEN+"Island home set!");
			    
				}else{
					player.sendMessage(ChatColor.RED+"You may only set island homes on your own island!");
				}
				
			}else{
				player.sendMessage(ChatColor.RED+"You are not a high enough rank to set your island's home");
			}
			
		}else{
			player.sendMessage(ChatColor.RED+"You have no island!");
		}
		
	}
	
	public static void visit(Player player, String target){
		
		SCPlayer sctarget = SkyCraft.db().getPlayer(target);
		
		if(sctarget.hasIsland()){
			if(sctarget.getIsland().visitable){
				player.sendMessage(ChatColor.GREEN+"Tepeporting to "+target+"'s island");
				player.sendMessage(ChatColor.GREEN+"Island Message:");
				player.sendMessage(ChatColor.GREEN+sctarget.getIsland().message);
				player.teleport(sctarget.getIsland().getHome());
			}else{
				player.sendMessage(ChatColor.BLUE+target+"'s island is closed to visitors");
			}
		}else{
			player.sendMessage(ChatColor.RED+"That player doesn't have an island");
		}
		
	}
	
	public static void createIsland(Player player){
		
		SCPlayer scplayer = SkyCraft.db().getPlayer(player.getName());
		
		if(scplayer.hasIsland()){
			player.sendMessage(ChatColor.RED+"You already have an island! Delete it if you want to create a new one.");
		}else{
			scplayer.IslandID = IslandFactory.createNewIsland(player.getName());
			scplayer.IslandRank = SCPlayer.RANK_OWNER;
			SkyCraft.db().updatePlayer(scplayer);
			player.sendMessage(ChatColor.GREEN+"Island Created!");
			player.teleport(scplayer.getIsland().getHome());
		}
		
	}
	
	public static void invitePlayer(Player player, String invited){
		
		SCPlayer scp = SkyCraft.db().getPlayer(player.getName());
		SCPlayer sci = SkyCraft.db().getPlayer(invited);
		
		if(scp.hasIsland()){
			sci.invited = scp.name;
			player.sendMessage(ChatColor.RED+"You have invited "+sci.name+" to your island!");
			SkyCraft.getInstance().getServer().getPlayer(invited).sendMessage(ChatColor.GREEN+"You have been invited to "+player.getName()+"'s island! Type /island accept to join!");
			SkyCraft.db().updatePlayer(sci);
		}else{
			player.sendMessage(ChatColor.RED+"You can only invite players if you have an island!");
		}
		
	}
	
	public static void acceptInvite(Player player){
		
		SCPlayer scp = SkyCraft.db().getPlayer(player.getName());
		SCPlayer sct = SkyCraft.db().getPlayer(scp.invited);
		
		if(sct.name.equals("")){
			player.sendMessage(ChatColor.RED+"You have no invites!");
			return;
		}else{
			if(scp.hasIsland()){
				player.sendMessage(ChatColor.RED+"You already have an island! Delete it before you accept invites!");
			}else{
				if(sct.hasIsland()){
					Island island = sct.getIsland();
					island.addMember(scp.name);
					scp.invited = null;
					scp.IslandID = island.id;
					scp.IslandRank = SCPlayer.RANK_MEMBER;
			
					player.sendMessage(ChatColor.GREEN+"You have been added to "+sct.name+"'s island!");
			
					SkyCraft.db().updateIsland(island);
					SkyCraft.db().updatePlayer(scp);
				}else{
					player.sendMessage(ChatColor.RED+sct.name+" does not have an island!");
					scp.invited = null;
				}
			}
		}
	}
	
	public static void declineInvite(Player player){
		
		SCPlayer scp = SkyCraft.db().getPlayer(player.getName());
		if(SkyCraft.db().getPlayer(scp.invited).name==""){
			return;
		}
		
		player.sendMessage(ChatColor.GREEN+"You declined the invite from "+scp.invited);
		try{
			SkyCraft.getInstance().getServer().getPlayerExact(scp.invited).sendMessage(ChatColor.RED+scp.name+" declined your invite");
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().info(ChatColor.RED+scp.name+" declined an offline player's invite");
		}
		scp.invited = null;
		
	}
	
	public static void kickPlayer(Player player, String target){
		
		SCPlayer scp = SkyCraft.db().getPlayer(player.getName());
		SCPlayer sck = SkyCraft.db().getPlayer(target);
		
		if(scp.hasIsland()){
			if(sck.hasIsland()){
				if(scp.IslandID==sck.IslandID){
					if(scp.IslandRank>sck.IslandRank&&scp.IslandRank>=SCPlayer.RANK_OFFICER){
						player.sendMessage(ChatColor.GREEN+sck.name+" has been kicked from your island.");
						try{
							SkyCraft.getInstance().getServer().getPlayer(sck.name).sendMessage(ChatColor.RED+"You have been kicked from your island :(");
						}catch(Exception e){
							SkyCraft.getInstance().getLogger().info(ChatColor.RED+"An offline player, "+sck.name+", has been kicked from their island");
						}
						Island island = scp.getIsland();
						island.removeMember(sck.name);
						sck.IslandID = -1;
						
						SkyCraft.db().updateIsland(island);
						SkyCraft.db().updatePlayer(sck);
					}else{
						player.sendMessage(ChatColor.RED+"You are not high enough rank to kick that player!");
					}
				}else{
					player.sendMessage(ChatColor.RED+"That player is not in your island!");
				}
			}else{
				player.sendMessage(ChatColor.RED+"That player is not in your island!");
			}
		}else{
			player.sendMessage(ChatColor.RED+"You do not have an island!");
		}
		
	}
	
	public static void deleteIsland(Player player){
		
		SCPlayer scp = SkyCraft.db().getPlayer(player.getName());
		
		if(scp.hasIsland()){
			if(scp.IslandRank == SCPlayer.RANK_OWNER){
				IslandFactory.deleteIsland(scp.getIsland());
			}
		}
		
	}
	
	//Dev Operations
	
	public static void tp(Player player, String target){
		
		SCPlayer sctarget = SkyCraft.db().getPlayer(target);
		player.sendMessage(ChatColor.RED+"Teleporting to "+target+"'s island");
		player.teleport(sctarget.getIsland().getHome());
		
	}
	
	public static void transferIsland(Player player, String player1, String player2){
		
		Island island = SkyCraft.db().getIsland(SCPlayerManager.getIslandID(player1));
		SCPlayer owner = SkyCraft.db().getPlayer(player1);
		SCPlayer newowner = SkyCraft.db().getPlayer(player2);
		if(newowner.hasIsland()){
			//Player2 already has an island. Do not transfer player1's island to them. Send a message to the person who ran this command
			player.sendMessage(ChatColor.RED+newowner.name+" already has an island. Delete their island before transferring "+owner.name+"'s island to them.");
		}else{
			//Player2 does not have an island. Run the logic, transfer the island ownership.
			
			newowner.IslandID = island.id;
			owner.IslandID = -1;
			
			island.owner = newowner.name;
		}
			
	}
	
	public static void deleteIsland(Player player, String target){
		//Admin command to delete island
		IslandFactory.deleteIsland(SkyCraft.db().getIsland(SkyCraft.db().getPlayer(target).IslandID));
		player.sendMessage(ChatColor.GREEN+target+"'s island has been deleted!");
		
	}

}
