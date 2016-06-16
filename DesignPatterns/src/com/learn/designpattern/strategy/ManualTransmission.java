package com.learn.designpattern.strategy;

public class ManualTransmission implements TransmissionBehaviour {

	@Override
	public void changeGear() {
		System.out.println("Press Clutch, and shift gear");
	}

}
