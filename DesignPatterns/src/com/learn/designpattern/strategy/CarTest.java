package com.learn.designpattern.strategy;

public class CarTest {
	public static void main(String[] args) {
		// Set the behaviors at runtime through the appropriate constructors.
		/* Besides get the flexibility to change the behavior at runtime by 
		calling appropriate setter function. */ 
		Car car = new BasicCar();
		car.carBody();
		car.drive();
		System.out.println("***********************");
		car = new SuperCar();
		car.carBody();
		car.drive();
	}
}
