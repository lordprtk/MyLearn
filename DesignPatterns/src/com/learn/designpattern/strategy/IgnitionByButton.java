package com.learn.designpattern.strategy;

public class IgnitionByButton implements IgnitionBehaviour{

	@Override
	public void startCar() {
		System.out.println("Press Ignition button to initiate ignition");
	}

}
