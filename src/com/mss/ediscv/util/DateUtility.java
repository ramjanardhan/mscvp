package com.mss.ediscv.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
 * This is date Utility class. will provide instances of current time stamp and 
 * used for date format changing.  
 */

public class DateUtility {
    
    private static Logger logger = LogManager.getLogger(DateUtility.class.getName());

    private static DateUtility _instance;

    private DateUtility() {
    }

    public static DateUtility getInstance() throws ServiceLocatorException {
        try {
            if (_instance == null) {
                _instance = new DateUtility();
            }
        } catch (Exception ex) {
            throw new ServiceLocatorException(ex);
        }
        return _instance;
    }

    public String formatToDB2TS(String srcTS) {
        StringBuilder targetTS = new StringBuilder();
        int milliSec = Integer.parseInt(srcTS.substring(srcTS.indexOf(".") + 1, srcTS.length()));
        targetTS.append(srcTS.substring(6, 10));
        targetTS.append("-" + srcTS.substring(0, 5));
        targetTS.append(" " + srcTS.substring(11, srcTS.indexOf(".")));
        targetTS.append("." + (milliSec));
        return targetTS.toString();
    }

    public String formatToDB2BatchTS(String srcTS) {
        StringBuilder targetTS = new StringBuilder();
        targetTS.append(srcTS.substring(6, 10));
        targetTS.append("-" + srcTS.substring(0, 2));
        targetTS.append("-" + srcTS.substring(3, 5));
        targetTS.append(" " + srcTS.substring(11, srcTS.length()));
        return targetTS.toString();
    }

    public String formatToDB2TS1(String srcTS) {
        StringBuilder targetTS = new StringBuilder();
        int milliSec = Integer.parseInt(srcTS.substring(srcTS.indexOf(".") + 1, srcTS.length()));
        targetTS.append(srcTS.substring(6, 10));
        targetTS.append("-" + srcTS.substring(0, 2));
        targetTS.append("-" + srcTS.substring(3, 5));
        targetTS.append(" " + srcTS.substring(11, srcTS.indexOf(".")));
        targetTS.append("." + (milliSec));
        return targetTS.toString();
    }

    public String formatToCCTS(String srcTS) {
        StringBuilder targetTS = new StringBuilder();
        int microSec = Integer.parseInt(srcTS.substring(srcTS.indexOf(".") + 1, srcTS.length()));
        targetTS.append(srcTS.substring(5, 10));
        targetTS.append("-" + srcTS.substring(0, 4));
        targetTS.append(" " + srcTS.substring(11, srcTS.indexOf(".")));
        targetTS.append("." + (microSec / 1000));
        return targetTS.toString();
    }

    public String formatToDB2Date(String srcDate) {
        StringBuilder targetDate = new StringBuilder();
        targetDate.append(srcDate.substring(6, 10));
        targetDate.append("-" + srcDate.substring(0, 2));
        targetDate.append("-" + srcDate.substring(3, 5));
        return targetDate.toString();
    }

    public String formatToCCDate(String srcDate) {
        StringBuilder targetDate = new StringBuilder();
        targetDate.append(srcDate.substring(8, 10));
        targetDate.append("-" + srcDate.substring(5, 7));
        targetDate.append("-" + srcDate.substring(0, 4));
        return targetDate.toString();
    }

    public String getCurrentDB2TS() throws ServiceLocatorException {
        String stringFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";
        DateFormat myDateFormat = new SimpleDateFormat(stringFormat);
        Calendar cal = new GregorianCalendar();
        java.util.Date now = cal.getTime();
        long time = now.getTime();
        java.sql.Date date = new java.sql.Date(time);
        return myDateFormat.format(date);
    }

    public Timestamp getCurrentDB2Timestamp() throws ServiceLocatorException {
        String strFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";
        DateFormat myDateFormat = new SimpleDateFormat(strFormat);
        Calendar cal = new GregorianCalendar();
        java.util.Date now = cal.getTime();
        long time = now.getTime();
        return new Timestamp(time);
    }

