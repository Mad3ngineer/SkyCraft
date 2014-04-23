package org.mad3ngineer;

import java.util.ArrayList;

import org.bukkit.Location;

import com.sk89q.worldedit.Vector;

public class IslandFactory {
	
	public static Island createNewIsland(String owner){
		
		Island island = new Island();
		
		island.owner = owner;
		island.message = "";
		island.visitable = false;
		
		voxel pos = findEmptySpot();
		island.lx = pos.x;
		island.ly = pos.y;
		
		Vector center = island.getCenter();
		
		island.x = center.getX() + -4;
		island.z = center.getZ() + 5;
		island.y = center.getY() + 2;
		
		SkyCraft.db().updateIsland(island);
		
		WGInterface.protectIsland(island);
		WEInterface.pasteIsland(new Vector(island.getLowCorner().x, 0, island.getLowCorner().y), new Vector(island.getHighCorner().x, 0, island.getHighCorner().y), island.getCenter(), "normal.schematic");
		
		return island;
		
	}
	
	private static voxel findEmptySpot(){
		
		ArrayList<voxel> open = new ArrayList<voxel>();
		ArrayList<voxel> closed = new ArrayList<voxel>();
		
		voxel pos = new voxel();
		pos.x = 0;
		pos.y = 0;
		
		open.add(pos);
		
		do{
			
			//Check for place closest to origin
			int d = -1;
			int dv = 0;
			for(int i=0;i<open.size();i++){
				int s = abs(open.get(i).x)+abs(open.get(i).y);
				SkyCraft.getInstance().getLogger().info("Position "+open.get(i).x+", "+open.get(i).y+" distance "+s);
				if(d==-1){
					d = s;
					dv = i;
				}
				if(s<d){
					dv = i;
					d = s;
				}
			}
			pos = open.get(dv);
			
			Island i = SkyCraft.db().getIsland(pos.x+";"+pos.y);
			
			if(i.owner!=null){
				if(SkyCraft.db().getIsland(pos.x+";"+pos.y).owner.equals("")){
					//Island id returned is -1, meaning that there is no island at that location. This is a good spot.
					return pos;
				}else{
					//Island is not equal to -1, so it exists. Not a good place for a new island.
					open.remove(pos);
					closed.add(pos);
					pos.x+=1;
					open.add(pos);
					pos.x-=2;
					open.add(pos);
					pos.x+=1;
					pos.y+=1;
					open.add(pos);
					pos.y-=2;
					open.add(pos);
				}
			}else{
				return pos;
			}
			
		}while(true);
		
		
	}
	
	private static int abs(int a){
		//absolute value
		if(a>=0){
			return a;
		}else{
			return -a;
		}
		
	}

}
