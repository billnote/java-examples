package com.bill.examples;

public class EnumStaticFieldExample {
	public static void main(String[] args) {
		System.out.println("Test enum static1: " + EnumStatic.Test);
		System.out.println();
		// throw NullPointerException
		System.out.println("Test enum static2: " + EnumStatic.find("test"));
	}
}

