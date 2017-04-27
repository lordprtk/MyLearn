package com.java8;

public class AngleBetweenHourAndMinute {

	public static void main(String[] args){
		int hour=3;
		int min=26;
		
		float minAngle = (float) (min/60.0*360);
		float minHour = hour/12*360 + min/60*30;
		System.out.println(minAngle);
	}
}
