package com.bill.examples;

import java.util.List;

public class TrieLeaf<T extends Comparable<T>> {
	TrieLeaf<T> parent;
	List<TrieLeaf<T>> children;
	T value;

	public void print(StringBuffer printer, String prefix, boolean isTail) {
		printer.append((prefix + (isTail ? "└── " : "├── ") + value)).append("\n");
		if (null != children) {
			for (int i = 0; i < children.size() - 1; i++) {
				children.get(i).print(printer, prefix + (isTail ? "    " : "│   "), false);
			}
			if (children.size() > 0) {
				children.get(children.size() - 1).print(printer, prefix + (isTail ? "    " : "│   "), true);
			}
		}
	}
}
