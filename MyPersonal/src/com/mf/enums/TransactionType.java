package com.mf.enums;
public enum TransactionType{
		BUY(0, "Buy"), SELL(1, "Sell");
		private final int code;
		private final String name;
		TransactionType(final int code, final String name) {
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