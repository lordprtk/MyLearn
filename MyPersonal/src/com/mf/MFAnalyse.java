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


public class MFAnalyse {
	public final static BigDecimal ZERO_VAL = BigDecimal.ZERO;
	
	public static void main(String[] args){
		try{
			XSSFWorkbook workbook = new XSSFWorkbook(new File("c:\\myreads\\mftest.xlsx"));
			XSSFSheet sheetInvested = workbook.getSheet("Invested");
			XSSFSheet sheetShifi = workbook.getSheet("InvestedShifi");
			Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap = new HashMap<FundFamily, Map<FundDetails,List<Transaction>>>();
			readInvestments(familyMap, sheetInvested);
			Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMapShifi = new HashMap<FundFamily, Map<FundDetails,List<Transaction>>>();
			readInvestments(familyMapShifi, sheetShifi);
			
			XSSFSheet sheetLatestNAV = workbook.getSheet("LatestNAV");
			Map<String, CurrentNAV> mapNAV = new HashMap<String, CurrentNAV>();
			readCurrentNAV(mapNAV, sheetLatestNAV);
			
			workbook.close();
			
			Map<FundFamily, AMCSummary> amcSummary = new HashMap<FundFamily, AMCSummary>();
			List<AllFundSummary> summaryListPrtk = new ArrayList<AllFundSummary>();
			analyseFunds(familyMap, mapNAV, amcSummary);
			for(FundFamily fundFamily : familyMap.keySet()){
				Map<FundDetails, List<Transaction>> family = familyMap.get(fundFamily);
				family.keySet().stream().forEach(fund -> analyseForAllSheet(fund, family.get(fund), summaryListPrtk, mapNAV));
			}
			Map<FundType, FundTypeContrib> mapFundTypeContribs = analyseFundTypeContrib(summaryListPrtk);
			Map<FundFamily, AMCSummary> amcSummaryShifi = new HashMap<FundFamily, AMCSummary>();
			List<AllFundSummary> summaryListShifi = new ArrayList<AllFundSummary>();
			analyseFunds(familyMapShifi, mapNAV, amcSummaryShifi);
			for(FundFamily fundFamily : familyMapShifi.keySet()){
				Map<FundDetails, List<Transaction>> family = familyMapShifi.get(fundFamily);
				family.keySet().stream().forEach(fund -> analyseForAllSheet(fund, family.get(fund), summaryListShifi, mapNAV));
			}
			
			writeOutput(familyMap, amcSummary, "prtk", summaryListPrtk, summaryListShifi);
			writeOutput(familyMapShifi, amcSummaryShifi, "shifi", null, null);
			System.out.println("Completed");
		}catch(IOException e){
			System.out.println("Exception ");
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("all exceptions");
			e.printStackTrace();
		}
	}
	
	private static Map<FundType, FundTypeContrib> analyseFundTypeContrib(List<AllFundSummary> summaryList){
		Map<FundType, FundTypeContrib> mapFundTypeContribs = new HashMap<FundType, FundTypeContrib>();
		for(AllFundSummary summary : summaryList){
			if(summary.getUnitsHolding().compareTo(ZERO_VAL) > 0){
				
			}
		}
		return mapFundTypeContribs;
	}
	
