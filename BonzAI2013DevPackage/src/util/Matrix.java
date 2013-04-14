package util;

public class Matrix {
	private int x;
	private int y;
	boolean board[][];
	public Matrix(int x, int y){
		this.x = x;
		this.y = y;
		board = new boolean[x][y];
	}
	public void reset(){

		board = new boolean[x][y];
	}
	public boolean get(int x, int y){
		if(x > -1 && x < this.x && y> -1 && y < this.y)
			return false;
	}
}
