package com.bill.examples;

public class DisplacementExample {
	public static void main(String[] args) {
		System.out.println(1 << 1);

		int start = 20161001;
		int end = 20161026;
		long value = end;
		value = (value << 32) + start;
		System.out.println(value);

		value = 86591312416186967l;
		// 还原结束时间
		int cend = (int) (value >> 32);
		// 还原开始时间
		int cstart = (int) ((value << 32) >> 32);
		System.out.println(cstart);
		System.out.println(cend);

		int i = Integer.MAX_VALUE;
		System.out.println(Integer.toBinaryString(i));
		int j = Integer.MIN_VALUE;
		System.out.println(Integer.toBinaryString(j));
		int r = i - j;
		System.out.println(Integer.toBinaryString(r));
		int x = (i & j);
		System.out.println(x);
		System.out.println(Integer.toBinaryString(x));
		int rf = ((i - j) >> 31);
		System.out.println(Integer.toBinaryString(rf));
		System.out.println(String.format("i=%s,j=%s,r=%s", i, j, rf));
	}
}
