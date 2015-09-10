package org.finance.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author cahana
 */
public class GeneralUtil
{
    private static final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat df2 = new SimpleDateFormat("MM-dd-yyyy");
    private static final SimpleDateFormat df3 = new SimpleDateFormat("MM/dd/yyyy h:mm a");

    public static String printCurrentTime()
    {
        SimpleDateFormat sf = new SimpleDateFormat("mm:ss:SS");
        Calendar c = new GregorianCalendar();
        c.setTime(new Date(System.currentTimeMillis()));
        return sf.format(c.getTime());
    }

    /**
     * check if date1 is equal to or before date2 format of input dates should be mm/dd/yyyy but if
     * it's not, it will automatically convert mm-dd-yyyy to mm/dd/yyyy
     * 
     * @param list itinerary
     * @return int if equal, return 0 if date1 < date2, return -1 if date1 > date2, return 1
     */
    public static int compareDates(String date1, String date2)
    {

        Date d1 = null;
        Date d2 = null;

        // convert to Date class
        date1 = date1.replace('-', '/');
        date2 = date2.replace('-', '/');
        try {
            d1 = df.parse(date1);
            d2 = df.parse(date2);
        }
        catch (ParseException ignored) {
            // prior validation ensures that there's data in the date fields
            return -1;
        }

        // not going to check if dates are the same because they shouldn't be and it by doing so
        // it throws off the calculation
        if (d1.before(d2)) {
            return -1;
        }
        else if (d1.after(d2)) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Compare dates with time. format of input dates should be mm/dd/yyyy h:mm a
     * 
     * @param list itinerary
     * @return int if equal, return 0 if date1 < date2, return -1 if date1 > date2, return 1
     */
    public static int compareDatesWithTimes(String date1, String date2)
    {

        Date d1 = null;
        Date d2 = null;

        // convert to Date class
        date1 = date1.replace('-', '/');
        date2 = date2.replace('-', '/');
        try {
            d1 = df3.parse(date1);
            d2 = df3.parse(date2);
        }
        catch (ParseException ignored) {
            // prior validation ensures that there's data in the date fields
            return 0;
        }

        if (d1.before(d2)) {
            return -1;
        }
        else if (d1.after(d2)) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * combine two lists into one list
     * 
     * @param list1
     * @param list2
     * @return combined list
     */
    @SuppressWarnings("unchecked")
    public static List combineLists(List list1, List list2)
    {
        List newList = new ArrayList();

        if (list1 == null) {
            list1 = new ArrayList();
        }
        if (list2 == null) {
            list2 = new ArrayList();
        }

        for (int i = 0; i < list1.size(); i++) {
            newList.add(list1.get(i));
        }

        for (int i = 0; i < list2.size(); i++) {
            newList.add(list2.get(i));
        }

        return newList;
    }

    /**
     * Calculates the days from the current date.
     * 
     * <pre>
     * Takes in positive or negative days so allows for 
     * x number of days before or after today.
     * 
     * @param days
     * @return calculated date in the format MM/dd/yyyy 
     * @throws IllegalArgumentException
     */
    public static Date addDaysToToday(int days)
    {

        Calendar c1 = new GregorianCalendar();
        c1.setTime(new Date(System.currentTimeMillis()));
        c1.add(Calendar.DAY_OF_MONTH, days);

        return c1.getTime();
    }

    /**
     * Calculates a <code>Date</code> object by adding the passed number of days.
     * 
     * <pre>
     * Negative values are accepted as the days parameter
     * 
     * @param param 
     * @param days  number of days to add
     * @return new Date after &lt;param&gt;days&lt;/param&gt; are added 
     * @throws IllegalArgumentException
     */
    public static Date addDays(Date param, int days)
    {

        Calendar c1 = new GregorianCalendar();
        c1.setTime(param);
        c1.add(Calendar.DAY_OF_MONTH, days);

        return c1.getTime();
    }

    /**
     * Calculates a <code>Date</code> object by adding the passed number of minutes.
     * 
     * @param param
     * @param minutes number of minutes to add
     * @return new Date after <param>minutes</param> are added
     * @throws IllegalArgumentException
     */
    public static Date addMinutes(Date param, int minutes)
    {

        Calendar c1 = new GregorianCalendar();
        c1.setTime(param);
        c1.add(Calendar.MINUTE, minutes);

        return c1.getTime();
    }

    /**
     * Calculates a <code>Date</code> object by adding the passed number of seconds.
     * 
     * @param param
     * @param seconds number of seconds to add
     * @return new Date after <param>seconds</param> are added
     * @throws IllegalArgumentException
     */
    public static Date addSeconds(Date param, int seconds)
    {

        Calendar c1 = new GregorianCalendar();
        c1.setTime(param);
        c1.add(Calendar.SECOND, seconds);

        return c1.getTime();
    }

    /**
     * Calculates a <code>Date</code> object by adding the passed number of days.
     * 
     * <pre>
     * Negative values are accepted as the days parameter
     * 
     * @param date 
     * @param days  number of days to add; can accept negative values
     * @return new Date after &lt;param&gt;days&lt;/param&gt; are added 
     * @throws IllegalArgumentException
     */
    public static Date addDays(String date, int days)
    throws IllegalArgumentException
    {

        if (StringUtils.isBlank(date)) {
            throw new IllegalArgumentException();
        }

        Date param = null;
        try {
            param = df.parse(date);
        }
        catch (Exception e) {
            try {
                param = df2.parse(date);
            }
            catch (Exception e2) {
                throw new IllegalArgumentException();
            }
        }

        return addDays(param, days);
    }

    /**
     * Compares values.
     * <p>
     * Return value > 0 if param1 > param2, value == 0 if parm1 == param2, value < 0 if param1 <
     * param2
     * 
     * @param param1
     * @param param2
     * @return int
     */
    public static int compare(String param1, String param2)
    {

        double p1 = Double.parseDouble(Formatter.removeComma(Formatter.formatWhitespace(param1, "0")));
        double p2 = Double.parseDouble(Formatter.removeComma(Formatter.formatWhitespace(param2, "0")));

        return Double.compare(p1, p2);
    }

    /**
     * Creates a comma delimited list of <code>String</code> values for insert into a sql statement.
     * 
     * @param list values must be of type String
     * @return comma delimited list
     */
    @SuppressWarnings("unchecked")
    public static String makeCommaDelimitedStringForSql(List list)
    {
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            String element = iter.next().toString();
            if (StringUtils.isBlank(sb.toString())) {
                sb.append("'").append(element).append("'");
            }
            else {
                sb.append(",'").append(element).append("'");
            }
        }
        return sb.toString();
    }

    /**
     * Returns the number of objects that need to be created in order for the array to not throw an
     * ArrayIndexOutOfBoundsException.
     * 
     * @param index
     * @param listSize
     * @return
     */
    public static int listSizeChecker(int index, int listSize)
    {
        int createCount = (index + 1) - listSize;
        if (createCount > 0) {
            return createCount;
        }
        else {
            // the list has an object for this index
            return 0;
        }
    }

    /**
     * Checks if the string passed in is a negative number.
     * 
     * @param number
     * @return
     */
    public static boolean isNegativeNumber(String numberAsString)
    {
        if (StringUtils.isBlank(numberAsString)) {
            return false;
        }
        Double number = new Double(0D);
        try {
            number = Double.parseDouble(numberAsString);
        }
        catch (NumberFormatException e) {
            return false;
        }
        
        return number < 0D;
    }
    
    /**
     * Converts the stacktrace to a string
     * 
     * @param t
     * @return stacktrace
     */
    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    public static String getCustomStackTrace(Throwable t)
    {
        // add the class name and any message passed to constructor
        final StringBuilder result = new StringBuilder();
        result.append(t.toString());
        final String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);

        // add each element of the stack trace
        for (StackTraceElement element : t.getStackTrace()) {
            result.append(element);
            result.append(NEW_LINE);
        }
        return result.toString();
    }
    
