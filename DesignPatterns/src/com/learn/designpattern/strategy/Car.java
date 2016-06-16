package com.learn.designpattern.strategy;

public abstract class Car {
	private IgnitionBehaviour ignition;
	private TransmissionBehaviour transmission;
	
	public IgnitionBehaviour getIgnition() {
		return ignition;
	}
	public void setIgnition(IgnitionBehaviour ignition) {
		this.ignition = ignition;
	}
	public TransmissionBehaviour getTransmission() {
		return transmission;
	}
	public void setTransmission(TransmissionBehaviour transmission) {
		this.transmission = transmission;
	}
	
	private void accelerate(){
		System.out.println("Press Accelerator to move forward");
	}
	private void brake(){
		System.out.println("Press brake to slow down");
	}
	public void stop(){
		System.out.println("Keep the brake pressed till the vehicle stops");
	}
	private void startCar(){
		ignition.startCar();
	}
	private void changeGear(){
		transmission.changeGear();
	}
	
	public abstract void carBody();
	
	public void drive(){
		startCar();
		changeGear();
		accelerate();
		brake();
		accelerate();
		stop();
	}
}
