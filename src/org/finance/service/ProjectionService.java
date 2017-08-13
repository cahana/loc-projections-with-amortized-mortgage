package org.finance.service;

import java.io.File;
import java.util.List;

public interface ProjectionService {

	public void performHelocProjection(List<Double> interestRates, Double balance, int startingMonth, int startingYear, int projectionLength, File basic, File mooIncome, File outputFile)
	throws Exception;
	
}
