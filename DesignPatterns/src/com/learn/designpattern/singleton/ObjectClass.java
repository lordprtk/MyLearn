package com.learn.designpattern.singleton;

public class ObjectClass {

	private static ObjectClass object = null;
	private ObjectClass(){
		
	}
	
	public static ObjectClass getInstance(){
		if(null == object){
			object = new ObjectClass();
		}
		return object;
	}
	
	public void print(String message){
		System.out.println(message);
	}
}
