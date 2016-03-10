package com.learn.designpattern.factory;

public class Factory {
	public static ObjectClass getInstance(int num){
		switch(num){
		case 1:
			return new ObjectClass_1();
		case 2:
			return new ObjectClass_2();
		case 3:
			return new ObjectClass_3();
		case 4:
			return new ObjectClass_4();
		default:
			return null;
		}
	}
	
	public static void main(String[] args){
		for(int i=1;i<=5;i++){
			ObjectClass object = getInstance(i);
			if(null != object){
				object.print();
				object.print("This is going to print");
			}else{
				System.out.println("No class selected");
			}
		}
	}
}
