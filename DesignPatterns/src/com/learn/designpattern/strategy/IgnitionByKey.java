package com.learn.designpattern.strategy;

public class IgnitionByKey implements IgnitionBehaviour{

	@Override
	public void startCar() {
		System.out.println("Rotate the key to initiate ignition");
	}

}