    public String convertStringToMySQLDate(String dateString) {
        SimpleDateFormat sdfInput = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdfInput.parse(dateString);
        } catch (ParseException parseException) {
            LoggerUtility.log(logger, "ParseException occurred in convertStringToMySQLDate method:: " + parseException.getMessage(), Level.ERROR, parseException.getCause());
        }
        return sdfOutput.format(date);
    }

    public String DateViewToDBCompare(String viewDate) {
        System.out.println("view date is"+viewDate);
        StringTokenizer stringTokenizer = new StringTokenizer(viewDate);
        String date = stringTokenizer.nextToken();
        String time = stringTokenizer.nextToken();
        stringTokenizer = new StringTokenizer(date, "/");
        String month = stringTokenizer.nextToken();
        String date1 = stringTokenizer.nextToken();
        String year = stringTokenizer.nextToken();
        String covertedDate = year + "-" + month + "-" + date1;
        String convertedDateTime = covertedDate + " " + time + ":00.0";
        return convertedDateTime;
    }

    public String DateTo12HourceFormat(String datein24) {
        String dateString = "datein24";
        String datein12 = "";
        String str[] = dateString.split(" ");
        int hour = Integer.parseInt(str[1].substring(0, 2));
        int i = 0;
        if (hour > 12) {
            hour = hour - 12;
            i++;
        }
        String changedTime = str[1].substring(2, str[1].length());
        if (hour < 10) {
            changedTime = "0" + hour + changedTime;
        } else {
            changedTime = hour + changedTime;
        }
        if (i == 0) {
            changedTime = changedTime + " AM";
        } else {
            changedTime = changedTime + " PM";
        }
        datein12 = str[0] + " " + changedTime;
        return datein12;
    }

    public String fromDate() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date d = new Date(System.currentTimeMillis() - (2 * DAY_IN_MS));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        return sdf.format(d);
    }

    public String toDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        return sdf.format(d);
    }

    public int monthYear(String viewDate, String dateormonth) {
        StringTokenizer st = new StringTokenizer(viewDate);
        String date = st.nextToken();
        st = new StringTokenizer(date, "/");
        int month = Integer.parseInt(st.nextToken());
        int year = Integer.parseInt(st.nextToken());
        int ConvertedDateTime = 0;
        if (dateormonth.equalsIgnoreCase("month")) {
            ConvertedDateTime = month;
        }
        if (dateormonth.equalsIgnoreCase("year")) {
            ConvertedDateTime = year;
        }
        return ConvertedDateTime;
    }

    public String getCurrentMySqlDateTime1() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return simpleDateFormat.format(calendar.getTime());
    }

    public ArrayList getdailyHours() {
        Date date = new Date();
        String stringDateFormat = "HH:MM:SS";
        DateFormat dateFormat = new SimpleDateFormat(stringDateFormat);
        String formattedDate = dateFormat.format(date);
        int hrs1 = Integer.parseInt(formattedDate.substring(0, 2));
        ArrayList al = new ArrayList();
        for (int i = 0; i <= hrs1; i++) {
            al.add(i + ":00");
        }
        return al;

    }

    public ArrayList getDaysInMonth() {
        ArrayList days = new ArrayList();
        Date d = new Date();
        String str = d.toString().substring(4, 10);
        String str1[] = str.split(" ");
        String month = str1[0];
        int date = Integer.parseInt(str1[1]);
        for (int i = date; i > 0; i--) {
            days.add(month + " " + i);
        }
        Collections.reverse(days);
        return days;
    }

    public ArrayList LastXHoursYMinutesInterval() {
        int m = Integer.parseInt(com.mss.ediscv.util.Properties.getProperty("TimeInterval"));
        Date dateObj = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);
        int inputHours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int min = 0;
        int interval = 0;
        if (m == 15) {
            min = getNear15Minute(minutes);
            if (inputHours == 0) {
                if (minutes < 15) {
                    interval = 0;
                } else if ((minutes > 15) && (minutes < 30)) {
                    interval = 1;
                } else if ((minutes > 30) && (minutes < 45)) {
                    interval = 2;
                } else if ((minutes > 45) && (minutes < 60)) {
                    interval = 3;
                }
            } else if (inputHours == 1) {
                if (minutes < 15) {
                    interval = 5;
                } else if ((minutes > 15) && (minutes < 30)) {
                    interval = 6;
                } else if ((minutes > 30) && (minutes < 45)) {
                    interval = 7;
                } else if ((minutes > 45) && (minutes < 60)) {
                    interval = 8;
                }
            } else {
                interval = 9;
            }
        } else if (m == 30) {
            min = getNear30Minute(minutes);
            if (inputHours == 0) {
                if (minutes < 30) {
                    interval = 0;
                } else if ((minutes > 30) && (minutes < 60)) {
                    interval = 1;
                }
            }else if (inputHours == 1) {
                if (minutes < 30) {
                    interval = 2;
                } else if ((minutes > 30) && (minutes < 60)) {
                    interval = 3;
                }
            }  else {
                interval = 4;
            }
        }
        ArrayList a = new ArrayList();
        for (int i = interval; i > 0; i--) {
            if (min == 0) {
                min = 60;
                --inputHours;
            }
            if (min == 60) {
                a.add((inputHours + 1) + ":00");
            } else {
                a.add(inputHours + ":" + min);
            }
            if (m == 15) {
                min -= 15;
            } else if (m == 30) {
                min -= 30;
            }
        }
        if (inputHours == 0) {
            a.add("00:00");
        }
        Collections.reverse(a);
        return a;
    }


    public int getNear15Minute(int x) {
        int res = 0;
        if (x < 15) {
            res = 0;
        } else if ((x > 15) && (x < 30)) {
            res = 15;
        } else if ((x > 30) && (x < 45)) {
            res = 30;
        } else if ((x > 45) && (x < 60)) {
            res = 45;
        } else {
            res = 0;
        }
        return (res);
    }

    public int getNear30Minute(int x) {
        int res = 0;
        if (x < 30) {
            res = 0;
        } else if ((x > 30) && (x < 60)) {
            res = 30;
        } else {
            res = 0;
        }
        return (res);
    }

    public List getYearlyList() {
        List yearList = new ArrayList();
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int min = 2000;
        for (int i = min; i <= year; i++) {
            yearList.add(i);
        }
        Collections.reverse(yearList);
        return yearList;
    }

    public ArrayList XaxisYearofMonth(String year) {
        System.out.println("Entered in xaxisyearof month method");
        ArrayList ConvertedMonth = new ArrayList();
        Calendar calender = Calendar.getInstance();
            System.out.println("Calendar.YEAR:"+Calendar.YEAR);
        int currYear = calender.get(Calendar.YEAR);
         System.out.println("year:"+currYear);
        if (Integer.parseInt(year) == currYear) {
            System.out.println("Calendar.MONTH:"+Calendar.MONTH);
            int month = calender.get(Calendar.MONTH);
            System.out.println("month:"+month);
            for (int i = 0; i <= month; i++) {
                ConvertedMonth.add(Month.of(i + 1).name().substring(0, 3));
            }
        } else if (Integer.parseInt(year) < currYear) {
            System.out.println("year:"+year);
            for (int i = 1; i <= 12; i++) {
                System.out.println("i value is"+i);
               String s=Month.of(i).name();
               ConvertedMonth.add(Month.of(i).name().substring(0, 3));
                System.out.println("after ConvertedMonth");
            }
        }
        return ConvertedMonth;
    }

    public ArrayList XaxisMonthofDays(String year, String month) {
        int daysInMonth;
        ArrayList convertedDays = new ArrayList();
        Calendar calender = Calendar.getInstance();
        int curentYear = calender.get(Calendar.YEAR);
        int curentMonth = calender.get(Calendar.MONTH);
        if (Integer.parseInt(year) == curentYear) {
            if (Integer.parseInt(month) == (curentMonth + 1)) {
                int date = calender.get(Calendar.DATE);
                for (int i = 1; i <= date; i++) {
                //    convertedDays.add(Month.of(curentMonth + 1).name().substring(0, 3) + " " + (i));
                }
            } else {
                int month2 = Integer.parseInt(month);
                if (month2 == 4 || month2 == 6 || month2 == 9 || month2 == 11) {
                    daysInMonth = 30;
                    for (int i = 1; i <= daysInMonth; i++) {
                     //   convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                    }
                } else if (month2 == 2) {
                    if (Integer.parseInt(year) % 4 == 0) {
                        daysInMonth = 29;
                        for (int i = 1; i <= daysInMonth; i++) {
                       //     convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                        }
                    } else {
                        daysInMonth = 28;
                        for (int i = 1; i <= daysInMonth; i++) {
                       //     convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                        }
                    }
                } else {
                    daysInMonth = 31;
                    for (int i = 1; i <= daysInMonth; i++) {
                    //    convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                    }
                }
            }
        } else if (Integer.parseInt(year) < curentYear) {
            int month2 = Integer.parseInt(month);
            if (month2 == 4 || month2 == 6 || month2 == 9 || month2 == 11) {
                daysInMonth = 30;
                for (int i = 1; i <= daysInMonth; i++) {
                //    convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                }
            } else if (month2 == 2) {
                if (Integer.parseInt(year) % 4 == 0) {
                    daysInMonth = 29;
                    for (int i = 1; i <= daysInMonth; i++) {
                  //      convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                    }
                } else {
                    daysInMonth = 28;
                    for (int i = 1; i <= daysInMonth; i++) {
                   //     convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                    }
                }
            } else {
                daysInMonth = 31;
                for (int i = 1; i <= daysInMonth; i++) {
                  //  convertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                }
            }
        }
        return convertedDays;
    }

    public List getYearofMonth(String year) {
        List ConvertedYear = new ArrayList();
        Calendar calender = Calendar.getInstance();
        int currYear = calender.get(Calendar.YEAR);
        if (Integer.parseInt(year) == currYear) {
            int month = calender.get(Calendar.MONTH);
            for (int i = 0; i <= month; i++) {
                ConvertedYear.add((i + 1));
            }
        } else if (Integer.parseInt(year) < currYear) {
            for (int i = 1; i <= 12; i++) {
                ConvertedYear.add(i);
            }
        }
        return ConvertedYear;
    }

    public List getMonthofDate(String year, String month) {
        int daysInMonth;
        List ConvertedDays = new ArrayList();
        Calendar calender = Calendar.getInstance();
        int currYear = calender.get(Calendar.YEAR);
        int currMonth = calender.get(Calendar.MONTH);
        if (Integer.parseInt(year) == currYear) {
            if (Integer.parseInt(month) == (currMonth + 1)) {
                int date = calender.get(Calendar.DATE);
                for (int i = 1; i <= date; i++) {
                    ConvertedDays.add((i));
                }
            } else {
                int month2 = Integer.parseInt(month);
                if (month2 == 4 || month2 == 6 || month2 == 9 || month2 == 11) {
                    daysInMonth = 30;
                    for (int i = 1; i <= daysInMonth; i++) {
                        ConvertedDays.add((i));
                    }
                } else if (month2 == 2) {
                    if (Integer.parseInt(year) % 4 == 0) {
                        daysInMonth = 29;
                        for (int i = 1; i <= daysInMonth; i++) {
                     //       ConvertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                        }
                    } else {
                        daysInMonth = 28;
                        for (int i = 1; i <= daysInMonth; i++) {
                     //       ConvertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                        }
                    }
                } else {
                    daysInMonth = 31;
                    for (int i = 1; i <= daysInMonth; i++) {
                        ConvertedDays.add((i));
                    }
                }
            }
        } else if (Integer.parseInt(year) < currYear) {
            int month2 = Integer.parseInt(month);
            if (month2 == 4 || month2 == 6 || month2 == 9 || month2 == 11) {
                daysInMonth = 30;
                for (int i = 1; i <= daysInMonth; i++) {
                    ConvertedDays.add((i));
                }
            } else if (month2 == 2) {
                if (Integer.parseInt(year) % 4 == 0) {
                    daysInMonth = 29;
                    for (int i = 1; i <= daysInMonth; i++) {
                 //       ConvertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                    }
                } else {
                    daysInMonth = 28;
                    for (int i = 1; i <= daysInMonth; i++) {
                 //       ConvertedDays.add(Month.of(month2).name().subSequence(0, 3) + " " + (i));
                    }
                }
            } else {
                daysInMonth = 31;
                for (int i = 1; i <= daysInMonth; i++) {
                    ConvertedDays.add((i));
                }
            }
        }
        return ConvertedDays;
    }

}
