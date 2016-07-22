package com.bill.examples;

class Env {
	public int id;
}

public class MethodEnvExample {
	public static void  main(String[] args) {
		Long id = 1l;
		Env env = new Env();
		env.id = 1;
		
		testEnv(env, id);
		System.out.println("env: " + env.id + "; id:" + id);
	}
	
	private static void testEnv(Env env, Long id) {
		env = new Env();
		id = 2l;
		env.id = 2;
	}
}
