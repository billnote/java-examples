package com.bill.examples;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatExample {
	public static void main(String[] args){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd hh:mm:ss.SSS z");
		try {
			Date date = df.parse("20171010 12:12:2.0 GMT");
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
