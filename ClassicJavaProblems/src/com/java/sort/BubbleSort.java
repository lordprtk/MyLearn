package com.java.sort;

import java.util.Arrays;

/*
 *  Bubble sort repeatedly steps through the list to be sorted, compares each 
 *  pair of adjacent items and swaps them if they are in the wrong order. The 
 *  pass through the list is repeated until no swaps are needed, which indicates 
 *  that the list is sorted.
 */

public class BubbleSort {
	public static void main(String[] args) {
		//int[] arr = {1, 8, 20, 35, 14};
		int[] arr = {1,4,5,2,3,7,5,34,7,5,3,7,8,2};
		int max = arr.length;
		System.out.println("Initial "+Arrays.toString(arr));
		for(int i=0;i<max-1;i++){
			boolean swapped = false;
			for(int j=0;j<max-i-1;j++){
				if(arr[j]>arr[j+1]){
					sort(arr, j);
					swapped = true;
				}
			}
			if(!swapped){
				System.out.println("Breaking");
				break;
			}
		}
		System.out.println("Final "+Arrays.toString(arr));
	}
	
	private static void sort(int[] arr, int j){
		int temp = arr[j];
		arr[j] = arr[j+1];
		arr[j+1] = temp;
		return;
	}
}
