package com.mf.enums;
public enum FundFamily{
		DSP("DSP Blackrock"), ICICI("ICICI Prudential"), Birla("Birla"), Mirae("Mirae Asset"), HDFC("HDFC"), Kotak("Kotak Mahindra");
		private final String code;
		FundFamily(final String code){
			this.code=code;
		}
		public String getCode(){
			return code;
		}
	}