package com.java8;

import java.util.ArrayList;

public class AllCombinationsOfCharacters {

	public static void main(String[] args) {
		String str = "abcde";
		ArrayList<String> combo = combine(str);
		for(String c:combo){
			System.out.print(c+",");
		}
		System.out.println("\nTotal count "+combo.size());
	}
	
	private static ArrayList<String> combine(String a){
		int length = a.length();
		ArrayList<String> combo = new ArrayList<String>();
		if(length>1){
			ArrayList<String> arr = combine(a.substring(0,length-1));
			String b = a.substring(length-1);
			for(String val:arr){
				int strLen = val.length();
				combo.add(val+b);
				if(strLen>=2){
					for(int i=1;i<strLen;i++){
						combo.add(val.substring(0,i)+b+val.substring(i));
					}
				}
				combo.add(b+val);
			}
			return combo;
		}else{
			combo.add(a);
			return combo;
		}
	}

}
