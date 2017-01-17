package com.bill.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie<T extends Comparable<T>> {
	public TrieLeaf<T> root = new TrieLeaf<>();

	public Trie() {
		root.parent = null;
	}

	// 使用一个二维数组表示树的结构
	public TrieLeaf<T> createTrie(List<T>[] leafs) {
		for (int i = 0; i < leafs.length; i++) {
			TrieLeaf<T> parent = root;
			List<T> values = leafs[i];
			int size = values.size();
			for (int j = 0; j < size; j++) {
				if (null == parent.children) {
					parent.children = new ArrayList<>();
					TrieLeaf<T> leaf = new TrieLeaf<>();
					leaf.value = values.get(j);
					leaf.parent = parent;
					parent.children.add(leaf);
					parent = leaf;
					continue;
				}
				List<TrieLeaf<T>> children = parent.children;

				int chdSize = children.size();
				for (int n = 0; n < chdSize; n++) {
					TrieLeaf<T> child = children.get(n);
					int compare = children.get(n).value.compareTo(values.get(j));
					if (compare < 0) {
						if (n == chdSize - 1) {
							TrieLeaf<T> leaf = new TrieLeaf<>();
							leaf.value = values.get(j);
							leaf.parent = parent;
							children.add(leaf);
							parent = leaf;
						}
						continue;
					} else if (compare > 0) {
						TrieLeaf<T> leaf = new TrieLeaf<>();
						leaf.value = values.get(j);
						leaf.parent = parent;
						children.add(n, leaf);
						parent = leaf;
						break;
					} else {
						parent = child;
						break;
					}

				}
			}
		}

		return root;
	}

	public void print() {
		StringBuffer printer = new StringBuffer();
		root.print(printer, "", true);
		System.out.println(printer);
	}

	private TrieLeaf<T> search(TrieLeaf<T> leaf, T[] keys, int level) {
		if (null != leaf) {
			List<TrieLeaf<T>> children = leaf.children;
			int size = children.size();

			if (null != children && size > 0) {
				if (children.get(0).value.compareTo(keys[level]) > 0
						|| children.get(size - 1).value.compareTo(keys[level]) < 0) {
					return null;
				}
				for (int i = 0; i < size; i++) {
					int compare = children.get(i).value.compareTo(keys[level]);
					if (compare < 0) {
						continue;
					} else if (compare == 0) {
						return ++level == keys.length ? children.get(i) : search(children.get(i), keys, level);
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	public TrieLeaf<T> search(T[] keys) {
		return search(root, keys, 0);
	}

	public static void main(String[] args) {
		List<Integer>[] leafs = new List[9];
		leafs[0] = Arrays.asList(1, 4, 5, 7, 9);
		leafs[1] = Arrays.asList(1, 5, 7, 9);
		leafs[2] = Arrays.asList(4, 5);
		leafs[3] = Arrays.asList(4, 7, 8);
		leafs[4] = Arrays.asList(4, 5, 7);
		leafs[5] = Arrays.asList(3, 5, 8, 11);
		leafs[6] = Arrays.asList(3, 5, 8, 12);
		leafs[7] = Arrays.asList(3, 5, 8, 13);
		leafs[8] = Arrays.asList(2, 4, 9);

		List<Integer[]> trieKeys = new ArrayList<>();
		trieKeys.add(new Integer[] { 1, 4, 5, 7, 9 });
		trieKeys.add(new Integer[] { 1, 5, 7, 9 });
		trieKeys.add(new Integer[] { 4, 5 });
		trieKeys.add(new Integer[] { 4, 7, 8 });
		trieKeys.add(new Integer[] { 4, 5, 7 });
		trieKeys.add(new Integer[] { 3, 5, 8, 11 });
		trieKeys.add(new Integer[] { 3, 5, 8, 12 });
		trieKeys.add(new Integer[] { 3, 5, 8, 13 });
		trieKeys.add(new Integer[] { 2, 4, 9 });

		Map<String, Integer> findMap = new HashMap<String, Integer>();
		findMap.put("1, 4, 5, 7, 9", 9);
		findMap.put("1, 5, 7, 9", 9);
		findMap.put("4, 5", 5);
		findMap.put("4, 7, 8", 8);
		findMap.put("4, 5, 7", 7);
		findMap.put("3, 5, 8, 11", 11);
		findMap.put("3, 5, 8, 12", 12);
		findMap.put("3, 5, 8, 13", 13);
		findMap.put("2, 4, 9", 9);
		List<String> mapKeys = new ArrayList<>(findMap.keySet());

		System.out.println("create trie");
		Trie<Integer> trie = new Trie<>();
		trie.createTrie(leafs);

		trie.print();

		TrieLeaf<Integer> leaf = trie.search(new Integer[] { 1, 4, 5, 7, 9 });
		assert (null != leaf);
		System.out.println(leaf.value);

		leaf = trie.search(new Integer[] { 1, 4, 5, 7, 8 });
		assert (null == leaf);
		System.out.println("leaf is null");

		leaf = trie.search(new Integer[] { 1, 4, 5 });
		assert (null != leaf);
		System.out.println(leaf.value);

		leaf = trie.search(new Integer[] { 4, 5 });
		assert (null != leaf);
		System.out.println(leaf.value);

		leaf = trie.search(new Integer[] { 3, 5, 8 });
		assert (null != leaf);
		System.out.println(leaf.value);

		leaf = trie.search(new Integer[] { 3, 5, 8, 11 });
		assert (null != leaf);
		System.out.println(leaf.value);

		leaf = trie.search(new Integer[] { 3, 5, 8, 15 });
		assert (null == leaf);
		System.out.println("leaf is null");

		leaf = trie.search(new Integer[] { 2, 4, 9 });
		assert (null != leaf);
		System.out.println(leaf.value);

		leaf = trie.search(new Integer[] { 2, 4, 15 });
		assert (null == leaf);
		System.out.println("leaf is null");

		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			int idx = i % 9;
			Integer[] key = trieKeys.get(idx);
			trie.search(key);
			trie.search(new Integer[]{1, 3, 5, 7});
		}
		long end = System.currentTimeMillis();
		System.out.println("trie time:" + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			int idx = i % 9;
			String key = mapKeys.get(idx);
			findMap.get(key);
			findMap.get("1, 3, 5, 7");
		}
		end = System.currentTimeMillis();
		System.out.println("trie time:" + (end - start));
	}
}
