package org.finance.util;

import java.math.BigDecimal;

/**
 * Performs addition, subtraction, multiplication, division of currency or money.
 * 
 * <p>Using <code>double</code> values to perform mathematical calculations 
 * can result in unexpected decimal results, i.e. 4.2399999999999.  
 * The <code>BigDecimal</code> class allows users to set the precision of the numbers
 * being calculated and the rounding mode for the operands as well as the results.  
 * With this, we can accurately add, subtract, multiply, and divide currency.
 * 
 * @author cahana
 */
public class FinancialCalculations {
    
//  private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN; 
  private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
  private static final int ROUNDING_PRECISION = 3;
  private static final int CURRENCY_PRECISION = 2;
  
  /**
   * Add two values using BigDecimal addition.
   * 
   * @param operand1
   * @param operand2
   * @return operand1 + operand2
   * @exception NumberFormatException
   */
  public static double add(double operand1, double operand2) {
      
    BigDecimal xValue = new BigDecimal(operand1);
    xValue = xValue.setScale(ROUNDING_PRECISION, ROUNDING_MODE);
    
    BigDecimal yValue = new BigDecimal(operand2);
    yValue = yValue.setScale(ROUNDING_PRECISION, ROUNDING_MODE);
    
    BigDecimal zValue = xValue.add(yValue);
    zValue = zValue.setScale(CURRENCY_PRECISION, ROUNDING_MODE);
    
    return zValue.doubleValue();
  }
  
  /**
   * Subtract two values using BigDecimal subtraction.
   * 
   * @param operand1
   * @param operand2
   * @return operand1 - operand2
   * @exception NumberFormatException
   */
  public static double subtract(double operand1, double operand2) {
      
    BigDecimal xValue = new BigDecimal(operand1);
    xValue = xValue.setScale(ROUNDING_PRECISION, ROUNDING_MODE);
    
    BigDecimal yValue = new BigDecimal(operand2);
    yValue = yValue.setScale(ROUNDING_PRECISION, ROUNDING_MODE);
    
    BigDecimal zValue = xValue.subtract(yValue);
    zValue = zValue.setScale(CURRENCY_PRECISION, ROUNDING_MODE);
    
    return zValue.doubleValue();
  }
  
  /**
   * Add two values using BigDecimal addition.
   * <p>Method handles nulls by converting them to 0.00.
   * 
   * @param operand1
   * @param operand2
   * @return operand1 + operand2
   * @exception NumberFormatException
   */
  public static double add(String operand1, String operand2) {
      
    operand1 = Formatter.removeComma(Formatter.formatWhitespace(operand1, "0.00"));
    operand2 = Formatter.removeComma(Formatter.formatWhitespace(operand2, "0.00"));
    
    return add(Double.parseDouble(operand1), Double.parseDouble(operand2));
  }
  
  /**
   * Subtract two values using BigDecimal subtraction.
   * <p>Method handles nulls by converting them to 0.00.
   * 
   * @param operand1
   * @param operand2
   * @return operand1 - operand2
   * @exception NumberFormatException
   */
  public static double subtract(String operand1, String operand2) {
      
    operand1 = Formatter.removeComma(Formatter.formatWhitespace(operand1, "0.00"));
    operand2 = Formatter.removeComma(Formatter.formatWhitespace(operand2, "0.00"));
    
    return subtract(Double.parseDouble(operand1), Double.parseDouble(operand2));
  }
  
  /**
   * Performs division using BigDecimal division.
   * 
   * @param operand1
   * @param operand2
   * @param roundingPrecision the decimal place at which to use as the rounding factor
   * @param resultPrecision the number of decimal places to display for the final result 
   * @return operand2 / operand1
   * @exception NumberFormatException
   */
  public static double divide(double operand1, double operand2, 
    int roundingPrecision, int resultPrecision) {
    
    if (operand1==0D || operand2==0D) {
      return 0;
    }
    
    BigDecimal xValue = new BigDecimal(operand1);
    xValue = xValue.setScale(roundingPrecision, ROUNDING_MODE);
    
    BigDecimal zValue = new BigDecimal(operand2);
    zValue = zValue.setScale(roundingPrecision, ROUNDING_MODE);
    
    BigDecimal yValue = zValue.divide(xValue, ROUNDING_MODE);
    yValue = yValue.setScale(resultPrecision, ROUNDING_MODE);
    
    return yValue.doubleValue();
  }
  
