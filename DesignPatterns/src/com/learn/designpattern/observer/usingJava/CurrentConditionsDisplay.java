package com.learn.designpattern.observer.usingJava;

import java.util.Observable;
import java.util.Observer;

public class CurrentConditionsDisplay implements Observer, DisplayElement {
	private float temperature;
	private float humidity;

	public void update(float temp, float pressure, float humidity) {
		this.temperature = temp;
		this.humidity = humidity;
		display();
	}

	@Override
	public void display() {
		System.out.println("Current Conditions --> TEMP - "+temperature+" F, HUMIDITY - "+humidity+"%");
	}

	@Override
	public void update(Observable paramObservable, Object paramObject) {
		// TODO Auto-generated method stub
		
	}

}
