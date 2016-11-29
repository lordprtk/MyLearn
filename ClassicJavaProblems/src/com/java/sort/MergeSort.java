package com.java.sort;

import java.util.Arrays;

/* 
 * MergeSort is a Divide and Conquer algorithm. It divides input array in two halves, calls itself for 
 * the two halves and then merges the two sorted halves.
*/

public class MergeSort {
	public static void main(String[] args) {
		//int[] arr = {1, 8, 20, 35, 14};
		int[] arr = {1,4,5,2,3,7,5,34,7,5,3,7,8,2};
		System.out.println(Arrays.toString(arr));
		mergesort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}
	
	private static void mergesort(int[] arr, int left, int right){
		if(right<=left){
			return;
		}
		int mid = left+(right-left)/2;
		mergesort(arr, left, mid);
		mergesort(arr, mid+1, right);
		merge(arr, left, mid, right);
	}
	
	private static void merge(int[] arr, int left, int mid, int right){
		int counter1 = mid-left+1, counter2 = right-mid;
		int[] subArr1 = new int[counter1];
		int[] subArr2 = new int[counter2];
		for(int i=0; i<counter1;i++){
			subArr1[i] = arr[left+i];
		}
		for(int i=0; i<counter2;i++){
			subArr2[i] = arr[mid+i+1];
		}
		int i=0,j=0;
		for(;i<counter1 && j<counter2;){
			if(subArr1[i]<subArr2[j]){
				arr[left+i+j] = subArr1[i];
				i++;
			}else{
				arr[left+i+j] = subArr2[j];
				j++;
			}
		}
		while(i<counter1){
			arr[left+i+j] = subArr1[i];
			i++;
		}
		while(j<counter2){
			arr[left+i+j] = subArr2[j];
			j++;
		}
	}
}
