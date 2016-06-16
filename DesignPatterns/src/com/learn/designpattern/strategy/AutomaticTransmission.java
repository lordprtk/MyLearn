package com.learn.designpattern.strategy;

public class AutomaticTransmission implements TransmissionBehaviour {

	@Override
	public void changeGear() {
		System.out.println("Gear change automatically");
	}

}
