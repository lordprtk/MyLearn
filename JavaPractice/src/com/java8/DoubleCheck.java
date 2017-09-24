package com.java8;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class DoubleCheck {

	public static void main(String[] args) {
		//NumberFormat.getInstance().
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("$"+(new BigDecimal("123.1245").setScale(2, RoundingMode.HALF_EVEN)));
	}

}