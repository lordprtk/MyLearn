package com.java.search;

public class SmallestInRotatedSortedArray {
	public static void main(String[] args) {
		int[] arr = {14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,1,2,3,4,5,6,7,8,9,10,11,12,13};
		int len = arr.length;
		int pos = find(arr, 0, len-1);
		System.out.println(arr[pos]);
		
	}
	
	private static int find(int[] arr, int left, int right){
		/* First pulse is from left to max element, second pulse is from smallest element to right*/
		// extreme condition, size zero or size two
	    int m;
	    // Precondition: A[l] > A[r]
	    if( arr[left] <= arr[right] )
	        return left;
	 
	    while( left <= right )
	    {
	        // Termination condition (l will eventually falls on r, and r always
	        // point minimum possible value)
	        if( left == right )
	            return left;
	        //Below calculation is same as saying m = (left+right)/2;
	        
	        m = left + (right-left)/2; // 'm' can fall in first pulse,
	                        // second pulse or exactly in the middle
	 
	        if( arr[m] < arr[right] )
	            // min can't be in the range
	            // (m < i <= r), we can exclude A[m+1 ... r]
	        	right = m;
	        else
	            // min must be in the range (m < i <= r),
	            // we must search in A[m+1 ... r]
	        	left = m+1;
	    }
	 
	    return -1;
	}
}