	private static void analyseForAllSheet(FundDetails fund, List<Transaction> listTransactions, List<AllFundSummary> sumList, 
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
	
	private static void readCurrentNAV(Map<String, CurrentNAV> mapNAV, XSSFSheet sheetLatestNAV){
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
	
	private static void analyseFunds(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, Map<String, 
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
	
	private static void setInvCost_CurrVal(Transaction tr, CurrentNAV currentNav){
		if(tr.getUnitsStillHolding()!=null){
			tr.setInvestmentCost(tr.getUnitsStillHolding().multiply(tr.getNav()).setScale(2, RoundingMode.HALF_EVEN));
			tr.setCurrrentVal(tr.getUnitsStillHolding().multiply(currentNav.getLatestNAV()).setScale(2, RoundingMode.HALF_EVEN));
		}else{
			tr.setInvestmentCost(null);
			tr.setCurrrentVal(null);
		}
	}
	
	private static void analyseTransaction(List<Transaction> processed, Transaction tr, CurrentNAV currentNav){
		if(tr.getTransactionType() == TransactionType.BUY){
			tr.setUnitsStillHolding(tr.getUnits());
			tr.setRedeemedUnits_1(null);
			tr.setRedemptionDate_1(null);
			tr.setRedeemedUnits_2(null);
			tr.setRedemptionDate_2(null);
			tr.setInvestmentCost(tr.getAmount());
			tr.setCurrrentVal(tr.getUnitsStillHolding().multiply(currentNav.getLatestNAV()).setScale(2, RoundingMode.HALF_EVEN));
		}else if(tr.getTransactionType() == TransactionType.SELL){
			tr.setUnitsStillHolding(null);
			tr.setRedeemedUnits_1(null);
			tr.setRedemptionDate_1(null);
			tr.setRedeemedUnits_2(null);
			tr.setRedemptionDate_2(null);
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
							}
						}
					}
				}else{
					break;
				}
			}
		}
	}
	
	private static void readInvestments(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, XSSFSheet sheetInvested) throws Exception{
		Iterator<Row> iter = sheetInvested.iterator();
		while(iter.hasNext()){
			Row row = iter.next();
			if(row.getRowNum() == 0){
				row = iter.next();
			}
			getTransactionDetails(familyMap, row);
		}
	}
	
	private static void createAllFundSheet(XSSFWorkbook workOut, List<AllFundSummary> summaryList, List<AllFundSummary> summaryListShifi){
		XSSFSheet sheet = workOut.createSheet("AllMine");
		writeToAllSheet(sheet, summaryList);
		
		sheet = workOut.createSheet("AllShifi");
		writeToAllSheet(sheet, summaryListShifi);
	}
	
	private static void writeToAllSheet(XSSFSheet sheet, List<AllFundSummary> summaryList){
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
		Cell cell = row.createCell(2);
		cell.setCellValue(netRedeemedAmt.doubleValue());
		cell = row.createCell(4);
		cell.setCellValue(netInvestment.doubleValue());
		cell = row.createCell(7);
		cell.setCellValue(netCurrentValue.doubleValue());
		cell = row.createCell(8);
		cell.setCellValue(netUnbookedProfit.doubleValue());
		cell = row.createCell(9);
		cell.setCellValue(netBookedProfit.doubleValue());
		cell = row.createCell(10);
		cell.setCellValue(netLastValue.doubleValue());
		
		BigDecimal change = netCurrentValue.subtract(netLastValue);
		String str = null;
		if(change.compareTo(ZERO_VAL) < 0){
			str = "Difference - Loss "+change.abs().toPlainString();
		}else{
			str = "Difference - Profit "+change.abs().toPlainString();
		}
		cell = row.createCell(11);
		cell.setCellValue(str);
		row = sheet.createRow(rowNum++);
		cell = row.createCell(8);
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cell.setCellStyle(cellStyle);
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,8,9));
		cell.setCellValue(netBookedProfit.add(netUnbookedProfit).doubleValue());
	}
	
	private static void createSummaryRow(AllFundSummary summary, Row row){
		int cellNum = 0;
		Cell cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getFundName());
		cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getActualInvestment().doubleValue());
		cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getRedeemedAmt().doubleValue());
		cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getUnitsHolding().doubleValue());
		cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getInvestmentCost().doubleValue());
		cell = row.createCell(cellNum++);
		if(null != summary.getAvgCost()){
			cell.setCellValue(summary.getAvgCost().doubleValue());
		}
		cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getCurrentNAV().doubleValue());
		cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getCurrentVal().doubleValue());
		cell = row.createCell(cellNum++);
		if(null != summary.getUnbookedProfit()){
			cell.setCellValue(summary.getUnbookedProfit().doubleValue());
		}
		cell = row.createCell(cellNum++);
		cell.setCellValue(summary.getBookedProfit().doubleValue());
		cell = row.createCell(cellNum++);
		if(null != summary.getLastVal()){
			cell.setCellValue(summary.getLastVal().doubleValue());
		}
		cell = row.createCell(cellNum++);
		if(null != summary.getLastVal() && null != summary.getCurrentVal()){
			cell.setCellValue(summary.getCurrentVal().subtract(summary.getLastVal()).doubleValue());
		}
	}
	
	private static Cell getStyledHeaderCell(int cellNum, Row row, CellStyle cellStyle){
		Cell cell = row.createCell(cellNum);
		cell.setCellStyle(cellStyle);
		return cell;
	}
	
	private static XSSFCellStyle getHeaderRowStyle(Row row){
		XSSFCellStyle cellStyle = (XSSFCellStyle)row.getSheet().getWorkbook().createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		//cellStyle.setFillForegroundColor(new XSSFColor(new Color(155, 194, 230)));
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(115, 115, 255)));
		return cellStyle;
	}
	
	private static void createHeaderAll(Row row){
		CellStyle cellStyle = getHeaderRowStyle(row);
		int cellNum = 0;
		
		Cell cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Fund Name");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Actual Investment");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Redeemed Amount");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Units Holding");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Investment Cost");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Average Cost");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Current NAV");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Current Value");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Unbooked Profit");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Booked Profit");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Last Net Value");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Difference From Last");
	}
	
	private static void writeOutput(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, Map<FundFamily, AMCSummary> amcSummary, 
			String name, List<AllFundSummary> summaryList, List<AllFundSummary> summaryListShifi) throws Exception{
		XSSFWorkbook workOut = new XSSFWorkbook();
		CellStyle cellStyle = workOut.createCellStyle();
		CreationHelper createHelper = workOut.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mmm-yy"));
		if(summaryList != null && summaryListShifi != null){
			createAllFundSheet(workOut, summaryList, summaryListShifi);
		}
		for(FundFamily family : familyMap.keySet()){
			int rowNum = 0;
			XSSFSheet sheet = workOut.createSheet(family.getCode());
			Map<FundDetails, List<Transaction>> map = familyMap.get(family);
			Row row = sheet.createRow(rowNum++);
			createHeader(row);
			for(FundDetails fund : map.keySet()){
				List<Transaction> trans = map.get(fund);
				Collections.sort(trans);
				for(Transaction tr: trans){
					row = sheet.createRow(rowNum++);
					createTransactionRow(cellStyle, row, fund, tr);
				}
			}
			rowNum+=2;
			createAmcSummary(amcSummary.get(family), rowNum, sheet);
		}
		FileOutputStream fileOut = new FileOutputStream("c:\\myreads\\myanalysis_"+name+".xlsx");
		workOut.write(fileOut);
		fileOut.close();
		workOut.close();
	}
	
	private static void createAmcSummary(AMCSummary amcSummary, int rowNum, XSSFSheet sheet){
		Row row = sheet.createRow(rowNum++);
		int cellNum = 0;
		Cell cell = row.createCell(cellNum++);
		cell.setCellValue("Current Cost");
		cell = row.createCell(cellNum++);
		cell.setCellValue(amcSummary.getCurrentInvestment().doubleValue());
		
		row = sheet.createRow(rowNum++);
		cellNum = 0;
		cell = row.createCell(cellNum++);
		cell.setCellValue("Current Value");
		cell = row.createCell(cellNum++);
		cell.setCellValue(amcSummary.getCurrentValue().doubleValue());
		
		row = sheet.createRow(rowNum++);
		cellNum = 0;
		cell = row.createCell(cellNum++);
		cell.setCellValue("Profit");
		cell = row.createCell(cellNum++);
		cell.setCellValue(amcSummary.getCurrentValue().subtract(amcSummary.getCurrentInvestment()).doubleValue());
	}
	
	private static void createTransactionRow(CellStyle cellStyle, Row row, FundDetails fund, Transaction transaction){
		int cellNum = 0;
		Cell cell = row.createCell(cellNum++);
		cell.setCellValue(fund.getFundName());
		cell = row.createCell(cellNum++);
		cell.setCellValue(fund.getFundType().getName());
		cell = row.createCell(cellNum++);
		cell.setCellValue(transaction.getTransactionType().getName());
		cell = row.createCell(cellNum++);
		cell.setCellValue(transaction.getDate());
		cell.setCellStyle(cellStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue(transaction.getAmount().doubleValue());
		cell = row.createCell(cellNum++);
		cell.setCellValue(transaction.getNav().doubleValue());
		cell = row.createCell(cellNum++);
		cell.setCellValue(transaction.getUnits().doubleValue());
		cell = row.createCell(cellNum++);
		if(transaction.getUnitsStillHolding() != null){
			cell.setCellValue(transaction.getUnitsStillHolding().doubleValue());
		}
		cell = row.createCell(cellNum++);
		if(transaction.getInvestmentCost() != null){
			cell.setCellValue(transaction.getInvestmentCost().doubleValue());
		}
		cell = row.createCell(cellNum++);
		if(transaction.getCurrrentVal() != null){
			cell.setCellValue(transaction.getCurrrentVal().doubleValue());
		}
		cell = row.createCell(cellNum++);
		if(transaction.getRedeemedUnits_1() != null){
			cell.setCellValue(transaction.getRedeemedUnits_1().doubleValue());
		}
		cell = row.createCell(cellNum++);
		if(transaction.getRedemptionDate_1() != null){
			cell.setCellValue(transaction.getRedemptionDate_1());
			cell.setCellStyle(cellStyle);
		}
		cell = row.createCell(cellNum++);
		if(transaction.getRedeemedUnits_2() != null){
			cell.setCellValue(transaction.getRedeemedUnits_2().doubleValue());
		}
		cell = row.createCell(cellNum++);
		if(transaction.getRedemptionDate_2() != null){
			cell.setCellValue(transaction.getRedemptionDate_2());
			cell.setCellStyle(cellStyle);
		}
		cell = row.createCell(cellNum++);
		if(null != transaction.getCurrentStatus()){
			cell.setCellValue(transaction.getCurrentStatus().doubleValue());
		}
		cell = row.createCell(cellNum++);
		if(null != transaction.getReturnPerc()){
			cell.setCellValue(transaction.getReturnPerc().doubleValue());
		}
	}
	
	private static void createHeader(Row row){
		CellStyle cellStyle = getHeaderRowStyle(row);
		int cellNum = 0;
		Cell cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Fund Name");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Fund Type");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Transaction Type");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Date");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Amount");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("NAV");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Units");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Units Still Holding");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Investment Cost");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Current Value");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Redeemed Unit 1");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Redemption Date 1");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Redeemed Unit 2");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Redemption Date 2");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Current Status");
		cell = getStyledHeaderCell(cellNum++, row, cellStyle);
		cell.setCellValue("Return %");
	}
	
	private static void setFundType(String fundType, FundDetails fund) throws Exception{
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
		default:
			throw new Exception("Unknown Fund Type");
		}
	}
	
	private static void setTransactionType(String type, Transaction trans) throws Exception{
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
	
	private static FundFamily getFundFamily(String fundFamily) throws Exception{
		switch (fundFamily) {
		case "ICICI Prudential":
			return FundFamily.ICICI;
		case "Birla Sun Life":
			return FundFamily.Birla;
		case "DSP Blackrock":
			return FundFamily.DSP;
		case "HDFC":
			return FundFamily.HDFC;
		case "Kotak Mahindra":
			return FundFamily.Kotak;
		case "Mirae Asset":
			return FundFamily.Mirae;
		default:
			throw new Exception("unknown Family");
		}
	}
	
	private static void getTransactionDetails(Map<FundFamily, Map<FundDetails, List<Transaction>>> familyMap, Row row) throws Exception{
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
