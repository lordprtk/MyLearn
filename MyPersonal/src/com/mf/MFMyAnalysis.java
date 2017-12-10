package com.mf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mf.enums.FundFamily;
import com.mf.enums.FundType;
import com.mf.enums.TransactionType;


public class MFMyAnalysis {
	public final BigDecimal ZERO_VAL = BigDecimal.ZERO;
	
	private CellStyle styleHeaderRow = null;
	private CellStyle oddDataRow = null;
	private CellStyle evenDataRow = null;
	private CellStyle oddDataRowDate = null;
	private CellStyle evenDataRowDate = null;
	private CellStyle totalDataRow = null;
	private CellStyle negativeData = null;
	
	public static void main(String[] args){
		try{
			MFMyAnalysis myAnalysis = new MFMyAnalysis();
			XSSFWorkbook workbook = new XSSFWorkbook(new File("/Users/prafali/root/MyLearn/trunk/MyPersonal/MFTest.xlsx"));
			XSSFSheet sheetInvested = workbook.getSheet("Invested");
			XSSFSheet sheetShifi = workbook.getSheet("InvestedShifi");
			Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap = new HashMap<FundFamily, Map<FundDetails,List<Transaction>>>();
			myAnalysis.readInvestments(familyMap, sheetInvested);
			Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMapShifi = new HashMap<FundFamily, Map<FundDetails,List<Transaction>>>();
			myAnalysis.readInvestments(familyMapShifi, sheetShifi);
			
			XSSFSheet sheetLatestNAV = workbook.getSheet("LatestNAV - Table 1");
			Map<String, CurrentNAV> mapNAV = new HashMap<String, CurrentNAV>();
			myAnalysis.readCurrentNAV(mapNAV, sheetLatestNAV);
			
			workbook.close();
			
			Map<FundFamily, AMCSummary> amcSummary = new HashMap<FundFamily, AMCSummary>();
			List<AllFundSummary> summaryListPrtk = new ArrayList<AllFundSummary>();
			myAnalysis.analyseFunds(familyMap, mapNAV, amcSummary);
			for(FundFamily fundFamily : familyMap.keySet()){
				Map<FundDetails, List<Transaction>> family = familyMap.get(fundFamily);
				family.keySet().stream().forEach(fund -> myAnalysis.analyseForAllSheet(fund, family.get(fund), summaryListPrtk, mapNAV));
			}
			Map<FundType, FundTypeContrib> mapFundTypeContribs = myAnalysis.analyseFundTypeContrib(summaryListPrtk);
			
			Map<FundFamily, AMCSummary> amcSummaryShifi = new HashMap<FundFamily, AMCSummary>();
			List<AllFundSummary> summaryListShifi = new ArrayList<AllFundSummary>();
			myAnalysis.analyseFunds(familyMapShifi, mapNAV, amcSummaryShifi);
			for(FundFamily fundFamily : familyMapShifi.keySet()){
				Map<FundDetails, List<Transaction>> family = familyMapShifi.get(fundFamily);
				family.keySet().stream().forEach(fund -> myAnalysis.analyseForAllSheet(fund, family.get(fund), summaryListShifi, mapNAV));
			}
			Map<FundType, FundTypeContrib> mapFundTypeContribsShifi = myAnalysis.analyseFundTypeContrib(summaryListShifi);
			
			myAnalysis.writeOutput(familyMap, amcSummary, "prtk", summaryListPrtk, summaryListShifi, mapFundTypeContribs, mapFundTypeContribsShifi);
			myAnalysis.writeOutput(familyMapShifi, amcSummaryShifi, "shifi", null, null, null, null);
			System.out.println("Completed");
		}catch(IOException e){
			System.out.println("Exception ");
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("all exceptions");
			e.printStackTrace();
		}
	}
	
