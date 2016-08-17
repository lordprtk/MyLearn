package com.java.matrix;

/*
 * 	Input:
        1    2   3   4
        5    6   7   8
        9   10  11  12
        13  14  15  16
	Clockwise Output: 
	1 2 3 4 8 12 16 15 14 13 9 5 6 7 11 10 
	Anti-Clockwise Output:
	16 12 8 4 3 2 1 5 9 13 14 15 11 7 6 10
	
	Input:
        1   2   3   4  5   6
        7   8   9  10  11  12
        13  14  15 16  17  18
	Clockwise Output: 
	1 2 3 4 5 6 12 18 17 16 15 14 13 7 8 9 10 11
	Anti-Clockwise Output:
	18 12 6 5 4 3 2 1 7 13 14 15 16 17 11 10 9 8
*/

public class SpiralMatrix {
	
	public static void main(String[] args){
		int[][] matrix = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
		int row = matrix.length;
		int column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		printMatrixStyle(matrix, row, column);
		printClockwiseSpiral(matrix, row, column);
		printAntiClockwiseSpiral(matrix, row, column);
		matrix = new int[][]{{1,2,3,4,5,6},{7,8,9,10,11,12},{13,14,15,16,17,18}};
		row = matrix.length;
		column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		printMatrixStyle(matrix, row, column);
		printClockwiseSpiral(matrix, row, column);
		printAntiClockwiseSpiral(matrix, row, column);
		matrix = new int[][]{{1,2,3},{4,5,6},{7,8,9},{10,11,12},{13,14,15},{16,17,18}};
		row = matrix.length;
		column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		printMatrixStyle(matrix, row, column);
		printClockwiseSpiral(matrix, row, column);
		printAntiClockwiseSpiral(matrix, row, column);
	}
	
	public static void printMatrixStyle(int matrix[][], int row, int col){
		System.out.println("INPUT MATRIX");
		StringBuffer str = null;
		for(int i=0;i<row; i++){
			str = new StringBuffer();
			for(int j=0;j<col;j++){
				str.append(String.format("%3d", matrix[i][j])+" ");
			}
			System.out.println(str);
		}
	}
	
	private static void printAntiClockwiseSpiral(int matrix[][], int row, int col){
		System.out.println("Print Anti-Clockwise Spiral");
		StringBuffer str = new StringBuffer();
		int i, iRow=0, iCol=0, totalCount=0;
		int elementCount=row*col;
		while(totalCount<elementCount){
			//Last column in iteration
			for(i=row-1;i>=iRow;i--){
				str.append(String.format("%2d", matrix[i][col-1])+" ");
				totalCount++;
			}
			col--;
			//First row in iteration
			for(i=col-1;i>=iCol;i--){
				str.append(String.format("%2d", matrix[iRow][i])+" ");
				totalCount++;
			}
			iRow++;
			//First column in iteration
			if(iCol<col){
				for(i=iRow;i<row;i++){
					str.append(String.format("%2d", matrix[i][iCol])+" ");
					totalCount++;
				}
				iCol++;
			}
			//Last row in iteration
			if(iRow<row){
				for(i=iCol;i<col;i++){
					str.append(String.format("%2d", matrix[row-1][i])+" ");
					totalCount++;
				}
				row--;
			}
		}
		System.out.println(str);
	}
	
	public static void printClockwiseSpiral(int matrix[][], int row, int col){
		System.out.println("Print Clockwise Spiral");
		StringBuffer str = new StringBuffer();
		int i=0, iRow=0, iCol=0, totalCount = 0;
		int elementCount = row*col;
		//Any of the below 2 conditions will work
		//while(iCol<col && iRow<row){
		while(totalCount<elementCount){
			//First Row in iteration
			for(i=iCol; i<col; i++){
				str.append(String.format("%2d", matrix[iRow][i])+ " ");
				totalCount++;
			}
			iRow++;
			//Last column in iteration
			for(i=iRow; i<row; i++){
				str.append(String.format("%2d", matrix[i][col-1])+ " ");
				totalCount++;
			}
			col--;
			//Last row in iteration
			if(iRow<row){
				for(i=col-1;i>=iCol;i--){
					str.append(String.format("%2d", matrix[row-1][i])+ " ");
					totalCount++;
				}
				row--;
			}
			//First column in iteration
			if(iCol<col){
				for(i=row-1;i>=iRow;i--){
					str.append(String.format("%2d", matrix[i][iCol])+ " ");
					totalCount++;
				}
				iCol++;
			}
		}
		System.out.println(str);
	}
	
}
