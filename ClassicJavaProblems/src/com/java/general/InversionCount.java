package com.java.general;

import java.util.Arrays;

/*
 * Inversion Count for an array indicates – how far (or close) the array is from 
 * being sorted. If array is already sorted then inversion count is 0. If array is 
 * sorted in reverse order that inversion count is the maximum. 
 * Formally speaking, two elements a[i] and a[j] form an inversion if a[i] > a[j] and i < j
 * 
 * Example:
 * The sequence 2, 4, 1, 3, 5 has three inversions (2, 1), (4, 1), (4, 3).
*/

public class InversionCount {
	public static void main(String[] args) {
		//int[] arr = {1, 20, 6, 4, 5};
		int[] arr = {1,4,5,2,3,7,5,34,7,5,3,7,8,2};
		int len = arr.length;
		int inCount = 0;
		StringBuffer str = new StringBuffer();
		int rows = 0;
		for(int i=0;i<len-1;i++){
			for(int j=i+1;j<len;j++){
				if(arr[i]>arr[j]){
					inCount++;
					str.append("("+arr[i]+","+arr[j]+")");
					if(str.length() - rows*60>60){
						str.append("\n");
						rows++;
					}
					//System.out.println("Inversion ->"+arr[i]+","+arr[j]);
				}
			}
		}
		System.out.println(str);
		System.out.println("Total Inversions "+inCount);
		usingMergeSort(arr);
	}
	
	private static void usingMergeSort(int[] arr){
		//arr = new int[]{34,7,5,3,7,8,2};
		int len = arr.length;
		System.out.println("Using Merge Sort - array before "+Arrays.toString(arr));
		int count = mergeSort(arr, 0, len-1);
		System.out.println("Using Merge Sort - array after  "+Arrays.toString(arr));
		System.out.println("Total Inversions using merge sort "+count);
	}
	
	
	private static int mergeSort(int[] arr, int l, int r){
		if(r<=l){
			return 0;
		}
		int m = (l+r)/2;
		int count1 = mergeSort(arr, l, m);
		int count2 = mergeSort(arr, m+1, r);
		int count3= merge(arr, l, m, r);
		return count1+count2+count3;
	}
	
	private static int merge(int[] arr, int l, int m, int r){
		int n1 = m-l+1;
		int n2 = r-m;
		int[] arr1 = new int[n1];
		int[] arr2 = new int[n2];
		int i,j;
		for(i=0;i<n1;i++){
			arr1[i]=arr[l+i];
		}
		for(j=0;j<n2;j++){
			arr2[j]=arr[m+j+1];
		}
		i=0;
		j=0;
		int count = 0;
		for(;i<n1 && j<n2;){
			if(arr1[i]<=arr2[j]){
				arr[l+i+j] = arr1[i];
				i++;
			}else{
				arr[l+i+j]=arr2[j];
				j++;
				// No of elements in left sub-array which are greater than the arr2[j] all amount to inversion.
				count+=(n1-i);
			}
		}
		while(i<n1){
			arr[l+i+j] = arr1[i];
			i++;
		}
		while(j<n2){
			arr[l+i+j]=arr2[j];
			j++;
		}
		return count;
	}
}
