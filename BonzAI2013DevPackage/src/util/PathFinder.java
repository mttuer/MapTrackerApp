package util;

import java.util.LinkedList;
import java.util.List;

import bonzai.api.GameState;
import bonzai.api.Tile;

public class PathFinder {
	private boolean board[][];
	private Tile nextTile;
	int x;
	int y;
	
	public PathFinder(int x, int y){
		this.x = x;
		this.y = y;
		board = new boolean[x][y];
	}
	
	private void resetBoard(){
		board = new boolean[x][y];
	}
	public Tile getNextTile(){
		return nextTile;
	}
	public int computeLowestCost(GameState state, Tile start, Tile end){
		this.resetBoard();
		return lowestCostPath(state,start,end);
	}
	
	private int lowestCostPath(GameState state, Tile start, Tile end){
		int sx = start.getX();
		int sy = start.getY();		
		int ex = end.getX();
		int ey = end.getY();
		int xCost, yCost, diagCost;
		diagCost = xCost = yCost = Integer.MAX_VALUE;
		int yChange = 0, xChange = 0;
		
		board[sx][sy] = true; // mark tile as visited
		
		if(sx == ex && sy == ey){
			return 0;
			
		}else if(sx == ex){ 	//case x = x move y
			yChange = sy > ey? -1: 1;
			
			
		}else if(sy == ey){ //case y = y move x
			xChange = sx > ex? -1: 1;
			
		}else{ //case not x nor y: Diag
			yChange = sy > ey? -1: 1;
			xChange = sx > ex? -1: 1;
			Tile t;
			Tile minTx = null,minTy = null,minTd = null;
			boolean checkAltRoute = true;
			//left right
			if((t = state.getTile(sx + xChange, sy)) != null && t.canFarmhandCross()){

				if(!board[sx +xChange][sy]){
					xCost = lowestCostPath(state, t, end);
					checkAltRoute = false;
				}else
					xCost =  Integer.MAX_VALUE;
				minTx = t;
			}else if(t == null) 
				xCost = Integer.MAX_VALUE;
			
			// up down
			if((t = state.getTile(sx , sy + yChange)) != null && t.canFarmhandCross()){
				if(!board[sx][sy + yChange]){
					yCost = lowestCostPath(state, t, end);
					checkAltRoute = false;
				}else
					yCost =  Integer.MAX_VALUE;
				minTy = t;
			}else if(t == null) 
				yCost = Integer.MAX_VALUE;
			
			// diagonal
			if((t = state.getTile(sx + xChange, sy + yChange)) != null && t.canFarmhandCross()){
				//if(!board[sx +xChange][sy + yChange]){
					diagCost = lowestCostPath(state, t, end);
					checkAltRoute = false;
				//}else
				//	diagCost =  Integer.MAX_VALUE;
				minTd = t;
			}else if(t == null) 
				diagCost =  Integer.MAX_VALUE;
				
			if(checkAltRoute){
				List<Tile> validPaths = new LinkedList<Tile>();
				
				if(yChange == 1 && xChange == 1){ // up right
					if((t = state.getTile(sx - 1, sy + 1)) != null && !board[sx - 1][sy + 1]){
						validPaths.add(t);
					}
					
					if((t = state.getTile(sx + 1, sy - 1)) != null && !board[sx + 1][sy - 1]){
						validPaths.add(t);
					}
					if(validPaths.isEmpty()){
						if((t = state.getTile(sx - 1, sy)) != null && !board[sx - 1][sy])
							validPaths.add(t);
						if((t = state.getTile(sx + 1, sy)) != null && !board[sx + 1][sy])
							validPaths.add(t);
					}
				}else if(yChange == 1 && xChange == -1){ //up left
					if((t = state.getTile(sx + 1, sy + 1)) != null && !board[sx + 1][sy + 1]){
						validPaths.add(t);
					}
					
					if((t = state.getTile(sx - 1, sy - 1)) != null && !board[sx - 1][sy - 1]){
						validPaths.add(t);
					}
					if(validPaths.isEmpty()){
						if((t = state.getTile(sx, sy - 1)) != null && !board[sx][sy - 1])
							validPaths.add(t);
						if((t = state.getTile(sx, sy + 1)) != null && !board[sx][sy + 1])
							validPaths.add(t);
					}
					
				}else if(yChange == -1 && xChange == -1){ //down left
					if((t = state.getTile(sx + 1, sy- 1)) != null && !board[sx + 1][sy - 1]){
						validPaths.add(t);
					}
					
					if((t = state.getTile(sx - 1, sy + 1)) != null && !board[sx - 1][sy + 1]){
						validPaths.add(t);
					}
					if(validPaths.isEmpty()){
						if((t = state.getTile(sx + 1, sy)) != null && !board[sx + 1][sy])
							validPaths.add(t);
						if((t = state.getTile(sx, sy + 1)) != null && !board[sx][sy + 1])
							validPaths.add(t);
					}
				}else if(yChange == -1 && xChange == 1){ //down right
					if((t = state.getTile(sx + 1, sy + 1)) != null && !board[sx + 1][sy + 1]){
						validPaths.add(t);
					}
					
					if((t = state.getTile(sx - 1, sy - 1)) != null && !board[sx - 1][sy - 1]){
						validPaths.add(t);
					}
					if(validPaths.isEmpty()){
						if((t = state.getTile(sx - 1, sy)) != null && !board[sx - 1][sy])
							validPaths.add(t);
						if((t = state.getTile(sx, sy + 1)) != null && !board[sx][sy + 1])
							validPaths.add(t);
					}
				}
				
				//===================================================================
				// compute costs

				int min = Integer.MAX_VALUE;
				int minCheck;
				Tile minT = null;
				for(Tile vt: validPaths){
					if(min < (minCheck = lowestCostPath(state,vt,end) )){
						min = minCheck;
						minT = vt;
						
					}
				}
				nextTile = minT;
				return min ==Integer.MAX_VALUE? min: 1 + min;
					
			}else{
				if(xCost < yCost){
					nextTile = minTx;
					if(diagCost < xCost)
						nextTile = minTd;
				}else{
					nextTile  = minTy;
					if(diagCost <= yCost){
						nextTile = minTd;
					}
				}
				int check = Math.min(Math.min(xCost, yCost), diagCost); 
				return check == Integer.MAX_VALUE ? check: check + 1; 
			}
			
		}
		
		Tile t;
		// Farmhand can try optimized tile
		if((t = state.getTile(sx + xChange, sy + yChange)).canFarmhandCross()){
			int check = lowestCostPath(state,t,end);
			nextTile = t;
			return check == Integer.MAX_VALUE ? check: check + 1;
		}else{ // Find an alternate route
			List<Tile> validPaths = new LinkedList<Tile>();
			if(yChange != 0){ //down or up
				//try bottom right diagonal
				if((t = state.getTile(sx + 1, sy + yChange)) != null){
					validPaths.add(t);
				}
				
				if((t = state.getTile(sx - 1, sy + yChange)) != null){
					validPaths.add(t);
				}
				if(validPaths.isEmpty()){
					if((t = state.getTile(sx + 1, sy)) != null && !board[sx + 1][sy])
						validPaths.add(t);
					if((t = state.getTile(sx - 1, sy)) != null && !board[sx - 1][sy])
						validPaths.add(t);
				}
				
				if(validPaths.isEmpty()){
					if((t = state.getTile(sx + 1, sy - yChange)) != null && !board[sx + 1][sy - 1])
						validPaths.add(t);
					if((t = state.getTile(sx - 1, sy - yChange)) != null && !board[sx - 1][sy - 1])
						validPaths.add(t);
				}
			} else if(xChange != 0){ //left or right
				//try bottom right diagonal
				if((t = state.getTile(sx + xChange, sy + 1)) != null){
					validPaths.add(t);
				}
				
				if((t = state.getTile(sx + xChange, sy - 1)) != null){
					validPaths.add(t);
				}
				if(validPaths.isEmpty()){
					if((t = state.getTile(sx, sy + 1)) != null && !board[sx + 1][sy])
						validPaths.add(t);
					if((t = state.getTile(sx, sy - 1)) != null && !board[sx - 1][sy])
						validPaths.add(t);
				}
				
				if(validPaths.isEmpty()){
					if((t = state.getTile(sx - xChange, sy - 1)) != null && !board[sx + 1][sy - 1])
						validPaths.add(t);
					if((t = state.getTile(sx - xChange, sy - 1)) != null && !board[sx - 1][sy - 1])
						validPaths.add(t);
				}
			}
			
			//check validPaths
			int min = Integer.MAX_VALUE;
			int minCheck;
			Tile minT = null;
			for(Tile vt: validPaths){
				if(min < (minCheck = lowestCostPath(state,vt,end) )){
					min = minCheck;
					minT = vt;
					
				}
			}
			nextTile = minT;
			return min ==Integer.MAX_VALUE? min: 1 + min;
		}
		
	}


}
