package com.java8;

import java.util.Arrays;

public class UniqueCharsInString {

	public static void main(String[] args) {
		String str = "aab cb";
		System.out.println(isUniqueChars(str));
		System.out.println(onlyUnique(str.toCharArray()));
		String newStr = "bc baa";
		System.out.println(checkReverse(str,newStr));
		System.out.println(checkAnagram(str, newStr));
	}
	
	private static String checkAnagram(String str1, String str2){
		return null;
	}
	
	private static boolean checkReverse(String str1, String str2){
		if(str1.length()!=str2.length()){
			return false;
		}
		for(int i=0;i<str1.length();i++){
			if(str1.charAt(i)==str2.charAt(str2.length()-1-i)) continue;
			else return false;
		}
		return true;
	}
	
	private static String onlyUnique(char[] str){
		int len=str.length;
		int check=0;
		for(int i=0; i<len; i++){
			int c=str[i]-'a';
			if((check&(1<<c))>0){
				for(int j=i+1;j<len;j++){
					str[j-1]=str[j];
				}
				str[len-1]=0;
				len--;
				i--;
			}
			check = check|(1<<c);
		}
		return String.valueOf(str).trim();
	}

	/*Checks the bits of checker. For each character, it sets the positional bit to 1
	 * in the checker string
	 * Example:
	 * when b is encountered for the first time, it sets the bit#2 from right to 1
	 * in checker (using the bitwise OR operator.
	 * If b is encountered again, the bit-wise AND operator will return
	 * a value greater than 0 */
	public static boolean isUniqueChars(String str) {
		int checker = 0;
		for (int i = 0; i < str.length(); ++i) {
			int val = str.charAt(i) - 'a';
			if ((checker & (1 << val)) > 0) return false;
			checker |= (1 << val);
		}
		return true;
	}
}
