package org.finance.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.finance.service.impl.ProjectionServiceImpl;

public class Projection {

	public static void main(String[] args) {
		// args 1. interest array 2. balance 3. starting month
		//check args
		if (args.length != 8) {
			System.out.println("Required args are: ");
			System.out.println("1. comma delimited interest rates");
			System.out.println("2. balance (no commas)");
			System.out.println("3. starting month (January is 1)");
			System.out.println("4. starting year");
			System.out.println("5. how long to project out (in years)");
			System.out.println("6. file location of monthly items");
			System.out.println("7. file location of every other Friday pay dates");
			System.out.println("8. file location of projection output (use .csv extension)");
			System.exit(1);
		}
		
		//1. interest array
		List<Double> interestRates = new ArrayList<Double>();
		StringTokenizer st = new StringTokenizer(args[0], ",");
		while (st.hasMoreTokens()) {
			interestRates.add(new Double(st.nextToken()));
		}
		
		//2. balance
		Double balance = new Double(args[1]);
		
		//3. starting month
		int month = new Integer(args[2]).intValue();
		
		//4. starting year
		int year = new Integer(args[3]).intValue();
		
		//5. how long to project out (in years)
		int projectionLength = new Integer(args[4]).intValue();
		
		//6. file location of monthly income/expenses
		File basicFile = new File(args[5]);
		
		//7. file location of every other Friday pay dates
		File mooIncomeFile = new File(args[6]);
		
		//8. file location of projection output (use .csv extension)
		File outputFile = new File(args[7]);
		
		ProjectionService ps = new ProjectionServiceImpl();
		try {
			ps.performHelocProjection(interestRates, balance, month, year, projectionLength, basicFile, mooIncomeFile, outputFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
