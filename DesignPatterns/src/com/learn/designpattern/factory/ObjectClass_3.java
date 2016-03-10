package com.learn.designpattern.factory;

public class ObjectClass_3 implements ObjectClass{

	public void print(){
		System.out.println("Printing from object class 3");
	}
	public void print(String message){
		System.out.println("class 3 - "+message);
	}
}
