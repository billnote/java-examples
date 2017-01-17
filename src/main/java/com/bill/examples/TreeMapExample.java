package com.bill.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TreeMapExample {
	static TreeMap<Long, Integer> priceTree = new TreeMap<Long, Integer>();
	static HashMap<String, Integer> priceMap = new HashMap<String, Integer>();

	static final int DEFAULT_PRICE = 10;

	static void initPriceTree(String tree) {
		String[] nodes = tree.split(",");
		for (int i = 0; i < nodes.length; i++) {
			String node = nodes[i];
			String[] nodeEntry = node.split(":");
			long key = Long.parseLong(nodeEntry[0]);
			int price = nodeEntry.length == 2 ? Integer.parseInt(nodeEntry[1]) : 0;

			priceTree.put(key, price);
		}
	}

	static void initPriceMap(String pathsPrice) {
		String[] nodes = pathsPrice.split(",");
		for (int i = 0; i < nodes.length; i++) {
			String node = nodes[i];
			String[] nodeEntry = node.split(":");
			if (nodeEntry.length != 2) {
				continue;
			}
			String key = nodeEntry[0];
			int price = Integer.parseInt(nodeEntry[1]);

			priceMap.put(key, price);
		}
	}

	static int analyzePrice(long[] keys) {
		int firstIdx = 0;
		int lastIdx = keys.length - 1;

		Entry<Long, Integer> firstEntry = priceTree.firstEntry();
		Entry<Long, Integer> lastEntry = priceTree.lastEntry();

		long minKey = firstEntry.getKey();
		long maxKey = lastEntry.getKey();

		if (keys[lastIdx] < minKey || keys[firstIdx] > maxKey) {
			return DEFAULT_PRICE;
		} else {
			List<Entry<Long, Integer>> matchEntries = new ArrayList<>();
			for (int i = 0; i < keys.length; i++) {
				Entry<Long, Integer> entry = priceTree.floorEntry(keys[i]);
				if (entry.getKey() == keys[i]) {
					matchEntries.add(entry);
				}

				if (entry.getKey() == maxKey) {
					break;
				}
			}

			int matchSize = matchEntries.size();
			if (matchSize > 0) {
				Entry<Long, Integer> entry = matchEntries.get(0);
				int maxPrice = entry.getValue();
				StringBuilder sb = new StringBuilder(String.valueOf(entry.getKey()));
				for (int i = 1; i < matchSize; i++) {
					entry = matchEntries.get(i);
					sb.append("-").append(entry.getKey());
					Integer pathPrice = priceMap.get(sb.toString());
					if (null != pathPrice) {
						maxPrice = max(maxPrice, entry.getValue(), pathPrice);
					} else {
						maxPrice = Math.max(maxPrice, entry.getValue());
					}
				}
				return maxPrice;
			}
		}

		return DEFAULT_PRICE;
	}

	static int max(int... values) {
		return values.length > 1 ? Math.max(values[0], max(Arrays.copyOfRange(values, 1, values.length))) : values[0];

	}

	public static void main(String[] args) {
		String priceTreeStr = "100:100,101,201,400:200,401:150,500:300,501:400";
		String pathsPrice = "101-201-500:700,101-200-501:900,101-201:1000";

		initPriceTree(priceTreeStr);
		initPriceMap(pathsPrice);

		long[] keys = new long[] { 100l, 201l, 400l, 500l };
		int price = analyzePrice(keys);
		System.out.println(String.format("Price:%s", price));

		keys = new long[] { 101l, 201l, 500l };
		price = analyzePrice(keys);
		System.out.println(String.format("Price:%s", price));

		keys = new long[] { 99l };
		price = analyzePrice(keys);
		System.out.println(String.format("Price:%s", price));

		keys = new long[] { 44l, 55l, 99l };
		price = analyzePrice(keys);
		System.out.println(String.format("Price:%s", price));

		keys = new long[] { 501l };
		price = analyzePrice(keys);
		System.out.println(String.format("Price:%s", price));
	}
}
