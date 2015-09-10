package org.finance.util;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class Formatter {
	// set up logging
	private static SimpleDateFormat oracleFormat = new SimpleDateFormat(
			"dd-MMM-yyyy");
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"MM/dd/yyyy h:mm a");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");
	public static final String DATEDELIMITERS = "-/. ";
	public static final String DECIMAL = ".";
	public static final String ORACLE = "oracle";
	public static final String DISPLAY = "display";
	public static final String DISPLAY2 = "display2";
	public static final String SQL = "sql";
	static final String MONTH_NAMES[] = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	static final int QUARTERS[] = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
	static final int FISCAL_QUARTERS[] = { 3, 3, 3, 4, 4, 4, 1, 1, 1, 2, 2, 2 };
	public static final String LINE_BREAK = "\r\n";
	public static final String EXCEL_LINE_BREAK = "\n";
	public static final String ESCAPED_LINE_BREAK = "\\r\\n";
	public static final String HTML_BREAK = "<br>";

	/**
	 * default constructor
	 */
	public Formatter() {
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to return rep String if field is a whitespace
	 */
	public static String formatWhitespace(String inputStr) {
		return formatWhitespace(inputStr, "");
	} // formatWhitespace()

	// -------------------------------------------------------------------------------------
	/**
	 * Function to return rep String if field is a whitespace
	 */
	public static String formatWhitespace(String inputStr, String replacementStr) {
		// handle null parameters
		if (replacementStr == null) {
			replacementStr = "";
		}

		if (inputStr == null) {
			return replacementStr;
		} else if ((inputStr.trim().length() == 0)
				|| (inputStr.trim().equalsIgnoreCase("null"))) {
			return replacementStr;
		} else {
			return inputStr.trim();
		}
	} // formatWhitespace()

	// -------------------------------------------------------------------------------------
	public static String replaceString(String inputStr, String oldStr,
			String newStr) {
		if ((inputStr == null) || (inputStr.length() == 0) || (oldStr == null)
				|| (newStr == null)) {
			return "";
		}

		StringBuffer outputBuff = new StringBuffer(inputStr);
		String currStr = inputStr;
		int nextIdx = currStr.indexOf(oldStr);

		while (nextIdx != -1) {
			outputBuff.replace(nextIdx, nextIdx + oldStr.length(), newStr);

			// This is not necessary in JDK 1.4 because StringBuffer has
			// indexOf() method
			currStr = outputBuff.toString();

			// Find the matching oldStr starting from the end of last
			// replacement
			nextIdx = currStr.indexOf(oldStr, nextIdx + newStr.length());
		}

		return outputBuff.toString();
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Takes a comma-delimited string and wraps each element in single quotes.
	 * 
	 * @param line
	 *            the <code>String</code> line to parse.
	 * @return newLine the single quote formatted comma-delimited
	 *         <code>String</code>.
	 */
	public String delimitSingleQuotes(String line) {
		StringBuffer newLine = new StringBuffer();

		if (line != null) {
			StringTokenizer st = new StringTokenizer(line, ", '");
			String token = "";
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				newLine.append("'").append(token).append("'");
				if (st.hasMoreTokens()) {
					newLine.append(",");
				}
			}
		}
		return newLine.toString();
	}// end: delimitSingleQuotes

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a string into a currency format
	 */
	public static String formatDbInteger(String num) {
		int value = Integer.parseInt(Formatter.formatWhitespace(num, "0"));
		if (value == 0)
			return "";
		else
			return Integer.toString(value);
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a string into a currency format
	 */
	public static String formatDbDouble(String dblNum) {
		double dblValue = Double.parseDouble(Formatter.formatWhitespace(dblNum,
				"0.0"));
		if (dblValue == 0)
			return "";

		dblNum = Double.toString(dblValue);
		int decimalPt = dblNum.indexOf(".");
		if (decimalPt != -1) {
			String intNum = dblNum.substring(0, decimalPt);
			int intValue = Integer.parseInt(intNum);
			if (intValue == dblValue) {
				return intNum;
			}
		}

		return dblNum;
	}

	/**
	 * reverses the last and first name
	 * 
	 * ex. Doe, John A becomes John A Doe
	 * 
	 * @param name
	 * @return String
	 */
	public static String convertToFirstLastName(String name) {
		int commaPos = name.indexOf(",");

		String newName = "";
		if (commaPos == -1) {
			newName = name;
		} else {
			int suffixIndex = getSuffixIndex(name);
			if (suffixIndex == 0) {
				newName = name.substring(commaPos + 1).trim() + " "
						+ name.substring(0, commaPos).trim();
			} else {
				newName = name.substring(commaPos + 1, suffixIndex).trim()
						+ " " + name.substring(0, commaPos).trim() + " "
						+ name.substring(suffixIndex);
			}
		}

		return newName;
	}

	/**
	 * Converts the parameter to a display "Yes" or "No" value.
	 * 
	 * 
	 * @param name
	 * @return String
	 */
	public static String convertToYesNo(String value) {
		if (StringUtils.isBlank(value)) {
			// return default value
			return "No";
		} else {
			if (value.equalsIgnoreCase("Y")) {
				return "Yes";
			} else if (value.equalsIgnoreCase("N")) {
				return "No";
			} else if (value.equalsIgnoreCase("on")) {
				return "Yes";
			} else if (value.equalsIgnoreCase("off")) {
				return "No";
			} else if (value.equals("1")) {
				return "Yes";
			} else if (value.equals("0")) {
				return "No";
			} else {
				return "No";
			}
		}
	}

	/**
	 * finds the index of the name suffix assuming the name is "lastname,
	 * firstname suffix"
	 * 
	 * @param name
	 * @return String
	 */
	public static int getSuffixIndex(String name) {

		if (name.endsWith("II")) {
			return name.lastIndexOf("II");
		} else if (name.endsWith("III")) {
			return name.lastIndexOf("III");
		} else if (name.endsWith("IV")) {
			return name.lastIndexOf("IV");
		} else {
			return 0;
		}
	}

	// -------------------------------------------------------------------------------------
	/**
	 * formats a <code>String</code> name with capitalized first letter.
	 * 
	 * ex. SCOTT ogasawara --> Scott Ogasawara
	 * 
	 * @param name
	 *            A unformatted name.
	 * @return newName A formatted name.
	 */
	public static String formatName(String name) {

		// ignore nulls
		if ("".equals(formatWhitespace(name))) {
			return "";
		}

		StringTokenizer st = new StringTokenizer(name.trim(), " ");
		StringBuffer newName = new StringBuffer();
		String temp = "";
		String restOfName = "";
		while (st.hasMoreTokens()) {
			temp = st.nextToken().toLowerCase();

			char firstLetter = temp.charAt(0);
			restOfName = temp.substring(1);

			newName.append(Character.toString(firstLetter).toUpperCase()
					+ restOfName);
			if (st.hasMoreTokens()) {
				newName.append(" ");
			}
		}

		return checkHyphen(newName.toString());
	}// end: formatName

	/**
	 * Capitalizes the letter following each hyphen in a string.
	 * 
	 * @param name
	 *            A name with possible hyphens.
	 * @return name The name with proper capitalizations.
	 */
	private static String checkHyphen(String name) {
		int index = name.indexOf("-");
		String head = "";
		String tail = "";

		while (index != -1) {
			head = name.substring(0, index + 1);
			char letter = name.charAt(index + 1);
			tail = name.substring(index + 2);
			name = head + Character.toString(letter).toUpperCase() + tail;
			index = tail.indexOf("-"); // locate next hyphen
		}
		return name;
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a string into a comma delimited number format.
	 */
	public static String formatNumber(String tData) {

		return formatNumber(formatWhitespace(tData, "0.00"), 2, 2);
	}

	/**
	 * Function to format a string into a comma delimited number format.
	 */
	public static String formatNumber(String tData, int minDeciPlaces,
			int maxDeciPlaces) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		NumberFormat numFormatter = NumberFormat.getNumberInstance();
		numFormatter.setMaximumFractionDigits(maxDeciPlaces);
		numFormatter.setMinimumFractionDigits(minDeciPlaces);

		return numFormatter.format(Double.parseDouble(formatWhitespace(tData,
				"0.00")));
	}

	/**
	 * Function to format a string into a non grouping number format.
	 */
	public static String formatNumberWithoutGrouping(String tData) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		NumberFormat numFormatter = NumberFormat.getNumberInstance();
		numFormatter.setGroupingUsed(false);
		numFormatter.setMaximumFractionDigits(2);
		numFormatter.setMinimumFractionDigits(2);

		return numFormatter.format(Double.parseDouble(formatWhitespace(tData,
				"0.00")));
	}

	/**
	 * Function to format a string into a comma delimited number format.
	 */
	public static String formatNumber(String tData, int minIntPlaces,
			int maxIntPlaces, int minDeciPlaces, int maxDeciPlaces,
			boolean isGrouping) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		NumberFormat numFormatter = NumberFormat.getNumberInstance();
		numFormatter.setMaximumIntegerDigits(maxIntPlaces);
		numFormatter.setMinimumIntegerDigits(minIntPlaces);
		numFormatter.setMaximumFractionDigits(maxDeciPlaces);
		numFormatter.setMinimumFractionDigits(minDeciPlaces);
		numFormatter.setGroupingUsed(isGrouping);

		return numFormatter.format(Double.parseDouble(formatWhitespace(tData,
				"0.00")));
	}

	/**
	 * Function to format a string into a comma delimited number format.
	 */
	public static String formatInteger(Integer tData) {
		if (tData == null) {
			return "00";
		}

		NumberFormat numFormatter = NumberFormat.getNumberInstance();
		numFormatter.setMaximumIntegerDigits(2);
		numFormatter.setMinimumIntegerDigits(2);

		return numFormatter.format(tData);
	}

	/**
	 * Function to format a string into a comma delimited number format.
	 */
	public static String formatNumber(double tData) {

		return formatNumber(tData, 2, 2);
	}

	/**
	 * Function to format a string into a comma delimited number format.
	 */
	public static String formatNumber(double tData, int minDeciPlaces,
			int maxDeciPlaces) {

		NumberFormat numFormatter = NumberFormat.getNumberInstance();
		numFormatter.setMaximumFractionDigits(maxDeciPlaces);
		numFormatter.setMinimumFractionDigits(minDeciPlaces);

		return numFormatter.format(tData);
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a string into a currency format
	 */
	public static String formatCurrency(String tData) {
		NumberFormat genFormatter = NumberFormat.getCurrencyInstance();
		genFormatter.setMaximumFractionDigits(2);
		genFormatter.setMinimumFractionDigits(2);

		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		} else {
			return genFormatter.format(Double.parseDouble(formatWhitespace(
					tData, "0.00")));
		}
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Method formatMoney prefixes a dollar sign and appends a zero.
	 * 
	 * @param money
	 *            A string dollar value to format
	 * @return value A properly formatted dollar value
	 */
	public static String formatMoneyPlain(String money) {

		if (money == null || money.equals("")) {
			money = "";
		}
		// add the '0' or '00' to the string
		else {
			int len = money.length();
			int index = money.indexOf(".");

			StringBuffer value = new StringBuffer();
			value.append(money);
			if (index == -1) {
				value.append(".00");
			} else if (len - index == 2) {
				value.append("0");
			}
			money = value.toString();
		}

		return money;
	}// end: formatMoney;

	// -------------------------------------------------------------------------------------
	/**
	 * Method formatMoney prefixes a dollar sign and appends a zero.
	 * 
	 * @param money
	 *            A string dollar value to format
	 * @return value A properly formatted dollar value
	 */
	/*
	 * public static String formatMoney(String money) { String methodName =
	 * "formatMoney";
	 * 
	 * if (money == null || money.equals("")) { money = ""; } //strip the '$'
	 * off from the string else if (money.indexOf("$") != -1){ int len =
	 * money.length(); int index = money.indexOf("$")+1; money =
	 * money.substring(index,len); } //add the '$' and '00' to the string else {
	 * //if (money != null && !money.equalsIgnoreCase("")){ //String methodName =
	 * "formatMoney"; StringBuffer value = new StringBuffer();
	 * 
	 * int len = money.length(); int index = money.indexOf(".");
	 * //fmtr.printError(money + " : has length = "+len + " & '.' index = " +
	 * index, methodName, CLASS_NAME);
	 * 
	 * value.append("$"); value.append(money); if(index == -1){
	 * value.append(".00"); } else if(len - index == 2){ value.append("0"); }
	 * money = value.toString(); }
	 * 
	 * return money; }//end: formatMoney;
	 */
	// -------------------------------------------------------------------------------------
	/**
	 * formats a <code>String</code> into money or numeric representations.
	 * 
	 * ex. 1200.1 --> $1,200.10
	 * 
	 * @param money
	 *            A string dollar value to format
	 * @return value A properly formatted dollar value
	 */
	public static String formatMoney(String money) {

		if (money == null || money.equals("")) {
			money = "";
		}
		// strip the '$' and commas from the money value
		else if (money.indexOf("$") != -1) {
			StringTokenizer st = new StringTokenizer(money, "$,");
			StringBuffer value = new StringBuffer();
			while (st.hasMoreTokens()) {
				value.append(st.nextToken());
			}
			money = value.toString();
		}
		// add the '$' and '00' to the string
		else {
			StringBuffer value = new StringBuffer();
			value.append("$");
			value.append(formatNumber(money));
			money = value.toString();
		}

		return money;
	}// end: formatMoney;

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a integer-string with prefixed zeros.
	 */
	public static String formatDocNum(String num) {
		NumberFormat intFormatter = NumberFormat.getNumberInstance();
		intFormatter.setMinimumIntegerDigits(6);
		intFormatter.setParseIntegerOnly(true);
		intFormatter.setGroupingUsed(false);

		if ((num == null) || (num.trim().length() == 0)) {
			return "";
		} else {
			return intFormatter.format(Integer.parseInt(formatWhitespace(num,
					"")));
		}
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a string into a phone number format only processes 10
	 * or 7 length strings return unformated otherwise assumes no COUNTRY code
	 * present
	 */
	public static String formatPhone(String tData) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		String tPhoneNo = stripPhone(tData);

		if (tPhoneNo.length() == 10) {
			return "(" + tPhoneNo.substring(0, 3) + ") "
					+ tPhoneNo.substring(3, 6) + "-"
					+ tPhoneNo.substring(6, 10);
		} else if (tPhoneNo.length() == 7) {
			return tPhoneNo.substring(0, 3) + "-" + tPhoneNo.substring(3, 7);
		} else {
			return tData;
		}
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a string into a UH abreviated phone number format
	 * 
	 * return unformated otherwise assumes no COUNTRY code present
	 */
	public static String formatUhPhone(String tData) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		String tPhoneNo = stripPhone(tData).trim();

		int length = tPhoneNo.length();
		int index = length - 5;
		if (index < 0)
			return tData;

		return tPhoneNo.substring(index, index + 1) + "-"
				+ tPhoneNo.substring(index + 1);

	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to return the area code string of a phone number only processes
	 * 10 length strings assumes no COUNTRY code present
	 */
	public static String getAreaCode(String tData) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		String tPhoneNo = stripPhone(tData);

		if (tPhoneNo.length() == 10) {
			return tPhoneNo.substring(0, 3);
		} else {
			return "";
		}
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to return the NON area code string of a phone number only
	 * processes 10 or 7 length strings assumes no COUNTRY code present
	 */
	public static String removeAreaCode(String tData) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		String tPhoneNo = stripPhone(tData);

		if (tPhoneNo.length() == 10) {
			return tPhoneNo.substring(3, 6) + "-" + tPhoneNo.substring(6, 10);
		} else if (tPhoneNo.length() == 7) {
			return tPhoneNo.substring(0, 3) + "-" + tPhoneNo.substring(3, 7);
		} else {
			return tData;
		}
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to format a string into a phone number format only processes 10
	 * or 7 length strings return unformated otherwise assumes no COUNTRY code
	 * present
	 */
	public static String stripPhone(String tData) {
		if ((tData == null) || (tData.trim().length() == 0)) {
			return "";
		}

		if (tData.indexOf("(") != -1) {
			tData = replaceString(tData, "(", "");
		}

		if (tData.indexOf(")") != -1) {
			tData = replaceString(tData, ")", "");
		}

		if (tData.indexOf("-") != -1) {
			tData = replaceString(tData, "-", "");
		}

		if (tData.indexOf(" ") != -1) {
			tData = replaceString(tData, " ", "");
		}
		return tData;
	}

	// -------------------------------------------------------------------------------------
	/**
	 * Function to parse SSN formatting characters from SSN.
	 * 
	 * @param ssn
	 *            The SSN to parse.
	 * @return ssn The SSN stripped of any formatting characters.
	 */
	public static String stripSSN(String ssn) {
		if ((ssn == null) || (ssn.trim().length() == 0)) {
			return "";
		}

		if (ssn.indexOf("-") != -1) {
			ssn = replaceString(ssn, "-", "");
		}

		if (ssn.indexOf(" ") != -1) {
			ssn = replaceString(ssn, " ", "");
		}
		return ssn;
	}// end: stripSSN

	/**
	 * formats an SSN from ######### to ###-##-####.
	 * 
	 * @param ssn
	 *            The ssn string.
	 * @return ssn Formatted with the "-" inserted/removed.
	 */
	public static String formatSSN(String ssn) {
		if (ssn == null || "".equals(ssn)) {
			return "";
		}

		if (ssn.length() > 5) {
			ssn = ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-"
					+ ssn.substring(5);
		}

		return ssn;
	}// end: formatSSN

	// -------------------------------------------------------------------------------------
	/**
	 * Gets today's date in oracle format (dd-MMM-yyyy)
	 * 
	 * @return Today's date in oracle String format
	 */
	public static String getTodayInOracleFormat() {
		return (oracleFormat.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * Gets today's date in format (MM-dd-yyyy)
	 */
	public static String getToday() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		return (formatter.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * Gets today's year
	 * 
	 * @return Today's year
	 */
	public static int getCurrentYear() {
		// determine today's date
		GregorianCalendar today = new GregorianCalendar();
		return today.get(GregorianCalendar.YEAR);
	}

	/**
	 * Gets today's month
	 * 
	 * @return Today's month
	 */
	public static int getCurrentMonth() {
		// determine today's date
		GregorianCalendar today = new GregorianCalendar();
		return today.get(GregorianCalendar.MONTH);
	}

	/**
	 * Gets the current quarter of the year
	 * 
	 * @return current quarter
	 */
	public static int getCurrentQuarter() {
		return QUARTERS[getCurrentMonth()];
	}

	/**
	 * Gets the current quarter of the year
	 * 
	 * @return current quarter
	 */
	public static int getCurrentFiscalQuarter() {
		return FISCAL_QUARTERS[getCurrentMonth()];
	}

	/**
	 * Gets the current fiscal year
	 * 
	 * Note: Fiscal year starts 07-01- year ends 06-30-(year+1) Thus, the start
	 * of the year is returned.
	 * 
	 * @return Current fiscal year
	 */
	public static int getCurrentFiscalYear() {
		// determine today's date
		GregorianCalendar today = new GregorianCalendar();
		int todayYear = today.get(GregorianCalendar.YEAR);
		int todayMonth = today.get(GregorianCalendar.MONTH);
		int fiscalYear = todayYear;

		// if jan - june
		if (todayMonth >= 0 && todayMonth <= 5) {
			fiscalYear = todayYear - 1;
		}
		// if july - dec
		// else if(todayMonth >= 6 && todayMonth <= 11) {
		// fiscalYear = todayYear;
		// }

		return fiscalYear;
	}

	// =================================================================
	/**
	 * Creates an formatted date
	 * 
	 * @param outType
	 *            Specifies how to format the date.
	 * @return The formatted date string.
	 */
	public static String getToday(String outType) {
		return convertDateToString(new Date(System.currentTimeMillis()),
				outType);
	}

	/**
	 * Creates a timestamp of the current time
	 * 
	 * @return current timestamp
	 */
	public static Timestamp getTimestamp() {
		return (new Timestamp(System.currentTimeMillis()));
	}
	
	/**
     * Creates a timestamp of the current time
     * 
     * @return current timestamp
     */
    public static Timestamp getTimestamp(Date date) {
        return (new Timestamp(date.getTime()));
    }

	/**
	 * Converts a <strong>Date</strong> object into oracle format.
	 * 
	 * @param date
	 *            The
	 */
	public static String convertToOracleFormat(Date date) {
		return (oracleFormat.format(date));
	}

	/**
	 * formats a <code>String</code> date into an Oracle formatted date.
	 * 
	 * ex. 10/31/2002 --> 31-OCT-2002
	 * 
	 * @param date
	 *            A <code>String</code> date.
	 * @return date The Oracle formatted date.
	 */
	public static String convertToOracleFormat(String date) {

		// ignore nulls
		if ("".equals(formatWhitespace(date))) {
			return "";
		}

		StringTokenizer st = new StringTokenizer(date.trim(), "-/. ");
		String month = "";
		String day = "";
		String year = "";

		if (st.hasMoreTokens())
			month = st.nextToken();
		if (st.hasMoreTokens())
			day = st.nextToken();
		if (st.hasMoreTokens())
			year = st.nextToken();

		date = day + "-" + MONTH_NAMES[Integer.parseInt(month) - 1] + "-"
				+ year;

		return date;
	}// end: setOracleDate

	/**
	 * Converts a <strong>Date</strong> as specified by the parameter
	 * discriptor.
	 * 
	 * @param date1
	 *            The <strong>Date</strong> object to be formatted.
	 * @param outType
	 *            The new format to convert to.
	 * @return date1 The converted date as <code>String</code>.
	 */
	public static String convertDateToString(java.util.Date date1,
			String outType) {
		SimpleDateFormat newDate = null;

		if (date1 == null || "".equals(date1.toString()) || outType == null
				|| "".equals(outType)) {
			return "";
		}

		if (outType.equalsIgnoreCase(ORACLE)) {
			newDate = new SimpleDateFormat("dd-MMM-yyyy");
		} else if (outType.equalsIgnoreCase(SQL)) {
			newDate = new SimpleDateFormat("yyyy-MM-dd");
		} else if (outType.equalsIgnoreCase(DISPLAY)) {
			newDate = new SimpleDateFormat("MM-dd-yyyy");
		} else if (outType.equalsIgnoreCase(DISPLAY2)) {
			newDate = new SimpleDateFormat("MM/dd/yyyy");
		}

		if (newDate == null) {
			// default to DISPLAY
			newDate = new SimpleDateFormat("MM-dd-yyyy");
		}

		return (newDate.format(date1));
	}// end: convertDateToString

	/**
	 * 
	 * 
	 */
	public static String convertDateToDisplayString(java.util.Date date) {
		if (date == null)
			return "";
		return convertDateToString(date, DISPLAY);
	}

	/**
	 * Converts a <code>String</code> date as specified by the parameter
	 * discriptor.
	 * 
	 * @param date1
	 *            The <code>String</code> object to be reformatted.
	 * @param inType
	 *            The format currently represented.
	 * @param outType
	 *            The new format to convert to.
	 * @return date1 The converted date as <code>String</code>.
	 */
	public static String convertDate(String date1, String inType, String outType) {
		
		if (StringUtils.isBlank(date1) || StringUtils.isBlank(inType) || StringUtils.isBlank(outType)) {
			return "";
		}

		StringTokenizer st = new StringTokenizer(date1.trim(), DATEDELIMITERS);
		String month = "";
		String day = "";
		String year = "";

		if (inType.equalsIgnoreCase(DISPLAY)) {
			if (st.hasMoreTokens())
				month = st.nextToken();
			if (st.hasMoreTokens())
				day = st.nextToken();
			if (st.hasMoreTokens())
				year = st.nextToken();
		} else if (inType.equalsIgnoreCase(ORACLE)) {
			if (st.hasMoreTokens())
				day = st.nextToken();
			if (st.hasMoreTokens())
				month = Integer.toString(getMonthInt(st.nextToken()));
			if (st.hasMoreTokens())
				year = st.nextToken();
		} else if (inType.equalsIgnoreCase(SQL)) {
			if (st.hasMoreTokens())
				year = st.nextToken();
			if (st.hasMoreTokens())
				month = st.nextToken();
			if (st.hasMoreTokens())
				day = st.nextToken();
		}

		StringBuffer newDate = new StringBuffer();
		if (outType.equalsIgnoreCase(DISPLAY)) {
			newDate.append(month).append("-").append(day).append("-").append(
					year);
		} else if (outType.equalsIgnoreCase(DISPLAY2)) {
			newDate.append(month).append("/").append(day).append("/").append(
					year);
		} else if (outType.equalsIgnoreCase(ORACLE)) {
			newDate.append(day).append("-").append(
					MONTH_NAMES[Integer.parseInt(month) - 1]).append("-")
					.append(year);
		} else if (outType.equalsIgnoreCase(SQL)) {
			newDate.append(year).append("-").append(prefixZeros(month,2)).append("-").append(
					prefixZeros(day,2));
		}

		/*
		 * if(_log.isDebugEnabled()) { _log.debug("--Date to convert-----");
		 * _log.debug("convert from: "+inType+" --> "+outType); _log.debug("
		 * from: "+date1+" --> "+newDate.toString()); }
		 */
		return newDate.toString();
	}// end: convertDate

	/**
	 * formats a <code>String</code> date into a sql <strong>Date</strong>
	 * object.
	 * 
	 * @param date
	 *            a <code>String</code> date.
	 * @param inType
	 *            the current type of format of the string date.
	 * @return date the sql <strong>Date</strong> object.
	 */
	public static java.sql.Date convertToDateObject(String date1, String inType) {
		String newDate = convertDate(date1, inType, SQL);
		if ("".equals(newDate))
			return null;
		return java.sql.Date.valueOf(newDate);
	}// end: convertToDateObject

	public static Date convertStringToDate(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		Date d = null;
		try {
			d = dateFormat.parse(date);
		}
		catch (ParseException ignored) {
			ignored.printStackTrace();
		}
		return d;
	}
	
	/**
	 * Formats a <code>String</code> date in the MM-dd-yyyy format into a sql
	 * <strong>Date</strong> object.
	 * 
	 * @param date
	 *            a <code>String</code> date.
	 * @return date the sql <strong>Date</strong> object.
	 */
	public static java.sql.Date convertDisplayStringToDateObject(String date) {
		return convertToDateObject(date, DISPLAY);
	}

	/**
	 * Translate <code>Calendar</code> object into a display
	 * <Code>String</code>. Format will be MM/dd/yyyy h:mm a or 12/25/2006
	 * 1:00 PM
	 */
	public static String convertCalendarToDisplay(Calendar calendar) {
		return dateTimeFormat.format(calendar.getTime());
	}

	/**
	 * Translate a date/time display <Code>String</code> into
	 * <code>Calendar</code> object. Format of date/time must be MM/dd/yyyy
	 * h:mm a or 12/25/2006 1:00 PM
	 */
	public static Calendar convertDisplayToCalendar(String dateTimeDisplay) {
		Calendar c = null;
		try {
			c = Calendar.getInstance();
			c.setTime((Date) dateTimeFormat.parseObject(dateTimeDisplay));
		} catch (ParseException ignored) {
			// ignore and return null if cannot parse object
		}

		return c;
	}

	/**
	 * Returns the int value of the abreviated month name.
	 * 
	 * @param month
	 *            a <code>String</code> month.
	 * @return int the <code>int</code> value.
	 */
	public static int getMonthInt(String month) {
		for (int i = 0; i < MONTH_NAMES.length; i++) {
			if (MONTH_NAMES[i].equalsIgnoreCase(month))
				return i + 1;
		}
		return -1;
	}// end: getMonthInt

	// ==========================================================================
	/**
	 * Modifies the representation of checkbox values for JSPage and DB table
	 * between database (Y/N) to HTML (on/offd).
	 * 
	 * @param checkbox
	 *            the value of the checkbox flag
	 * 
	 * @return 'on' if 'Y', '' if 'N', 'Y' if 'on', 'N' if 'off'
	 */
	public static String formatCheckbox(String checkbox) {
		checkbox = formatWhitespace(checkbox, "");

		if (checkbox.equalsIgnoreCase("Y"))
			return "on";
		else if (checkbox.equalsIgnoreCase("N"))
			return "";
		else if (checkbox.equalsIgnoreCase("on"))
			return "Y";
		else if (checkbox.equalsIgnoreCase("off"))
			return "N";
		else if (checkbox.equals(""))
			return "N";
		else
			return "";
	}// end: formatCheckbox

	/**
	 * formats a timestamp
	 */
	public static String formatForDisplayWithTime(Timestamp ts) {
		if (ts == null) {
			return StringUtils.EMPTY;
		}

		SimpleDateFormat newDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

		return (newDate.format(new Date(ts.getTime())));
	}

	/**
	 * formats a timestamp without the time
	 */
	public static String formatForDisplay(Timestamp ts) {
		if (ts == null) {
		    return StringUtils.EMPTY;
		}

		SimpleDateFormat newDate = new SimpleDateFormat("MM-dd-yyyy");

		return (newDate.format(new Date(ts.getTime())));
	}

	/**
	 * formats a timestamp without the time and with alpha month.
	 */
	public static String formatForDisplay2(Timestamp ts) {
		if (ts == null) {
			return "";
		}

		SimpleDateFormat newDate = new SimpleDateFormat("MMMMM dd, yyyy");

		return (newDate.format(new Date(ts.getTime())));
	}

	/**
	 * formats a timestamp without the time
	 */
	public static String formatTimestamp(Timestamp ts, String output) {
		if (ts == null) {
			// throw new IllegalArgumentException( "timestamp is null or emtpy"
			// );
			return "";
		}

		SimpleDateFormat newDate = null;
		if (output.equals(SQL)) {
			newDate = new SimpleDateFormat("yyyy-MM-dd");
		} else if (output.equals(DISPLAY)) {
			newDate = new SimpleDateFormat("MM-dd-yyyy");
		} else if (output.equals(ORACLE)) {
			newDate = new SimpleDateFormat("dd-MMM-yyyy");
		} else {
			newDate = new SimpleDateFormat("MM-dd-yyyy");
		}

		return (newDate.format(new Date(ts.getTime())));
	}

	/**
	 * formats a timestamp without the time
	 */
	public static String formatTimestamp(Timestamp ts) {
		if (ts == null) {
			// throw new IllegalArgumentException( "timestamp is null or emtpy"
			// );
			return "";
		}

		return formatTimestamp(ts, SQL);
	}

	/**
	 * converts 0,0.0,0.00 to empty string
	 * 
	 * @param value
	 * @return String
	 */
	public static String convertZeroToBlank(String value) {
		if (value == null || "0".equals(value) || "0.0".equals(value)
				|| "0.00".equals(value)) {
			return "";
		} else {
			return value;
		}
	}

	/**
	 * removes the comma in an amount field
	 */
	public static String removeComma(String commaField) {
		if (commaField == null) {
			return "";
		}

		StringTokenizer stoken;
		StringBuffer tempString = new StringBuffer();
		stoken = new StringTokenizer(commaField, ",");

		while (stoken.hasMoreTokens()) {
			tempString.append(stoken.nextToken());
		}

		return tempString.toString();
	} // removeComma

	/**
	 * removes all forward slashes from the field.
	 */
	public static String removeForwardSlash(String field) {
		if (field == null) {
			return "";
		}

		StringTokenizer stoken;
		StringBuffer tempString = new StringBuffer();
		stoken = new StringTokenizer(field, "/");

		while (stoken.hasMoreTokens()) {
			tempString.append(stoken.nextToken());
		}

		return tempString.toString();
	} // removeComma

	/**
	 * adds the suffix for any given number
	 * 
	 * For example if 1 is passed in, 1st is returned if 2 is passed in, 2nd is
	 * returned
	 * 
	 * Special cases: all numbers ending with 1 is _st, except for 11 which is
	 * 11th all numbers ending with 2 is _nd, except for 12 which is 12th all
	 * numbers ending with 3 is _rd, except for 13 which is 13th
	 * 
	 * @param number
	 * @return number + suffix
	 */
	public static String appendNumberSuffix(int number) {
		String newNumber = Integer.toString(number);

		if (newNumber.endsWith("11")) {
			newNumber = newNumber + "th";
		} else if (newNumber.endsWith("12")) {
			newNumber = newNumber + "th";
		} else if (newNumber.endsWith("13")) {
			newNumber = newNumber + "th";
		} else if (newNumber.endsWith("1")) {
			newNumber = newNumber + "st";
		} else if (newNumber.endsWith("2")) {
			newNumber = newNumber + "nd";
		} else if (newNumber.endsWith("3")) {
			newNumber = newNumber + "rd";
		} else {
			newNumber = newNumber + "th";
		}
		return newNumber;
	}

	/**
	 * Takes in a string with a specified field length. If the string is less
	 * than the field length, it fills the string with 0's up to the specified
	 * field length. Used for fields that are of type numeric.
	 * 
	 * @param s
	 *            The feature to be added to the Zeros attribute
	 * @param fieldLength
	 *            The feature to be added to the Zeros attribute
	 * @return Description of the Return Value
	 */
	public static String prefixZeros(String s, int fieldLength) {
		s = s.trim();
		int stringLength = s.length();
		int j;
		int toCount = fieldLength - 1;

		if (stringLength == 0 || s.equalsIgnoreCase("")) {
			char[] to = new char[fieldLength];
			for (j = 0; j < fieldLength; j++) {
				to[j] = '0';
			}

			// System.out.println("String "+s+" = '"+new String(to,0,j)+"' in
			// ==");
			return new String(to, 0, j);
		} else if (stringLength < fieldLength) {
			char[] from = s.trim().toCharArray();
			char[] to = new char[fieldLength];

			for (j = stringLength - 1; j > -1; j--) {
				to[toCount--] = from[j];
			}

			while (toCount != -1) {
				to[toCount--] = '0';
			}

			// System.out.println("String "+s+" = '"+new
			// String(to,0,fieldLength)+"' in <");
			return new String(to, 0, fieldLength);
		} else if (stringLength > fieldLength) {
			char[] from = s.trim().substring(0, fieldLength).toCharArray();
			char[] to = new char[fieldLength];

			for (j = fieldLength - 1; j > -1; j--) {
				to[toCount--] = from[j];
			}

			// System.out.println("String "+s+" = '"+new
			// String(to,0,fieldLength)+"' in >");
			return new String(to, 0, fieldLength);
		}

		// System.out.println("This one satisfies the field length");
		return s;
	}

	/**
	 * determines if the string passed in is a whole number
	 * 
	 * @param number
	 * @return true if whole number; false otherwise
	 */
	public static boolean isWholeNumber(String number) {
		if ("".equals(number)) {
			return false;
		}
		int decimalPosition = number.indexOf(DECIMAL);
		if (decimalPosition < 0) {
			return true;
		} else {
			int decimalValue = Integer.parseInt(number
					.substring(decimalPosition + 1));
			if (decimalValue == 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Finds the starting record number of the page index that this record is
	 * located on.
	 * 
	 * <p>
	 * For example, if there are 20 documents per page and the record we want to
	 * view is on page 5, our program needs to return the record# that is listed
	 * first on page 5, which is record# 80.
	 * 
	 * @param docsPerPage
	 * @param recordNum
	 * @return
	 */
	public static int findPageIndex(int docsPerPage, int recordNum) {
		if (docsPerPage == 0) {
			return 0;
		}
		int pageIndex = recordNum / docsPerPage;
		pageIndex *= docsPerPage;

		return pageIndex;
	}

	/**
	 * Function to test if value is a number
	 */
	public static boolean isNumeric(String field) {
		if (StringUtils.isBlank(field)) {
			return false;
		}

		try {
			Integer.parseInt(field);

		} catch (NumberFormatException pe) {
			// if exception thrown, field is not a number
			return false;
		}

		return true;
	}

	public static String convertLineBreakToHtmlBreak(String value) {
		return replaceString(value, Formatter.LINE_BREAK, Formatter.HTML_BREAK);
	}
}// eof: Formatter.java
