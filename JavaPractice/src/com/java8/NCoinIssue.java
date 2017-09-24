package com.java8;


public class NCoinIssue {
	public static void main(String[] args){
		int[] opt={1,2,3};
		int sum=5;
		System.out.println(combo(opt, opt.length, sum));
	}
	
	private static int combo(int[] opt, int len, int sum){
		if(sum==0) 
			return 1;
		if(sum<0 || len<=0){
			return 0;
		}
		if(len==1){
			if(sum%opt[0]==0){
				return 1;
			}
		}
		int val1 = combo(opt, len-1, sum);
		int val2 = combo(opt, len, sum-opt[len-1]);
		
		return val1+val2;
	}
	
}
