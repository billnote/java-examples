package com.bill.examples;

import java.util.Locale;

public class SystemProtoExample {
	public static void main(String[] args) {
		System.out.println(System.getProperty( "os.name" ).toLowerCase( Locale.US ));
	}
}