	private Map<FundType, FundTypeContrib> analyseFundTypeContrib(List<AllFundSummary> summaryList){
		Map<FundType, FundTypeContrib> mapFundTypeContribs = new HashMap<FundType, FundTypeContrib>();
		BigDecimal totalCost = new BigDecimal(0);
		BigDecimal currentVal = new BigDecimal(0);
		for(AllFundSummary summary : summaryList){
			if(summary.getUnitsHolding().compareTo(ZERO_VAL) > 0){
				FundType fundType = summary.getFundType();
				FundTypeContrib contrib = mapFundTypeContribs.get(fundType);
				if(null == contrib){
					contrib = new FundTypeContrib();
					contrib.setFundType(fundType);
					contrib.setCurrentVal(new BigDecimal(0));
					contrib.setInvestedVal(new BigDecimal(0));
				}
				contrib.setCurrentVal(contrib.getCurrentVal().add(summary.getCurrentVal()));
				contrib.setInvestedVal(contrib.getInvestedVal().add(summary.getInvestmentCost()));
				mapFundTypeContribs.put(fundType, contrib);
				totalCost = totalCost.add(summary.getInvestmentCost());
				currentVal = currentVal.add(summary.getCurrentVal());
			}
		}
		if(totalCost.compareTo(ZERO_VAL) > 0 && currentVal.compareTo(ZERO_VAL) > 0){
			for(FundType type : mapFundTypeContribs.keySet()){
				FundTypeContrib contrib = mapFundTypeContribs.get(type);
				contrib.setCurrentPerc(contrib.getCurrentVal().divide(currentVal, 2, RoundingMode.HALF_EVEN));
				contrib.setInvestPerc(contrib.getInvestedVal().divide(totalCost, 2, RoundingMode.HALF_EVEN));
				contrib.setProfit(contrib.getCurrentVal().subtract(contrib.getInvestedVal()));
			}
		}
		return mapFundTypeContribs;
	}
	
	private void analyseForAllSheet(FundDetails fund, List<Transaction> listTransactions, List<AllFundSummary> sumList, 
			Map<String,CurrentNAV> mapNAV){
		AllFundSummary sum = new AllFundSummary();
		String fundName = fund.getFundName();
		BigDecimal actualInvestment = new BigDecimal(0);
		BigDecimal redeemedAmt = new BigDecimal(0);
		BigDecimal unitsHolding = new BigDecimal(0);
		BigDecimal investmentCost = new BigDecimal(0);
		BigDecimal currentNAV = mapNAV.get(fundName).getLatestNAV();
		BigDecimal costOfRedeemedUnits = new BigDecimal(0);
		
		for(Transaction tr : listTransactions){
			if(tr.getTransactionType() == TransactionType.BUY){
				actualInvestment = actualInvestment.add(tr.getAmount());
				unitsHolding = unitsHolding.add(tr.getUnitsStillHolding());
				investmentCost = investmentCost.add(tr.getInvestmentCost());
				if(tr.getUnitsStillHolding().compareTo(ZERO_VAL) == 0){
					costOfRedeemedUnits = costOfRedeemedUnits.add(tr.getAmount());
				}else{
					BigDecimal redeemedUnitsCost = tr.getUnits().subtract(tr.getUnitsStillHolding()).multiply(tr.getNav()).setScale(2, RoundingMode.HALF_EVEN);
					costOfRedeemedUnits = costOfRedeemedUnits.add(redeemedUnitsCost);
				}
			}else{
				redeemedAmt = redeemedAmt.add(tr.getAmount());
			}
		}
		sum.setFundName(fundName);
		sum.setFundType(fund.getFundType());
		sum.setActualInvestment(actualInvestment);
		sum.setRedeemedAmt(redeemedAmt);
		sum.setUnitsHolding(unitsHolding);
		sum.setInvestmentCost(investmentCost);
		sum.setCurrentNAV(currentNAV);
		if(unitsHolding.compareTo(ZERO_VAL) > 0){
			sum.setCurrentVal(currentNAV.multiply(unitsHolding).setScale(2, RoundingMode.HALF_EVEN));
			sum.setLastVal(mapNAV.get(fundName).getPreviousNAV().multiply(unitsHolding).setScale(2, RoundingMode.HALF_EVEN));
			sum.setAvgCost(investmentCost.divide(unitsHolding, 4, RoundingMode.HALF_EVEN));
			sum.setUnbookedProfit(sum.getCurrentVal().subtract(sum.getInvestmentCost()));
		}else{
			sum.setCurrentVal(ZERO_VAL);
		}
		sum.setBookedProfit(sum.getRedeemedAmt().subtract(costOfRedeemedUnits));
		
		synchronized (sumList) {
			sumList.add(sum);
		}
	}
	
