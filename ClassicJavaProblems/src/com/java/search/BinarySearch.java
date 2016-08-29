package com.java.search;

public class BinarySearch {
	public static void main(String[] args) {
		int[] arr = {0,1,2,3,4,5,6,7,8,9,10};
		int len = arr.length; 
		int element = 4;
		int pos = recursiveSearch(arr, 0, len-1, element);
		if(pos != -1){
			System.out.println("using recursive search, position of "+element+" is "+pos);
		}else{
			System.out.println("using recursive search, element "+element+" not found");
		}
		pos = iterativeSearch(arr, 0, len-1, element);
		if(pos != -1){
			System.out.println("using iterative search, position of "+element+" is "+pos);
		}else{
			System.out.println("using iterative search, element "+element+" not found");
		}
		pos = leastComparisons(arr, 0, len-1, element);
		if(pos != -1){
			System.out.println("using least comparison search, position of "+element+" is "+pos);
		}else{
			System.out.println("using least comparison search, element "+element+" not found");
		}
	}

	private static int recursiveSearch(int[] arr, int left, int right, int element){
		if(left<=right){
			int mid = (left+right)/2;
			if(arr[mid] == element){
				return mid;
			}
			if(arr[mid]<element){
				return recursiveSearch(arr, mid+1, right, element);
			}else{
				return recursiveSearch(arr, left, mid-1, element);
			}
		}else{
			return -1;
		}
	}
	
	private static int iterativeSearch(int[] arr, int left, int right, int element){
		int i = 1;
		while(left<=right){
			System.out.println("Iteration #"+i++);
			int mid = (left+right)/2;
			if(arr[mid] == element){
				return mid;
			}
			if(arr[mid]<element){
				left = mid+1;
			}else{
				right = mid-1;
			}
		}
		return -1;
	}
	
	private static int leastComparisons(int[] arr, int left, int right, int element){
		int i = 1;
		/* If right-left is greater than 1, it means there are more than 2 elements
		 * to search for the element. If right-left = 1, it means there are only 2 elements
		 * left to search. Since this is a sorted array, required element must be on the left.
		 * If that is not the case, element is not present
		 * */
		while((right-left)>1){
			System.out.println("Iteration #"+i++ +" left="+left+" right="+right);
			int mid = (left+right)/2;
			if(arr[mid]<=element){
				left = mid;
			}else{
				right=mid;
			}
		}
		if(arr[left]==element){
			return left;
		}
		return -1;
	}
}
