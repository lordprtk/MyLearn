package com.java.sort;

import java.util.Arrays;

/*
 * The selection sort algorithm sorts an array by repeatedly finding 
 * the minimum element (considering ascending order) from unsorted part 
 * and putting it at the beginning.
*/
public class SelectionSort {
	public static void main(String[] args) {
		int[] arr = {1,4,5,2,3,7,5,34,7,5,3,7,8,2};
		System.out.println(Arrays.toString(arr));
		int max = arr.length - 1;
		for(int i=0;i<max;i++){
			int index = i;
			for(int j=i+1;j<=max;j++){
				if(arr[j]<arr[index]){
					index=j;
				}
			}
			int temp = arr[i];
			arr[i] = arr[index];
			arr[index] = temp;
		}
		System.out.println(Arrays.toString(arr));
	}
}
