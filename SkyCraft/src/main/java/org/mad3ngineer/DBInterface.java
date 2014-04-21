package org.mad3ngineer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.Vector;

import lib.PatPeter.SQLibrary.Database;
import lib.PatPeter.SQLibrary.MySQL;

public class DBInterface {
	
	private static Database sql;
	
	public void init(Plugin instance){
		
		sql = new MySQL(Logger.getLogger("Minecraft"), 
	            "[SkyCraft] ", 
	            SkyCraft.getInstance().getConfig().getString("db_host"),
	            SkyCraft.getInstance().getConfig().getInt("db_port"), 
	            SkyCraft.getInstance().getConfig().getString("db_database"), 
	            SkyCraft.getInstance().getConfig().getString("db_username"), 
	            SkyCraft.getInstance().getConfig().getString("db_password"));
		
		if(!sql.isOpen()){
			sql.open();
		}
		
		dbQuery("CREATE TABLE IF NOT EXISTS sc_islands (visitable BOOLEAN, home varchar(150), location varchar(50), owner varchar(50), members varchar(300), message varchar(300))");

		dbQuery("CREATE TABLE IF NOT EXISTS sc_players (islandloc varchar(50), name varchar(50) PRIMARY KEY, islandrank varchar(50), invite varchar(50), hasisland int)");
			
	}
	
	public Island getIsland(String location){
		
		Island island = new Island();
		
		String[] l = location.split(";");
		
		island.lx = Integer.parseInt(l[0]);
		island.ly = Integer.parseInt(l[1]);
		
		ResultSet res = dbQuery("SELECT * FROM sc_islands WHERE location='"+location+"'");
		
		String members = "";
		String home = "0;0;0";
		
		try{
			if(res.next()){
				if(res.getString("owner")!=null)
					island.owner = res.getString("owner");
				if(res.getString("message")!=null)
					island.message = res.getString("message");
				
				island.visitable = (res.getInt("visitable")==1);
				
				if(res.getString("members")!=null)
					members = res.getString("members");
				if(res.getString("home")!=null)
					home = res.getString("home");
			}
		}catch(Exception e){
			
		}
		
		SkyCraft.getInstance().getLogger().info("splitting members: "+members);
		
		ArrayList<String> m = new ArrayList<String>();
		
		String[] ma = members.split(";");
		
		for(int i=0;i<ma.length;i++){
			m.add(ma[i]);
		}
		
		SkyCraft.getInstance().getLogger().info("splitting home");

		String[] h = home.split(";");
		
		try{
			island.x = Double.parseDouble(h[0]);
			island.y = Double.parseDouble(h[1]);
			island.z = Double.parseDouble(h[2]);
		}catch(Exception e){
			island.x = 1;
			island.y = 1;
			island.z = 1;
		}
		
		SkyCraft.getInstance().getLogger().info("Home is "+island.x+", "+island.y+", "+island.z);
		
		SkyCraft.getInstance().getLogger().info("Island Fetched");
		
		return island;
		
	}
	
	public Island updateIsland(Island island){
		
		ResultSet row = dbQuery("SELECT * FROM sc_islands WHERE location='"+island.lx+";"+island.ly+"'");
		
		int visit = 0;
		
		if(island.visitable)
			visit = 1;
		
		String members = "";
		
		if(island.members.size()>=1){
			members = island.members.get(0);
		}
		if(island.members.size()>1){
			for(int i = 1; i < island.members.size(); i++){
				members = members + ";" + island.members.get(i);
			}
		}
		
		try{
			if(row.next()){
				dbQuery("UPDATE sc_islands SET home='"+island.x+";"+island.y+";"+island.z+";', message='"+island.message+"', owner='"+island.owner+"', visitable='"+visit+"', members='"+members+"' WHERE location='"+island.lx+";"+island.ly+"';");
			}else{
				dbQuery("INSERT INTO sc_islands (home, location, message, owner, visitable) VALUES ('"+island.x+";"+island.y+";"+island.z+"','"+island.lx+";"+island.ly+"','"+island.message+"','"+island.owner+"','"+visit+"');");
			}
		}catch(Exception e){
			
		}
		
		return island;
		
	}
	
