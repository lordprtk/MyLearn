package com.learn.designpattern.strategy;

public class BasicCar extends Car{
	public BasicCar(){
		setIgnition(new IgnitionByKey());
		setTransmission(new ManualTransmission());
	}

	@Override
	public void carBody() {
		System.out.println("This is a basic car");		
	}
	
	@Override
	public void stop() {
		super.stop();
		System.out.println("Press clutch, and set gear to neutral");
	}
}
