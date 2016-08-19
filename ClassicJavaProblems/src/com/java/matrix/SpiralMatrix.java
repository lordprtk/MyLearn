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
		/*StringBuffer str = new StringBuffer("{");
		for(int i=1;i<=64;i++){
			if(i%8==0){
				str.append(i+"},{");
			}else{
				str.append(i+",");
			}
		}
		System.out.println(str);*/
		int[][] matrix = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
		int row = matrix.length;
		int column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		System.out.println("****************************************************");
		System.out.println("INPUT MATRIX");
		printMatrixStyle(matrix, row, column);
		printClockwiseSpiral(matrix, row, column);
		printElementKClockwiseSpiral(matrix, row, column, 14);
		printElementKClockwiseSpiral_Efficient(matrix, row, column, 14);
		printAntiClockwiseSpiral(matrix, row, column);
		
		/*matrix = new int[][]{{1,2,3,4,5,6,7,8},{9,10,11,12,13,14,15,16},{17,18,19,20,21,22,23,24},
				{25,26,27,28,29,30,31,32},{33,34,35,36,37,38,39,40},{41,42,43,44,45,46,47,48},{49,50,51,52,53,54,55,56},{57,58,59,60,61,62,63,64}};
		row = matrix.length;
		column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		System.out.println("****************************************************");
		System.out.println("INPUT MATRIX");
		printMatrixStyle(matrix, row, column);
		printClockwiseSpiral(matrix, row, column);
		printElementKClockwiseSpiral(matrix, row, column, 63);
		printElementKClockwiseSpiral_Efficient(matrix, row, column, 63);
		printAntiClockwiseSpiral(matrix, row, column);*/
		
		/*matrix = new int[][]{{1,2,3,4,5,6},{7,8,9,10,11,12},{13,14,15,16,17,18}};
		row = matrix.length;
		column = 0;
		if(row > 0){
			column = matrix[0].length;
		}else{
			System.out.println("No rows in matrix");
			return;
		}
		System.out.println("****************************************************");
		System.out.println("INPUT MATRIX");
		printMatrixStyle(matrix, row, column);
		printClockwiseSpiral(matrix, row, column);
		printElementKClockwiseSpiral(matrix, row, column, 15);
		printElementKClockwiseSpiral_Efficient(matrix, row, column, 15);
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
		System.out.println("****************************************************");
		System.out.println("INPUT MATRIX");
		printMatrixStyle(matrix, row, column);
		printClockwiseSpiral(matrix, row, column);
		printElementKClockwiseSpiral(matrix, row, column, 14);
		printElementKClockwiseSpiral_Efficient(matrix, row, column, 14);
		printAntiClockwiseSpiral(matrix, row, column);*/
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
	
	private static void printClockwiseSpiral(int matrix[][], int row, int col){
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
	
	private static void printElementKClockwiseSpiral(int matrix[][], int row, int col, int k){
		int i=0, iRow=0, iCol=0, totalCount = 0;
		int elementCount = row*col;
		int element = 0;
		boolean elementFound = false;
		//Any of the below 2 conditions will work
		//while(iCol<col && iRow<row){
		while(totalCount<elementCount){
			//First Row in iteration
			for(i=iCol; i<col; i++){
				totalCount++;
				if(k==totalCount){
					elementFound = true;
					element = matrix[iRow][i];
					break;
				}
			}
			if(elementFound){
				break;
			}
			iRow++;
			//Last column in iteration
			for(i=iRow; i<row; i++){
				totalCount++;
				if(k==totalCount){
					elementFound = true;
					element = matrix[i][col-1];
					break;
				}
			}
			if(elementFound){
				break;
			}
			col--;
			//Last row in iteration
			if(iRow<row){
				for(i=col-1;i>=iCol;i--){
					totalCount++;
					if(k==totalCount){
						elementFound = true;
						element = matrix[row-1][i];
						break;
					}
				}
				if(elementFound){
					break;
				}
				row--;
			}
			//First column in iteration
			if(iCol<col){
				for(i=row-1;i>=iRow;i--){
					totalCount++;
					if(k==totalCount){
						elementFound = true;
						element = matrix[i][iCol];
						break;
					}
				}
				if(elementFound){
					break;
				}
				iCol++;
			}
		}
		if(elementFound){
			System.out.println("Element "+k+" is - "+element);
		}else{
			System.out.println("Some error encountered. Element not found!!");
		}
	}
	
	private static void printElementKClockwiseSpiral_Efficient(int matrix[][], int row, int col, int k){
		/* This works by checking the element in each spiral
		 * Either the element will be in the outermost spiral, or in the matrix inside the outermost spiral
		 * If the element is in outermost spiral, the conditions specified will find it
		 * Otherwise, the function is called recursively for finding the element in the matrix inside the outermost spiral*/
		try{
			int element = elementK(matrix, row, col, k);
			System.out.println("Using efficient method, Element "+k+" is - "+element);
		}catch(Exception e){
			System.out.println("Some error encountered. Element not found!!");
		}
	}
	
	private static int elementK(int matrix[][], int row, int col, int k) throws Exception{
		if(k<=0 || k>row*col){
			throw new Exception("Faulty input provided");
		}
		if(k<=col){
			//Element in first row of the matrix passed
			return matrix[0][k-1];
		}else if(k<=row+col-1){
			//Element in last column of the matrix passed
			return matrix[k-col][col-1];
		}else if(k<=2*col+row-2){
			//Element in last row of the matrix passed
			/*(row+col-1) gives the number of elements in the first row and last column
			 * k-(row+col-1) gives the number of elements left to search since the required
			 * element is in the last row only.
			*/
			return matrix[row-1][col-1-(k-(row+col-1))];
		}else if(k<=2*col+2*row-4){
			//Element in first column of the matrix passed
			/*(2*col+row-2) gives the number of elements in the first row, last column and
			 * last row
			 * k-(2*col+row-2) gives the number of elements left to search since the required
			 * element is in the first column only.
			*/
			return matrix[row-1-(k-(2*col+row-2))][0];
		}else{
			int[][] smallMatrix = innerMatrix(matrix, row, col);
			return elementK(smallMatrix, row-2, col-2, k-(2*col+2*row-4));
		}
	}
	
	private static int[][] innerMatrix(int[][] matrix, int row, int col) throws Exception{
		if(row>2 && col>2){
			int[][] newMatrix = new int[row-2][col-2];
			for(int i=1;i<row-1;i++){
				for(int j=1;j<col-1;j++){
					newMatrix[i-1][j-1] = matrix[i][j];
				}
			}
			System.out.println("New matrix passed");
			printMatrixStyle(newMatrix, row-2, col-2);
			return newMatrix;
		}else{
			throw new Exception("Inner Matrix cannot be found");
		}
	}
}
