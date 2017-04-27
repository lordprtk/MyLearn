package com.learn.designpattern.observer.usingJava;

public class WeatherStation {

	public static void main(String[] args) {
		WeatherData weather = new WeatherData();
		CurrentConditionsDisplay current = new CurrentConditionsDisplay();
		HeatIndexDisplay heatIndex = new HeatIndexDisplay();
		weather.addObserver(heatIndex);
		/*weather.registerObserver(current);
		weather.registerObserver(heatIndex);*/
		weather.setMeasurements(35, 20, 30);
	}
}