    public static String escapeSpecialCharacters(String html) {
        html = html.replaceAll("</", "&lt;&#47;");
        html = html.replaceAll("<", "&lt;");
        html = html.replaceAll(">", "&gt;");
        return html;
    }
    
    public static boolean isZeroDollarsR(String amount)
    {
        if (StringUtils.isBlank(amount)) {
            return true;
        }

//        return amount.matches("^0*[,\\.]?0*");
        return amount.matches("^((0{1,3}(,000)*)|(0+))(.0*)?$");
    }
    
    /**
     * Checks if the string passed in is a negative number.
     * 
     * @param number
     * @return
     */
    public static boolean isZeroDollars(String amount)
    {
        if (StringUtils.isBlank(amount)) {
            return true;
        }
        Double number = new Double(0D);
        try {
            number = Double.parseDouble(amount.replaceAll(",",""));
        }
        catch (NumberFormatException e) {
            return false;
        }
        
        return number.doubleValue() == 0;
    }
    
    public static double calculateAccruedInterest(int daysBetween, double balance, double interestRate) {
    	double accruedInterest = 0D;
    	double dailyInterest = (interestRate/100)/365; 
    	if (daysBetween == 0) {
    		accruedInterest = balance * dailyInterest;
    	}
    	else {
    		accruedInterest = balance * dailyInterest * daysBetween;
    	}
    	return accruedInterest;
    }
    
    public static int calculateDaysBetween(Date younger, Date older) {
    	return Days.daysBetween(new DateTime(younger), new DateTime(older)).getDays();
    }
}