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
		
		open.add(new voxel(0,0));
		
		do{
			
			voxel pos = new voxel();
			
			//Check for place closest to origin
			int d = -1;
			
			for(voxel test: closed){
				SkyCraft.getInstance().getLogger().info("Closed: "+test.x+","+test.y);
			}
			
			for(voxel test: open){
				int s = abs(test.x)+abs(test.y);
				SkyCraft.getInstance().getLogger().info("Position "+test.x+", "+test.y+" distance "+s+" best "+d);
				if(d==-1){
					d = s;
					pos = test;
				}
				if(s<d){
					d = s;
					pos = test;
				}
			}
			
			SkyCraft.getInstance().getLogger().info("Closest found; testing.");
			
			Island i = SkyCraft.db().getIsland(pos.x+";"+pos.y);
			
			if(i.owner!=null){
				if(i.owner.equals("")){
					return pos;
				}else{
					voxel x1 = new voxel(pos.x+1, pos.y);
					voxel x2 = new voxel(pos.x-1, pos.y);
					voxel y1 = new voxel(pos.x, pos.y+1);
					voxel y2 = new voxel(pos.x, pos.y-1);
					if(!contains(closed, x1)){
						if(!contains(open, x1)){
							open.add(x1);
						}
					}
					if(!contains(closed, x2)){
						if(!contains(open, x2)){
							open.add(x2);
						}
					}
					if(!contains(closed, y1)){
						if(!contains(open, y1)){
							open.add(y1);
						}
					}
					if(!contains(closed, y2)){
						if(!contains(open, y2)){
							open.add(y2);
						}
					}
					open.remove(pos);
					closed.add(pos);
					
				}
			}else{
				return pos;
			}
			
		}while(true);
		
		
	}
	
	private static boolean contains(ArrayList<voxel> al, voxel v){
		
		for(voxel test: al){
			SkyCraft.getInstance().getLogger().info("Testing voxel "+test.x+","+test.y+" vs "+v.x+","+v.y);
			if(test.x==v.x&&test.y==v.y){
				SkyCraft.getInstance().getLogger().info("They were equal.");
				return true;
			}
		}
		SkyCraft.getInstance().getLogger().info("Doesn't contain "+v.x+","+v.y);
		return false;
		
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
