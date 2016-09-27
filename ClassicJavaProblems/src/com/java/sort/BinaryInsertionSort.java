package com.java.sort;

import java.util.Arrays;

/* Insertion sort with the position for insertion being found using binary search */

public class BinaryInsertionSort {
	public static void main(String[] args) {
		//int[] arr = {1, 8, 20, 35, 14};
		int[] arr = {1,4,5,2,3,7,5,34,7,5,3,7,8,2};
		int len = arr.length;
		System.out.println(Arrays.toString(arr));
		for(int i=1;i<len;i++){
			int element = arr[i];
			int j=i-1;
			int pos = position(element, 0, j, arr);
			while(j>=pos){
				arr[j+1]=arr[j];
				j--;
			}
			arr[pos]=element;
		}
		System.out.println(Arrays.toString(arr));
	}
	
	private static int position(int element, int left, int right, int[] arr){
		if (right <= left)
	        return (element > arr[left])?  (left + 1): left;
	 
	    int mid = (left + right)/2;
	 
	    if(element == arr[mid])
	        return mid+1;
	 
	    if(element > arr[mid])
	        return position(element, mid+1, right, arr);
	    return position(element, left, mid-1, arr);
	}

}