	public SCPlayer getPlayer(String name){
		
		name = name.replace(" ", "");
		
		SCPlayer player = new SCPlayer();
		
		ResultSet res = dbQuery("SELECT * FROM sc_players WHERE name='"+name+"'");
		
		player.name = name;
		
		String loc = "0;0";
		
		try{
			res.next();
			if(res.getString("islandloc")!=null)
				loc = res.getString("islandloc");
				
			player.hasIsland = res.getInt("hasisland");
			player.IslandRank = res.getInt("islandrank");
				
			if(res.getString("invite")!=null)
				player.invited = res.getString("invite");
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe("Could not get all info for player "+name);
			SkyCraft.getInstance().getLogger().severe(e.toString());
			player.IslandRank = 0;
			player.invited = "";
		}
		
		String[] pos = loc.split(";");
		
		player.IX = Integer.parseInt(pos[0]);
		player.IY = Integer.parseInt(pos[1]);
		
		return player;
		
	}
	
	public void updatePlayer(SCPlayer player){
		
		ResultSet res = dbQuery("SELECT * FROM sc_players WHERE name='"+player.name+"'");
		
		try{
			if(res.next()){
				dbQuery("UPDATE sc_players SET islandrank='"+player.IslandRank+"', invite='"+player.invited+"', islandloc='"+player.IX+";"+player.IY+"', hasisland='"+player.hasIsland+"' WHERE name='"+player.name+"';");
			}else{
				dbQuery("INSERT INTO sc_players (islandrank, invite, name, islandloc, hasisland) VALUES ('"+player.IslandRank+"', '"+player.invited+"', '"+player.name+"', '"+player.IX+";"+player.IY+"', '"+player.hasIsland+"')");
			}
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe(e.getMessage());
		}
		
	}
	
	public void deleteIsland(Island island){
		
		dbQuery("DELETE FROM sc_islands WHERE location='"+island.lx+";"+island.ly+"'");
		
	}
	
	public ResultSet dbQuery(String query){
		
		ResultSet result = null;
		
		if (!sql.isOpen()) {
		    sql.open();
		}
		
		try {
			/*Temporary line, remove after debug*/
			SkyCraft.getInstance().getLogger().info(query);
			result = sql.query(query);
		} catch (SQLException e) {
			SkyCraft.getInstance().getLogger().severe("Could not run query '"+query+"' on database "+SkyCraft.getInstance().getConfig().getString("db_database"));
			SkyCraft.getInstance().getLogger().severe(e.toString());
			
			SkyCraft.exit();
		}
		
		return result;
		
	}
	
