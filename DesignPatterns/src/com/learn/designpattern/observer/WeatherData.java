package com.learn.designpattern.observer;

import java.util.ArrayList;

public class WeatherData implements Subject {
	
	private ArrayList<Observer> observers;
	private float temperature;
	private float pressure;
	private float humidity;
	
	public WeatherData(){
		observers = new ArrayList<>();
	}
	public void measurementsChanged(){
		notifyObservers();
	}
	
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getPressure() {
		return pressure;
	}
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	public void setMeasurements(float temp, float pressure, float humidity){
		this.temperature = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		measurementsChanged();
	}

	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		int index = observers.indexOf(o);
		if(index >= 0){
			observers.remove(index);
		}
	}

	@Override
	public void notifyObservers() {
		for(Observer ob: observers){
			ob.update(this.temperature, this.pressure, this.humidity);
		}
	}

}
