package com.java.matrix;

/*
 * 	Input:
        1    2   3   4
        5    6   7   8
        9   10  11  12
        13  14  15  16
	Output: 
	1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 

	Input:
        1   2   3   4  5   6
        7   8   9  10  11  12
        13  14  15 16  17  18
	Output: 
	1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
*/

public class LinearMatrix {
	
	public static void main(String[] args){
		int[][] matrix = {{1,2,3,4,5,6},{7,8,9,10,11,12},{13,14,15,16,17,18}};
		int row = matrix.length;
		int column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		printMatrixStyle(matrix, row, column);
		printLinear(matrix, row, column);
		matrix = new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
		row = matrix.length;
		column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		printMatrixStyle(matrix, row, column);
		printLinear(matrix, row, column);
		
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
	
	private static void printLinear(int[][] matrix, int row, int column){
		System.out.println("Linear Print");
		StringBuffer str = new StringBuffer();
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				str.append(matrix[i][j]+" ");
			}
		}
		System.out.println(str.deleteCharAt(str.length()-1));
	}
}
