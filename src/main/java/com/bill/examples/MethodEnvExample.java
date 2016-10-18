package com.bill.examples;

class Env {
	public int id;
}

public class MethodEnvExample {
	static int i;
	public static void  main(String[] args) {
		Long id = 1l;
		Env env = new Env();
		env.id = 1;
		
		testEnv(env, id);
		System.out.println("env: " + env.id + "; id:" + id);
		
		
		testOjb(i);
	}
	
	
	private static void testOjb(Object... values) {
		StringBuilder sb = new StringBuilder();
		for (Object object : values) {
			sb.append(object).append("|");
		}
		
		System.out.println(sb);
	}
	
	private static void testEnv(Env env, Long id) {
		env = new Env();
		id = 2l;
		env.id = 2;
	}
}
