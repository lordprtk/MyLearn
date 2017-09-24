package com.mf;

import java.math.BigDecimal;

import com.mf.enums.FundType;

public class AllFundSummary {
	private String fundName;
	private FundType fundType;
	private BigDecimal actualInvestment;
	private BigDecimal redeemedAmt;
	private BigDecimal unitsHolding;
	private BigDecimal investmentCost;
	private BigDecimal avgCost;
	private BigDecimal currentNAV;
	private BigDecimal currentVal;
	private BigDecimal unbookedProfit;
	private BigDecimal bookedProfit;
	private BigDecimal lastVal;
	
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public BigDecimal getActualInvestment() {
		return actualInvestment;
	}
	public void setActualInvestment(BigDecimal actualInvestment) {
		this.actualInvestment = actualInvestment;
	}
	public BigDecimal getRedeemedAmt() {
		return redeemedAmt;
	}
	public void setRedeemedAmt(BigDecimal redeemedAmt) {
		this.redeemedAmt = redeemedAmt;
	}
	public BigDecimal getUnitsHolding() {
		return unitsHolding;
	}
	public void setUnitsHolding(BigDecimal unitsHolding) {
		this.unitsHolding = unitsHolding;
	}
	public BigDecimal getInvestmentCost() {
		return investmentCost;
	}
	public void setInvestmentCost(BigDecimal investmentCost) {
		this.investmentCost = investmentCost;
	}
	public BigDecimal getAvgCost() {
		return avgCost;
	}
	public void setAvgCost(BigDecimal avgCost) {
		this.avgCost = avgCost;
	}
	public BigDecimal getCurrentNAV() {
		return currentNAV;
	}
	public void setCurrentNAV(BigDecimal currentNAV) {
		this.currentNAV = currentNAV;
	}
	public BigDecimal getCurrentVal() {
		return currentVal;
	}
	public void setCurrentVal(BigDecimal currentVal) {
		this.currentVal = currentVal;
	}
	public BigDecimal getUnbookedProfit() {
		return unbookedProfit;
	}
	public void setUnbookedProfit(BigDecimal unbookedProfit) {
		this.unbookedProfit = unbookedProfit;
	}
	public BigDecimal getBookedProfit() {
		return bookedProfit;
	}
	public void setBookedProfit(BigDecimal bookedProfit) {
		this.bookedProfit = bookedProfit;
	}
	public BigDecimal getLastVal() {
		return lastVal;
	}
	public void setLastVal(BigDecimal lastVal) {
		this.lastVal = lastVal;
	}
	public FundType getFundType() {
		return fundType;
	}
	public void setFundType(FundType fundType) {
		this.fundType = fundType;
	}
}
