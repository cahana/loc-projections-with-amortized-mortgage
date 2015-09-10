package org.finance.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.finance.domain.BasicItem;
import org.finance.domain.LineItem;
import org.finance.service.ProjectionService;
import org.finance.util.FinancialCalculations;
import org.finance.util.Formatter;
import org.finance.util.GeneralUtil;
import org.finance.util.Type;

public class ProjectionServiceImpl implements ProjectionService {

	private static final String CSV_DELIMITER = ",";
	
	@Override
	public void performHelocProjection(List<Double> interestRates, Double balance, int startingMonth, int startingYear, File basic, File mooIncome, File outputFile) 
	throws Exception {
		System.out.println("crunching numbers");
		
		// read in fixed monthly items
		String record = "";
		List<BasicItem> basicList = new ArrayList<BasicItem>();
		BufferedReader br = new BufferedReader(new FileReader(basic));
		while ((record = br.readLine()) != null) {
            record = StringUtils.trim(record);
            basicList.add(buildBasicItem(record));
        }
		br.close();
		
		// read in marcie's income dates
		List<LineItem> mooIncomeList = new ArrayList<LineItem>();
		br = new BufferedReader(new FileReader(mooIncome));
		while ((record = br.readLine()) != null) {
            record = StringUtils.trim(record);
            mooIncomeList.add(buildMooIncomeLineItem(record));
        }
		br.close();
		
		// print out the results
//		for (BasicItem basicItem : basicList) {
//			System.out.println(basicItem.toString());
//		}
//		
//		for (LineItem lineItem : mooIncomeList) {
//			System.out.println(lineItem.toString());
//		}
		
		List<LineItem> items = new ArrayList<LineItem>();
		// what's the outter loop conditional?? balance? or 20 years?
		int control = startingYear + 21;
		int tempStartingYear = startingYear;
		int tempStartingMonth = startingMonth;
		while (tempStartingYear < control) {
			// loop through the months beginning with the starting month
			for (int i = tempStartingMonth-1; i < 12; i++) {
				// loop through the basicItems and create LineItems
				for (BasicItem basicItem : basicList) {
					items.add(buildLineItem(basicItem, i+1, tempStartingYear));
				}
			}
			// reset month to January and increment the year
			tempStartingMonth = 1;
			tempStartingYear++;
		}
		
//		for (LineItem lineItem : items) {
//			System.out.println(lineItem.toString());
//		}
		
		// combine the items and the mooIncome list
		List<LineItem> finalList = GeneralUtil.combineLists(items, mooIncomeList);
		Collections.sort(finalList, new Comparator() {
            public int compare(Object o1, Object o2)
            {
                Date date1 = ((LineItem) o1).getDate();
                Date date2 = ((LineItem) o2).getDate();
                
                return date1.compareTo(date2);
            }
        });
		
		// how do we know what interest rates to use if there are promotional rates
		int controlMonth = startingMonth;
		int controlYear = startingYear;
		// key will be the effective date of the rate
		TreeMap<Date,Double> interestMap = new TreeMap<Date,Double>();
		for (Double rate : interestRates) {
			interestMap.put(Formatter.convertStringToDate(controlMonth+"/1/"+controlYear++), rate);
		}
					
		// take the final list and calculate the numbers
		Double previousRunningAccruedInterest = 0D;
		int daysBetween = 0;
		LineItem currentItem = null;
		LineItem nextItem = null;
		for (int i = 0; i < finalList.size(); i++) {
			currentItem = finalList.get(i);
			
			// find out balance
			if (currentItem.getType() == Type.Expense) {
				currentItem.setBalance(FinancialCalculations.add(balance, currentItem.getAmount()));
			}
			else {
				currentItem.setBalance(FinancialCalculations.subtract(balance, currentItem.getAmount()));
			}
			balance = currentItem.getBalance();
			
			// find out accrued dailiy interest for this line item
			if (i < finalList.size()-1) {
				nextItem = finalList.get(i+1);
			}
			else {
				nextItem = null;
			}
			
			
			
			
			// last item in array
			if (nextItem == null) {
				currentItem.setAccruedInterest(GeneralUtil.calculateAccruedInterest(0, balance, interestMap.floorEntry(currentItem.getDate()).getValue()));
			}
			else {
				// set accrued interest to 0 if this date is the same as the next date
				daysBetween = GeneralUtil.calculateDaysBetween(currentItem.getDate(), nextItem.getDate());
				if (daysBetween == 0) {
					currentItem.setAccruedInterest(0D);
				}
				else {
					currentItem.setAccruedInterest(GeneralUtil.calculateAccruedInterest(daysBetween, balance, interestMap.floorEntry(currentItem.getDate()).getValue()));
				}
			}
			
			// calculate running interest total and link to the line item
			if (i == 0) {
				currentItem.setRunningAccruedInterest(currentItem.getAccruedInterest());
			}
			else {
				currentItem.setRunningAccruedInterest(currentItem.getAccruedInterest()+previousRunningAccruedInterest);
			}
			
			// need this to keep a running total of the interest total
			previousRunningAccruedInterest = currentItem.getRunningAccruedInterest();
			
			// check balance
			if (balance < 0) {
				break;
			}
		}
		
		// print out "partial" line item results to a file
		PrintWriter pw = new PrintWriter(outputFile);
		for (LineItem lineItem : finalList) {
			//pw.println(lineItem.toString());
			pw.println(convertToCsv(lineItem));
		}
		pw.close();
	}
	
	private String convertToCsv(LineItem lineItem) {
		StringBuilder sb = new StringBuilder();
		sb.append(Formatter.convertDateToString(lineItem.getDate(), Formatter.DISPLAY2)).append(CSV_DELIMITER);
		sb.append(lineItem.getDescription()).append(CSV_DELIMITER);
		sb.append(lineItem.getType().name()).append(CSV_DELIMITER);
		sb.append(lineItem.getAmount()).append(CSV_DELIMITER);
		sb.append(lineItem.getBalance()).append(CSV_DELIMITER);
		sb.append(lineItem.getAccruedInterest()).append(CSV_DELIMITER);
		sb.append(lineItem.getRunningAccruedInterest());
		return sb.toString();
	}
	
	private BasicItem buildBasicItem(String record) {
		BasicItem item = new BasicItem();
		StringTokenizer st = new StringTokenizer(record, ",");
		item.setDayOfMonth(new Integer(st.nextToken()));
		item.setDescription(st.nextToken());
		item.setType(Type.valueOf(st.nextToken()));
		item.setAmount(new Double(st.nextToken()));
		
		return item;
		
	}

	private LineItem buildMooIncomeLineItem(String record) {
		LineItem item = new LineItem();
		StringTokenizer st = new StringTokenizer(record, ",");
		item.setDate(Formatter.convertStringToDate(st.nextToken()));
		item.setDescription("paycheck");
		item.setType(Type.Income);
		item.setAmount(new Double(st.nextToken()));
		
		return item;
		
	}
	
	private LineItem buildLineItem(BasicItem basicItem, int month, int year) {
		LineItem item = new LineItem();

		item.setDate(Formatter.convertStringToDate(month+"/"+basicItem.getDayOfMonth()+"/"+year));
		item.setDescription(basicItem.getDescription());
		item.setType(basicItem.getType());
		item.setAmount(basicItem.getAmount());
		
		return item;
		
	}
}
