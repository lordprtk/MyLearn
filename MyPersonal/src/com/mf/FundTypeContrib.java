package com.mf;

import java.math.BigDecimal;
import com.mf.enums.FundType;

public class FundTypeContrib {
	FundType fundType;
	BigDecimal investedVal;
	BigDecimal investPerc;
	BigDecimal currentVal;
	BigDecimal currentPerc;
	BigDecimal profit;
	
	public FundType getFundType() {
		return fundType;
	}
	public void setFundType(FundType fundType) {
		this.fundType = fundType;
	}
	public BigDecimal getInvestedVal() {
		return investedVal;
	}
	public void setInvestedVal(BigDecimal investedVal) {
		this.investedVal = investedVal;
	}
	public BigDecimal getCurrentVal() {
		return currentVal;
	}
	public void setCurrentVal(BigDecimal currentVal) {
		this.currentVal = currentVal;
	}
	public BigDecimal getInvestPerc() {
		return investPerc;
	}
	public void setInvestPerc(BigDecimal investPerc) {
		this.investPerc = investPerc;
	}
	public BigDecimal getCurrentPerc() {
		return currentPerc;
	}
	public void setCurrentPerc(BigDecimal currentPerc) {
		this.currentPerc = currentPerc;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
}
