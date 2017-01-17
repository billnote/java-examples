package com.bill.examples;

import java.util.HashSet;

public class HashExample {
	public static void main(String[] args) {
		HashSet<String> strSet = new HashSet<>();

		String a = new String("abcdefg");
		String b = new String("abcdefg");
		strSet.add(a);
		strSet.add(b);

		System.out.println(strSet.size());

		Long l1 = new Long(1000000l);
		long l2 = 1000000l;
		System.out.println(l1 == l2);
	}
}