	private void readCurrentNAV(Map<String, CurrentNAV> mapNAV, XSSFSheet sheetLatestNAV){
		Iterator<Row> iterNAV = sheetLatestNAV.iterator();
		while(iterNAV.hasNext()){
			Row row = iterNAV.next();
			if(row.getRowNum() == 0){
				row = iterNAV.next();
			}
			String fundName = row.getCell(0).getStringCellValue();
			BigDecimal todayNAV = BigDecimal.valueOf(row.getCell(1).getNumericCellValue());
			BigDecimal previousNAV = BigDecimal.valueOf(row.getCell(2).getNumericCellValue());
			BigDecimal lastNAV = BigDecimal.valueOf(row.getCell(3).getNumericCellValue());
			CurrentNAV currentNAV = new CurrentNAV(todayNAV, previousNAV, lastNAV);
			mapNAV.put(fundName, currentNAV);
		}
	}
	
	private void analyseFunds(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, Map<String, 
			CurrentNAV> mapNAV, Map<FundFamily, AMCSummary> amcSummaryDetails) throws Exception{
		for(FundFamily family : familyMap.keySet()){
			Map<FundDetails, List<Transaction>> funds = familyMap.get(family);
			AMCSummary amcSummary = new AMCSummary();
			for(FundDetails fund : funds.keySet()){
				CurrentNAV currentNav = mapNAV.get(fund.getFundName());
				List<Transaction> transactions = funds.get(fund);
				transactions.stream().forEach(tr -> tr.setUnits(tr.getAmount().divide(tr.getNav(), 3, RoundingMode.HALF_EVEN)));
				List<Transaction> processedTr = new ArrayList<Transaction>();
				for(Transaction tr : transactions){
					analyseTransaction(processedTr, tr, currentNav);
					processedTr.add(tr);
				}
				for(Transaction tr : transactions){
					if(tr.getTransactionType() == TransactionType.BUY){
						if(tr.getUnitsStillHolding().compareTo(ZERO_VAL) > 0){

							tr.setCurrentStatus(tr.getCurrrentVal().subtract(tr.getInvestmentCost()));
							BigDecimal val_1 = tr.getCurrentStatus().multiply(new BigDecimal(36500));
							LocalDate ld = new java.sql.Date(tr.getDate().getTime()).toLocalDate();
							long time = ChronoUnit.DAYS.between(ld, LocalDate.now());
							BigDecimal val_2 = tr.getInvestmentCost().multiply(new BigDecimal(time));
							if(val_2.compareTo(ZERO_VAL) != 0){
								tr.setReturnPerc(val_1.divide(val_2, 3, RoundingMode.HALF_EVEN));
							}

							amcSummary.setCurrentInvestment(amcSummary.getCurrentInvestment().add(tr.getInvestmentCost()));
							amcSummary.setCurrentValue(amcSummary.getCurrentValue().add(tr.getCurrrentVal()));
						}
					}
				}
			}
			amcSummaryDetails.put(family, amcSummary);
		}
	}
	
	private void setInvCost_CurrVal(Transaction tr, CurrentNAV currentNav){
		if(tr.getUnitsStillHolding()!=null){
			tr.setInvestmentCost(tr.getUnitsStillHolding().multiply(tr.getNav()).setScale(2, RoundingMode.HALF_EVEN));
			tr.setCurrrentVal(tr.getUnitsStillHolding().multiply(currentNav.getLatestNAV()).setScale(2, RoundingMode.HALF_EVEN));
		}else{
			tr.setInvestmentCost(null);
			tr.setCurrrentVal(null);
		}
	}
	
