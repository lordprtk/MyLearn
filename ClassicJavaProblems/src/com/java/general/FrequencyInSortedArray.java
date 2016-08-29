package com.java.general;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FrequencyInSortedArray {

	public static void main(String[] args) {
		int[] arr = {1,1,2,2,4,5,5,5,5,5,5};
		Map<Integer, Integer> map = new HashMap<>();
		int high = arr.length - 1;
		findFrequency(arr, map, 0, high);
		Set<Integer> set = map.keySet();
		Iterator<Integer> iter = set.iterator();
		while(iter.hasNext()){
			Integer key = iter.next();
			System.out.println("Frequency of "+key+" = "+map.get(key));
		}
	}
	
	private static void findFrequency(int[] arr, Map<Integer, Integer> map, int low, int high){
		if(arr[low] == arr[high]){
			int count = high-low+1;
			Integer val = map.get(arr[low]);
			if(null==val){
				map.put(arr[low], count);
			}else{
				map.put(arr[low], val+count);
			}
		}else{
			int mid = low+(high-low)/2;
			findFrequency(arr, map, low, mid);
			findFrequency(arr, map, mid+1, high);
		}
	}
}
