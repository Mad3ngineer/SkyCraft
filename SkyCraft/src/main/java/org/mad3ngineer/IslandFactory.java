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
		
		String island = null;
		
		voxel pos = new voxel();
		pos.x = 0;
		pos.y = 0;
		
		open.add(pos);
		
		do{
			
			//Check for place closest to origin
			int d = -1;
			for(voxel test: open){
				int s = abs(test.x)+abs(test.y);
				SkyCraft.getInstance().getLogger().info("Position "+test.x+", "+test.y+" distance "+s+" best "+d);
				if(d==-1){
					d = s;
					island = test.x+";"+test.y;
				}
				if(s<d){
					d = s;
					island = test.x+";"+test.y;
				}
			}
			
			Island i = SkyCraft.db().getIsland(island);
			
			if(i.owner!=null){
				if(SkyCraft.db().getIsland(pos.x+";"+pos.y).owner.equals("")){
					//Island id returned is -1, meaning that there is no island at that location. This is a good spot.
					return pos;
				}else{
					//Island is not equal to -1, so it exists. Not a good place for a new island.
					open.remove(pos);
					closed.add(pos);
					voxel x1 = new voxel(pos.x+1, pos.y);
					voxel x2 = new voxel(pos.x-1, pos.y);
					voxel y1 = new voxel(pos.x, pos.y+1);
					voxel y2 = new voxel(pos.x, pos.y-1);
					if(!closed.contains(x1))
						open.add(x1);
					if(!closed.contains(x2))
						open.add(x2);
					if(!closed.contains(y1))
						open.add(y1);
					if(!closed.contains(y2))
						open.add(y2);
					
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
