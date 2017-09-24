package com.mf;

import java.math.BigDecimal;

public class AMCSummary {

	private BigDecimal currentInvestment = new BigDecimal(0);
	private BigDecimal currentValue = new BigDecimal(0);
	public BigDecimal getCurrentInvestment() {
		return currentInvestment;
	}
	public void setCurrentInvestment(BigDecimal currentInvestment) {
		this.currentInvestment = currentInvestment;
	}
	public BigDecimal getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}
}
