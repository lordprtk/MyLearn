package com.mf;

import com.mf.enums.FundType;

public class FundDetails{
	
	private String fundName;
	private FundType fundType;
	
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public FundType getFundType() {
		return fundType;
	}
	public void setFundType(FundType fundType) {
		this.fundType = fundType;
	}
	public FundDetails(String name){
		this.fundName = name;
	}
	
	@Override
	public int hashCode() {
		return this.fundName.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FundDetails){
			FundDetails newObj = (FundDetails) obj;
			if(this.getFundName().equals(newObj.getFundName())){
					return this.getFundType() == newObj.getFundType();
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	@Override
	public String toString() {
		return this.getFundName();
	}
}
