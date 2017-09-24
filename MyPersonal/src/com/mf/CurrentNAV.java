package com.mf;

import java.math.BigDecimal;

public class CurrentNAV {

	private BigDecimal latestNAV;
	private BigDecimal previousNAV;
	private BigDecimal oldestNAV;

	
	public BigDecimal getLatestNAV() {
		return latestNAV;
	}
	public void setLatestNAV(BigDecimal latestNAV) {
		this.latestNAV = latestNAV;
	}
	public BigDecimal getPreviousNAV() {
		return previousNAV;
	}
	public void setPreviousNAV(BigDecimal previousNAV) {
		this.previousNAV = previousNAV;
	}
	public BigDecimal getOldestNAV() {
		return oldestNAV;
	}
	public void setOldestNAV(BigDecimal oldestNAV) {
		this.oldestNAV = oldestNAV;
	}
	public CurrentNAV(BigDecimal latestNAV, BigDecimal previousNAV, BigDecimal oldestNAV) {
		this.latestNAV = latestNAV;
		this.previousNAV = previousNAV;
		this.oldestNAV = oldestNAV;
	}
}
