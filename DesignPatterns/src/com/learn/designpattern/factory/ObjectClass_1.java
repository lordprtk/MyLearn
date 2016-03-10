package com.learn.designpattern.factory;

public class ObjectClass_1 implements ObjectClass{

	public void print(){
		System.out.println("Printing from object class 1");
	}
	public void print(String message){
		System.out.println("class 1 - "+message);
	}
}