	private void analyseTransaction(List<Transaction> processed, Transaction tr, CurrentNAV currentNav){
		if(tr.getTransactionType() == TransactionType.BUY){
			tr.setUnitsStillHolding(tr.getUnits());
			tr.setRedeemedUnits_1(null);
			tr.setRedemptionDate_1(null);
			tr.setRedeemedUnits_2(null);
			tr.setRedemptionDate_2(null);
			tr.setRedeemedUnits_3(null);
			tr.setRedemptionDate_3(null);
			tr.setInvestmentCost(tr.getAmount());
			tr.setCurrrentVal(tr.getUnitsStillHolding().multiply(currentNav.getLatestNAV()).setScale(2, RoundingMode.HALF_EVEN));
		}else if(tr.getTransactionType() == TransactionType.SELL){
			tr.setUnitsStillHolding(null);
			tr.setRedeemedUnits_1(null);
			tr.setRedemptionDate_1(null);
			tr.setRedeemedUnits_2(null);
			tr.setRedemptionDate_2(null);
			tr.setRedeemedUnits_3(null);
			tr.setRedemptionDate_3(null);
			BigDecimal units = new BigDecimal(tr.getUnits().toPlainString());
			for(Transaction pTr : processed){
				if(units.compareTo(ZERO_VAL) > 0){
					if(pTr.getTransactionType() == TransactionType.BUY){
						BigDecimal redeemedUnits = null;
						if(pTr.getUnitsStillHolding().compareTo(ZERO_VAL) > 0){
							if(pTr.getUnitsStillHolding().compareTo(units) >= 0){
								pTr.setUnitsStillHolding(pTr.getUnitsStillHolding().subtract(units));
								redeemedUnits = units;
								units = ZERO_VAL;
							}else{
								units = units.subtract(pTr.getUnitsStillHolding());
								redeemedUnits = pTr.getUnitsStillHolding();
								pTr.setUnitsStillHolding(ZERO_VAL);
							}
							setInvCost_CurrVal(pTr, currentNav);
							if(null == pTr.getRedeemedUnits_1()){
								pTr.setRedeemedUnits_1(redeemedUnits);
								pTr.setRedemptionDate_1(tr.getDate());
							}else if(null == pTr.getRedeemedUnits_2()){
								pTr.setRedeemedUnits_2(redeemedUnits);
								pTr.setRedemptionDate_2(tr.getDate());
							}else if(null == pTr.getRedeemedUnits_3()){
								pTr.setRedeemedUnits_3(redeemedUnits);
								pTr.setRedemptionDate_3(tr.getDate());
							}
						}
					}
				}else{
					break;
				}
			}
		}
	}
	
	private void readInvestments(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, XSSFSheet sheetInvested) throws Exception{
		Iterator<Row> iter = sheetInvested.iterator();
		while(iter.hasNext()){
			Row row = iter.next();
			if(row.getRowNum() == 0){
				row = iter.next();
			}
			getTransactionDetails(familyMap, row);
		}
	}
	
	private void createAllFundSheet(XSSFWorkbook workOut, List<AllFundSummary> summaryList, List<AllFundSummary> summaryListShifi,
			Map<FundType, FundTypeContrib> mapFundTypeContribs, Map<FundType, FundTypeContrib> mapFundTypeContribsShifi){
		XSSFSheet sheet = workOut.createSheet("AllMine");
		writeToAllSheet(sheet, summaryList, mapFundTypeContribs);
		
		sheet = workOut.createSheet("AllShifi");
		writeToAllSheet(sheet, summaryListShifi, mapFundTypeContribsShifi);
	}
	
