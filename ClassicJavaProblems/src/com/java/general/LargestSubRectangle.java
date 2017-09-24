package com.java.general;

public class LargestSubRectangle {
//INCOMPLETE - SOME CASES NOT HANDLED
	public static void main(String[] args) {
		/*String[][] matrix = {{"X","O","X","X","O","X"},{"X","O","X","X","X","X"},
							 {"X","X","X","O","O","X"},{"O","X","X","X","X","X"},
							 {"X","X","X","O","X","O"},{"O","O","X","O","O","O"}};*/
		String[][] matrix = {{"O","X","X","X"},{"X","X","X","X"},
				 			 {"O","X","O","X"},{"X","X","X","X"}};
		/*String[][] matrix = {{"X","O","X","X","X"},{"O","X","X","O","X"},
	 			 			 {"X","O","X","O","X"},{"X","O","X","O","X"},{"X","X","X","X","X"}};*/
		display(matrix);
		int row = matrix.length;
		int col = matrix[0].length;
		int[][] hor = new int[row][col];
		int[][] ver = new int[row][col];
		//int[][] min = new int[row][col];
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				if("X".equals(matrix[i][j])){
					ver[i][j] = (i==0)?1:ver[i-1][j]+1;
					hor[i][j] = (j==0)?1:hor[i][j-1]+1;
					//min[i][j] = Math.min(hor[i][j], ver[i][j]);
				}
			}
		}
		System.out.println();
		display(hor);
		System.out.println();
		display(ver);
		int max = maxSubRectangle(hor, ver, row, col);
		System.out.println("Max perimeter of sub-rectangle "+max);
	}
	
	private static int maxSubRectangle(int[][] hor, int[][] ver, int row, int col){
		int maxP = 0;
		for(int i=row-1;i>=0;i--){
			for(int j=col-1;j>=0;j--){
				int perm = 0;
				int small = Math.min(hor[i][j], ver[i][j]);
				while(small>1){
					if(hor[i-small+1][j]>=small &&
							ver[i][j-small+1]>=small){
						perm = 4*(small-1);
						break;
					}else if(hor[i-small+2][j]>=small &&
							ver[i][j-small+1]>=small-1){
						perm = 4*small -6;
						break;
					}else if(hor[i-small+1][j]>=small-1 &&
							ver[i][j-small+2]>=small){
						perm = 4*small -6;
						break;
					}
					small--;
				}
				if(perm>maxP){
					maxP = perm;
				}
			}
		}
		return maxP;
	}
	
	private static void display(String[][] matrix){
		int row = matrix.length;
		int col = matrix[0].length;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				System.out.print(String.format("%-2s",matrix[i][j]));
			}
			System.out.println();
		}
	}
	
	private static void display(int[][] matrix){
		int row = matrix.length;
		int col = matrix[0].length;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				System.out.print(String.format("%2d",matrix[i][j]));
			}
			System.out.println();
		}
	}
}
