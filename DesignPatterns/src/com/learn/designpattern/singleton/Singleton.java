package com.learn.designpattern.singleton;

public class Singleton {
	public static void main(String[] args){
		ObjectClass obj = ObjectClass.getInstance();
		obj.print("Printing from a singleton class");
	}
}
