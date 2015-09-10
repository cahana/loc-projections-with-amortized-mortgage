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
		if (args.length != 7) {
			System.out.println("Required args are: ");
			System.out.println("1. comma delimited interest rates");
			System.out.println("2. balance (no commas)");
			System.out.println("3. starting month (January is 1)");
			System.out.println("4. starting year");
			System.out.println("5. file location of monthly items");
			System.out.println("6. file location of marcie's pay dates");
			System.out.println("7. file location of projection output");
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
		
		//4. starting month
		int year = new Integer(args[3]).intValue();
		
		//5. file location of monthly items
		File basicFile = new File(args[4]);
		
		//6. file location of marcie's pay dates
		File mooIncomeFile = new File(args[5]);
		
		//6. file location of marcie's pay dates
		File outputFile = new File(args[6]);
		
		ProjectionService ps = new ProjectionServiceImpl();
		try {
		ps.performHelocProjection(interestRates, balance, month, year, basicFile, mooIncomeFile, outputFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
