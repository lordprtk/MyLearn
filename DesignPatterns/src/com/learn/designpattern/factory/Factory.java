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
		ObjectClass object = getInstance(1);
		object.print();
		object.print("This is going to print");
	}
}
