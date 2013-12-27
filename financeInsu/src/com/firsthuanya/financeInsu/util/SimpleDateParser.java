package com.firsthuanya.financeInsu.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateParser {
	private String dateString;
	
	public SimpleDateParser(String dateStr){
		if(dateStr == null) 
			throw new IllegalArgumentException("");
		
		this.dateString = dateStr.trim();
	}
	
	public boolean isValid(){
		//yyyy-MM-dd HH:mm:ss
		//yyyy/MM/dd HH:mm:ss
		//yyyy.MM.dd HH:mm:ss		
		String[] dateTime = this.dateString.split(" ");
		if(dateTime.length == 1)
			return isValidDatePart(dateTime[0]);
		else
			return isValidDatePart(dateTime[0]) && isValidTimePart(dateTime[dateTime.length - 1]);		
	}
	
	private boolean isValidDatePart(String datePart){
		String[] date = splitDatePart(datePart);
		
		if(date.length != 3)
			return false;
		
		if(!date[0].matches("^[0-9]{1,4}$")) return false;
		if(!date[1].matches("^[0-9]{1,2}$")) return false;
		if(!date[2].matches("^[0-9]{1,2}$")) return false;
		
		int y = Integer.parseInt(date[0]);
		int m = Integer.parseInt(date[1]);
		int d = Integer.parseInt(date[2]);
		
		if(y < 2000) return false;
		if(m < 1 || m > 12) return false;
//		if(d < 1 || d > 31) return false;
		
		if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12)
			return d >= 1 && d <= 31;
		if(m == 4 || m == 6 || m == 9 || m == 11)
			return d >= 1 && d <= 30;
		if(m == 2 && (y % 4 == 0))
			return  d >= 1 && d <= 29;
		if(m == 2 && (y % 4 != 0))
			return  d >= 1 && d <= 28;
			
		return false;
	}
	
	private boolean isValidTimePart(String timePart){
		String[] time = timePart.trim().split(":");
		
		if(time.length != 3)
			return false;
		
		if(!time[0].matches("^[0-9]{1,2}$")) return false;
		if(!time[1].matches("^[0-9]{1,2}$")) return false;
		if(!time[2].matches("^[0-9]{1,2}$")) return false;
		
		int H = Integer.parseInt(time[0]);
		int m = Integer.parseInt(time[1]);
		int s = Integer.parseInt(time[2]);
		
		if(H < 0 || H > 23)
			return false;
		if(m < 0 || m > 59)
			return false;
		if(s < 0 || s > 59)
			return false;
		
		return true;		
	}
	
	private String[] splitDatePart(String datePart){
		String[] date = datePart.trim().split("-");
		if(date.length == 3)
			return date;
		date = datePart.trim().split("/");
		if(date.length == 3)
			return date;
		date = datePart.trim().split("\\.");
		return date;
	}
	
	public Date parse(){
		String[] dateTime = this.dateString.split(" ");
		String pattern = null;
		if(dateTime.length == 1){
			if(dateTime[0].contains("-"))
				pattern = "yyyy-MM-dd";
			if(dateTime[0].contains("."))
				pattern = "yyyy.MM.dd";
			if(dateTime[0].contains("/"))
				pattern = "yyyy/MM/dd";
		}
		
		if(dateTime.length > 1){
			if(dateTime[0].contains("-"))
				pattern = "yyyy-MM-dd HH:mm:ss";
			if(dateTime[0].contains("."))
				pattern = "yyyy.MM.dd HH:mm:ss";
			if(dateTime[0].contains("/"))
				pattern = "yyyy/MM/dd HH:mm:ss";
		}
		
		DateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(this.dateString);
		} catch (ParseException e) {
			IllegalStateException exception = new IllegalStateException("Couldn't parse the dateString: " + this.dateString,e);
			throw exception;
		}
	}
	
	public static void main(String[] args){
		SimpleDateParser parser = new SimpleDateParser("2012.02.29 15:04:1");
		if(parser.isValid())
			System.out.println(parser.parse());
		else
			System.out.println("日期格式错误");
		
//		SimpleDateParser parser = new SimpleDateParser("2013/11/3 1:5:09");
//		System.out.println(parser.parse());
		
//		System.out.println(("2013.3.29").split(".").length);
//		System.out.println(SimpleDateParser.isValidTimePart("23:1:33"));
		
//		String[] str = "".split(" ");
//		System.out.println(str.length);
//		System.out.println(str[0]);
//		System.out.println(str[1]);

		
	}
}