	//Old code
	
	
	/*public int getIslandID(String Location){
		
		int id = -1;
		
		ResultSet result = dbQuery("SELECT * FROM sc_islands WHERE location='"+Location+"';");
		
		try{
			if(result.next()){
				id = result.getInt("id");
			}else{
				id = -1;
			}
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe("Could not use result from table sc_islands row where location = "+Location);
			SkyCraft.getInstance().getLogger().severe(e.toString());
			id = -1;
		}
		
		return id;
		
	}
	
	public Island getIsland(int id){
		
		Island island = new Island();
		String home = "";
		String message = "";
		String owner = "";
		String memberslist = "";
		boolean visitable = false;
		
		ResultSet result = dbQuery("SELECT * FROM sc_islands WHERE id = '"+id+"';");
		
		try{
		
			result.next();
		
			if(result.getString("owner")!=null){
				try {
					home = result.getString("home");
					visitable = result.getBoolean("visitable");
					owner = result.getString("owner");
					message = result.getString("message");
					memberslist = result.getString("members");
				} catch (SQLException e) {
					SkyCraft.getInstance().getLogger().severe("Could not use result from table sc_islands row id "+id);
					SkyCraft.getInstance().getLogger().severe(e.toString());
				}
			
			}
			
		}catch(Exception e){
			
		}
		
		double x;
		double y;
		double z;
		
		String[] coords = home.split(";");
		
		try{
			x = Double.parseDouble(coords[0]);
			y = Double.parseDouble(coords[1]);
			z = Double.parseDouble(coords[2]);
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe("Could not cast coordinates to integers, aborting.");
			SkyCraft.getInstance().getLogger().severe(e.toString());
			return null;
		}
		
		//List of members that are in the island, stored in an array
		String[] members = memberslist.split(";");
		
		for(int i = 0; i < members.length; i++){
			island.members.add(members[i]);
		}
		
		
		//Add information
		island.id = id;
		island.message = message;
		island.owner = owner;
		island.visitable = visitable;
		island.x = x;
		island.y = y;
		island.z = z;
		
		return island;	
		
	}
	
	public SCPlayer getPlayer(String target){
		
		SCPlayer player = new SCPlayer();
		String name = target;
		int islandid = -1;
		String invite = "";
		int islandrank = SCPlayer.RANK_MEMBER;
		
		ResultSet result = dbQuery("SELECT * FROM sc_players WHERE name='"+target+"'");
		
		try {
			invite = result.getString("invite");
			islandid = result.getInt("islandid");
			islandrank = result.getInt("islandrank");
		} catch (Exception e) {
			SkyCraft.getInstance().getLogger().info("Could not get player entry for "+target+", creating default player object");
		}
		
		player.invited = invite;
		player.IslandRank = islandrank;
		player.IslandID = islandid;
		player.name = name;
		
		return player;
		
	}
	
	public void updateIsland(Island island){

		//Update the database record for the island, based on id
		String members = "";
		
		int visit = 0;
		
		if(island.visitable == true){
			visit = 1;
		}
		
		if(island.members.size()>0){
			members = island.members.get(0);
			
			for(int i = 1; i < island.members.size(); i++){
				members = members+";"+island.members.get(i);
			}
		}
		dbQuery("UPDATE sc_islands SET home='"+island.x+";"+island.y+";"+island.z+";', message='"+island.message+"', owner='"+island.owner+"', visitable='"+visit+"', members='"+members+"' WHERE id='"+island.id+"';");
		
	}
	
	public void updatePlayer(SCPlayer player){
		
		//Update the database record for the given player, based on name
		ResultSet r = dbQuery("SELECT * FROM sc_players WHERE name='"+player.name+"'");
		
		try{
			if(r.next()){
			
				dbQuery("UPDATE sc_players SET islandid='"+player.IslandID+"', islandrank='"+player.IslandRank+"', invite='"+player.invited+"' WHERE name='"+player.name+"';");
		
			}else{
				dbQuery("INSERT INTO sc_players (islandid, islandrank, invite, name) VALUES ('"+player.IslandID+"', '"+player.IslandRank+"', '"+player.invited+"', '"+player.name+"')");
			}
		}catch(Exception e){
			
		}
		
	}

	public int addIsland(Island island){
		
		int visit = 0;
		
		if(island.visitable == true){
			visit = 1;
		}

		int id = -1;
		
		String location = island.lx+";"+island.ly;
		
		dbQuery("INSERT INTO sc_islands (home, location, message, owner, visitable) VALUES ('"+island.x+";"+island.y+";"+island.z+"','"+location+"','"+island.message+"','"+island.owner+"','"+visit+"');");
		
		ResultSet result = dbQuery("SELECT * FROM sc_islands WHERE location='"+location+"'");
		
		try{
			id =  result.getInt("id");
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe("Could not get id from added row in sc_islands at position "+island.lx+","+island.ly);
			SkyCraft.getInstance().getLogger().severe(e.toString());
		}
		
		return id;
		
	}*/
	
}
