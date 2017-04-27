package com.learn.designpattern.observer;

public class CurrentConditionsDisplay implements Observer, DisplayElement {
	private float temperature;
	private float humidity;

	@Override
	public void update(float temp, float pressure, float humidity) {
		this.temperature = temp;
		this.humidity = humidity;
		display();
	}

	@Override
	public void display() {
		System.out.println("Current Conditions --> TEMP - "+temperature+" F, HUMIDITY - "+humidity+"%");
	}

}
