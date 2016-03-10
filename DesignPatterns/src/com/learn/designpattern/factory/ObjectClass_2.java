package com.learn.designpattern.factory;

public class ObjectClass_2 implements ObjectClass{

	public void print(){
		System.out.println("Printing from object class 2");
	}
	public void print(String message){
		System.out.println("class 2 - "+message);
	}
}
