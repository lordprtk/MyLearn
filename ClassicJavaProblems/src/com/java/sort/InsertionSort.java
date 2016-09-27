package com.java.sort;

import java.util.Arrays;

/*
 * Pick element arr[i] and insert it into sorted sequence arr[0…i-1]
*/
public class InsertionSort {
	public static void main(String[] args) {
		int[] arr = {1,4,5,2,3,7,5,34,7,5,3,7,8,2};
		int len = arr.length;
		System.out.println(Arrays.toString(arr));
		for(int i=1;i<len;i++){
			int element = arr[i];
			int j=i-1;
			// All elements before arr[i] will be sorted. Move all elemets greater than arr[i] by one place and set arr[i] in the sorted part of the array.
			while(j>=0 && arr[j]>element){
				arr[j+1]=arr[j];
				j--;
			}
			arr[j+1]=element;
			System.out.println(Arrays.toString(arr));
		}
	}
}