  /**
   * Performs division using BigDecimal division.
   * <p>Method handles nulls by converting them to 0.00.  The default rounding and result precision
   * are defined as member variables of this class.
   * 
   * @param operand1
   * @param operand2
   * @param roundingPrecision the decimal place at which to use as the rounding factor
   * @param resultPrecision the number of decimal places to display for the final result
   * @return operand2 / operand1
   * @exception NumberFormatException
   */
  public static double divide(String operand1, String operand2, 
      int roundingPrecision, int resultPrecision) {
    
    operand1 = Formatter.removeComma(Formatter.formatWhitespace(operand1, "0.00"));
    operand2 = Formatter.removeComma(Formatter.formatWhitespace(operand2, "0.00"));
    
    return divide(Double.parseDouble(operand1), Double.parseDouble(operand2), 
        roundingPrecision, resultPrecision);
  }
  
  /**
   * Performs division using BigDecimal division.
   * <p>Method handles nulls by converting them to 0.00.  Uses the default rounding and 
   * result precision, which are defined as member variables of this class.
   * 
   * @param operand1
   * @param operand2
   * @return operand2 / operand1
   * @exception NumberFormatException
   */
  public static double divide(String operand1, String operand2) {
    
    operand1 = Formatter.removeComma(Formatter.formatWhitespace(operand1, "0.00"));
    operand2 = Formatter.removeComma(Formatter.formatWhitespace(operand2, "0.00"));
    
    return divide(Double.parseDouble(operand1), Double.parseDouble(operand2), 
        ROUNDING_PRECISION, CURRENCY_PRECISION);
  }
  
  /**
   * Performs division using BigDecimal division.
   * <p>Uses the default rounding and result precision, 
   * which are defined as member variables of this class.
   * 
   * @param operand1
   * @param operand2
   * @return operand2 / operand1
   * @exception NumberFormatException
   */
  public static double divide(double operand1, double operand2) {
    
    return divide(operand1, operand2, ROUNDING_PRECISION, CURRENCY_PRECISION);
  }
  
  /**
   * Multiply using BigDecimal multiplication.
   * <p>Method handles nulls by converting them to 0.00.
   * 
   * @param operand1
   * @param operand2
   * @return operand1 * operand2
   * @exception NumberFormatException
   */
  public static double multiply(String operand1, String operand2) {
    
    operand1 = Formatter.removeComma(Formatter.formatWhitespace(operand1, "0.00"));
    operand2 = Formatter.removeComma(Formatter.formatWhitespace(operand2, "0.00"));
    
    return multiply(Double.parseDouble(operand1), Double.parseDouble(operand2));
  }
  
  /**
   * Multiply using BigDecimal multiplication.
   * 
   * @param operand1
   * @param operand2
   * @return operand1 * operand2
   * @exception NumberFormatException
   */
  public static double multiply(double operand1, double operand2) {
    
    BigDecimal xValue = new BigDecimal(operand1);
    xValue = xValue.setScale(ROUNDING_PRECISION, ROUNDING_MODE);
    
    BigDecimal yValue = new BigDecimal(operand2);
    yValue = yValue.setScale(ROUNDING_PRECISION, ROUNDING_MODE);
    
    BigDecimal zValue = xValue.multiply(yValue);
    zValue = zValue.setScale(CURRENCY_PRECISION, ROUNDING_MODE);
    
    return zValue.doubleValue();
  }
  
  public static int wholeNumberRounder(String operand) {
      operand = Formatter.removeComma(Formatter.formatWhitespace(operand, "0.00"));
      
      return wholeNumberRounder(Double.parseDouble(operand));   
  }
  
  public static int wholeNumberRounder(double operand) {
      BigDecimal bd = new BigDecimal(operand);
      bd = bd.setScale(0, ROUNDING_MODE);
      return bd.intValue();   
  }
}
