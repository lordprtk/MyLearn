package com.mf;

import java.math.BigDecimal;
import java.util.Date;

import com.mf.enums.TransactionType;

public class Transaction implements Comparable<Transaction>{
	
	private boolean sip;
	private TransactionType transactionType;
	private Date date;
	private BigDecimal amount;
	private BigDecimal nav;
	
	private BigDecimal units;
	private BigDecimal unitsStillHolding;
	private BigDecimal investmentCost;
	private BigDecimal currrentVal;
	private BigDecimal redeemedUnits_1;
	private Date redemptionDate_1;
	private BigDecimal redeemedUnits_2;
	private Date redemptionDate_2;
	private BigDecimal currentStatus;
	private BigDecimal returnPerc;

	public boolean isSip() {
		return sip;
	}
	public void setSip(boolean sip) {
		this.sip = sip;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getNav() {
		return nav;
	}
	public void setNav(BigDecimal nav) {
		this.nav = nav;
	}
	
	public Transaction(String sip, Date date, BigDecimal amount, BigDecimal nav) {
		if(sip.equals("Yes")){
			this.sip = true;
		}else{
			this.sip = false;
		}
		this.date = date;
		this.amount = amount;
		this.nav = nav;
	}
	@Override
	public int compareTo(Transaction o) {
		if(this.date.after(o.date)){
			return 1;
		}else if(this.date.before(o.date)){
			return -1;
		}
		return 0;
	}
	public BigDecimal getUnits() {
		return units;
	}
	public void setUnits(BigDecimal units) {
		this.units = units;
	}
	public BigDecimal getUnitsStillHolding() {
		return unitsStillHolding;
	}
	public void setUnitsStillHolding(BigDecimal unitsStillHolding) {
		this.unitsStillHolding = unitsStillHolding;
	}
	public BigDecimal getInvestmentCost() {
		return investmentCost;
	}
	public void setInvestmentCost(BigDecimal investmentCost) {
		this.investmentCost = investmentCost;
	}
	public BigDecimal getCurrrentVal() {
		return currrentVal;
	}
	public void setCurrrentVal(BigDecimal currrentVal) {
		this.currrentVal = currrentVal;
	}
	public BigDecimal getRedeemedUnits_1() {
		return redeemedUnits_1;
	}
	public void setRedeemedUnits_1(BigDecimal redeemedUnits_1) {
		this.redeemedUnits_1 = redeemedUnits_1;
	}
	public Date getRedemptionDate_1() {
		return redemptionDate_1;
	}
	public void setRedemptionDate_1(Date redemptionDate_1) {
		this.redemptionDate_1 = redemptionDate_1;
	}
	public BigDecimal getRedeemedUnits_2() {
		return redeemedUnits_2;
	}
	public void setRedeemedUnits_2(BigDecimal redeemedUnits_2) {
		this.redeemedUnits_2 = redeemedUnits_2;
	}
	public Date getRedemptionDate_2() {
		return redemptionDate_2;
	}
	public void setRedemptionDate_2(Date redemptionDate_2) {
		this.redemptionDate_2 = redemptionDate_2;
	}
	public BigDecimal getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(BigDecimal currentStatus) {
		this.currentStatus = currentStatus;
	}
	public BigDecimal getReturnPerc() {
		return returnPerc;
	}
	public void setReturnPerc(BigDecimal returnPerc) {
		this.returnPerc = returnPerc;
	}
}
