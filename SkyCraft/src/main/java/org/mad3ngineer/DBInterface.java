package org.mad3ngineer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

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
		
		if(!sql.checkTable("sc_islands")){
			dbQuery("CREATE TABLE IF NOT EXISTS sc_islands (id INTEGER PRIMARY KEY AUTOINCREMENT, visitable BOOLEAN, home CHAR[150], location CHAR[50], owner CHAR[50], members CHAR[300])");
		}
		if(!sql.checkTable("sc_players")){
			dbQuery("CREATE TABLE IF NOT EXISTS sc_players (name CHAR[50], islandid INTEGER, islandrank CHAR[50], invite CHAR[50]");
		}
		
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
			SkyCraft.getInstance().getLogger().severe("\n----------StackTrace----------");
			SkyCraft.getInstance().getLogger().severe(e.toString());
			
			SkyCraft.exit();
		}
		
		return result;
		
	}
	
	public int getIslandID(String Location){
		
		int id = -1;
		
		ResultSet result = dbQuery("SELECT * FROM sc_islands WHERE location = '"+Location+"';");
		try{
			id = result.getInt("id");
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe("Could not use result from table sc_islands row where location = "+Location);
			SkyCraft.getInstance().getLogger().severe(e.toString());
		}
		
		return id;
		
	}
	
	public Island getIsland(int id){
		
		Island island = new Island();
		String home = null;
		String message = null;
		String owner = null;
		String memberslist = null;
		boolean visitable = false;
		
		ResultSet result = dbQuery("SELECT * FROM sc_islands WHERE id = '"+id+"';");
		
		try {
			home = result.getString("home");
			visitable = result.getBoolean("visitable");
			owner = result.getString("owner");
			message = result.getString("message");
			memberslist = result.getString("members");
		} catch (SQLException e) {
			SkyCraft.getInstance().getLogger().severe("Could not use result from table sc_islands row id "+id);
			SkyCraft.getInstance().getLogger().severe(e.toString());
			return null;
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
		String invite = null;
		String islandrank = null;
		
		ResultSet result = dbQuery("SELECT * FROM sc_players WHERE name = "+target);
		
		try {
			invite = result.getString("invite");
			islandid = result.getInt("islandid");
			islandrank = result.getString("islandrank");
		} catch (SQLException e) {
			SkyCraft.getInstance().getLogger().severe("Could not use result from table sc_players where name = "+target);
			SkyCraft.getInstance().getLogger().severe(e.toString());
			return null;
		}
		
		player.invited = invite;
		player.name = name;
		player.IslandID = islandid;
		player.IslandRank = Integer.parseInt(islandrank);
		
		return player;
		
	}
	
	public void updateIsland(Island island){

		//Update the database record for the island, based on id
		String members = "";
		
		if(island.members.size()>0){
			members = island.members.get(0);
			
			for(int i = 1; i < island.members.size(); i++){
				members = members+";"+island.members.get(i);
			}
		}
		dbQuery("UPDATE sc_islands SET home='"+island.x+";"+island.y+";"+island.z+";', message='"+island.message+"', owner='"+island.owner+"', visitable='"+island.visitable+"', members='"+members+"' WHERE id='"+island.id+"';");
		
	}
	
	public void updatePlayer(SCPlayer player){
		
		//Update the database record for the given player, based on name
		dbQuery("UPDATE sc_players SET islandid='"+player.IslandID+"', islandrank='"+player.IslandRank+"', invite='"+player.invited+"' WHERE name='"+player.name+"';");
		
	}

	public int addIsland(Island island){

		int id = -1;
		
		ResultSet result = dbQuery("INSERT INTO sc_islands (home, location, message, owner, visitable) VALUES ('"+island.x+";"+island.y+";"+island.z+"','"+island.lx+";"+island.ly+"','"+island.message+"','"+island.owner+"','"+island.visitable+"');");
		try{
			id =  result.getInt("id");
		}catch(Exception e){
			SkyCraft.getInstance().getLogger().severe("Could not get id from added row in sc_islands at position "+island.lx+","+island.ly);
			SkyCraft.getInstance().getLogger().severe(e.toString());
		}
		
		return id;
		
	}
	
	public void deleteIsland(Island island){
		
		ResultSet result = dbQuery("DELETE FROM sc_islands WHERE owner='"+island.owner+"'");
		
	}
	
}