	private void writeToAllSheet(XSSFSheet sheet, List<AllFundSummary> summaryList, Map<FundType, FundTypeContrib> mapFundTypeContribs){
		int rowNum = 0;
		Row row = sheet.createRow(rowNum++);
		createHeaderAll(row);
		BigDecimal netInvestment = new BigDecimal(0);
		BigDecimal netCurrentValue = new BigDecimal(0);
		BigDecimal netLastValue = new BigDecimal(0);
		BigDecimal netBookedProfit = new BigDecimal(0);
		BigDecimal netUnbookedProfit = new BigDecimal(0);
		BigDecimal netRedeemedAmt = new BigDecimal(0);
		for(AllFundSummary summary : summaryList){
			row = sheet.createRow(rowNum++);
			createSummaryRow(summary, row);
			if(null != summary.getInvestmentCost()){
				netInvestment = netInvestment.add(summary.getInvestmentCost());
			}
			if(null != summary.getCurrentVal()){
				netCurrentValue = netCurrentValue.add(summary.getCurrentVal());
			}
			if(null != summary.getLastVal()){
				netLastValue = netLastValue.add(summary.getLastVal());
			}
			if(null != summary.getUnbookedProfit()){
				netUnbookedProfit = netUnbookedProfit.add(summary.getUnbookedProfit());
			}
			if(null != summary.getBookedProfit()){
				netBookedProfit = netBookedProfit.add(summary.getBookedProfit());
			}
			if(null != summary.getRedeemedAmt()){
				netRedeemedAmt = netRedeemedAmt.add(summary.getRedeemedAmt());
			}
		}
		row = sheet.createRow(rowNum++);
		Cell cell = getCell(row, 3, "T", false);
		cell.setCellValue(netRedeemedAmt.doubleValue());
		cell = getCell(row, 5, "T", false);
		cell.setCellValue(netInvestment.doubleValue());
		cell = getCell(row, 8, "T", false);
		cell.setCellValue(netCurrentValue.doubleValue());
		cell = getCell(row, 9, "T", false);
		cell.setCellValue(netUnbookedProfit.doubleValue());
		cell = getCell(row, 10, "T", false);
		cell.setCellValue(netBookedProfit.doubleValue());
		cell = getCell(row, 11, "T", false);
		cell.setCellValue(netLastValue.doubleValue());
		
		BigDecimal change = netCurrentValue.subtract(netLastValue);
		String str = null;
		if(change.compareTo(ZERO_VAL) < 0){
			str = "Difference - Loss "+change.abs().toPlainString();
		}else{
			str = "Difference - Profit "+change.abs().toPlainString();
		}
		cell = getCell(row, 12, "T", false);;
		cell.setCellValue(str);
		row = sheet.createRow(rowNum++);
		cell = getCell(row, 10, "T", false);
		cell = getCell(row, 9, "T", false);
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cell.setCellStyle(cellStyle);
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,9,10));
		cell.setCellValue(netBookedProfit.add(netUnbookedProfit).doubleValue());
		writeFundTypeContrib(sheet, mapFundTypeContribs);
	}
	
	private void writeFundTypeContrib(XSSFSheet sheet, Map<FundType, FundTypeContrib> mapFundTypeContribs){
		Integer rowNum = 0;
		Row row = sheet.getRow(rowNum++);
		createHeaderFundTypeContrib(row);
		for(FundType fundType : mapFundTypeContribs.keySet()){
			Integer cellNum = 14;
			row = sheet.getRow(rowNum++);
			String type = "O";
			if(row.getRowNum()%2==0){
				type = "E";
			}
			FundTypeContrib contrib = mapFundTypeContribs.get(fundType);
			Cell cell = getCell(row, cellNum++, type, false);
			cell.setCellValue(contrib.getFundType().getName());
			cell = getCell(row, cellNum++, type, false);
			cell.setCellValue(contrib.getInvestedVal().doubleValue());
			cell = getCell(row, cellNum++, type, false);
			cell.setCellValue(contrib.getInvestPerc().doubleValue());
			cell = getCell(row, cellNum++, type, false);
			cell.setCellValue(contrib.getProfit().doubleValue());
			cell = getCell(row, cellNum++, type, false);
			cell.setCellValue(contrib.getCurrentVal().doubleValue());
			cell = getCell(row, cellNum++, type, false);
			cell.setCellValue(contrib.getCurrentPerc().doubleValue());
		}
	}
	
	private void createHeaderFundTypeContrib(Row row){
		Integer cellNum = 14;
		Cell cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Fund Type");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Investment Made");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Investment Percentage");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Profit");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Current Value");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Current Percentage");
		cell = getCell(row, cellNum++, "H", false);
	}
	
	private void createSummaryRow(AllFundSummary summary, Row row){
		Integer cellNum = 0;
		String type = "O";
		if(row.getRowNum()%2==0){
			type = "E";
		}
		Cell cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getFundName());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getFundType().getName());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getActualInvestment().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getRedeemedAmt().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getUnitsHolding().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getInvestmentCost().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		if(null != summary.getAvgCost()){
			cell.setCellValue(summary.getAvgCost().doubleValue());
		}
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getCurrentNAV().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(summary.getCurrentVal().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		if(null != summary.getUnbookedProfit()){
			if(summary.getUnbookedProfit().compareTo(ZERO_VAL) < 0) {
				cell.setCellStyle(negativeData);
			}
			cell.setCellValue(summary.getUnbookedProfit().doubleValue());
		}
		cell = getCell(row, cellNum++, type, false);
		if(summary.getBookedProfit().compareTo(ZERO_VAL) < 0) {
			cell.setCellStyle(negativeData);
		}
		cell.setCellValue(summary.getBookedProfit().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		if(null != summary.getLastVal()){
			cell.setCellValue(summary.getLastVal().doubleValue());
		}
		cell = getCell(row, cellNum++, type, false);
		if(null != summary.getLastVal() && null != summary.getCurrentVal()){
			cell.setCellValue(summary.getCurrentVal().subtract(summary.getLastVal()).doubleValue());
		}
	}
	
	private void createHeaderAll(Row row){
		Integer cellNum = 0;
		Cell cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Fund Name");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Fund Type");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Actual Investment");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Redeemed Amount");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Units Holding");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Investment Cost");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Average Cost");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Current NAV");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Current Value");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Unbooked Profit");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Booked Profit");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Last Net Value");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Difference From Last");
	}
	
	private void createAllStyles(XSSFWorkbook workBook){
		CreationHelper createHelper = workBook.getCreationHelper();
		
		XSSFCellStyle cellStyle = (XSSFCellStyle)workBook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(115, 115, 255)));
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		styleHeaderRow = cellStyle;
		
		cellStyle = (XSSFCellStyle)workBook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(155, 205, 230)));
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		oddDataRow = cellStyle;
		
		cellStyle = (XSSFCellStyle)workBook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(155, 205, 230)));
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mmm-yy"));
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		oddDataRowDate = cellStyle;
		
		cellStyle = (XSSFCellStyle)workBook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(160, 194, 180)));
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		evenDataRow = cellStyle;
		
		cellStyle = (XSSFCellStyle)workBook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(160, 194, 180)));
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mmm-yy"));
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		evenDataRowDate = cellStyle;
		
		cellStyle = (XSSFCellStyle)workBook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(80, 194, 50)));
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		totalDataRow = cellStyle;
		
		cellStyle = (XSSFCellStyle)workBook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(250, 10, 10)));
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		negativeData = cellStyle;
	}
	
	private void writeOutput(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, Map<FundFamily, AMCSummary> amcSummary, 
			String name, List<AllFundSummary> summaryList, List<AllFundSummary> summaryListShifi, 
			Map<FundType, FundTypeContrib> mapFundTypeContribs, Map<FundType, FundTypeContrib> mapFundTypeContribsShifi) throws Exception{
		XSSFWorkbook workOut = new XSSFWorkbook();
		createAllStyles(workOut);
		
		if(summaryList != null && summaryListShifi != null){
			createAllFundSheet(workOut, summaryList, summaryListShifi, mapFundTypeContribs, mapFundTypeContribsShifi);
		}
		for(FundFamily family : familyMap.keySet()){
			int rowNum = 0;
			XSSFSheet sheet = workOut.createSheet(family.getCode());
			Map<FundDetails, List<Transaction>> map = familyMap.get(family);
			Row row = sheet.createRow(rowNum++);
			createHeader(row);
			int counter = -1;
			for(FundDetails fund : map.keySet()){
				counter++;
				String type = "O";
				if(counter % 2 == 0){
					type = "E";
				}
				List<Transaction> trans = map.get(fund);
				Collections.sort(trans);
				for(Transaction tr: trans){
					row = sheet.createRow(rowNum++);
					//createTransactionRow(sheet, fund, tr, rowNum);
					createTransactionRow(row, fund, tr, type);
				}
			}
			rowNum+=2;
			createAmcSummary(amcSummary.get(family), rowNum, sheet);
		}
		FileOutputStream fileOut = new FileOutputStream("/Users/prafali/root/MyLearn/trunk/MyPersonal/"+name+".xlsx");
		workOut.write(fileOut);
		fileOut.close();
		workOut.close();
	}
	
	private void createAmcSummary(AMCSummary amcSummary, int rowNum, XSSFSheet sheet){
		Row row = sheet.createRow(rowNum++);
		int cellNum = 0;
		Cell cell = getCell(row, cellNum++, "T", false);
		cell.setCellValue("Current Cost");
		cell = getCell(row, cellNum++, "T", false);
		cell.setCellValue(amcSummary.getCurrentInvestment().doubleValue());
		
		row = sheet.createRow(rowNum++);
		cellNum = 0;
		cell = getCell(row, cellNum++, "T", false);
		cell.setCellValue("Current Value");
		cell = getCell(row, cellNum++, "T", false);
		cell.setCellValue(amcSummary.getCurrentValue().doubleValue());
		
		row = sheet.createRow(rowNum++);
		cellNum = 0;
		cell = getCell(row, cellNum++, "T", false);
		cell.setCellValue("Profit");
		cell = getCell(row, cellNum++, "T", false);
		cell.setCellValue(amcSummary.getCurrentValue().subtract(amcSummary.getCurrentInvestment()).doubleValue());
	}
	
	private Cell getCell(Row row, Integer cellNum, String type, boolean dateType){
		Cell cell = row.createCell(cellNum);
		if(type.equals("H")){
			cell.setCellStyle(styleHeaderRow);
		}else if(type.equals("E")){
			if(!dateType){
				cell.setCellStyle(evenDataRow);
			}else{
				cell.setCellStyle(evenDataRowDate);
			}
		}else if(type.equals("O")){
			if(!dateType){
				cell.setCellStyle(oddDataRow);
			}else{
				cell.setCellStyle(oddDataRowDate);
			}
		}else if(type.equals("T")){
			cell.setCellStyle(totalDataRow);
		}else if(type.equals("N")) {
			cell.setCellStyle(negativeData);
		}
		return cell;
	}
	
	private void createTransactionRow(Row row, FundDetails fund, Transaction transaction, String type){
		
		Integer cellNum = 0;
		Cell cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(fund.getFundName());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(fund.getFundType().getName());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(transaction.getTransactionType().getName());
		cell = getCell(row, cellNum++, type, true);
		cell.setCellValue(transaction.getDate());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(transaction.getAmount().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(transaction.getNav().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		cell.setCellValue(transaction.getUnits().doubleValue());
		cell = getCell(row, cellNum++, type, false);
		if(transaction.getUnitsStillHolding() != null){
			cell.setCellValue(transaction.getUnitsStillHolding().doubleValue());
		}
		cell = getCell(row, cellNum++, type, false);
		if(transaction.getInvestmentCost() != null){
			cell.setCellValue(transaction.getInvestmentCost().doubleValue());
		}
		cell = getCell(row, cellNum++, type, false);
		if(transaction.getCurrrentVal() != null){
			cell.setCellValue(transaction.getCurrrentVal().doubleValue());
		}
		cell = getCell(row, cellNum++, type, false);
		if(transaction.getRedeemedUnits_1() != null){
			cell.setCellValue(transaction.getRedeemedUnits_1().doubleValue());
		}
		cell = getCell(row, cellNum++, type, true);
		if(transaction.getRedemptionDate_1() != null){
			cell.setCellValue(transaction.getRedemptionDate_1());
		}
		cell = getCell(row, cellNum++, type, false);
		if(transaction.getRedeemedUnits_2() != null){
			cell.setCellValue(transaction.getRedeemedUnits_2().doubleValue());
		}
		cell = getCell(row, cellNum++, type, true);
		if(transaction.getRedemptionDate_2() != null){
			cell.setCellValue(transaction.getRedemptionDate_2());
		}
		cell = getCell(row, cellNum++, type, false);
		if(transaction.getRedeemedUnits_3() != null){
			cell.setCellValue(transaction.getRedeemedUnits_3().doubleValue());
		}
		cell = getCell(row, cellNum++, type, true);
		if(transaction.getRedemptionDate_3() != null){
			cell.setCellValue(transaction.getRedemptionDate_3());
		}
		cell = getCell(row, cellNum++, type, false);
		if(null != transaction.getCurrentStatus()){
			cell.setCellValue(transaction.getCurrentStatus().doubleValue());
		}
		cell = getCell(row, cellNum++, type, false);
		if(null != transaction.getReturnPerc()){
			cell.setCellValue(transaction.getReturnPerc().doubleValue());
		}
	}
	
	private void createHeader(Row row){
		Integer cellNum = 0;
		Cell cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Fund Name");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Fund Type");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Transaction Type");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Date");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Amount");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("NAV");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Units");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Units Still Holding");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Investment Cost");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Current Value");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Redeemed Unit 1");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Redemption Date 1");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Redeemed Unit 2");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Redemption Date 2");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Redeemed Unit 3");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Redemption Date 3");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Current Status");
		cell = getCell(row, cellNum++, "H", false);
		cell.setCellValue("Return %");
	}
	
	private void setFundType(String fundType, FundDetails fund) throws Exception{
		switch (fundType) {
		case "Equity":
			fund.setFundType(FundType.EQUITY);
			break;
		case "Hybrid":
			fund.setFundType(FundType.HYBRID);
			break;
		case "Ultra Short Term":
			fund.setFundType(FundType.USTP);
			break;
		case "Liquid":
			fund.setFundType(FundType.LIQUID);
			break;
		case "Short Term":
			fund.setFundType(FundType.SHORT);
			break;
		case "Debt Long Term":
			fund.setFundType(FundType.DEBTLONG);
			break;
		case "Debt MIP":
			fund.setFundType(FundType.DEBTMIP);
			break;
		default:
			throw new Exception("Unknown Fund Type");
		}
	}
	
	private void setTransactionType(String type, Transaction trans) throws Exception{
		switch(type){
		case "Buy":
			trans.setTransactionType(TransactionType.BUY);
			break;
		case "Sell":
			trans.setTransactionType(TransactionType.SELL);
			break;
		default:
			throw new Exception("Unknown Transaction type");
		}
	}
	
	private FundFamily getFundFamily(String fundFamily) throws Exception{
		switch (fundFamily) {
		case "ICICI Prudential":
			return FundFamily.ICICI;
		case "ABSL":
			return FundFamily.ABSL;
		case "DSP Blackrock":
			return FundFamily.DSP;
		case "HDFC":
			return FundFamily.HDFC;
		case "Kotak Mahindra":
			return FundFamily.Kotak;
		case "Mirae Asset":
			return FundFamily.Mirae;
		case "Principal":
			return FundFamily.Principal;
		default:
			throw new Exception("unknown Family");
		}
	}
	
	private void getTransactionDetails(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, Row row) throws Exception{
		String fundName = row.getCell(0).getStringCellValue();
		String family = row.getCell(1).getStringCellValue();
		String fundType = row.getCell(3).getStringCellValue();
		
		FundFamily fundFamily = getFundFamily(family);
		
		FundDetails fund = new FundDetails(fundName);
		setFundType(fundType, fund);
		
		Map<FundDetails, List<Transaction>> map = null;
		if(familyMap.containsKey(fundFamily)){
			map = familyMap.get(fundFamily);
		}else{
			map = new HashMap<FundDetails, List<Transaction>>();
			familyMap.put(fundFamily, map);
		}
		List<Transaction> transactions = null;
		
		if(map.containsKey(fund)){
			transactions = map.get(fund);
		}else{
			transactions = new ArrayList<Transaction>();
			map.put(fund, transactions);
		}
		
		String transactionType = row.getCell(4).getStringCellValue();
		String sip = row.getCell(2).getStringCellValue();
		Date date = row.getCell(5).getDateCellValue();
		BigDecimal amount = BigDecimal.valueOf(row.getCell(6).getNumericCellValue());
		BigDecimal nav = BigDecimal.valueOf(row.getCell(7).getNumericCellValue());
		Transaction transaction = new Transaction(sip, date, amount, nav);
		setTransactionType(transactionType, transaction);
		transactions.add(transaction);
	}

	
}
