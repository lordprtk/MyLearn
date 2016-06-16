package com.learn.designpattern.strategy;

public class SuperCar extends Car{
	public SuperCar(){
		setIgnition(new IgnitionByButton());
		setTransmission(new AutomaticTransmission());
	}
	
	@Override
	public void carBody() {
		System.out.println("This is a super car");		
	}
}
