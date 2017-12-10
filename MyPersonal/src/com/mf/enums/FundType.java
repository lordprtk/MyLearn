package com.mf.enums;
public enum FundType{
		EQUITY(0, "Equity"), HYBRID(1, "Hybrid"), LIQUID(2, "Liquid"), USTP(3, "Ultra Short Term"), SHORT(4, "Short Term"), 
		DEBTLONG(5, "Debt Long Term"), DEBTMIP(6, "Debt MIP");
		private final int code;
		private final String name;
		FundType(final int code, final String name) {
			this.code = code;
			this.name = name;
		}
		public int getCode() {
			return code;
		}
		
		public String getName(){
			return name;
		}
	}