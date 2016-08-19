package com.java.matrix;

public class DiamondPrints {

	public static void main(String[] args) {
		int[][] matrix = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
		int row = matrix.length;
		int col = 0;
		if(row > 0){
			col = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		System.out.println("****************************************************");
		System.out.println("INPUT MATRIX");
		printMatrixStyle(matrix, row, col);
		System.out.println("****************************************************");
		System.out.println("TOP DIAMOND");
		printTopDiamond(matrix, row, col);
		System.out.println("****************************************************");
		System.out.println("BOTTOM DIAMOND");
		printBottomDiamond(matrix, row, col);
		System.out.println("****************************************************");
		System.out.println("TOP DIAMOND_2");
		printTopDiamond_2(matrix, row, col);
		System.out.println("****************************************************");
		System.out.println("BOTTOM DIAMOND_2");
		printBottomDiamond_2(matrix, row, col);
	}
	private static void printMatrixStyle(int matrix[][], int row, int col){
		StringBuffer str = null;
		for(int i=0;i<row; i++){
			str = new StringBuffer();
			for(int j=0;j<col;j++){
				str.append(String.format("%3d", matrix[i][j])+" ");
			}
			System.out.println(str);
		}
	}
	
	private static void printTopDiamond(int[][] matrix, int row, int col){
		for(int i=0;i<row;i++){
			StringBuffer str = new StringBuffer();
			for(int k=1;k<=i;k++){
				str.append("    ");
			}
			for(int j=i;j<col;j++){
				str.append(String.format("%3d", matrix[i][j])+" ");
			}
			System.out.println(str);
		}
	}
	
	private static void printBottomDiamond(int[][] matrix, int row, int col){
		for(int i=0;i<row;i++){
			StringBuffer str = new StringBuffer();
			for(int j=0;j<=i;j++){
				str.append(String.format("%3d", matrix[i][j])+" ");
			}
			System.out.println(str);
		}
	}
	
	private static void printTopDiamond_2(int[][] matrix, int row, int col){
		for(int i=0;i<row;i++){
			StringBuffer str = new StringBuffer();
			for(int j=0;j<col-i;j++){
				str.append(String.format("%3d", matrix[i][j])+" ");
			}
			System.out.println(str);
		}
	}
	
	private static void printBottomDiamond_2(int[][] matrix, int row, int col){
		for(int i=0;i<row;i++){
			StringBuffer str = new StringBuffer();
			for(int k=1;k<=col-1-i;k++){
				str.append("    ");
			}
			for(int j=col-i-1;j<col;j++){
				str.append(String.format("%3d", matrix[i][j])+" ");
			}
			System.out.println(str);
		}
	}
}
