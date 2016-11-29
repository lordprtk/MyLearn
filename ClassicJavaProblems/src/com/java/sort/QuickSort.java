package com.java.sort;

import java.util.Arrays;

public class QuickSort {
	public static void main(String[] args) {
		//int[] arr = {1, 20, 8, 35, 14};
		int[] arr = {1,4,5,2,3,7,5,34,7,5,3,7,8,2};
		System.out.println(Arrays.toString(arr));
		int len = arr.length;
		randomQuickSort(arr, 0, len-1);
		System.out.println(Arrays.toString(arr));
	}
	
	private static void quickSort(int[] arr, int left, int right){
		if(left<right){
			int pos = partition(arr, left, right);
			System.out.println(pos);
			quickSort(arr, left, pos-1);
			quickSort(arr, pos+1, right);
		}
	}
	
	private static void randomQuickSort(int[] arr, int left, int right){
		if(left<right){
			int pos = randomPartition(arr, left, right);
			System.out.println(pos);
			quickSort(arr, left, pos-1);
			quickSort(arr, pos+1, right);
		}
	}
	
	private static int randomPartition(int[] arr, int left, int right){
		int random = (int)(Math.random()*right);
		int temp = arr[right];
		arr[right] = arr[random];
		arr[random] = temp;
		
		int val = arr[right];
		int pos = left-1;
		for(int i=left;i<=right-1;i++){
			if(arr[i]<=val){
				pos++;
				temp = arr[i];
				arr[i] = arr[pos];
				arr[pos] = temp;
			}
		}
		pos++;
		temp = arr[pos];
		arr[pos] = val;
		arr[right] = temp;
		return pos;
	}
	
	private static int partition(int[] arr, int left, int right){
		int val = arr[right];
		int pos = left - 1;
		for(int i=left;i<=right-1;i++){
			if(arr[i]<=val){
				pos++;
				int temp = arr[pos];
				arr[pos] = arr[i];
				arr[i] = temp;
			}
		}
		pos++;
		int temp = arr[pos];
		arr[pos] = arr[right];
		arr[right] = temp; 
		//System.out.println(Arrays.toString(arr));
		return pos;
	}
}
